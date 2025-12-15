package com.sistema.puntoventas.service;

import com.sistema.puntoventas.dto.VentaRequest;
import com.sistema.puntoventas.entity.*;
import com.sistema.puntoventas.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sistema.puntoventas.dto.FacturaDTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

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

    @Transactional(readOnly = true)
    public List<FacturaDTO> obtenerFacturasAgrupadas() {
        List<Venta> todasLasVentas = ventaRepository.findAll();
        
        // Agrupar ventas por ticket
        Map<String, List<Venta>> ventasPorTicket = todasLasVentas.stream()
                .collect(Collectors.groupingBy(Venta::getTicket));
        
        return ventasPorTicket.entrySet().stream()
                .map(entry -> {
                    String ticket = entry.getKey();
                    List<Venta> ventas = entry.getValue();
                    Venta primeraVenta = ventas.get(0);
                    
                    // Calcular total
                    BigDecimal total = ventas.stream()
                            .map(Venta::getImporte)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    
                    // Obtener nombre del cliente
                    String clienteNombre = "Consumidor Final";
                    if (primeraVenta.getCliente() != null) {
                        clienteNombre = primeraVenta.getCliente().getNombre() + " " + 
                                       primeraVenta.getCliente().getApellido();
                    }
                    
                    // Crear items
                    List<FacturaDTO.ItemFactura> items = ventas.stream()
                            .map(v -> FacturaDTO.ItemFactura.builder()
                                    .codigo(v.getCodigo())
                                    .descripcion(v.getDescripcion())
                                    .cantidad(v.getCantidad())
                                    .precio(v.getPrecio())
                                    .importe(v.getImporte())
                                    .build())
                            .collect(Collectors.toList());
                    
                    return FacturaDTO.builder()
                            .ticket(ticket)
                            .fecha(primeraVenta.getFecha())
                            .cliente(clienteNombre)
                            .total(total)
                            .cantidadItems(ventas.size())
                            .credito(primeraVenta.getCredito())
                            .items(items)
                            .build();
                })
                .sorted((a, b) -> b.getFecha().compareTo(a.getFecha())) // Más recientes primero
                .collect(Collectors.toList());
    }
}
