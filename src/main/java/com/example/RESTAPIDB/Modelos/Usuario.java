package com.example.RESTAPIDB.Modelos;

import com.example.RESTAPIDB.Modelos.Sistema.Lata;
import com.example.RESTAPIDB.Modelos.Sistema.Maquina;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Document(collection = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {
    @Id
    @Field("_id")
    private String id;
    private String nombre;
    private String contrasenia;
    private String rol; //TODO Pasar a enum
    private String avatarImage;
    private List<String> idLatasFavoritas;
    private List<String> idMaquinasFavoritas;
}
