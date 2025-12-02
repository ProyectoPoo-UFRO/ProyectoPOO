package com.example.RESTAPIDB.Modelos.Sistema;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="latas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Lata {

    @Id
    private String _id;
    private String nombre;
    private int precio;
    private String imagen;

    public int getPrecio() {
        return precio;
    }
}
