package com.subasta.subasta_api.repository;

import com.subasta.subasta_api.model.Producto;
import com.subasta.subasta_api.model.Producto.EstadoProducto;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
    List<Producto> findByEstado(EstadoProducto estadoProducto);
}
