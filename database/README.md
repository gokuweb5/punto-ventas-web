# Database Setup - Sistema Punto de Ventas

## Migraciones Disponibles

### 01 - Schema Inicial
Crea las tablas base del sistema (usuarios, clientes, productos, bodega, ventas).

### 02 - Datos de Prueba (si existe)
Inserta datos de ejemplo para desarrollo.

### 03 - Movimientos de Inventario
**NUEVA TABLA**: `movimientos_inventario` para auditoría y control de inventario.

## Cómo Ejecutar las Migraciones

### Opción 1: Desde MySQL Workbench
1. Abrir MySQL Workbench
2. Conectarse a la base de datos `punto_de_ventas`
3. Abrir el archivo `migrations/03_create_movimientos_inventario.sql`
4. Ejecutar el script (⚡ Execute)

### Opción 2: Desde Línea de Comandos
```bash
# Navegar a la carpeta del proyecto
cd c:\Users\Navarrete\Desktop\punto-ventas-web

# Ejecutar la migración
mysql -u root -p punto_de_ventas < database/migrations/03_create_movimientos_inventario.sql
```

### Opción 3: Copiar y Pegar
1. Abrir el archivo `migrations/03_create_movimientos_inventario.sql`
2. Copiar todo el contenido
3. Pegarlo en MySQL Workbench o cualquier cliente MySQL
4. Ejecutar

## Verificar Instalación

Ejecutar en MySQL:
```sql
USE punto_de_ventas;

-- Ver todas las tablas
SHOW TABLES;

-- Ver estructura de la nueva tabla
DESCRIBE movimientos_inventario;

-- Verificar que esté vacía
SELECT COUNT(*) FROM movimientos_inventario;
```

## Estructura de la Nueva Tabla

```sql
movimientos_inventario
├── IdMovimiento (PK, AUTO_INCREMENT)
├── IdProducto (FK -> productos)
├── TipoMovimiento (VARCHAR)
├── Cantidad (INT)
├── CantidadAnterior (INT)
├── CantidadNueva (INT)
├── Motivo (VARCHAR, nullable)
├── Referencia (VARCHAR, nullable)
├── IdUsuario (FK -> usuarios, nullable)
└── Fecha (DATETIME, default CURRENT_TIMESTAMP)
```

## Relaciones
- **productos**: Cada movimiento está asociado a un producto (CASCADE DELETE)
- **usuarios**: Cada movimiento puede tener un usuario responsable (SET NULL on delete)

## Índices
- `idx_producto`: Para búsquedas por producto
- `idx_tipo_movimiento`: Para filtrar por tipo
- `idx_fecha`: Para consultas por rango de fechas
- `idx_referencia`: Para buscar por ticket/referencia

## Notas Importantes

⚠️ **Backup**: Siempre hacer backup antes de ejecutar migraciones en producción.

⚠️ **Orden**: Ejecutar las migraciones en orden numérico.

⚠️ **Permisos**: Asegurarse de tener permisos de CREATE TABLE en la base de datos.
