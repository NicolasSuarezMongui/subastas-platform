package com.subasta.subasta_api.dto;

import com.subasta.subasta_api.model.Puja;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PujaResponse(
    Long id,
    Long productoId,
    String nombreProducto,
    Long usuarioId,
    String username,
    BigDecimal monto,
    LocalDateTime createdAt
) {
    public static PujaResponse from(Puja puja) {
        return new PujaResponse(
            puja.getId(),
            puja.getProducto().getId(),
            puja.getProducto().getNombre(),
            puja.getUsuario().getId(),
            puja.getUsuario().getUsername(),
            puja.getMonto(),
            puja.getCreatedAt()
        );
    }
}
