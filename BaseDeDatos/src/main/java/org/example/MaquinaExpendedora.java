package org.example;

import org.bson.types.ObjectId;

/**
 * Representa la máquina expendedora en sí.
 */
public class MaquinaExpendedora {
    private ObjectId id;
    private String codigo;
    private double latitud;
    private double longitud;
    private String ubicacion;
    private String estado;

    // Constructor vacío
    public MaquinaExpendedora() {
    }

    // Constructor principal
    public MaquinaExpendedora(String codigo, double latitud, double longitud, String ubicacion, String estado) {
        this.codigo = codigo;
        this.latitud = latitud;
        this.longitud = longitud;
        this.ubicacion = ubicacion;
        this.estado = estado;
    }

    // --- Getters y Setters ---

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
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

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}