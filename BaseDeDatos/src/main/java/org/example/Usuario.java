package org.example;

import org.bson.types.ObjectId;

public class Usuario {

    private ObjectId id;
    private String usuario;
    private String ContraEncriptada;
    private String rol;

    public Usuario(){
    }

    public Usuario(ObjectId id, String usuario, String contraEncriptada, String rol) {
        this.id = id;
        this.usuario = usuario;
        ContraEncriptada = contraEncriptada;
        this.rol = rol;
    }

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

    public String getContraEncriptada() {
        return ContraEncriptada;
    }

    public void setContraEncriptada(String contraEncriptada) {
        ContraEncriptada = contraEncriptada;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
    @Override
    public String toString() {
        return "Usuario: " + usuario + " | Rol: " + rol;
    }
}
