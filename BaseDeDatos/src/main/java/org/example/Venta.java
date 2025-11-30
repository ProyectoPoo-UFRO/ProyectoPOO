package org.example;

import java.util.Date;

public class Venta {
    private String id;
    private String idMaquina;
    private String idProducto;
    private int cantidad;
    private int total;
    private Date fecha;

    public Venta() {}

    public Venta(String id, String idMaquina, String idProducto, int cantidad, int total, Date fecha) {
        this.id = id;
        this.idMaquina = idMaquina;
        this.idProducto = idProducto;
        this.cantidad = cantidad;
        this.total = total;
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

    public int getTotal() {
        return total;
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

    public void setTotal(int total) {
        this.total = total;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    @Override
    public String toString() {
        return String.format("Venta[id=%s, maquina=%s, producto=%s, cantidad=%d, total=%d, fecha=%s]",
                id, idMaquina, idProducto, cantidad, total, fecha);
    }
}
