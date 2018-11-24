create table mbmark_benchmark (
   id  bigserial not null,
    archived boolean not null,
    content varchar(8192) not null,
    created timestamp not null,
    declare varchar(4096),
    init varchar(4096),
    measurement int4 not null,
    name varchar(255) not null,
    project_id varchar(255) not null,
    warmup int4 not null,
    user_id int8,
    primary key (id)
);

create table mbmark_benchmark_state (
   id  bigserial not null,
    archived boolean not null,
    container_id varchar(255),
    number_of_connections int4 not null,
    project_id varchar(255) not null,
    type varchar(16) not null,
    updated timestamp not null,
    user_id int8,
    primary key (id)
);

create table mbmark_measure_method (
   id  bigserial not null,
    archived boolean not null,
    method varchar(4096) not null,
    order_position int4 not null,
    result_id int8 not null,
    primary key (id)
);

create table mbmark_role (
   id  bigserial not null,
    archived boolean not null,
    type varchar(15) not null,
    primary key (id)
);

create table mbmark_user (
   id  bigserial not null,
    archived boolean not null,
    created timestamp not null,
    email varchar(255) not null,
    enabled boolean,
    firstname varchar(255) not null,
    lastname varchar(255) not null,
    password varchar(255) not null,
    primary key (id)
);

create table mbmark_user_role (
   user_id int8 not null,
    role_id int8 not null,
    primary key (user_id, role_id)
);

create index IDXj6ipfh5mxg9mpd0912b04qt6s on mbmark_benchmark (project_id);

alter table mbmark_benchmark 
   drop constraint if exists UKj6ipfh5mxg9mpd0912b04qt6s;

alter table mbmark_benchmark 
   add constraint UKj6ipfh5mxg9mpd0912b04qt6s unique (project_id);

create index IDXl4a16lo2fyvmmc89ogyfqv2ul on mbmark_benchmark_state (project_id);

alter table mbmark_benchmark_state 
   drop constraint if exists UKl4a16lo2fyvmmc89ogyfqv2ul;

alter table mbmark_benchmark_state 
   add constraint UKl4a16lo2fyvmmc89ogyfqv2ul unique (project_id);

alter table mbmark_role 
   drop constraint if exists UK_fiaddmsa40tvp59c5csnhmqst;

alter table mbmark_role 
   add constraint UK_fiaddmsa40tvp59c5csnhmqst unique (type);

alter table mbmark_user 
   drop constraint if exists UK7fqw9cx4dy2nvsqhl0g092v5x;

alter table mbmark_user 
   add constraint UK7fqw9cx4dy2nvsqhl0g092v5x unique (email);

alter table mbmark_benchmark 
   add constraint FKhqriqbk4wmt6axupffk4ylh6l 
   foreign key (user_id) 
   references mbmark_user;

alter table mbmark_benchmark_state 
   add constraint FK8rs2b7hqrn0phn96qu0pi0493 
   foreign key (user_id) 
   references mbmark_user;

alter table mbmark_measure_method 
   add constraint FKmlku3h3277exv6c83n8a9ywh3 
   foreign key (result_id) 
   references mbmark_benchmark;

alter table mbmark_user_role 
   add constraint FKktfkdqa6uwj7gpgvq0mgnm75n 
   foreign key (role_id) 
   references mbmark_role;

alter table mbmark_user_role 
   add constraint FK3mt2c0oxk2jmoapmr1xd3h0y2 
   foreign key (user_id) 
   references mbmark_user;