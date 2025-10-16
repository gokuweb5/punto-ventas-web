# Sistema de Punto de Ventas Web

Sistema moderno de punto de ventas desarrollado con **Spring Boot** y **React**, migrado desde la aplicación Java Swing original.

## 🚀 Tecnologías Utilizadas

### Backend
- **Spring Boot 3.2.0** - Framework Java
- **Spring Data JPA** - Persistencia de datos
- **Spring Security** - Seguridad y autenticación
- **JWT** - JSON Web Tokens para autenticación stateless
- **MySQL** - Base de datos (usa la misma BD del proyecto original)
- **Lombok** - Reducción de código boilerplate
- **Maven** - Gestión de dependencias

### Frontend
- **React 18** - Librería UI
- **Vite** - Build tool moderno y rápido
- **TailwindCSS** - Framework CSS utility-first
- **React Router** - Navegación
- **Zustand** - State management
- **Axios** - Cliente HTTP
- **Lucide React** - Iconos

## 📋 Requisitos Previos

- **Java 17** o superior
- **Node.js 18** o superior
- **MySQL 8.0** o superior
- **Maven 3.8** o superior

## 🗄️ Configuración de Base de Datos

El sistema utiliza la **misma base de datos** del proyecto Swing original (`punto_de_ventas`).

### Configurar Conexión

Edita el archivo `src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/punto_de_ventas
    username: root
    password: tu_password_aqui
```

### Nota Importante sobre Contraseñas

El sistema original usa **MD5** para encriptar contraseñas. El nuevo sistema usa **BCrypt** (más seguro).

Para actualizar la contraseña del usuario admin:

```sql
-- Generar hash BCrypt de "admin" (ejemplo)
UPDATE usuarios 
SET Password = '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy'
WHERE Usuario = 'admin';
```

O puedes usar el endpoint de registro para crear nuevos usuarios con BCrypt.

## 🛠️ Instalación y Ejecución

### Backend (Spring Boot)

1. **Navega al directorio raíz del proyecto:**
   ```bash
   cd punto-ventas-web
   ```

2. **Compila el proyecto:**
   ```bash
   mvn clean install
   ```

3. **Ejecuta la aplicación:**
   ```bash
   mvn spring-boot:run
   ```

   El backend estará disponible en: `http://localhost:8080/api`

### Frontend (React)

1. **Navega al directorio frontend:**
   ```bash
   cd frontend
   ```

2. **Instala las dependencias:**
   ```bash
   npm install
   ```

3. **Ejecuta el servidor de desarrollo:**
   ```bash
   npm run dev
   ```

   El frontend estará disponible en: `http://localhost:3000`

## 📁 Estructura del Proyecto

```
punto-ventas-web/
├── src/main/java/com/sistema/puntoventas/
│   ├── entity/          # Entidades JPA (mapean tablas MySQL)
│   ├── repository/      # Repositorios Spring Data JPA
│   ├── service/         # Lógica de negocio
│   ├── controller/      # Controladores REST
│   ├── dto/             # Data Transfer Objects
│   ├── security/        # Configuración JWT y seguridad
│   └── config/          # Configuraciones Spring
├── src/main/resources/
│   └── application.yml  # Configuración de la aplicación
├── frontend/
│   ├── src/
│   │   ├── pages/       # Páginas principales
│   │   ├── components/  # Componentes reutilizables
│   │   ├── services/    # Servicios API
│   │   └── store/       # Estado global (Zustand)
│   ├── package.json
│   └── vite.config.js
└── pom.xml              # Dependencias Maven
```

## 🔐 Autenticación

### Login

**Endpoint:** `POST /api/auth/login`

**Request:**
```json
{
  "usuario": "admin",
  "password": "admin"
}
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "tipo": "Bearer",
  "idUsuario": 1,
  "usuario": "admin",
  "nombre": "Administrador",
  "apellido": "Sistema",
  "role": "Administrador"
}
```

