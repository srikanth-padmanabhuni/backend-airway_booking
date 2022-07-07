CREATE SEQUENCE hibernate_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE users(
	id BIGINT PRIMARY KEY,
	username TEXT UNIQUE,
	name TEXT NOT NULL,
	password TEXT,
	role TEXT,
	discountpoints DOUBLE(10, 4)
)

CREATE TABLE flights(
	id BIGINT PRIMARY KEY,
	flightnumber TEXT UNIQUE,
	flightname TEXT NOT NULL,
	fare DOUBLE(10, 4),
	origin TEXT,
	destination TEXT,
	arraival BIGINT,
	departure BIGINT
)

CREATE TABLE bookings(
	id BIGINT PRIMARY KEY,
	username NOT NULL FOREIGN KEY REFERENCES users(userName),
	flightnumber NOT NULL FOREIGN KEY REFERENCES flights(flightnumber),
	bookedon BIGINT,
	discountapplied DOUBLE(10, 4),
	bookingstatus TEXT
)