package com.example.foro.controller;

import com.example.foro.exception.GlobalExceptionHandler;
import com.example.foro.model.Usuario;
import com.example.foro.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


import java.util.Optional;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/crear")
    public ResponseEntity<Usuario> crearUsuario(@Valid @RequestBody Usuario usuario) {
        System.out.println("Usuario recibido: " + usuario.getUsername());
        System.out.println("Contraseña recibida: " + usuario.getPassword());
        System.out.println("Rol recibido: " + usuario.getRole());
    
        Usuario nuevoUsuario = usuarioService.crearUsuario(usuario);
        return ResponseEntity.ok(nuevoUsuario);
    }

    @GetMapping
    public ResponseEntity<List<Usuario>> obtenerTodosUsuarios() {
        GlobalExceptionHandler.logInfo("Listando todos los usuarios");
        List<Usuario> usuarios = usuarioService.obtenerTodosUsuarios();
        if (usuarios.isEmpty()) {
            GlobalExceptionHandler.logWarning("No se encontraron usuarios.");
            return ResponseEntity.noContent().build(); // Retorna código 204 si no hay usuarios
        }
        return ResponseEntity.ok(usuarios); // Retorna la lista de usuarios con código 200
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obtenerUsuario(@PathVariable Long id) {
        GlobalExceptionHandler.logInfo("Buscando usuario con ID: " + id);
        Optional<Usuario> usuario = usuarioService.obtenerUsuario(id);
        return usuario.map(ResponseEntity::ok)
                      .orElseGet(() -> {
                          GlobalExceptionHandler.logWarning("Usuario con ID " + id + " no encontrado.");
                          return ResponseEntity.notFound().build();
                      });
    }

    @PutMapping("/modificar/{id}")
    public ResponseEntity<Usuario> modificarUsuario(@PathVariable Long id, @Valid @RequestBody Usuario usuario) {
        GlobalExceptionHandler.logInfo("Intentando modificar usuario con ID: " + id);
        Optional<Usuario> usuarioActualizado = usuarioService.modificarUsuario(id, usuario);
        return usuarioActualizado.map(ResponseEntity::ok)
                                 .orElseGet(() -> {
                                     GlobalExceptionHandler.logWarning("Usuario con ID " + id + " no encontrado para modificación.");
                                     return ResponseEntity.notFound().build();
                                 });
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id) {
        GlobalExceptionHandler.logWarning("Intentando eliminar usuario con ID: " + id);
        usuarioService.eliminarUsuario(id);
        GlobalExceptionHandler.logInfo("Usuario con ID " + id + " eliminado correctamente.");
        return ResponseEntity.noContent().build();
    }
}