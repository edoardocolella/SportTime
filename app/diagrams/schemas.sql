CREATE TABLE USER(
	id TEXT NOT NULL,
	name TEXT NOT NULL,
	surname TEXT NOT NULL,
	nickname TEXT NOT NULL,
	description TEXT NOT NULL,
	gender TEXT NOT NULL,
	birthdate DATE NOT NULL,
	email TEXT NOT NULL,
	phoneNumber TEXT NOT NULL,
	location TEXT NOT NULL,
	imageUri TEXT,
	monday TEXT,
	tuesday TEXT,
	wednesday TEXT,
	thursday TEXT,
	friday TEXT,
	saturday TEXT,
	sunday TEXT,
	PRIMARY KEY (id)
);

CREATE TABLE PLAYGROUND(
	id TEXT NOT NULL,
	name TEXT NOT NULL,
	description TEXT NOT NULL,
	location TEXT NOT NULL,
	sport TEXT NOT NULL,
	price DOUBLE NOT NULL,
	PRIMARY KEY (id)
);

CREATE TABLE RESERVATION(
	id TEXT NOT NULL,
	idUser TEXT NOT NULL,
	idPlayground TEXT NOT NULL,
	date DATE NOT NULL,
	startTime TIME NOT NULL,
	endTime TIME NOT NULL,
	PRIMARY KEY (id)
);

CREATE TABLE USER_HAS_SPORT(
	idUser TEXT NOT NULL,
	idSport TEXT NOT NULL,
	level TEXT NOT NULL,
	PRIMARY KEY (idUser, idSport)
);

