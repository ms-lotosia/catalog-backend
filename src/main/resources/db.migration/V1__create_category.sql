create table category
(
    id          bigserial primary key,
    name        varchar(100) not null,
    description varchar(500),
    image       varchar(500),
    constraint uk_category_slug unique (slug)
);