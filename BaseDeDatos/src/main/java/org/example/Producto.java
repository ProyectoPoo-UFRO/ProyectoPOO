package org.example;
import org.bson.types.ObjectId;

public class Producto {
    private ObjectId id;
    private String nombre;
    private double precio;
    private String categoria;
    private String codigo;
    private int stock;
    private String imagen;

    public Producto(String cocaCola, double v, String bebida, String a1, int i, String image){

    }


    public Producto(ObjectId id, String nombre, double precio, String categoria, String codigo, int stock, String imagen) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.categoria = categoria;
        this.codigo = codigo;
        this.stock = stock;
        this.imagen = imagen;
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

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    @Override
    public String toString() {
        return "Slor: " + codigo +
                " | Nombre: " + nombre +
                " | Precio: $" + precio +
                " | Stock: " + stock +
                " | Categoría: " + categoria;
    }
}
