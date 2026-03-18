package com.dentalcenter.dental_app.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Appuntamento {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime dataOra;  // Data e ora di INIZIO dell'appuntamento
    
    private LocalDateTime dataFine; // Data e ora di FINE
    
    private String motivo;          // Es: "Pulizia", "Controllo"
    
    @ManyToOne
    @JoinColumn(name = "paziente_id")
    private Paziente paziente;

    // --- Getter e Setter ---
    
    public Long getId() { 
        return id; 
    }
    
    public void setId(Long id) { 
        this.id = id; 
    }
    
    public LocalDateTime getDataOra() { 
        return dataOra; 
    }
    
    public void setDataOra(LocalDateTime dataOra) { 
        this.dataOra = dataOra; 
    }
    
    public LocalDateTime getDataFine() { 
        return dataFine; 
    }
    
    public void setDataFine(LocalDateTime dataFine) { 
        this.dataFine = dataFine; 
    }
    
    public String getMotivo() { 
        return motivo; 
    }
    
    public void setMotivo(String motivo) { 
        this.motivo = motivo; 
    }
    
    public Paziente getPaziente() { 
        return paziente; 
    }
    
    public void setPaziente(Paziente paziente) { 
        this.paziente = paziente; 
    }
}