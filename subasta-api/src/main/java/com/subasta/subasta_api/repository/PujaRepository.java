package com.subasta.subasta_api.repository;

import com.subasta.subasta_api.model.Puja;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface PujaRepository extends JpaRepository<Puja, Long> {
    List<Puja> findByProductoIdOrderByMontoDesc(Long productoId);

    @Query("SELECT p FROM Puja p WHERE p.producto.id = :productoId ORDER BY p.monto DESC LIMIT 1")
    Optional<Puja> findPujaMaximaByProductoId(Long productoId);
}
