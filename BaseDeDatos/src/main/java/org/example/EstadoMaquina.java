package org.example;

import org.bson.types.ObjectId;
import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;

public class EstadoMaquina {

    private ObjectId id;
    private Map<String, Integer> dineroDisponible;
    private String energia;
    private double temperatura;
    private List<String> errores;

    public EstadoMaquina(){
        this.dineroDisponible = new HashMap<>();
        this.errores = new ArrayList<>();
    }








}
