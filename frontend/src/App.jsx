import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom'
import { useAuthStore } from './store/authStore'
import Login from './pages/Login'
import Dashboard from './pages/Dashboard'
import Productos from './pages/Productos'
import Ventas from './pages/Ventas'
import Clientes from './pages/Clientes'
import Categorias from './pages/Categorias'
import ConfiguracionFacturas from './pages/ConfiguracionFacturas'
import Layout from './components/Layout'

function PrivateRoute({ children }) {
  const { token } = useAuthStore()
  return token ? children : <Navigate to="/login" />
}

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/login" element={<Login />} />
        <Route
          path="/"
          element={
            <PrivateRoute>
              <Layout />
            </PrivateRoute>
          }
        >
          <Route index element={<Dashboard />} />
          <Route path="productos" element={<Productos />} />
          <Route path="categorias" element={<Categorias />} />
          <Route path="ventas" element={<Ventas />} />
          <Route path="clientes" element={<Clientes />} />
          <Route path="configuracion-facturas" element={<ConfiguracionFacturas />} />
        </Route>
      </Routes>
    </Router>
  )
}

export default App
