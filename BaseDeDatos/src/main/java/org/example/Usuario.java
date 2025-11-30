package org.example;

import org.bson.types.ObjectId;

/**
 * Representa a un usuario (Técnico o Administrador)
 */
public class Usuario {
    private ObjectId id;
    private String usuario;
    private String passwordHash; // Contraseña encriptada (o simple para el ejemplo)
    private String rol; // "admin" o "tecnico"

    public Usuario() {
    }

    public Usuario(String usuario, String password, String rol) {
        this.usuario = usuario;
        this.passwordHash = password; // Simulación simple de passwordHash
        this.rol = rol;
    }

    // --- Getters y Setters ---

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
}