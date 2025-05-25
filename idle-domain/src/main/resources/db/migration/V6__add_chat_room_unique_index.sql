-- V6__add_chat_room_unique_index.sql

ALTER TABLE chat_room
    ADD UNIQUE KEY uniq_carer_center (carer_id, center_id);