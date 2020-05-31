-- question 6

SELECT DISTINCT c.name AS carrier
FROM Carriers c,
  (SELECT x.carrier_id, x.dest_city
  FROM Flights x
  WHERE x.origin_city = 'Seattle WA') as f
WHERE c.cid = f.carrier_id
AND f.dest_city = 'San Francisco CA'
ORDER BY carrier;

-- 4 rows retrieved starting from 1 in 3 s 498 ms

/*
CARRIER
Alaska Airlines Inc.
SkyWest Airlines Inc.
United Air Lines Inc.
Virgin America
*/
