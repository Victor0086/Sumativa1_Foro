
package com.example.foro.service;



import com.example.foro.exception.GlobalExceptionHandler;
import com.example.foro.model.Usuario;
import com.example.foro.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.List;
    
@Service
public class UsuarioService {
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<Usuario> obtenerTodosUsuarios() {
        GlobalExceptionHandler.logInfo("Obteniendo todos los usuarios");
        return usuarioRepository.findAll(); // Llama al repositorio para obtener todos los usuarios
    }
    
    
    public Usuario crearUsuario(Usuario usuario) {
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        GlobalExceptionHandler.logInfo("Guardando usuario en base de datos: " + usuario.getUsername());
        return usuarioRepository.save(usuario);
    }
    
    public Optional<Usuario> obtenerUsuario(Long id) {
        GlobalExceptionHandler.logInfo("Obteniendo usuario con ID: " + id);
        return usuarioRepository.findById(id);
    }
    
    public Optional<Usuario> modificarUsuario(Long id, Usuario usuarioActualizado) {
        return usuarioRepository.findById(id).map(usuario -> {
            usuario.setUsername(usuarioActualizado.getUsername());
            usuario.setPassword(passwordEncoder.encode(usuarioActualizado.getPassword()));
            usuario.setRole(usuarioActualizado.getRole());
            GlobalExceptionHandler.logInfo("Usuario con ID " + id + " actualizado correctamente.");
            return usuarioRepository.save(usuario);
        });
    }
    
    public void eliminarUsuario(Long id) {
        GlobalExceptionHandler.logWarning("Eliminando usuario con ID: " + id);
        usuarioRepository.deleteById(id);
    }
    
    public Optional<Usuario> buscarPorUsername(String username) {
        GlobalExceptionHandler.logInfo("Buscando usuario por username: " + username);
        return usuarioRepository.findByUsername(username);
    }
}

