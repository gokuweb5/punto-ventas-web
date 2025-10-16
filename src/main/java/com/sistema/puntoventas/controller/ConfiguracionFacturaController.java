package com.sistema.puntoventas.controller;

import com.sistema.puntoventas.entity.ConfiguracionFactura;
import com.sistema.puntoventas.service.ConfiguracionFacturaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/configuracion-facturas")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"}, allowCredentials = "true")
public class ConfiguracionFacturaController {

    private final ConfiguracionFacturaService configuracionFacturaService;

    @GetMapping("/actual")
    public ResponseEntity<ConfiguracionFactura> obtenerConfiguracionActual() {
        try {
            return ResponseEntity.ok(configuracionFacturaService.obtenerConfiguracionActiva());
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/estado")
    public ResponseEntity<Map<String, Object>> obtenerEstado() {
        Map<String, Object> estado = new HashMap<>();
        estado.put("necesitaNuevoRango", configuracionFacturaService.necesitaNuevoRango());
        estado.put("facturasRestantes", configuracionFacturaService.facturasRestantes());
        
        try {
            ConfiguracionFactura config = configuracionFacturaService.obtenerConfiguracionActiva();
            estado.put("numeroActual", config.getNumeroActual());
            estado.put("numeroFin", config.getNumeroFin());
        } catch (RuntimeException e) {
            estado.put("numeroActual", 0);
            estado.put("numeroFin", 0);
        }
        
        return ResponseEntity.ok(estado);
    }

    @PostMapping("/configurar")
    public ResponseEntity<ConfiguracionFactura> configurarNuevoRango(
            @RequestBody Map<String, Integer> request) {
        Integer inicio = request.get("inicio");
        Integer fin = request.get("fin");

        if (inicio == null || fin == null) {
            return ResponseEntity.badRequest().build();
        }

        if (inicio >= fin) {
            return ResponseEntity.badRequest().build();
        }

        ConfiguracionFactura config = configuracionFacturaService.configurarNuevoRango(inicio, fin);
        return ResponseEntity.status(HttpStatus.CREATED).body(config);
    }
}
