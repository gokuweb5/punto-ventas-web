package com.sistema.puntoventas.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "clientes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "Nid", length = 50)
    private String nid;

    @Column(name = "Nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "Apellido", length = 100)
    private String apellido;

    @Column(name = "Direccion", length = 255)
    private String direccion;

    @Column(name = "Email", length = 100)
    private String email;

    @Column(name = "Telefono", length = 20)
    private String telefono;

    @CreatedDate
    @Column(name = "Fecha", updatable = false)
    private LocalDateTime fecha;

    @Column(name = "Credito")
    private Boolean credito = false;

    @Lob
    @Column(name = "Imagen", columnDefinition = "LONGBLOB")
    private byte[] imagen;
}
