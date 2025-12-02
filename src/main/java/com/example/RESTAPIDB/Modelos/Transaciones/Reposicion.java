package com.example.RESTAPIDB.Modelos.Transaciones;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "reposiciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reposicion {
    @Id
    private String _id;
    private String idMaquina;
    private String idProducto;
    private int cantidad;
    private LocalDateTime fecha; //TODO ver opciones para usar mejor la fecha
}
