package com.example.RESTAPIDB.Controller;


import com.example.RESTAPIDB.Modelos.Sistema.Maquina;
import com.example.RESTAPIDB.Services.MaquinaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/maquinas")
public class MaquinaController {

    @Autowired
    private MaquinaService maquinaService;

    @GetMapping
    public ResponseEntity<List<Maquina>> getAllMaquinas() {
        List<Maquina> maquinas = maquinaService.allMaquinas();
        return new ResponseEntity<>(maquinas, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Maquina>> getOneMaquina(@PathVariable String id) {
        return  new ResponseEntity<Optional<Maquina>>(maquinaService.oneMaquina(id),HttpStatus.OK);
    }

    @PostMapping("/addMaquina")
    public void addMaquina(@RequestBody Maquina maquina){
        maquinaService.addMaquina(maquina);
    }
}
