-- 1
CREATE TABLE `addresses`(
`id` INT PRIMARY KEY AUTO_INCREMENT,
`name` VARCHAR(100) NOT NULL
);

CREATE TABLE `categories`(
`id` INT PRIMARY KEY AUTO_INCREMENT,
`name` VARCHAR(10) NOT NULL
);

CREATE TABLE `clients`(
`id` INT PRIMARY KEY AUTO_INCREMENT,
`full_name` VARCHAR (50) NOT NULL,
`phone_number` VARCHAR(20) NOT NULL
);

CREATE TABLE `drivers`(
`id` INT PRIMARY KEY AUTO_INCREMENT,
`first_name` VARCHAR(30) NOT NULL,
`last_name` VARCHAR(30) NOT NULL,
`age` INT NOT NULL,
`rating` FLOAT DEFAULT 5.5
);           

CREATE TABLE `cars`(
`id` INT PRIMARY KEY AUTO_INCREMENT,
`make` VARCHAR(20) NOT NULL,
`model` VARCHAR(20),
`year` INT NOT NULL DEFAULT 0,
`mileage` INT DEFAULT 0,
`condition` CHAR(1) NOT NULL,
`category_id` INT NOT NULL,
CONSTRAINT fk_cars_category
FOREIGN KEY (`category_id`)
REFERENCES `categories`(`id`)
);

CREATE TABLE `courses`(
`id` INT PRIMARY KEY AUTO_INCREMENT,
`from_address_id` INT NOT NULL,
`start` DATETIME NOT NULL,
`bill` DECIMAL(10, 2) DEFAULT 10,
`car_id` INT NOT NULL,
`client_id` INT NOT NULL,
CONSTRAINT fk_courses_address
FOREIGN KEY (`from_address_id`)
REFERENCES `addresses`(`id`),
CONSTRAINT fk_courses_cars
FOREIGN KEY (`car_id`)
REFERENCES `cars`(`id`),
CONSTRAINT fk_courses_clients
FOREIGN KEY (`client_id`)
REFERENCES `clients`(`id`)
);

CREATE TABLE `cars_drivers`(
`car_id` INT NOT NULL,
`driver_id` INT NOT NULL,
CONSTRAINT pk_car_driver
PRIMARY KEY (`car_id`, `driver_id`),
CONSTRAINT fk_cars_carID
FOREIGN KEY (`car_id`)
REFERENCES `cars`(`id`),
CONSTRAINT fk_drivers_driverID
FOREIGN KEY (`driver_id`)
REFERENCES `drivers`(`id`)
);

-- 2
INSERT INTO clients(full_name, phone_number)
SELECT CONCAT_WS(' ', first_name, last_name), CONCAT('(088) 9999', (d.id * 2))  FROM drivers AS d
WHERE d.id BETWEEN 10 AND 20;

-- 3
  UPDATE cars AS c SET c.`condition` = 'C'
        WHERE
    (mileage IS NULL OR mileage >= 800000)
        AND `year` <= 2010
        AND make NOT LIKE 'Mercedes-Benz';
        
-- 4
DELETE cl FROM clients AS cl
LEFT JOIN courses AS co ON cl.id = co.client_id
WHERE co.client_id IS NULL AND char_length(full_name) > 3 ;

-- 5
SELECT make, model, `condition` FROM cars
ORDER BY id;

-- 6
SELECT d.first_name, d.last_name, c.make, c.model, c.mileage FROM cars_drivers AS cd
JOIN drivers AS d ON d.id = cd.driver_id
JOIN cars AS c ON cd.car_id = c.id
WHERE c.mileage IS NOT NULL
ORDER BY c.mileage DESC, d.first_name ASC;

-- 7
SELECT c.id, c.make, c.mileage, COUNT(client_id) AS 'course_count', ROUND(AVG(bill), 2) FROM cars AS c
LEFT JOIN courses AS co ON c.id = co.car_id
GROUP BY c.id
HAVING course_count != 2
ORDER BY course_count DESC, c.id;

-- 8
SELECT full_name, COUNT(co.car_id) AS 'count_courses', SUM(co.bill)  FROM clients AS cl
JOIN courses AS co ON cl.id = co.client_id
WHERE full_name LIKE '_a%'
GROUP BY cl.id
HAVING count_courses > 1
ORDER BY full_name;

-- 9
SELECT a.name,
CASE 
WHEN (HOUR(`start`) >= 6) AND (HOUR(`start`) <= 20) THEN 'Day'
ELSE 'Night'
END AS 'day_time', co.bill, cl.full_name, cars.make, cars.model, categories.`name`
 FROM courses AS co
JOIN addresses AS a ON co.from_address_id = a.id
JOIN clients AS cl ON co.client_id = cl.id
JOIN cars ON co.car_id = cars.id
JOIN categories ON categories.id = cars.category_id
ORDER BY co.id;

-- 10
DELIMITER :
CREATE FUNCTION udf_courses_by_client(phone_num VARCHAR (20)) 
RETURNS INT
DETERMINISTIC
BEGIN
	RETURN (SELECT COUNT(*) FROM clients AS cl
JOIN courses AS co ON cl.id = co.client_id
WHERE cl.phone_number = phone_num
GROUP BY cl.id);
END
:

-- 11
CREATE PROCEDURE udp_courses_by_address(address_name VARCHAR(100))
BEGIN
SELECT a.`name`, cl.full_name,  
CASE 
WHEN co.bill <= 20 THEN 'Low'
WHEN co.bill <= 30 THEN 'Medium'
ELSE 'High'
END AS 'bill_level', c.make, c.`condition`, ca.`name`
 FROM courses AS co
JOIN addresses AS a ON co.from_address_id = a.id
JOIN clients AS cl ON co.client_id = cl.id
JOIN cars AS c ON co.car_id = c.id
JOIN categories AS ca ON c.category_id = ca.id
WHERE a.`name` = address_name
ORDER BY c.make, cl.full_name;
END
:
