package com.dentalcenter.dental_app.controller;

import com.dentalcenter.dental_app.model.Paziente;
import com.dentalcenter.dental_app.repository.PazienteRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pazienti")
public class PazienteRestController {

    private final PazienteRepository pazienteRepository;

    public PazienteRestController(PazienteRepository pazienteRepository) {
        this.pazienteRepository = pazienteRepository;
    }

    // Endpoint per ottenere tutti i pazienti (formato JSON)
    @GetMapping
    public List<Paziente> getAllPazienti() {
        return pazienteRepository.findAll();
    }

    // Endpoint per ottenere un singolo paziente per ID
    @GetMapping("/{id}")
    public Paziente getPazienteById(@PathVariable Long id) {
        return pazienteRepository.findById(id).orElse(null);
    }
}