package com.example.RESTAPIDB.repo;

import com.example.RESTAPIDB.model.Usuario;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepo extends MongoRepository<Usuario,String> {
    Usuario findByNombre(String nombre);
    boolean existsByNombre(String nombre);
}