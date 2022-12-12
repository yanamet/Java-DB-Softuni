-- 1
CREATE TABLE `users`(
`id` INT PRIMARY KEY,
`username` VARCHAR(30) UNIQUE NOT NULL,
`password` VARCHAR(30) NOT NULL,
`email` VARCHAR(50) NOT NULL,
`gender` CHAR(1) NOT NULL,
`age` INT NOT NULL,
`job_title` VARCHAR(40) NOT NULL,
`ip` VARCHAR(30) NOT NULL
);


CREATE TABLE `addresses`(
`id` INT PRIMARY KEY AUTO_INCREMENT,
`address` VARCHAR(30) NOT NULL,
`town` VARCHAR(30) NOT NULL,
`country` VARCHAR(30) NOT NULL,
`user_id` INT NOT NULL,
CONSTRAINT fk_address_user
FOREIGN KEY  (`user_id`)
REFERENCES `users`(`id`)
);

CREATE TABLE `photos`(
`id` INT PRIMARY KEY AUTO_INCREMENT,
`description` TEXT NOT NULL,
`date` DATETIME NOT NULL,
`views` INT NOT NULL DEFAULT 0
);

CREATE TABLE `comments`(
`id` INT PRIMARY KEY AUTO_INCREMENT,
`comment` VARCHAR(255) NOT NULL,
`date` DATETIME NOT NULL,
`photo_id` INT NOT NULL,
CONSTRAINT fk_comment_photo
FOREIGN KEY  (`photo_id`)
REFERENCES `photos`(`id`)
);

CREATE TABLE `users_photos`(
`user_id` INT NOT NULL,
`photo_id` INT NOT NULL,
KEY k_user_photo (`user_id`, `photo_id`),
CONSTRAINT fk_userID_users
FOREIGN KEY  (`user_id`)
REFERENCES `users`(`id`),
CONSTRAINT fk_phoroID_photos
FOREIGN KEY  (`photo_id`)
REFERENCES `photos`(`id`)
);

CREATE TABLE `likes`(
`id` INT PRIMARY KEY AUTO_INCREMENT,
`photo_id` INT,
`user_id` INT,
CONSTRAINT fk_likes_users
FOREIGN KEY  (`user_id`)
REFERENCES `users`(`id`),
CONSTRAINT fk_likes_photos
FOREIGN KEY  (`photo_id`)
REFERENCES `photos`(`id`)
);

-- 2
INSERT INTO addresses(address, town, country, user_id)
SELECT username, `password`, ip, age  FROM users AS u
WHERE gender = 'M';

-- 3
UPDATE addresses 
SET country = (CASE
WHEN country LIKE 'B%' THEN 'Blocked'
WHEN country LIKE 'T%' THEN 'Test'
WHEN country LIKE 'P%' THEN 'In Progress'
ELSE country
END );

-- 4
DELETE a FROM addresses AS a
WHERE a.id % 3 = 0;

-- 5
SELECT username, gender, age FROM users
ORDER BY age DESC, username ASC;

-- 6
SELECT p.id, p.`date`, `description`, COUNT(c.id) AS 'comment_count' FROM photos AS p
JOIN comments AS c ON p.id = c.photo_id
GROUP BY photo_id
ORDER BY comment_count DESC, p.id ASC
LIMIT 5;

-- 7
SELECT CONCAT_WS(' ', u.id, u.`username`), u.email FROM users_photos AS up
JOIN users AS u ON up.user_id = u.id
WHERE user_id = photo_id
ORDER BY u.id;

-- 8
SELECT p.id, COUNT(DISTINCT l.id) AS 'LIKES', COUNT( DISTINCT c.id) AS 'COMMENTS' FROM photos AS p
LEFT JOIN likes AS l ON p.id = l.photo_id
LEFT JOIN comments AS c ON p.id = c.photo_id
GROUP BY p.id
ORDER BY LIKES DESC, COMMENTS DESC, p.id ASC;

-- 9
SELECT CONCAT(SUBSTRING(`description`, 1, 30), '...') AS 'summary', `date` FROM photos AS p
WHERE DAY(`date`) = 10
ORDER BY `date` DESC;

-- 10
DELIMITER :
CREATE FUNCTION udf_users_photos_count4(username VARCHAR(30))
RETURNS INT
DETERMINISTIC
BEGIN
 RETURN (SELECT COUNT(*) FROM users AS u
JOIN users_photos AS up ON up.user_id = u.id
WHERE u.username = username
GROUP BY u.id);
END
:

-- 11


