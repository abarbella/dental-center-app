package com.dentalcenter.dental_app.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class InterventoDente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer numeroDente;
    private String descrizione;
    private LocalDate dataIntervento;
    private LocalDate dataRichiamo;
    
    // Costo dell'intervento
    private Double costo;

    @ManyToOne
    @JoinColumn(name = "paziente_id")
    private Paziente paziente;

    public InterventoDente() {}

    // --- GETTER E SETTER ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Integer getNumeroDente() { return numeroDente; }
    public void setNumeroDente(Integer numeroDente) { this.numeroDente = numeroDente; }

    public String getDescrizione() { return descrizione; }
    public void setDescrizione(String descrizione) { this.descrizione = descrizione; }

    public LocalDate getDataIntervento() { return dataIntervento; }
    public void setDataIntervento(LocalDate dataIntervento) { this.dataIntervento = dataIntervento; }

    public LocalDate getDataRichiamo() { return dataRichiamo; }
    public void setDataRichiamo(LocalDate dataRichiamo) { this.dataRichiamo = dataRichiamo; }

    public Double getCosto() { return costo; }
    public void setCosto(Double costo) { this.costo = costo; }

    public Paziente getPaziente() { return paziente; }
    public void setPaziente(Paziente paziente) { this.paziente = paziente; }
}