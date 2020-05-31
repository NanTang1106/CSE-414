-- question 4

SELECT DISTINCT x2.dest_city AS city
FROM Flights x2
WHERE x2.origin_city IN
  (SELECT x1.dest_city
  FROM Flights x1
  WHERE x1.origin_city = 'Seattle WA')
AND x2.dest_city NOT IN
  (SELECT x1.dest_city
  FROM Flights x1
  WHERE x1.origin_city = 'Seattle WA')
AND x2.dest_city <> 'Seattle WA'
ORDER BY city;

-- 256 rows retrieved starting from 1 in 22 s 618 ms

/*
CITY
Aberdeen SD
Abilene TX
Adak Island AK
Aguadilla PR
Akron OH
Albany GA
Albany NY
Alexandria LA
Allentown/Bethlehem/Easton PA
Alpena MI
Amarillo TX
Appleton WI
Arcata/Eureka CA
Asheville NC
Ashland WV
Aspen CO
Atlantic City NJ
Augusta GA
Bakersfield CA
Bangor ME
*/
