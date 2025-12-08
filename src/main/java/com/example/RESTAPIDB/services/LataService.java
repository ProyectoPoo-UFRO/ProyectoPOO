package com.example.RESTAPIDB.services;

import com.example.RESTAPIDB.dto.request.CrearLataRequest;
import com.example.RESTAPIDB.dto.response.LataResponse;
import com.example.RESTAPIDB.model.sistema.Lata;
import com.example.RESTAPIDB.repo.LataRepo;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class LataService {

    private final String url = "images/latas/default.png";

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

    public LataResponse crearLata(CrearLataRequest request) {
        Lata lata = new Lata();
        lata.setNombre(request.getNombre());
        lata.setPrecio(request.getPrecio());
        lata.setImagen(url);

        Lata l = lataRepo.save(lata);

        return new LataResponse(l.getId());
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