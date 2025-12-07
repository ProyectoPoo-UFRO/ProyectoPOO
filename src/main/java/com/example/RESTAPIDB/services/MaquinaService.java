package com.example.RESTAPIDB.services;

import com.example.RESTAPIDB.model.sistema.Estado;
import com.example.RESTAPIDB.model.sistema.Maquina;
import com.example.RESTAPIDB.model.sistema.Producto;
import com.example.RESTAPIDB.repo.LataRepo;
import com.example.RESTAPIDB.repo.MaquinaRepo;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class MaquinaService {

    private final MaquinaRepo maquinaRepo;
    private final LataRepo lataRepo;

    public MaquinaService(MaquinaRepo maquinaRepo, LataRepo lataRepo) {
        this.maquinaRepo = maquinaRepo;
        this.lataRepo = lataRepo;
    }

    public List<Maquina> obtenerMaquinas() {
        return maquinaRepo.findAll();
    }

    public Optional<Maquina> obtenerMaquina(String id) {
        return maquinaRepo.findById(id);
    }

    public Maquina crearMaquina(Maquina maquina) {
        return maquinaRepo.save(maquina);
    }

    public boolean cambiarStock(String maquinaId, String lataId, int stock) {
        Maquina maquina = maquinaRepo.findById(maquinaId).orElse(null);
        if (maquina == null) {
            return false;
        }

        for (Producto producto : maquina.getProductos()) {
            if (producto.getLataId().equals(lataId)) {
                producto.setStock(stock);

                maquinaRepo.save(maquina);
                return true;
            }
        }
        return false;
    }

    public Optional<Maquina> agregarProducto(String maquinaId, Producto producto) {
        Optional<Maquina> maquinaOpt = maquinaRepo.findById(maquinaId);
        if (maquinaOpt.isEmpty()) return Optional.empty();

        if (lataRepo.findById(producto.getLataId()).isEmpty()) {
            return Optional.empty();
        }

        Maquina maquina = maquinaOpt.get();

        Optional<Producto> productoExistenteOpt = maquina.getProductos().stream()
                .filter(p -> p.getLataId().equals(producto.getLataId()))
                .findFirst();

        if (productoExistenteOpt.isPresent()) {
            Producto productoExistente = productoExistenteOpt.get();
            int nuevoStock = productoExistente.getStock() + producto.getStock();
            if (nuevoStock > maquina.getStockMaximo()) {
                productoExistente.setStock(maquina.getStockMaximo());
            } else {
                productoExistente.setStock(nuevoStock);
            }
        } else {
            if (producto.getStock() > maquina.getStockMaximo()) {
                producto.setStock(maquina.getStockMaximo());
            }
            maquina.getProductos().add(producto);
        }

        maquinaRepo.save(maquina);
        return Optional.of(maquina);
    }

    public Optional<Maquina> quitarProducto(String maquinaId, String lataId) {
        Optional<Maquina> maquinaOpt = maquinaRepo.findById(maquinaId);
        if (maquinaOpt.isEmpty()) return Optional.empty();

        Maquina maquina = maquinaOpt.get();
        boolean removed = maquina.getProductos().removeIf(p -> p.getLataId().equals(lataId));

        if (!removed) return Optional.empty();

        maquinaRepo.save(maquina);
        return Optional.of(maquina);
    }

    public boolean cambiarEstado(String maquinaId, String estadoStr) {
        Optional<Maquina> maquinaOpt = maquinaRepo.findById(maquinaId);
        if (maquinaOpt.isEmpty()) {
            return false; // No existe la máquina
        }

        Estado nuevoEstado;
        try {
            nuevoEstado = Estado.valueOf(estadoStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            return false;
        }

        Maquina maquina = maquinaOpt.get();
        maquina.setEstado(nuevoEstado);

        maquinaRepo.save(maquina);
        return true;
    }

}