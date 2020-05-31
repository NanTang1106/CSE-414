-- question 3

SELECT w.day_of_week AS day_of_week, AVG(arrival_delay) AS delay
FROM Flights f JOIN Weekdays w ON f.day_of_week_id = w.did
GROUP BY w.day_of_week
ORDER BY delay DESC
LIMIT(1);

-- 1 row
