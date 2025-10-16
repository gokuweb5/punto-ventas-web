package com.sistema.puntoventas.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private String tipo;
    private Integer idUsuario;
    private String usuario;
    private String nombre;
    private String apellido;
    private String role;
}
