/* 📦 Warehousing BC Schema */

-- Insert Sellers
INSERT INTO warehousing.sellers (id, name, street_name, street_number, city, country)
VALUES
    ('23f11460-22bb-4888-b52a-2c5f6c9d1ea5', 'Hüseyin Safa Ablak', 'Rue des Frites', '112', 'Brussels', 'Belgium'),
    ('5148d75a-ea67-4d00-b87a-1b07cd174f55', 'Noah Guerin', 'Boulevard du Chocolat', '77', 'Mechelen', 'Belgium'),
    ('e4230518-a7dc-4426-8ffc-0790a6bc026d', 'Alperen Doğancı', 'Avenue des Gaufres', '33', 'Liège', 'Belgium');

-- Insert Buyers
INSERT INTO warehousing.buyers (id, name, street_name, street_number, city, country)
VALUES
    ('be32da76-2ca5-4bee-9f3b-62aab7839622', 'Thomas Maxwell', 'Nationalestraat', '5', 'Antwerp', 'Belgium'),
    ('da27f14f-5c54-43cd-a6a0-5dc5c5c744fb', 'Raoul Van den Berge', 'Pothoekstraat', '125', 'Antwerp', 'Belgium'),
    ('065a4145-7791-4017-8c6e-e0838c917d51', 'Kevin Smeyers', 'Brusselstraat', '45', 'Antwerp', 'Belgium'),
    ('0e2ee25f-0ada-4727-8c50-6e2c305fa345', 'Bart Vochten', 'Meir', '117-127', 'Antwerp', 'Belgium');

-- Insert Warehouses
INSERT INTO warehousing.warehouses (warehouse_number, balance, balance_updated_at, percentage_filled, raw_material, seller_id, warehouse_id, site_easting, site_northing) VALUES
--  1-) Warehouses of Hüseyin Safa Ablak
    ('WH-01', 0.0, NOW() - INTERVAL 1 MONTH, 0.0, 'GYPSUM', '23f11460-22bb-4888-b52a-2c5f6c9d1ea5','cd37c514-7cd6-4031-8130-854ffaeee00b', 0.0, 0.0),
    -- TODO: Test out Raw Material assignment logic(!= IRON_ORE) for this Warehouse
    ('WH-02', 0.0, NOW() - INTERVAL 1 MONTH, 0.0, 'IRON_ORE', '23f11460-22bb-4888-b52a-2c5f6c9d1ea5','7dc5dc59-2fd5-4b60-b669-8bcc5538a017', 140.0, 0.0),
    ('WH-03', 0.0, NOW() - INTERVAL 1 MONTH, 0.0, 'CEMENT', '23f11460-22bb-4888-b52a-2c5f6c9d1ea5','1054e87e-e275-4015-a81c-8004a863d277', 280.0, 0.0),
    ('WH-04', 0.0, NOW() - INTERVAL 1 MONTH, 0.0, 'PETCOKE', '23f11460-22bb-4888-b52a-2c5f6c9d1ea5','e807926a-33a4-4dfb-84c9-4f2c74829b8b', 420.0, 0.0),
    ('WH-05', 0.0, NOW() - INTERVAL 1 MONTH, 0.0, 'SLAG', '23f11460-22bb-4888-b52a-2c5f6c9d1ea5','c7bc9dfb-745b-43cb-a4b4-02632a33633f', 560.0, 0.0),
