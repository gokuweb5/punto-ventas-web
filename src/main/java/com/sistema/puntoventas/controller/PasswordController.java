package com.sistema.puntoventas.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/password")
@RequiredArgsConstructor
public class PasswordController {

    private final PasswordEncoder passwordEncoder;

    @GetMapping("/encode/{password}")
    public String encodePassword(@PathVariable String password) {
        return passwordEncoder.encode(password);
    }
}
