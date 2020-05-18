USE FamilyMapDb;
GO
IF NOT EXISTS (SELECT * FROM Users)
BEGIN
	INSERT INTO Users (Username, User_Password, Email)
	VALUES
	('colin314', '0123456789', 'c.anderson1830@gmail.com')
	INSERT INTO Persons (Person_ID, Username, First_Name, Last_Name, Gender, Father_ID, Mother_ID, Spouse_ID)
	VALUES 
	((SELECT Person_ID FROM Users WHERE Username = 'colin314'), 
		'colin314', 'Colin', 'Anderson', 'm', null, null, null)

	INSERT INTO Persons (Username, First_Name, Last_Name, Gender, Father_ID, Mother_ID, Spouse_ID)
	VALUES 
	('colin314', 'Neil', 'Anderson', 'm', null, null, null),
	('colin314', 'Ruth', 'Anderson', 'F', null, null, null);

	DECLARE @myID uniqueidentifier
	SET @myID = (SELECT Person_ID FROM Users WHERE Username = 'colin314')

	INSERT INTO Events (Username, Person_ID, Latitude, Longitude, Country, City, EventType, Event_Year)
	VALUES
	('colin314', @myID, '38.5816° N', '121.4944° W', 'United States', 'Sacramento', 'Birth', 1995);

END

SELECT * FROM Persons
SELECT * FROM Users
SELECT * FROM Events

USE master;
GO