--  2-) Warehouses of Noah Guerin
    ('WH-06', 0.0, NOW() - INTERVAL 1 MONTH, 0.0, 'GYPSUM', '5148d75a-ea67-4d00-b87a-1b07cd174f55', '2200cbb8-1899-475b-9f31-619d604411ae', 0.0, 170.0),
    ('WH-07', 0.0, NOW() - INTERVAL 1 MONTH, 0.0, 'IRON_ORE', '5148d75a-ea67-4d00-b87a-1b07cd174f55', '234b0c02-6f4d-4b1f-98ad-124ace7a44db', 140.0, 170.0),
    ('WH-08', 0.0, NOW() - INTERVAL 1 MONTH, 0.0, 'CEMENT', '5148d75a-ea67-4d00-b87a-1b07cd174f55', 'bd45f2a6-20d9-4d1d-9357-b2dc5a1ff003', 280.0, 170.0),
    ('WH-09', 0.0, NOW() - INTERVAL 1 MONTH, 0.0, 'PETCOKE', '5148d75a-ea67-4d00-b87a-1b07cd174f55', 'de7c7500-5869-44fe-9a9f-f837d0cf0fa6', 420.0, 170.0),
    ('WH-10', 0.0, NOW() - INTERVAL 1 MONTH, 0.0, 'SLAG', '5148d75a-ea67-4d00-b87a-1b07cd174f55', '45def704-6b87-442c-b575-91bb2ec804a9', 560.0, 170.0),
--  3-) Warehouses of Alperen Doğancı
    ('WH-11', 0.0, NOW() - INTERVAL 1 MONTH, 0.0, 'GYPSUM', 'e4230518-a7dc-4426-8ffc-0790a6bc026d', '0da2c2aa-8ad1-4c3d-9468-1582fa37459a', 0.0, 340.0),
    ('WH-12', 0.0, NOW() - INTERVAL 1 MONTH, 0.0, 'IRON_ORE', 'e4230518-a7dc-4426-8ffc-0790a6bc026d', '317a6bce-bd7a-4d0c-9ae3-eb5b5b9be686', 140.0, 340.0),
    ('WH-13', 0.0, NOW() - INTERVAL 1 MONTH, 0.0, 'CEMENT', 'e4230518-a7dc-4426-8ffc-0790a6bc026d', '57603c7c-87e3-4a9d-9054-a3d11fa58f3b', 280.0, 340.0),
    ('WH-14', 0.0, NOW() - INTERVAL 1 MONTH, 0.0, 'PETCOKE', 'e4230518-a7dc-4426-8ffc-0790a6bc026d', '70d2c0a7-b92f-4637-98a6-9472d07d7931', 420.0, 340.0),
    ('WH-15', 0.0, NOW() - INTERVAL 1 MONTH, 0.0, 'SLAG', 'e4230518-a7dc-4426-8ffc-0790a6bc026d', '01a218ac-38a1-4fa1-9565-b2d2267cecc8', 560.0, 340.0);

-- Insert Deliveries
INSERT INTO warehousing.warehouse_deliveries(amount, time, delivery_id, warehouse_id) VALUES
--  WH-01
    (20, NOW() - INTERVAL 5 DAY, '6795a41e-9ebc-401d-9252-fb4debb093b3', 'cd37c514-7cd6-4031-8130-854ffaeee00b'),
    (21, NOW() - INTERVAL 4 DAY, '3ed00b96-3f2c-45f0-bac1-1be0356364bf', 'cd37c514-7cd6-4031-8130-854ffaeee00b'),
    (22, NOW() - INTERVAL 3 DAY, 'a0a25c58-97bd-4824-aa02-d929cd6e72b2', 'cd37c514-7cd6-4031-8130-854ffaeee00b'),
    (23, NOW() - INTERVAL 2 DAY, '99636f3f-39e6-48aa-a42f-286d8790dad9', 'cd37c514-7cd6-4031-8130-854ffaeee00b'),
    (24, NOW() - INTERVAL 1 DAY, 'f2b3c635-1c3d-4a02-b008-0f49fc9e5f6f', 'cd37c514-7cd6-4031-8130-854ffaeee00b'),
    (25, NOW(), 'c0779f2e-807b-4068-a919-e8a9c5d31ae9', 'cd37c514-7cd6-4031-8130-854ffaeee00b'),
