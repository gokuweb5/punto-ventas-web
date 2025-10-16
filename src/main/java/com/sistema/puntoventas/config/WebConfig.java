package com.sistema.puntoventas.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(@NonNull ResourceHandlerRegistry registry) {
        // Obtener ruta absoluta del directorio uploads
        File uploadDir = new File("uploads/productos");
        String uploadPath = "file:///" + uploadDir.getAbsolutePath().replace("\\", "/") + "/";
        
        System.out.println("Sirviendo imágenes desde: " + uploadPath);
        
        // Servir imágenes desde el directorio uploads
        registry.addResourceHandler("/imagenes/**")
                .addResourceLocations(uploadPath);
    }
}
