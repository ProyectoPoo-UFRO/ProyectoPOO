package com.example.RESTAPIDB.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MaquinaResponse {

    private String id;
    private String ubicacion;
    private int stockMaximo;
    private String maquinaImagenURL;

}