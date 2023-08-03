create table feed
(
    id             bigint auto_increment primary key,
    writer_id      bigint                                not null,
    age_range      varchar(16)                           not null comment '나이범위 (ALL, ZERO_TO_TEN, TEEN, TWENTY, THIRTY, FORTY, FIFTY, SIXTY, SEVENTY, EIGHTY, NINETY)',
    content        varchar(256)                          not null,
    visible_target varchar(16)                           not null comment '피드 노출대상 (CHILD, ADULT)',
    genders        varchar(32) default '[]'              not null,
    mbti_chars     varchar(32) default '[]'              not null,
    created_at     datetime    default CURRENT_TIMESTAMP not null,
    updated_at     datetime    default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP
);



create table if not exists favorite_feed
(
    id         bigint auto_increment primary key,
    feed_id    bigint   not null,
    user_id    bigint   not null,
    created_at datetime not null default now(),
    updated_at datetime not null default now() on update now()
);

create unique index favorite_feed_feed_id_user_id_uindex
    on favorite_feed (feed_id, user_id);

create table if not exists feed_view_history
(
    id         bigint auto_increment primary key,
    feed_id    bigint   not null,
    user_id    bigint   not null,
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


create table if not exists block_feed
(
    id         bigint auto_increment primary key,
    feed_id    bigint   not null,
    user_id    bigint   not null,
    created_at datetime not null default now(),
    updated_at datetime not null default now() on update now()
);

create unique index feed_id_user_id_uniq
    on block_feed (feed_id, user_id);


create table if not exists block_reply
(
    id         bigint auto_increment primary key,
    reply_id   bigint   not null,
    user_id    bigint   not null,
    created_at datetime not null default now(),
    updated_at datetime not null default now() on update now()
);

create unique index reply_id_user_id_uniq
    on block_reply (reply_id, user_id);
