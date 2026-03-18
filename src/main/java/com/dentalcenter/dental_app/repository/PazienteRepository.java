package com.dentalcenter.dental_app.repository;

import com.dentalcenter.dental_app.model.Paziente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PazienteRepository extends JpaRepository<Paziente, Long> {
    
    // Ricerca ottimizzata direttamente lato Database
    @Query("SELECT p FROM Paziente p WHERE LOWER(p.nome) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(p.cognome) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(p.codiceFiscale) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Paziente> cercaPerKeyword(@Param("keyword") String keyword);
    
}