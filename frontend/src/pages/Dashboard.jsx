import { Package, ShoppingCart, Users, DollarSign } from 'lucide-react'

export default function Dashboard() {
  return (
    <div className="p-8">
      <h1 className="text-3xl font-bold text-gray-800 mb-8">Dashboard</h1>
      
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-8">
        <div className="bg-white rounded-lg shadow p-6">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-gray-600 text-sm">Productos</p>
              <p className="text-3xl font-bold text-gray-800">0</p>
            </div>
            <div className="bg-blue-100 p-3 rounded-full">
              <Package className="w-8 h-8 text-blue-600" />
            </div>
          </div>
        </div>

        <div className="bg-white rounded-lg shadow p-6">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-gray-600 text-sm">Ventas Hoy</p>
              <p className="text-3xl font-bold text-gray-800">0</p>
            </div>
            <div className="bg-green-100 p-3 rounded-full">
              <ShoppingCart className="w-8 h-8 text-green-600" />
            </div>
          </div>
        </div>

        <div className="bg-white rounded-lg shadow p-6">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-gray-600 text-sm">Clientes</p>
              <p className="text-3xl font-bold text-gray-800">0</p>
            </div>
            <div className="bg-purple-100 p-3 rounded-full">
              <Users className="w-8 h-8 text-purple-600" />
            </div>
          </div>
        </div>

        <div className="bg-white rounded-lg shadow p-6">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-gray-600 text-sm">Ingresos Hoy</p>
              <p className="text-3xl font-bold text-gray-800">$0.00</p>
            </div>
            <div className="bg-yellow-100 p-3 rounded-full">
              <DollarSign className="w-8 h-8 text-yellow-600" />
            </div>
          </div>
        </div>
      </div>

      <div className="bg-white rounded-lg shadow p-6">
        <h2 className="text-xl font-bold text-gray-800 mb-4">Bienvenido al Sistema de Punto de Ventas</h2>
        <p className="text-gray-600">
          Utiliza el menú lateral para navegar entre las diferentes secciones del sistema.
        </p>
      </div>
    </div>
  )
}
