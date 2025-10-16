package com.sistema.puntoventas.repository;

import com.sistema.puntoventas.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer> {
    List<Cliente> findByNombreContainingIgnoreCase(String nombre);
    List<Cliente> findByCredito(Boolean credito);
}
