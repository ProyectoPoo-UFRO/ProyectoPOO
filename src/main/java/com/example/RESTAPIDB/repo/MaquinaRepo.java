package com.example.RESTAPIDB.repo;

import com.example.RESTAPIDB.model.sistema.Maquina;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MaquinaRepo extends MongoRepository<Maquina,String> {}