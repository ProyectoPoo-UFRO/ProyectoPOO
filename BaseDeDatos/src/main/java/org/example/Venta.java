package org.example;

import org.bson.types.ObjectId;
import java.time.LocalDateTime;

/**
 * Registra cada transacción de venta.
 */
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

    // Constructor principal (corregido)
    public Venta(ObjectId productoId, int cantidad, double montoPagado, double vueltoEntregado) {
        this.productoId = productoId;
        this.cantidad = cantidad;
        this.montoPagado = montoPagado;
        this.vueltoEntregado = vueltoEntregado;
        this.fecha = LocalDateTime.now();
    }

    // --- Getters y Setters (Necesarios para el Mapper) ---

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

    public double getVueltoEntregado() {
        return vueltoEntregado;
    }

    public void setVueltoEntregado(double vueltoEntregado) {
        this.vueltoEntregado = vueltoEntregado;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }
}