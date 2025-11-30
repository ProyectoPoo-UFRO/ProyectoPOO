package org.example;

public class EstadoMaquina {
    public static final String ONLINE = "ONLINE";
    public static final String OFFLINE = "OFFLINE";
    public static final String SIN_STOCK = "SIN_STOCK";
    public static final String MANTENCION = "MANTENCION";

    private String estado;

    public EstadoMaquina() {
        this.estado = ONLINE;
    }

    public EstadoMaquina(String estado) {
        this.estado = estado;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return estado;
    }
}
