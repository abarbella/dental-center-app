package com.dentalcenter.dental_app.repository;

import com.dentalcenter.dental_app.model.MovimentoContabile;
import com.dentalcenter.dental_app.model.Paziente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MovimentoContabileRepository extends JpaRepository<MovimentoContabile, Long> {
    
    // Trova tutti i movimenti di un paziente specifico (utile per il calcolo in cartella)
    List<MovimentoContabile> findByPaziente(Paziente paziente);
    
    // Trova movimenti per tipologia
    List<MovimentoContabile> findByTipo(String tipo);
}