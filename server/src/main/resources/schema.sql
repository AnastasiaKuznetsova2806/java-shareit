create table if not exists USERS
(
    ID_USER   bigint generated by default as identity not null,
    NAME_USER varchar(255)                            not null,
    EMAIL     varchar(512)                            not null,
    constraint PK_USERS primary key (ID_USER),
    constraint UQ_USERS_EMAIL unique (EMAIL)
);

create table if not exists REQUESTS
(
    ID_REQUEST   bigint generated by default as identity not null,
    DESCRIPTION  varchar(1024)                           not null,
    REQUESTOR_ID bigint                                  not null,
    CREATED      timestamp without time zone,
    constraint PK_REQUESTS primary key (ID_REQUEST),
    constraint FK_REQUESTS_USERS_REQUESTOR_ID foreign key (REQUESTOR_ID)
        references USERS on delete cascade
);

create table if not exists ITEMS
(
    ID_ITEM      bigint generated by default as identity not null,
    NAME_ITEM    varchar(255)                            not null,
    DESCRIPTION  varchar(1024)                           not null,
    IS_AVAILABLE boolean                                 not null,
    OWNER_ID     bigint                                  not null,
    REQUEST_ID   bigint,
    constraint PK_ITEMS primary key (ID_ITEM),
    constraint FK_ITEMS_USERS_OWNER_ID foreign key (OWNER_ID)
        references USERS on delete cascade,
    constraint FK_ITEMS_REQUESTS_REQUEST_ID foreign key (REQUEST_ID)
        references REQUESTS on delete cascade
);

create table if not exists BOOKINGS
(
    ID_BOOKING bigint generated by default as identity not null,
    START_DATE timestamp without time zone,
    END_DATE   timestamp without time zone,
    ITEM_ID    bigint                                  not null,
    BOOKER_ID  bigint                                  not null,
    STATUS     bigint                                  not null,
    constraint PK_BOOKINGS primary key (ID_BOOKING),
    constraint FK_BOOKINGS_ITEMS_ITEM_ID foreign key (ITEM_ID)
        references ITEMS on delete cascade,
    constraint FK_BOOKINGS_USERS_BOOKER_ID foreign key (BOOKER_ID)
        references USERS on delete cascade
);

create table if not exists COMMENTS
(
    ID_COMMENT   bigint generated by default as identity not null,
    TEXT_COMMENT varchar(2024)                           not null,
    ITEM_ID      bigint                                  not null,
    AUTHOR_ID    bigint                                  not null,
    CREATED      timestamp without time zone,
    constraint PK_COMMENTS primary key (ID_COMMENT),
    constraint FK_COMMENTS_ITEMS_ITEM_ID foreign key (ITEM_ID)
        references ITEMS on delete cascade,
    constraint FK_COMMENTS_USERS_AUTHOR_ID foreign key (AUTHOR_ID)
        references USERS on delete cascade
);