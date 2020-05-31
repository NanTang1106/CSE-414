-- question 7

SELECT DISTINCT c.name AS carrier
FROM Carriers c, Flights f
WHERE c.cid = f.carrier_id
AND f.dest_city = 'San Francisco CA'
AND f.origin_city = 'Seattle WA';

-- 4 rows retrieved starting from 1 in 3 s 379 ms

/*
CARRIER
Alaska Airlines Inc.
SkyWest Airlines Inc.
United Air Lines Inc.
Virgin America
*/
