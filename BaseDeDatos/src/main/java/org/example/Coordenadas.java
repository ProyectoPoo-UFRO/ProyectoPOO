package org.example;

public class Coordenadas {
    private double latitud;
    private double longitud;

    // ✔ Constructor vacío requerido por deserialización
    public Coordenadas() {}

    public Coordenadas(double latitud, double longitud) {
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    @Override
    public String toString() {
        return "Lat: " + latitud + ", Lng: " + longitud;
    }
}
