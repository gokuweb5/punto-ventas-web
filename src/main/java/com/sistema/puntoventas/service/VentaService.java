package com.sistema.puntoventas.service;

import com.sistema.puntoventas.dto.VentaRequest;
import com.sistema.puntoventas.entity.*;
import com.sistema.puntoventas.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VentaService {

    private final VentaRepository ventaRepository;
    private final ProductoRepository productoRepository;
    private final ClienteRepository clienteRepository;
    private final UsuarioRepository usuarioRepository;
    private final BodegaRepository bodegaRepository;
    private final InventarioService inventarioService;

    @Transactional(readOnly = true)
    public List<Venta> obtenerTodasLasVentas() {
        return ventaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Venta> obtenerVentasPorFecha(LocalDateTime inicio, LocalDateTime fin) {
        return ventaRepository.findByFechaBetween(inicio, fin);
    }

    @Transactional
    public String procesarVenta(VentaRequest request, Integer idUsuario) {
        String ticket = generarTicket();
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Log para debug
        System.out.println("DEBUG - ID Cliente recibido: " + request.getIdCliente());
        
        Cliente cliente = null;
        if (request.getIdCliente() != null) {
            cliente = clienteRepository.findById(request.getIdCliente())
                    .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
            System.out.println("DEBUG - Cliente encontrado: " + cliente.getNombre() + " " + cliente.getApellido());
        } else {
            System.out.println("DEBUG - No se recibió ID de cliente");
        }

        List<Venta> ventas = new java.util.ArrayList<>();

        for (VentaRequest.VentaItemRequest item : request.getItems()) {
            Producto producto = productoRepository.findById(item.getIdProducto())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

            // Verificar existencia en bodega
            Bodega bodega = bodegaRepository.findByProducto_IdProducto(producto.getIdProducto())
                    .orElseThrow(() -> new RuntimeException("No hay registro de inventario para: " + producto.getProducto()));
            
            if (bodega.getExistencia() < item.getCantidad()) {
                throw new RuntimeException("Stock insuficiente para el producto: " + producto.getProducto());
            }

            // Calcular importe
            BigDecimal descuento = item.getDescuento() != null ? item.getDescuento() : BigDecimal.ZERO;
            BigDecimal precioConDescuento = item.getPrecio().subtract(descuento);
            BigDecimal importe = precioConDescuento.multiply(BigDecimal.valueOf(item.getCantidad()));

            // Crear venta
            Venta venta = new Venta();
            venta.setTicket(ticket);
            venta.setProducto(producto);
            venta.setCodigo(item.getCodigo());
            venta.setDescripcion(producto.getProducto());
            venta.setCantidad(item.getCantidad());
            venta.setPrecio(item.getPrecio());
            venta.setDescuento(descuento);
            venta.setImporte(importe);
            venta.setCliente(cliente);
            venta.setUsuario(usuario);
            venta.setCredito(request.getCredito());

            ventas.add(ventaRepository.save(venta));

            // Actualizar inventario usando el servicio de inventario
            inventarioService.registrarSalida(
                producto.getIdProducto(),
                item.getCantidad(),
                "Venta",
                ticket,
                idUsuario
            );
        }

        // Devolver el ticket generado
        return ticket;
    }

    private String generarTicket() {
        return "TKT-" + System.currentTimeMillis() + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
