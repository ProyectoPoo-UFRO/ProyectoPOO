package com.example.RESTAPIDB.services;

import com.example.RESTAPIDB.dto.request.CrearMaquinaRequest;
import com.example.RESTAPIDB.dto.response.MaquinaResponse;
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

    public MaquinaResponse crearMaquina(CrearMaquinaRequest request) {
        Maquina maquina = new Maquina();
        maquina.setEstado(Estado.OPERATIVA);
        maquina.setUbicacion(request.getUbicacion());
        maquina.setStockMaximo(request.getStockMaximo());
        maquina.setProductos(List.of());
        maquina.setMaquinaImagenURL("/images/maquinas/default.jpg");

        return crearRespuesta(maquinaRepo.save(maquina));
    }

    private MaquinaResponse crearRespuesta(Maquina maquina) {
        MaquinaResponse response = new MaquinaResponse();
        response.setId(maquina.getId());
        response.setUbicacion(maquina.getUbicacion());
        response.setStockMaximo(maquina.getStockMaximo());
        response.setMaquinaImagenURL(maquina.getMaquinaImagenURL());

        return response;
    }


    public boolean cambiarStock(String maquinaId, String lataId, int stock) {
        return maquinaRepo.findById(maquinaId)
                .map(maquina -> {
                    maquina.getProductos().stream()
                            .filter(p -> p.getLataId().equals(lataId))
                            .findFirst()
                            .ifPresent(p -> p.setStock(stock));
                    maquinaRepo.save(maquina);
                    return true;
                })
                .orElse(false);
    }

    public Optional<Maquina> agregarProducto(String maquinaId, Producto producto) {
        return maquinaRepo.findById(maquinaId)
                .flatMap(maquina -> lataRepo.findById(producto.getLataId())
                        .map(lata -> {
                            maquina.getProductos().stream()
                                    .filter(p -> p.getLataId().equals(producto.getLataId()))
                                    .findFirst()
                                    .ifPresentOrElse(
                                            p -> p.setStock(Math.min(p.getStock() + producto.getStock(), maquina.getStockMaximo())),
                                            () -> {
                                                producto.setStock(Math.min(producto.getStock(), maquina.getStockMaximo()));
                                                maquina.getProductos().add(producto);
                                            }
                                    );
                            return maquinaRepo.save(maquina);
                        })
                );
    }

    public Optional<Maquina> quitarProducto(String maquinaId, String lataId) {
        return maquinaRepo.findById(maquinaId)
                .filter(maquina -> maquina.getProductos().removeIf(p -> p.getLataId().equals(lataId)))
                .map(maquinaRepo::save);
    }

    public boolean cambiarEstado(String maquinaId, Estado nuevoEstado) {
        return maquinaRepo.findById(maquinaId)
                .map(maquina -> {
                    maquina.setEstado(nuevoEstado);
                    maquinaRepo.save(maquina);
                    return true;
                })
                .orElse(false);
    }
}
