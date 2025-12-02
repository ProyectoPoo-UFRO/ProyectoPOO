package com.example.RESTAPIDB.Controller;

import com.example.RESTAPIDB.Modelos.Sistema.Lata;
import com.example.RESTAPIDB.Services.LataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/latas")
public class LataController {
    @Autowired
    private LataService lataService;

    @GetMapping
    public ResponseEntity<List<Lata>> getAllLatas() {
        List<Lata> latas = lataService.allLatas();
        return new ResponseEntity<>(latas, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Lata>> getOneMaquina(@PathVariable String id) {
        return  new ResponseEntity<Optional<Lata>>(lataService.oneMaquina(id),HttpStatus.OK);
    }
}
