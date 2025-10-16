package com.sistema.puntoventas.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * Entidad para registrar movimientos de inventario
 * Permite llevar un historial de entradas y salidas de productos
 */
@Entity
@Table(name = "movimientos_inventario")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class MovimientoInventario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdMovimiento")
    private Integer idMovimiento;

    @ManyToOne
    @JoinColumn(name = "IdProducto", nullable = false)
    private Producto producto;

    @Column(name = "TipoMovimiento", nullable = false, length = 20)
    private String tipoMovimiento; // ENTRADA, SALIDA, AJUSTE, VENTA, DEVOLUCION

    @Column(name = "Cantidad", nullable = false)
    private Integer cantidad;

    @Column(name = "CantidadAnterior", nullable = false)
    private Integer cantidadAnterior;

    @Column(name = "CantidadNueva", nullable = false)
    private Integer cantidadNueva;

    @Column(name = "Motivo", length = 255)
    private String motivo;

    @Column(name = "Referencia", length = 100)
    private String referencia; // Número de ticket, orden de compra, etc.

    @ManyToOne
    @JoinColumn(name = "IdUsuario")
    private Usuario usuario;

    @CreatedDate
    @Column(name = "Fecha", updatable = false)
    private LocalDateTime fecha;
}
