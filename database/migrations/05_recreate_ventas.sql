-- =====================================================
-- SCRIPT DE RECREACIÓN: Tabla ventas
-- Fecha: 2025-10-06
-- Descripción: Eliminar y recrear tabla ventas con estructura correcta
-- =====================================================

-- 1. ELIMINAR TABLA EXISTENTE
DROP TABLE IF EXISTS ventas;

-- 2. CREAR TABLA CON ESTRUCTURA CORRECTA
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
    
    -- Claves foráneas
    CONSTRAINT fk_ventas_producto FOREIGN KEY (IdProducto) REFERENCES productos(IdProducto),
    CONSTRAINT fk_ventas_cliente FOREIGN KEY (IdCliente) REFERENCES clientes(IdCliente),
    CONSTRAINT fk_ventas_usuario FOREIGN KEY (IdUsuario) REFERENCES usuarios(IdUsuario),
    
    -- Índices para mejorar rendimiento
    INDEX idx_ventas_ticket (Ticket),
    INDEX idx_ventas_fecha (Fecha),
    INDEX idx_ventas_producto (IdProducto),
    INDEX idx_ventas_cliente (IdCliente)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 3. RESTAURAR DATOS DESDE RESPALDO (si existe)
-- Nota: Ejecutar solo si hay datos importantes en ventas_backup
-- INSERT INTO ventas (Ticket, IdProducto, Codigo, Descripcion, Cantidad, Precio, Descuento, Importe, IdCliente, IdUsuario, Credito, Fecha)
-- SELECT Ticket, IdProducto, Codigo, Descripcion, Cantidad, Precio, Descuento, Importe, IdCliente, IdUsuario, Credito, Fecha
-- FROM ventas_backup;

-- 4. VERIFICAR ESTRUCTURA
DESCRIBE ventas;

-- 5. VERIFICAR DATOS RESTAURADOS (si se ejecutó el INSERT)
-- SELECT COUNT(*) AS total_registros_restaurados FROM ventas;
