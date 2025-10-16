package com.sistema.puntoventas.repository;

import com.sistema.puntoventas.entity.Caja;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CajaRepository extends JpaRepository<Caja, Integer> {
    Optional<Caja> findByEstado(Boolean estado);
}
