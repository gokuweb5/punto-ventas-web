-- Tabla para registrar movimientos de inventario
-- Permite llevar un historial completo de entradas, salidas y ajustes

CREATE TABLE IF NOT EXISTS movimientos_inventario (
    IdMovimiento INT AUTO_INCREMENT PRIMARY KEY,
    IdProducto INT NOT NULL,
    TipoMovimiento VARCHAR(20) NOT NULL COMMENT 'ENTRADA, SALIDA, AJUSTE_ENTRADA, AJUSTE_SALIDA, VENTA, DEVOLUCION',
    Cantidad INT NOT NULL,
    CantidadAnterior INT NOT NULL,
    CantidadNueva INT NOT NULL,
    Motivo VARCHAR(255) NULL COMMENT 'Descripción del motivo del movimiento',
    Referencia VARCHAR(100) NULL COMMENT 'Número de ticket, orden de compra, etc.',
    IdUsuario INT NULL,
    Fecha DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_movimiento_producto FOREIGN KEY (IdProducto) 
        REFERENCES productos(IdProducto) ON DELETE CASCADE,
    CONSTRAINT fk_movimiento_usuario FOREIGN KEY (IdUsuario) 
        REFERENCES usuarios(IdUsuario) ON DELETE SET NULL,
    
    INDEX idx_producto (IdProducto),
    INDEX idx_tipo_movimiento (TipoMovimiento),
    INDEX idx_fecha (Fecha),
    INDEX idx_referencia (Referencia)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
COMMENT='Registro de movimientos de inventario para auditoría y control';
