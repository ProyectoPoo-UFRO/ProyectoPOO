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

    // Obtener todas las ventas
    @GetMapping
    public ResponseEntity<List<Venta>> obtenerVentas() {
        List<Venta> ventas = ventaService.obtenerVentas();
        if (ventas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(ventas);
    }

    // Obtener venta por ID
    @GetMapping("/{ventaId}")
    public ResponseEntity<Venta> obtenerVentaPorId(@PathVariable("ventaId") String ventaId) {
        return ventaService.obtenerVentaPorId(ventaId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Obtener ventas por máquina
    @GetMapping("/maquina/{maquinaId}")
    public ResponseEntity<List<Venta>> obtenerVentasPorMaquina(@PathVariable("maquinaId") String maquinaId) {
        List<Venta> ventas = ventaService.obtenerVentasPorMaquina(maquinaId);
        if (ventas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(ventas);
    }

    // Obtener ventas por usuario
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Venta>> obtenerVentasPorUsuario(@PathVariable("usuarioId") String usuarioId) {
        List<Venta> ventas = ventaService.obtenerVentasPorUsuario(usuarioId);
        if (ventas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(ventas);
    }

    // Crear venta
    @PostMapping
    public ResponseEntity<?> crearVenta(@RequestBody @Valid VentaRequest ventaRequest) {
        try {
            Venta ventaCreada = ventaService.agregarVenta(
                    ventaRequest.getIdMaquina(),
                    ventaRequest.getItems(),
                    ventaRequest.getIdUsuario()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(ventaCreada);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

}
