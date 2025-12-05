package com.example.RESTAPIDB.Modelos;

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
    private Rol rol;
    private String avatarImage;
    private List<String> idLatasFavoritas;
    private List<String> idMaquinasFavoritas;

}