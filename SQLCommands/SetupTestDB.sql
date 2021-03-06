USE FamilyMapDb_Tester;
GO

DECLARE @u1 uniqueidentifier 
SET @u1 = '62CC127E-477E-45D1-BF6A-B63FB89F075B'
DECLARE @u2 uniqueidentifier 
SET @u2 = '21E6B772-74F1-43D8-B7D3-9306F08CC838'
DECLARE @u3 uniqueidentifier 
SET @u3 = 'D8B340FC-F421-4472-8D08-FB28D3D5138F'

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