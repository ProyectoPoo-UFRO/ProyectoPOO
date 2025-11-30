package org.example;

import org.bson.types.ObjectId;

public class Producto {
    private ObjectId id;
    private String nombre;
    private double precio;
    private String categoria;
    private String imagenUrl;

    public Producto() {
    }

    public Producto(String nombre, double precio, String categoria, String imagenUrl) {
        this.nombre = nombre;
        this.precio = precio;
        this.categoria = categoria;
        this.imagenUrl = imagenUrl;
    }


    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }

    @Override
    public String toString() {
        return nombre + " (ID: " + (id != null ? id.toHexString() : "N/A") + ")";
    }
}