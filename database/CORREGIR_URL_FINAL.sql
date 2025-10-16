-- Corregir URL de imagen (sin /api/)
UPDATE productos 
SET ImagenUrl = 'http://localhost:8091/imagenes/PROD001.png' 
WHERE IdProducto = 1;

-- Verificar
SELECT IdProducto, Codigo, Producto, ImagenUrl FROM productos WHERE IdProducto = 1;
