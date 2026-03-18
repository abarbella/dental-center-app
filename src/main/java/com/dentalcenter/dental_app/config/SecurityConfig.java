package com.dentalcenter.dental_app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                // Risorse statiche e pagine di utilità accessibili senza login
                .requestMatchers("/login", "/genera-accessi", "/css/**", "/js/**", "/images/**", "/favicon.ico", 
                                 "/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html", "/error").permitAll()
                
                // ESCLUSIVE MEDICO (Amministrazione e Finanza)
                .requestMatchers("/contabilita/**", "/chiusure/**").hasRole("MEDICO") 
                
                // GESTIONE CLINICA CONDIVISA (Medico e Staff)
                .requestMatchers("/home", "/calendario", "/pazienti/**", "/appuntamenti/**").hasAnyRole("MEDICO", "STAFF")
                
                // AREA RISERVATA PAZIENTE
                .requestMatchers("/paziente/**").hasRole("PAZIENTE")
                
                // Qualsiasi altra richiesta richiede autenticazione
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .successHandler((request, response, authentication) -> {
                    String roles = authentication.getAuthorities().toString();
                    
                    // Logica di redirect basata sul ruolo
                    if (roles.contains("ROLE_MEDICO") || roles.contains("ROLE_STAFF")) {
                        response.sendRedirect("/home");
                    } else if (roles.contains("ROLE_PAZIENTE")) {
                        response.sendRedirect("/paziente/home");
                    } else {
                        response.sendRedirect("/login?error");
                    }
                })
                .permitAll()
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/login?logout")
                .deleteCookies("JSESSIONID")
                .invalidateHttpSession(true)
                .permitAll()
            );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}