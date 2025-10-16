package com.sistema.puntoventas.repository;

import com.sistema.puntoventas.entity.ConfiguracionFactura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConfiguracionFacturaRepository extends JpaRepository<ConfiguracionFactura, Integer> {
    Optional<ConfiguracionFactura> findFirstByActivoTrueOrderByIdDesc();
}
