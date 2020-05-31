-- question 6

SELECT c.name AS name, MAX(f.price) AS max_price
FROM Flights f
JOIN Carriers c ON f.carrier_id = c.cid
WHERE (f.origin_city = 'Seattle WA' AND f.dest_city = 'New York NY')
OR (f.origin_city = 'New York NY' AND f.dest_city = 'Seattle WA')
GROUP BY c.name
ORDER BY max_price;

-- 3 rows
