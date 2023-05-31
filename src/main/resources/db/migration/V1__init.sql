CREATE TABLE IF NOT EXISTS users
(
    id         uuid primary key,
    username   varchar(36) not null,
    password   varchar(100) not null,
    email      varchar(50) unique,
    created_at timestamp default current_timestamp,
    updated_at timestamp default current_timestamp
);

CREATE TABLE IF NOT EXISTS roles
(
    id         bigserial primary key,
    name       varchar(50) not null,
    created_at timestamp default current_timestamp,
    updated_at timestamp default current_timestamp
);

CREATE TABLE IF NOT EXISTS users_roles
(
    user_id uuid not null references users (id),
    role_id bigint not null references roles (id),
    primary key (user_id, role_id)
);