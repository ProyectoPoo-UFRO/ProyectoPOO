package org.example;

import  org.bson.types.ObjectId;
import java.time.LocalDateTime;

public class Venta {

    private ObjectId id;
    private ObjectId productoId;
    private int cantidad;
    private double montoPagado;
    private double vueltoEntregado;
    private LocalDateTime fecha;

    public Venta() {
        this.fecha = LocalDateTime.now();
    }

    public Venta(ObjectId id, ObjectId productoId, int cantidad, double montoPagado, double vueltoEntrado) {
        this.id = id;
        this.productoId = productoId;
        this.cantidad = cantidad;
        this.montoPagado = montoPagado;
        this.vueltoEntregado = vueltoEntregado;
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

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getMontoPagado() {
        return montoPagado;
    }

    public void setMontoPagado(double montoPagado) {
        this.montoPagado = montoPagado;
    }

    public double getVueltoEntrado() {
        return vueltoEntregado;
    }

    public void setVueltoEntrado(double vueltoEntrado) {
        this.vueltoEntregado = vueltoEntrado;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    @Override
    public String toString() {
        return "Venta Registrada en " + fecha.toString() +
                " | Producto ID: " + productoId.toHexString() +
                " | Pagado: $" + montoPagado +
                " | Vuelto: $" + vueltoEntregado;
    }
}
