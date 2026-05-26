package com.subasta.subasta_api.controller;

import com.subasta.subasta_api.model.Producto;
import com.subasta.subasta_api.service.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
public class ProductoController {
    
    private final ProductoService productoService;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public List<Producto> listar() {
        return productoService.listarActivos();
    }

    @GetMapping("todos")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Producto> listarTodos() {
        return productoService.listarTodos();
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Producto> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(productoService.buscarPorId(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Producto> crear(@RequestBody Producto producto) {
        return ResponseEntity.status(201).body(productoService.crear(producto));
    }

}
