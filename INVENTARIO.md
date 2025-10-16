# Sistema de Inventario - Punto de Ventas

## Descripción General

El sistema de inventario está diseñado para llevar un control preciso de las existencias de productos, registrando todos los movimientos (entradas, salidas, ajustes) con trazabilidad completa.

## Estructura de Base de Datos

### Tabla: `productos`
Almacena la información básica de los productos (código, nombre, precio, categoría, etc.).

### Tabla: `bodega`
Mantiene el stock actual de cada producto. Relación **1:1** con productos.
- `Id`: Identificador único
- `IdProducto`: Referencia al producto
- `Existencia`: Cantidad actual en inventario
- `Fecha`: Fecha de creación del registro

### Tabla: `movimientos_inventario` (Nueva)
Registra el historial completo de movimientos de inventario para auditoría.
- `IdMovimiento`: Identificador único
- `IdProducto`: Producto afectado
- `TipoMovimiento`: ENTRADA, SALIDA, AJUSTE_ENTRADA, AJUSTE_SALIDA, VENTA, DEVOLUCION
- `Cantidad`: Cantidad del movimiento
- `CantidadAnterior`: Stock antes del movimiento
- `CantidadNueva`: Stock después del movimiento
- `Motivo`: Descripción del motivo
- `Referencia`: Número de ticket, orden de compra, etc.
- `IdUsuario`: Usuario que realizó el movimiento
- `Fecha`: Timestamp del movimiento

## Arquitectura del Sistema

### Entidades JPA

#### `Producto.java`
- Información del producto
- **Relación unidireccional** con Bodega (manejada desde Bodega)

#### `Bodega.java`
- Stock actual del producto
- Relación `@OneToOne` con Producto

#### `MovimientoInventario.java`
- Historial de movimientos
- Relación `@ManyToOne` con Producto y Usuario

### Servicios

#### `ProductoService.java`
- CRUD de productos
- Al crear un producto, automáticamente crea su registro en bodega
- Obtiene existencias desde `BodegaRepository`

#### `InventarioService.java` (Nuevo)
Servicio centralizado para gestionar inventario:

**Métodos principales:**
- `registrarEntrada()`: Registra entradas de inventario (compras, devoluciones)
- `registrarSalida()`: Registra salidas de inventario (ventas, mermas)
- `ajustarInventario()`: Ajusta el inventario a una cantidad específica
- `obtenerHistorialProducto()`: Consulta el historial de movimientos
- `obtenerExistencia()`: Obtiene el stock actual

**Características:**
- Transaccional: Garantiza consistencia de datos
- Auditoría completa: Registra cada movimiento con usuario y fecha
- Validaciones: Verifica stock suficiente antes de salidas
- Trazabilidad: Mantiene cantidad anterior y nueva en cada movimiento

#### `VentaService.java`
- Procesa ventas
- **Actualizado** para usar `InventarioService.registrarSalida()`
- Registra automáticamente el movimiento de inventario con referencia al ticket

## Flujo de Operaciones

### 1. Crear Producto
```java
ProductoService.crearProducto(productoDTO)
  ├─ Crea el producto en la tabla productos
  └─ Crea registro en bodega con existencia inicial
```

### 2. Registrar Venta
```java
VentaService.procesarVenta(ventaRequest)
  ├─ Valida existencia en bodega
  ├─ Crea registro de venta
  └─ InventarioService.registrarSalida()
      ├─ Actualiza existencia en bodega
      └─ Crea registro en movimientos_inventario
```

### 3. Entrada de Inventario (Compra)
```java
InventarioService.registrarEntrada(idProducto, cantidad, motivo, referencia, idUsuario)
  ├─ Incrementa existencia en bodega
  └─ Registra movimiento tipo ENTRADA
```

### 4. Ajuste de Inventario
```java
InventarioService.ajustarInventario(idProducto, nuevaCantidad, motivo, idUsuario)
  ├─ Establece nueva cantidad en bodega
  └─ Registra movimiento tipo AJUSTE_ENTRADA o AJUSTE_SALIDA
```

## Tipos de Movimientos

| Tipo | Descripción | Uso |
|------|-------------|-----|
| `ENTRADA` | Entrada de mercancía | Compras, recepciones |
| `SALIDA` | Salida de mercancía | Mermas, transferencias |
| `AJUSTE_ENTRADA` | Ajuste positivo | Corrección de inventario |
| `AJUSTE_SALIDA` | Ajuste negativo | Corrección de inventario |
| `VENTA` | Venta a cliente | Registrado automáticamente |
| `DEVOLUCION` | Devolución de cliente | Entrada por devolución |

## Instalación

### 1. Ejecutar Migración SQL
```sql
-- Ejecutar en MySQL
source database/migrations/03_create_movimientos_inventario.sql
```

O manualmente:
```bash
mysql -u root -p punto_de_ventas < database/migrations/03_create_movimientos_inventario.sql
```

### 2. Verificar Tablas
```sql
USE punto_de_ventas;
SHOW TABLES;
DESCRIBE movimientos_inventario;
```

### 3. Compilar Backend
```bash
mvn clean compile
mvn spring-boot:run
```

## API Endpoints (Futuros)

Para implementar en el futuro:

```
POST   /api/inventario/entrada       - Registrar entrada
POST   /api/inventario/salida        - Registrar salida
POST   /api/inventario/ajuste        - Ajustar inventario
GET    /api/inventario/producto/{id} - Historial de producto
GET    /api/inventario/movimientos   - Todos los movimientos
```

## Ventajas del Sistema

1. **Trazabilidad completa**: Cada movimiento queda registrado
2. **Auditoría**: Se sabe quién, cuándo y por qué se modificó el inventario
3. **Consistencia**: Transacciones garantizan integridad de datos
4. **Escalabilidad**: Fácil agregar nuevos tipos de movimientos
5. **Reportes**: Base para generar reportes de inventario

## Mejoras Futuras

- [ ] Dashboard de inventario en frontend
- [ ] Alertas de stock mínimo
- [ ] Reportes de movimientos por fecha/usuario
- [ ] Exportación de historial a Excel/PDF
- [ ] Inventario por ubicación/sucursal
- [ ] Lotes y fechas de vencimiento
- [ ] Códigos de barras
- [ ] Integración con proveedores

## Notas Importantes

⚠️ **Relación Producto-Bodega**: Se cambió de bidireccional a unidireccional para evitar problemas de cascada y lazy loading. Ahora se accede a la bodega mediante `BodegaRepository.findByProducto_IdProducto()`.

⚠️ **Transacciones**: Todos los métodos que modifican inventario son `@Transactional` para garantizar consistencia.

⚠️ **Validaciones**: El sistema valida stock suficiente antes de permitir salidas.
