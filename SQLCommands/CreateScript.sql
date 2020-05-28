USE master;
GO

IF (EXISTS (SELECT name FROM master.dbo.sysdatabases WHERE name = 'FamilyMapDb'))
	BEGIN
		ALTER DATABASE FamilyMapDb SET single_user WITH ROLLBACK IMMEDIATE;
		DROP DATABASE FamilyMapDb;
	END

CREATE DATABASE FamilyMapDb;
GO
USE FamilyMapDb;
GO

CREATE TABLE Users (
	Username NVARCHAR(100),
	Person_ID UNIQUEIDENTIFIER NOT NULL DEFAULT NEWID(),
	PRIMARY KEY (Username),
	User_Password NVARCHAR(50) NOT NULL,
	Email NVARCHAR(100) NOT NULL
	)

CREATE TABLE Persons (
	Person_ID UNIQUEIDENTIFIER DEFAULT NEWID(),
	Username NVARCHAR(100) NOT NULL,
	First_Name NVARCHAR(100) NOT NULL,
	Last_Name NVARCHAR(100) NOT NULL,
	Gender NVARCHAR(1) NOT NULL,
	Father_ID UNIQUEIDENTIFIER,
	Mother_ID UNIQUEIDENTIFIER,
	Spouse_ID UNIQUEIDENTIFIER,
	PRIMARY KEY (Person_ID),
	CONSTRAINT CHK_Gender CHECK (Gender='m' OR Gender='f')
	);

CREATE TABLE Events (
	Event_ID uniqueidentifier DEFAULT NEWID(),
		PRIMARY KEY (Event_ID),
	Username nvarchar(100) NOT NULL,
	Person_ID uniqueidentifier NOT NULL,
	Latitude nvarchar(MAX),
	Longitude nvarchar(MAX),
	Country nvarchar(MAX),
	City nvarchar(MAX),
	EventType nvarchar(MAX),
	Event_Year smallint
	)

CREATE TABLE AuthTokens (
	Token uniqueidentifier NOT NULL UNIQUE,
	Username nvarchar(100) NOT NULL
	)
USE master;
GO