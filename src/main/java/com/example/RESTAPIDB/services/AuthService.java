package com.example.RESTAPIDB.services;

import com.example.RESTAPIDB.dto.request.LoginRequest;
import com.example.RESTAPIDB.dto.request.RegisterRequest;
import com.example.RESTAPIDB.model.Rol;
import com.example.RESTAPIDB.model.Usuario;
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
    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(5);
    private final JWTService jwtService;

    public AuthService(AuthenticationManager authenticationManager, JWTService jwtService, UsuarioRepo repo) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.repo = repo;
    }

    //TODO separar en verificar y crear token
    public String verificarLogin(LoginRequest user) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getNombre(), user.getContrasenia())
        );

        if (auth.isAuthenticated()){
            return jwtService.generarToken(user.getNombre());
        }

        return "problema";
    }

    public Usuario registrar(RegisterRequest request){
        //TODO añadir meotodo para verificar si usuarioexiste
        return repo.save(crearUsuario(request));
    }

    public Usuario crearUsuario(RegisterRequest request){
        Usuario u = new Usuario();
        String contraseniaCifrada = encoder.encode(request.getContrasenia());
        u.setContrasenia(contraseniaCifrada);
        u.setNombre(request.getNombre());
        u.setRol(Rol.USER);
        u.setIdLatasFavoritas(new ArrayList<>());
        u.setIdMaquinasFavoritas(new ArrayList<>());
        return u;
    }

}