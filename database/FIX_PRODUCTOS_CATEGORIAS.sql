SET @dbname = DATABASE();
SET @tablename = 'productos';
SET @columnname1 = 'IdCategoria';
SET @columnname2 = 'ImagenUrl';
SET @preparedStatement1 = (SELECT IF(
  (
    SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
    WHERE
      (table_name = @tablename)
      AND (table_schema = @dbname)
      AND (column_name = @columnname1)
  ) > 0,
  'SELECT 1',
  'ALTER TABLE productos ADD COLUMN IdCategoria INT NULL AFTER Producto'
));
PREPARE alterIfNotExists1 FROM @preparedStatement1;
EXECUTE alterIfNotExists1;
DEALLOCATE PREPARE alterIfNotExists1;

SET @preparedStatement2 = (SELECT IF(
  (
    SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
    WHERE
      (table_name = @tablename)
      AND (table_schema = @dbname)
      AND (column_name = @columnname2)
  ) > 0,
  'SELECT 1',
  'ALTER TABLE productos ADD COLUMN ImagenUrl VARCHAR(500) NULL AFTER Descuento'
));
PREPARE alterIfNotExists2 FROM @preparedStatement2;
EXECUTE alterIfNotExists2;
DEALLOCATE PREPARE alterIfNotExists2;

SELECT 'Columnas verificadas y agregadas si no existian' AS status;
