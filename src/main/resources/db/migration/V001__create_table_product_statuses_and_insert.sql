CREATE TABLE product_statuses (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

INSERT INTO product_statuses (name) VALUES ('OUT_OF_STOCK');
INSERT INTO product_statuses (name) VALUES ('IN_STOCK');
INSERT INTO product_statuses (name) VALUES ('AWAITING_DELIVERY');