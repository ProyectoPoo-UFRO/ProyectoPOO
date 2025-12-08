package com.example.RESTAPIDB.controller;

import com.example.RESTAPIDB.model.transaciones.Venta;
import com.example.RESTAPIDB.dto.request.VentaRequest;
import com.example.RESTAPIDB.services.VentaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/ventas")
public class VentaController {

    private final VentaService ventaService;

    public VentaController(VentaService ventaService) {
        this.ventaService = ventaService;
    }

    @GetMapping
    public ResponseEntity<List<Venta>> getAllVentas() {
        List<Venta> ventas = ventaService.obtenerVentas();
        if (ventas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(ventas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Venta> getVentaById(@PathVariable String id) {
        return ventaService.obtenerVentaPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/maquina/{maquinaId}")
    public ResponseEntity<List<Venta>> getVentasByMaquina(@PathVariable String maquinaId) {
        List<Venta> ventas = ventaService.obtenerVentasPorMaquina(maquinaId);
        if (ventas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(ventas);
    }

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<Venta>> getVentasByUsuario(@PathVariable String idUsuario) {
        List<Venta> ventas = ventaService.obtenerVentasPorUsuario(idUsuario);
        if (ventas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(ventas);
    }

    @PostMapping
    public ResponseEntity<?> createVenta(@RequestBody @Valid VentaRequest request) {
        try {
            Venta nuevaVenta = ventaService.agregarVenta(
                    request.getIdMaquina(),
                    request.getItems(),
                    request.getIdUsuario()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaVenta);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

}