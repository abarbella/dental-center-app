package com.dentalcenter.dental_app.component;

import com.dentalcenter.dental_app.model.*;
import com.dentalcenter.dental_app.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final MedicoRepository medicoRepository; // AGGIUNTO
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, 
                           MedicoRepository medicoRepository, // AGGIUNTO
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.medicoRepository = medicoRepository; // AGGIUNTO
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        // Creiamo l'utente Admin e il profilo Medico solo se il database degli utenti è vuoto
        if (userRepository.count() == 0) {
            System.out.println(">>> Configurazione account amministratore e profilo medico...");

            // 1. Creazione Utente (Credenziali di accesso)
            User admin = new User();
            admin.setEmail("admin@dentalcenter.it");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole(Role.MEDICO);
            userRepository.save(admin);

            // 2. Creazione Profilo Medico (Dati anagrafici e professionali)
            Medico medico = new Medico();
            medico.setNome("Mario");
            medico.setCognome("Rossi");
            medico.setSpecializzazione("Odontoiatria e Protesi Dentaria");
            medico.setUser(admin); // Colleghiamo il profilo all'account creato sopra
            
            medicoRepository.save(medico);

            System.out.println(">>> Account Admin creato: admin@dentalcenter.it / admin123");
            System.out.println(">>> Profilo Medico configurato: Dott. Mario Rossi (" + medico.getSpecializzazione() + ")");
        }
    }
}