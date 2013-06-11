--liquibase formatted sql

--changeset steven:1

create table DAY_RECORDS (
	id int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
	date date not null unique
);


create table TIME_RECORDS (
    id int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    hours int not null,
    day_record_id int not null,
    aspect VARCHAR(40) not null,
    foreign key (day_record_id) references DAY_RECORDS (id)
);
