drop schema if exists "order" cascade;

create schema "order";

create extension if not exists "uuid-ossp";

DROP TYPE IF EXISTS order_status;
CREATE TYPE order_status AS ENUM ('PENDING', 'PAID', 'APPROVED', 'CANCELLED', 'CANCELLING');

DROP TABLE IF EXISTS "order".orders CASCADE;

CREATE TABLE "order".orders
(
    id               uuid           NOT NULL,
    customer_id      uuid           NOT NULL,
    restaurant_id    uuid           NOT NULL,
    tracking_id      uuid           NOT NULL,
    price            numeric(10, 2) NOT NULL,
    order_status     order_status   NOT NULL,
    failure_messages character varying COLLATE pg_catalog."default",
    CONSTRAINT orders_pkey PRIMARY KEY (id)
);

DROP TABLE IF EXISTS "order".order_items CASCADE;

CREATE TABLE "order".order_items
(
    id         bigint         NOT NULL,
    order_id   uuid           NOT NULL,
    product_id uuid           NOT NULL,
    price      numeric(10, 2) NOT NULL,
    quantity   integer        NOT NULL,
    sub_total  numeric(10, 2) NOT NULL,
    CONSTRAINT order_items_pkey PRIMARY KEY (id, order_id)
);

ALTER TABLE "order".order_items
    ADD CONSTRAINT "FK_ORDER_ID" FOREIGN KEY (order_id)
        REFERENCES "order".orders (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE
        NOT VALID;

DROP TABLE IF EXISTS "order".order_address CASCADE;

CREATE TABLE "order".order_address
(
    id          uuid                                           NOT NULL,
    order_id    uuid UNIQUE                                    NOT NULL,
    street      character varying COLLATE pg_catalog."default" NOT NULL,
    postal_code character varying COLLATE pg_catalog."default" NOT NULL,
    city        character varying COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT order_address_pkey PRIMARY KEY (id, order_id)
);

ALTER TABLE "order".order_address
    ADD CONSTRAINT "FK_ORDER_ID" FOREIGN KEY (order_id)
        REFERENCES "order".orders (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE
        NOT VALID;

DROP TYPE IF EXISTS saga_status;
CREATE TYPE saga_status AS ENUM ('STARTED', 'FAILED', 'SUCCEEDED', 'PROCESSING', 'COMPENSATING', 'COMPENSATED');

DROP TYPE IF EXISTS outbox_status;
CREATE TYPE outbox_status AS ENUM ('STARTED', 'COMPLETED', 'FAILED');

DROP TABLE IF EXISTS "order".payment_outbox CASCADE;
CREATE TABLE "order".payment_outbox
(
    id            uuid                                           NOT NULL,
    saga_id       uuid                                           NOT NULL,
    created_at    TIMESTAMP WITH TIME ZONE                       NOT NULL,
    processed_at  TIMESTAMP WITH TIME ZONE                       NOT NULL,
    type          character varying COLLATE pg_catalog."default" NOT NULL,
    payload       jsonb                                          NOT NULL,
    outbox_status outbox_status                                  NOT NULL,
    saga_status   saga_status                                    NOT NULL,
    order_status  order_status                                   NOT NULL,
    version       integer                                        NOT NULL,
    CONSTRAINT payment_outbox_pkey PRIMARY KEY (id)
);

DROP INDEX IF EXISTS "payment_outbox_saga_status";
CREATE INDEX "payment_outbox_saga_status"
    ON "order".payment_outbox
        (type, outbox_status, saga_status);

DROP INDEX IF EXISTS "payment_outbox_saga_id";
CREATE INDEX "payment_outbox_saga_id"
    ON "order".payment_outbox
        (type, saga_id, saga_status);

DROP TABLE IF EXISTS "order".restaurant_approval_outbox CASCADE;
CREATE TABLE "order".restaurant_approval_outbox
(
    id            uuid                                           NOT NULL,
    saga_id       uuid                                           NOT NULL,
    created_at    TIMESTAMP WITH TIME ZONE                       NOT NULL,
    processed_at  TIMESTAMP WITH TIME ZONE                       NOT NULL,
    type          character varying COLLATE pg_catalog."default" NOT NULL,
    payload       jsonb                                          NOT NULL,
    outbox_status outbox_status                                  NOT NULL,
    saga_status   saga_status                                    NOT NULL,
    order_status  order_status                                   NOT NULL,
    version       integer                                        NOT NULL,
    CONSTRAINT restaurant_approval_outbox_pkey PRIMARY KEY (id)
);

DROP INDEX IF EXISTS "restaurant_approval_outbox_saga_status";
CREATE INDEX "restaurant_approval_outbox_saga_status"
    ON "order".restaurant_approval_outbox
        (type, outbox_status, saga_status);

DROP INDEX IF EXISTS "restaurant_approval_outbox_saga_id";
CREATE INDEX "restaurant_approval_outbox_saga_id"
    ON "order".restaurant_approval_outbox
        (type, saga_id, saga_status);