package com.dentalcenter.dental_app.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class MovimentoContabile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate data;
    private Double importo;
    private String descrizione;
    
    // Tipi suggeriti: ENTRATA, USCITA_FORNITORE, SPESA_GENERICA
    private String tipo;

    // Campo opzionale per i fornitori
    private String fornitore;

    // Relazione con il paziente: fondamentale per tracciare gli incassi in cartella
    @ManyToOne
    @JoinColumn(name = "paziente_id")
    private Paziente paziente;

    public MovimentoContabile() {}

    // --- GETTER E SETTER ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDate getData() { return data; }
    public void setData(LocalDate data) { this.data = data; }

    public Double getImporto() { return importo; }
    public void setImporto(Double importo) { this.importo = importo; }

    public String getDescrizione() { return descrizione; }
    public void setDescrizione(String descrizione) { this.descrizione = descrizione; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getFornitore() { return fornitore; }
    public void setFornitore(String fornitore) { this.fornitore = fornitore; }

    public Paziente getPaziente() { return paziente; }
    public void setPaziente(Paziente paziente) { this.paziente = paziente; }
}