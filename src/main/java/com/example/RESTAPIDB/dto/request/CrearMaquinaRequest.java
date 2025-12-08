package com.example.RESTAPIDB.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CrearMaquinaRequest {

    @NotBlank(message = "La ubicación es obligatoria")
    private String ubicacion;

    @PositiveOrZero(message = "El stock máximo no puede ser negativo")
    private int stockMaximo;

}