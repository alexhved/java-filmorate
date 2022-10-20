create table if not exists users
(
    id   int auto_increment
        primary key,
    name      varchar(20) not null ,
    email     varchar(30) not null ,
    login     varchar(20) not null ,
    birthday date not null
);
create table if not exists films
(
    id           int auto_increment
        primary key,
    name        varchar(20),
    description  varchar(200),
    release_date date,
    duration     int     not null,
    rating_mpa   varchar not null
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
    film_id int not null,
    user_id int not null,
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