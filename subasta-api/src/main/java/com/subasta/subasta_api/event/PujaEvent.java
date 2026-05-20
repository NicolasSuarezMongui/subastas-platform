package com.subasta.subasta_api.event;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PujaEvent(
    Long pujaId,
    Long productoId,
    String nombreProducto,
    Long usuarioId,
    String username,
    BigDecimal monto,
    BigDecimal precioAnterior,
    LocalDateTime timestamp
) {}