package com.sistema.puntoventas.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "productos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdProducto")
    private Integer idProducto;

    @Column(name = "Codigo", unique = true, nullable = false, length = 50)
    private String codigo;

    @Column(name = "Producto", nullable = false, length = 150)
    private String producto;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "IdCategoria")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Categoria categoriaObj;

    @Column(name = "Precio", nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    @Column(name = "Descuento", precision = 10, scale = 2)
    private BigDecimal descuento = BigDecimal.ZERO;

    @Column(name = "ImagenUrl", length = 500)
    private String imagenUrl;

    @Lob
    @Column(name = "Caracteristicas", columnDefinition = "TEXT")
    private String caracteristicas;

    @Column(name = "CodigoBarras", length = 100)
    private String codigoBarras;

    @Column(name = "NumeroSerie", length = 100)
    private String numeroSerie;

    @Column(name = "Departamento", length = 100)
    private String departamento;

    @Column(name = "Categoria", length = 100)
    private String categoria;

    @Column(name = "IdCompra")
    private Integer idCompra;

    @Lob
    @Column(name = "Imagen", columnDefinition = "LONGBLOB")
    private byte[] imagen;

    @CreatedDate
    @Column(name = "Fecha", updatable = false)
    private LocalDateTime fecha;

    // Relación con bodega manejada desde Bodega entity (unidireccional)
    // Para obtener existencia, usar BodegaRepository.findByProducto_IdProducto()
}
