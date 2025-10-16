package com.sistema.puntoventas.repository;

import com.sistema.puntoventas.entity.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Integer> {
    List<Venta> findByTicket(String ticket);
    List<Venta> findByClienteId(Integer clienteId);
    List<Venta> findByUsuarioIdUsuario(Integer usuarioId);
    
    @Query("SELECT v FROM Venta v WHERE v.fecha BETWEEN :inicio AND :fin")
    List<Venta> findByFechaBetween(@Param("inicio") LocalDateTime inicio, @Param("fin") LocalDateTime fin);
}
