package com.subasta.subasta_api.dto;

import java.math.BigDecimal;

public record PujaRequest(
    Long usuarioId,
    BigDecimal monto
) {}
