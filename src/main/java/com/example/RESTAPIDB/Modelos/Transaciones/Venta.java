package com.example.RESTAPIDB.Modelos.Transaciones;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "ventas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Venta {

    @Id
    private String _id;
    private String idMaquina;
    private List<VentaItem> items;
    private String idUsuario;
    private int total;
    private LocalDateTime fecha;

}