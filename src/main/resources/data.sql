drop table if exists users_roles, users, roles;

create table if not exists users
(
    id       bigint auto_increment primary key,
    username varchar(255) not null unique,
    password varchar(255) not null
);

create table if not exists roles
(
    id   bigint primary key auto_increment,
    name varchar(255) unique
);

insert into roles(name)
values ('ROLE_USER');
insert into roles(name)
values ('ROLE_ADMIN');
insert into roles(name)
values ('ROLE_SALES');

create table if not exists users_roles
(
    user_id bigint,
    role_id bigint,
    primary key (user_id, role_id)
);

INSERT INTO users (username, password)
VALUES ('user', '$2y$10$X3hj3qZ1nb3ok6.CBK1Tu.J0UShodeFkC7S4gKEAd7ZfipFVjJq.q');
INSERT INTO users (username, password)
VALUES ('admin', '$2y$10$nR2NSC8mf1XQyoNAskL85OfNIxATrBVgwjEbbOJ1O1Fte0xrzNkLK');
INSERT INTO users (username, password)
VALUES ('sales manager', '$2y$10$ETXNYBsiykCidxpZAqcUxutqzDfdNsij4jZ3SkxPzZx5uajNsjj6q');

alter table users_roles
    add foreign key (user_id) references users (id);
alter table users_roles
    add foreign key (role_id) references roles (id);

insert into users_roles (user_id, role_id)
VALUES (1, 1);
insert into users_roles (user_id, role_id)
VALUES (2, 2);
insert into users_roles (user_id, role_id)
VALUES (2, 1);
insert into users_roles (user_id, role_id)
VALUES (2, 3);
insert into users_roles (user_id, role_id)
VALUES (3, 1);
insert into users_roles (user_id, role_id)
VALUES (3, 3);