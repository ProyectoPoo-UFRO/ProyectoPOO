package com.example.RESTAPIDB.Modelos.Sistema;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Optional;

@Document(collection = "maquinas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Maquina {
    @Id
    private String _id;
    private String estado;
    private Ubicacion ubicacion;
    private List<Producto> productos;
    private String maquinaImagenURL = "/images/maquinas/default.jpg";

    public String get_id() {
        return _id;
    }

    public String getEstado() {
        return estado;
    }

    public Ubicacion getUbicacion() {
        return ubicacion;
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public String getMaquinaImagenURL() {
        return maquinaImagenURL;
    }
}