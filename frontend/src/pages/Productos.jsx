import { useState, useEffect } from 'react'
import { productosAPI, categoriasAPI } from '../services/api'
import { Plus, Search, Edit, Trash2, X, Image as ImageIcon } from 'lucide-react'

export default function Productos() {
  const [productos, setProductos] = useState([])
  const [categorias, setCategorias] = useState([])
  const [loading, setLoading] = useState(true)
  const [searchTerm, setSearchTerm] = useState('')
  const [showModal, setShowModal] = useState(false)
  const [editingProducto, setEditingProducto] = useState(null)
  const [formData, setFormData] = useState({
    codigo: '',
    producto: '',
    precio: '',
    existencia: '',
    categoria: '',
    idCategoria: '',
    imagenUrl: '',
    caracteristicas: '',
    codigoBarras: '',
    numeroSerie: ''
  })
  const [imagenFile, setImagenFile] = useState(null)
  const [imagenPreview, setImagenPreview] = useState('')
  const [imagenModal, setImagenModal] = useState(null)

  useEffect(() => {
    cargarProductos()
    cargarCategorias()
  }, [])

  const cargarProductos = async () => {
    try {
      const response = await productosAPI.getAll()
      setProductos(response.data)
    } catch (error) {
      console.error('Error al cargar productos:', error)
      alert('Error al cargar productos')
    } finally {
      setLoading(false)
    }
  }

  const cargarCategorias = async () => {
    try {
      const response = await categoriasAPI.getActivas()
      setCategorias(response.data)
    } catch (error) {
      console.error('Error al cargar categorías:', error)
    }
  }

  const handleOpenModal = (producto = null) => {
    if (producto) {
      setEditingProducto(producto)
      setFormData({
        codigo: producto.codigo,
        producto: producto.producto,
        precio: producto.precio,
        existencia: producto.existencia,
        categoria: producto.categoria || '',
        idCategoria: producto.categoriaObj?.idCategoria || '',
        imagenUrl: producto.imagenUrl || '',
        caracteristicas: producto.caracteristicas || '',
        codigoBarras: producto.codigoBarras || '',
        numeroSerie: producto.numeroSerie || ''
      })
      setImagenPreview(producto.imagenUrl || '')
    } else {
      setEditingProducto(null)
      setFormData({
        codigo: '',
        producto: '',
        precio: '',
        existencia: '',
        categoria: '',
        idCategoria: '',
        imagenUrl: '',
        caracteristicas: '',
        codigoBarras: '',
        numeroSerie: ''
      })
      setImagenPreview('')
    }
    setImagenFile(null)
    setShowModal(true)
  }

  const handleCloseModal = () => {
    setShowModal(false)
    setEditingProducto(null)
    setFormData({
      codigo: '',
      producto: '',
      precio: '',
      existencia: '',
      categoria: '',
      idCategoria: '',
      imagenUrl: '',
      caracteristicas: '',
      codigoBarras: '',
      numeroSerie: ''
    })
    setImagenFile(null)
    setImagenPreview('')
  }

  const handleInputChange = (e) => {
    const { name, value } = e.target
    setFormData(prev => ({
      ...prev,
      [name]: value
    }))
  }

  const handleImagenChange = (e) => {
    const file = e.target.files[0]
    if (file) {
      // Validar que sea imagen
      if (!file.type.startsWith('image/')) {
        alert('Por favor selecciona un archivo de imagen')
        return
      }
      setImagenFile(file)
      // Crear preview
      const reader = new FileReader()
      reader.onloadend = () => {
        setImagenPreview(reader.result)
      }
      reader.readAsDataURL(file)
    }
  }

  const handleSubmit = async (e) => {
    e.preventDefault()
    try {
      // Si hay imagen, subirla primero
      let imageUrl = formData.imagenUrl
      if (imagenFile) {
        const formDataImg = new FormData()
        formDataImg.append('file', imagenFile)
        formDataImg.append('codigo', formData.codigo)
        
        const uploadResponse = await fetch('http://localhost:8091/api/imagenes/upload', {
          method: 'POST',
          body: formDataImg
        })
        
        if (uploadResponse.ok) {
          const uploadData = await uploadResponse.json()
          imageUrl = `http://localhost:8091/api/imagenes/${formData.codigo}.png`
        }
      }

      const productoData = {
        codigo: formData.codigo,
        producto: formData.producto,
        precio: parseFloat(formData.precio),
        existencia: parseInt(formData.existencia),
        descuento: formData.descuento ? parseFloat(formData.descuento) : 0,
        departamento: formData.departamento || null,
        categoria: formData.categoria || null,
        idCategoria: formData.idCategoria ? parseInt(formData.idCategoria) : null,
        imagenUrl: imageUrl || null,
        caracteristicas: formData.caracteristicas || null,
        codigoBarras: formData.codigoBarras || null,
        numeroSerie: formData.numeroSerie || null
      }

      if (editingProducto) {
        await productosAPI.update(editingProducto.idProducto, productoData)
        alert('Producto actualizado exitosamente')
      } else {
        await productosAPI.create(productoData)
        alert('Producto creado exitosamente')
      }
      handleCloseModal()
      cargarProductos()
    } catch (error) {
      console.error('Error al guardar producto:', error)
      alert('Error al guardar producto')
    }
  }

  const handleDelete = async (id) => {
    if (window.confirm('¿Estás seguro de eliminar este producto?')) {
      try {
        await productosAPI.delete(id)
        alert('Producto eliminado exitosamente')
        cargarProductos()
      } catch (error) {
        console.error('Error al eliminar producto:', error)
        alert('Error al eliminar producto')
      }
    }
  }

  const productosFiltrados = productos.filter(p =>
    p.producto.toLowerCase().includes(searchTerm.toLowerCase()) ||
    p.codigo.toLowerCase().includes(searchTerm.toLowerCase())
  )

  return (
    <div className="p-8">
      <div className="flex justify-between items-center mb-8">
        <h1 className="text-3xl font-bold text-gray-800">Productos</h1>
        <button 
          onClick={() => handleOpenModal()}
          className="bg-blue-600 text-white px-6 py-2 rounded-lg hover:bg-blue-700 flex items-center gap-2"
        >
          <Plus className="w-5 h-5" />
          Nuevo Producto
        </button>
      </div>

      <div className="bg-white rounded-lg shadow mb-6 p-4">
        <div className="relative">
          <Search className="absolute left-3 top-3 w-5 h-5 text-gray-400" />
          <input
            type="text"
            placeholder="Buscar por nombre o código..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
            className="w-full pl-10 pr-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
          />
        </div>
      </div>

      <div className="bg-white rounded-lg shadow overflow-hidden">
        <table className="min-w-full divide-y divide-gray-200">
          <thead className="bg-gray-50">
            <tr>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Imagen
              </th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Código
              </th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Producto
              </th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Precio
              </th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Existencia
              </th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Categoría
              </th>
              <th className="px-6 py-3 text-right text-xs font-medium text-gray-500 uppercase tracking-wider">
                Acciones
              </th>
            </tr>
          </thead>
          <tbody className="bg-white divide-y divide-gray-200">
            {loading ? (
              <tr>
                <td colSpan="7" className="px-6 py-4 text-center text-gray-500">
                  Cargando productos...
                </td>
              </tr>
            ) : productosFiltrados.length === 0 ? (
              <tr>
                <td colSpan="7" className="px-6 py-4 text-center text-gray-500">
                  No se encontraron productos
                </td>
              </tr>
            ) : (
              productosFiltrados.map((producto) => (
                <tr key={producto.idProducto} className="hover:bg-gray-50">
                  <td className="px-6 py-4 whitespace-nowrap">
                    {producto.imagenUrl ? (
                      <img 
                        src={producto.imagenUrl} 
                        alt={producto.producto} 
                        className="w-12 h-12 object-cover rounded cursor-pointer hover:opacity-75 transition-opacity" 
                        onClick={() => setImagenModal(producto.imagenUrl)}
                      />
                    ) : (
                      <div className="w-12 h-12 bg-gray-200 rounded flex items-center justify-center">
                        <ImageIcon className="w-6 h-6 text-gray-400" />
                      </div>
                    )}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                    {producto.codigo}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                    {producto.producto}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                    ${producto.precio.toFixed(2)}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                    {producto.existencia}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                    {producto.categoriaObj?.nombre || producto.categoria || '-'}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-right text-sm font-medium">
                    <button 
                      onClick={() => handleOpenModal(producto)}
                      className="text-blue-600 hover:text-blue-900 mr-4"
                      title="Editar"
                    >
                      <Edit className="w-5 h-5" />
                    </button>
                    <button 
                      onClick={() => handleDelete(producto.idProducto)}
                      className="text-red-600 hover:text-red-900"
                      title="Eliminar"
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
      {showModal && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 p-4">
          <div className="bg-white rounded-lg p-8 max-w-2xl w-full max-h-[90vh] overflow-y-auto">
            <div className="flex justify-between items-center mb-6">
              <h2 className="text-2xl font-bold text-gray-800">
                {editingProducto ? 'Editar Producto' : 'Nuevo Producto'}
              </h2>
              <button 
                onClick={handleCloseModal}
                className="text-gray-500 hover:text-gray-700"
              >
                <X className="w-6 h-6" />
              </button>
            </div>

            <form onSubmit={handleSubmit} className="space-y-4">
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">
                  Código *
                </label>
                <input
                  type="text"
                  name="codigo"
                  value={formData.codigo}
                  onChange={handleInputChange}
                  required
                  className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                  placeholder="Ej: PROD001"
                />
              </div>

              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">
                  Nombre del Producto *
                </label>
                <input
                  type="text"
                  name="producto"
                  value={formData.producto}
                  onChange={handleInputChange}
                  required
                  className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                  placeholder="Ej: Laptop HP"
                />
              </div>

              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">
                  Precio *
                </label>
                <input
                  type="number"
                  name="precio"
                  value={formData.precio}
                  onChange={handleInputChange}
                  required
                  step="0.01"
                  min="0"
                  className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                  placeholder="0.00"
                />
              </div>

              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">
                  Existencia *
                </label>
                <input
                  type="number"
                  name="existencia"
                  value={formData.existencia}
                  onChange={handleInputChange}
                  required
                  min="0"
                  className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                  placeholder="0"
                />
              </div>

              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">
                  Categoría
                </label>
                <select
                  name="idCategoria"
                  value={formData.idCategoria}
                  onChange={handleInputChange}
                  className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                >
                  <option value="">Sin categoría</option>
                  {categorias.map(cat => (
                    <option key={cat.idCategoria} value={cat.idCategoria}>
                      {cat.nombre}
                    </option>
                  ))}
                </select>
              </div>

              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">
                  Código de Barras
                </label>
                <input
                  type="text"
                  name="codigoBarras"
                  value={formData.codigoBarras}
                  onChange={handleInputChange}
                  className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                  placeholder="Ej: 7501234567890"
                />
              </div>

              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">
                  Número de Serie
                </label>
                <input
                  type="text"
                  name="numeroSerie"
                  value={formData.numeroSerie}
                  onChange={handleInputChange}
                  className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                  placeholder="Ej: SN123456789"
                />
              </div>

              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">
                  Características / Especificaciones
                </label>
                <textarea
                  name="caracteristicas"
                  value={formData.caracteristicas}
                  onChange={handleInputChange}
                  rows="3"
                  className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                  placeholder="Ej: Intel Core i3, 8GB RAM, 512GB SSD, 14&quot; HD"
                />
              </div>

              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">
                  Imagen del Producto
                </label>
                <input
                  type="file"
                  accept="image/*"
                  onChange={handleImagenChange}
                  className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                />
                <p className="text-xs text-gray-500 mt-1">
                  Se guardará como: {formData.codigo || 'CODIGO'}.png
                </p>
                {imagenPreview && (
                  <div className="mt-2">
                    <img src={imagenPreview} alt="Vista previa" className="w-32 h-32 object-cover rounded border" />
                  </div>
                )}
              </div>

              <div className="flex gap-3 pt-4">
                <button
                  type="button"
                  onClick={handleCloseModal}
                  className="flex-1 px-4 py-2 border border-gray-300 text-gray-700 rounded-lg hover:bg-gray-50"
                >
                  Cancelar
                </button>
                <button
                  type="submit"
                  className="flex-1 px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700"
                >
                  {editingProducto ? 'Actualizar' : 'Crear'}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}

      {/* Modal de Imagen */}
      {imagenModal && (
        <div 
          className="fixed inset-0 bg-black bg-opacity-90 flex items-center justify-center z-50 p-4"
          onClick={() => setImagenModal(null)}
        >
          <div className="relative max-w-4xl max-h-[90vh]">
            <button
              onClick={() => setImagenModal(null)}
              className="absolute -top-10 right-0 text-white hover:text-gray-300 text-2xl font-bold"
            >
              <X className="w-8 h-8" />
            </button>
            <img 
              src={imagenModal} 
              alt="Vista completa" 
              className="max-w-full max-h-[85vh] object-contain rounded-lg"
              onClick={(e) => e.stopPropagation()}
            />
          </div>
        </div>
      )}
    </div>
  )
}
