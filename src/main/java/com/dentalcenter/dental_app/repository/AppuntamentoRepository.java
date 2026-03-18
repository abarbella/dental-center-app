package com.dentalcenter.dental_app.repository;

import com.dentalcenter.dental_app.model.Appuntamento;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AppuntamentoRepository extends JpaRepository<Appuntamento, Long> {
    // Utile per caricare solo gli appuntamenti del mese corrente
    List<Appuntamento> findAll();
}