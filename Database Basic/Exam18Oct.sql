-- 1

CREATE TABLE `pictures`(
`id` INT PRIMARY KEY AUTO_INCREMENT,
`url` VARCHAR(100) NOT NULL,
`added_on` DATETIME NOT NULL
);

CREATE TABLE `categories`(
`id` INT PRIMARY KEY AUTO_INCREMENT,
`name` VARCHAR(40) NOT NULL UNIQUE
); 

CREATE TABLE `products`(
`id` INT PRIMARY KEY AUTO_INCREMENT,
`name` VARCHAR(40) NOT NULL UNIQUE,
`best_before` DATE,
`price` DECIMAL(10, 2) NOT NULL,
`description` TEXT,
`category_id` INT NOT NULL,
`picture_id` INT NOT NULL,
CONSTRAINT fk_product_category
FOREIGN KEY (`category_id`)
REFERENCES `categories`(`id`),
CONSTRAINT fk_product_pictore
FOREIGN KEY (`picture_id`)
REFERENCES `pictures`(`id`)
); 

CREATE TABLE `towns`(
`id` INT PRIMARY KEY AUTO_INCREMENT,
`name` VARCHAR(20) NOT NULL UNIQUE
);

CREATE TABLE `addresses`(
`id` INT PRIMARY KEY AUTO_INCREMENT,
`name` VARCHAR(50) NOT NULL UNIQUE,
`town_id` INT NOT NULL,
CONSTRAINT fk_address_town
FOREIGN KEY (`town_id`)
REFERENCES `towns`(`id`)
);

CREATE TABLE `stores`(
`id` INT PRIMARY KEY AUTO_INCREMENT,
`name` VARCHAR(20) NOT NULL UNIQUE,
`rating` FLOAT NOT NULL,
`has_parking` TINYINT(1) DEFAULT 0,
`address_id` INT NOT NULL,
CONSTRAINT fk_store_address
FOREIGN KEY (`address_id`)
REFERENCES `addresses`(`id`)
);

CREATE TABLE `products_stores`(
`product_id` INT NOT NULL,
`store_id` INT NOT NULL,
CONSTRAINT pk_product_store
PRIMARY KEY (`product_id`, `store_id`),
CONSTRAINT fk_products_productID
FOREIGN KEY (`product_id`)
REFERENCES `products`(`id`),
CONSTRAINT fk_stores_storeID
FOREIGN KEY (`store_id`)
REFERENCES `stores`(`id`)
);

CREATE TABLE `employees`(
`id` INT PRIMARY KEY AUTO_INCREMENT,
`first_name` VARCHAR(15) NOT NULL,
`middle_name` CHAR(1),
`last_name` VARCHAR(20) NOT NULL,
`salary` DECIMAL(19, 2) DEFAULT 0,
`hire_date` DATE NOT NULL,
`manager_id` INT,
`store_id` INT NOT NULL,
CONSTRAINT fk_employees_managerID
FOREIGN KEY (`manager_id`)
REFERENCES `employees`(`id`),
CONSTRAINT fk_employees_store
FOREIGN KEY (`store_id`)
REFERENCES `stores`(`id`)
);

-- 2
 INSERT INTO products_stores
 SELECT (p.id), (1) FROM products AS p
 LEFT JOIN products_stores AS ps ON p.id = ps.product_id
 WHERE ps.product_id IS NULL;
 
 -- 3
 UPDATE employees
SET manager_id = 3, salary = salary - 500
WHERE YEAR(hire_date) > 2003 AND store_id != 14 AND store_id != 5;

-- 4 
DELETE e FROM employees AS e
WHERE manager_id IS NOT NULL AND manager_id != id AND salary >= 6000;

-- 5
SELECT first_name, middle_name, last_name,	salary,	hire_date FROM employees
ORDER BY hire_date DESC;

-- 6
SELECT pr.name, pr.price, best_before, CONCAT(SUBSTRING(pr.description, 1, 10), '...'), pi.url FROM products AS pr
JOIN pictures AS pi ON pi.id = pr.picture_id
WHERE char_length(pr.description) > 100 AND YEAR(pi.added_on) < 2019 AND pr.price > 20
ORDER BY pr.price DESC;

-- 7
SELECT s.`name`, COUNT(product_id) AS 'count_products', ROUND(AVG(p.price), 2) AS 'avg_price' FROM stores AS s
LEFT JOIN products_stores AS ps ON s.id = ps.store_id
LEFT JOIN products AS p ON p.id = ps.product_id
GROUP BY s.id
ORDER BY count_products DESC, avg_price DESC, s.id;

-- 8
SELECT CONCAT_WS(' ', e.first_name, e.last_name), s.`name`, a.`name`, salary FROM employees AS e
JOIN stores AS s ON e.store_id = s.id
JOIN addresses AS a ON s.address_id = a.id
WHERE salary < 4000 AND a.`name` LIKE '%5%' AND CHAR_LENGTH(s.`name`) > 8 AND e.last_name LIKE '%n';

-- 9
SELECT REVERSE(s.`name`), CONCAT(UPPER(t.`name`), '-', a.`name`) AS 'full_address'
, COUNT(e.id) AS 'employee_count' FROM employees AS e
JOIN stores AS s ON e.store_id = s.id
JOIN addresses AS a ON s.address_id = a.id
JOIN towns AS t ON a.town_id = t.id
GROUP BY store_id
HAVING employee_count >= 1
ORDER BY full_address ASC;

-- 10
DELIMITER :
CREATE FUNCTION udf_top_paid_employee_by_store(store_name VARCHAR(50)) 
RETURNS VARCHAR(100)
DETERMINISTIC
BEGIN
RETURN (SELECT CONCAT(e.first_name, ' ' , middle_name, '. ', last_name,' works in store for ', TIMESTAMPDIFF(YEAR, hire_date, '2020-10-18'), ' years') FROM employees AS e
JOIN stores AS s ON e.store_id = s.id
WHERE s.name = store_name
ORDER BY salary DESC
LIMIT 1 );
END
:

-- 11
CREATE PROCEDURE udp_update_product_price4 (address_name VARCHAR (50))
BEGIN
DECLARE additional_price INT;
SET additional_price = IF(address_name LIKE '0%' , 100 , 200);
UPDATE products AS p
JOIN products_stores AS ps ON ps.product_id = p.id
JOIN stores AS s ON ps.store_id = s.id
JOIN addresses AS a ON s.address_id = a.id
SET p.price = p.price + additional_price
WHERE a.name = address_name;
END
: