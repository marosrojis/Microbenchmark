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
    success boolean not null,
    warmup int4 not null,
    user_id int8,
    primary key (id)
);

create table mbmark_benchmark_state (
   id  bigserial not null,
    archived boolean not null,
    completed int4 not null,
    container_id varchar(255),
    number_of_connections int4 not null,
    project_id varchar(255) not null,
    estimated_end_time timestamp,
    type varchar(32) not null,
    updated timestamp not null,
    user_id int8,
    primary key (id)
);

create table mbmark_measure_method (
   id  bigserial not null,
    archived boolean not null,
    method varchar(4096) not null,
    order_position int4 not null,
    benchmark_id int8 not null,
    primary key (id)
);

create table mbmark_property (
   id  bigserial not null,
    key varchar(255) not null,
    value text not null,
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
    enabled boolean not null,
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
   drop constraint if exists UK_BENCHMARK_PROJECT_ID;

alter table mbmark_benchmark
   add constraint UK_BENCHMARK_PROJECT_ID unique (project_id);
create index IDX_BENCHMARK_STATE_PROJECT_ID on mbmark_benchmark_state (project_id);

alter table mbmark_benchmark_state
   drop constraint if exists UK_BENCHMARK_STATE_PROJECT_ID;

alter table mbmark_benchmark_state
   add constraint UK_BENCHMARK_STATE_PROJECT_ID unique (project_id);
create index IDX_PROPERTY_KEY on mbmark_property (key);

alter table mbmark_property
   drop constraint if exists UK_PROPERTY_KEY;

alter table mbmark_property
   add constraint UK_PROPERTY_KEY unique (key);

alter table mbmark_role
   drop constraint if exists UK_ROLE_TYPE;

alter table mbmark_role
   add constraint UK_ROLE_TYPE unique (type);

alter table mbmark_user
   drop constraint if exists UK_USER_EMAIL;

alter table mbmark_user
   add constraint UK_USER_EMAIL unique (email);

alter table mbmark_benchmark
   add constraint FK_BENCHMARK_USER_ID
   foreign key (user_id)
   references mbmark_user;

alter table mbmark_benchmark_state
   add constraint FK_BENCHMARK_STATE_USER_ID
   foreign key (user_id)
   references mbmark_user;

alter table mbmark_measure_method
   add constraint FK_MEASURE_METHOD_BENCHMARK_ID
   foreign key (benchmark_id)
   references mbmark_benchmark;

alter table mbmark_user_role
   add constraint FK_USER_ROLE_ROLE_ID
   foreign key (role_id)
   references mbmark_role;

alter table mbmark_user_role
   add constraint FK_USER_ROLE_USER_ID
   foreign key (user_id)
   references mbmark_user;