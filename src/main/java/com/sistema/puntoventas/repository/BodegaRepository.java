package com.sistema.puntoventas.repository;

import com.sistema.puntoventas.entity.Bodega;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BodegaRepository extends JpaRepository<Bodega, Integer> {
    Optional<Bodega> findByProducto_IdProducto(Integer idProducto);
}
