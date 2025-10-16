package com.sistema.puntoventas.service;

import com.sistema.puntoventas.entity.ConfiguracionFactura;
import com.sistema.puntoventas.repository.ConfiguracionFacturaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ConfiguracionFacturaService {

    private final ConfiguracionFacturaRepository configuracionFacturaRepository;

    @Transactional(readOnly = true)
    public ConfiguracionFactura obtenerConfiguracionActiva() {
        return configuracionFacturaRepository.findFirstByActivoTrueOrderByIdDesc()
                .orElseThrow(() -> new RuntimeException("No hay configuración de facturas activa. Por favor configure un rango de facturas."));
    }

    @Transactional
    public String obtenerSiguienteNumeroFactura() {
        ConfiguracionFactura config = obtenerConfiguracionActiva();
        
        if (!config.tieneFacturasDisponibles()) {
            throw new RuntimeException("Se agotaron las facturas del rango actual. Por favor configure un nuevo rango.");
        }

        String numeroFactura = config.obtenerSiguienteNumeroFactura();
        config.incrementarNumero();
        configuracionFacturaRepository.save(config);

        return numeroFactura;
    }

    @Transactional
    public ConfiguracionFactura configurarNuevoRango(Integer inicio, Integer fin) {
        // Desactivar configuración anterior si existe
        configuracionFacturaRepository.findFirstByActivoTrueOrderByIdDesc()
                .ifPresent(config -> {
                    config.setActivo(false);
                    configuracionFacturaRepository.save(config);
                });

        // Crear nueva configuración
        ConfiguracionFactura nuevaConfig = new ConfiguracionFactura();
        nuevaConfig.setNumeroInicio(inicio);
        nuevaConfig.setNumeroFin(fin);
        nuevaConfig.setNumeroActual(inicio);
        nuevaConfig.setActivo(true);

        return configuracionFacturaRepository.save(nuevaConfig);
    }

    @Transactional(readOnly = true)
    public boolean necesitaNuevoRango() {
        try {
            ConfiguracionFactura config = obtenerConfiguracionActiva();
            return !config.tieneFacturasDisponibles();
        } catch (RuntimeException e) {
            return true; // No hay configuración activa
        }
    }

    @Transactional(readOnly = true)
    public int facturasRestantes() {
        try {
            ConfiguracionFactura config = obtenerConfiguracionActiva();
            return config.facturasRestantes();
        } catch (RuntimeException e) {
            return 0;
        }
    }
}
