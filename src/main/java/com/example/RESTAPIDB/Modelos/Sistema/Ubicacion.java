package com.example.RESTAPIDB.Modelos.Sistema;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ubicacion {
    private double latitud;
    private double longitud;
}