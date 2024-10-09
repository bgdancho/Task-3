CREATE TABLE purchases (
    id BIGSERIAL PRIMARY KEY,
    product_id BIGSERIAL NOT NULL,
    subscriber_id BIGSERIAL NOT NULL,
    quantity INT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE,
    FOREIGN KEY (subscriber_id) REFERENCES subscribers(id) ON DELETE CASCADE
);