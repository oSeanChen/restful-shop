-- Insert sample categories
INSERT INTO categories (id, category_name)
VALUES (1, 'Peripherals'),
       (2, 'Monitors'),
       (3, 'Accessories');

-- Insert sample suppliers
INSERT INTO suppliers (id, supplier_name)
VALUES (1, 'TechSupply Inc.'),
       (2, 'GadgetWorld Ltd.'),
       (3, 'ElectroGoods Co.');

INSERT INTO products (product_name, category_id, supplier_id, unit_price, units_in_stock, discontinued)
VALUES ('Wireless Mouse', 1, 1, 24.99, 150, FALSE),
       ('Bluetooth Keyboard', 1, 2, 49.99, 75, FALSE),
       ('USB-C Charger', 3, 1, 19.99, 200, FALSE),
       ('Gaming Headset', 1, 3, 79.99, 50, FALSE),
       ('4K Monitor', 2, 2, 299.99, 40, TRUE),
       ('External Hard Drive', 3, 1, 89.99, 120, FALSE),
       ('Mechanical Keyboard', 1, 3, 129.99, 30, TRUE),
       ('Portable SSD', 3, 2, 159.99, 60, FALSE),
       ('Wireless Earbuds', 1, 1, 199.99, 80, FALSE),
       ('Laptop Stand', 2, 3, 39.99, 100, FALSE);