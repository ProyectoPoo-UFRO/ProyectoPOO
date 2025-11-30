package org.example;

import org.bson.types.ObjectId;
import java.time.LocalDateTime;

public class Reposicion {
    private ObjectId id;
    private ObjectId productoId;
    private int cantidadAgregada;
    private String tecnico; // Nombre del técnico
    private LocalDateTime fecha;

    public Reposicion() {
        this.fecha = LocalDateTime.now();
    }

    public Reposicion(ObjectId productoId, int cantidadAgregada, String tecnico) {
        this.productoId = productoId;
        this.cantidadAgregada = cantidadAgregada;
        this.tecnico = tecnico;
        this.fecha = LocalDateTime.now();
    }


    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public ObjectId getProductoId() {
        return productoId;
    }

    public void setProductoId(ObjectId productoId) {
        this.productoId = productoId;
    }

    public int getCantidadAgregada() {
        return cantidadAgregada;
    }

    public void setCantidadAgregada(int cantidadAgregada) {
        this.cantidadAgregada = cantidadAgregada;
    }

    public String getTecnico() {
        return tecnico;
    }

    public void setTecnico(String tecnico) {
        this.tecnico = tecnico;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }
}