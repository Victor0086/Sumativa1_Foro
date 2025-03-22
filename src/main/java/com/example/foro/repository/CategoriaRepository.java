package com.example.foro.repository;

import com.example.foro.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    // Método para encontrar una categoría por su nombre
    Optional<Categoria> findByNombre(String nombre);
}
