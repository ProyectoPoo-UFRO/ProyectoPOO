package com.example.RESTAPIDB.Controller;

import com.example.RESTAPIDB.Modelos.Sistema.Maquina;
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

    @GetMapping("/{id}/setStock/{newStock}")
    public void setStock(@PathVariable String id,@PathVariable int newStock){

    }

}