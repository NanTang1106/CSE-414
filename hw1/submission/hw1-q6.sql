-- question 6
SELECT * FROM MyRestaurants WHERE DATE(last_visit) < DATE('now', '-3 month') AND liking = 1;
