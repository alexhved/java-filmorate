drop table if exists films cascade;
drop table if exists genres cascade;
drop table if exists genres_films cascade;
drop table if exists LIKES_USERS cascade;
drop table if exists MPA cascade;
drop table if exists USERS cascade;
drop table if exists FRIENDS cascade;
drop table if exists APPROVED_FRIENDS cascade;

create table if not exists users
(
    id   int auto_increment
        primary key,
    name      varchar(20) not null ,
    email     varchar(30) unique not null ,
    login     varchar(20) unique not null ,
    birthday date not null
);

create table if not exists mpa
(
    ID   INTEGER not null
        primary key,
    NAME CHARACTER VARYING(20)
);

create table if not exists FILMS
(
    ID           INTEGER auto_increment
        primary key,
    NAME         CHARACTER VARYING(20)
        unique,
    DESCRIPTION  CHARACTER VARYING(200),
    RATE INTEGER,
    RELEASE_DATE DATE,
    DURATION     INTEGER not null,
    MPA_ID       INTEGER,
    constraint FILMS_MPA_ID_FK
        foreign key (MPA_ID) references MPA
);

create table if not exists genres
(
    id    int auto_increment
        primary key,
    name varchar(20)
);

create table if not exists genres_films
(
    film_id  int,
    genre_id int,
    constraint GENRES_FILMS_FILMS_ID_FK
        foreign key (film_id) references FILMS,
    constraint GENRES_FILMS_GENRES_ID_FK
        foreign key (genre_id) references GENRES
);

create table if not exists likes_users
(
    film_id int,
    user_id int,
    constraint LIKES_USERS_FILMS_ID_FK
        foreign key (film_id) references FILMS,
    constraint LIKES_USERS_USERS_ID_FK
        foreign key (user_id) references USERS
);

create table if not exists friends
(
    user_id   int,
    friend_id int,
    constraint FRIENDS_USERS_USER_ID_FK
        foreign key (user_id) references USERS
);

create table if not exists APPROVED_FRIENDS
(
    USER_ID   INTEGER,
    FRIEND_ID INTEGER not null
        primary key
);

