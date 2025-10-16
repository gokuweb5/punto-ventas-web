import { useState, useEffect } from 'react'
import { FileText, AlertCircle, CheckCircle } from 'lucide-react'

export default function ConfiguracionFacturas() {
  const [estado, setEstado] = useState(null)
  const [nuevoRango, setNuevoRango] = useState({ inicio: '', fin: '' })
  const [loading, setLoading] = useState(true)
  const [guardando, setGuardando] = useState(false)

  useEffect(() => {
    cargarEstado()
  }, [])

  const cargarEstado = async () => {
    try {
      const response = await fetch('http://localhost:8091/api/configuracion-facturas/estado')
      const data = await response.json()
      setEstado(data)
    } catch (error) {
      console.error('Error al cargar estado:', error)
    } finally {
      setLoading(false)
    }
  }

  const configurarNuevoRango = async (e) => {
    e.preventDefault()
    setGuardando(true)
    
    try {
      const response = await fetch('http://localhost:8091/api/configuracion-facturas/configurar', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          inicio: parseInt(nuevoRango.inicio),
          fin: parseInt(nuevoRango.fin)
        })
      })

      if (response.ok) {
        alert('Rango de facturas configurado exitosamente')
        setNuevoRango({ inicio: '', fin: '' })
        cargarEstado()
      } else {
        alert('Error al configurar rango de facturas')
      }
    } catch (error) {
      console.error('Error:', error)
      alert('Error al configurar rango de facturas')
    } finally {
      setGuardando(false)
    }
  }

  if (loading) {
    return (
      <div className="p-8">
        <div className="text-center">Cargando...</div>
      </div>
    )
  }

  return (
    <div className="p-8">
      <h1 className="text-3xl font-bold text-gray-800 mb-8">Configuración de Facturas</h1>

      {/* Estado Actual */}
      <div className="bg-white rounded-lg shadow p-6 mb-6">
        <h2 className="text-xl font-semibold mb-4 flex items-center gap-2">
          <FileText className="w-5 h-5" />
          Estado Actual
        </h2>

        {estado?.necesitaNuevoRango ? (
          <div className="bg-red-50 border border-red-200 rounded-lg p-4 mb-4">
            <div className="flex items-center gap-2 text-red-800">
              <AlertCircle className="w-5 h-5" />
              <span className="font-semibold">¡Se agotaron las facturas!</span>
            </div>
            <p className="text-red-600 text-sm mt-2">
              Debes configurar un nuevo rango de facturas para poder continuar vendiendo.
            </p>
          </div>
        ) : (
          <div className="bg-green-50 border border-green-200 rounded-lg p-4 mb-4">
            <div className="flex items-center gap-2 text-green-800">
              <CheckCircle className="w-5 h-5" />
              <span className="font-semibold">Sistema operativo</span>
            </div>
            <p className="text-green-600 text-sm mt-2">
              Facturas disponibles: <span className="font-bold">{estado?.facturasRestantes}</span>
            </p>
          </div>
        )}

        <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
          <div className="bg-gray-50 p-4 rounded-lg">
            <p className="text-sm text-gray-600">Número Actual</p>
            <p className="text-2xl font-bold text-gray-800">
              {String(estado?.numeroActual || 0).padStart(8, '0')}
            </p>
          </div>
          <div className="bg-gray-50 p-4 rounded-lg">
            <p className="text-sm text-gray-600">Número Final</p>
            <p className="text-2xl font-bold text-gray-800">
              {String(estado?.numeroFin || 0).padStart(8, '0')}
            </p>
          </div>
          <div className="bg-gray-50 p-4 rounded-lg">
            <p className="text-sm text-gray-600">Facturas Restantes</p>
            <p className="text-2xl font-bold text-blue-600">
              {estado?.facturasRestantes || 0}
            </p>
          </div>
        </div>
      </div>

      {/* Configurar Nuevo Rango */}
      <div className="bg-white rounded-lg shadow p-6">
        <h2 className="text-xl font-semibold mb-4">Configurar Nuevo Rango</h2>
        
        <form onSubmit={configurarNuevoRango} className="space-y-4">
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">
                Número de Inicio
              </label>
              <input
                type="number"
                value={nuevoRango.inicio}
                onChange={(e) => setNuevoRango({ ...nuevoRango, inicio: e.target.value })}
                className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                placeholder="Ej: 1001"
                required
                min="1"
              />
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">
                Número Final
              </label>
              <input
                type="number"
                value={nuevoRango.fin}
                onChange={(e) => setNuevoRango({ ...nuevoRango, fin: e.target.value })}
                className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                placeholder="Ej: 2000"
                required
                min="2"
              />
            </div>
          </div>

          {nuevoRango.inicio && nuevoRango.fin && (
            <div className="bg-blue-50 border border-blue-200 rounded-lg p-4">
              <p className="text-sm text-blue-800">
                Se configurarán <span className="font-bold">
                  {parseInt(nuevoRango.fin) - parseInt(nuevoRango.inicio) + 1}
                </span> facturas disponibles
              </p>
            </div>
          )}

          <div className="bg-yellow-50 border border-yellow-200 rounded-lg p-4">
            <p className="text-sm text-yellow-800">
              <strong>Importante:</strong> Al configurar un nuevo rango, el rango anterior se desactivará automáticamente.
            </p>
          </div>

          <button
            type="submit"
            disabled={guardando || !nuevoRango.inicio || !nuevoRango.fin}
            className="w-full bg-blue-600 text-white py-3 rounded-lg font-semibold hover:bg-blue-700 transition-colors disabled:bg-gray-400"
          >
            {guardando ? 'Configurando...' : 'Configurar Rango'}
          </button>
        </form>
      </div>
    </div>
  )
}
