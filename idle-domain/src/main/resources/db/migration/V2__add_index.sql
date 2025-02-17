-- V2__add_index.sql

-- Create index for chat room
CREATE INDEX idx_sender_id ON chat_room(sender_id);
CREATE INDEX idx_receiver_id ON chat_room(receiver_id);

CREATE INDEX idx_receiver_id ON notification(receiver_id);
CREATE INDEX idx_created_at ON notification(created_at);

CREATE INDEX idx_room_id ON chat_message(room_id);
CREATE INDEX idx_sender_id ON chat_message(sender_id);

CREATE UNIQUE INDEX idx_phone_number ON center_manager(phone_number);
CREATE UNIQUE INDEX idx_phone_number ON carer(phone_number);