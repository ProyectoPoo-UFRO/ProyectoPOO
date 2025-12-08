package com.example.RESTAPIDB.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VentaItem {

    private String lataId;
    private int cantidad;

}