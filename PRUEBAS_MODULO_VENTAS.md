# 🧪 Guía de Pruebas - Módulo de Ventas

## 📋 Pre-requisitos

Antes de probar el módulo de ventas, asegúrate de:

1. ✅ Ejecutar el script `fix_ventas_table.sql` en la base de datos
2. ✅ Reiniciar la aplicación Spring Boot
3. ✅ Tener el frontend corriendo en `http://localhost:5173`
4. ✅ Tener al menos:
   - 1 usuario registrado (para login)
   - 2-3 productos con stock disponible
   - 1-2 clientes registrados

## 🎯 Casos de Prueba

### 1. Prueba Básica: Venta Simple

**Objetivo**: Crear una venta básica con un producto

**Pasos**:
1. Ir a la página de Ventas
2. En el campo de búsqueda, escribir el código de un producto y presionar Enter
3. Verificar que el producto se agregue al carrito
4. Hacer clic en "Procesar Venta"
5. Verificar mensaje de éxito

**Resultado Esperado**:
- ✅ Producto agregado al carrito
- ✅ Total calculado correctamente
- ✅ Venta procesada exitosamente
- ✅ Inventario actualizado (verificar en módulo de productos)
- ✅ Carrito limpiado después de la venta

---

### 2. Prueba: Venta con Múltiples Productos

**Objetivo**: Crear una venta con varios productos diferentes

**Pasos**:
1. Agregar 3 productos diferentes al carrito
2. Verificar que cada producto muestre su precio y cantidad
3. Procesar la venta

**Resultado Esperado**:
- ✅ Todos los productos en el carrito
- ✅ Subtotal correcto
- ✅ Total final correcto
- ✅ Inventario de todos los productos actualizado

---

### 3. Prueba: Modificar Cantidad

**Objetivo**: Cambiar la cantidad de un producto en el carrito

**Pasos**:
1. Agregar un producto al carrito
2. Cambiar la cantidad a 5 en el input numérico
3. Verificar que el importe se actualice
4. Procesar la venta

**Resultado Esperado**:
- ✅ Cantidad actualizada en el carrito
- ✅ Importe recalculado correctamente
- ✅ Inventario descontado por la cantidad correcta

---

### 4. Prueba: Validación de Stock

**Objetivo**: Verificar que no se pueda vender más de lo disponible

**Pasos**:
1. Agregar un producto que tenga stock limitado (ej: 3 unidades)
2. Intentar cambiar la cantidad a más del stock disponible (ej: 10)
3. Verificar mensaje de error

**Resultado Esperado**:
- ✅ Mensaje de alerta: "Stock insuficiente. Disponible: X"
- ✅ Cantidad no se actualiza
- ✅ No se permite procesar la venta con cantidad inválida

---

### 5. Prueba: Producto Duplicado

**Objetivo**: Agregar el mismo producto dos veces

**Pasos**:
1. Agregar un producto al carrito
2. Buscar y agregar el mismo producto nuevamente
3. Verificar que la cantidad se incremente en lugar de duplicar la línea

**Resultado Esperado**:
- ✅ Solo una línea del producto en el carrito
- ✅ Cantidad incrementada automáticamente
- ✅ Total calculado correctamente

---

### 6. Prueba: Eliminar del Carrito

**Objetivo**: Remover un producto del carrito

**Pasos**:
1. Agregar 2 productos al carrito
2. Hacer clic en el icono de basura de uno de ellos
3. Verificar que se elimine

**Resultado Esperado**:
- ✅ Producto eliminado del carrito
- ✅ Total recalculado
- ✅ El otro producto permanece en el carrito

---

### 7. Prueba: Venta con Cliente

**Objetivo**: Asociar una venta a un cliente específico

**Pasos**:
1. Agregar productos al carrito
2. Seleccionar un cliente del dropdown
3. Procesar la venta
4. Verificar en la base de datos que la venta tenga el IdCliente correcto

**Resultado Esperado**:
- ✅ Cliente seleccionado correctamente
- ✅ Venta procesada con cliente asociado
- ✅ Registro en BD con IdCliente correcto

---

### 8. Prueba: Venta a Crédito

**Objetivo**: Marcar una venta como crédito

**Pasos**:
1. Agregar productos al carrito
2. Seleccionar un cliente
3. Marcar el checkbox "Venta a crédito"
4. Procesar la venta
5. Verificar en BD que el campo Credito = TRUE

**Resultado Esperado**:
- ✅ Checkbox marcado
- ✅ Venta procesada
- ✅ Campo Credito = TRUE en la base de datos

---

### 9. Prueba: Venta sin Cliente (Público General)

**Objetivo**: Procesar una venta sin cliente específico

**Pasos**:
1. Agregar productos al carrito
2. NO seleccionar ningún cliente
3. Procesar la venta

**Resultado Esperado**:
- ✅ Venta procesada exitosamente
- ✅ IdCliente = NULL en la base de datos
- ✅ Inventario actualizado correctamente

