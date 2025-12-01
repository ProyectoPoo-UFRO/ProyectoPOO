package org.example;

public class Maquina {
    private String nombre;
    private String tipo;
    private int año;

    public Maquina(String nombre, String tipo, int año) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.año = año;
    }

    public String getNombre() {
        return nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public int getAño() {
        return año;
    }

    @Override
    public String toString() {
        return "Maquina{" +
                "nombre='" + nombre + '\'' +
                ", tipo='" + tipo + '\'' +
                ", año=" + año +
                '}';
    }
}
