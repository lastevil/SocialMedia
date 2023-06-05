CREATE TABLE IF NOT EXISTS posts
(
    id         uuid primary key,
    user_id     uuid not null references users (id),
    header     text not null,
    body       text,
    photo_link text,
    photo_type varchar(100),
    created_at timestamp default current_timestamp
);

CREATE TABLE IF NOT EXISTS subscribers
(
    id          bigserial primary key,
    sender_id   uuid not null references users (id),
    receiver_id uuid not null references users (id),
    is_friends  boolean not null
);

CREATE TABLE IF NOT EXISTS messages
(
    id          bigserial primary key,
    sender_id   uuid not null references users (id),
    receiver_id uuid not null references users (id),
    message     text,
    created_at  timestamp default current_timestamp
);