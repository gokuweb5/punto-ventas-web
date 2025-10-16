# 🎯 Instrucciones: Activar Sistema de Categorías

## 💡 Concepto: Sistema 100% Flexible

El sistema **NO incluye categorías predefinidas**. El administrador crea las categorías según su tipo de negocio:

- 🖥️ **Tienda de Computadoras**: Hardware, Software, Periféricos, etc.
- 🚗 **Tienda Automotriz**: Repuestos, Accesorios, Lubricantes, etc.
- 👕 **Tienda de Ropa**: Hombre, Mujer, Niños, Deportiva, etc.
- 🍔 **Restaurante**: Comidas, Bebidas, Postres, etc.

---

## 📋 Paso 1: Ejecutar Script SQL

1. Abrir phpMyAdmin
2. Seleccionar base de datos `punto_de_ventas`
3. Ir a pestaña SQL
4. Copiar y pegar el contenido de: **`SETUP_CATEGORIAS_COMPLETO.sql`**
5. Ejecutar

Esto creará:
- ✅ Tabla `categorias` (vacía, lista para usar)
- ✅ Campos `IdCategoria` e `ImagenUrl` en tabla `productos`

---

## 🔄 Paso 2: Reiniciar Backend

1. Detener la aplicación Spring Boot
2. Iniciar nuevamente

---

## 🎨 Paso 3: Crear tus Categorías

1. Ir a `http://localhost:5173`
2. Login
3. Hacer clic en **"Categorías"** en el menú lateral
4. La tabla estará vacía - ¡Perfecto!
5. Hacer clic en **"Nueva Categoría"**
6. Crear las categorías de tu negocio

### Funcionalidades Disponibles:
- ✅ **Crear** categorías personalizadas
- ✅ **Editar** categoría existente
- ✅ **Desactivar** categoría
- ✅ **Ver** todas las categorías

---

## 📦 Paso 4: Actualizar Productos (Próximo)

Después de probar categorías, actualizaremos el formulario de productos para:
- Seleccionar categoría
- Agregar URL de imagen
- Mostrar imagen en la lista

---

## 🎫 Verificar Ticket de Venta

Para ver el ticket único de tu venta anterior, ejecuta en phpMyAdmin:

```sql
SELECT Ticket, Descripcion, Cantidad, Precio, Importe, Fecha 
FROM ventas 
ORDER BY Fecha DESC 
LIMIT 1;
```

El ticket tiene formato: `TKT-[timestamp]-[UUID]`

---

## ✨ Lo que Viene

1. **Categorías** ✅ (Listo para probar)
2. **Imágenes en productos** (Siguiente)
3. **Búsqueda mejorada** (Siguiente)
4. **Facturas PDF** (Siguiente)
