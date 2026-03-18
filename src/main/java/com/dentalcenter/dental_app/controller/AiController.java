package com.dentalcenter.dental_app.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller per il modulo Dental AI Advisor.
 * NOTA: Il modulo è attualmente in fase di "Technical Preview".
 */
@RestController
@RequestMapping("/api/ai")
public class AiController {

    @GetMapping("/richiamo")
    public ResponseEntity<String> generaTestoRichiamo(
            @RequestParam(value = "nomePaziente", defaultValue = "Paziente") String nomePaziente, 
            @RequestParam(value = "intervento", defaultValue = "controllo") String intervento) {
        
        // Messaggio informativo sullo stato dell'implementazione
        String response = "Gentile Dottore, il modulo 'Dental Advisor AI' è attualmente in fase di sviluppo.\n\n" +
                          "L'interfaccia è pronta per l'integrazione con modelli LLM (OpenAI/Llama), " +
                          "che verrà completata nei prossimi aggiornamenti per garantire la generazione " +
                          "automatica di richiami personalizzati e analisi cliniche avanzate. La ringraziamo per la pazienza.";
        
        return ResponseEntity.ok(response);
    }
}