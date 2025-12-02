package com.example.RESTAPIDB.Services;

import com.example.RESTAPIDB.Modelos.Sistema.Lata;
import com.example.RESTAPIDB.Repository.LataRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LataService {

    @Autowired
    private LataRepo lataRepo;

    public List<Lata> allLatas(){
        return lataRepo.findAll();
    }

    public Optional<Lata> oneMaquina(String id){
        return lataRepo.findById(id);
    }
}
