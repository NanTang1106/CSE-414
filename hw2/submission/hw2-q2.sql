-- question 2

SELECT c.name, f1.flight_num AS f1_flight_num,
f1.origin_city AS f1_origin_city, f1.dest_city AS f1_dest_city,
f1.actual_time AS f1_actural_time, f2.flight_num AS f2_flight_num,
f2.origin_city AS f2_origin_city, f2.dest_city AS f2_dest_city,
f2.actual_time AS f2_actural_time,
(f1.actual_time + f2.actual_time) AS actural_time
FROM Flights f1
JOIN Flights f2 ON f1.dest_city = f2.origin_city
JOIN Months m ON f1.month_id = m.mid
JOIN Carriers c ON f1.carrier_id = c.cid
WHERE f1.origin_city = 'Seattle WA'
AND f2.dest_city = 'Boston MA'
AND m.month = 'July'
AND f1.day_of_month = 15
AND f2.day_of_month = 15
AND f1.carrier_id = f2.carrier_id
AND f1.actual_time + f2.actual_time < 420;

-- 1472 rows
