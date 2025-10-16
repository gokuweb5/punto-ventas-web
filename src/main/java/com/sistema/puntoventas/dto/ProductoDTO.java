package com.sistema.puntoventas.dto;

import com.sistema.puntoventas.entity.Categoria;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProductoDTO {
    private Integer idProducto;
    private String codigo;
    private String producto;
    private BigDecimal precio;
    private BigDecimal descuento;
    private String departamento;
    private String categoria;
    private Integer idCategoria;
    private Integer existencia;
    private String imagenUrl;
    private String caracteristicas;
    private String codigoBarras;
    private String numeroSerie;
    private Categoria categoriaObj;
}
