package com.sistema.puntoventas.repository;

import com.sistema.puntoventas.entity.Compra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompraRepository extends JpaRepository<Compra, Integer> {
    List<Compra> findByProveedorId(Integer proveedorId);
}
