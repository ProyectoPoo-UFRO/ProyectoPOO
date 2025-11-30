package org.example;

public class Usuario {
    private String id;
    private String nombre;
    private String rol; // "admin", "tecnico", "usuario"

    public Usuario() {}

    public Usuario(String id, String nombre, String rol) {
        this.id = id;
        this.nombre = nombre;
        this.rol = rol;
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getRol() {
        return rol;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    @Override
    public String toString() {
        return String.format("%s (id=%s, rol=%s)", nombre, id, rol);
    }
}
