package com.example.RESTAPIDB.services;

import com.example.RESTAPIDB.model.Usuario;
import com.example.RESTAPIDB.model.sistema.Maquina;
import com.example.RESTAPIDB.model.sistema.Producto;
import com.example.RESTAPIDB.model.transaciones.Venta;
import com.example.RESTAPIDB.dto.VentaItem;
import com.example.RESTAPIDB.repo.LataRepo;
import com.example.RESTAPIDB.repo.MaquinaRepo;
import com.example.RESTAPIDB.repo.UsuarioRepo;
import com.example.RESTAPIDB.repo.VentaRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    @Transactional
    public Venta agregarVenta(String idMaquina, List<VentaItem> items, String idUsuario) {

        validarItems(items);

        Maquina maquina = obtenerMaquina(idMaquina);
        Usuario usuario = obtenerUsuario(idUsuario);

        int total = obtenerTotal(maquina, items);

        descontarSaldoUsuario(usuario, total);

        Venta venta = crearVenta(idMaquina, idUsuario, items, total);

        guardarCambios(usuario, maquina, venta);

        return venta;
    }

    private Maquina obtenerMaquina(String idMaquina) {
        return maquinaRepo.findById(idMaquina)
                .orElseThrow(() -> new RuntimeException("La máquina con id " + idMaquina + " no existe"));
    }

    private Usuario obtenerUsuario(String idUsuario) {
        return usuarioRepo.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("El usuario con id " + idUsuario + " no existe"));
    }

    private void validarItems(List<VentaItem> items) {
        if (items == null || items.isEmpty()) {
            throw new RuntimeException("No se han enviado items para la venta");
        }
    }

    private int obtenerTotal(Maquina maquina, List<VentaItem> items) {
        int total = 0;

        for (VentaItem item : items) {
            validarCantidad(item);

            Producto producto = obtenerProducto(maquina, item);

            validarStock(producto, item);

            producto.setStock(producto.getStock() - item.getCantidad());

            int precio = obtenerPrecioLata(item);

            total += precio * item.getCantidad();
        }

        return total;
    }

    private void validarCantidad(VentaItem item){
        if (item.getCantidad() <= 0) {
            throw new RuntimeException("Cantidad inválida para el producto " + item.getLataId());
        }
    }

    private Producto obtenerProducto(Maquina maquina, VentaItem item){
        return maquina.getProductos().stream()
                .filter(p -> p.getLataId().equals(item.getLataId()))
                .findFirst()
                .orElseThrow(() ->
                        new RuntimeException("El producto " + item.getLataId() + " no está en la máquina")
                );
    }

    private void validarStock(Producto producto, VentaItem item){
        if (producto.getStock() < item.getCantidad()) {
            throw new RuntimeException("Stock insuficiente para el producto " + item.getLataId());
        }
    }

    private int obtenerPrecioLata(VentaItem item){
        return lataRepo.findById(item.getLataId())
                .orElseThrow(() -> new RuntimeException("La lata " + item.getLataId() + " no existe"))
                .getPrecio();
    }

    private void descontarSaldoUsuario(Usuario usuario, int total) {
        verificarSaldo(usuario, total);
        usuario.setSaldo(usuario.getSaldo() - total);
    }

    private void guardarCambios(Usuario usuario, Maquina maquina, Venta venta) {
        usuarioRepo.save(usuario);
        maquinaRepo.save(maquina);
        ventaRepo.save(venta);
    }

    private void verificarSaldo(Usuario usuario, int total) {
        if (usuario.getSaldo() < total) {
            throw new RuntimeException("Saldo insuficiente");
        }
    }

    private Venta crearVenta(String idMaquina, String idUsuario, List<VentaItem> items, int total) {
        Venta venta = new Venta();
        venta.setIdMaquina(idMaquina);
        venta.setIdUsuario(idUsuario);
        venta.setItems(items);
        venta.setTotal(total);
        venta.setFecha(LocalDateTime.now());
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