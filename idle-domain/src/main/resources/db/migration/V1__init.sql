-- V1__init.sql

-- Create tables
create table applys (
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                        carer_id BINARY(16) NOT NULL,
                        id BINARY(16) NOT NULL,
                        job_posting_id BINARY(16) NOT NULL,
                        apply_method_type ENUM ('CALLING','APP') NOT NULL,
                        entity_status VARCHAR(255),
                        PRIMARY KEY (id)
) engine=InnoDB;

create table carer (
                       birth_year INT NOT NULL,
                       experience_year INT,
                       latitude DECIMAL(14, 10) NOT NULL,
                       longitude DECIMAL(14, 10) NOT NULL,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                       id BINARY(16) NOT NULL,
                       introduce TEXT,
                       lot_number_address VARCHAR(255) NOT NULL,
                       name VARCHAR(255) NOT NULL,
                       phone_number VARCHAR(255) NOT NULL,
                       profile_image_url VARCHAR(255),
                       road_name_address VARCHAR(255) NOT NULL,
                       speciality TEXT,
                       carer_account_status VARCHAR(20) NOT NULL,
                       entity_status VARCHAR(255),
                       gender VARCHAR(255) NOT NULL,
                       job_search_status VARCHAR(20) NOT NULL,
                       location POINT SRID 4326 NOT NULL,
                       PRIMARY KEY (id)
) engine=InnoDB;

create table center (
                        latitude DECIMAL(14, 10) NOT NULL,
                        longitude DECIMAL(14, 10) NOT NULL,
                        id BINARY(16) NOT NULL,
                        business_registration_number VARCHAR(255) NOT NULL,
                        center_name VARCHAR(255) NOT NULL,
                        detailed_address VARCHAR(255) NOT NULL,
                        introduce TEXT,
                        lot_number_address VARCHAR(255) NOT NULL,
                        office_number VARCHAR(255) NOT NULL,
                        profile_image_url VARCHAR(255),
                        road_name_address VARCHAR(255) NOT NULL,
                        PRIMARY KEY (id)
) engine=InnoDB;

create table center_manager (
                                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                id BINARY(16) NOT NULL,
                                center_business_registration_number VARCHAR(255) NOT NULL,
                                identifier VARCHAR(255) NOT NULL,
                                name VARCHAR(255) NOT NULL,
                                password VARCHAR(255) NOT NULL,
                                phone_number VARCHAR(255) NOT NULL,
                                entity_status VARCHAR(255),
                                status VARCHAR(255) NOT NULL,
                                PRIMARY KEY (id)
) engine=InnoDB;

create table chat_message (
                              created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                              updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                              id BINARY(16) NOT NULL,
                              chat_room_id BINARY(16) NOT NULL,
                              sender_id BINARY(16) NOT NULL,
                              receiver_id BINARY(16) NOT NULL,
                              content TEXT NOT NULL,
                              is_read BIT NOT NULL,
                              entity_status VARCHAR(255),
                              PRIMARY KEY (id)
) ENGINE=InnoDB;

create table chat_room (
                           created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                           updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                           id BINARY(16) NOT NULL,
                           carer_id BINARY(16) NOT NULL,
                           center_id BINARY(16) NOT NULL,
                           entity_status VARCHAR(255),
                           PRIMARY KEY (id)
) engine=InnoDB;

create table crawled_job_posting (
                                     created_at DATE DEFAULT(CURRENT_DATE),
                                     apply_deadline VARCHAR(255),
                                     apply_method VARCHAR(255),
                                     center_address VARCHAR(255),
                                     center_name VARCHAR(255),
                                     client_address VARCHAR(255),
                                     content TEXT,
                                     direct_url TEXT,
                                     pay_info VARCHAR(255),
                                     recruitment_process VARCHAR(255),
                                     required_document VARCHAR(255),
                                     title VARCHAR(255),
                                     work_schedule VARCHAR(255),
                                     work_time VARCHAR(255),
                                     entity_status VARCHAR(255),
                                     location POINT SRID 4326,
                                     id BINARY(16) NOT NULL,
                                     PRIMARY KEY (id)
) engine=InnoDB;

