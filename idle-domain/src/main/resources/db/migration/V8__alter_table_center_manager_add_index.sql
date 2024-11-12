-- V8__alter_table_center_manager_add_index.sql

ALTER TABLE center_manager
ADD UNIQUE INDEX idx_phone_number (phone_number);
