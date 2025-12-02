package com.example.RESTAPIDB.Repository;

import com.example.RESTAPIDB.Modelos.Transaciones.Venta;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VentaRepo extends MongoRepository<Venta, String> {
    List<Venta> findByIdMaquina(String idMaquina);
}
