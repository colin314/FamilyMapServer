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
	Latitude float(4),
	Longitude float(4),
	Country nvarchar(MAX),
	City nvarchar(MAX),
	EventType nvarchar(MAX),
	Event_Year smallint
	)

CREATE TABLE AuthTokens (
	Token nvarchar(100) NOT NULL UNIQUE,
	Username nvarchar(100) NOT NULL
	)
USE master;
GO