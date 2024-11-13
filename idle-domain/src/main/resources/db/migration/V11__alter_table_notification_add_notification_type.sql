-- V11__alter_table_notification_add_notification_type.sql

ALTER TABLE notification
    MODIFY COLUMN notification_type ENUM('APPLICANT', 'NEW_JOB_POSTING', 'CENTER_AUTHENTICATION')
