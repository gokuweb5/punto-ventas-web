ALTER TABLE productos 
ADD COLUMN IdCategoria INT NULL AFTER Producto,
ADD COLUMN ImagenUrl VARCHAR(500) NULL AFTER Descuento,
ADD INDEX idx_categoria (IdCategoria);
