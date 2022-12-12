-- 1
CREATE TABLE `countries`(
`id` INT PRIMARY KEY AUTO_INCREMENT,
`name` VARCHAR(45) NOT NULL
);

CREATE TABLE `towns`(
`id` INT PRIMARY KEY AUTO_INCREMENT,
`name` VARCHAR(45) NOT NULL,
`country_id` INT NOT NULL,
CONSTRAINT fk_town_country
FOREIGN KEY (`country_id`)
REFERENCES `countries`(`id`)
);

CREATE TABLE `stadiums`(
`id` INT PRIMARY KEY AUTO_INCREMENT,
`name` VARCHAR(45) NOT NULL,
`capacity` INT NOT NULL,
`town_id` INT NOT NULL,
CONSTRAINT fk_stadium_town
FOREIGN KEY (`town_id`)
REFERENCES `towns`(`id`)
);

CREATE TABLE `teams`(
`id` INT PRIMARY KEY AUTO_INCREMENT,
`name` VARCHAR(45) NOT NULL,
`established` DATE NOT NULL,
`fan_base` BIGINT(20) NOT NULL DEFAULT 0,
`stadium_id` INT NOT NULL,
CONSTRAINT fk_team_stadium
FOREIGN KEY (`stadium_id`)
REFERENCES `stadiums`(`id`)
);

CREATE TABLE `skills_data`(
`id` INT PRIMARY KEY AUTO_INCREMENT,
`dribbling` INT DEFAULT 0,
`pace` INT DEFAULT 0,
`passing` INT DEFAULT 0,
`shooting` INT DEFAULT 0,
`speed` INT DEFAULT 0,
`strength` INT DEFAULT 0
);

CREATE TABLE `coaches`(
`id` INT PRIMARY KEY AUTO_INCREMENT,
`first_name` VARCHAR(10) NOT NULL,
`last_name` VARCHAR(20) NOT NULL,
`salary` DECIMAL(10, 2) NOT NULL DEFAULT 0,
`coach_level` INT NOT NULL DEFAULT 0
);

CREATE TABLE `players`(
`id` INT PRIMARY KEY AUTO_INCREMENT,
`first_name` VARCHAR(10) NOT NULL,
`last_name` VARCHAR(20) NOT NULL,
`age` INT NOT NULL DEFAULT 0,
`position` CHAR(1) NOT NULL,
`salary` DECIMAL(10, 2) NOT NULL DEFAULT 0,
`hire_date` DATETIME,
`skills_data_id` INT NOT NULL,
`team_id` INT,
CONSTRAINT fk_player_skill
FOREIGN KEY (`skills_data_id`)
REFERENCES `skills_data`(`id`),
CONSTRAINT fk_player_team
FOREIGN KEY (`team_id`)
REFERENCES `teams`(`id`)
);

CREATE TABLE `players_coaches`(
`player_id` INT,
`coach_id` INT,
KEY k_player_coach(`player_id`, `coach_id`),
CONSTRAINT fk_players_playerID
FOREIGN KEY (`player_id`)
REFERENCES `players`(`id`),
CONSTRAINT fk_coaches_coachID
FOREIGN KEY (`coach_id`)
REFERENCES `coaches`(`id`)
);

-- 2
INSERT INTO coaches (first_name, last_name, salary, coach_level)
SELECT first_name, last_name, (salary * 2), CHAR_LENGTH(first_name) FROM players
WHERE age >= 45;

-- 3
UPDATE coaches AS c
JOIN players_coaches AS pc ON c.id = pc.coach_id
SET c.coach_level = c.coach_level + 1
where c.first_name LIKE 'A%';

-- 4
DELETE p FROM players AS p
WHERE p.age >= 45;

-- 5
SELECT first_name, age, salary FROM players
ORDER  BY salary DESC;

-- 6
SELECT p.id, CONCAT(first_name, ' ', last_name) AS full_name, age, position, hire_date 
FROM players AS p
JOIN skills_data AS sd ON sd.id = p.skills_data_id
WHERE age < 23 AND position = 'A' AND hire_date IS NULL AND sd.strength > 50
ORDER  BY salary ASC, age;

-- 7
SELECT t.`name`, t.established,	t.fan_base, COUNT(p.id) AS 'player_count' FROM teams AS t
LEFT JOIN players AS p ON p.team_id = t.id
GROUP BY t.id
ORDER BY player_count DESC, fan_base DESC;

-- 8
SELECT MAX(sd.speed) AS 'max_speed', towns.name FROM players AS p
JOIN skills_data AS sd ON sd.id = p.skills_data_id
right JOIN teams AS t ON p.team_id = t.id
JOIN stadiums AS s ON t.stadium_id = s.id
JOIN towns ON s.town_id = towns.id
WHERE t.name != 'Devify'
GROUP BY towns.name
ORDER BY max_speed DESC, towns.name;

-- 9
SELECT c.name, COUNT(DISTINCT p.id) AS 'count_players', SUM(p.salary) FROM players AS p
right JOIN teams AS t ON p.team_id = t.id
JOIN stadiums AS s ON t.stadium_id = s.id
JOIN towns ON s.town_id = towns.id
RIGHT JOIN countries AS c ON c.id = towns.country_id
GROUP BY c.id
ORDER BY count_players DESC, c.name ASC;

-- 10
DELIMITER :
CREATE FUNCTION udf_stadium_players_count3 (stadium_name VARCHAR(30)) 
RETURNS INT
DETERMINISTIC
BEGIN
RETURN (SELECT COUNT(p.id) FROM players AS p
JOIN teams AS t ON p.team_id = t.id
RIGHT JOIN stadiums AS s ON t.stadium_id = s.id
where  s.name = stadium_name
GROUP BY s.id
);
END
:

-- 12
CREATE PROCEDURE udp_find_playmaker (min_dribble_points INT, team_name VARCHAR(45))
BEGIN
SELECT CONCAT(first_name, ' ', last_name) AS full_name	,age,	salary	,dribbling	,speed	,t.name FROM players AS p
JOIN skills_data AS sd ON p.skills_data_id = sd.id
JOIN teams AS t ON p.team_id = t.id
WHERE t.name =  team_name AND sd.dribbling > min_dribble_points and sd.speed >  (SELECT AVG(sd.speed) FROM players AS p
JOIN skills_data AS sd ON p.skills_data_id = sd.id )
ORDER BY sd.speed DESC
LIMIT 1;
END
:

