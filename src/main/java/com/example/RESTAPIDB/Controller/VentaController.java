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

    @GetMapping("/{id}")
    public ResponseEntity<Venta> getOneVentaById(@PathVariable String id) {
        return ventaService.obtenerVentaPorId(id).map(v -> new ResponseEntity<>(v, HttpStatus.OK)).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/maquina/{maquinaId}")
    public ResponseEntity<List<Venta>> getVentasByMaquinaId(@PathVariable String maquinaId) {
        List<Venta> ventas = ventaService.obtenerVentasPorMaquina(maquinaId);
        return new ResponseEntity<>(ventas, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<Venta> addVenta(@RequestBody VentaRequest request) {

        Venta nuevaVenta = ventaService.agregarVenta(
                request.getIdMaquina(),
                request.getItems(),
                request.getIdUsuario()
        );

        return new ResponseEntity<>(nuevaVenta, HttpStatus.CREATED);
    }

}