### Uso del Token

Incluye el token en el header de todas las peticiones protegidas:

```
Authorization: Bearer <token>
```

## 📡 API Endpoints

### Productos

- `GET /api/productos` - Listar todos los productos
- `GET /api/productos/{id}` - Obtener producto por ID
- `GET /api/productos/codigo/{codigo}` - Buscar por código
- `POST /api/productos` - Crear producto
- `PUT /api/productos/{id}` - Actualizar producto
- `DELETE /api/productos/{id}` - Eliminar producto

### Ventas

- `GET /api/ventas` - Listar todas las ventas
- `GET /api/ventas/fecha?inicio={fecha}&fin={fecha}` - Ventas por rango de fechas
- `POST /api/ventas` - Procesar nueva venta

### Clientes

- `GET /api/clientes` - Listar todos los clientes
- `GET /api/clientes/{id}` - Obtener cliente por ID
- `POST /api/clientes` - Crear cliente
- `PUT /api/clientes/{id}` - Actualizar cliente
- `DELETE /api/clientes/{id}` - Eliminar cliente

## 🎨 Características del Frontend

- ✅ **Diseño Responsive** - Funciona en desktop, tablet y móvil
- ✅ **UI Moderna** - Interfaz limpia con TailwindCSS
- ✅ **Navegación SPA** - Sin recargas de página
- ✅ **Autenticación JWT** - Login seguro con tokens
- ✅ **Estado Persistente** - Los datos se mantienen al recargar
- ✅ **Búsqueda en Tiempo Real** - Filtros instantáneos

## 🔄 Migración desde Swing

### Diferencias Principales

| Aspecto | Swing (Original) | Web (Nuevo) |
|---------|------------------|-------------|
| **Interfaz** | Desktop (JFrame) | Web (React) |
| **Arquitectura** | Monolítica | Cliente-Servidor |
| **Autenticación** | Sesión local | JWT Tokens |
| **Base de Datos** | JDBC directo | Spring Data JPA |
| **Seguridad** | MD5 | BCrypt |
| **Acceso** | Local | Remoto (navegador) |

### Ventajas del Sistema Web

1. **Acceso Remoto** - Usar desde cualquier dispositivo con navegador
2. **Multi-usuario** - Varios usuarios simultáneos
3. **Escalable** - Fácil agregar más funcionalidades
4. **Mantenible** - Código organizado en capas
5. **Seguro** - Spring Security + JWT
6. **Moderno** - UI responsive y atractiva

## 🚧 Desarrollo

### Compilar para Producción

**Backend:**
```bash
mvn clean package
java -jar target/punto-ventas-web-1.0.0.jar
```

**Frontend:**
```bash
cd frontend
npm run build
```

Los archivos estáticos se generarán en `frontend/dist/`

## 📝 Notas Importantes

1. **Compatibilidad BD**: El sistema usa exactamente la misma estructura de base de datos
2. **Contraseñas**: Actualiza las contraseñas a BCrypt para mayor seguridad
3. **CORS**: Configurado para desarrollo (localhost:3000). Ajusta para producción
4. **JWT Secret**: Cambia la clave secreta en producción (`application.yml`)

## 🐛 Solución de Problemas

### Error de Conexión a MySQL

```
Caused by: java.sql.SQLException: Access denied for user 'root'@'localhost'
```

**Solución**: Verifica usuario y contraseña en `application.yml`

### Error CORS en Frontend

**Solución**: Asegúrate que el backend esté corriendo en puerto 8080

### Token Expirado

**Solución**: Vuelve a iniciar sesión. Los tokens duran 24 horas por defecto

## 📞 Soporte

Para dudas o problemas, revisa:
- Logs del backend: Consola donde ejecutaste `mvn spring-boot:run`
- Logs del frontend: Consola del navegador (F12)

## 📄 Licencia

Este proyecto es una migración del sistema original de punto de ventas.
