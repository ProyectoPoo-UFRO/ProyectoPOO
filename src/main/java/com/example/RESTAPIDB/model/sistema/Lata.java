package com.example.RESTAPIDB.model.sistema;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection="latas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Lata {

    @Id
    @Field("_id")
    private String id;
    private String nombre;
    private int precio;
    private String imagen;

}