package com.example.RESTAPIDB.services;

import com.example.RESTAPIDB.dto.request.CrearMaquinaRequest;
import com.example.RESTAPIDB.dto.response.MaquinaResponse;
import com.example.RESTAPIDB.model.sistema.Estado;
import com.example.RESTAPIDB.model.sistema.Lata;
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

    public MaquinaResponse crearMaquina(CrearMaquinaRequest request) {
        // Construir la entidad
        Maquina maquina = new Maquina();
        maquina.setEstado(Estado.OPERATIVA);
        maquina.setUbicacion(request.getUbicacion());
        maquina.setStockMaximo(request.getStockMaximo());
        maquina.setProductos(List.of());
        maquina.setMaquinaImagenURL("/images/maquinas/default.jpg");

        Maquina maquinaGuardada = maquinaRepo.save(maquina);

        MaquinaResponse response = new MaquinaResponse();
        response.setId(maquinaGuardada.getId());
        response.setUbicacion(maquinaGuardada.getUbicacion());
        response.setStockMaximo(maquinaGuardada.getStockMaximo());
        response.setMaquinaImagenURL(maquinaGuardada.getMaquinaImagenURL());

        return response;
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
        System.out.println("Buscando máquina con ID: " + maquinaId);
        if (maquinaOpt.isEmpty()) {
            System.out.println("No se encontró la máquina.");
            return Optional.empty();
        }

        String lataId = producto.getLataId(); // ajusta el tipo según tu entidad
        System.out.println("Buscando lata con ID: " + lataId);

        Optional<Lata> lataOpt = lataRepo.findById(lataId);
        if (lataOpt.isEmpty()) {
            System.out.println("No se encontró la lata con ID: " + lataId);
            return Optional.empty();
        }
        System.out.println("Lata encontrada: " + lataOpt.get());


        Maquina maquina = maquinaOpt.get();
        System.out.println("Máquina encontrada: " + maquina);

        Optional<Producto> productoExistenteOpt = maquina.getProductos().stream()
                .filter(p -> p.getLataId().equals(producto.getLataId()))
                .findFirst();

        if (productoExistenteOpt.isPresent()) {
            Producto productoExistente = productoExistenteOpt.get();
            System.out.println("Producto existente encontrado: " + productoExistente);
            int nuevoStock = productoExistente.getStock() + producto.getStock();
            System.out.println("Stock actual: " + productoExistente.getStock() + ", stock a agregar: " + producto.getStock() + ", stock calculado: " + nuevoStock);
            if (nuevoStock > maquina.getStockMaximo()) {
                productoExistente.setStock(maquina.getStockMaximo());
                System.out.println("Nuevo stock ajustado al máximo: " + maquina.getStockMaximo());
            } else {
                productoExistente.setStock(nuevoStock);
                System.out.println("Nuevo stock asignado: " + nuevoStock);
            }
        } else {
            System.out.println("Producto no existe en la máquina. Agregando nuevo producto: " + producto);
            if (producto.getStock() > maquina.getStockMaximo()) {
                producto.setStock(maquina.getStockMaximo());
                System.out.println("Stock del nuevo producto ajustado al máximo: " + maquina.getStockMaximo());
            }
            maquina.getProductos().add(producto);
        }

        maquinaRepo.save(maquina);
        System.out.println("Máquina guardada: " + maquina);
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

    public boolean cambiarEstado(String maquinaId, Estado nuevoEstado) {
        Optional<Maquina> maquinaOpt = maquinaRepo.findById(maquinaId);
        if (maquinaOpt.isEmpty()) return false;

        Maquina maquina = maquinaOpt.get();
        maquina.setEstado(nuevoEstado);
        maquinaRepo.save(maquina);
        return true;
    }

}