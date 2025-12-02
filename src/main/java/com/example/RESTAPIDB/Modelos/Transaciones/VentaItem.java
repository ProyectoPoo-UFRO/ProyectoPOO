package com.example.RESTAPIDB.Modelos.Transaciones;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VentaItem {
    private String idLata;
    private int cantidad;

    public String getIdLata() {
        return idLata;
    }

    public int getCantidad() {
        return cantidad;
    }
}
