package com.example.RESTAPIDB.Modelos.Transaciones;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "ventas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Venta {

    @Id
    private String _id;
    private String idMaquina;
    private List<VentaItem> items;
    private String idUsuario;
    private int total;
    private LocalDateTime fecha;

    public void set_id(String _id) {
        this._id = _id;
    }

    public void setIdMaquina(String idMaquina) {
        this.idMaquina = idMaquina;
    }

    public void setItems(List<VentaItem> items) {
        this.items = items;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }
}
