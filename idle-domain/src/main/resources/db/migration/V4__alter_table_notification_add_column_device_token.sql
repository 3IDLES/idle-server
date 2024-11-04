-- V4__alter_table_notification_add_column_device_token.sql

-- Add secondary index on deviceToken and userId columns
CREATE INDEX idx_user_id ON notification(user_id);
