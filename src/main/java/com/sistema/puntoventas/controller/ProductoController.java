package com.sistema.puntoventas.controller;

import com.sistema.puntoventas.dto.ProductoDTO;
import com.sistema.puntoventas.entity.Producto;
import com.sistema.puntoventas.service.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/productos")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"}, allowCredentials = "true")
public class ProductoController {

    private final ProductoService productoService;

    @GetMapping
    public ResponseEntity<List<ProductoDTO>> obtenerTodos() {
        return ResponseEntity.ok(productoService.obtenerTodosLosProductos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoDTO> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(productoService.obtenerProductoPorId(id));
    }

    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<ProductoDTO> obtenerPorCodigo(@PathVariable String codigo) {
        return ResponseEntity.ok(productoService.obtenerProductoPorCodigo(codigo));
    }

    @PostMapping
    public ResponseEntity<Producto> crear(@RequestBody ProductoDTO productoDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(productoService.crearProducto(productoDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizar(@PathVariable Integer id, @RequestBody ProductoDTO productoDTO) {
        return ResponseEntity.ok(productoService.actualizarProducto(id, productoDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        productoService.eliminarProducto(id);
        return ResponseEntity.noContent().build();
    }
}
