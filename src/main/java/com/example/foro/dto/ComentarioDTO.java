package com.example.foro.dto;

public class ComentarioDTO {
    private Long id;
    private String contenido;
    private Long usuarioId;

    public ComentarioDTO() {}

    public ComentarioDTO(Long id, String contenido, Long usuarioId) {
        this.id = id;
        this.contenido = contenido;
        this.usuarioId = usuarioId;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getContenido() { return contenido; }
    public void setContenido(String contenido) { this.contenido = contenido; }

    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }
}
