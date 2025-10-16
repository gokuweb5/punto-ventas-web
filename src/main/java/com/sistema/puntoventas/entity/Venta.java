package com.sistema.puntoventas.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "ventas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdVenta")
    private Integer idVenta;

    @Column(name = "Ticket", length = 50)
    private String ticket;

    @ManyToOne
    @JoinColumn(name = "IdProducto", nullable = false)
    private Producto producto;

    @Column(name = "Codigo", length = 50)
    private String codigo;

    @Column(name = "Descripcion", length = 255)
    private String descripcion;

    @Column(name = "Cantidad", nullable = false)
    private Integer cantidad;

    @Column(name = "Precio", nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    @Column(name = "Descuento", precision = 10, scale = 2)
    private BigDecimal descuento = BigDecimal.ZERO;

    @Column(name = "Importe", nullable = false, precision = 10, scale = 2)
    private BigDecimal importe;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "IdCliente")
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "IdUsuario")
    private Usuario usuario;

    @Column(name = "Credito")
    private Boolean credito = false;

    @CreatedDate
    @Column(name = "Fecha", updatable = false)
    private LocalDateTime fecha;
}
