package com.example.RESTAPIDB.Modelos.Transaciones;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VentaRequest {
    private String idMaquina;
    private String idUsuario;
    private List<VentaItem> items;

    public String getIdMaquina() {
        return idMaquina;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public List<VentaItem> getItems() {
        return items;
    }
}

