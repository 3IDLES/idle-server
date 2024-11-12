-- V9__alter_table_carer_add_index.sql

ALTER TABLE carer
ADD UNIQUE INDEX idx_phone_number (phone_number);
