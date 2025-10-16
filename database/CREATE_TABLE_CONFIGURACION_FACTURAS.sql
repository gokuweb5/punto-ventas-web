-- Tabla para configuración de rangos de facturas
CREATE TABLE IF NOT EXISTS configuracion_facturas (
    Id INT AUTO_INCREMENT PRIMARY KEY,
    NumeroInicio INT NOT NULL,
    NumeroFin INT NOT NULL,
    NumeroActual INT NOT NULL,
    Activo BOOLEAN DEFAULT TRUE,
    FechaCreacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FechaAgotamiento TIMESTAMP NULL,
    CONSTRAINT chk_rango CHECK (NumeroFin > NumeroInicio),
    CONSTRAINT chk_actual CHECK (NumeroActual >= NumeroInicio AND NumeroActual <= NumeroFin)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Insertar configuración inicial (ejemplo: facturas del 1 al 1000)
INSERT INTO configuracion_facturas (NumeroInicio, NumeroFin, NumeroActual, Activo) 
VALUES (1, 1000, 1, TRUE);

SELECT 'Tabla de configuración de facturas creada exitosamente' AS status;
