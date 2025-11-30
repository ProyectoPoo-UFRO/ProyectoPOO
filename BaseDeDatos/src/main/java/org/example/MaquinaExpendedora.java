package org.example;

import java.util.ArrayList;

public class MaquinaExpendedora {
    private String id;
    private String estado;
    private Coordenadas ubicacion;
    private ArrayList<Producto> productos;

    // ✔ Constructor vacío (Mongo lo NECESITA)
    public MaquinaExpendedora() {
        this.productos = new ArrayList<>();
    }

    public MaquinaExpendedora(String id, String estado, Coordenadas ubicacion) {
        this.id = id;
        this.estado = estado;
        this.ubicacion = ubicacion;
        this.productos = new ArrayList<>();
    }

    // ✔ SETTERS obligatorios para el mapper
    public void setId(String id) {
        this.id = id;
    }

    public void setUbicacion(Coordenadas ubicacion) {
        this.ubicacion = ubicacion;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getId() {
        return id;
    }

    public String getEstado() {
        return estado;
    }

    public Coordenadas getUbicacion() {
        return ubicacion;
    }

    public ArrayList<Producto> getProductos() {
        return productos;
    }

    public void agregarProducto(Producto p) {
        productos.add(p);
    }

    @Override
    public String toString() {
        return "\nMAQUINA " + id +
                "\nEstado: " + estado +
                "\nUbicación: " + ubicacion +
                "\nCant. Productos: " + productos.size();
    }
}
