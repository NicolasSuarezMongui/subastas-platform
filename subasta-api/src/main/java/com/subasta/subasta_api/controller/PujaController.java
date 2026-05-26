package com.subasta.subasta_api.controller;

import com.subasta.subasta_api.dto.PujaRequest;
import com.subasta.subasta_api.dto.PujaResponse;
import com.subasta.subasta_api.service.PujaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/productos/{productoId}/pujas")
@RequiredArgsConstructor
public class PujaController {
    
    private final PujaService pujaService;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public List<PujaResponse> listar(@PathVariable Long productoId) {
        return pujaService.listarPujasPorProducto(productoId)
                .stream()
                .map(PujaResponse::from)
                .toList();
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<PujaResponse> pujar(
        @PathVariable Long productoId,
        @RequestBody PujaRequest request
    ){
        var puja = pujaService.realizarPuja(
            productoId,
            request.usuarioId(),
            request.monto()
        );

        return ResponseEntity.status(201).body(PujaResponse.from(puja));
    }

}
