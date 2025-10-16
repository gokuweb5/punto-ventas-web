package com.sistema.puntoventas.security;

import com.sistema.puntoventas.entity.Usuario;
import com.sistema.puntoventas.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByUsuario(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        return User.builder()
                .username(usuario.getUsuario())
                .password(usuario.getPassword())
                .authorities(Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + usuario.getRole())))
                .accountExpired(false)
                .accountLocked(!usuario.getIsActive())
                .credentialsExpired(false)
                .disabled(!usuario.getState())
                .build();
    }
}
