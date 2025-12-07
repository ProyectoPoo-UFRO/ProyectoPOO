package com.example.RESTAPIDB.services;

import com.example.RESTAPIDB.model.Usuario;
import com.example.RESTAPIDB.model.UsuarioDetails;
import com.example.RESTAPIDB.repo.UsuarioRepo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UsuarioDetailsService implements UserDetailsService {

    private final UsuarioRepo userRepo;

    public UsuarioDetailsService(UsuarioRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String nombre) throws UsernameNotFoundException {
        Usuario user = userRepo.findByNombre(nombre);
        if (user==null){
            throw new UsernameNotFoundException("No encontrado");
        }
        return new UsuarioDetails(user);
    }

}