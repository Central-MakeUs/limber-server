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

-- limberdb 데이터베이스 사용
\c limberdb;

-- FOCUS_TYPE 테이블에 데이터 삽입
DO
$$
BEGIN
   IF NOT EXISTS (
       SELECT 1 FROM FOCUS_TYPE WHERE title = '학습'
   ) THEN
       INSERT INTO FOCUS_TYPE (title, user_id, default_flag, del_flag, sequence, reg_dt, reg_id)
       VALUES
           ('학습', NULL, 'Y', 'N', 1, CURRENT_TIMESTAMP, 'admin'),
           ('업무', NULL, 'Y', 'N', 2, CURRENT_TIMESTAMP, 'admin'),
           ('회의', NULL, 'Y', 'N', 3, CURRENT_TIMESTAMP, 'admin'),
           ('작업', NULL, 'Y', 'N', 4, CURRENT_TIMESTAMP, 'admin'),
           ('독서', NULL, 'Y', 'N', 5, CURRENT_TIMESTAMP, 'admin');
END IF;
END
$$;
