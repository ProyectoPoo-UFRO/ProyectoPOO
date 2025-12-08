package com.example.RESTAPIDB.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CrearLataRequest {

    @NotBlank(message = "El nombre de la lata es obligatorio")
    private String nombre;

    @PositiveOrZero(message = "El precio no puede ser negativo")
    private int precio;

}