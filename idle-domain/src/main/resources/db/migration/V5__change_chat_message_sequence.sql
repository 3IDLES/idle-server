-- V5__change_chat_message_sequence.sql

ALTER TABLE chat_message
DROP COLUMN is_read;

ALTER TABLE chat_message
    ADD COLUMN sequence BIGINT NOT NULL;
