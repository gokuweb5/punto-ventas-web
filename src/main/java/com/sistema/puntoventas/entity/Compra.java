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
@Table(name = "compras")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Compra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdCompra")
    private Integer idCompra;

    @Column(name = "Ticket", length = 50)
    private String ticket;

    @Column(name = "Descripcion", length = 255)
    private String descripcion;

    @Column(name = "Cantidad", nullable = false)
    private Integer cantidad;

    @Column(name = "Precio", nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    @Column(name = "Importe", nullable = false, precision = 10, scale = 2)
    private BigDecimal importe;

    @ManyToOne
    @JoinColumn(name = "IdProveedor")
    private Proveedor proveedor;

    @ManyToOne
    @JoinColumn(name = "IdUsuario")
    private Usuario usuario;

    @Column(name = "Credito")
    private Boolean credito = false;

    @CreatedDate
    @Column(name = "Fecha", updatable = false)
    private LocalDateTime fecha;
}
