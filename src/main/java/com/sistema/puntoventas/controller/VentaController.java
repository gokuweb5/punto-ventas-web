package com.sistema.puntoventas.controller;

import com.sistema.puntoventas.dto.VentaRequest;
import com.sistema.puntoventas.dto.VentaResponse;
import com.sistema.puntoventas.entity.Venta;
import com.sistema.puntoventas.service.VentaService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/ventas")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"}, allowCredentials = "true")
public class VentaController {

    private final VentaService ventaService;

    @GetMapping
    public ResponseEntity<List<Venta>> obtenerTodas() {
        return ResponseEntity.ok(ventaService.obtenerTodasLasVentas());
    }

    @GetMapping("/fecha")
    public ResponseEntity<List<Venta>> obtenerPorFecha(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {
        return ResponseEntity.ok(ventaService.obtenerVentasPorFecha(inicio, fin));
    }

    @PostMapping
    public ResponseEntity<VentaResponse> procesarVenta(
            @RequestBody VentaRequest request,
            Authentication authentication) {
        // En un escenario real, obtendrías el ID del usuario del token JWT
        // Por ahora, asumimos que está en el authentication
        Integer idUsuario = 1; // Esto debería venir del usuario autenticado
        
        String ticket = ventaService.procesarVenta(request, idUsuario);
        VentaResponse response = new VentaResponse(ticket, "Venta procesada exitosamente");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
