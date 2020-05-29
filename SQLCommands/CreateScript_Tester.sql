USE master;
GO

IF (EXISTS (SELECT name FROM master.dbo.sysdatabases WHERE name = 'FamilyMapDb_Tester'))
	BEGIN
		ALTER DATABASE FamilyMapDb_Tester SET single_user WITH ROLLBACK IMMEDIATE;
		DROP DATABASE FamilyMapDb_Tester;
	END

CREATE DATABASE FamilyMapDb_Tester;
GO
USE FamilyMapDb_Tester;
GO

CREATE TABLE Users (
	Username NVARCHAR(100) UNIQUE,
	Person_ID nvarchar(max),
	User_Password NVARCHAR(50) NOT NULL,
	Email NVARCHAR(100) NOT NULL
	)

CREATE TABLE Persons (
	Person_ID nvarchar(100) UNIQUE,
	Username NVARCHAR(100) NOT NULL,
	First_Name NVARCHAR(100),
	Last_Name NVARCHAR(100),
	Gender NVARCHAR(1) NOT NULL,
	Father_ID nvarchar(100),
	Mother_ID nvarchar(100),
	Spouse_ID nvarchar(100),
	);

CREATE TABLE Events (
	Event_ID nvarchar(100) UNIQUE,
	Username nvarchar(max),
	Person_ID nvarchar(max) NOT NULL,
	Latitude float(16),
	Longitude float(16),
	Country nvarchar(MAX),
	City nvarchar(MAX),
	EventType nvarchar(MAX),
	Event_Year smallint
	)

CREATE TABLE AuthTokens (
	Token nvarchar(100) NOT NULL UNIQUE,
	Username nvarchar(100) NOT NULL
	)
GO

DECLARE @u1 nvarchar(max) 
SET @u1 = '62CC127E-477E-45D1-BF6A-B63FB89F075B'
DECLARE @u2 nvarchar(max) 
SET @u2 = '21E6B772-74F1-43D8-B7D3-9306F08CC838'
DECLARE @u3 nvarchar(max) 
SET @u3 = 'D8B340FC-F421-4472-8D08-FB28D3D5138F'
DECLARE @u4 nvarchar(max)
SET @u4 = 'c0eb66bb-8ff9-4131-bef9-6090100dc1c2'
DECLARE @u5 nvarchar(max)
SET @u5 = '27e87f19-380a-4213-a38f-13e2529ff114'
DECLARE @u6 nvarchar(max)
SET @u6 = '8d1e3801-5cdc-4011-8884-bd34f9eadbc7'
DECLARE @u7 nvarchar(max)
SET @u7 = '44b4c8e6-8261-42c0-8779-c646433b281a'
DECLARE @u8 nvarchar(max)
SET @u8 = 'ffae4e6f-bae6-4f03-91c2-f8fbdd780c0c'

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
(@u5, 'colin314', 'Neil', 'Anderson', 'm', null, null, null),
(@u6, 'colin314', 'Ruth', 'Anderson', 'f', null, null, null)

INSERT INTO Events (Event_ID, Username, Person_ID, Latitude, Longitude, Country,
City, EventType, Event_Year)
VALUES
(@u7, 'colin314', @u5, 150.5, 160.7, 'United States', 'Lodi', 'Birth', 1962),
(@u8, 'colin314', @u5, 150.5, 160.7, 'United States', 'Provo', 'Marriage', 1962)

INSERT INTO AuthTokens (Token, Username)
VALUES
(@u4, 'colin314')

GO

SELECT * FROM Users
SELECT * FROM Persons
SELECT * FROM Events
SELECT * FROM AuthTokens

USE master;
GO