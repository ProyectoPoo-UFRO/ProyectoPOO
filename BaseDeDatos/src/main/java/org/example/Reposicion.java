package org.example;

import java.util.Date;

public class Reposicion {
    private String id;
    private String idMaquina;
    private String idProducto;
    private int cantidad;
    private Date fecha;

    public Reposicion() {}

    public Reposicion(String id, String idMaquina, String idProducto, int cantidad, Date fecha) {
        this.id = id;
        this.idMaquina = idMaquina;
        this.idProducto = idProducto;
        this.cantidad = cantidad;
        this.fecha = fecha;
    }

    public String getId() {
        return id;
    }

    public String getIdMaquina() {
        return idMaquina;
    }

    public String getIdProducto() {
        return idProducto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setIdMaquina(String idMaquina) {
        this.idMaquina = idMaquina;
    }

    public void setIdProducto(String idProducto) {
        this.idProducto = idProducto;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    @Override
    public String toString() {
        return String.format("Reposicion[id=%s, maquina=%s, producto=%s, cantidad=%d, fecha=%s]",
                id, idMaquina, idProducto, cantidad, fecha);
    }
}
