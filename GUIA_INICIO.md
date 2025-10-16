# 🚀 Guía de Inicio Rápido

Esta guía te ayudará a poner en marcha el sistema en **5 minutos**.

## ✅ Paso 1: Verificar Requisitos

Asegúrate de tener instalado:

- [ ] **Java 17+** - Ejecuta: `java -version`
- [ ] **Node.js 18+** - Ejecuta: `node -v`
- [ ] **MySQL 8+** - Ejecuta: `mysql --version`
- [ ] **Maven 3.8+** - Ejecuta: `mvn -v`

## 📦 Paso 2: Configurar Base de Datos

### Opción A: Usar la BD existente

Si ya tienes la base de datos `punto_de_ventas` del proyecto Swing:

1. Abre `src/main/resources/application.yml`
2. Actualiza las credenciales:
   ```yaml
   spring:
     datasource:
       username: root
       password: TU_PASSWORD_AQUI
   ```

### Opción B: Crear nueva BD

Si no tienes la base de datos, ejecuta el script:

```bash
mysql -u root -p < ../Sistema\ punto\ de\ ventas/database_setup.sql
```

## 🔐 Paso 3: Actualizar Contraseña Admin

El sistema original usa MD5, el nuevo usa BCrypt. Actualiza la contraseña:

```sql
USE punto_de_ventas;

-- Contraseña: admin (en BCrypt)
UPDATE usuarios 
SET Password = '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy'
WHERE Usuario = 'admin';
```

## 🖥️ Paso 4: Iniciar Backend

Abre una terminal en la carpeta `punto-ventas-web`:

```bash
# Compilar e iniciar
mvn spring-boot:run
```

Espera a ver: `Started PuntoVentasApplication in X seconds`

El backend estará en: **http://localhost:8080/api**

## 🎨 Paso 5: Iniciar Frontend

Abre **OTRA terminal** en la carpeta `punto-ventas-web/frontend`:

```bash
# Instalar dependencias (solo la primera vez)
npm install

# Iniciar servidor de desarrollo
npm run dev
```

El frontend estará en: **http://localhost:3000**

## 🎉 Paso 6: Iniciar Sesión

1. Abre tu navegador en: **http://localhost:3000**
2. Ingresa las credenciales:
   - **Usuario:** `admin`
   - **Contraseña:** `admin`
3. ¡Listo! Ya puedes usar el sistema

## 🐛 Problemas Comunes

### "Access denied for user 'root'"

**Solución:** Verifica usuario/contraseña en `application.yml`

### "Port 8080 is already in use"

**Solución:** Cambia el puerto en `application.yml`:
```yaml
server:
  port: 8081
```

### "Cannot connect to backend"

**Solución:** 
1. Verifica que el backend esté corriendo (puerto 8080)
2. Revisa la consola del backend por errores

### "Bad credentials" al hacer login

**Solución:** Ejecuta el UPDATE de contraseña BCrypt (Paso 3)

## 📱 Acceso desde Otros Dispositivos

Para acceder desde otro dispositivo en la misma red:

1. Encuentra tu IP local: `ipconfig` (Windows) o `ifconfig` (Mac/Linux)
2. En `frontend/vite.config.js`, agrega:
   ```js
   server: {
     host: '0.0.0.0',
     port: 3000
   }
   ```
3. Accede desde otro dispositivo: `http://TU_IP:3000`

## 🎯 Próximos Pasos

- Explora el **Dashboard** para ver estadísticas
- Agrega **Productos** desde el menú lateral
- Registra **Clientes** para ventas a crédito
- Procesa tu primera **Venta**

## 📚 Documentación Completa

Para más detalles, consulta el archivo `README.md`

---

**¿Todo funcionando?** ¡Perfecto! Ahora puedes empezar a usar el sistema 🎊
