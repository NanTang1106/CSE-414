-- create Carriers
CREATE TABLE Carriers (
  cid varchar(7),
  name varchar(83),
  PRIMARY KEY (cid)
);

-- create Months
CREATE TABLE Months (
  mid int,
  month varchar(9),
  PRIMARY KEY (mid)
);

-- create Weekdays
CREATE TABLE Weekdays (
  did int,
  day_of_week varchar(9),
  PRIMARY KEY (did)
);

-- create Flights
CREATE TABLE Flights (
  fid int,
  month_id int,        -- 1-12
  day_of_month int,    -- 1-31
  day_of_week_id int,  -- 1-7, 1 = Monday, 2 = Tuesday, etc
  carrier_id varchar(7),
  flight_num int,
  origin_city varchar(34),
  origin_state varchar(47),
  dest_city varchar(34),
  dest_state varchar(46),
  departure_delay int, -- in mins
  taxi_out int,        -- in mins
  arrival_delay int,   -- in mins
  canceled int,        -- 1 means canceled
  actual_time int,     -- in mins
  distance int,        -- in miles
  capacity int,
  price int,            -- in $
  PRIMARY KEY (fid),
  FOREIGN KEY (carrier_id) REFERENCES Carriers(cid),
  FOREIGN KEY (month_id) REFERENCES Months(mid),
  FOREIGN KEY (day_of_week_id) REFERENCES Weekdays(did)
);

PRAGMA foreign_keys = ON;

.mode csv
.import ../starter-code/carriers.csv Carriers
.import ../starter-code/months.csv Months
.import ../starter-code/weekdays.csv Weekdays
.import ../starter-code/flights-small.csv Flights
