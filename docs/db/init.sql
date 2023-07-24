create table if not exists feed
(
    id         bigint auto_increment primary key,
    writer_id  bigint       not null,
    age_range  varchar(16)  not null comment '나이범위 (ALL, PEER)',
    content    varchar(256) not null,
    genders    varchar(32)  not null default '[]',
    mbti_chars varchar(32)  not null default '[]',
    created_at datetime     not null default now(),
    updated_at datetime     not null default now() on update now()
);



create table if not exists favorite_feed
(
    id         bigint auto_increment primary key,
    feed_id    bigint   not null,
    user_id    bigint   not null,
    created_at datetime not null default now(),
    updated_at datetime not null default now() on update now()
);


create table if not exists feed_view_history
(
    id         bigint auto_increment primary key,
    feed_id    bigint   not null,
    user_id    bigint   not null,
    count      int      not null default 0,
    created_at datetime not null default now(),
    updated_at datetime not null default now() on update now()
);


create table reply
(
    id         bigint auto_increment primary key,
    feed_id    bigint       not null,
    writer_id  bigint       not null,
    content    varchar(256) not null,
    created_at datetime     not null default now(),
    updated_at datetime     not null default now() on update now()
);


create table peek_reply
(
    id         bigint auto_increment primary key,
    reply_id   bigint   not null,
    user_id    bigint   not null,
    created_at datetime not null default now(),
    updated_at datetime not null default now() on update now()
);


