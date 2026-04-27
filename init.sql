DO $$
BEGIN
   IF NOT EXISTS (SELECT FROM pg_catalog.pg_roles WHERE rolname = 'kindergarten') THEN
CREATE ROLE kindergarten LOGIN PASSWORD 'kindergarten';
END IF;
END
$$;

GRANT ALL PRIVILEGES ON DATABASE kindergarten TO kindergarten;

\c kindergarten
GRANT ALL ON SCHEMA public TO kindergarten;