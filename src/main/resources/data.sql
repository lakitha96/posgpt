-- src/main/resources/data.sql

-- Insert products
INSERT INTO product (name, description, price, stock_quantity, category, barcode)
VALUES ('Smartphone X Pro', 'Latest smartphone with advanced features', 799.99, 50, 'Electronics', 'PROD001'),
       ('Wireless Earbuds', 'Noise-cancelling wireless earbuds', 129.99, 100, 'Electronics', 'PROD002'),
       ('Smart Watch Series 5', 'Fitness and health tracking smartwatch', 249.99, 30, 'Electronics', 'PROD003'),
       ('Laptop Ultra', 'Lightweight laptop for productivity', 1299.99, 20, 'Electronics', 'PROD004'),
       ('Coffee Maker Deluxe', 'Programmable coffee maker', 89.99, 35, 'Home Appliances', 'PROD005'),
       ('Blender Pro', 'High-speed blender for smoothies', 79.99, 40, 'Home Appliances', 'PROD006'),
       ('Air Fryer XL', 'Family-sized air fryer', 149.99, 25, 'Home Appliances', 'PROD007'),
       ('Wireless Charger', 'Fast wireless charger for smartphones', 29.99, 60, 'Accessories', 'PROD008'),
       ('Bluetooth Speaker', 'Portable bluetooth speaker', 59.99, 45, 'Electronics', 'PROD009'),
       ('USB-C Cable Pack', 'Set of 3 USB-C cables', 19.99, 100, 'Accessories', 'PROD010');

-- Insert customers
INSERT INTO customer (name, email, phone, loyalty_level, loyalty_points)
VALUES ('John Smith', 'john.smith@example.com', '555-123-4567', 'GOLD', 750),
       ('Emily Johnson', 'emily.johnson@example.com', '555-234-5678', 'SILVER', 320),
       ('Michael Brown', 'michael.brown@example.com', '555-345-6789', 'STANDARD', 50),
       ('Jessica Davis', 'jessica.davis@example.com', '555-456-7890', 'PLATINUM', 1200),
       ('David Wilson', 'david.wilson@example.com', '555-567-8901', 'SILVER', 430);

-- Insert sales
INSERT INTO sale (customer_id, date_time, total_amount, payment_method, status)
VALUES (1, '2023-05-01T10:30:00', 879.98, 'CREDIT_CARD', 'COMPLETED'),
       (2, '2023-05-02T14:15:00', 249.99, 'CREDIT_CARD', 'COMPLETED'),
       (3, '2023-05-03T16:45:00', 149.99, 'CASH', 'COMPLETED'),
       (4, '2023-05-05T09:20:00', 1499.97, 'CREDIT_CARD', 'COMPLETED'),
       (2, '2023-05-08T13:10:00', 19.99, 'DEBIT_CARD', 'COMPLETED'),
       (1, '2023-05-10T15:30:00', 209.98, 'CREDIT_CARD', 'COMPLETED'),
       (5, '2023-05-12T11:45:00', 59.99, 'CASH', 'COMPLETED'),
       (4, '2023-05-15T17:00:00', 129.99, 'DEBIT_CARD', 'COMPLETED'),
       (3, '2023-05-18T10:15:00', 29.99, 'CASH', 'COMPLETED'),
       (1, '2023-05-20T14:30:00', 1299.99, 'CREDIT_CARD', 'COMPLETED');

-- Insert sale items
-- Sale 1: John bought a Smartphone X Pro and a Wireless Charger
INSERT INTO sale_item (sale_id, product_id, quantity, unit_price, subtotal, discount)
VALUES (1, 1, 1, 799.99, 799.99, 0),
       (1, 8, 1, 29.99, 29.99, 0);

-- Sale 2: Emily bought a Smart Watch
INSERT INTO sale_item (sale_id, product_id, quantity, unit_price, subtotal, discount)
VALUES (2, 3, 1, 249.99, 249.99, 0);

-- Sale 3: Michael bought an Air Fryer
INSERT INTO sale_item (sale_id, product_id, quantity, unit_price, subtotal, discount)
VALUES (3, 7, 1, 149.99, 149.99, 0);

-- Sale 4: Jessica bought a Laptop Ultra and Wireless Earbuds
INSERT INTO sale_item (sale_id, product_id, quantity, unit_price, subtotal, discount)
VALUES (4, 4, 1, 1299.99, 1299.99, 0),
       (4, 2, 1, 129.99, 129.99, 0),
       (4, 8, 1, 29.99, 29.99, 0);

-- Sale 5: Emily bought USB-C Cable Pack
INSERT INTO sale_item (sale_id, product_id, quantity, unit_price, subtotal, discount)
VALUES (5, 10, 1, 19.99, 19.99, 0);

-- Sale 6: John bought Wireless Earbuds and Bluetooth Speaker
INSERT INTO sale_item (sale_id, product_id, quantity, unit_price, subtotal, discount)
VALUES (6, 2, 1, 129.99, 129.99, 0),
       (6, 9, 1, 59.99, 59.99, 0);

-- Sale 7: David bought a Bluetooth Speaker
INSERT INTO sale_item (sale_id, product_id, quantity, unit_price, subtotal, discount)
VALUES (7, 9, 1, 59.99, 59.99, 0);

-- Sale 8: Jessica bought Wireless Earbuds
INSERT INTO sale_item (sale_id, product_id, quantity, unit_price, subtotal, discount)
VALUES (8, 2, 1, 129.99, 129.99, 0);

-- Sale 9: Michael bought a Wireless Charger
INSERT INTO sale_item (sale_id, product_id, quantity, unit_price, subtotal, discount)
VALUES (9, 8, 1, 29.99, 29.99, 0);

