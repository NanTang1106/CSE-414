--question 2

SELECT DISTINCT f1.origin_city AS city
FROM Flights f1
WHERE 180 > ALL (SELECT f2.actual_time
                FROM Flights f2
                WHERE f2.origin_city = f1.origin_city)
ORDER BY city;

-- 109 rows retrieved starting from 1 in 11 s 914 ms

/*
CITY
Aberdeen SD
Abilene TX
Alpena MI
Ashland WV
Augusta GA
Barrow AK
Beaumont/Port Arthur TX
Bemidji MN
Bethel AK
Binghamton NY
Brainerd MN
Bristol/Johnson City/Kingsport TN
Butte MT
Carlsbad CA
Casper WY
Cedar City UT
Chico CA
College Station/Bryan TX
Columbia MO
Columbus GA
*/
