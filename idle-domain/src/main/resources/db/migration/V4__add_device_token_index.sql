-- V4__add_device_token_index.sql
CREATE UNIQUE INDEX idx_unique_device_token ON device_token(device_token);