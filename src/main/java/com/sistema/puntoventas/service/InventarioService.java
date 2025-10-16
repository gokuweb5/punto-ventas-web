package com.sistema.puntoventas.service;

import com.sistema.puntoventas.entity.Bodega;
import com.sistema.puntoventas.entity.MovimientoInventario;
import com.sistema.puntoventas.entity.Producto;
import com.sistema.puntoventas.repository.BodegaRepository;
import com.sistema.puntoventas.repository.MovimientoInventarioRepository;
import com.sistema.puntoventas.repository.ProductoRepository;
import com.sistema.puntoventas.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Servicio para gestionar el inventario y sus movimientos
 */
@Service
@RequiredArgsConstructor
public class InventarioService {

    private final BodegaRepository bodegaRepository;
    private final MovimientoInventarioRepository movimientoRepository;
    private final ProductoRepository productoRepository;
    private final UsuarioRepository usuarioRepository;

    /**
     * Registra una entrada de inventario (compra, devolución, ajuste positivo)
     */
    @Transactional
    public Bodega registrarEntrada(Integer idProducto, Integer cantidad, String motivo, 
                                   String referencia, Integer idUsuario) {
        Producto producto = productoRepository.findById(idProducto)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        Bodega bodega = bodegaRepository.findByProducto_IdProducto(idProducto)
                .orElseThrow(() -> new RuntimeException("No existe registro de bodega para este producto"));

        Integer cantidadAnterior = bodega.getExistencia();
        Integer cantidadNueva = cantidadAnterior + cantidad;
        bodega.setExistencia(cantidadNueva);

        // Registrar movimiento
        registrarMovimiento(producto, "ENTRADA", cantidad, cantidadAnterior, 
                          cantidadNueva, motivo, referencia, idUsuario);

        return bodegaRepository.save(bodega);
    }

    /**
     * Registra una salida de inventario (venta, merma, ajuste negativo)
     */
    @Transactional
    public Bodega registrarSalida(Integer idProducto, Integer cantidad, String motivo, 
                                  String referencia, Integer idUsuario) {
        Producto producto = productoRepository.findById(idProducto)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        Bodega bodega = bodegaRepository.findByProducto_IdProducto(idProducto)
                .orElseThrow(() -> new RuntimeException("No existe registro de bodega para este producto"));

        Integer cantidadAnterior = bodega.getExistencia();
        
        if (cantidadAnterior < cantidad) {
            throw new RuntimeException("Stock insuficiente. Disponible: " + cantidadAnterior);
        }

        Integer cantidadNueva = cantidadAnterior - cantidad;
        bodega.setExistencia(cantidadNueva);

        // Registrar movimiento
        registrarMovimiento(producto, "SALIDA", cantidad, cantidadAnterior, 
                          cantidadNueva, motivo, referencia, idUsuario);

        return bodegaRepository.save(bodega);
    }

    /**
     * Ajusta el inventario a una cantidad específica
     */
    @Transactional
    public Bodega ajustarInventario(Integer idProducto, Integer nuevaCantidad, 
                                    String motivo, Integer idUsuario) {
        Producto producto = productoRepository.findById(idProducto)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        Bodega bodega = bodegaRepository.findByProducto_IdProducto(idProducto)
                .orElseThrow(() -> new RuntimeException("No existe registro de bodega para este producto"));

        Integer cantidadAnterior = bodega.getExistencia();
        Integer diferencia = nuevaCantidad - cantidadAnterior;
        
        bodega.setExistencia(nuevaCantidad);

        // Registrar movimiento
        String tipoMovimiento = diferencia >= 0 ? "AJUSTE_ENTRADA" : "AJUSTE_SALIDA";
        registrarMovimiento(producto, tipoMovimiento, Math.abs(diferencia), 
                          cantidadAnterior, nuevaCantidad, motivo, "AJUSTE", idUsuario);

        return bodegaRepository.save(bodega);
    }

    /**
     * Obtiene el historial de movimientos de un producto
     */
    @Transactional(readOnly = true)
    public List<MovimientoInventario> obtenerHistorialProducto(Integer idProducto) {
        return movimientoRepository.findByProducto_IdProducto(idProducto);
    }

    /**
     * Obtiene movimientos por rango de fechas
     */
    @Transactional(readOnly = true)
    public List<MovimientoInventario> obtenerMovimientosPorFecha(LocalDateTime inicio, LocalDateTime fin) {
        return movimientoRepository.findByFechaBetween(inicio, fin);
    }

    /**
     * Obtiene la existencia actual de un producto
     */
    @Transactional(readOnly = true)
    public Integer obtenerExistencia(Integer idProducto) {
        return bodegaRepository.findByProducto_IdProducto(idProducto)
                .map(Bodega::getExistencia)
                .orElse(0);
    }

    /**
     * Método privado para registrar movimientos de inventario
     */
    private void registrarMovimiento(Producto producto, String tipoMovimiento, Integer cantidad,
                                    Integer cantidadAnterior, Integer cantidadNueva,
                                    String motivo, String referencia, Integer idUsuario) {
        MovimientoInventario movimiento = new MovimientoInventario();
        movimiento.setProducto(producto);
        movimiento.setTipoMovimiento(tipoMovimiento);
        movimiento.setCantidad(cantidad);
        movimiento.setCantidadAnterior(cantidadAnterior);
        movimiento.setCantidadNueva(cantidadNueva);
        movimiento.setMotivo(motivo);
        movimiento.setReferencia(referencia);

        if (idUsuario != null) {
            usuarioRepository.findById(idUsuario).ifPresent(movimiento::setUsuario);
        }

        movimientoRepository.save(movimiento);
    }
}
