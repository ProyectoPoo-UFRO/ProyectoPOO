package com.example.RESTAPIDB.repo;

import com.example.RESTAPIDB.model.transaciones.Reposicion;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReposicionRepo extends MongoRepository<Reposicion, String> {}