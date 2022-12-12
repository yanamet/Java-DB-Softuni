-- 2
INSERT INTO `actors`(first_name, last_name, birthdate, height, awards, country_id)
SELECT REVERSE(a.first_name), REVERSE(a.last_name), DATE(a.birthdate - 2), (a.height + 10), (a.country_id), (3) FROM `actors` AS a 
WHERE a.id <= 10;

-- 3
UPDATE movies_additional_info AS m 
SET m.runtime = m.runtime - 10
WHERE m.id >= 15 AND m.id <= 25; 

-- 4
DELETE c FROM countries AS c
LEFT JOIN movies AS m ON m.country_id = c.id
WHERE title IS NULL;

-- 5
SELECT * FROM countries AS c
ORDER BY c.currency DESC, c.id;

-- 6
SELECT mi.id, title, runtime, budget, release_date  FROM movies_additional_info AS mi
JOIN movies AS m ON m.movie_info_id = mi.id
WHERE YEAR(mi.release_date) BETWEEN 1996 AND 1999
ORDER BY mi.runtime ASC, mi.id
LIMIT 20;

-- 7
SELECT  CONCAT(first_name, " " ,last_name) AS `full_name`,
CONCAT(REVERSE(last_name), LENGTH(last_name), '@cast.com') AS `email`,
 TIMESTAMPDIFF(YEAR, birthdate, NOW()) AS `age`, height FROM actors AS a
 left JOIN movies_actors AS ma ON a.id = ma.actor_id
 WHERE ma.movie_id IS NULL
 ORDER BY height ASC;
 
 -- 8
 SELECT c.`name`, COUNT(m.id) AS movie_count FROM movies AS m, countries AS c
WHERE m.country_id = c.id
GROUP BY m.country_id
HAVING movie_count >= 7
ORDER BY c.`name` DESC;

-- 9 
SELECT title, 
CASE
	WHEN rating <= 4 THEN 'poor'
    WHEN rating <= 7 THEN 'good'
    ELSE 'excellent'
END AS 'rating'
,
IF(mi.has_subtitles = 1, 'english', '-') AS 'subtitles', budget FROM movies_additional_info AS mi
JOIN movies AS m ON m.movie_info_id = mi.id
ORDER BY budget DESC;

-- 10
DELIMITER |*
CREATE FUNCTION `udf_actor_history_movies_count2` (full_name VARCHAR(50))
RETURNS INTEGER
deterministic
BEGIN

RETURN (SELECT COUNT(ma.movie_id) FROM actors AS a
JOIN movies_actors AS ma ON ma.actor_id = a.id
JOIN genres_movies AS gm ON gm.movie_id = ma.movie_id
JOIN genres AS g ON gm.genre_id = g.id
WHERE g.id = 12 AND CONCAT(first_name, ' ', last_name) = full_name
GROUP BY full_name);
END
|*

-- 11