create table deleted_user_info (
                                   deleted_at datetime(6) NOT NULL,
                                   id BINARY(16) NOT NULL,
                                   phone_number VARCHAR(255) NOT NULL,
                                   reason VARCHAR(255) NOT NULL,
                                   role ENUM ('CENTER','CARER') NOT NULL,
                                   PRIMARY KEY (id)
) engine=InnoDB;

create table device_token (
                              created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                              updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                              id BINARY(16) NOT NULL,
                              user_id BINARY(16),
                              device_token VARCHAR(255),
                              entity_status VARCHAR(255),
                              user_type VARCHAR(255),
                              PRIMARY KEY (id)
) engine=InnoDB;

create table job_posting (
                             apply_deadline date,
                             birth_year INT NOT NULL,
                             care_level INT NOT NULL,
                             is_bowel_assistance BIT NOT NULL,
                             is_experience_preferred BIT NOT NULL,
                             is_meal_assistance BIT NOT NULL,
                             is_walking_assistance BIT NOT NULL,
                             pay_amount int NOT NULL,
                             weight int,
                             created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                             updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                             center_id BINARY(16) NOT NULL,
                             id BINARY(16) NOT NULL,
                             client_name VARCHAR(255) NOT NULL,
                             disease TEXT,
                             end_time VARCHAR(255) NOT NULL,
                             extra_requirement TEXT,
                             lot_number_address VARCHAR(255) NOT NULL,
                             road_name_address VARCHAR(255) NOT NULL,
                             start_time VARCHAR(255) NOT NULL,
                             apply_deadline_type ENUM ('LIMITED','UNLIMITED') NOT NULL,
                             entity_status VARCHAR(255),
                             gender ENUM ('MAN','WOMAN') NOT NULL,
                             job_posting_status ENUM ('IN_PROGRESS','COMPLETED') NOT NULL,
                             location POINT SRID 4326 NOT NULL,
                             mental_status ENUM ('NORMAL','EARLY_STAGE','OVER_MIDDLE_STAGE') NOT NULL,
                             pay_type ENUM ('HOURLY','WEEKLY','MONTHLY') NOT NULL,
                             PRIMARY KEY (id)
) engine=InnoDB;

create table job_posting_apply_method (
                                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                          updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                          id BINARY(16) NOT NULL,
                                          job_posting_id BINARY(16) NOT NULL,
                                          apply_method ENUM ('CALLING','APP') NOT NULL,
                                          entity_status VARCHAR(255),
                                          PRIMARY KEY (id)
) engine=InnoDB;

create table job_posting_favorite (
                                      created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                      updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                      carer_id BINARY(16) NOT NULL,
                                      id BINARY(16) NOT NULL,
                                      job_posting_id BINARY(16) NOT NULL,
                                      entity_status VARCHAR(255),
                                      job_posting_type ENUM ('WORKNET','CAREMEET') NOT NULL,
                                      PRIMARY KEY (id)
) engine=InnoDB;

create table job_posting_life_assistance (
                                             created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                             updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                             id BINARY(16) NOT NULL,
                                             job_posting_id BINARY(16) NOT NULL,
                                             entity_status VARCHAR(255),
                                             life_assistance ENUM ('CLEANING','LAUNDRY','WALKING','HEALTH','TALKING','NONE') NOT NULL,
                                             PRIMARY KEY (id)
) engine=InnoDB;

create table job_posting_weekday (
                                     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                     updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                     id BINARY(16) NOT NULL,
                                     job_posting_id BINARY(16) NOT NULL,
                                     entity_status VARCHAR(255),
                                     weekday ENUM ('MONDAY','TUESDAY','WEDNESDAY','THURSDAY','FRIDAY','SATURDAY','SUNDAY') NOT NULL,
                                     PRIMARY KEY (id)
) engine=InnoDB;

create table notification (
                              is_read VARCHAR(255),
                              created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                              updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                              id BINARY(16) NOT NULL,
                              receiver_id BINARY(16),
                              body VARCHAR(255),
                              image_url VARCHAR(255),
                              notification_details_json json,
                              title VARCHAR(255),
                              entity_status VARCHAR(255),
                              notification_type ENUM ('APPLICANT','CENTER_AUTHENTICATION','NEW_JOB_POSTING'),
                              PRIMARY KEY (id)
) engine=InnoDB;