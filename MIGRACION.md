# 📋 Guía de Migración desde Java Swing

Esta guía explica cómo migrar datos y funcionalidades del sistema Swing original al nuevo sistema web.

## 🗄️ Compatibilidad de Base de Datos

### ✅ Tablas Compatibles

El nuevo sistema es **100% compatible** con la base de datos existente:

| Tabla Original | Estado | Notas |
|----------------|--------|-------|
| `usuarios` | ✅ Compatible | Actualizar contraseñas a BCrypt |
| `productos` | ✅ Compatible | Sin cambios |
| `bodega` | ✅ Compatible | Sin cambios |
| `clientes` | ✅ Compatible | Sin cambios |
| `proveedores` | ✅ Compatible | Sin cambios |
| `ventas` | ✅ Compatible | Sin cambios |
| `compras` | ✅ Compatible | Sin cambios |
| `cajas` | ✅ Compatible | Sin cambios |
| `roles` | ✅ Compatible | Sin cambios |

### 🔄 Migración de Contraseñas

El sistema Swing usa **MD5** (inseguro), el nuevo usa **BCrypt** (seguro).

#### Opción 1: Actualizar Todas las Contraseñas

```sql
-- Generar hash BCrypt para cada usuario
-- Ejemplo: contraseña "admin123"
UPDATE usuarios 
SET Password = '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cyhQQopqkqLDQvlnQmGPvQwVBhXMa'
WHERE Usuario = 'admin';

-- Repetir para cada usuario
```

#### Opción 2: Migración Automática (Recomendado)

Crea un script de migración que:
1. Lee usuarios con MD5
2. Genera hash BCrypt
3. Actualiza la tabla

```java
// Ejemplo de código para migración
@Service
public class PasswordMigrationService {
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    public void migrarContraseñas() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        
        for (Usuario usuario : usuarios) {
            // Asume que la contraseña original es conocida
            String nuevaPassword = passwordEncoder.encode("temporal123");
            usuario.setPassword(nuevaPassword);
            usuarioRepository.save(usuario);
        }
    }
}
```

## 🔀 Mapeo de Funcionalidades

### Login (ViewModels.LoginVM → AuthController)

**Swing:**
```java
var login = new LoginVM();
Object[] objects = login.Verificar();
var listUsuario = (List<TUsuarios>) objects[0];
```

**Web:**
```javascript
const response = await authAPI.login({ usuario, password });
const { token, ...user } = response.data;
setAuth(token, user);
```

### Productos (ProductosVM → ProductoService)

**Swing:**
```java
ProductosModel model = new ProductosModel();
List<TProductos> productos = model.getProductos();
```

**Web:**
```java
@Service
public class ProductoService {
    public List<ProductoDTO> obtenerTodosLosProductos() {
        return productoRepository.findAllWithBodega();
    }
}
```

### Ventas (VentasVM → VentaService)

**Swing:**
```java
// Proceso de venta en Swing
TVentas_temporal venta = new TVentas_temporal();
venta.setIdProducto(producto.getId());
// ...
```

**Web:**
```java
@Transactional
public List<Venta> procesarVenta(VentaRequest request, Integer idUsuario) {
    // Validación, cálculos y guardado
    // Actualización automática de inventario
}
```

## 📊 Migración de Datos Existentes

### 1. Backup de Seguridad

```bash
mysqldump -u root -p punto_de_ventas > backup_antes_migracion.sql
```

### 2. Verificar Integridad

```sql
-- Verificar productos sin bodega
SELECT p.* FROM productos p
LEFT JOIN bodega b ON p.IdProducto = b.IdProducto
WHERE b.Id IS NULL;

-- Crear registros de bodega faltantes
INSERT INTO bodega (IdProducto, Existencia)
SELECT IdProducto, 0 FROM productos
WHERE IdProducto NOT IN (SELECT IdProducto FROM bodega);
```

### 3. Limpiar Datos Temporales

```sql
-- Limpiar tablas temporales si existen
TRUNCATE TABLE ventas_temporal;
TRUNCATE TABLE compras_temporal;
```

## 🔄 Convivencia de Sistemas

Puedes usar **ambos sistemas simultáneamente** con la misma base de datos:

### ⚠️ Consideraciones

1. **Contraseñas**: Los usuarios creados en el sistema web no podrán iniciar sesión en Swing (BCrypt vs MD5)
2. **Transacciones**: Ambos sistemas pueden leer/escribir datos sin conflictos
3. **Cajas**: Coordinar apertura/cierre de cajas entre sistemas

### 🔒 Recomendación

Para evitar conflictos:
- **Fase 1**: Usa ambos sistemas en paralelo (1-2 semanas)
- **Fase 2**: Migra completamente al sistema web
- **Fase 3**: Mantén el Swing como respaldo

## 📈 Ventajas del Nuevo Sistema

| Característica | Swing | Web |
|----------------|-------|-----|
| **Acceso** | Solo local | Desde cualquier lugar |
| **Usuarios simultáneos** | 1 | Ilimitados |
| **Actualizaciones** | Reinstalar en cada PC | Actualizar servidor |
| **Seguridad** | MD5 | BCrypt + JWT |
| **UI** | Desktop fija | Responsive |
| **Mantenimiento** | Difícil | Fácil |
| **Escalabilidad** | Limitada | Alta |

## 🛠️ Personalización

### Agregar Nuevos Campos

Si necesitas agregar campos personalizados:

1. **Actualiza la tabla MySQL:**
```sql
ALTER TABLE productos ADD COLUMN marca VARCHAR(100);
```

2. **Actualiza la entidad JPA:**
```java
@Column(name = "marca", length = 100)
private String marca;
```

3. **Actualiza el DTO:**
```java
private String marca;
```

4. **Actualiza el frontend:**
```jsx
<input name="marca" ... />
```

## 📞 Soporte de Migración

Si encuentras problemas durante la migración:

1. Revisa los logs del backend
2. Verifica la estructura de la base de datos
3. Compara con el sistema Swing original
4. Consulta la documentación de Spring Boot

## ✅ Checklist de Migración

- [ ] Backup de base de datos
- [ ] Actualizar contraseñas a BCrypt
- [ ] Verificar integridad de datos
- [ ] Probar login con usuarios existentes
- [ ] Verificar productos y existencias
- [ ] Probar proceso de venta completo
- [ ] Verificar reportes y consultas
- [ ] Capacitar usuarios en nueva interfaz
- [ ] Establecer período de convivencia
- [ ] Migración completa

---

**¿Necesitas ayuda?** Revisa el archivo `README.md` para más detalles técnicos.
