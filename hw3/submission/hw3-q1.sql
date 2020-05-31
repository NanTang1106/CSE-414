-- question 1

SELECT DISTINCT x.origin_city AS origin_city,
                x.dest_city AS dest_city,
                x.actual_time AS time
FROM (SELECT y1.origin_city, MAX(y1.actual_time) AS max_time
     FROM Flights y1
     GROUP BY y1.origin_city) AS y,
Flights x
WHERE y.origin_city = x.origin_city AND y.max_time = x.actual_time
ORDER BY origin_city, dest_city;

-- 334 rows retrieved starting from 1 in 13 s 971 ms

/*
ORIGIN_CITY	DEST_CITY	TIME
Aberdeen SD	Minneapolis MN	106
Abilene TX	Dallas/Fort Worth TX	111
Adak Island AK	Anchorage AK	471
Aguadilla PR	New York NY	368
Akron OH	Atlanta GA	408
Albany GA	Atlanta GA	243
Albany NY	Atlanta GA	390
Albuquerque NM	Houston TX	492
Alexandria LA	Atlanta GA	391
Allentown/Bethlehem/Easton PA	Atlanta GA	456
Alpena MI	Detroit MI	80
Amarillo TX	Houston TX	390
Anchorage AK	Barrow AK	490
Appleton WI	Atlanta GA	405
Arcata/Eureka CA	San Francisco CA	476
Asheville NC	Chicago IL	279
Ashland WV	Cincinnati OH	84
Aspen CO	Los Angeles CA	304
Atlanta GA	Honolulu HI	649
Atlantic City NJ	Fort Lauderdale FL	212
*/
