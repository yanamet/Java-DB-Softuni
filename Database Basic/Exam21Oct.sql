-- 1
CREATE TABLE `planets`(
`id` INT PRIMARY KEY AUTO_INCREMENT,
`name` VARCHAR(30) NOT NULL
);

CREATE TABLE `spaceports`(
`id` INT PRIMARY KEY AUTO_INCREMENT,
`name` VARCHAR(50) NOT NULL,
`planet_id` INT,
CONSTRAINT fk_spaceports_planet
FOREIGN KEY (`planet_id`)
REFERENCES `planets`(`id`)
);

CREATE TABLE `spaceships`(
`id` INT PRIMARY KEY AUTO_INCREMENT,
`name` VARCHAR(50) NOT NULL,
`manufacturer` VARCHAR(30) NOT NULL,
`light_speed_rate` INT DEFAULT 0
);

CREATE TABLE `colonists`(
`id` INT PRIMARY KEY AUTO_INCREMENT,
`first_name` VARCHAR(20) NOT NULL,
`last_name` VARCHAR(20) NOT NULL,
`ucn` CHAR(10) NOT NULL UNIQUE,
`birth_date` DATE NOT NULL
);

CREATE TABLE `journeys`(
`id` INT PRIMARY KEY AUTO_INCREMENT,
`journey_start` DATETIME NOT NULL,
`journey_end` DATETIME NOT NULL,
`purpose` ENUM('Medical', 'Technical', 'Educational', 'Military'),
`destination_spaceport_id` INT,
`spaceship_id` INT,
CONSTRAINT fk_journey_spaceports
FOREIGN KEY (`destination_spaceport_id`)
REFERENCES `spaceports`(`id`),
CONSTRAINT fk_journey_spaceship
FOREIGN KEY (`spaceship_id`)
REFERENCES `spaceships`(`id`)
);

CREATE TABLE `travel_cards`(
`id` INT PRIMARY KEY AUTO_INCREMENT,
`card_number` CHAR(10) NOT NULL UNIQUE,
`job_during_journey` ENUM('Pilot', 'Engineer', 'Trooper', 'Cleaner', 'Cook') NOT NULL,
`colonist_id` INT,
`journey_id` INT,
CONSTRAINT fk_cards_colonist
FOREIGN KEY (`colonist_id`)
REFERENCES `colonists`(`id`),
CONSTRAINT fk_cards_journey
FOREIGN KEY (`journey_id`)
REFERENCES `journeys`(`id`)
);

-- 2
INSERT INTO travel_cards(card_number, job_during_journey, colonist_id, journey_id)
SELECT 
IF(timestampdiff(YEAR, '1980-01-01', c.birth_date) > 0, 
CONCAT(YEAR(c.birth_date), DAY(c.birth_date), LEFT(c.ucn, 4)), 
CONCAT(YEAR(c.birth_date), MONTH(c.birth_date), RIGHT(c.ucn, 4))) AS 'card_num',
CASE
WHEN c.id % 2 = 0 THEN 'Pilot'
WHEN c.id % 3 = 0 THEN 'Cook'
ELSE 'Engineer'
END AS 'job',
c.id,
LEFT(c.ucn, 1) AS 'id'
FROM colonists AS c
WHERE c.id BETWEEN 96 AND 100
;

-- 3
UPDATE journeys 
SET purpose = (CASE
WHEN id % 2 = 0 THEN 'Medical'
WHEN id % 3 = 0 THEN 'Technical'
WHEN id % 5 = 0 THEN 'Educational'
WHEN id % 7 = 0 THEN 'Military'
ELSE purpose
END );

-- 4
DELETE c FROM colonists AS c
LEFT JOIN travel_cards AS tc ON tc.colonist_id = c.id
WHERE tc.journey_id IS NULL;

-- 5
SELECT card_number, job_during_journey FROM travel_cards
ORDER BY card_number;

-- 6
SELECT id, CONCAT_WS(' ', first_name, last_name) AS full_name, ucn 
FROM colonists
ORDER BY first_name, last_name, id;

