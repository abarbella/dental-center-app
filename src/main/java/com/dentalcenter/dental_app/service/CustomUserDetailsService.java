package com.dentalcenter.dental_app.service;

import com.dentalcenter.dental_app.model.User;
import com.dentalcenter.dental_app.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Cerchiamo l'utente 
        User user = userRepository.findByEmail(email);
        
        if (user == null) {
            throw new UsernameNotFoundException("Utente non trovato con email: " + email);
        }

        // Recuperiamo il nome del ruolo dall'ENUM (es. MEDICO, STAFF)
        String roleName = user.getRole().name();

        return org.springframework.security.core.userdetails.User.withUsername(user.getEmail())
                .password(user.getPassword())
                .roles(roleName) // Passiamo "MEDICO", lui creerà "ROLE_MEDICO"
                .build();
    }
}