package com.example.RESTAPIDB.Controller;

import com.example.RESTAPIDB.Controller.DTO.StockUpdateRequest;
import com.example.RESTAPIDB.Modelos.Sistema.Maquina;
import com.example.RESTAPIDB.Modelos.Sistema.Producto;
import com.example.RESTAPIDB.Services.MaquinaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/maquinas")
public class MaquinaController {

    private final MaquinaService maquinaService;

    public MaquinaController(MaquinaService maquinaService){
        this.maquinaService=maquinaService;
    }

    @PostMapping
    public ResponseEntity<Maquina> crearMaquina(@RequestBody Maquina maquina){
        Maquina m = maquinaService.crearMaquina(maquina);
        return ResponseEntity.status(HttpStatus.CREATED).body(m);
    }

    @GetMapping
    public ResponseEntity<List<Maquina>> obtenerMaquinas() {
        List<Maquina> maquinas = maquinaService.obtenerMaquinas();
        if (maquinas.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(maquinas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Maquina> obtenerMaquina(@PathVariable String id) {
        return maquinaService.obtenerMaquina(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{maquinaId}/stock")
    public ResponseEntity<String> setStock(@PathVariable String maquinaId, @RequestBody StockUpdateRequest request) {
        boolean actualizado = maquinaService.cambiarStock(maquinaId, request.getLataId(), request.getNewStock());

        if (actualizado) {
            return ResponseEntity.ok("Stock actualizado correctamente.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Máquina o producto no encontrado.");
        }
    }

    @PostMapping("/{maquinaId}/productos")
    public ResponseEntity<String> agregarProducto(@PathVariable String maquinaId, @RequestBody Producto producto) {

        return maquinaService.agregarProducto(maquinaId, producto)
                .map(maquina -> ResponseEntity.ok("Correctamente"))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Maquina o Lata no encontrada"));
    }

    @DeleteMapping("/{maquinaId}/productos/{lataId}")
    public ResponseEntity<String> quitarProducto(@PathVariable String maquinaId, @PathVariable String lataId) {

        return maquinaService.quitarProducto(maquinaId, lataId)
                .map(maquina -> ResponseEntity.ok("Correcto"))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Maquina o producto no encontrado"));
    }

}