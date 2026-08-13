-- Runs once, on first initialisation of the Postgres volume.
-- `mvn -P integration test` points at a separate database so a test run can
-- drop and recreate schema without touching development data.
CREATE DATABASE centroid_finder_test;
