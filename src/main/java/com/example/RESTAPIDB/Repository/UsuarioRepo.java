package com.example.RESTAPIDB.Repository;

import com.example.RESTAPIDB.Modelos.Usuario;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepo extends MongoRepository<Usuario,String> {}