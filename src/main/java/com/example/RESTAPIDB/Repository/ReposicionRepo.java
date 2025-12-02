package com.example.RESTAPIDB.Repository;

import com.example.RESTAPIDB.Modelos.Transaciones.Reposicion;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReposicionRepo extends MongoRepository<Reposicion, String> {
}
