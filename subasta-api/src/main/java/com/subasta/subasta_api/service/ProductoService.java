package com.subasta.subasta_api.service;

import com.subasta.subasta_api.model.Producto;
import com.subasta.subasta_api.model.Producto.EstadoProducto;
import com.subasta.subasta_api.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductoService {
    
    private final ProductoRepository productoRepository;

    public List<Producto> listarActivos() {
        return productoRepository.findByEstado(EstadoProducto.ACTIVO);
    }

    public List<Producto> listarTodos() {
        return productoRepository.findAll();
    }

    public Producto buscarPorId(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con id: " + id));
    }

    public Producto crear(Producto producto) {
        producto.setPrecioActual(producto.getPrecioBase());
        return productoRepository.save(producto);
    }
}
