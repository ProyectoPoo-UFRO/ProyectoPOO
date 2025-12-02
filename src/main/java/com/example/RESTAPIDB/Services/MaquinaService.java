package com.example.RESTAPIDB.Services;


import com.example.RESTAPIDB.Modelos.Sistema.Maquina;
import com.example.RESTAPIDB.Repository.MaquinaRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MaquinaService {

    @Autowired
    private MaquinaRepo maquinaRepo;

    public List<Maquina> allMaquinas(){
        return maquinaRepo.findAll();
    }

    public Optional<Maquina> oneMaquina(String id){
        return maquinaRepo.findById(id);
    }

    public void addMaquina(Maquina maquina){
        maquinaRepo.save(maquina);
    }

    //TODO reponer Stock
    //TODO comprar lata en Maquina X
}
