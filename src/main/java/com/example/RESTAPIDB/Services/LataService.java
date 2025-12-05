package com.example.RESTAPIDB.Services;

import com.example.RESTAPIDB.Modelos.Sistema.Lata;
import com.example.RESTAPIDB.Repository.LataRepo;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class LataService {

    private final LataRepo lataRepo;

    public LataService(LataRepo lataRepo) {
        this.lataRepo = lataRepo;
    }

    public List<Lata> obtenerLatas() {
        return lataRepo.findAll();
    }

    public Optional<Lata> obtenerLata(String id) {
        return lataRepo.findById(id);
    }

    public Lata crearLata(Lata lata) {
        return lataRepo.save(lata);
    }

    public boolean cambiarPrecio(String id, int precio) {
        Optional<Lata> l = lataRepo.findById(id);
        if (l.isPresent()) {
            Lata lata = l.get();
            lata.setPrecio(precio);
            lataRepo.save(lata);
            return true;
        }
        return false;
    }

}