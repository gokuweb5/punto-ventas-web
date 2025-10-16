-- =====================================================
-- SCRIPT COMPLETO: Arreglar Tabla Ventas
-- Fecha: 2025-10-06
-- Descripción: Respaldo, eliminación y recreación de tabla ventas
-- =====================================================

-- PASO 1: RESPALDO DE DATOS EXISTENTES
-- =====================================================
DROP TABLE IF EXISTS ventas_backup;

CREATE TABLE ventas_backup AS
SELECT * FROM ventas;

SELECT 'RESPALDO COMPLETADO' AS status, COUNT(*) AS registros_respaldados FROM ventas_backup;

-- PASO 2: ELIMINAR TABLA EXISTENTE
-- =====================================================
DROP TABLE IF EXISTS ventas;

SELECT 'TABLA ELIMINADA' AS status;

-- PASO 3: RECREAR TABLA CON ESTRUCTURA CORRECTA
-- =====================================================
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

SELECT 'TABLA RECREADA' AS status;

-- PASO 4: RESTAURAR DATOS (OPCIONAL - Descomentar si hay datos importantes)
-- =====================================================
-- INSERT INTO ventas (Ticket, IdProducto, Codigo, Descripcion, Cantidad, Precio, Descuento, Importe, IdCliente, IdUsuario, Credito, Fecha)
-- SELECT Ticket, IdProducto, Codigo, Descripcion, Cantidad, Precio, Descuento, Importe, IdCliente, IdUsuario, Credito, Fecha
-- FROM ventas_backup
-- WHERE IdProducto IS NOT NULL;

-- SELECT 'DATOS RESTAURADOS' AS status, COUNT(*) AS registros_restaurados FROM ventas;

-- PASO 5: VERIFICAR ESTRUCTURA FINAL
-- =====================================================
DESCRIBE ventas;

-- PASO 6: LIMPIAR TABLA DE RESPALDO (OPCIONAL - Descomentar después de verificar)
-- =====================================================
-- DROP TABLE IF EXISTS ventas_backup;
-- SELECT 'RESPALDO ELIMINADO' AS status;
