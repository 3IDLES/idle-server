-- V2__create_table_chat_room.sql

-- Create table for chat room
CREATE TABLE chat_room (
    id BINARY(16) PRIMARY KEY,
    sender_id BINARY(16) NOT NULL,
    receiver_id BINARY(16) NOT NULL,
    entity_status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX (sender_id),
    INDEX (receiver_id)
)
