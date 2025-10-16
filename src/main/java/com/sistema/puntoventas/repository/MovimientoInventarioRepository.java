package com.sistema.puntoventas.repository;

import com.sistema.puntoventas.entity.MovimientoInventario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MovimientoInventarioRepository extends JpaRepository<MovimientoInventario, Integer> {
    List<MovimientoInventario> findByProducto_IdProducto(Integer idProducto);
    List<MovimientoInventario> findByFechaBetween(LocalDateTime inicio, LocalDateTime fin);
    List<MovimientoInventario> findByTipoMovimiento(String tipoMovimiento);
}
