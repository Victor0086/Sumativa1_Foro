package com.example.foro.service;

import com.example.foro.repository.ComentarioRepository;
import com.example.foro.repository.CategoriaRepository;
import com.example.foro.exception.GlobalExceptionHandler;
import com.example.foro.model.Categoria;
import com.example.foro.model.Comentario;
import com.example.foro.model.Tema;
import com.example.foro.repository.TemaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import jakarta.annotation.PostConstruct;

@Service
public class TemaService {

    @Autowired
    private TemaRepository temaRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private ComentarioRepository comentarioRepository;

@PostConstruct
    public void crearCategoriasPredefinidas() {
        List<String> nombresCategorias = List.of("Tecnología", "Ciencia", "Deportes", "Arte", "Historia");

        for (String nombre : nombresCategorias) {
            if (categoriaRepository.findByNombre(nombre).isEmpty()) {
                Categoria categoria = new Categoria();
                categoria.setNombre(nombre);
                categoriaRepository.save(categoria);  // Guarda la nueva categoría si no existe
                GlobalExceptionHandler.logInfo("Categoría creada: " + nombre);  
                GlobalExceptionHandler.logInfo("Categoría ya existe: " + nombre);  
            }
        }
    }

    public Optional<Comentario> comentarTema(Long temaId, Comentario comentario) {
        return temaRepository.findById(temaId).map(tema -> {
            comentario.setTema(tema);  // Relaciona el comentario con el tema, no lo guarda nuevamente
            comentario.setUsuario(tema.getUsuario());  // Asume que el usuario es el que realiza el comentario
    
            // Guardar solo el comentario, no el tema ni los comentarios dentro del tema
            GlobalExceptionHandler.logInfo("Agregando comentario al tema ID: " + temaId);
            return comentarioRepository.save(comentario);  // Solo guarda el comentario, no el tema
        });
    }
    


    public Tema crearTema(Tema tema) {
        GlobalExceptionHandler.logInfo("Guardando tema en base de datos: " + tema.getTitulo());
        return temaRepository.save(tema); 
    }
    public List<Tema> listarTemas() {
        GlobalExceptionHandler.logInfo("Listando todos los temas");
        return temaRepository.findAll();
    }

    public Optional<Tema> obtenerTema(Long id) {
        GlobalExceptionHandler.logInfo("Obteniendo tema con ID: " + id);
        return temaRepository.findById(id);
    }


    public void eliminarTema(Long id) {
        GlobalExceptionHandler.logWarning("Eliminando tema con ID: " + id);
        temaRepository.deleteById(id);
        GlobalExceptionHandler.logInfo("Tema con ID " + id + " eliminado correctamente.");
    }
}
