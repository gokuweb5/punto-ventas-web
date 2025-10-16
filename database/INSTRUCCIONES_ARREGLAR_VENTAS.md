# 🔧 Instrucciones para Arreglar Tabla Ventas

## 📋 Contexto
La tabla `ventas` tiene columnas duplicadas o estructura incorrecta. Este documento guía el proceso de corrección.

## ✅ Scripts Creados

### 1. `fix_ventas_table.sql` (RECOMENDADO)
**Script consolidado que ejecuta todo el proceso en un solo paso**
- ✅ Respaldo automático
- ✅ Eliminación de tabla
- ✅ Recreación con estructura correcta
- ✅ Verificación de estructura

### 2. Scripts individuales (opcional)
- `04_backup_ventas.sql` - Solo respaldo
- `05_recreate_ventas.sql` - Solo recreación

## 🚀 Pasos de Ejecución

### Opción A: Script Consolidado (Recomendado)

```bash
# 1. Conectar a MySQL
mysql -u root -p punto_de_ventas

# 2. Ejecutar script consolidado
source database/migrations/fix_ventas_table.sql
```

### Opción B: Scripts Individuales

```bash
# 1. Conectar a MySQL
mysql -u root -p punto_de_ventas

# 2. Respaldar datos
source database/migrations/04_backup_ventas.sql

# 3. Recrear tabla
source database/migrations/05_recreate_ventas.sql
```

### Opción C: Desde MySQL Workbench

1. Abrir MySQL Workbench
2. Conectar a la base de datos `punto_de_ventas`
3. Abrir el archivo `fix_ventas_table.sql`
4. Ejecutar todo el script (⚡ icono de rayo o Ctrl+Shift+Enter)

## 📊 Estructura Final de la Tabla

```sql
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
    
    CONSTRAINT fk_ventas_producto FOREIGN KEY (IdProducto) REFERENCES productos(IdProducto),
    CONSTRAINT fk_ventas_cliente FOREIGN KEY (IdCliente) REFERENCES clientes(IdCliente),
    CONSTRAINT fk_ventas_usuario FOREIGN KEY (IdUsuario) REFERENCES usuarios(IdUsuario)
);
```

## 🔍 Verificación Post-Ejecución

```sql
-- Verificar estructura
DESCRIBE ventas;

-- Verificar que no hay datos (tabla nueva)
SELECT COUNT(*) FROM ventas;

-- Verificar claves foráneas
SHOW CREATE TABLE ventas;
```

## ⚠️ Notas Importantes

1. **Respaldo de Datos**: El script crea automáticamente `ventas_backup` antes de eliminar la tabla
2. **Restauración**: Si hay datos importantes en `ventas_backup`, descomentar la sección de restauración en el script
3. **Claves Foráneas**: La tabla tiene referencias a `productos`, `clientes` y `usuarios`
4. **Índices**: Se crean índices automáticos en `Ticket`, `Fecha`, `IdProducto` e `IdCliente` para mejor rendimiento

## 🎯 Siguiente Paso

Después de ejecutar el script:
1. ✅ Reiniciar la aplicación Spring Boot
2. ✅ Probar el módulo de ventas desde el frontend
3. ✅ Verificar que se pueden crear ventas correctamente
4. ✅ Verificar que el inventario se actualiza correctamente

## 📞 Problemas Comunes

### Error: "Table 'ventas' doesn't exist"
- **Solución**: Normal, la tabla se eliminó. El script la recreará automáticamente.

### Error: "Cannot add foreign key constraint"
- **Solución**: Verificar que las tablas `productos`, `clientes` y `usuarios` existen y tienen los campos correctos.

### Error: "Duplicate column name"
- **Solución**: Este es el problema que estamos arreglando. Ejecutar el script completo.
