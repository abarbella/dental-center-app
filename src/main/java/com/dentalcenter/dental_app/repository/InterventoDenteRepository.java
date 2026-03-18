package com.dentalcenter.dental_app.repository;

import com.dentalcenter.dental_app.model.InterventoDente;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface InterventoDenteRepository extends JpaRepository<InterventoDente, Long> {
    // Fondamentale: recupera tutti i lavori di un singolo paziente
    List<InterventoDente> findByPazienteId(Long pazienteId);
}