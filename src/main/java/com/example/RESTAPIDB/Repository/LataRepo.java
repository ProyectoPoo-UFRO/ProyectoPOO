package com.example.RESTAPIDB.Repository;

import com.example.RESTAPIDB.Modelos.Sistema.Lata;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LataRepo extends MongoRepository<Lata,String> {
}
