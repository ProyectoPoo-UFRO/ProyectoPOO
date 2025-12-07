package com.example.RESTAPIDB.model.transaciones;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Document(collection = "reposiciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reposicion {

    @Id
    @Field("_id")
    private String id;
    private String idMaquina;
    private String idProducto;
    private int cantidad;
    private LocalDateTime fecha;

}
