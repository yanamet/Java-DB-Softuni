-- 1

CREATE TABLE `branches`(
`id` INT PRIMARY KEY AUTO_INCREMENT,
`name` VARCHAR(30) NOT NULL UNIQUE
);

CREATE TABLE `employees`(
`id` INT PRIMARY KEY AUTO_INCREMENT,
`first_name` VARCHAR(20) NOT NULL,
`last_name` VARCHAR(20) NOT NULL,
`salary` DECIMAL(10, 2) NOT NULL,
`started_on` DATE NOT NULL,
`branch_id` INT NOT NULL,
CONSTRAINT fk_employee_branch
FOREIGN KEY (`branch_id`)
REFERENCES `branches`(`id`)
);

CREATE TABLE `clients`(
`id` INT PRIMARY KEY AUTO_INCREMENT,
`full_name` VARCHAR(50) NOT NULL,
`age` INT NOT NULL
);

CREATE TABLE `employees_clients`(
`employee_id` INT,
`client_id` INT,
KEY k_employee_client(`employee_id`, `client_id`),
CONSTRAINT fk_employees_employeeID
FOREIGN KEY (`employee_id`)
REFERENCES `employees`(`id`),
CONSTRAINT fk_clients_clientID
FOREIGN KEY (`client_id`)
REFERENCES `clients`(`id`)
);

CREATE TABLE `bank_accounts`(
`id` INT PRIMARY KEY AUTO_INCREMENT,
`account_number` VARCHAR(10) NOT NULL,
`balance` DECIMAL(10, 2) NOT NULL,
`client_id` INT NOT NULL UNIQUE,
CONSTRAINT fk_bankAcc_client
FOREIGN KEY (`client_id`)
REFERENCES `clients`(`id`)
);

CREATE TABLE `cards`(
`id` INT PRIMARY KEY AUTO_INCREMENT,
`card_number` VARCHAR(19) NOT NULL,
`card_status` VARCHAR(7) NOT NULL,
`bank_account_id` INT NOT NULL,
CONSTRAINT fk_card_bankAcc
FOREIGN KEY (`bank_account_id`)
REFERENCES `bank_accounts`(`id`)
);

-- 2
INSERT INTO cards (card_number, card_status, bank_account_id)
SELECT reverse(full_name), ('Active'), id FROM clients
WHERE id BETWEEN 191 AND 200;

-- 3
UPDATE employees_clients AS ec
JOIN (SELECT ec1.employee_id, COUNT(ec1.client_id) AS 'count' FROM employees_clients AS ec1
GROUP BY ec1.employee_id
ORDER BY count ASC, ec1.employee_id) AS c
SET ec.employee_id = c.employee_id
WHERE ec.employee_id = ec.client_id;

-- 4
DELETE e FROM employees AS e
LEFT JOIN employees_clients AS ec ON ec.employee_id = e.id
WHERE ec.client_id IS NULL;

-- 5
SELECT id, full_name FROM clients
ORDER BY id;

-- 6
SELECT id, CONCAT_WS(' ', first_name, last_name) AS full_name, 
concat('$',salary) as salary, started_on FROM employees
WHERE salary >= 100000 AND YEAR(started_on) >= 2018 
 AND MONTH(started_on) >= 1 AND DAY(started_on) >= 1
 ORDER BY salary DESC, id;
 
 -- 7
 SELECT c.id, CONCAT(c.card_number, ' : ', cl.full_name) FROM cards AS c
JOIN bank_accounts AS ba ON c.bank_account_id = ba.id
JOIN clients AS cl ON ba.client_id = cl.id
ORDER BY c.id DESC;

-- 8
SELECT CONCAT_WS(' ', first_name, last_name) AS full_name, e.started_on, COUNT(*) AS 'clients_count' FROM employees AS e
JOIN employees_clients AS ec ON ec.employee_id = e.id
JOIN clients AS c ON ec.client_id = c.id
GROUP BY e.id
ORDER BY clients_count DESC, e.id
LIMIT 5;

-- 9
SELECT b.`name`, COUNT(c.id) AS 'cards_count' FROM branches AS b
LEFT JOIN employees AS e ON e.branch_id = b.id
LEFT JOIN employees_clients AS ec ON ec.employee_id = e.id
LEFT JOIN clients AS cl ON ec.client_id = cl.id
LEFT JOIN bank_accounts AS ba ON ba.client_id = cl.id
LEFT JOIN cards AS c ON c.bank_account_id = ba.id
GROUP BY b.`name`
ORDER BY cards_count DESC, b.`name`;

-- 10
DELIMITER :
CREATE FUNCTION udf_client_cards_count(`name` VARCHAR(30))
RETURNS INT
DETERMINISTIC
BEGIN
RETURN (SELECT COUNT(DISTINCT c.id) FROM  clients AS cl 
LEFT JOIN bank_accounts AS ba ON ba.client_id = cl.id
LEFT JOIN cards AS c ON c.bank_account_id = ba.id
GROUP BY cl.full_name
HAVING full_name = `name`);
END
:

-- 11
CREATE PROCEDURE udp_clientinfo (full_name VARCHAR(50))
BEGIN 
SELECT c.full_name, c.age, ba.account_number, CONCAT('$', ba.balance) FROM clients as c
JOIN bank_accounts AS ba ON ba.client_id = c.id
WHERE c.full_name = full_name;
END
:
