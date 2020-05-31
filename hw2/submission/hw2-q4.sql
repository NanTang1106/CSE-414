-- question 4

SELECT DISTINCT c.name AS name
FROM Flights f
JOIN Carriers c ON f.carrier_id = c.cid
GROUP BY c.name, f.day_of_month, f.month_id
HAVING COUNT(*) > 1000;

-- 12 rows
