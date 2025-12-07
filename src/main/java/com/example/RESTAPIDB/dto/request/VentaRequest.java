package com.example.RESTAPIDB.dto.request;

import com.example.RESTAPIDB.dto.VentaItem;
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

}