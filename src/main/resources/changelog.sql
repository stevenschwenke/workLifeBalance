--liquibase formatted sql

--changeset nvoxland:1
create table TIME_RECORDS (
    id int primary key,
    hours int
);