--  WH-03
    (20, NOW() - INTERVAL 5 DAY, '117a3fcb-8374-4db7-bdee-d81ac4893001', '1054e87e-e275-4015-a81c-8004a863d277'),
    (25, NOW() - INTERVAL 4 DAY, 'a5fd6ff5-1d09-4026-a1b6-1a1c2a5e1fd7', '1054e87e-e275-4015-a81c-8004a863d277'),
    (30, NOW() - INTERVAL 3 DAY, '0cd5c99e-c3c5-4092-8139-f2a087bcf7e6', '1054e87e-e275-4015-a81c-8004a863d277'),
    (20, NOW() - INTERVAL 2 DAY, 'ee6a7de4-4191-4c6e-a429-49930fe71ad2', '1054e87e-e275-4015-a81c-8004a863d277'),
    (25, NOW() - INTERVAL 1 DAY, 'c4d74fc4-59c2-4f00-8e55-477d3968e8ae', '1054e87e-e275-4015-a81c-8004a863d277'),
    (30, NOW(), 'f65364ae-114f-40cf-826c-9b8657407a8f', '1054e87e-e275-4015-a81c-8004a863d277'),
--  WH-08 (To fill it to %80 threshold inserting an unrealistic Delivery of 400000 tons, so it becomes unavailable for new Appointments)
    (400000, NOW() - INTERVAL 5 DAY, '7a28dac9-f321-4ef3-93bd-ec7b36df2984', 'bd45f2a6-20d9-4d1d-9357-b2dc5a1ff003'),
--  WH-15
    (25, NOW() - INTERVAL 5 DAY, '5bfa7ad0-9448-4914-bace-3ef69009f310', '01a218ac-38a1-4fa1-9565-b2d2267cecc8'),
    (26, NOW() - INTERVAL 4 DAY, '322eb0c6-6de5-456e-856b-fe8a16398adb', '01a218ac-38a1-4fa1-9565-b2d2267cecc8'),
    (27, NOW() - INTERVAL 3 DAY, 'b437134f-e67a-4afb-a79e-7a7ce49fd005', '01a218ac-38a1-4fa1-9565-b2d2267cecc8'),
    (28, NOW() - INTERVAL 2 DAY, '51f9ab82-1312-4587-96db-022bf2523016', '01a218ac-38a1-4fa1-9565-b2d2267cecc8'),
    (29, NOW() - INTERVAL 1 DAY, '706fe7df-6b8a-4913-87b3-cf7143e30536', '01a218ac-38a1-4fa1-9565-b2d2267cecc8'),
    (30, NOW(), '91731012-e76b-475c-95f0-965d4c6ae2a4', '01a218ac-38a1-4fa1-9565-b2d2267cecc8');

-- Insert Purchase Orders
INSERT INTO warehousing.purchase_orders (order_date, po_number, buyer_id, purchase_order_id, seller_id, buyer_name, seller_name, status) VALUES
    (NOW() - INTERVAL 3 WEEK, 'PO000001', 'be32da76-2ca5-4bee-9f3b-62aab7839622', '11111111-1111-1111-1111-111111111111', '23f11460-22bb-4888-b52a-2c5f6c9d1ea5', 'Thomas Maxwell', 'Hüseyin Safa Ablak', 'PENDING'),
    (NOW() - INTERVAL 3 WEEK, 'PO000002', 'da27f14f-5c54-43cd-a6a0-5dc5c5c744fb', '22222222-2222-2222-2222-222222222222', '5148d75a-ea67-4d00-b87a-1b07cd174f55', 'Raoul Van den Berge', 'Noah Guerin', 'PENDING'),
    (NOW() - INTERVAL 3 WEEK, 'PO000003', '065a4145-7791-4017-8c6e-e0838c917d51', '33333333-3333-3333-3333-333333333333', 'e4230518-a7dc-4426-8ffc-0790a6bc026d', 'Kevin Smeyers', 'Alperen Doğancı', 'PENDING'),
    -- Enter a Shipping Order with Reference ID of 44444444-4444-4444-4444-444444444444 to test it in the waterside.http file
    (NOW() - INTERVAL 3 WEEK, 'PO000004', '0e2ee25f-0ada-4727-8c50-6e2c305fa345', '44444444-4444-4444-4444-444444444444', '23f11460-22bb-4888-b52a-2c5f6c9d1ea5', 'Bart Vochten', 'Hüseyin Safa Ablak', 'PENDING');

