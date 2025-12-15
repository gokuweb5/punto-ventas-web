package com.sistema.puntoventas.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FacturaDTO {
    private String ticket;
    private LocalDateTime fecha;
    private String cliente;
    private BigDecimal total;
    private Integer cantidadItems;
    private Boolean credito;
    private List<ItemFactura> items;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ItemFactura {
        private String codigo;
        private String descripcion;
        private Integer cantidad;
        private BigDecimal precio;
        private BigDecimal importe;
    }
}
