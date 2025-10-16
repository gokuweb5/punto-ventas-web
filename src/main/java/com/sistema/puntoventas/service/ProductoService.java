package com.sistema.puntoventas.service;

import com.sistema.puntoventas.dto.ProductoDTO;
import com.sistema.puntoventas.entity.Bodega;
import com.sistema.puntoventas.entity.Categoria;
import com.sistema.puntoventas.entity.Producto;
import com.sistema.puntoventas.repository.BodegaRepository;
import com.sistema.puntoventas.repository.CategoriaRepository;
import com.sistema.puntoventas.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final BodegaRepository bodegaRepository;
    private final CategoriaRepository categoriaRepository;

    @Transactional(readOnly = true)
    public List<ProductoDTO> obtenerTodosLosProductos() {
        return productoRepository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ProductoDTO obtenerProductoPorId(Integer id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        return convertirADTO(producto);
    }

    @Transactional(readOnly = true)
    public ProductoDTO obtenerProductoPorCodigo(String codigo) {
        Producto producto = productoRepository.findByCodigo(codigo)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        return convertirADTO(producto);
    }

    @Transactional
    public Producto crearProducto(ProductoDTO productoDTO) {
        // Crear y guardar el producto
        Producto producto = new Producto();
        producto.setCodigo(productoDTO.getCodigo());
        producto.setProducto(productoDTO.getProducto());
        producto.setPrecio(productoDTO.getPrecio());
        producto.setDescuento(productoDTO.getDescuento() != null ? productoDTO.getDescuento() : java.math.BigDecimal.ZERO);
        producto.setDepartamento(productoDTO.getDepartamento());
        producto.setCategoria(productoDTO.getCategoria());
        producto.setImagenUrl(productoDTO.getImagenUrl());
        producto.setCaracteristicas(productoDTO.getCaracteristicas());
        producto.setCodigoBarras(productoDTO.getCodigoBarras());
        producto.setNumeroSerie(productoDTO.getNumeroSerie());
        
        // Asignar categoría si existe
        if (productoDTO.getIdCategoria() != null) {
            categoriaRepository.findById(productoDTO.getIdCategoria())
                    .ifPresent(producto::setCategoriaObj);
        }
        
        Producto productoGuardado = productoRepository.save(producto);
        
        // Crear bodega por separado
        Bodega bodega = new Bodega();
        bodega.setProducto(productoGuardado);
        bodega.setExistencia(productoDTO.getExistencia() != null ? productoDTO.getExistencia() : 0);
        bodegaRepository.save(bodega);
        
        return productoGuardado;
    }

    @Transactional
    public Producto actualizarProducto(Integer id, ProductoDTO productoDTO) {
        Producto productoExistente = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        
        productoExistente.setCodigo(productoDTO.getCodigo());
        productoExistente.setProducto(productoDTO.getProducto());
        productoExistente.setPrecio(productoDTO.getPrecio());
        productoExistente.setDescuento(productoDTO.getDescuento());
        productoExistente.setDepartamento(productoDTO.getDepartamento());
        productoExistente.setCategoria(productoDTO.getCategoria());
        productoExistente.setImagenUrl(productoDTO.getImagenUrl());
        productoExistente.setCaracteristicas(productoDTO.getCaracteristicas());
        productoExistente.setCodigoBarras(productoDTO.getCodigoBarras());
        productoExistente.setNumeroSerie(productoDTO.getNumeroSerie());
        
        // Actualizar categoría
        if (productoDTO.getIdCategoria() != null) {
            categoriaRepository.findById(productoDTO.getIdCategoria())
                    .ifPresent(productoExistente::setCategoriaObj);
        } else {
            productoExistente.setCategoriaObj(null);
        }
        
        // Actualizar existencia en bodega
        if (productoDTO.getExistencia() != null) {
            bodegaRepository.findByProducto_IdProducto(id).ifPresent(bodega -> {
                bodega.setExistencia(productoDTO.getExistencia());
                bodegaRepository.save(bodega);
            });
        }
        
        return productoRepository.save(productoExistente);
    }

    @Transactional
    public void eliminarProducto(Integer id) {
        productoRepository.deleteById(id);
    }

    private ProductoDTO convertirADTO(Producto producto) {
        ProductoDTO dto = new ProductoDTO();
        dto.setIdProducto(producto.getIdProducto());
        dto.setCodigo(producto.getCodigo());
        dto.setProducto(producto.getProducto());
        dto.setPrecio(producto.getPrecio());
        dto.setDescuento(producto.getDescuento());
        dto.setDepartamento(producto.getDepartamento());
        dto.setCategoria(producto.getCategoria());
        dto.setImagenUrl(producto.getImagenUrl());
        dto.setCaracteristicas(producto.getCaracteristicas());
        dto.setCodigoBarras(producto.getCodigoBarras());
        dto.setNumeroSerie(producto.getNumeroSerie());
        
        // Cargar categoría si existe
        if (producto.getCategoriaObj() != null) {
            dto.setIdCategoria(producto.getCategoriaObj().getIdCategoria());
            dto.setCategoriaObj(producto.getCategoriaObj());
        }
        
        // Obtener existencia desde bodega
        Integer existencia = bodegaRepository.findByProducto_IdProducto(producto.getIdProducto())
                .map(Bodega::getExistencia)
                .orElse(0);
        dto.setExistencia(existencia);
        
        return dto;
    }
}
