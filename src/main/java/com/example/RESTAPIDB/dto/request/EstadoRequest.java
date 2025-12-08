package com.example.RESTAPIDB.dto.request;

import com.example.RESTAPIDB.model.sistema.Estado;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EstadoRequest {

    private Estado estado;

}