CREATE TABLE todo (
      id            SERIAL PRIMARY KEY,
      name          VARCHAR(100) NOT NULL,
      description   VARCHAR(100) NOT NULL,
      date_time     TIMESTAMP NOT NULL,
      status        boolean NOT NULL
);