CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

SET timezone = 'UTC';

DO $$
BEGIN
  RAISE NOTICE 'Finance API database initialized successfully';
END $$;
