CREATE ROLE limber WITH LOGIN PASSWORD 'limber1234';
CREATE DATABASE limberdb OWNER limber;
GRANT ALL PRIVILEGES ON DATABASE limberdb TO limber;