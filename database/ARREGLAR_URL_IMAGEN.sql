-- Actualizar la URL de la imagen del producto existente
UPDATE productos 
SET ImagenUrl = '/imagenes/PROD001.png' 
WHERE IdProducto = 1;

-- Verificar
SELECT IdProducto, Codigo, Producto, ImagenUrl FROM productos WHERE IdProducto = 1;
