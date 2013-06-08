--liquibase formatted sql

--changeset steven:1

create table DAY_RECORDS (
	id int primary key,
	date date not null
);

CREATE SEQUENCE DAY_RECORDS_SEQ AS BIGINT START WITH 1 INCREMENT BY 1;

create table TIME_RECORDS (
    id int primary key,
    hours int not null,
    day_record_id int not null,
    foreign key (day_record_id) references DAY_RECORDS (id)
);

CREATE SEQUENCE TIME_RECORDS_SEQ AS BIGINT START WITH 1 INCREMENT BY 1;
