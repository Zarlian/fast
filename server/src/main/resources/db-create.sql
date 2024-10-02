DROP TABLE IF EXISTS locations, users, teleporters, favourites, permissions, user_permissions, groups, group_members, transactions, user_transactions, trips, teleporter_settings CASCADE;

create table locations (
    location_id bigint generated always as identity primary key,
    longitude double not null,
    latitude double not null
);

CREATE TABLE users (
   adria_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
   name VARCHAR(255) NOT NULL,
   address VARCHAR(255) NOT NULL,
   current_location VARCHAR(255) NOT NULL,
   profile_picture VARCHAR(255),
   token VARCHAR(256),
   foreign key (current_location) references locations(location_id)
);

CREATE TABLE teleporters (
    teleporter_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(255),
    address VARCHAR(255) NOT NULL,
    location VARCHAR(255) NOT NULL,
    type VARCHAR(10) NOT NULL,
    owner BIGINT NOT NULL,
    FOREIGN KEY (owner) REFERENCES users(adria_id),
    FOREIGN KEY (location) REFERENCES locations(location_id)
);


create table favourites (
    adria_id bigint not null,
    teleporter_id bigint not null,
    type varchar(10),
    name varchar(255) not null,
    primary key (adria_id, teleporter_id),
    foreign key (adria_id) references users(adria_id),
    foreign key (teleporter_id) references teleporters(teleporter_id)
);

create table user_permissions (
    permission_id bigint generated always as identity primary key,
    can_access_all_logs boolean not null,
    can_manage_lists boolean not null,
    can_assign_admin_permissions boolean not null,
    can_control_teleporters boolean not null,
    teleporter_id bigint not null,
    adria_id bigint not null,
    foreign key (teleporter_id) references teleporters(teleporter_id),
    foreign key (adria_id) references users(adria_id)
);

create table groups (
    group_id bigint generated always as identity primary key,
    leader_id bigint not null,
    name varchar(255) not null,
    foreign key (leader_id) references users(adria_id)
);

create table group_members (
    group_id bigint not null,
    adria_id bigint not null,
    foreign key (group_id) references groups(group_id),
    foreign key (adria_id) references users(adria_id)
);

create table transactions (
    transact_id bigint generated always as identity primary key,
    type varchar(15) not null,
    name varchar(255) not null,
    price double not null,
    max_uses int
);

create table user_transactions (
    adria_id bigint not null,
    transact_id bigint not null,
    uses_left int,
    foreign key (adria_id) references users(adria_id),
    foreign key (transact_id) references transactions(transact_id)
);

create table trips (
    trip_id bigint generated always as identity primary key,
    adria_id bigint not null,
    group_id bigint,
    from_teleporter_id bigint not null,
    to_teleporter_id bigint not null,
    departure datetime not null,
    arrival datetime not null,
    foreign key (adria_id) references users(adria_id),
    foreign key (from_teleporter_id) references teleporters(teleporter_id),
    foreign key (to_teleporter_id) references teleporters(teleporter_id),
    foreign key (group_id) references groups(group_id)
);

create table teleporter_settings (
    teleporter_id bigint not null,
    visible boolean not null,
    foreign key (teleporter_id) references teleporters(teleporter_id)
);