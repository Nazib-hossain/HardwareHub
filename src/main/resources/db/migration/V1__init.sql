-- ===================================================================
-- V1__init.sql - Initial Schema for Hardware Inventory Platform
-- ===================================================================

-- Categories table
CREATE TABLE categories (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(100)  NOT NULL UNIQUE,
    description VARCHAR(500)
);

-- Products table (FK → categories)
CREATE TABLE products (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    name            VARCHAR(200) NOT NULL,
    description     VARCHAR(1000),
    price           DECIMAL(10, 2) NOT NULL,
    stock_quantity  INT NOT NULL DEFAULT 0,
    image_url       VARCHAR(500),
    category_id     BIGINT,
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_product_category FOREIGN KEY (category_id) REFERENCES categories(id)
);

-- Suppliers table
CREATE TABLE suppliers (
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    name          VARCHAR(200)  NOT NULL,
    contact_email VARCHAR(200),
    phone         VARCHAR(50)
);

-- Product-Supplier join table (Many-to-Many)
CREATE TABLE product_suppliers (
    product_id  BIGINT NOT NULL,
    supplier_id BIGINT NOT NULL,
    PRIMARY KEY (product_id, supplier_id),
    CONSTRAINT fk_ps_product  FOREIGN KEY (product_id)  REFERENCES products(id)  ON DELETE CASCADE,
    CONSTRAINT fk_ps_supplier FOREIGN KEY (supplier_id) REFERENCES suppliers(id) ON DELETE CASCADE
);

-- Application users table
CREATE TABLE app_user (
    id       BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role     VARCHAR(50)  NOT NULL DEFAULT 'USER',
    enabled  BOOLEAN      NOT NULL DEFAULT TRUE
);
