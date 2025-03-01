-- Sample data for customers
INSERT INTO customers (first_name, last_name, email) VALUES ('John', 'Doe', 'john.doe@example.com');
INSERT INTO customers (first_name, last_name, email) VALUES ('Jane', 'Smith', 'jane.smith@example.com');
INSERT INTO customers (first_name, last_name, email) VALUES ('Bob', 'Johnson', 'bob.johnson@example.com');
INSERT INTO customers (first_name, last_name, email) VALUES ('Alice', 'Brown', 'alice.brown@example.com');
INSERT INTO customers (first_name, last_name, email) VALUES ('Charlie', 'Davis', 'charlie.davis@example.com');

-- Sample data for products
INSERT INTO products (name, description, price, stock) VALUES ('Laptop', 'High-performance laptop with SSD', 999.99, 25);
INSERT INTO products (name, description, price, stock) VALUES ('Smartphone', '5G smartphone with 6.5 inch display', 699.99, 50);
INSERT INTO products (name, description, price, stock) VALUES ('Headphones', 'Noise-cancelling wireless headphones', 199.99, 100);
INSERT INTO products (name, description, price, stock) VALUES ('Tablet', '10-inch tablet with stylus', 349.99, 30);
INSERT INTO products (name, description, price, stock) VALUES ('Monitor', '27-inch 4K monitor', 299.99, 15);

INSERT INTO customer_products (customer_id, product_id) VALUES (1,1);
INSERT INTO customer_products (customer_id, product_id) VALUES (1,3);
INSERT INTO customer_products (customer_id, product_id) VALUES (2,2);
INSERT INTO customer_products (customer_id, product_id) VALUES (2,5);
INSERT INTO customer_products (customer_id, product_id) VALUES (3,3);
INSERT INTO customer_products (customer_id, product_id) VALUES (3,1);
INSERT INTO customer_products (customer_id, product_id) VALUES (4,2);
INSERT INTO customer_products (customer_id, product_id) VALUES (4,4);
INSERT INTO customer_products (customer_id, product_id) VALUES (5,2);
INSERT INTO customer_products (customer_id, product_id) VALUES (5,5);



