USE `product_crud_db`;

CREATE TABLE IF NOT EXISTS `categories` (
    `id` INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(200) NOT NULL
);

CREATE TABLE IF NOT EXISTS `products` (
    `id` INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(200) NOT NULL,
    `code` VARCHAR(50) NOT NULL,
    `price` DOUBLE,
    `category_id` INT,
     FOREIGN KEY fk_products_categories (category_id) REFERENCES categories (id)
);
