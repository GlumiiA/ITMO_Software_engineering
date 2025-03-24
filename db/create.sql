BEGIN;

CREATE TABLE attempts (
                          id SERIAL PRIMARY KEY,
                          is_hit BOOLEAN NOT NULL,
                          x FLOAT NOT NULL,
                          y FLOAT NOT NULL,
                          r FLOAT NOT NULL,
                          curTime DATE NOT NULL,
                          executeTime BIGINT NOT NULL
);

COMMIT;