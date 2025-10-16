package com.sistema.puntoventas.service;

import com.sistema.puntoventas.dto.LoginRequest;
import com.sistema.puntoventas.dto.LoginResponse;
import com.sistema.puntoventas.entity.Usuario;
import com.sistema.puntoventas.repository.UsuarioRepository;
import com.sistema.puntoventas.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UsuarioRepository usuarioRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public LoginResponse login(LoginRequest request) {
        // Autenticar usuario
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getUsuario(), request.getPassword())
        );

        // Generar token
        String token = jwtUtil.generateToken(request.getUsuario());

        // Obtener información del usuario
        Usuario usuario = usuarioRepository.findByUsuario(request.getUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return new LoginResponse(
            token,
            "Bearer",
            usuario.getIdUsuario(),
            usuario.getUsuario(),
            usuario.getNombre(),
            usuario.getApellido(),
            usuario.getRole()
        );
    }

    public Usuario registrarUsuario(Usuario usuario) {
        // Encriptar contraseña
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        return usuarioRepository.save(usuario);
    }
}
