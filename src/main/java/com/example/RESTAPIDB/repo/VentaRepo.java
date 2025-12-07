package com.example.RESTAPIDB.repo;

import com.example.RESTAPIDB.model.transaciones.Venta;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VentaRepo extends MongoRepository<Venta, String> {

    List<Venta> findByIdMaquina(String idMaquina);

    List<Venta> findByIdUsuario(String idUsuario);

}