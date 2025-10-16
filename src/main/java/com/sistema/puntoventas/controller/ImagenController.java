package com.sistema.puntoventas.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/imagenes")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"}, allowCredentials = "true")
public class ImagenController {

    // Directorio donde se guardarán las imágenes
    private static final String UPLOAD_DIR = "uploads/productos/";

    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> uploadImagen(
            @RequestParam("file") MultipartFile file,
            @RequestParam("codigo") String codigo) {
        
        Map<String, String> response = new HashMap<>();
        
        try {
            // Validar que el archivo no esté vacío
            if (file.isEmpty()) {
                response.put("error", "El archivo está vacío");
                return ResponseEntity.badRequest().body(response);
            }

            // Validar que sea una imagen
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                response.put("error", "El archivo debe ser una imagen");
                return ResponseEntity.badRequest().body(response);
            }

            // Crear directorio si no existe
            File uploadDir = new File(UPLOAD_DIR);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            // Nombre del archivo: {codigo}.png
            String fileName = codigo + ".png";
            Path filePath = Paths.get(UPLOAD_DIR + fileName);

            // Guardar archivo
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // URL de la imagen
            String imageUrl = "/imagenes/" + fileName;
            
            response.put("success", "Imagen subida exitosamente");
            response.put("imageUrl", imageUrl);
            response.put("fileName", fileName);
            
            return ResponseEntity.ok(response);
            
        } catch (IOException e) {
            response.put("error", "Error al subir la imagen: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/{fileName}")
    public ResponseEntity<byte[]> getImagen(@PathVariable String fileName) {
        try {
            Path filePath = Paths.get(UPLOAD_DIR + fileName);
            
            if (!Files.exists(filePath)) {
                return ResponseEntity.notFound().build();
            }
            
            byte[] imageBytes = Files.readAllBytes(filePath);
            
            return ResponseEntity.ok()
                    .header("Content-Type", "image/png")
                    .body(imageBytes);
                    
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{codigo}")
    public ResponseEntity<Map<String, String>> deleteImagen(@PathVariable String codigo) {
        Map<String, String> response = new HashMap<>();
        
        try {
            String fileName = codigo + ".png";
            Path filePath = Paths.get(UPLOAD_DIR + fileName);
            
            if (Files.exists(filePath)) {
                Files.delete(filePath);
                response.put("success", "Imagen eliminada exitosamente");
                return ResponseEntity.ok(response);
            } else {
                response.put("error", "Imagen no encontrada");
                return ResponseEntity.notFound().build();
            }
            
        } catch (IOException e) {
            response.put("error", "Error al eliminar la imagen: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
