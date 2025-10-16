DROP TABLE IF EXISTS movimientos_inventario;

CREATE TABLE movimientos_inventario (
    IdMovimiento INT AUTO_INCREMENT PRIMARY KEY,
    IdProducto INT NOT NULL,
    TipoMovimiento VARCHAR(20) NOT NULL,
    Cantidad INT NOT NULL,
    CantidadAnterior INT NOT NULL,
    CantidadNueva INT NOT NULL,
    Motivo VARCHAR(255) NULL,
    Referencia VARCHAR(100) NULL,
    IdUsuario INT NULL,
    Fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_producto (IdProducto),
    INDEX idx_tipo_movimiento (TipoMovimiento),
    INDEX idx_fecha (Fecha),
    INDEX idx_referencia (Referencia)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
