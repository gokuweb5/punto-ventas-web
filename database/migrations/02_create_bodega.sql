-- Tabla bodega para control de inventario
CREATE TABLE IF NOT EXISTS bodega (
    Id INT AUTO_INCREMENT PRIMARY KEY,
    IdProducto INT NOT NULL,
    Existencia INT NOT NULL DEFAULT 0,
    Fecha DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_bodega_producto FOREIGN KEY (IdProducto) 
        REFERENCES productos(IdProducto) ON DELETE CASCADE,
    
    CONSTRAINT uk_bodega_producto UNIQUE (IdProducto),
    INDEX idx_bodega_producto (IdProducto)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
COMMENT='Control de inventario por producto';
