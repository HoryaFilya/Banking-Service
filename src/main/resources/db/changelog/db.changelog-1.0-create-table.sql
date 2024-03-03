--liquibase formatted sql

--changeset mikhail:1
CREATE TABLE IF NOT EXISTS users
(
    id                   BIGSERIAL PRIMARY KEY,
    login                VARCHAR(64)  NOT NULL UNIQUE,
    password             VARCHAR(255) NOT NULL,
    current_balance      BIGINT       NOT NULL CHECK ( current_balance >= 0 ),
    max_possible_deposit BIGINT       NOT NULL CHECK ( current_balance >= 0 ),
    birthdate            DATE,
    firstname            VARCHAR(64)  NOT NULL,
    lastname             VARCHAR(64)  NOT NULL,
    patronymic           VARCHAR(64)  NOT NULL
);
--rollback DROP TABLE users;

--changeset mikhail:2
CREATE TABLE IF NOT EXISTS phones
(
    id        BIGSERIAL PRIMARY KEY,
    telephone BIGINT NOT NULL UNIQUE,
    users_id  BIGINT NOT NULL REFERENCES users (id)
);
--rollback DROP TABLE phones;

--changeset mikhail:3
CREATE TABLE IF NOT EXISTS emails
(
    id       BIGSERIAL PRIMARY KEY,
    email    VARCHAR(64) NOT NULL UNIQUE,
    users_id BIGINT       NOT NULL REFERENCES users (id)
);
--rollback DROP TABLE emails;