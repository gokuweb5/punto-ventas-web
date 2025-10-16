# 📂 Estructura del Proyecto

## 🏗️ Arquitectura General

```
punto-ventas-web/
│
├── 🔧 Backend (Spring Boot)
│   └── src/main/java/com/sistema/puntoventas/
│
├── 🎨 Frontend (React + Vite)
│   └── frontend/
│
└── 📚 Documentación
    ├── README.md
    ├── GUIA_INICIO.md
    └── MIGRACION.md
```

## 🔧 Backend - Spring Boot

### Estructura de Paquetes

```
src/main/java/com/sistema/puntoventas/
│
├── 📦 entity/                    # Entidades JPA (Modelos de BD)
│   ├── Usuario.java              # Tabla: usuarios
│   ├── Producto.java             # Tabla: productos
│   ├── Bodega.java               # Tabla: bodega
│   ├── Cliente.java              # Tabla: clientes
│   ├── Proveedor.java            # Tabla: proveedores
│   ├── Venta.java                # Tabla: ventas
│   ├── Compra.java               # Tabla: compras
│   └── Caja.java                 # Tabla: cajas
│
├── 🗄️ repository/                # Acceso a Datos (Spring Data JPA)
│   ├── UsuarioRepository.java
│   ├── ProductoRepository.java
│   ├── ClienteRepository.java
│   ├── VentaRepository.java
│   ├── ProveedorRepository.java
│   ├── CompraRepository.java
│   └── CajaRepository.java
│
├── 💼 service/                   # Lógica de Negocio
│   ├── AuthService.java          # Autenticación y registro
│   ├── ProductoService.java      # Gestión de productos
│   ├── VentaService.java         # Procesamiento de ventas
│   └── ClienteService.java       # Gestión de clientes
│
├── 🌐 controller/                # Endpoints REST API
│   ├── AuthController.java       # /api/auth/*
│   ├── ProductoController.java   # /api/productos/*
│   ├── VentaController.java      # /api/ventas/*
│   └── ClienteController.java    # /api/clientes/*
│
├── 📋 dto/                       # Data Transfer Objects
│   ├── LoginRequest.java
│   ├── LoginResponse.java
│   ├── ProductoDTO.java
│   └── VentaRequest.java
│
├── 🔐 security/                  # Seguridad y JWT
│   ├── JwtUtil.java              # Generación/validación tokens
│   ├── JwtAuthenticationFilter.java
│   └── CustomUserDetailsService.java
│
├── ⚙️ config/                    # Configuraciones
│   └── SecurityConfig.java       # Spring Security + CORS
│
└── 🚀 PuntoVentasApplication.java # Clase principal
```

### Recursos

```
src/main/resources/
├── application.yml               # Configuración principal
└── application-prod.yml          # Configuración producción (opcional)
```

## 🎨 Frontend - React

### Estructura de Carpetas

```
frontend/
│
├── 📄 public/                    # Archivos estáticos
│
├── 🎯 src/
│   │
│   ├── 📱 pages/                 # Páginas principales
│   │   ├── Login.jsx             # Página de login
│   │   ├── Dashboard.jsx         # Panel principal
│   │   ├── Productos.jsx         # Gestión de productos
│   │   ├── Ventas.jsx            # Punto de venta
│   │   └── Clientes.jsx          # Gestión de clientes
│   │
│   ├── 🧩 components/            # Componentes reutilizables
│   │   └── Layout.jsx            # Layout con sidebar
│   │
│   ├── 🔌 services/              # Servicios API
│   │   └── api.js                # Axios + endpoints
│   │
│   ├── 📦 store/                 # Estado global (Zustand)
│   │   └── authStore.js          # Estado de autenticación
│   │
│   ├── 🎨 index.css              # Estilos globales + Tailwind
│   ├── 📱 App.jsx                # Componente raíz + rutas
│   └── 🚀 main.jsx               # Punto de entrada
│
├── 📦 package.json               # Dependencias npm
├── ⚡ vite.config.js             # Configuración Vite
├── 🎨 tailwind.config.js         # Configuración Tailwind
├── 📝 postcss.config.js          # PostCSS
└── 🌐 index.html                 # HTML base
```

## 🔄 Flujo de Datos

### 1. Autenticación

```
Usuario → Login.jsx → authAPI.login() → POST /api/auth/login
                                              ↓
                                    AuthController.login()
                                              ↓
                                    AuthService.login()
                                              ↓
                                    JWT Token generado
                                              ↓
                                    LoginResponse ← Usuario
                                              ↓
                                    Token guardado en Zustand
```

### 2. Consulta de Productos

```
Productos.jsx → productosAPI.getAll() → GET /api/productos
                                              ↓
                                    ProductoController.obtenerTodos()
                                              ↓
                                    ProductoService.obtenerTodosLosProductos()
                                              ↓
                                    ProductoRepository.findAllWithBodega()
                                              ↓
                                    MySQL Query
                                              ↓
                                    List<ProductoDTO> ← Productos.jsx
```

### 3. Procesamiento de Venta

