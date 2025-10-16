ALTER TABLE productos 
ADD COLUMN IF NOT EXISTS Caracteristicas TEXT NULL AFTER ImagenUrl,
ADD COLUMN IF NOT EXISTS CodigoBarras VARCHAR(100) NULL AFTER Caracteristicas,
ADD COLUMN IF NOT EXISTS NumeroSerie VARCHAR(100) NULL AFTER CodigoBarras;

SELECT 'Columnas agregadas exitosamente' AS status;

DESCRIBE productos;
