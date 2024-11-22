-- DROP TABLE IF EXISTS order_details;
-- DROP TABLE IF EXISTS orders;

CREATE TABLE IF NOT EXISTS orders
(
    id               BIGSERIAL PRIMARY KEY,
    order_number     VARCHAR(13)    NOT NULL,
    total_amount     NUMERIC(15, 2) NOT NULL,
    order_date       DATE           NOT NULL,
    recipient        VARCHAR(100)   NOT NULL,
    delivery_address VARCHAR(200)   NOT NULL,
    payment_type     VARCHAR(20)    NOT NULL,
    delivery_type    VARCHAR(20)    NOT NULL
);

CREATE TABLE IF NOT EXISTS order_details
(
    id           BIGSERIAL PRIMARY KEY,
    product_code BIGINT         NOT NULL,
    product_name VARCHAR(100)   NOT NULL,
    quantity     INT            NOT NULL,
    unit_price   NUMERIC(10, 2) NOT NULL,
    order_id     BIGINT         NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders (id) ON DELETE CASCADE
);
