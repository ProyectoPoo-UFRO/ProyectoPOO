//TODO Añadir verificaciones
package com.example.RESTAPIDB.services;

import com.example.RESTAPIDB.model.sistema.Lata;
import com.example.RESTAPIDB.model.sistema.Maquina;
import com.example.RESTAPIDB.model.sistema.Producto;
import com.example.RESTAPIDB.model.transaciones.Venta;
import com.example.RESTAPIDB.dto.VentaItem;
import com.example.RESTAPIDB.repo.LataRepo;
import com.example.RESTAPIDB.repo.MaquinaRepo;
import com.example.RESTAPIDB.repo.UsuarioRepo;
import com.example.RESTAPIDB.repo.VentaRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class VentaService {

    private final VentaRepo ventaRepo;
    private final UsuarioRepo usuarioRepo;
    private final MaquinaRepo maquinaRepo;
    private final LataRepo lataRepo;

    public VentaService(VentaRepo ventaRepo, UsuarioRepo usuarioRepo, MaquinaRepo maquinaRepo, LataRepo lataRepo) {
        this.ventaRepo = ventaRepo;
        this.usuarioRepo = usuarioRepo;
        this.maquinaRepo = maquinaRepo;
        this.lataRepo = lataRepo;
    }

    public Venta agregarVenta(String idMaquina, List<VentaItem> items, String idUsuario) {

        Maquina maquina = maquinaRepo.findById(idMaquina)
                .orElseThrow(() -> new RuntimeException("La máquina no existe"));
        usuarioRepo.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("El usuario no existe"));

        int total = 0;

        for (VentaItem item : items) {
            Producto producto = maquina.getProductos().stream()
                    .filter(p -> p.getLataId().equals(item.getIdLata()))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("El producto " + item.getIdLata() + " no está en esta máquina"));
            if (producto.getStock() < item.getCantidad()) {
                throw new RuntimeException("Stock insuficiente para " + item.getIdLata());
            }
            producto.setStock(producto.getStock() - item.getCantidad());

            Lata lata = lataRepo.findById(item.getIdLata())
                    .orElseThrow(() -> new RuntimeException("La lata " + item.getIdLata() + " no existe"));

            total += lata.getPrecio() * item.getCantidad();
        }

        Venta venta = new Venta();
        venta.setIdMaquina(idMaquina);
        venta.setIdUsuario(idUsuario);
        venta.setItems(items);
        venta.setTotal(total);
        venta.setFecha(LocalDateTime.now());

        ventaRepo.save(venta);
        maquinaRepo.save(maquina);
        return venta;
    }

    public Optional<Venta> obtenerVentaPorId(String id) {
        return ventaRepo.findById(id);
    }

    public List<Venta> obtenerVentas() {
        return ventaRepo.findAll();
    }

    public List<Venta> obtenerVentasPorMaquina(String idMaquina) {
        return ventaRepo.findByIdMaquina(idMaquina);
    }

    public List<Venta> obtenerVentasPorUsuario(String idUsuario) {
        return ventaRepo.findByIdUsuario(idUsuario);
    }

}