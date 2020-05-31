-- q4
-- seperate by comma with header
.head on
.mode csv
SELECT * FROM MyRestaurants;

-- seperate by | with header
.mode list
SELECT * FROM MyRestaurants;

-- in column form with width 15 and header
.mode column
.width 15 15 15 15 15
SELECT * FROM MyRestaurants;

-- three outputs without header
.head off
.mode csv
SELECT * FROM MyRestaurants;

.mode list
SELECT * FROM MyRestaurants;

.mode column
.width 15 15 15 15 15
SELECT * FROM MyRestaurants;
