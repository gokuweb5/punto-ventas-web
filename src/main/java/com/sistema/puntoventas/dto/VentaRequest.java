package com.sistema.puntoventas.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class VentaRequest {
    private Integer idCliente;
    private Boolean credito;
    private List<VentaItemRequest> items;
    
    @Data
    public static class VentaItemRequest {
        private Integer idProducto;
        private String codigo;
        private Integer cantidad;
        private BigDecimal precio;
        private BigDecimal descuento;
    }
}
