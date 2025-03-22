package com.example.foro.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;




    @Entity
    @Table(name = "usuarios")
    @JsonIgnoreProperties(value = {"password"}, allowSetters = true)
    public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
        
    @NotBlank(message = "El nombre de usuario no puede estar vacío")
    @Size(min = 3, max = 20, message = "El nombre de usuario debe tener entre 3 y 20 caracteres")
    @Column(unique = true, nullable = false)
    private String username;

    @NotBlank(message = "La contraseña no puede estar vacía")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    @Column(nullable = false)
    private String password;
        
    @NotBlank(message = "El rol no puede estar vacío")
    @Column(nullable = false)
    private String role;
        


    // Getters y Setters
        
    public Long getId() {
        return id;
    }
        
    public void setId(Long id) {
        this.id = id;
    }
        
    public String getUsername() {
        return username;
    }
        
    public void setUsername(String username) {
        this.username = username;
    }
      
    public String getPassword() {
        return password;
    }
        
    public void setPassword(String password) {
        this.password = password;
    }
        
    public String getRole() {
        return role;
    }
        
    public void setRole(String role) {
        this.role = role;
    }
}    

