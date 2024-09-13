CREATE TABLE IF NOT EXISTS usuario (
  username VARCHAR(250) NOT NULL,
  password VARCHAR(250) NOT NULL
);

-- Password: password
INSERT INTO usuario (username, password)  VALUES ('username', '$2a$10$DF41s08EU6eHZdCo.EOaWumgZoXts84oaW1AVufCAqhzrRsKLSrJe');