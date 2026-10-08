create extension if not exists pg_trgm;

create table product (
                         id          bigserial     primary key,
                         sku         varchar(64)   not null,
                         name        varchar(200)  not null,
                         description varchar(2000),
                         price       numeric(12,2) not null,
                         category_id bigint        not null references category (id),
                         active      boolean       not null default true,
                         version     bigint        not null default 0,
                         created_at  timestamptz   not null,
                         updated_at  timestamptz   not null,
                         constraint uk_product_sku unique (sku),
                         constraint ck_product_price check (price > 0),
                         constraint ck_product_name check (length(trim(name)) > 0)
);

create table product_image (
                               id         bigserial    primary key,
                               product_id bigint       not null references product (id) on delete cascade,
                               url        varchar(500) not null,
                               position   int          not null
);
create index idx_product_image_product on product_image (product_id, position);

create index idx_product_newest      on product (created_at desc, id desc) where active;
create index idx_product_cat_newest  on product (category_id, created_at desc, id desc) where active;
create index idx_product_price       on product (price, id) where active;
create index idx_product_name_trgm   on product using gin (lower(name) gin_trgm_ops) where active;