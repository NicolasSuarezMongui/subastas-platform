package com.subasta.subasta_api.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "productos")
@Data
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "precio_base", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioBase;

    @Column(name = "precio_actual", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioActual;

    @Enumerated(EnumType.STRING)
    private EstadoProducto estado = EstadoProducto.ACTIVO;

    @Column(name = "fecha_cierre")
    private LocalDateTime fechaCierre;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    public enum EstadoProducto {
        ACTIVO, CERRADO, CANCELADO
    }
}