package com.example.RESTAPIDB.Controller;

import com.example.RESTAPIDB.Modelos.Sistema.Lata;
import com.example.RESTAPIDB.Services.LataService;
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
    public ResponseEntity<Lata> crearLata(@RequestBody Lata lata){
        Lata l = lataService.crearLata(lata);
        return ResponseEntity.status(HttpStatus.CREATED).body(l);
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
    public ResponseEntity<Lata> obtenerLata(@PathVariable String id) {
        return lataService.obtenerLata(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound()
                        .build());
    }

    @PatchMapping("/{id}/precio")
    public ResponseEntity<Void> actualizarPrecioLata(@PathVariable String id, @RequestBody int nuevoPrecio){
        boolean actualizado = lataService.cambiarPrecio(id, nuevoPrecio);

        if (!actualizado) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }


}