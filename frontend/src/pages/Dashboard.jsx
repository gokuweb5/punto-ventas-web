import { useState, useEffect } from 'react'
import { useNavigate } from 'react-router-dom'
import { Package, ShoppingCart, Users, DollarSign, TrendingUp, TrendingDown, ArrowRight, Clock, AlertCircle } from 'lucide-react'
import { productosAPI, ventasAPI, clientesAPI } from '../services/api'

export default function Dashboard() {
  const navigate = useNavigate()
  const [stats, setStats] = useState({
    productos: 0,
    ventasHoy: 0,
    clientes: 0,
    ingresosHoy: 0,
    loading: true
  })
  const [recentSales, setRecentSales] = useState([])
  const [lowStock, setLowStock] = useState([])

  useEffect(() => {
    loadDashboardData()
  }, [])

  const loadDashboardData = async () => {
    try {
      const [productosRes, clientesRes, ventasRes] = await Promise.all([
        productosAPI.getAll(),
        clientesAPI.getAll(),
        ventasAPI.getAll()
      ])

      const productos = productosRes.data
      const clientes = clientesRes.data
      const ventas = ventasRes.data

      console.log('Ventas recibidas:', ventas)

      // Agrupar ventas por ticket
      const ventasAgrupadas = ventas.reduce((acc, venta) => {
        const ticket = venta.ticket
        if (!acc[ticket]) {
          acc[ticket] = {
            ticket: ticket,
            fecha: venta.fecha,
            cliente: venta.cliente,
            items: [],
            total: 0
          }
        }
        acc[ticket].items.push(venta)
        acc[ticket].total += parseFloat(venta.importe || 0)
        return acc
      }, {})

      const ventasArray = Object.values(ventasAgrupadas)
      console.log('Ventas agrupadas:', ventasArray)

      // Filtrar ventas de hoy
      const hoy = new Date()
      const hoyStr = hoy.toISOString().split('T')[0]
      
      const ventasHoy = ventasArray.filter(v => {
        if (!v.fecha) return false
        
        // Convertir la fecha de la venta a formato comparable
        const fechaVenta = new Date(v.fecha)
        const fechaVentaStr = fechaVenta.toISOString().split('T')[0]
        
        return fechaVentaStr === hoyStr
      })

      console.log('Ventas de hoy:', ventasHoy)
      console.log('Fecha actual:', hoyStr)
      
      const ingresosHoy = ventasHoy.reduce((sum, v) => {
        console.log('Venta total:', v.total)
        return sum + (v.total || 0)
      }, 0)
      
      console.log('Ingresos calculados:', ingresosHoy)

      // Productos con bajo stock (menos de 10 unidades)
      const productosLowStock = productos
        .filter(p => p.stock < 10)
        .sort((a, b) => a.stock - b.stock)
        .slice(0, 5)

      // Últimas 5 ventas agrupadas
      const ultimasVentas = ventasArray
        .sort((a, b) => new Date(b.fecha) - new Date(a.fecha))
        .slice(0, 5)

      setStats({
        productos: productos.length,
        ventasHoy: ventasHoy.length,
        clientes: clientes.length,
        ingresosHoy: ingresosHoy,
        loading: false
      })

      setRecentSales(ultimasVentas)
      setLowStock(productosLowStock)
    } catch (error) {
      console.error('Error cargando datos del dashboard:', error)
      setStats(prev => ({ ...prev, loading: false }))
    }
  }

  const StatCard = ({ title, value, icon: Icon, color, trend }) => (
    <div className="bg-white rounded-xl shadow-sm hover:shadow-md transition-all duration-300 p-6 border border-gray-100">
      <div className="flex items-center justify-between">
        <div className="flex-1">
          <p className="text-gray-600 text-sm font-medium mb-1">{title}</p>
          <p className="text-3xl font-bold text-gray-800 mb-2">
            {stats.loading ? (
              <span className="inline-block w-20 h-8 bg-gray-200 rounded animate-pulse"></span>
            ) : (
              value
            )}
          </p>
          {trend && (
            <div className={`flex items-center text-sm ${
              trend > 0 ? 'text-green-600' : trend < 0 ? 'text-red-600' : 'text-gray-600'
            }`}>
              {trend > 0 ? (
                <TrendingUp className="w-4 h-4 mr-1" />
              ) : trend < 0 ? (
                <TrendingDown className="w-4 h-4 mr-1" />
              ) : null}
              <span className="font-medium">{Math.abs(trend)}%</span>
              <span className="ml-1 text-gray-500">vs ayer</span>
            </div>
          )}
        </div>
        <div className={`${color} p-4 rounded-xl`}>
          <Icon className="w-8 h-8" />
        </div>
      </div>
    </div>
  )

  return (
    <div className="p-8 bg-gray-50 min-h-screen">
      <div className="mb-8">
        <h1 className="text-3xl font-bold text-gray-800 mb-2">Dashboard</h1>
        <p className="text-gray-600">Resumen general de tu negocio</p>
      </div>
      
      {/* Stats Cards */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-8">
        <StatCard
          title="Total Productos"
          value={stats.productos}
          icon={Package}
          color="bg-blue-100 text-blue-600"
        />
        <StatCard
          title="Ventas Hoy"
          value={stats.ventasHoy}
          icon={ShoppingCart}
          color="bg-green-100 text-green-600"
          trend={12}
        />
        <StatCard
          title="Clientes"
          value={stats.clientes}
          icon={Users}
          color="bg-purple-100 text-purple-600"
        />
        <StatCard
          title="Ingresos Hoy"
          value={`$${stats.ingresosHoy.toFixed(2)}`}
          icon={DollarSign}
          color="bg-yellow-100 text-yellow-600"
          trend={8}
        />
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6 mb-8">
        {/* Recent Sales */}
        <div className="bg-white rounded-xl shadow-sm p-6 border border-gray-100">
          <div className="flex items-center justify-between mb-6">
            <h2 className="text-xl font-bold text-gray-800">Ventas Recientes</h2>
            <Clock className="w-5 h-5 text-gray-400" />
          </div>
          {stats.loading ? (
            <div className="space-y-4">
              {[1, 2, 3].map(i => (
                <div key={i} className="h-16 bg-gray-100 rounded animate-pulse"></div>
              ))}
            </div>
          ) : recentSales.length > 0 ? (
            <div className="space-y-3">
              {recentSales.map((venta, index) => (
                <div key={index} className="flex items-center justify-between p-3 bg-gray-50 rounded-lg hover:bg-gray-100 transition-colors">
                  <div className="flex-1">
                    <p className="font-medium text-gray-800">
                      {venta.cliente?.nombre || 'Cliente General'}
                    </p>
                    <p className="text-sm text-gray-500">
                      {new Date(venta.fecha).toLocaleString('es-ES', {
                        day: '2-digit',
                        month: '2-digit',
                        hour: '2-digit',
                        minute: '2-digit'
                      })}
                    </p>
                  </div>
                  <div className="text-right">
                    <p className="font-bold text-green-600">${venta.total?.toFixed(2)}</p>
                    <p className="text-xs text-gray-500">{venta.items?.length || 0} items</p>
                  </div>
                </div>
              ))}
            </div>
          ) : (
            <div className="text-center py-8 text-gray-500">
              <ShoppingCart className="w-12 h-12 mx-auto mb-2 opacity-50" />
              <p>No hay ventas registradas</p>
            </div>
          )}
        </div>

        {/* Low Stock Alert */}
        <div className="bg-white rounded-xl shadow-sm p-6 border border-gray-100">
          <div className="flex items-center justify-between mb-6">
            <h2 className="text-xl font-bold text-gray-800">Alertas de Stock</h2>
            <AlertCircle className="w-5 h-5 text-orange-500" />
          </div>
          {stats.loading ? (
            <div className="space-y-4">
              {[1, 2, 3].map(i => (
                <div key={i} className="h-16 bg-gray-100 rounded animate-pulse"></div>
              ))}
            </div>
          ) : lowStock.length > 0 ? (
            <div className="space-y-3">
              {lowStock.map((producto, index) => (
                <div key={index} className="flex items-center justify-between p-3 bg-orange-50 rounded-lg border border-orange-200">
                  <div className="flex-1">
                    <p className="font-medium text-gray-800">{producto.nombre}</p>
                    <p className="text-sm text-gray-500">Código: {producto.codigo}</p>
                  </div>
                  <div className="text-right">
                    <p className={`font-bold ${
                      producto.stock === 0 ? 'text-red-600' : 
                      producto.stock < 5 ? 'text-orange-600' : 'text-yellow-600'
                    }`}>
                      {producto.stock} unidades
                    </p>
                    <p className="text-xs text-gray-500">
                      {producto.stock === 0 ? 'Sin stock' : 'Stock bajo'}
                    </p>
                  </div>
                </div>
              ))}
            </div>
          ) : (
            <div className="text-center py-8 text-gray-500">
              <Package className="w-12 h-12 mx-auto mb-2 opacity-50" />
              <p>Todos los productos tienen stock suficiente</p>
            </div>
          )}
        </div>
      </div>

      {/* Quick Actions */}
      <div className="bg-gradient-to-r from-blue-600 to-blue-700 rounded-xl shadow-lg p-8 text-white">
        <h2 className="text-2xl font-bold mb-6">Acciones Rápidas</h2>
        <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
          <button 
            onClick={() => navigate('/ventas')}
            className="bg-white/10 hover:bg-white/20 backdrop-blur-sm rounded-lg p-4 text-left transition-all duration-300 hover:scale-105 border border-white/20"
          >
            <ShoppingCart className="w-8 h-8 mb-2" />
            <p className="font-semibold mb-1">Nueva Venta</p>
            <p className="text-sm text-blue-100">Registrar una venta rápida</p>
            <ArrowRight className="w-5 h-5 mt-2 ml-auto" />
          </button>
          <button 
            onClick={() => navigate('/productos')}
            className="bg-white/10 hover:bg-white/20 backdrop-blur-sm rounded-lg p-4 text-left transition-all duration-300 hover:scale-105 border border-white/20"
          >
            <Package className="w-8 h-8 mb-2" />
            <p className="font-semibold mb-1">Agregar Producto</p>
            <p className="text-sm text-blue-100">Añadir nuevo producto al inventario</p>
            <ArrowRight className="w-5 h-5 mt-2 ml-auto" />
          </button>
          <button 
            onClick={() => navigate('/clientes')}
            className="bg-white/10 hover:bg-white/20 backdrop-blur-sm rounded-lg p-4 text-left transition-all duration-300 hover:scale-105 border border-white/20"
          >
            <Users className="w-8 h-8 mb-2" />
            <p className="font-semibold mb-1">Nuevo Cliente</p>
            <p className="text-sm text-blue-100">Registrar un nuevo cliente</p>
            <ArrowRight className="w-5 h-5 mt-2 ml-auto" />
          </button>
        </div>
      </div>
    </div>
  )
}