-- Sale 10: John bought a Laptop Ultra
INSERT INTO sale_item (sale_id, product_id, quantity, unit_price, subtotal, discount)
VALUES (10, 4, 1, 1299.99, 1299.99, 0);


-- Insert chat messages for John Smith (Customer ID 1)
INSERT INTO chat_message (customer_id, message_content, response_content, timestamp, message_type, related_product_id,
                          related_sale_id, resulted_in_sale, resulted_in_return)
VALUES (1, 'What are the features of the Smart Watch Series 5?',
        'The Smart Watch Series 5 features heart rate monitoring, step counting, sleep tracking, water resistance, and smartphone notifications.',
        '2023-05-15T09:30:00', 'PRODUCT_INQUIRY', 3, NULL, false, false);

INSERT INTO chat_message (customer_id, message_content, response_content, timestamp, message_type, related_product_id,
                          related_sale_id, resulted_in_sale, resulted_in_return)
VALUES (1, 'I want to track my purchase from May 1st',
        'I can see your purchase from May 1, 2023 (Sale #1). You bought a Smartphone X Pro and a Wireless Charger. The total was $879.98, paid via CREDIT_CARD.',
        '2023-05-16T14:45:00', 'ORDER_TRACKING', NULL, 1, false, false);

INSERT INTO chat_message (customer_id, message_content, response_content, timestamp, message_type, related_product_id,
                          related_sale_id, resulted_in_sale, resulted_in_return)
VALUES (1, 'Can you recommend accessories for my Smartphone?',
        'Based on your purchase, I recommend: 1. Wireless Charger for fast charging, 2. Bluetooth Speaker for music, 3. USB-C Cable Pack for extra connections.',
        '2023-05-17T11:20:00', 'RECOMMENDATION', 1, NULL, false, false);

-- Insert chat messages for Emily Johnson (Customer ID 2)
INSERT INTO chat_message (customer_id, message_content, response_content, timestamp, message_type, related_product_id,
                          related_sale_id, resulted_in_sale, resulted_in_return)
VALUES (2, 'Do you have any coffee makers on sale?',
        'Yes, we have the Coffee Maker Deluxe available for $89.99. It features auto-brewing, temperature control, and a built-in grinder.',
        '2023-05-18T10:10:00', 'PRODUCT_INQUIRY', 5, NULL, false, false);

INSERT INTO chat_message (customer_id, message_content, response_content, timestamp, message_type, related_product_id,
                          related_sale_id, resulted_in_sale, resulted_in_return)
VALUES (2, 'When will my Smart Watch be delivered?',
        'Your Smart Watch Series 5 purchase (Sale #2) from May 2, 2023 has been completed. If not received, please contact support.',
        '2023-05-19T15:35:00', 'ORDER_TRACKING', 3, 2, false, false);

-- Insert chat messages for Michael Brown (Customer ID 3)
INSERT INTO chat_message (customer_id, message_content, response_content, timestamp, message_type, related_product_id,
                          related_sale_id, resulted_in_sale, resulted_in_return)
VALUES (3, 'How do I return a product?',
        'You can return products within 30 days with receipt for a full refund. Visit our store or initiate online through your account.',
        '2023-05-20T09:45:00', 'GENERAL', NULL, NULL, false, false);

INSERT INTO chat_message (customer_id, message_content, response_content, timestamp, message_type, related_product_id,
                          related_sale_id, resulted_in_sale, resulted_in_return)
VALUES (3, 'I need info about my Air Fryer purchase',
        'Your Air Fryer XL purchase (Sale #3) from May 3, 2023 was $149.99 in CASH. Status: COMPLETED. It has a 2-year warranty.',
        '2023-05-21T14:20:00', 'ORDER_TRACKING', 7, 3, false, false);

-- Insert chat messages for Jessica Davis (Customer ID 4)
INSERT INTO chat_message (customer_id, message_content, response_content, timestamp, message_type, related_product_id,
                          related_sale_id, resulted_in_sale, resulted_in_return)
VALUES (4, 'My Laptop Ultra is running slow',
        'Try restarting, closing background apps, updating OS, running disk cleanup, and ensuring you have free storage space.',
        '2023-05-22T11:30:00', 'PRODUCT_INQUIRY', 4, NULL, false, false);

INSERT INTO chat_message (customer_id, message_content, response_content, timestamp, message_type, related_product_id,
                          related_sale_id, resulted_in_sale, resulted_in_return)
VALUES (4, 'What did I purchase on May 5th?',
        'On May 5, 2023 (Sale #4), you purchased a Laptop Ultra, Wireless Earbuds, and a Wireless Charger. Total: $1,499.97.',
        '2023-05-22T11:45:00', 'ORDER_TRACKING', NULL, 4, false, false);

-- Insert chat messages for David Wilson (Customer ID 5)
INSERT INTO chat_message (customer_id, message_content, response_content, timestamp, message_type, related_product_id,
                          related_sale_id, resulted_in_sale, resulted_in_return)
VALUES (5, 'What is the warranty on the Bluetooth Speaker?',
        'The Bluetooth Speaker has a 1-year warranty for defects. Register online for an 18-month extended warranty.',
        '2023-05-23T16:15:00', 'PRODUCT_INQUIRY', 9, NULL, false, false);

INSERT INTO chat_message (customer_id, message_content, response_content, timestamp, message_type, related_product_id,
                          related_sale_id, resulted_in_sale, resulted_in_return)
VALUES (5, 'How do I earn more loyalty points?',
        'You have 430 SILVER points. You need 70 more for GOLD. Earn 1 point per $1 spent, 100 for referrals, and 10 for reviews.',
        '2023-05-24T13:40:00', 'GENERAL', NULL, NULL, false, false);
