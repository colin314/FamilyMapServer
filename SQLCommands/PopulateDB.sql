USE FamilyMapDb;
GO

DECLARE @u1 uniqueidentifier 
SET @u1 = NEWID()
DECLARE @u2 uniqueidentifier 
SET @u2 = NEWID()
DECLARE @u3 uniqueidentifier 
SET @u3 = NEWID()

INSERT INTO Users (Username, Person_ID, User_Password, Email)
VALUES ('colin314', @u1, 'password', 'c.anderson1830@gmail.com'),
('rDavis', @u2, 'password', 'rhen@gmail.com'),
('kDavis', @u3, 'password', 'kye@gmail.com');


INSERT INTO Persons (Person_ID, Username, First_Name, Last_Name,
Gender, Father_ID, Mother_ID, Spouse_ID)
VALUES
(@u1, 'colin314', 'Colin', 'Anderson', 'm', null, null, null),
(@u2, 'rDavis', 'Rhen', 'Davis', 'm', null, null, null),
(@u3, 'kDavis', 'Kye', 'Davis', 'm', null, null, null),
(NEWID(), 'colin314', 'Neil', 'Anderson', 'm', null, null, null),
(NEWID(), 'colin314', 'Ruth', 'Anderson', 'f', null, null, null)


SELECT * FROM Users
SELECT * FROM Persons