-- Insert Order Lines
INSERT INTO warehousing.order_lines (amount, line_number, order_line_id, purchase_order_id, raw_material) VALUES
    (40, 1, '7a11eb77-5304-49db-a2be-e45a00ff65dd', '11111111-1111-1111-1111-111111111111', 'GYPSUM'),
    (15, 1, '1dbf2b58-931c-4db5-85b1-4826308d0106', '22222222-2222-2222-2222-222222222222', 'CEMENT'),
    (20, 1, 'da2b08c8-c2da-4745-b720-fadd6befd047', '33333333-3333-3333-3333-333333333333', 'SLAG'),
    (134, 1, 'ae690ad5-e185-443f-97f9-3fbd9ec5842c', '44444444-4444-4444-4444-444444444444', 'GYPSUM');

/* 🚚 Landside BC Schema */

-- Insert Warehouses
INSERT INTO landside.landside_warehouses (is_available, supplier_id, warehouse_id, raw_material) VALUES
--   1-) Warehouses of Hüseyin Safa Ablak
    (true,'23f11460-22bb-4888-b52a-2c5f6c9d1ea5','cd37c514-7cd6-4031-8130-854ffaeee00b', 'GYPSUM'),
    (true,'23f11460-22bb-4888-b52a-2c5f6c9d1ea5','7dc5dc59-2fd5-4b60-b669-8bcc5538a017', 'IRON_ORE'),
    (true,'23f11460-22bb-4888-b52a-2c5f6c9d1ea5','1054e87e-e275-4015-a81c-8004a863d277', 'CEMENT'),
    (true,'23f11460-22bb-4888-b52a-2c5f6c9d1ea5','e807926a-33a4-4dfb-84c9-4f2c74829b8b', 'PETCOKE'),
    (true,'23f11460-22bb-4888-b52a-2c5f6c9d1ea5','c7bc9dfb-745b-43cb-a4b4-02632a33633f', 'SLAG'),
--   2-) Warehouses of Noah Guerin
    (true,'5148d75a-ea67-4d00-b87a-1b07cd174f55','2200cbb8-1899-475b-9f31-619d604411ae', 'GYPSUM'),
    (true,'5148d75a-ea67-4d00-b87a-1b07cd174f55','234b0c02-6f4d-4b1f-98ad-124ace7a44db', 'IRON_ORE'),
    -- WH-08 is unavailable for new Appointments
    (false,'5148d75a-ea67-4d00-b87a-1b07cd174f55','bd45f2a6-20d9-4d1d-9357-b2dc5a1ff003', 'CEMENT'),
    (true,'5148d75a-ea67-4d00-b87a-1b07cd174f55','de7c7500-5869-44fe-9a9f-f837d0cf0fa6', 'PETCOKE'),
    (true,'5148d75a-ea67-4d00-b87a-1b07cd174f55','45def704-6b87-442c-b575-91bb2ec804a9', 'SLAG'),
--   3-) Warehouses of Alperen Doğancı
    (true,'e4230518-a7dc-4426-8ffc-0790a6bc026d','0da2c2aa-8ad1-4c3d-9468-1582fa37459a', 'GYPSUM'),
    (true,'e4230518-a7dc-4426-8ffc-0790a6bc026d','317a6bce-bd7a-4d0c-9ae3-eb5b5b9be686', 'IRON_ORE'),
    (true,'e4230518-a7dc-4426-8ffc-0790a6bc026d','57603c7c-87e3-4a9d-9054-a3d11fa58f3b', 'CEMENT'),
    (true,'e4230518-a7dc-4426-8ffc-0790a6bc026d','70d2c0a7-b92f-4637-98a6-9472d07d7931', 'PETCOKE'),
    (true,'e4230518-a7dc-4426-8ffc-0790a6bc026d','01a218ac-38a1-4fa1-9565-b2d2267cecc8', 'SLAG');

