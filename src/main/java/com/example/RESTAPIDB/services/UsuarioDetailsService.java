package com.example.RESTAPIDB.services;

import com.example.RESTAPIDB.model.Usuario;
import com.example.RESTAPIDB.model.UsuarioDetails;
import com.example.RESTAPIDB.repo.UsuarioRepo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioDetailsService implements UserDetailsService {

    private final UsuarioRepo repo;

    public UsuarioDetailsService(UsuarioRepo repo) {
        this.repo = repo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario user = repo.findByNombre(username);
        if (user == null) {
            throw new UsernameNotFoundException("Usuario no encontrado");
        }
        return new UsuarioDetails(user);
    }

    public UserDetails loadUserById(String id) throws UsernameNotFoundException {
        Optional<Usuario> user = repo.findById(id);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("Usuario no encontrado");
        }
        return new UsuarioDetails(user.orElse(null));
    }
}
