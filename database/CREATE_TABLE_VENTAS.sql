DROP TABLE IF EXISTS ventas;

CREATE TABLE ventas (
    IdVenta INT AUTO_INCREMENT PRIMARY KEY,
    Ticket VARCHAR(50),
    IdProducto INT NOT NULL,
    Codigo VARCHAR(50),
    Descripcion VARCHAR(255),
    Cantidad INT NOT NULL,
    Precio DECIMAL(10, 2) NOT NULL,
    Descuento DECIMAL(10, 2) DEFAULT 0.00,
    Importe DECIMAL(10, 2) NOT NULL,
    IdCliente INT,
    IdUsuario INT,
    Credito BOOLEAN DEFAULT FALSE,
    Fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_ventas_ticket (Ticket),
    INDEX idx_ventas_fecha (Fecha),
    INDEX idx_ventas_producto (IdProducto),
    INDEX idx_ventas_cliente (IdCliente)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