-- Insert weigh bridges
INSERT INTO landside.weigh_bridges (weigh_bridge_id, number, occupied_by_visit_id) VALUES
    ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'WB-01', NULL),
    ('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 'WB-02', NULL),
    ('cccccccc-cccc-cccc-cccc-cccccccccccc', 'WB-03', NULL);

-- Insert daily schedules for yesterday, 2 days ahead, 3 days ahead
INSERT INTO landside.daily_schedules (date) VALUES
    (NOW() - INTERVAL 1 DAY),
    (NOW() + INTERVAL 2 DAY),
    (NOW() + INTERVAL 3 DAY);

-- Insert one time slot per day: 08:00–09:00, capacity 39 [Normally 24 slots per day, but either the sql file will become huge or I will need to write an ugly query for it :)]
INSERT INTO landside.time_slots (id, start_time, end_time, available_capacity, daily_schedule_date) VALUES
    ('11111111-1111-1111-1111-111111111111', NOW() - INTERVAL 1 DAY + INTERVAL 8 HOUR, NOW() - INTERVAL 1 DAY + INTERVAL 9 HOUR, 39, NOW() - INTERVAL 1 DAY),
    ('22222222-2222-2222-2222-222222222222', NOW() + INTERVAL 2 DAY + INTERVAL 8 HOUR, NOW() + INTERVAL 2 DAY + INTERVAL 9 HOUR, 39, NOW() + INTERVAL 2 DAY),
    ('33333333-3333-3333-3333-333333333333', NOW() + INTERVAL 3 DAY + INTERVAL 8 HOUR, NOW() + INTERVAL 3 DAY + INTERVAL 9 HOUR, 39, NOW() + INTERVAL 3 DAY);

-- Insert appointments linked to the time slots above
INSERT INTO landside.appointments (
    appointment_id, truck_license_plate, time_slot_id, warehouse_id, supplier_id,
    raw_material, arrival_window_start, arrival_window_end, status
) VALUES
      ('b154e84e-8daf-45d2-bcd8-cc0976945443', '1-ABC-123', '11111111-1111-1111-1111-111111111111', 'cd37c514-7cd6-4031-8130-854ffaeee00b', 'f24e0a31-4506-4cd1-a9f0-30e5fe9ae12e', 'GYPSUM', NOW() - INTERVAL 1 DAY + INTERVAL 8 HOUR, NOW() - INTERVAL 1 DAY + INTERVAL 9 HOUR, 'CANCELLED'),
      ('79fcc773-6f47-471f-af9d-a0487fc5e987', '2-XYZ-456', '22222222-2222-2222-2222-222222222222', 'bd45f2a6-20d9-4d1d-9357-b2dc5a1ff003', 'f24e0a31-4506-4cd1-a9f0-30e5fe9ae12e', 'CEMENT', NOW() + INTERVAL 2 DAY + INTERVAL 8 HOUR, NOW() + INTERVAL 2 DAY + INTERVAL 9 HOUR, 'SCHEDULED'),
      ('82038050-1c36-4901-af4a-ee77784a04ec', '3-JKL-789', '33333333-3333-3333-3333-333333333333', '01a218ac-38a1-4fa1-9565-b2d2267cecc8', 'f24e0a31-4506-4cd1-a9f0-30e5fe9ae12e', 'SLAG', NOW() + INTERVAL 3 DAY + INTERVAL 8 HOUR, NOW() + INTERVAL 3 DAY + INTERVAL 9 HOUR, 'SCHEDULED');

/* 🚢 Waterside BC Schema */

-- 🔍 Insert Inspection Operations
INSERT INTO waterside.inspection_operations (performed_on, id, inspector_signature, status) VALUES
    (NULL, 'a1e6b6de-0001-48e7-91ac-7c676e640001', NULL, 'SCHEDULED'),
    (NULL, 'a1e6b6de-0002-48e7-91ac-7c676e640002', NULL, 'SCHEDULED'),
    (NOW() - INTERVAL 1 DAY, 'a1e6b6de-0003-48e7-91ac-7c676e640003', 'test_signature_999', 'COMPLETED');

