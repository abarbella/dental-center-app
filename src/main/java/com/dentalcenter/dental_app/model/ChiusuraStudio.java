package com.dentalcenter.dental_app.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class ChiusuraStudio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime inizio;
    private LocalDateTime fine;
    private String motivo; // Es: "Chiuso per Ferie", "Pausa Pranzo", "Studio Chiuso"

    // Costruttori
    public ChiusuraStudio() {}

    // Getter e Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public LocalDateTime getInizio() { return inizio; }
    public void setInizio(LocalDateTime inizio) { this.inizio = inizio; }
    public LocalDateTime getFine() { return fine; }
    public void setFine(LocalDateTime fine) { this.fine = fine; }
    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }
}