-- V3__create_table_chat_message.sql

-- Create table for chat message
CREATE TABLE chat_message (
    id BINARY(16) PRIMARY KEY,
    room_id BINARY(16) NOT NULL,
    sender_id BINARY(16) NOT NULL,
    sender_type ENUM('USER', 'SYSTEM') NOT NULL,
    contents JSON NOT NULL,
    entity_status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX (room_id),
    INDEX (sender_id),
    INDEX (created_at)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

