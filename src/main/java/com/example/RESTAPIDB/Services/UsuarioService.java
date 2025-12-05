package com.example.RESTAPIDB.Services;

import com.example.RESTAPIDB.Modelos.Usuario;
import com.example.RESTAPIDB.Repository.UsuarioRepo;
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

}