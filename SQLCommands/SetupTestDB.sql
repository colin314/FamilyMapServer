USE FamilyMapDb;
GO

DECLARE @u1 uniqueidentifier 
SET @u1 = NEWID()
DECLARE @u1 uniqueidentifier 
SET @u2 = NEWID()
DECLARE @u1 uniqueidentifier 
SET @u2 = NEWID()

INSERT INTO Users (Username, Person_ID, User_Password, Email)
VALUES ('colin314', NEWID(), 'password', 'c.anderson1830@gmail.com'),
('rDavis', NEWID(), 'password', 'rhen@gmail.com'),


INSERT INTO Persons (Person_ID, 


SELECT * FROM Users
SELECT * FROM Persons