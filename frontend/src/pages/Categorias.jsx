import { useState, useEffect } from 'react'
import { Plus, Edit2, Trash2, FolderOpen } from 'lucide-react'
import { categoriasAPI } from '../services/api'

export default function Categorias() {
  const [categorias, setCategorias] = useState([])
  const [mostrarModal, setMostrarModal] = useState(false)
  const [categoriaEditando, setCategoriaEditando] = useState(null)
  const [formData, setFormData] = useState({
    nombre: '',
    descripcion: '',
    activo: true
  })

  useEffect(() => {
    cargarCategorias()
  }, [])

  const cargarCategorias = async () => {
    try {
      const response = await categoriasAPI.getAll()
      setCategorias(response.data)
    } catch (error) {
      console.error('Error al cargar categorías:', error)
      alert('Error al cargar categorías')
    }
  }

  const abrirModal = (categoria = null) => {
    if (categoria) {
      setCategoriaEditando(categoria)
      setFormData({
        nombre: categoria.nombre,
        descripcion: categoria.descripcion || '',
        activo: categoria.activo
      })
    } else {
      setCategoriaEditando(null)
      setFormData({
        nombre: '',
        descripcion: '',
        activo: true
      })
    }
    setMostrarModal(true)
  }

  const cerrarModal = () => {
    setMostrarModal(false)
    setCategoriaEditando(null)
    setFormData({
      nombre: '',
      descripcion: '',
      activo: true
    })
  }

  const handleSubmit = async (e) => {
    e.preventDefault()
    
    try {
      if (categoriaEditando) {
        await categoriasAPI.update(categoriaEditando.idCategoria, formData)
        alert('Categoría actualizada exitosamente')
      } else {
        await categoriasAPI.create(formData)
        alert('Categoría creada exitosamente')
      }
      cargarCategorias()
      cerrarModal()
    } catch (error) {
      console.error('Error al guardar categoría:', error)
      alert(error.response?.data?.message || 'Error al guardar categoría')
    }
  }

  const eliminarCategoria = async (id) => {
    if (!confirm('¿Estás seguro de desactivar esta categoría?')) return
    
    try {
      await categoriasAPI.delete(id)
      alert('Categoría desactivada exitosamente')
      cargarCategorias()
    } catch (error) {
      console.error('Error al eliminar categoría:', error)
      alert('Error al eliminar categoría')
    }
  }

  return (
    <div className="p-8">
      <div className="flex justify-between items-center mb-8">
        <h1 className="text-3xl font-bold text-gray-800">Categorías</h1>
        <button
          onClick={() => abrirModal()}
          className="bg-blue-600 text-white px-4 py-2 rounded-lg hover:bg-blue-700 transition-colors flex items-center gap-2"
        >
          <Plus className="w-5 h-5" />
          Nueva Categoría
        </button>
      </div>

      <div className="bg-white rounded-lg shadow overflow-hidden">
        <table className="min-w-full divide-y divide-gray-200">
          <thead className="bg-gray-50">
            <tr>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Nombre
              </th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Descripción
              </th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Estado
              </th>
              <th className="px-6 py-3 text-right text-xs font-medium text-gray-500 uppercase tracking-wider">
                Acciones
              </th>
            </tr>
          </thead>
          <tbody className="bg-white divide-y divide-gray-200">
            {categorias.length === 0 ? (
              <tr>
                <td colSpan="4" className="px-6 py-12 text-center text-gray-500">
                  <FolderOpen className="w-16 h-16 mx-auto mb-4 text-gray-300" />
                  <p>No hay categorías registradas</p>
                </td>
              </tr>
            ) : (
              categorias.map((categoria) => (
                <tr key={categoria.idCategoria} className="hover:bg-gray-50">
                  <td className="px-6 py-4 whitespace-nowrap">
                    <div className="font-medium text-gray-900">{categoria.nombre}</div>
                  </td>
                  <td className="px-6 py-4">
                    <div className="text-sm text-gray-600">{categoria.descripcion || '-'}</div>
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap">
                    <span className={`px-2 py-1 inline-flex text-xs leading-5 font-semibold rounded-full ${
                      categoria.activo 
                        ? 'bg-green-100 text-green-800' 
                        : 'bg-red-100 text-red-800'
                    }`}>
                      {categoria.activo ? 'Activa' : 'Inactiva'}
                    </span>
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-right text-sm font-medium">
                    <button
                      onClick={() => abrirModal(categoria)}
                      className="text-blue-600 hover:text-blue-900 mr-4"
                    >
                      <Edit2 className="w-5 h-5" />
                    </button>
                    <button
                      onClick={() => eliminarCategoria(categoria.idCategoria)}
                      className="text-red-600 hover:text-red-900"
                    >
                      <Trash2 className="w-5 h-5" />
                    </button>
                  </td>
                </tr>
              ))
            )}
          </tbody>
        </table>
      </div>

      {/* Modal */}
      {mostrarModal && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
          <div className="bg-white rounded-lg p-8 max-w-md w-full">
            <h2 className="text-2xl font-bold mb-6">
              {categoriaEditando ? 'Editar Categoría' : 'Nueva Categoría'}
            </h2>
            
            <form onSubmit={handleSubmit}>
              <div className="mb-4">
                <label className="block text-sm font-medium text-gray-700 mb-2">
                  Nombre *
                </label>
                <input
                  type="text"
                  value={formData.nombre}
                  onChange={(e) => setFormData({ ...formData, nombre: e.target.value })}
                  className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                  required
                />
              </div>

              <div className="mb-4">
                <label className="block text-sm font-medium text-gray-700 mb-2">
                  Descripción
                </label>
                <textarea
                  value={formData.descripcion}
                  onChange={(e) => setFormData({ ...formData, descripcion: e.target.value })}
                  className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                  rows="3"
                />
              </div>

              <div className="mb-6">
                <label className="flex items-center">
                  <input
                    type="checkbox"
                    checked={formData.activo}
                    onChange={(e) => setFormData({ ...formData, activo: e.target.checked })}
                    className="mr-2"
                  />
                  <span className="text-sm text-gray-700">Categoría activa</span>
                </label>
              </div>

              <div className="flex gap-4">
                <button
                  type="button"
                  onClick={cerrarModal}
                  className="flex-1 bg-gray-300 text-gray-700 py-2 rounded-lg hover:bg-gray-400 transition-colors"
                >
                  Cancelar
                </button>
                <button
                  type="submit"
                  className="flex-1 bg-blue-600 text-white py-2 rounded-lg hover:bg-blue-700 transition-colors"
                >
                  {categoriaEditando ? 'Actualizar' : 'Crear'}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  )
}
