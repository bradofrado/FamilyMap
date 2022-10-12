DROP TABLE IF EXISTS User;
DROP TABLE IF EXISTS Person;
DROP TABLE IF EXISTS Event;
DROP TABLE IF EXISTS AuthToken;

CREATE TABLE User (
	[username] VARCHAR(100) NOT NULL,
	[password] VARCHAR(100) NOT NULL,
	[email] VARCHAR(100) NOT NULL,
	[firstName] VARCHAR(100) NOT NULL,
	[lastName] VARCHAR(100) NOT NULL,
	[gender] CHAR(1) NOT NULL,
	[personID] VARCHAR(100) NOT NULL);

CREATE TABLE Person (
	[personID] VARCHAR(100) NOT NULL,
	[associatedUsername] VARCHAR(100) NOT NULL,
	[firstName] VARCHAR(100) NOT NULL,
	[lastName] VARCHAR(100) NOT NULL,
	[gender] CHAR(1) NOT NULL,
	[fatherID] VARCHAR(100),
	[motherID] VARCHAR(100),
	[spouseID] VARCHAR(100));

CREATE TABLE Event (
    [eventID] VARCHAR(100) NOT NULL,
    [associatedUsername] VARCHAR(100) NOT NULL,
    [personID] VARCHAR(100) NOT NULL,
    [latitude] FLOAT NOT NULL,
    [longitude] FLOAT NOT NULL,
    [country] VARCHAR(100) NOT NULL,
    [city] VARCHAR(100) NOT NULL,
    [eventType] VARCHAR(100) NOT NULL,
    [year] INT NOT NULL);

CREATE TABLE AuthToken (
    [authtoken] VARCHAR(100) NOT NULL,
    [username] VARCHAR(100) NOT NULL);