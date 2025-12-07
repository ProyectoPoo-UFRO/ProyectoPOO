package com.example.RESTAPIDB.repo;

import com.example.RESTAPIDB.model.sistema.Lata;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LataRepo extends MongoRepository<Lata,String> {}