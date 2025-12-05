package com.example.RESTAPIDB.Repository;

import com.example.RESTAPIDB.Modelos.Sistema.Maquina;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MaquinaRepo extends MongoRepository<Maquina,String> {}