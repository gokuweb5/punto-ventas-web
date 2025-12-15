import { useState, useEffect } from 'react'
import { FileText, Download, Search, Eye, X, Calendar, User, CreditCard } from 'lucide-react'
import { facturasAPI } from '../services/api'

export default function Facturas() {
  const [facturas, setFacturas] = useState([])
  const [loading, setLoading] = useState(true)
  const [busqueda, setBusqueda] = useState('')
  const [facturaSeleccionada, setFacturaSeleccionada] = useState(null)

  useEffect(() => {
    cargarFacturas()
  }, [])

  const cargarFacturas = async () => {
    try {
      const response = await facturasAPI.getAll()
      setFacturas(response.data)
    } catch (error) {
      console.error('Error al cargar facturas:', error)
    } finally {
      setLoading(false)
    }
  }

  const descargarFactura = (ticket) => {
    window.open(facturasAPI.descargar(ticket), '_blank')
  }

  const formatearFecha = (fecha) => {
    if (!fecha) return 'N/A'
    return new Date(fecha).toLocaleDateString('es-ES', {
      year: 'numeric',
      month: 'short',
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    })
  }

  const facturasFiltradas = facturas.filter(factura => 
    factura.ticket.toLowerCase().includes(busqueda.toLowerCase()) ||
    factura.cliente.toLowerCase().includes(busqueda.toLowerCase())
  )

  if (loading) {
    return (
      <div className="p-8">
        <div className="text-center">Cargando facturas...</div>
      </div>
    )
  }

  return (
    <div className="p-8">
      <div className="flex justify-between items-center mb-8">
        <h1 className="text-3xl font-bold text-gray-800">Facturas</h1>
        <div className="text-sm text-gray-500">
          Total: {facturas.length} facturas
        </div>
      </div>

      {/* Barra de búsqueda */}
      <div className="bg-white rounded-lg shadow p-4 mb-6">
        <div className="relative">
          <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400 w-5 h-5" />
          <input
            type="text"
            value={busqueda}
            onChange={(e) => setBusqueda(e.target.value)}
            placeholder="Buscar por ticket o cliente..."
            className="w-full pl-10 pr-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
          />
        </div>
      </div>

      {/* Lista de facturas */}
      {facturasFiltradas.length === 0 ? (
        <div className="bg-white rounded-lg shadow p-12 text-center">
          <FileText className="w-16 h-16 mx-auto mb-4 text-gray-300" />
          <p className="text-gray-500">No se encontraron facturas</p>
        </div>
      ) : (
        <div className="bg-white rounded-lg shadow overflow-hidden">
          <table className="min-w-full divide-y divide-gray-200">
            <thead className="bg-gray-50">
              <tr>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Ticket
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Fecha
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Cliente
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Items
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Total
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Tipo
                </th>
                <th className="px-6 py-3 text-right text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Acciones
                </th>
              </tr>
            </thead>
            <tbody className="bg-white divide-y divide-gray-200">
              {facturasFiltradas.map((factura) => (
                <tr key={factura.ticket} className="hover:bg-gray-50">
                  <td className="px-6 py-4 whitespace-nowrap">
                    <div className="flex items-center">
                      <FileText className="w-5 h-5 text-blue-500 mr-2" />
                      <span className="font-mono text-sm">{factura.ticket}</span>
                    </div>
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-600">
                    {formatearFecha(factura.fecha)}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap">
                    <div className="flex items-center">
                      <User className="w-4 h-4 text-gray-400 mr-2" />
                      <span className="text-sm text-gray-900">{factura.cliente}</span>
                    </div>
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-600">
                    {factura.cantidadItems} producto(s)
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap">
                    <span className="text-sm font-semibold text-gray-900">
                      ${factura.total?.toFixed(2)}
                    </span>
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap">
                    {factura.credito ? (
                      <span className="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-yellow-100 text-yellow-800">
                        <CreditCard className="w-3 h-3 mr-1" />
                        Crédito
                      </span>
                    ) : (
                      <span className="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-green-100 text-green-800">
                        Contado
                      </span>
                    )}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-right text-sm font-medium">
                    <button
                      onClick={() => setFacturaSeleccionada(factura)}
                      className="text-blue-600 hover:text-blue-900 mr-3"
                      title="Ver detalle"
                    >
                      <Eye className="w-5 h-5" />
                    </button>
                    <button
                      onClick={() => descargarFactura(factura.ticket)}
                      className="text-green-600 hover:text-green-900"
                      title="Descargar PDF"
                    >
                      <Download className="w-5 h-5" />
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}

      {/* Modal de detalle */}
      {facturaSeleccionada && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
          <div className="bg-white rounded-lg p-6 max-w-2xl w-full mx-4 max-h-[90vh] overflow-y-auto">
            <div className="flex justify-between items-center mb-6">
              <h2 className="text-xl font-bold text-gray-800">Detalle de Factura</h2>
              <button
                onClick={() => setFacturaSeleccionada(null)}
                className="text-gray-500 hover:text-gray-700"
              >
                <X className="w-6 h-6" />
              </button>
            </div>

            <div className="grid grid-cols-2 gap-4 mb-6">
              <div className="bg-gray-50 p-4 rounded-lg">
                <p className="text-sm text-gray-600">Ticket</p>
                <p className="font-mono font-semibold">{facturaSeleccionada.ticket}</p>
              </div>
              <div className="bg-gray-50 p-4 rounded-lg">
                <p className="text-sm text-gray-600">Fecha</p>
                <p className="font-semibold">{formatearFecha(facturaSeleccionada.fecha)}</p>
              </div>
              <div className="bg-gray-50 p-4 rounded-lg">
                <p className="text-sm text-gray-600">Cliente</p>
                <p className="font-semibold">{facturaSeleccionada.cliente}</p>
              </div>
              <div className="bg-gray-50 p-4 rounded-lg">
                <p className="text-sm text-gray-600">Tipo de Venta</p>
                <p className="font-semibold">
                  {facturaSeleccionada.credito ? 'Crédito' : 'Contado'}
                </p>
              </div>
            </div>

            <h3 className="font-semibold text-gray-800 mb-3">Productos</h3>
            <table className="min-w-full divide-y divide-gray-200 mb-6">
              <thead className="bg-gray-50">
                <tr>
                  <th className="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">Código</th>
                  <th className="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">Descripción</th>
                  <th className="px-4 py-2 text-center text-xs font-medium text-gray-500 uppercase">Cant.</th>
                  <th className="px-4 py-2 text-right text-xs font-medium text-gray-500 uppercase">Precio</th>
                  <th className="px-4 py-2 text-right text-xs font-medium text-gray-500 uppercase">Importe</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-gray-200">
                {facturaSeleccionada.items?.map((item, index) => (
                  <tr key={index}>
                    <td className="px-4 py-2 text-sm font-mono">{item.codigo}</td>
                    <td className="px-4 py-2 text-sm">{item.descripcion}</td>
                    <td className="px-4 py-2 text-sm text-center">{item.cantidad}</td>
                    <td className="px-4 py-2 text-sm text-right">${item.precio?.toFixed(2)}</td>
                    <td className="px-4 py-2 text-sm text-right font-semibold">${item.importe?.toFixed(2)}</td>
                  </tr>
                ))}
              </tbody>
            </table>

            <div className="border-t pt-4 flex justify-between items-center">
              <span className="text-xl font-bold text-gray-800">
                Total: ${facturaSeleccionada.total?.toFixed(2)}
              </span>
              <button
                onClick={() => descargarFactura(facturaSeleccionada.ticket)}
                className="bg-blue-600 text-white px-4 py-2 rounded-lg hover:bg-blue-700 transition-colors flex items-center gap-2"
              >
                <Download className="w-4 h-4" />
                Descargar PDF
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  )
}
