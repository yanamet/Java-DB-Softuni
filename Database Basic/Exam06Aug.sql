-- 1 
CREATE TABLE `addresses`(
`id` INT PRIMARY KEY AUTO_INCREMENT,
`name` VARCHAR(50) NOT NULL
);

CREATE TABLE `categories`(
`id` INT PRIMARY KEY AUTO_INCREMENT,
`name` VARCHAR(10) NOT NULL
);

CREATE TABLE `offices`(
`id` INT PRIMARY KEY AUTO_INCREMENT,
`workspace_capacity` INT NOT NULL,
`website` VARCHAR(50),
`address_id` INT NOT NULL,
CONSTRAINT fk_office_addres
FOREIGN KEY (`address_id`)
REFERENCES `addresses`(`id`)
);

CREATE TABLE `employees`(
`id` INT PRIMARY KEY AUTO_INCREMENT,
`first_name` VARCHAR(30) NOT NULL,
`last_name` VARCHAR(30) NOT NULL,
`age` INT NOT NULL,
`salary` DECIMAL(10, 2) NOT NULL,
`job_title` VARCHAR(20) NOT NULL,
`happiness_level` CHAR(1)
);

CREATE TABLE `teams`(
`id` INT PRIMARY KEY AUTO_INCREMENT,
`name` VARCHAR(40) NOT NULL,
`office_id` INT NOT NULL,
`leader_id` INT UNIQUE,
CONSTRAINT fk_team_office
FOREIGN KEY (`office_id`)
REFERENCES `offices`(`id`),
CONSTRAINT fk_leader_employee
FOREIGN KEY (`leader_id`)
REFERENCES `employees`(`id`)
);



CREATE TABLE `games`(
`id` INT PRIMARY KEY AUTO_INCREMENT,
`name` VARCHAR(50) NOT NULL UNIQUE,
`description` TEXT,
`rating` FLOAT DEFAULT 5.5 NOT NULL,
`budget` DECIMAL(10, 2) NOT NULL,
`release_date` DATE,
`team_id` INT NOT NULL,
CONSTRAINT fk_game_team
FOREIGN KEY (`team_id`)
REFERENCES `teams`(`id`)
);

CREATE TABLE `games_categories`(
`game_id` INT NOT NULL,
`category_id` INT NOT NULL,
CONSTRAINT pk_game_category
PRIMARY KEY (`game_id`, `category_id`),
CONSTRAINT fk_games_gameID
FOREIGN KEY (`game_id`)
REFERENCES `games`(`id`),
CONSTRAINT fk_categories_categoryID
FOREIGN KEY (`category_id`)
REFERENCES `categories`(`id`)
);

-- 2
INSERT INTO games(`name`, rating, budget, team_id)
SELECT LOWER(REVERSE(RIGHT(`name`, CHAR_LENGTH(`name`) -1 ))) , (id), (leader_id * 1000), (id) FROM teams
WHERE id BETWEEN 1 AND 9;

-- 3
UPDATE employees AS e
JOIN teams AS t ON e.id = t.leader_id
SET salary = salary + 1000
WHERE e.id = t.leader_id AND age < 40 AND salary < 5000;

-- 4
DELETE g FROM games AS g
LEFT JOIN games_categories AS ga ON g.id = ga.game_id
WHERE category_id IS NULL AND release_date IS NULL;

-- 5
SELECT first_name, last_name, age, salary, happiness_level FROM employees
ORDER BY salary, id;

-- 6
SELECT t.`name`, a.`name`, CHAR_LENGTH(a.`name`) FROM teams AS t
JOIN offices AS o ON t.office_id = o.id
JOIN addresses AS a ON a.id = o.address_id
WHERE website IS NOT NULL
ORDER BY t.`name`, a.`name` ;

-- 7
SELECT c.`name`, COUNT(game_id) AS game_count, ROUND(AVG(g.budget), 2), MAX(g.rating) AS max_rating FROM games_categories AS gc
JOIN categories AS c ON c.id = gc.category_id
JOIN games AS g ON g.id = gc.game_id
GROUP BY c.id
HAVING max_rating >= 9.5
ORDER BY game_count DESC, c.`name` ASC ;

-- 8
SELECT g.`name`, release_date, 
CONCAT(SUBSTRING(description, 1, 10), '...') AS 'summary',
 CASE
	WHEN MONTH(release_date) = 1
  OR MONTH(release_date) =  2 
  OR MONTH(release_date) =  3 THEN 'Q1'
	WHEN MONTH(release_date) = 4
  OR MONTH(release_date) =  5 
  OR MONTH(release_date) =  6 THEN 'Q2'
  WHEN MONTH(release_date) = 7
  OR MONTH(release_date) =  8 
  OR MONTH(release_date) =  9 THEN 'Q3'
   WHEN MONTH(release_date) = 10
  OR MONTH(release_date) =  11 
  OR MONTH(release_date) =  12 THEN 'Q4'
 END AS 'Quarters',
  t.`name` AS 'team_name'
 FROM games AS g
JOIN teams AS t ON t.id = g.team_id
WHERE g.
`name` LIKE '%2' AND YEAR(release_date) = 2022 AND MONTH(release_date) % 2 = 0
ORDER BY Quarters;

-- 9
SELECT g.name, CASE 
 WHEN g.budget < 50000 THEN 'Normal budget'
 ELSE 'Insufficient budget'
END AS 'budget_level', t.name AS 'team_name', a.name AS 'addres_name'
 FROM games AS g
LEFT JOIN games_categories AS ga ON g.id = ga.game_id
JOIN teams AS t ON t.id = g.team_id
JOIN offices AS o ON o.id = t.office_id
JOIN addresses AS a ON a.id = o.address_id
WHERE category_id IS NULL AND release_date IS NULL
ORDER BY g.name;

-- 10
DELIMITER :
CREATE FUNCTION udf_game_info_by_name (game_name VARCHAR (20)) 
RETURNS TEXT
DETERMINISTIC
BEGIN
RETURN( SELECT CONCAT('The ', g.`name` ,' is developed by a ', t.`name` , ' in an office with an address ', a.name) 
FROM games AS g
JOIN teams AS t ON t.id = g.team_id
JOIN offices AS o ON o.id = t.office_id
JOIN addresses AS a ON a.id = o.address_id
WHERE g.`name` = game_name);
END
:

-- 11
CREATE PROCEDURE udp_update_budget (min_game_rating FLOAT)
BEGIN
UPDATE games AS g
LEFT JOIN games_categories AS gc ON g.id = gc.game_id
SET budget = budget + 100000, release_date = DATE_ADD(release_date, INTERVAL 1 YEAR)
WHERE category_id IS NULL AND release_date IS NOT NULL AND rating > min_game_rating;
END
:

