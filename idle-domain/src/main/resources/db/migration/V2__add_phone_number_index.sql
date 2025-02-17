-- V2__add_phone_number_index.sql

CREATE UNIQUE INDEX idx_phone_number ON center_manager(phone_number);
CREATE UNIQUE INDEX idx_phone_number ON carer(phone_number);