-- 롤이 존재하지 않으면 생성
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

-- DB가 존재하지 않으면 생성
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

-- DB에 권한 부여
GRANT ALL PRIVILEGES ON DATABASE limberdb TO limber;

