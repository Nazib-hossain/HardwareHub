-- ===================================================================
-- V2__seed_data.sql - Seed Default Users, Categories & Sample Products
-- ===================================================================

-- Default admin user (password: admin123, BCrypt-hashed)
INSERT INTO app_user (username, password, role, enabled) VALUES
    ('admin', '$2a$10$2LNC55pq.hWKszJszMTmk.C0q9kKS9qeDKlnyRW4fnedbuA.ZYRQi', 'ADMIN', TRUE),
    ('user',  '$2a$10$2LNC55pq.hWKszJszMTmk.C0q9kKS9qeDKlnyRW4fnedbuA.ZYRQi', 'USER',  TRUE);


-- Sample categories
INSERT INTO categories (name, description) VALUES
    ('GPU',         'Graphics Processing Units for gaming and compute workloads'),
    ('CPU',         'Central Processing Units — desktop and server processors'),
    ('RAM',         'Memory modules — DDR4, DDR5 DIMM and SO-DIMM'),
    ('Storage',     'SSDs, HDDs, and NVMe drives'),
    ('Motherboard', 'ATX, Micro-ATX, and Mini-ITX motherboards'),
    ('PSU',         'Power Supply Units — modular and non-modular'),
    ('Cooling',     'Air coolers, AIO liquid coolers, and thermal paste'),
    ('Case',        'PC cases — tower, mid-tower, and SFF'),
    ('Peripheral',  'Keyboards, mice, monitors, and headsets'),
    ('Networking',  'Network cards, routers, and switches');

-- Sample products
INSERT INTO products (name, description, price, stock_quantity, image_url, category_id) VALUES
    ('NVIDIA RTX 5090',            '24GB GDDR7 — Flagship GPU for 4K gaming',           1999.99, 15,  NULL, 1),
    ('AMD Radeon RX 9070 XT',      '16GB GDDR6 — High-performance 1440p GPU',           549.99,  30,  NULL, 1),
    ('Intel Core Ultra 9 285K',    '24 cores / 24 threads — 5.7 GHz boost',             589.99,  25,  NULL, 2),
    ('AMD Ryzen 9 9950X',          '16 cores / 32 threads — 5.7 GHz boost',             549.99,  20,  NULL, 2),
    ('Corsair Vengeance DDR5 32GB', '2x16GB DDR5-6000 CL30 Kit',                        124.99,  50,  NULL, 3),
    ('Samsung 990 Pro 2TB',        'PCIe 4.0 NVMe M.2 SSD — 7450 MB/s read',           169.99,  40,  NULL, 4),
    ('ASUS ROG Maximus Z890 Hero', 'Intel Z890 ATX Motherboard — DDR5, Wi-Fi 7',       629.99,  10,  NULL, 5),
    ('Corsair RM1000x',            '1000W 80+ Gold Fully Modular ATX PSU',              189.99,  35,  NULL, 6),
    ('Noctua NH-D15 G2',           'Dual-tower CPU air cooler — 150W TDP',               109.99,  45,  NULL, 7),
    ('Lian Li O11 Dynamic EVO',    'Mid-tower ATX case — tempered glass',                169.99,  20,  NULL, 8);

-- Sample suppliers
INSERT INTO suppliers (name, contact_email, phone) VALUES
    ('TechDistro Global',   'sales@techdistro.com',    '+1-800-555-0101'),
    ('MicroParts Inc.',     'orders@microparts.com',   '+1-800-555-0202'),
    ('SiliconWave Supply',  'info@siliconwave.com',    '+1-800-555-0303');

-- Link products to suppliers
INSERT INTO product_suppliers (product_id, supplier_id) VALUES
    (1, 1), (1, 3),
    (2, 1), (2, 2),
    (3, 2), (3, 3),
    (4, 1),
    (5, 2), (5, 3),
    (6, 1), (6, 2),
    (7, 3),
    (8, 1),
    (9, 2),
    (10, 3);
