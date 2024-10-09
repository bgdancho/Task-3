CREATE TABLE product_subscribers (
    product_id BIGSERIAL NOT NULL,
    subscriber_id BIGSERIAL NOT NULL,
    PRIMARY KEY (product_id, subscriber_id),
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE,
    FOREIGN KEY (subscriber_id) REFERENCES subscribers(id) ON DELETE CASCADE
);