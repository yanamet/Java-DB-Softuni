
CREATE TABLE `products`(
`id` INT PRIMARY KEY AUTO_INCREMENT,
`name` VARCHAR(30) NOT NULL UNIQUE,
`type` VARCHAR(30) NOT NULL,
`price` DECIMAL(10, 2) NOT NULL
);

CREATE TABLE `clients`(
`id` INT PRIMARY KEY AUTO_INCREMENT,
`first_name` VARCHAR(50) NOT NULL,
`last_name` VARCHAR (50) NOT NULL,
`birthdate` DATE NOT NULL,
`card` VARCHAR(50),
`review` TEXT
);

CREATE TABLE `tables`(
`id` INT PRIMARY KEY AUTO_INCREMENT,
`floor` INT NOT NULL,
`reserved` TINYINT(1),
`capacity` INT NOT NULL
);

CREATE TABLE `waiters`(
`id` INT PRIMARY KEY AUTO_INCREMENT,
`first_name` VARCHAR(50) NOT NULL,
`last_name` VARCHAR (50) NOT NULL,
`email` VARCHAR(50) NOT NULL,
`phone` VARCHAR(50),
`salary` DECIMAL(10, 2)
);

CREATE TABLE `orders`(
`id` INT PRIMARY KEY AUTO_INCREMENT,
`table_id` INT NOT NULL,
`waiter_id` INT NOT NULL,
`order_time` TIME NOT NULL,
`payed_status` TINYINT(1),
CONSTRAINT fk_order_tableID
FOREIGN KEY (`table_id`)
REFERENCES `tables`(`id`),
CONSTRAINT fk_order_waiterID
FOREIGN KEY (`waiter_id`)
REFERENCES `waiters`(`id`)
);

CREATE TABLE `orders_clients`(
`order_id` INT,
`client_id` INT,
KEY pk_order_clients(`order_id`, `client_id`),
CONSTRAINT fk_orders_orderID
FOREIGN KEY (`order_id`)
REFERENCES `orders`(`id`),
CONSTRAINT fk_clients_clientID
FOREIGN KEY (`client_id`)
REFERENCES `clients`(`id`)
);

CREATE TABLE `orders_products`(
`order_id` INT,
`product_id` INT,
CONSTRAINT fk_ordersS_orderID
FOREIGN KEY (`order_id`)
REFERENCES `orders`(`id`),
CONSTRAINT fk_products_productID
FOREIGN KEY (`product_id`)
REFERENCES `products`(`id`)
);

-- 2
INSERT INTO products(`name`, `type`, `price`)
SELECT CONCAT(last_name, ' ', 'specialty'), ('Cocktail'), CEIL(salary * 0.01) FROM waiters
WHERE id > 6;

-- 3
UPDATE orders AS o
SET o.table_id = o.table_id - 1
WHERE o.id BETWEEN 12 AND 23;

-- 4
DELETE w FROM waiters AS w
LEFT JOIN orders AS o ON o.waiter_id = w.id
WHERE o.waiter_id IS NULL;

-- 5
SELECT id, first_name,	last_name,	birthdate,	card,	review FROM clients
ORDER BY birthdate DESC, id DESC;

-- 6
SELECT first_name,	last_name,	birthdate,	review FROM clients
WHERE card IS NULL AND YEAR(birthdate) BETWEEN 1978 AND 1993
ORDER BY last_name DESC, id ASC
LIMIT 5;

-- 7
SELECT CONCAT(w.last_name, w.first_name, CHAR_LENGTH(w.first_name), 'Restaurant') AS 'username',
REVERSE(SUBSTRING(email, 2, 12)) AS 'pass' FROM waiters AS w
WHERE w.salary IS NOT NULL
ORDER BY pass DESC;

-- 8
SELECT p.id, p.`name`, count(*) AS 'orders_count' FROM orders_products AS op
JOIN products AS p ON op.product_id = p.id
GROUP BY op.product_id
HAVING orders_count >= 5
ORDER BY orders_count DESC, p.`name` ASC ;

-- 9
SELECT t.id, t.capacity, COUNT(c.id) AS 'count_clients',
CASE 
WHEN t.capacity > COUNT(c.id) THEN 'Free seats'
WHEN t.capacity = COUNT(c.id) THEN 'Full'
WHEN t.capacity < COUNT(c.id) THEN 'Extra seats'
END AS 'seats'
 FROM tables AS t
JOIN orders AS o ON t.id = o.table_id
JOIN orders_clients AS oc ON oc.order_id = o.id
JOIN clients AS c ON oc.client_id = c.id
WHERE floor = 1
GROUP BY t.id
ORDER BY t.id DESC
;

-- 10
DELIMITER :
CREATE FUNCTION udf_client_bill2(full_name VARCHAR(50)) 
RETURNS DECIMAL(10, 2)
DETERMINISTIC
BEGIN
RETURN (SELECT SUM(p.price) FROM orders_clients AS oc
JOIN clients AS c ON oc.client_id = c.id
JOIN orders_products AS op ON oc.order_id = op.order_id
JOIN products AS p ON op.product_id = p.id
WHERE CONCAT(c.first_name, ' ', c.last_name) = full_name
GROUP BY c.id 
);
END
:

-- 11
CREATE PROCEDURE udp_happy_hour (type VARCHAR(50))
BEGIN
UPDATE products AS p
SET p.price = p.price * 0.8
WHERE p.`type` = type AND p.price >= 10.0;
END
:

