package com.example.RESTAPIDB.Services;

import com.example.RESTAPIDB.Modelos.Sistema.Maquina;
import com.example.RESTAPIDB.Repository.MaquinaRepo;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class MaquinaService {

    private final MaquinaRepo maquinaRepo;

    public MaquinaService(MaquinaRepo maquinaRepo) {
        this.maquinaRepo = maquinaRepo;
    }

    public List<Maquina> obtenerMaquinas() {
        return maquinaRepo.findAll();
    }

    public Optional<Maquina> obtenerMaquina(String id) {
        return maquinaRepo.findById(id);
    }

    public Maquina crearMaquina(Maquina maquina) {
        return maquinaRepo.save(maquina);
    }

    public boolean cambiarStock(String maquinaId, String lataId, int stock) {
        return false;
    }

}