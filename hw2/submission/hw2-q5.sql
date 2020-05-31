-- question 5

SELECT c.name AS name,
CAST(SUM(f.canceled) AS FLOAT)/
CAST(COUNT(*) AS FLOAT) AS percent
FROM Flights f
JOIN Carriers c ON f.carrier_id = c.cid
WHERE f.origin_city = 'Seattle WA'
GROUP BY c.name
HAVING percent > 0.005
ORDER BY percent ASC;

-- 6 rows
