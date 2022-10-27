select *
from USERS
         join FRIENDS F on USERS.ID = F.USER_ID;

select FRIEND_ID
from FRIENDS
where USER_ID = 1;

select * from USERS where ID in (
    select FRIEND_ID
    from FRIENDS
    where USER_ID = 1
);

select *
from FILMS f, MPA m
where f.MPA_ID=m.ID and f.ID = 1;

select *
from GENRES_FILMS
where FILM_ID = 1;

select *
from films f, MPA m
where f.MPA_ID = m.ID;

select g.ID, g.NAME
from GENRES_FILMS as gf
         join GENRES as g on gf.GENRE_ID = g.ID
where gf.FILM_ID = 1;