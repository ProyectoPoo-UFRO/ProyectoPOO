package com.example.RESTAPIDB.controller;

import com.example.RESTAPIDB.model.Usuario;
import com.example.RESTAPIDB.services.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public ResponseEntity<Usuario> crearUsuario(@RequestBody Usuario usuario) {
        Usuario u = usuarioService.crearUsuario(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(u);
    }

    @GetMapping
    public ResponseEntity<List<Usuario>> obtenerUsuarios() {
        List<Usuario> usuarios = usuarioService.obtenerUsuarios();
        if (usuarios.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obtenerUsuario(@PathVariable String id) {
        return usuarioService.obtenerUsuario(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/recargar")
    public ResponseEntity<Usuario> recargarSaldo(@PathVariable String id, @RequestBody Map<String, Double> request) {

        double monto = request.getOrDefault("monto", 0.0);

        try {
            return usuarioService.recargarSaldo(id, monto)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/{id}/favoritas/maquinas/{idMaquina}")
    public ResponseEntity<Usuario> agregarMaquinaFavorita(@PathVariable String id, @PathVariable String idMaquina) {
        return usuarioService.agregarMaquinaFavorita(id, idMaquina)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}/favoritas/maquinas/{idMaquina}")
    public ResponseEntity<Usuario> quitarMaquinaFavorita(@PathVariable String id, @PathVariable String idMaquina) {
        return usuarioService.quitarMaquinaFavorita(id, idMaquina)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/favoritas/latas/{idLata}")
    public ResponseEntity<Usuario> agregarLataFavorita(@PathVariable String id, @PathVariable String idLata) {
        return usuarioService.agregarLataFavorita(id, idLata)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    public ResponseEntity<Usuario> quitarLataFavorita(@PathVariable String id, @PathVariable String idLata) {

        return usuarioService.quitarLataFavorita(id, idLata)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

}