-- question 1
.open hw2.db

.header on
.mode tab

SELECT DISTINCT f.flight_num AS flight_num
FROM Flights f
JOIN Carriers c ON f.carrier_id = c.cid
JOIN Weekdays w ON f.day_of_week_id = w.did
WHERE f.origin_city = 'Seattle WA'
AND f.dest_city = 'Boston MA'
AND c.name = 'Alaska Airlines Inc.'
AND w.day_of_week = 'Monday';

-- 3 rows
