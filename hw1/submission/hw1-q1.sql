-- q1
CREATE TABLE Edges (
Source INT,
Destination INT
);

INSERT INTO Edges VALUES (10, 5);
INSERT INTO Edges VALUES (6, 25);
INSERT INTO Edges VALUES (1, 3);
INSERT INTO Edges VALUES (4, 4);

SELECT * FROM Edges;
SELECT Source FROM Edges;
SELECT * FROM Edges WHERE Source > Destination;

INSERT INTO Edges VALUES ('-1', '2000');
