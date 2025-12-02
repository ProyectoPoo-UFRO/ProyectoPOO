package com.example.RESTAPIDB.Services;

import com.example.RESTAPIDB.Modelos.Sistema.Lata;
import com.example.RESTAPIDB.Modelos.Sistema.Maquina;
import com.example.RESTAPIDB.Modelos.Sistema.Producto;
import com.example.RESTAPIDB.Modelos.Transaciones.Venta;
import com.example.RESTAPIDB.Modelos.Transaciones.VentaItem;
import com.example.RESTAPIDB.Repository.LataRepo;
import com.example.RESTAPIDB.Repository.MaquinaRepo;
import com.example.RESTAPIDB.Repository.UsuarioRepo;
import com.example.RESTAPIDB.Repository.VentaRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class VentaService {
    //TODO Añadir verificaciones 

    @Autowired
    private VentaRepo ventaRepo;

    @Autowired
    private UsuarioRepo usuarioRepo;

    @Autowired
    private MaquinaRepo maquinaRepo;

    @Autowired
    private LataRepo lataRepo;

    public Venta agregarVenta(String idMaquina, List<VentaItem> items, String idUsuario) {

        Maquina maquina = maquinaRepo.findById(idMaquina)
                .orElseThrow(() -> new RuntimeException("La máquina no existe"));

        // Verificar usuario
        usuarioRepo.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("El usuario no existe"));

        int total = 0;

        for (VentaItem item : items) {
            // Buscar producto en la máquina
            Producto producto = maquina.getProductos().stream()
                    .filter(p -> p.getLataId().equals(item.getIdLata()))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException(
                            "El producto " + item.getIdLata() + " no está en esta máquina"
                    ));

            if (producto.getStock() < item.getCantidad()) {
                throw new RuntimeException("Stock insuficiente para " + item.getIdLata());
            }

            // Actualizar stock
            producto.setStock(producto.getStock() - item.getCantidad());

            // Obtener precio desde Lata
            Lata lata = lataRepo.findById(item.getIdLata())
                    .orElseThrow(() -> new RuntimeException("La lata " + item.getIdLata() + " no existe"));

            total += lata.getPrecio() * item.getCantidad();
        }

        // Guardar la venta
        Venta venta = new Venta();
        venta.setIdMaquina(idMaquina);
        venta.setIdUsuario(idUsuario);
        venta.setItems(items);
        venta.setTotal(total);
        venta.setFecha(LocalDateTime.now());

        ventaRepo.save(venta);

        // Guardar cambios en la máquina
        maquinaRepo.save(maquina);

        return venta;
    }

    // Métodos de consulta

    public Optional<Venta> obtenerVentaPorId(String id) {
        return ventaRepo.findById(id);
    }

    public List<Venta> obtenerVentas() {
        return ventaRepo.findAll();
    }

    public List<Venta> obtenerVentasPorMaquina(String idMaquina) {
        return ventaRepo.findByIdMaquina(idMaquina);
    }
}
