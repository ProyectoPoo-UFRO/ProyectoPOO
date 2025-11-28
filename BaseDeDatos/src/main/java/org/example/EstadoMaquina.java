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

    public EstadoMaquina(ObjectId id, Map<String, Integer> dineroDisponible, String energia, double temperatura, List<String> errores) {
        this.id = id;
        this.dineroDisponible = dineroDisponible;
        this.energia = energia;
        this.temperatura = temperatura;
        this.errores = errores;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public Map<String, Integer> getDineroDisponible() {
        return dineroDisponible;
    }

    public void setDineroDisponible(Map<String, Integer> dineroDisponible) {
        this.dineroDisponible = dineroDisponible;
    }

    public String getEnergia() {
        return energia;
    }

    public void setEnergia(String energia) {
        this.energia = energia;
    }

    public double getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(double temperatura) {
        this.temperatura = temperatura;
    }

    public List<String> getErrores() {
        return errores;
    }

    public void setErrores(List<String> errores) {
        this.errores = errores;
    }

    @Override
    public String toString() {
        return "--- ESTADO DE LA MÁQUINA ---\n" +
                "Energía: " + energia + "\n" +
                "Temperatura: " + temperatura + "°C\n" +
                "Errores: " + (errores.isEmpty() ? "Ninguno" : String.join(", ", errores)) + "\n" +
                "Dinero en Caja: " + dineroDisponible;
    }

}
