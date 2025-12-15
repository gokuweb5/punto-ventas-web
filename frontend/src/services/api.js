import axios from 'axios'

const api = axios.create({
  baseURL: '/api',
})

api.interceptors.request.use((config) => {
  const token = JSON.parse(localStorage.getItem('auth-storage'))?.state?.token
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

export const authAPI = {
  login: (credentials) => api.post('/auth/login', credentials),
}

export const productosAPI = {
  getAll: () => api.get('/productos'),
  getById: (id) => api.get(`/productos/${id}`),
  getByCodigo: (codigo) => api.get(`/productos/codigo/${codigo}`),
  create: (producto) => api.post('/productos', producto),
  update: (id, producto) => api.put(`/productos/${id}`, producto),
  delete: (id) => api.delete(`/productos/${id}`),
}

export const ventasAPI = {
  getAll: () => api.get('/ventas'),
  getByFecha: (inicio, fin) => api.get(`/ventas/fecha?inicio=${inicio}&fin=${fin}`),
  create: (venta) => api.post('/ventas', venta),
}

export const clientesAPI = {
  getAll: () => api.get('/clientes'),
  getById: (id) => api.get(`/clientes/${id}`),
  create: (cliente) => api.post('/clientes', cliente),
  update: (id, cliente) => api.put(`/clientes/${id}`, cliente),
  delete: (id) => api.delete(`/clientes/${id}`),
}

export const categoriasAPI = {
  getAll: () => api.get('/categorias'),
  getActivas: () => api.get('/categorias/activas'),
  getById: (id) => api.get(`/categorias/${id}`),
  create: (categoria) => api.post('/categorias', categoria),
  update: (id, categoria) => api.put(`/categorias/${id}`, categoria),
  delete: (id) => api.delete(`/categorias/${id}`),
}

export const facturasAPI = {
  getAll: () => api.get('/facturas'),
  descargar: (ticket) => `http://localhost:8091/api/facturas/generar/${ticket}`,
}

export default api
