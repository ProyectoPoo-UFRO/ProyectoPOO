package com.example.RESTAPIDB.services;

import com.example.RESTAPIDB.model.Usuario;
import com.example.RESTAPIDB.repo.UsuarioRepo;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepo usuarioRepo;

    public UsuarioService(UsuarioRepo usuarioRepo) {
        this.usuarioRepo = usuarioRepo;
    }

    public Usuario crearUsuario(Usuario usuario) {
        return usuarioRepo.save(usuario);
    }

    public Optional<Usuario> obtenerUsuario(String id) {
        return usuarioRepo.findById(id);
    }

    public List<Usuario> obtenerUsuarios() {
        return usuarioRepo.findAll();
    }

    public Optional<Usuario> recargarSaldo(String idUsuario, double monto) {
        return usuarioRepo.findById(idUsuario).map(usuario -> {
            if (monto <= 0) {
                throw new IllegalArgumentException("El monto debe ser mayor a 0");
            }
            usuario.setSaldo(usuario.getSaldo() + monto);
            return usuarioRepo.save(usuario);
        });
    }

    public Optional<Usuario> agregarMaquinaFavorita(String idUsuario, String idMaquina) {
        return usuarioRepo.findById(idUsuario).map(usuario -> {
            if (!usuario.getIdMaquinasFavoritas().contains(idMaquina)) {
                usuario.getIdMaquinasFavoritas().add(idMaquina);
            }
            return usuarioRepo.save(usuario);
        });
    }

    public Optional<Usuario> quitarMaquinaFavorita(String idUsuario, String idMaquina) {
        return usuarioRepo.findById(idUsuario).map(usuario -> {
            usuario.getIdMaquinasFavoritas().remove(idMaquina);
            return usuarioRepo.save(usuario);
        });
    }

    public Optional<Usuario> agregarLataFavorita(String idUsuario, String idLata) {
        return usuarioRepo.findById(idUsuario).map(usuario -> {
            if (!usuario.getIdLatasFavoritas().contains(idLata)) {
                usuario.getIdLatasFavoritas().add(idLata);
            }
            return usuarioRepo.save(usuario);
        });
    }

    public Optional<Usuario> quitarLataFavorita(String idUsuario, String idLata) {
        return usuarioRepo.findById(idUsuario).map(usuario -> {
            usuario.getIdLatasFavoritas().remove(idLata);
            return usuarioRepo.save(usuario);
        });
    }

}