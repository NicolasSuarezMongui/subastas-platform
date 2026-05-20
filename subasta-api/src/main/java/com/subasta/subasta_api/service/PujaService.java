package com.subasta.subasta_api.service;

import com.subasta.subasta_api.model.Producto;
import com.subasta.subasta_api.model.Puja;
import com.subasta.subasta_api.model.Usuario;
import com.subasta.subasta_api.repository.PujaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PujaService {
    
    private final PujaRepository pujaRepository;
    private final ProductoService productoService;
    private final UsuarioService usuarioService;

    @Transactional
    public Puja realizarPuja(Long productoId, Long usuarioId, java.math.BigDecimal monto) {

        Producto producto = productoService.buscarPorId(productoId);
        Usuario usuario = usuarioService.buscarPorId(usuarioId);

        // Regla 1: el producto debe estar activo
        if (producto.getEstado() != Producto.EstadoProducto.ACTIVO) {
            throw new RuntimeException("El producto no está disponible para pujas.");
        }

        // Regla 2: la puja debe ser mayor al precio actual
        if (monto.compareTo(producto.getPrecioActual()) <= 0) {
            throw new RuntimeException(
                "La puja debe ser mayor al precio actual: " + producto.getPrecioActual()
            );
        }

        // Actualizar el precio actual del producto
        producto.setPrecioActual(monto);

        // Registrar la puja
        Puja puja = new Puja();
        puja.setProducto(producto);
        puja.setUsuario(usuario);
        puja.setMonto(monto);

        return pujaRepository.save(puja);
    }

    public List<Puja> listarPujasPorProducto(Long productoId) {
        return pujaRepository.findByProductoIdOrderByMontoDesc(productoId);
    }
}
