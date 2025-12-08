package com.example.RESTAPIDB.model.sistema;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "maquinas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Maquina {

    @Id
    private String id;
    private Estado estado;
    private String ubicacion;
    private int stockMaximo;
    private List<Producto> productos;
    private String maquinaImagenURL = "/images/maquinas/default.jpg";

}