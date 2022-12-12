-- 1
CREATE TABLE `brands` (
    `id` INT PRIMARY KEY AUTO_INCREMENT,
    `name` VARCHAR(40) NOT NULL UNIQUE
);

CREATE TABLE `categories` (
    `id` INT PRIMARY KEY AUTO_INCREMENT,
    `name` VARCHAR(40) NOT NULL UNIQUE
);

CREATE TABLE `reviews` (
    `id` INT PRIMARY KEY AUTO_INCREMENT,
    `content` TEXT,
    `rating` DECIMAL(10 , 2 ) NOT NULL,
    `picture_url` VARCHAR(80) NOT NULL,
    `published_at` DATETIME NOT NULL
);

CREATE TABLE `products` (
    `id` INT PRIMARY KEY AUTO_INCREMENT,
    `name` VARCHAR(40) NOT NULL,
    `price` DECIMAL(19 , 2 ) NOT NULL,
    `quantity_in_stock` INT,
    `description` TEXT,
    `brand_id` INT NOT NULL,
    `category_id` INT NOT NULL,
    `review_id` INT,
    CONSTRAINT fk_brand_product FOREIGN KEY (`brand_id`)
        REFERENCES `brands` (`id`),
    CONSTRAINT fk_category_product FOREIGN KEY (`category_id`)
        REFERENCES `categories` (`id`),
    CONSTRAINT fk_review_product FOREIGN KEY (`review_id`)
        REFERENCES `reviews` (`id`)
);


CREATE TABLE `customers` (
    `id` INT PRIMARY KEY AUTO_INCREMENT,
    `first_name` VARCHAR(20) NOT NULL,
    `last_name` VARCHAR(20) NOT NULL,
    `phone` VARCHAR(30) NOT NULL UNIQUE,
    `address` VARCHAR(60) NOT NULL,
    `discount_card` BIT NOT NULL DEFAULT 0
);

CREATE TABLE `orders` (
    `id` INT PRIMARY KEY AUTO_INCREMENT,
    `order_datetime` DATETIME NOT NULL,
    `customer_id` INT NOT NULL,
    CONSTRAINT fk_customer_order FOREIGN KEY (`customer_id`)
        REFERENCES `customers` (`id`)
);


CREATE TABLE `orders_products` (
    `order_id` INT,
    `product_id` INT,
    KEY pk_order_product (`order_id` , `product_id`),
    CONSTRAINT fk_order_orderID FOREIGN KEY (`order_id`)
        REFERENCES `orders` (`id`),
    CONSTRAINT fk_product_productID FOREIGN KEY (`product_id`)
        REFERENCES `products` (`id`)
);

-- 2
INSERT INTO reviews(content, picture_url, published_at, rating)
SELECT SUBSTRING(p.description, 1, 15), REVERSE(p.name),
DATE('2010-10-10'), (p.price / 8) FROM products AS p
WHERE p.id >= 5;

-- 3
UPDATE products AS p
SET p.quantity_in_stock = p.quantity_in_stock - 5
WHERE p.quantity_in_stock BETWEEN 60 AND 70;

-- 4
DELETE c FROM customers AS c
LEFT JOIN orders AS o ON c.id = o.customer_id
WHERE o.customer_id IS NULL;
 
 -- 5
 SELECT * FROM categories AS c
ORDER BY c.`name` DESC;

-- 6
SELECT id, brand_id, `name`, quantity_in_stock  FROM products AS p
WHERE price > 1000 AND quantity_in_stock < 30
ORDER BY P.quantity_in_stock ASC,  id;

-- 7
SELECT id, content, rating, picture_url, published_at  FROM reviews AS r
WHERE content LIKE 'My%' AND LENGTH(content) > 61
ORDER BY rating DESC;

-- 8
SELECT CONCAT(c.first_name, ' ', c.last_name) AS 'full_name', c.address, o.order_datetime FROM customers AS c
JOIN orders AS o 
ON c.id = o.customer_id
WHERE YEAR(o.order_datetime) <= 2018
ORDER BY full_name DESC;

-- 9
SELECT COUNT(p.id) AS items_count, c.`name`, SUM(p.quantity_in_stock) AS total_quantity FROM products AS p
JOIN categories AS c ON c.id = p.category_id
GROUP BY c.id
ORDER BY items_count DESC, total_quantity ASC
LIMIT 5;

-- 10
DELIMITER ***
CREATE FUNCTION udf_customer_products_count2(name VARCHAR(30)) 
RETURNS INT 
DETERMINISTIC
BEGIN
RETURN (SELECT COUNT(*) AS full_name FROM customers AS c
JOIN orders AS o ON c.id = o.customer_id
JOIN orders_products AS op ON op.order_id = o.id
WHERE first_name = name);
END
***

-- 11
CREATE PROCEDURE udp_reduce_price(category_name VARCHAR(50))
BEGIN
UPDATE products AS p
JOIN categories AS c ON p.category_id = c.id
JOIN reviews AS r ON p.review_id = r.id
SET p.price = p.price * 0.7
WHERE c.name = category_name AND r.rating < 4;
END
