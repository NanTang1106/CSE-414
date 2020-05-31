-- question 8

SELECT c.name AS name, SUM(f.departure_delay) AS delay
FROM Flights f
JOIN Carriers c ON f.carrier_id = c.cid
group by c.name;

-- 22 rows
