package com.dentalcenter.dental_app.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Paziente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String cognome;
    private String codiceFiscale;
    private String telefono;
    private String email;
    private String citta;

    @Column(columnDefinition = "TEXT")
    private String patologie; 
    @Column(columnDefinition = "TEXT")
    private String allergie;  
    private String gruppoSanguigno;

    @Column(columnDefinition = "TEXT")
    private String farmaci;          
    private String fumo;             
    private Boolean pacemaker;       
    private String gravidanza;       
    
    @Column(columnDefinition = "TEXT")
    private String noteAggiuntive;   

    // --- RELAZIONI CON CASCADE ---
    // Aggiunto orphanRemoval = true per pulire il database all'eliminazione
    
    @OneToMany(mappedBy = "paziente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Appuntamento> appuntamenti = new ArrayList<>();

    @OneToMany(mappedBy = "paziente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InterventoDente> interventi = new ArrayList<>();

    @OneToMany(mappedBy = "paziente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MovimentoContabile> movimenti = new ArrayList<>();

    // RELAZIONE CON USER: Fondamentale per evitare l'errore 500
    @OneToOne(mappedBy = "paziente", cascade = CascadeType.ALL, orphanRemoval = true)
    private User user;

    public Paziente() {}

    // --- GETTER E SETTER ESISTENTI ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getCognome() { return cognome; }
    public void setCognome(String cognome) { this.cognome = cognome; }
    public String getCodiceFiscale() { return codiceFiscale; }
    public void setCodiceFiscale(String codiceFiscale) { this.codiceFiscale = codiceFiscale; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getCitta() { return citta; }
    public void setCitta(String citta) { this.citta = citta; }
    public String getPatologie() { return patologie; }
    public void setPatologie(String patologie) { this.patologie = patologie; }
    public String getAllergie() { return allergie; }
    public void setAllergie(String allergie) { this.allergie = allergie; }
    public String getGruppoSanguigno() { return gruppoSanguigno; }
    public void setGruppoSanguigno(String gruppoSanguigno) { this.gruppoSanguigno = gruppoSanguigno; }
    public String getFarmaci() { return farmaci; }
    public void setFarmaci(String farmaci) { this.farmaci = farmaci; }
    public String getFumo() { return fumo; }
    public void setFumo(String fumo) { this.fumo = fumo; }
    public Boolean getPacemaker() { return pacemaker; }
    public void setPacemaker(Boolean pacemaker) { this.pacemaker = pacemaker; }
    public String getGravidanza() { return gravidanza; }
    public void setGravidanza(String gravidanza) { this.gravidanza = gravidanza; }
    public String getNoteAggiuntive() { return noteAggiuntive; }
    public void setNoteAggiuntive(String noteAggiuntive) { this.noteAggiuntive = noteAggiuntive; }

    public List<Appuntamento> getAppuntamenti() { return appuntamenti; }
    public void setAppuntamenti(List<Appuntamento> appuntamenti) { this.appuntamenti = appuntamenti; }
    public List<InterventoDente> getInterventi() { return interventi; }
    public void setInterventi(List<InterventoDente> interventi) { this.interventi = interventi; }
    public List<MovimentoContabile> getMovimenti() { return movimenti; }
    public void setMovimenti(List<MovimentoContabile> movimenti) { this.movimenti = movimenti; }

    // Getter e Setter per User
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}