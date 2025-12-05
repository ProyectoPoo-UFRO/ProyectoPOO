package com.example.RESTAPIDB.Controller.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockUpdateRequest {

    private String lataId;
    private int newStock;

}

