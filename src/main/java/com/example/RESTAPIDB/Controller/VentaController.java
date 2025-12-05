package com.example.RESTAPIDB.Controller;

import com.example.RESTAPIDB.Modelos.Transaciones.Venta;
import com.example.RESTAPIDB.Modelos.Transaciones.VentaRequest;
import com.example.RESTAPIDB.Services.VentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("api/v1/ventas")
public class VentaController {

    @Autowired
    private VentaService ventaService;

    @GetMapping
    public ResponseEntity<List<Venta>> getAllVentas() {
        return ResponseEntity.ok(ventaService.obtenerVentas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Venta> getOneVentaById(@PathVariable String id) {
        return ventaService.obtenerVentaPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/maquina/{maquinaId}")
    public ResponseEntity<List<Venta>> getVentasByMaquinaId(@PathVariable String maquinaId) {
        List<Venta> ventas = ventaService.obtenerVentasPorMaquina(maquinaId);
        if (ventas.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(ventas);
    }

    @PostMapping("/add")
    public ResponseEntity<Venta> addVenta(@RequestBody VentaRequest request) {
        Venta nuevaVenta = ventaService.agregarVenta(
                request.getIdMaquina(),
                request.getItems(),
                request.getIdUsuario()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaVenta);
    }

}