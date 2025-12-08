package com.example.RESTAPIDB.controller;

import com.example.RESTAPIDB.dto.request.CrearLataRequest;
import com.example.RESTAPIDB.dto.response.LataResponse;
import com.example.RESTAPIDB.model.sistema.Lata;
import com.example.RESTAPIDB.services.LataService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/latas")
public class LataController {

    private final LataService lataService;

    public LataController(LataService lataService) {
        this.lataService = lataService;
    }

    @PostMapping
    public ResponseEntity<LataResponse> crearLata(@Valid @RequestBody CrearLataRequest request) {
        LataResponse response = lataService.crearLata(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<Lata>> obtenerLatas() {
        List<Lata> latas = lataService.obtenerLatas();
        if (latas.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(latas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Lata> obtenerLata(@PathVariable("id") String id) {
        return lataService.obtenerLata(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound()
                        .build());
    }

    @PatchMapping("/{id}/precio")
    public ResponseEntity<Void> actualizarPrecioLata(@PathVariable("id") String id, @RequestBody int nuevoPrecio){
        boolean actualizado = lataService.cambiarPrecio(id, nuevoPrecio);
        if (!actualizado) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

}