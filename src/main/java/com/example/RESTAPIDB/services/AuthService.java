package com.example.RESTAPIDB.services;

import com.example.RESTAPIDB.dto.request.LoginRequest;
import com.example.RESTAPIDB.dto.request.RegisterRequest;
import com.example.RESTAPIDB.model.Rol;
import com.example.RESTAPIDB.model.Usuario;
import com.example.RESTAPIDB.model.UsuarioDetails;
import com.example.RESTAPIDB.repo.UsuarioRepo;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.ArrayList;

@Service
public class AuthService {

    private final UsuarioRepo repo;
    private final AuthenticationManager authenticationManager;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(5);
    private final JWTService jwtService;

    public AuthService(AuthenticationManager authenticationManager, JWTService jwtService, UsuarioRepo repo) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.repo = repo;
    }

    public Authentication autenticarUsuario(LoginRequest user) {
        return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getNombre(),
                        user.getContrasenia()
                )
        );
    }

    public String generarToken(Usuario usuario) {
        UsuarioDetails userDetails = new UsuarioDetails(usuario);
        return jwtService.generarToken(userDetails);
    }

    public String verificarLogin(LoginRequest user) {
        Authentication auth = autenticarUsuario(user);

        if (auth.isAuthenticated()) {
            Usuario usuario = repo.findByNombre(user.getNombre());

            if (usuario == null) {
                throw new RuntimeException("Usuario no encontrado");
            }

            return generarToken(usuario);
        }

        throw new RuntimeException("Error en autenticación");
    }

    public Usuario registrar(RegisterRequest request){
        if (repo.existsByNombre(request.getNombre())) {
            throw new RuntimeException("El nombre de usuario ya está en uso");
        }
        return repo.save(crearUsuario(request));
    }

    public Usuario crearUsuario(RegisterRequest request){
        Usuario u = new Usuario();
        u.setNombre(request.getNombre());
        u.setContrasenia(encoder.encode(request.getContrasenia()));
        u.setRol(Rol.USER);
        u.setIdLatasFavoritas(new ArrayList<>());
        u.setIdMaquinasFavoritas(new ArrayList<>());
        return u;
    }

}