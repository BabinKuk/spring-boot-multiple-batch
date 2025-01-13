DROP TABLE coffee IF EXISTS;

CREATE TABLE coffee  (
    coffee_id INTEGER AUTO_INCREMENT NOT NULL PRIMARY KEY,
    brand VARCHAR(20),
    origin VARCHAR(20),
    characteristics VARCHAR(30)
);

DROP TABLE IF EXISTS person;

CREATE TABLE person  (
    person_id INTEGER AUTO_INCREMENT NOT NULL PRIMARY KEY,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    age INTEGER,
    is_active BOOLEAN
);