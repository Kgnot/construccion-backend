-- Habilitar extensión UUID (PostgreSQL)
create extension if not exists "pgcrypto";

-- Catálogo tipos de documento
create table customer_c.document_type
(
    document_type_id bigint generated always as identity primary key,
    name varchar(50) not null unique,
    description varchar(100)
);

-- Tabla principal de clientes
create table customer_c.customer
(
    customer_id uuid primary key default gen_random_uuid(),
    first_name varchar(100) not null,
    last_name varchar(100) not null,
    document_type_id bigint not null,
    document_number varchar(50) not null unique,
    birth_date date,
    created_at timestamp default current_timestamp,
    updated_at timestamp default current_timestamp
);

-- Tabla teléfonos
create table customer_c.customer_phone
(
    customer_id uuid not null,
    phone_number varchar(20) not null,
    phone_type varchar(20),
    is_primary boolean default false,
    created_at timestamp default current_timestamp,
    primary key (customer_id, phone_number)
);

-- Tabla emails
create table customer_c.customer_email
(
    customer_id uuid not null,
    email_address varchar(255) not null,
    is_primary boolean default false,
    created_at timestamp default current_timestamp,
    primary key (customer_id, email_address)
);

-- Tabla direcciones
create table customer_c.customer_address
(
    address_id uuid primary key default gen_random_uuid(),
    customer_id uuid not null,
    address_line varchar(255) not null,
    city varchar(100),
    state varchar(100),
    country varchar(100),
    postal_code varchar(20),
    is_primary boolean default false,
    created_at timestamp default current_timestamp
);

-- =========================
-- FOREIGN KEYS
-- =========================

alter table customer_c.customer
    add constraint fk_customer_document_type
        foreign key (document_type_id)
            references customer_c.document_type(document_type_id);

alter table customer_c.customer_phone
    add constraint fk_customer_phone_customer
        foreign key (customer_id)
            references customer_c.customer(customer_id)
            on delete cascade;

alter table customer_c.customer_email
    add constraint fk_customer_email_customer
        foreign key (customer_id)
            references customer_c.customer(customer_id)
            on delete cascade;

alter table customer_c.customer_address
    add constraint fk_customer_address_customer
        foreign key (customer_id)
            references customer_c.customer(customer_id)
            on delete cascade;

-- =========================
-- SEED DATA: DOCUMENT TYPES
-- =========================

insert into customer_c.document_type (name, description)
values
    ('CC', 'Cédula de ciudadanía'),
    ('TI', 'Tarjeta de identidad'),
    ('CE', 'Cédula de extranjería'),
    ('PASSPORT', 'Pasaporte'),
    ('NIT', 'Número de identificación tributaria'),
    ('RC', 'Registro civil');