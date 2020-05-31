-- question 3

SELECT DISTINCT x.origin_city AS origin_city,
                (100.0 * ISNULL(y.shortcount, 0) / x.totalcount) AS percentage
FROM (SELECT y1.origin_city, COUNT(*) AS shortcount
     FROM Flights y1
     WHERE ISNULL(y1.actual_time, 181) < 180
     GROUP BY y1.origin_city) AS y
RIGHT OUTER JOIN (SELECT x1.origin_city, COUNT(*) AS totalcount
                 FROM Flights x1
                 GROUP BY x1.origin_city) AS x
ON y.origin_city = x.origin_city
ORDER BY percentage;

-- 327 rows retrieved starting from 1 in 14 s 847 ms

/*
ORIGIN_CITY   PERCENTAGE
Guam TT   0.000000000000
Pago Pago TT    0.000000000000
Aguadilla PR    29.433962264150
Anchorage AK    32.146037399821
San Juan PR   33.890360709190
Charlotte Amalie VI   40.000000000000
Ponce PR    41.935483870967
Fairbanks AK    50.691244239631
Kahului HI    53.664998528113
Honolulu HI   54.908808692277
San Francisco CA    56.307656826568
Los Angeles CA    56.604107648725
Seattle WA    57.755416553349
Long Beach CA   62.454116413214
Kona HI   63.282107574094
New York NY   63.481519772551
Las Vegas NV    65.163009288383
Christiansted VI    65.333333333333
Newark NJ   67.137355584082
Worcester MA 67.741935483878
*/
