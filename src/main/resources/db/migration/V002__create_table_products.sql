CREATE TABLE products (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(30) NOT NULL UNIQUE,
    description TEXT NOT NULL,
    price INTEGER NOT NULL CHECK (price >= 0),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    status_id BIGSERIAL NOT NULL,
    FOREIGN KEY (status_id) REFERENCES product_statuses(id)
);