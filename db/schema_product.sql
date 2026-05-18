create extension if not exists "pgcrypto";

create table inventory_c.product
(
    id uuid primary key default gen_random_uuid(),
    name varchar(255) not null,
    description text,
    serial_number varchar(255) not null unique,
    model varchar(255) not null,
    manufacturer varchar(255) not null,
    status varchar(50) not null,
    created_at timestamp default current_timestamp,
    updated_at timestamp default current_timestamp
);

create index idx_inventory_product_serial_number on inventory_c.product(serial_number);

create index idx_inventory_product_status on inventory_c.product(status);

create index idx_inventory_product_manufacturer on inventory_c.product(manufacturer);

comment on table inventory_c.product is 'Tabla de productos (dispositivos médicos) del inventory_context. Cada producto es un dispositivo único.';
comment on column inventory_c.product.id is 'Identificador único del producto (UUID)';
comment on column inventory_c.product.name is 'Nombre del producto/dispositivo';
comment on column inventory_c.product.description is 'Descripción detallada del producto';
comment on column inventory_c.product.serial_number is 'Número de serie único del dispositivo';
comment on column inventory_c.product.model is 'Modelo del dispositivo';
comment on column inventory_c.product.manufacturer is 'Fabricante del dispositivo';
comment on column inventory_c.product.status is 'Estado del producto (ACTIVE, INACTIVE, MAINTENANCE, etc.)';
comment on column inventory_c.product.created_at is 'Fecha y hora de creación del registro';
comment on column inventory_c.product.updated_at is 'Fecha y hora de la última actualización del registro';

