create table IF NOT EXISTS MPA
(
    MPA_ID INTEGER auto_increment primary key,
    MPA_NAME CHARACTER VARYING(50) not null
);

create table IF NOT EXISTS GENRES
(
    GENRE_ID INTEGER auto_increment primary key,
    GENRE_NAME CHARACTER VARYING(50)
);

create table IF NOT EXISTS FILMS
(
    FILM_ID      INTEGER auto_increment,
    FILM_NAME    CHARACTER VARYING(150) not null,
    DESCRIPTION  CHARACTER VARYING(200) not null,
    DURATION     INTEGER                not null,
    RELEASE_DATE DATE                   not null,
    MPA_ID       INTEGER                not null,
    constraint FILMS_PK
        primary key (FILM_ID),
    constraint FILMS_MPA_MPA_ID_FK
        foreign key (MPA_ID) references MPA
);

create table IF NOT EXISTS USERS
(
    USER_ID   INTEGER auto_increment,
    USER_NAME CHARACTER VARYING      not null,
    LOGIN     CHARACTER VARYING(50),
    EMAIL     CHARACTER VARYING(100) not null,
    BIRTHDAY  DATE                   not null,
    constraint USERS_PK
        primary key (USER_ID)
);


create table IF NOT EXISTS LIKES_LIST
(
    FILM_ID INTEGER not null,
    USER_ID INTEGER not null,
    constraint LIKES_LIST_FILMS_FILM_ID_FK
        foreign key (FILM_ID) references FILMS,
    constraint LIKES_LIST_USERS_USER_ID_FK
        foreign key (USER_ID) references USERS
);

create table IF NOT EXISTS FRIENDSHIP
(
USER_ID INTEGER NOT NULL REFERENCES users (USER_ID) ON DELETE CASCADE,
FRIEND_ID INTEGER NOT NULL REFERENCES users (USER_ID) ON DELETE CASCADE,
STATUS CHARACTER VARYING(30)
);

create table IF NOT EXISTS GENRE_LIST
(
    FILM_ID  INTEGER,
    GENRE_ID INTEGER,
    constraint GENRE_LIST_FILMS_FILM_ID_FK
        foreign key (FILM_ID) references FILMS,
    constraint GENRE_LIST_GENRES_GENRE_ID_FK
        foreign key (GENRE_ID) references GENRES
);