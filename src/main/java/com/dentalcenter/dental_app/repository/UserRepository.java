package com.dentalcenter.dental_app.repository;

import com.dentalcenter.dental_app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    /**
     * Recupera un utente dal database tramite il suo indirizzo email.
     * Utilizzato da Spring Security in fase di autenticazione e login.
     * * @param email L'indirizzo email dell'utente da cercare.
     * @return L'oggetto User corrispondente, oppure null se non presente.
     */
    User findByEmail(String email);
}