import { useState, useEffect } from 'react'
import { ShoppingCart, Plus, Trash2, FileText } from 'lucide-react'
import { productosAPI, clientesAPI, ventasAPI } from '../services/api'

export default function Ventas() {
  const [carrito, setCarrito] = useState([])
  const [codigoBusqueda, setCodigoBusqueda] = useState('')
  const [clientes, setClientes] = useState([])
  const [clienteSeleccionado, setClienteSeleccionado] = useState('')
  const [esCredito, setEsCredito] = useState(false)
  const [procesando, setProcesando] = useState(false)
  const [ultimoTicket, setUltimoTicket] = useState(null)
  const [mostrarExito, setMostrarExito] = useState(false)

  useEffect(() => {
    cargarClientes()
  }, [])

  const cargarClientes = async () => {
    try {
      const response = await clientesAPI.getAll()
      setClientes(response.data)
    } catch (error) {
      console.error('Error al cargar clientes:', error)
    }
  }

  const buscarProducto = async (e) => {
    if (e.key === 'Enter' && codigoBusqueda.trim()) {
      try {
        const response = await productosAPI.getByCodigo(codigoBusqueda.trim())
        agregarAlCarrito(response.data)
        setCodigoBusqueda('')
      } catch (error) {
        console.error('Error al buscar producto:', error)
        alert('Producto no encontrado')
      }
    }
  }

  const agregarAlCarrito = (producto) => {
    const itemExistente = carrito.find(item => item.idProducto === producto.idProducto)
    
    if (itemExistente) {
      if (itemExistente.cantidad + 1 > producto.existencia) {
        alert(`Stock insuficiente. Disponible: ${producto.existencia}`)
        return
      }
      setCarrito(carrito.map(item =>
        item.idProducto === producto.idProducto
          ? { ...item, cantidad: item.cantidad + 1 }
          : item
      ))
    } else {
      if (producto.existencia < 1) {
        alert('Producto sin stock')
        return
      }
      setCarrito([...carrito, {
        idProducto: producto.idProducto,
        codigo: producto.codigo,
        nombre: producto.producto,
        precio: producto.precio,
        cantidad: 1,
        existencia: producto.existencia,
        descuento: producto.descuento || 0
      }])
    }
  }

  const actualizarCantidad = (idProducto, nuevaCantidad) => {
    const item = carrito.find(i => i.idProducto === idProducto)
    if (nuevaCantidad > item.existencia) {
      alert(`Stock insuficiente. Disponible: ${item.existencia}`)
      return
    }
    if (nuevaCantidad < 1) return
    
    setCarrito(carrito.map(item =>
      item.idProducto === idProducto
        ? { ...item, cantidad: parseInt(nuevaCantidad) }
        : item
    ))
  }

  const eliminarDelCarrito = (idProducto) => {
    setCarrito(carrito.filter(item => item.idProducto !== idProducto))
  }

  const procesarVenta = async () => {
    if (carrito.length === 0) return
    
    setProcesando(true)
    try {
      const ventaRequest = {
        idCliente: clienteSeleccionado ? parseInt(clienteSeleccionado) : null,
        credito: esCredito,
        items: carrito.map(item => ({
          idProducto: item.idProducto,
          codigo: item.codigo,
          cantidad: item.cantidad,
          precio: item.precio,
          descuento: item.descuento
        }))
      }

      const response = await ventasAPI.create(ventaRequest)
      setUltimoTicket(response.data.ticket)
      setMostrarExito(true)
      setCarrito([])
      setClienteSeleccionado('')
      setEsCredito(false)
    } catch (error) {
      console.error('Error al procesar venta:', error)
      alert(error.response?.data?.message || 'Error al procesar la venta')
    } finally {
      setProcesando(false)
    }
  }

  const descargarFactura = () => {
    if (ultimoTicket) {
      window.open(`http://localhost:8091/api/facturas/generar/${ultimoTicket}`, '_blank')
    }
  }

  const cerrarModalExito = () => {
    setMostrarExito(false)
    setUltimoTicket(null)
  }

  const total = carrito.reduce((sum, item) => sum + (item.precio * item.cantidad), 0)
  const totalDescuentos = carrito.reduce((sum, item) => sum + (item.descuento * item.cantidad), 0)
  const totalFinal = total - totalDescuentos

  return (
    <div className="p-8">
      <h1 className="text-3xl font-bold text-gray-800 mb-8">Nueva Venta</h1>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        {/* Panel de búsqueda y productos */}
        <div className="lg:col-span-2">
          <div className="bg-white rounded-lg shadow p-6 mb-6">
            <label className="block text-sm font-medium text-gray-700 mb-2">
              Buscar Producto (Código o Nombre)
            </label>
            <input
              type="text"
              value={codigoBusqueda}
              onChange={(e) => setCodigoBusqueda(e.target.value)}
              onKeyDown={buscarProducto}
              placeholder="Escanea o escribe el código del producto..."
              className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
              autoFocus
            />
          </div>

          <div className="bg-white rounded-lg shadow p-6">
            <h2 className="text-xl font-bold text-gray-800 mb-4">Carrito de Compra</h2>
            {carrito.length === 0 ? (
              <div className="text-center py-12 text-gray-500">
                <ShoppingCart className="w-16 h-16 mx-auto mb-4 text-gray-300" />
                <p>El carrito está vacío</p>
                <p className="text-sm">Agrega productos para comenzar una venta</p>
              </div>
            ) : (
              <div className="space-y-4">
                {carrito.map((item) => (
                  <div key={item.idProducto} className="flex items-center justify-between border-b pb-4">
                    <div className="flex-1">
                      <p className="font-medium text-gray-900">{item.nombre}</p>
                      <p className="text-sm text-gray-600">Código: {item.codigo}</p>
                    </div>
                    <div className="flex items-center gap-4">
                      <input
                        type="number"
                        min="1"
                        max={item.existencia}
                        value={item.cantidad}
                        onChange={(e) => actualizarCantidad(item.idProducto, e.target.value)}
                        className="w-20 px-2 py-1 border border-gray-300 rounded"
                      />
                      <p className="font-medium text-gray-900 w-24 text-right">
                        ${(item.precio * item.cantidad).toFixed(2)}
                      </p>
                      <button 
                        onClick={() => eliminarDelCarrito(item.idProducto)}
                        className="text-red-600 hover:text-red-900"
                      >
                        <Trash2 className="w-5 h-5" />
                      </button>
                    </div>
                  </div>
                ))}
              </div>
            )}
          </div>
        </div>

        {/* Panel de resumen */}
        <div className="lg:col-span-1">
          <div className="bg-white rounded-lg shadow p-6 sticky top-8">
            <h2 className="text-xl font-bold text-gray-800 mb-4">Resumen</h2>
            
            <div className="space-y-3 mb-6">
              <div className="flex justify-between text-gray-600">
                <span>Subtotal:</span>
                <span>${total.toFixed(2)}</span>
              </div>
              <div className="flex justify-between text-gray-600">
                <span>Descuento:</span>
                <span>${totalDescuentos.toFixed(2)}</span>
              </div>
              <div className="border-t pt-3 flex justify-between text-xl font-bold text-gray-900">
                <span>Total:</span>
                <span>${totalFinal.toFixed(2)}</span>
              </div>
            </div>

            <div className="mb-4">
              <label className="block text-sm font-medium text-gray-700 mb-2">
                Cliente (Opcional)
              </label>
              <select 
                value={clienteSeleccionado}
                onChange={(e) => setClienteSeleccionado(e.target.value)}
                className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
              >
                <option value="">Seleccionar cliente...</option>
                {clientes.map(cliente => (
                  <option key={cliente.id} value={cliente.id}>
                    {cliente.nombre} {cliente.apellido}
                  </option>
                ))}
              </select>
            </div>

            <div className="mb-6">
              <label className="flex items-center">
                <input 
                  type="checkbox" 
                  checked={esCredito}
                  onChange={(e) => setEsCredito(e.target.checked)}
                  className="mr-2" 
                />
                <span className="text-sm text-gray-700">Venta a crédito</span>
              </label>
            </div>

            <button
              onClick={procesarVenta}
              disabled={carrito.length === 0 || procesando}
              className="w-full bg-green-600 text-white py-3 rounded-lg font-semibold hover:bg-green-700 transition-colors disabled:bg-gray-400 flex items-center justify-center gap-2"
            >
              <ShoppingCart className="w-5 h-5" />
              {procesando ? 'Procesando...' : 'Procesar Venta'}
            </button>
          </div>
        </div>
      </div>

      {/* Modal de Éxito */}
      {mostrarExito && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
          <div className="bg-white rounded-lg p-8 max-w-md w-full mx-4">
            <div className="text-center">
              <div className="mx-auto flex items-center justify-center h-12 w-12 rounded-full bg-green-100 mb-4">
                <svg className="h-6 w-6 text-green-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M5 13l4 4L19 7" />
                </svg>
              </div>
              <h3 className="text-lg font-medium text-gray-900 mb-2">
                ¡Venta Procesada Exitosamente!
              </h3>
              <p className="text-sm text-gray-500 mb-4">
                Ticket: <span className="font-mono font-semibold">{ultimoTicket}</span>
              </p>
              
              <div className="flex gap-3">
                <button
                  onClick={descargarFactura}
                  className="flex-1 bg-blue-600 text-white px-4 py-2 rounded-lg hover:bg-blue-700 transition-colors flex items-center justify-center gap-2"
                >
                  <FileText className="w-4 h-4" />
                  Descargar Factura
                </button>
                <button
                  onClick={cerrarModalExito}
                  className="flex-1 bg-gray-300 text-gray-700 px-4 py-2 rounded-lg hover:bg-gray-400 transition-colors"
                >
                  Cerrar
                </button>
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  )
}
