package com.sistema.puntoventas.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdUsuario")
    private Integer idUsuario;

    @Column(name = "Nid", length = 50)
    private String nid;

    @Column(name = "Nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "Apellido", length = 100)
    private String apellido;

    @Column(name = "Telefono", length = 20)
    private String telefono;

    @Column(name = "Direccion", length = 255)
    private String direccion;

    @Column(name = "Email", length = 100)
    private String email;

    @Column(name = "Usuario", nullable = false, unique = true, length = 50)
    private String usuario;

    @Column(name = "Password", nullable = false, length = 255)
    private String password;

    @Column(name = "Role", length = 50)
    private String role;

    @Lob
    @Column(name = "Imagen", columnDefinition = "LONGBLOB")
    private byte[] imagen;

    @Column(name = "Is_active")
    private Boolean isActive = true;

    @Column(name = "State")
    private Boolean state = true;

    @CreatedDate
    @Column(name = "Fecha", updatable = false)
    private LocalDateTime fecha;
}
