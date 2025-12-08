package com.example.RESTAPIDB.controller;

import com.example.RESTAPIDB.dto.request.CrearMaquinaRequest;
import com.example.RESTAPIDB.dto.request.EstadoRequest;
import com.example.RESTAPIDB.dto.request.StockUpdateRequest;
import com.example.RESTAPIDB.dto.response.MaquinaResponse;
import com.example.RESTAPIDB.model.sistema.Maquina;
import com.example.RESTAPIDB.model.sistema.Producto;
import com.example.RESTAPIDB.services.MaquinaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/maquinas")
public class MaquinaController {

    private final MaquinaService maquinaService;

    public MaquinaController(MaquinaService maquinaService){
        this.maquinaService = maquinaService;
    }

    @PostMapping
    public ResponseEntity<MaquinaResponse> crearMaquina(@Valid @RequestBody CrearMaquinaRequest request) {
        MaquinaResponse response = maquinaService.crearMaquina(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<Maquina>> obtenerMaquinas() {
        List<Maquina> maquinas = maquinaService.obtenerMaquinas();
        return ResponseEntity.ok(maquinas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Maquina> obtenerMaquina(@PathVariable("id") String id) {
        return maquinaService.obtenerMaquina(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{maquinaId}/stock")
    public ResponseEntity<String> setStock(@PathVariable("maquinaId") String maquinaId, @Valid @RequestBody StockUpdateRequest request) {

        boolean actualizado = maquinaService.cambiarStock(maquinaId, request.getLataId(), request.getNewStock());

        if (actualizado) {
            return ResponseEntity.ok("Stock actualizado correctamente.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Máquina o producto no encontrado.");
        }
    }

    @PostMapping("/{maquinaId}/productos")
    public ResponseEntity<String> agregarProducto(@PathVariable("maquinaId") String maquinaId, @Valid @RequestBody Producto producto) {
        return maquinaService.agregarProducto(maquinaId, producto)
                .map(maquina -> ResponseEntity.ok("Producto agregado correctamente"))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Máquina o producto no encontrado"));
    }

    @DeleteMapping("/{maquinaId}/productos/{lataId}")
    public ResponseEntity<String> quitarProducto(@PathVariable("maquinaId") String maquinaId, @PathVariable("lataId") String lataId) {
        return maquinaService.quitarProducto(maquinaId, lataId)
                .map(maquina -> ResponseEntity.ok("Producto eliminado correctamente"))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Máquina o producto no encontrado"));
    }

    @PostMapping("/maquina/{id}/estado")
    public ResponseEntity<?> cambiarEstado(@PathVariable String id, @RequestBody EstadoRequest request) {
        boolean exito = maquinaService.cambiarEstado(id, request.getEstado());
        if (exito) {
            return ResponseEntity.ok("Estado actualizado a " + request.getEstado());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("No se pudo actualizar el estado.");
        }
    }

}