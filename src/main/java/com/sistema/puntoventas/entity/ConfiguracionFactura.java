package com.sistema.puntoventas.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "configuracion_facturas")
@Data
@EntityListeners(AuditingEntityListener.class)
public class ConfiguracionFactura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Integer id;

    @Column(name = "NumeroInicio", nullable = false)
    private Integer numeroInicio;

    @Column(name = "NumeroFin", nullable = false)
    private Integer numeroFin;

    @Column(name = "NumeroActual", nullable = false)
    private Integer numeroActual;

    @Column(name = "Activo")
    private Boolean activo = true;

    @CreatedDate
    @Column(name = "FechaCreacion", updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "FechaAgotamiento")
    private LocalDateTime fechaAgotamiento;

    public boolean tieneFacturasDisponibles() {
        return activo && numeroActual <= numeroFin;
    }

    public int facturasRestantes() {
        return numeroFin - numeroActual + 1;
    }

    public String obtenerSiguienteNumeroFactura() {
        if (!tieneFacturasDisponibles()) {
            throw new RuntimeException("No hay facturas disponibles en el rango actual");
        }
        return String.format("%08d", numeroActual);
    }

    public void incrementarNumero() {
        if (numeroActual < numeroFin) {
            numeroActual++;
        } else {
            activo = false;
            fechaAgotamiento = LocalDateTime.now();
        }
    }
}