```
Ventas.jsx → ventasAPI.create() → POST /api/ventas
                                        ↓
                                VentaController.procesarVenta()
                                        ↓
                                VentaService.procesarVenta()
                                        ↓
                        ┌───────────────┴───────────────┐
                        ↓                               ↓
            Validar existencias              Calcular importes
                        ↓                               ↓
            Crear registros de venta        Actualizar inventario
                        ↓                               ↓
                        └───────────────┬───────────────┘
                                        ↓
                            Transacción MySQL
                                        ↓
                            List<Venta> ← Ventas.jsx
```

## 🗂️ Mapeo de Archivos (Swing → Web)

| Funcionalidad | Swing Original | Spring Boot Web |
|---------------|----------------|-----------------|
| **Login** | `ViewModels/LoginVM.java` | `controller/AuthController.java` |
| **Productos** | `ViewModels/ProductosVM.java` | `service/ProductoService.java` |
| **Ventas** | `Views/Sistema.java` | `controller/VentaController.java` |
| **Clientes** | `ViewModels/ClientesVM.java` | `service/ClienteService.java` |
| **Conexión BD** | `Conexion/Conexion.java` | `application.yml` + JPA |
| **Modelos** | `Models/Producto/TProductos.java` | `entity/Producto.java` |
| **UI** | `Views/*.java` (JFrame) | `frontend/src/pages/*.jsx` |

## 📊 Capas de la Aplicación

### Backend (Spring Boot)

```
┌─────────────────────────────────────┐
│         REST Controllers            │  ← Endpoints HTTP
│     (AuthController, etc.)          │
└─────────────────┬───────────────────┘
                  │
┌─────────────────▼───────────────────┐
│           Services                  │  ← Lógica de negocio
│     (AuthService, etc.)             │
└─────────────────┬───────────────────┘
                  │
┌─────────────────▼───────────────────┐
│         Repositories                │  ← Acceso a datos
│   (UsuarioRepository, etc.)         │
└─────────────────┬───────────────────┘
                  │
┌─────────────────▼───────────────────┐
│          Entities (JPA)             │  ← Mapeo ORM
│     (Usuario, Producto, etc.)       │
└─────────────────┬───────────────────┘
                  │
┌─────────────────▼───────────────────┐
│         MySQL Database              │  ← Base de datos
│      (punto_de_ventas)              │
└─────────────────────────────────────┘
```

### Frontend (React)

```
┌─────────────────────────────────────┐
│            Pages                    │  ← Vistas principales
│   (Login, Dashboard, etc.)          │
└─────────────────┬───────────────────┘
                  │
┌─────────────────▼───────────────────┐
│          Components                 │  ← Componentes UI
│         (Layout, etc.)              │
└─────────────────┬───────────────────┘
                  │
┌─────────────────▼───────────────────┐
│        Services (API)               │  ← Llamadas HTTP
│      (authAPI, productosAPI)        │
└─────────────────┬───────────────────┘
                  │
┌─────────────────▼───────────────────┐
│         Zustand Store               │  ← Estado global
│        (authStore)                  │
└─────────────────────────────────────┘
```

## 🔐 Seguridad

### Flujo de Autenticación JWT

```
1. Login
   Usuario → POST /api/auth/login
          → AuthService valida credenciales
          → JwtUtil genera token
          → Token enviado al cliente

2. Requests Protegidos
   Cliente → GET /api/productos (Header: Authorization: Bearer <token>)
          → JwtAuthenticationFilter intercepta
          → JwtUtil valida token
          → Si válido: permite acceso
          → Si inválido: 401 Unauthorized

3. Logout
   Cliente → Elimina token del localStorage
          → Redirige a /login
```

## 📝 Convenciones de Código

### Backend (Java)

- **Entidades**: PascalCase (`Usuario`, `Producto`)
- **Servicios**: PascalCase + Service (`ProductoService`)
- **Repositorios**: PascalCase + Repository (`ProductoRepository`)
- **DTOs**: PascalCase + DTO (`ProductoDTO`)
- **Métodos**: camelCase (`obtenerTodosLosProductos()`)

### Frontend (JavaScript)

- **Componentes**: PascalCase (`Login.jsx`, `Dashboard.jsx`)
- **Funciones**: camelCase (`handleSubmit`, `cargarProductos`)
- **Variables**: camelCase (`productos`, `searchTerm`)
- **Constantes**: UPPER_SNAKE_CASE (`API_URL`)

## 🚀 Puntos de Entrada

### Backend
```bash
# Archivo principal
src/main/java/com/sistema/puntoventas/PuntoVentasApplication.java

# Comando de ejecución
mvn spring-boot:run
```

### Frontend
```bash
# Archivo principal
frontend/src/main.jsx

# Comando de ejecución
npm run dev
```

## 📦 Dependencias Clave

### Backend (pom.xml)
- `spring-boot-starter-web` - REST API
- `spring-boot-starter-data-jpa` - ORM
- `spring-boot-starter-security` - Seguridad
- `mysql-connector-j` - Driver MySQL
- `jjwt` - JWT tokens
- `lombok` - Reducir boilerplate

### Frontend (package.json)
- `react` - Librería UI
- `react-router-dom` - Navegación
- `axios` - HTTP client
- `zustand` - State management
- `tailwindcss` - CSS framework
- `lucide-react` - Iconos

---

**Tip**: Usa esta estructura como referencia al agregar nuevas funcionalidades.