---

### 10. Prueba: Generación de Ticket

**Objetivo**: Verificar que cada venta tenga un ticket único

**Pasos**:
1. Procesar una venta con 2 productos
2. Verificar en la base de datos la tabla ventas
3. Confirmar que ambos productos tengan el mismo número de ticket
4. Procesar otra venta
5. Verificar que tenga un ticket diferente

**Resultado Esperado**:
- ✅ Todos los items de una venta comparten el mismo ticket
- ✅ Cada venta tiene un ticket único
- ✅ Formato: TKT-[timestamp]-[UUID]

---

### 11. Prueba: Producto No Encontrado

**Objetivo**: Buscar un producto que no existe

**Pasos**:
1. Escribir un código inexistente en el campo de búsqueda
2. Presionar Enter
3. Verificar mensaje de error

**Resultado Esperado**:
- ✅ Mensaje de alerta: "Producto no encontrado"
- ✅ Carrito sin cambios
- ✅ Campo de búsqueda limpiado

---

### 12. Prueba: Producto Sin Stock

**Objetivo**: Intentar agregar un producto sin existencias

**Pasos**:
1. Buscar un producto con existencia = 0
2. Intentar agregarlo al carrito

**Resultado Esperado**:
- ✅ Mensaje de alerta: "Producto sin stock"
- ✅ Producto NO se agrega al carrito

---

## 🔍 Verificaciones en Base de Datos

Después de cada venta, verificar:

```sql
-- Ver últimas ventas
SELECT * FROM ventas ORDER BY Fecha DESC LIMIT 10;

-- Ver inventario actualizado
SELECT p.Producto, b.Existencia 
FROM productos p 
JOIN bodega b ON p.IdProducto = b.IdProducto;

-- Ver movimientos de inventario
SELECT * FROM movimientos_inventario ORDER BY FechaMovimiento DESC LIMIT 10;

-- Ver ventas por ticket
SELECT v.Ticket, p.Producto, v.Cantidad, v.Precio, v.Importe, v.Fecha
FROM ventas v
JOIN productos p ON v.IdProducto = p.IdProducto
WHERE v.Ticket = 'TKT-XXXXXXXX'
ORDER BY v.Fecha;
```

---

## 📊 Validaciones de Integridad

### Verificar Consistencia de Inventario

```sql
-- El stock en bodega debe coincidir con las entradas menos las salidas
SELECT 
    p.Producto,
    b.Existencia AS stock_actual,
    (SELECT COALESCE(SUM(Cantidad), 0) FROM movimientos_inventario WHERE IdProducto = p.IdProducto AND TipoMovimiento = 'Entrada') AS total_entradas,
    (SELECT COALESCE(SUM(Cantidad), 0) FROM movimientos_inventario WHERE IdProducto = p.IdProducto AND TipoMovimiento = 'Salida') AS total_salidas,
    ((SELECT COALESCE(SUM(Cantidad), 0) FROM movimientos_inventario WHERE IdProducto = p.IdProducto AND TipoMovimiento = 'Entrada') - 
     (SELECT COALESCE(SUM(Cantidad), 0) FROM movimientos_inventario WHERE IdProducto = p.IdProducto AND TipoMovimiento = 'Salida')) AS stock_calculado
FROM productos p
JOIN bodega b ON p.IdProducto = b.IdProducto;
```

---

## ✅ Checklist Final

- [ ] Todas las ventas se registran correctamente
- [ ] El inventario se actualiza automáticamente
- [ ] Los tickets son únicos y consistentes
- [ ] Las validaciones de stock funcionan
- [ ] Las ventas con cliente se asocian correctamente
- [ ] Las ventas a crédito se marcan correctamente
- [ ] El carrito se limpia después de cada venta
- [ ] Los totales se calculan correctamente
- [ ] Los descuentos se aplican correctamente
- [ ] No hay errores en la consola del navegador
- [ ] No hay errores en los logs del backend

---

## 🐛 Problemas Comunes

### Error: "Cannot add foreign key constraint"
**Solución**: Ejecutar el script `fix_ventas_table.sql` completo

### Error: "Stock insuficiente" cuando hay stock
**Solución**: Verificar que la tabla `bodega` esté actualizada

### Error: "Usuario no encontrado"
**Solución**: Verificar que el usuario esté autenticado correctamente

### El carrito no se actualiza
**Solución**: Verificar que el frontend esté conectado al backend (revisar CORS)

---

## 🎉 Sistema Completado

Si todas las pruebas pasan, el sistema de facturación e inventarios está **100% funcional** y listo para producción.

### Funcionalidades Implementadas:
✅ Gestión de productos
✅ Control de inventario
✅ Gestión de clientes
✅ Módulo de ventas completo
✅ Actualización automática de inventario
✅ Validaciones de stock
✅ Ventas a crédito
✅ Generación de tickets
✅ Asociación de ventas a clientes