-- 🛢 Insert Bunkering Operations
INSERT INTO waterside.bunkering_operations (performed_at, queued_at, id, status) VALUES
    (NULL, NOW() - INTERVAL 2 DAY, 'ea9c2e4b-0001-4e77-9120-11c6f12a0001', 'QUEUED'),
    (NOW() - INTERVAL 1 HOUR, NOW() - INTERVAL 6 DAY, 'ea9c2e4b-0002-4e77-9120-11c6f12a0002', 'COMPLETED'),
    (NULL, NOW() - INTERVAL 6 DAY, 'ea9c2e4b-0003-4e77-9120-11c6f12a0003', 'QUEUED');

-- Insert Shipping Orders
INSERT INTO waterside.shipping_orders (actual_arrival_date, actual_departure_date, scheduled_arrival_date, scheduled_departure_date,
                                       bunkering_operation_id, inspection_operation_id, reference_id, shipping_order_id, buyer_id, status, vessel_number) VALUES
    (
        NOW() - INTERVAL 7 DAY, NULL,
        NOW() - INTERVAL 7 DAY, NOW() + INTERVAL 7 DAY,
        'ea9c2e4b-0001-4e77-9120-11c6f12a0001',
        'a1e6b6de-0001-48e7-91ac-7c676e640001',
        '11111111-1111-1111-1111-111111111111',
        'd88bc876-0001-469e-8be0-00d3e6b00001',
        'be32da76-2ca5-4bee-9f3b-62aab7839622',
        'SHIP_DOCKED',
        'VES-001'
    ),
    (
        NOW() - INTERVAL 7 DAY, NULL,
        NOW() - INTERVAL 7 DAY, NOW() + INTERVAL 7 DAY,
        'ea9c2e4b-0002-4e77-9120-11c6f12a0002',
        'a1e6b6de-0002-48e7-91ac-7c676e640002',
        '22222222-2222-2222-2222-222222222222',
        'e17387cd-6f22-42e3-b274-479df7798793',
        'da27f14f-5c54-43cd-a6a0-5dc5c5c744fb',
        'SHIP_BUNKERED',
        'VES-002'
    ),
    (
        NOW() - INTERVAL 7 DAY, NULL,
        NOW() - INTERVAL 7 DAY, NOW() + INTERVAL 7 DAY,
        'ea9c2e4b-0003-4e77-9120-11c6f12a0003',
        'a1e6b6de-0003-48e7-91ac-7c676e640003',
        '33333333-3333-3333-3333-333333333333',
        '40b9a2a0-c266-42c1-8e5c-69d32cb659f1',
        '065a4145-7791-4017-8c6e-e0838c917d51',
        'SHIP_INSPECTED',
        'VES-003'
    );

/* 🧾 Invoicing BC Schema */

-- Add CHECK constraints for currency enforcement at the DB level (USD only)
ALTER TABLE invoicing.invoice_lines
    ADD CONSTRAINT chk_invoice_lines_currency_usd CHECK (currency = 'USD');

ALTER TABLE invoicing.raw_materials
    ADD CONSTRAINT chk_raw_materials_storage_price_currency_usd CHECK (storage_price_currency = 'USD'),
    ADD CONSTRAINT chk_raw_materials_unit_price_currency_usd CHECK (unit_price_currency = 'USD');

-- Insert Raw Materials
INSERT INTO invoicing.raw_materials(id, name, storage_price_amount, storage_price_currency, unit_price_amount, unit_price_currency) VALUES
    ('47087577-97bd-4829-bdbf-e10df1172f98', 'GYPSUM', 1, 'USD', 13, 'USD'),
    ('bf1092f8-d11d-47cf-9329-1444885eae54', 'IRON_ORE', 5, 'USD', 110, 'USD'),
    ('ee7d268c-e4bb-4a28-8010-ce5d231acda9', 'CEMENT', 3, 'USD', 95, 'USD'),
    ('b3f94b72-3821-4011-bc25-23f399af65a1', 'PETCOKE', 10, 'USD', 210, 'USD'),
    ('db77cee6-32cb-49c7-b5c2-cb41e7d5cd98', 'SLAG', 7, 'USD', 160, 'USD');

-- Insert Commission rate for current and upcoming year (%0.01)
INSERT INTO invoicing.yearly_commission_rates(rate, year) VALUES
    (0.01, 2025),
    (0.01, 2026);