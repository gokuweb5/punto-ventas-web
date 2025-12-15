import { Outlet, Link, useNavigate } from 'react-router-dom'
import { useAuthStore } from '../store/authStore'
import { 
  LayoutDashboard, 
  Package, 
  FolderOpen,
  ShoppingCart, 
  Users,
  FileText,
  LogOut 
} from 'lucide-react'

export default function Layout() {
  const { user, logout } = useAuthStore()
  const navigate = useNavigate()

  const handleLogout = () => {
    logout()
    navigate('/login')
  }

  return (
    <div className="flex h-screen bg-gray-100">
      {/* Sidebar */}
      <aside className="w-64 bg-gray-900 text-white">
        <div className="p-4">
          <h1 className="text-2xl font-bold">Punto de Ventas</h1>
          <p className="text-sm text-gray-400 mt-1">{user?.nombre} {user?.apellido}</p>
        </div>
        
        <nav className="mt-8">
          <Link
            to="/"
            className="flex items-center px-6 py-3 text-gray-300 hover:bg-gray-800 hover:text-white transition-colors"
          >
            <LayoutDashboard className="w-5 h-5 mr-3" />
            Dashboard
          </Link>
          
          <Link
            to="/productos"
            className="flex items-center px-6 py-3 text-gray-300 hover:bg-gray-800 hover:text-white transition-colors"
          >
            <Package className="w-5 h-5 mr-3" />
            Productos
          </Link>
          
          <Link
            to="/categorias"
            className="flex items-center px-6 py-3 text-gray-300 hover:bg-gray-800 hover:text-white transition-colors"
          >
            <FolderOpen className="w-5 h-5 mr-3" />
            Categorías
          </Link>
          
          <Link
            to="/ventas"
            className="flex items-center px-6 py-3 text-gray-300 hover:bg-gray-800 hover:text-white transition-colors"
          >
            <ShoppingCart className="w-5 h-5 mr-3" />
            Ventas
          </Link>
          
          <Link
            to="/clientes"
            className="flex items-center px-6 py-3 text-gray-300 hover:bg-gray-800 hover:text-white transition-colors"
          >
            <Users className="w-5 h-5 mr-3" />
            Clientes
          </Link>
          
          <Link
            to="/facturas"
            className="flex items-center px-6 py-3 text-gray-300 hover:bg-gray-800 hover:text-white transition-colors"
          >
            <FileText className="w-5 h-5 mr-3" />
            Facturas
          </Link>
          
          <Link
            to="/configuracion-facturas"
            className="flex items-center px-6 py-3 text-gray-300 hover:bg-gray-800 hover:text-white transition-colors"
          >
            <FileText className="w-5 h-5 mr-3" />
            Config. Facturas
          </Link>
        </nav>

        <button
          onClick={handleLogout}
          className="absolute bottom-4 left-4 right-4 flex items-center justify-center px-6 py-3 bg-red-600 hover:bg-red-700 rounded-lg transition-colors"
        >
          <LogOut className="w-5 h-5 mr-2" />
          Cerrar Sesión
        </button>
      </aside>

      {/* Main Content */}
      <main className="flex-1 overflow-y-auto">
        <Outlet />
      </main>
    </div>
  )
}
