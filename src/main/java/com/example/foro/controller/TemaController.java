package com.example.foro.controller;

import com.example.foro.dto.ComentarioDTO;
import com.example.foro.exception.GlobalExceptionHandler;
import com.example.foro.repository.TemaRepository;
import com.example.foro.repository.CategoriaRepository;
import com.example.foro.repository.UsuarioRepository;
import com.example.foro.repository.ComentarioRepository;
import com.example.foro.model.Tema;
import com.example.foro.model.Usuario;
import com.example.foro.model.Categoria;
import com.example.foro.model.Comentario;
import com.example.foro.service.TemaService;
import jakarta.validation.Valid;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/temas")
public class TemaController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private TemaService temaService;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private TemaRepository temaRepository;

    @Autowired
    private ComentarioRepository comentarioRepository;

    @PostMapping("/crear")
    public ResponseEntity<Tema> crearTema(@Valid @RequestBody Tema tema) {
        // Buscar y cargar el usuario desde la base de datos
        Usuario usuario = usuarioRepository.findById(tema.getUsuario().getId())
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    
        // Buscar y cargar la categoría desde la base de datos
        Categoria categoria = categoriaRepository.findById(tema.getCategoria().getId())
            .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
    
        // Asignar usuario y categoría al tema
        tema.setUsuario(usuario);
        tema.setCategoria(categoria);
    
        // Guardar el tema
        Tema nuevoTema = temaRepository.save(tema);
    
        // Volver a recuperar el tema con todos los datos completos
        Tema temaGuardado = temaRepository.findById(nuevoTema.getId())
            .orElseThrow(() -> new RuntimeException("Error al recuperar el tema"));
    
        return ResponseEntity.ok(temaGuardado);
    }
    

    @GetMapping
    public List<Tema> listarTemas() {
        GlobalExceptionHandler.logInfo("Listando todos los temas");
        return temaService.listarTemas();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tema> obtenerTema(@PathVariable Long id) {
        Tema tema = temaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Tema no encontrado"));

        // Forzar la carga de los comentarios
        Hibernate.initialize(tema.getComentarios());

        return ResponseEntity.ok(tema);
    }

    @PostMapping("/{id}/comentar")
    public ResponseEntity<ComentarioDTO> comentarTema(@PathVariable Long id,
                                                      @RequestBody ComentarioDTO comentarioDto) {

        Tema tema = temaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Tema no encontrado"));

        Comentario comentario = new Comentario();
        comentario.setContenido(comentarioDto.getContenido());
        comentario.setTema(tema);
    
        if (comentarioDto.getUsuarioId() != null) {
            Usuario usuario = usuarioRepository.findById(comentarioDto.getUsuarioId())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            comentario.setUsuario(usuario);
        }

        Comentario comentarioGuardado = comentarioRepository.save(comentario);
    
    
        ComentarioDTO respuestaDto = new ComentarioDTO();
        respuestaDto.setId(comentarioGuardado.getId());
        respuestaDto.setContenido(comentarioGuardado.getContenido());
        if (comentarioGuardado.getUsuario() != null) {
            respuestaDto.setUsuarioId(comentarioGuardado.getUsuario().getId());
        }
    
        return ResponseEntity.ok(respuestaDto);
    }
    
    
    

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminarTema(@PathVariable Long id) {
        GlobalExceptionHandler.logWarning("Intentando eliminar tema con ID: " + id);
        temaService.eliminarTema(id);
        GlobalExceptionHandler.logInfo("Tema con ID " + id + " eliminado correctamente.");
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/modificar/{id}")
    public ResponseEntity<Tema> modificarTema(@PathVariable Long id, @Valid @RequestBody Tema temaActualizado) {
    Tema temaExistente = temaRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Tema no encontrado"));

    temaExistente.setTitulo(temaActualizado.getTitulo());
    temaExistente.setContenido(temaActualizado.getContenido());

    Usuario usuario = usuarioRepository.findById(temaActualizado.getUsuario().getId())
        .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    temaExistente.setUsuario(usuario);

    Categoria categoria = categoriaRepository.findById(temaActualizado.getCategoria().getId())
        .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
    temaExistente.setCategoria(categoria);

    Tema temaModificado = temaRepository.save(temaExistente);

    return ResponseEntity.ok(temaModificado);
}

}
