package org.example;

import org.bson.types.ObjectId;
import java.time.LocalDateTime;

public class Reposicion {

    private ObjectId id;
    private ObjectId productoId;
    private int cantidadAgregada;
    private LocalDateTime fechaReposicion;
    private String tecnico;

    public Reposicion(ObjectId productoId, int cantidad, String tecnico){
        this.fechaReposicion = LocalDateTime.now();
    }

    public Reposicion(String tecnico, ObjectId id, ObjectId productoId, int cantidadAgregada) {
        this.tecnico = tecnico;
        this.id = id;
        this.productoId = productoId;
        this.cantidadAgregada = cantidadAgregada;
        this.fechaReposicion = LocalDateTime.now();
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

    public LocalDateTime getFechaReposicion() {
        return fechaReposicion;
    }

    public void setFechaReposicion(LocalDateTime fechaReposicion) {
        this.fechaReposicion = fechaReposicion;
    }

    public String getTecnico() {
        return tecnico;
    }

    public void setTecnico(String tecnico) {
        this.tecnico = tecnico;
    }
    @Override
    public String toString() {
        return "Reposición de " + cantidadAgregada + " unidades en " + fechaReposicion.toString() +
                " por el técnico: " + tecnico;
    }
}
