drop table if exists DIRECTORS cascade;
drop table if exists film_director cascade;
drop table if exists FILMS cascade;
drop table if exists FILMS_GENRES cascade;
drop table if exists FILMS_LIKES cascade;
drop table if exists FRIENDS cascade;
drop table if exists GENRES cascade;
drop table if exists RATINGS cascade;
drop table if exists REVIEW cascade;
drop table if exists REVIEW_LIKES cascade;
drop table if exists USERS cascade;


create table DIRECTORS
(
    DIRECTOR_ID   INTEGER auto_increment
        primary key,
    DIRECTOR_NAME CHARACTER VARYING(60)
        constraint UC_DIRECTOR_NAME
            unique
);

create table GENRES
(
    GENRE_ID BIGINT auto_increment
        primary key,
    NAME     CHARACTER VARYING(30)
        constraint GENRE_NAME
            unique
);

create table RATINGS
(
    RATING_ID   INTEGER auto_increment
        primary key,
    RATING_NAME CHARACTER VARYING(10)
        constraint UC_RATING_NAME
            unique
);

create table FILMS
(
    FILM_ID      BIGINT auto_increment
        primary key,
    NAME         CHARACTER VARYING(150),
    DESCRIPTION  CHARACTER VARYING(500) not null,
    RELEASE_DATE DATE                   not null,
    DURATION     INTEGER                not null,
    RATING_ID    INTEGER
        references RATINGS,
    DIRECTOR_ID  INTEGER
        references DIRECTORS
);

create table FILMS_GENRES
(
    FILM_ID  BIGINT not null,
    GENRE_ID BIGINT not null,
    constraint GENRE_FILM
        primary key (FILM_ID, GENRE_ID),
    constraint GENRE_FILM_FILM_ID
        foreign key (FILM_ID) references FILMS
            on delete cascade,
    constraint GENRE_FILM_GENRE_ID
        foreign key (GENRE_ID) references GENRES
);

create table FILM_DIRECTOR
(
    FILM_ID     BIGINT  not null
        references FILMS,
    DIRECTOR_ID INTEGER not null,
    constraint FILM_DIRECTOR_DIRECTORS_DIRECTOR_ID_FK
        foreign key (DIRECTOR_ID) references DIRECTORS
);

create table USERS
(
    USER_ID   BIGINT auto_increment
        primary key,
    EMAIL     CHARACTER VARYING(50) not null
        constraint USER_EMAIL
            unique,
    LOGIN     CHARACTER VARYING(50) not null
        constraint USER_LOGIN
            unique,
    USER_NAME CHARACTER VARYING(50) not null,
    BIRTHDAY  DATE                  not null
);

create table FILMS_LIKES
(
    FILM_ID BIGINT not null,
    USER_ID BIGINT not null,
    constraint FILM_LIKE
        primary key (FILM_ID, USER_ID),
    constraint FILM_LIKE_FILM_ID
        foreign key (FILM_ID) references FILMS
            on delete cascade,
    constraint FILM_LIKE_USER_ID
        foreign key (USER_ID) references USERS
);

create table FRIENDS
(
    USER_ID   BIGINT not null,
    FRIEND_ID BIGINT not null,
    constraint FRIENDSHIP
        primary key (USER_ID, FRIEND_ID),
    constraint FRIENDSHIP_USER_ID1
        foreign key (USER_ID) references USERS
            on delete cascade,
    constraint FRIENDSHIP_USER_ID2
        foreign key (FRIEND_ID) references USERS
            on delete cascade
);

create table REVIEW
(
    REVIEW_ID          BIGINT auto_increment
        primary key,
    REVIEW_CONTENT     CHARACTER VARYING(1000) not null,
    REVIEW_IS_POSITIVE BOOLEAN                 not null,
    USER_ID            BIGINT                  not null
        references USERS
            on delete cascade,
    FILM_ID            BIGINT                  not null
        references FILMS
            on delete cascade,
    REVIEW_USEFUL      INTEGER
);

create table REVIEW_LIKES
(
    REVIEW_ID BIGINT not null
        references REVIEW
            on delete cascade,
    USER_ID   BIGINT not null
        references USERS
            on delete cascade,
    IS_LIKE   BOOLEAN
);


