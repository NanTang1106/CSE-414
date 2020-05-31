-- question 5

-- Select set of dest_city as set of all cities
SELECT DISTINCT y.dest_city AS city
FROM Flights y
WHERE y.dest_city NOT IN
-- dest_city can be reached by one stop
  (SELECT  DISTINCT x2.dest_city
  FROM Flights x2
  WHERE x2.origin_city IN
    (SELECT DISTINCT x1.dest_city
    FROM Flights x1
    WHERE x1.origin_city = 'Seattle WA')
  )
AND y.dest_city NOT IN
-- dest_city can be reached directly
  (SELECT DISTINCT x1.dest_city
  FROM Flights x1
  WHERE x1.origin_city = 'Seattle WA')
AND y.dest_city <> 'Seattle WA'
ORDER BY city;

-- 3 rows retrieved starting from 1 in 39 s 192 ms

/*
CITY
Devils Lake ND
Hattiesburg/Laurel MS
St. Augustine FL
*/
