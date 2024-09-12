BEGIN;
-- CREATE TYPE furnish_type AS ENUM (
--     'DESIGNER', 'NONE', 'FINE', 'BAD', 'LITTLE'
-- );
--
-- CREATE TYPE transport_type AS ENUM (
--      'FEW', 'NONE', 'LITTLE', 'NORMAL'
-- );

CREATE TABLE IF NOT EXISTS users (
    id_user SERIAL PRIMARY KEY,
    username VARCHAR(40) UNIQUE NOT NULL,
    password VARCHAR(64) NOT NULL
);

CREATE TABLE IF NOT EXISTS house (
    name_house VARCHAR PRIMARY KEY,
    count_flats_on_floor INT NOT NULL CHECK (count_flats_on_floor > 0),
    build_year integer NOT NULL CHECK(build_year > 0)
);
CREATE TABLE IF NOT EXISTS flat (
    id SERIAL PRIMARY KEY,
    name VARCHAR NOT NULL CONSTRAINT not_empty_name CHECK(length(name) > 0),
    coordinate_x integer NOT NULL,
    coordinate_y integer NOT NULL,
    creationdate date NOT NULL,
    area integer NOT NULL CHECK (area > 0),
    numbers_of_rooms integer NOT NULL CHECK (numbers_of_rooms > 0),
    numbers_of_bathrooms integer NOT NULL CHECK (numbers_of_bathrooms > 0),
    furnish_type VARCHAR NOT NULL,
    transport_type VARCHAR NOT NULL,
    name_house_flat VARCHAR REFERENCES house(name_house) ON DELETE SET NULL,
    creator_id INT NOT NULL REFERENCES users(id_user) ON DELETE CASCADE
);
INSERT INTO house(name_house, count_flats_on_floor, build_year)
VALUES ('Белорусская 6', 300, 2013),
       ('Вяземский переулок', 500, 1980);
INSERT INTO users(username, password)
VALUES ('Айгуль', '234bs'),
       ('Кот', '234fv3bs');
INSERT INTO flat(name, coordinate_x, coordinate_y, creationdate, area, numbers_of_rooms, numbers_of_bathrooms, furnish_type, transport_type, name_house_flat, creator_id)
VALUES ('306', 300, 203, '2023-12-12', 50, 3, 1, 'FINE', 'FEW', 'Белорусская 6', 1);
END;