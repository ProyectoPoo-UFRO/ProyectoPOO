package org.example;

public class Producto {
    private String nombre;
    private int stock;
    private int precio;

    // ✔ Constructor vacío necesario para deserialización
    public Producto() {}

    public Producto(String nombre, int stock, int precio) {
        this.nombre = nombre;
        this.stock = stock;
        this.precio = precio;
    }

    // ✔ SETTERS necesarios para el mapper
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getNombre() {
        return nombre;
    }

    public int getStock() {
        return stock;
    }

    public int getPrecio() {
        return precio;
    }

    @Override
    public String toString() {
        return nombre + " | Stock: " + stock + " | $" + precio;
    }
}
