DO
$$
BEGIN
   IF NOT EXISTS (
       SELECT FROM pg_catalog.pg_roles WHERE rolname = 'limber'
   ) THEN
CREATE ROLE limber WITH LOGIN PASSWORD 'limber1234';
END IF;
END
$$;

-- DB도 중복 방지
DO
$$
BEGIN
   IF NOT EXISTS (
       SELECT FROM pg_database WHERE datname = 'limberdb'
   ) THEN
       CREATE DATABASE limberdb OWNER limber;
END IF;
END
$$;

GRANT ALL PRIVILEGES ON DATABASE limberdb TO limber;