-- 7
SELECT id, journey_start, journey_end FROM journeys
WHERE purpose = 'Military'
ORDER BY journey_start;

-- 8
SELECT c.id, 	CONCAT_WS(' ', first_name, last_name)full_name FROM colonists AS c
JOIN travel_cards AS tc ON tc.colonist_id = c.id
WHERE tc.job_during_journey = 'Pilot'
ORDER BY c.id;

-- 9
SELECT COUNT(c.id) AS count FROM colonists AS c
JOIN travel_cards AS tc ON tc.colonist_id = c.id
JOIN journeys AS j ON tc.journey_id = j.id
where j.purpose = 'Technical';

-- 10
SELECT ss.`name`, sp.`name` FROM spaceships AS ss
JOIN journeys AS j ON j.spaceship_id = ss.id
JOIN spaceports AS sp ON j.destination_spaceport_id = sp.id
ORDER BY light_speed_rate DESC
LIMIT 1;

-- 11
SELECT ss.`name`, ss.`manufacturer` FROM spaceships AS ss
JOIN journeys AS j ON j.spaceship_id = ss.id
JOIN travel_cards AS tc ON tc.journey_id = j.id
JOIN colonists AS c ON tc.colonist_id = c.id
WHERE job_during_journey = 'Pilot' AND timestampdiff(YEAR, c.birth_date, '2019-01-01') < 30
ORDER BY ss.`name`;

-- 12
SELECT p.`name`,	sp.`name` FROM planets AS p
JOIN spaceports AS sp ON sp.planet_id = p.id
JOIN journeys AS j ON j.destination_spaceport_id = sp.id
WHERE j.purpose = 'Educational'
ORDER BY sp.`name` DESC;

-- 13
SELECT p.`name`, COUNT(DISTINCT j.id) AS 'count_journeys' FROM planets AS p
JOIN spaceports AS sp ON sp.planet_id = p.id
JOIN journeys AS j ON j.destination_spaceport_id = sp.id
GROUP BY p.`name`
ORDER BY count_journeys DESC, p.`name` ASC;

-- 14
SELECT j.id,	p.name,	sp.name,	j.purpose FROM journeys AS j
JOIN spaceports AS sp ON j.destination_spaceport_id = sp.id
JOIN planets AS p ON sp.planet_id = p.id
 ORDER BY timestampdiff(YEAR, journey_start, journey_end)
 LIMIT 1;
 
 -- 15
 SELECT job_during_journey FROM travel_cards AS tc
JOIN (SELECT * FROM journeys 
ORDER BY datediff(journey_end, journey_start) DESC
LIMIT 1) AS jj
WHERE tc.journey_id = jj.id
GROUP BY job_during_journey
ORDER BY COUNT(*)
LIMIT 1
;

-- 16
DELIMITER :
CREATE FUNCTION  udf_count_colonists_by_destination_planet (planet_name VARCHAR (30)) 
RETURNS INT
DETERMINISTIC
BEGIN
RETURN (SELECT COUNT(DISTINCT c.id) FROM planets AS p
JOIN spaceports AS sp ON sp.planet_id = p.id
JOIN journeys AS j ON j.destination_spaceport_id = sp.id
JOIN travel_cards AS tc ON tc.journey_id = j.id
JOIN colonists AS c ON tc.colonist_id = c.id
WHERE p.`name` = planet_name);
END
:

-- 17
CREATE PROCEDURE udp_modify_spaceship_light_speed_rate
(spaceship_name VARCHAR(50), light_speed_rate_increse INT(11)) 
BEGIN 
IF(SELECT COUNT(*) FROM spaceships AS sp WHERE sp.`name` = spaceship_name > 0) THEN
UPDATE spaceships AS sp 
SET sp.light_speed_rate = sp.light_speed_rate + light_speed_rate_increse
WHERE sp.`name` = spaceship_name;
ELSE 
 SIGNAL SQLSTATE '42927' 
 SET MESSAGE_TEXT = 'Spaceship you are trying to modify does not exists.';
END IF;
END
: