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
	        	    // 1. Risorse pubbliche
	        	    .requestMatchers("/login", "/css/**", "/js/**", "/images/**", "/favicon.ico", "/error").permitAll()
	        	    
	        	    // 2. Esclusive Medico (Amministrazione)
	        	    .requestMatchers("/contabilita/**", "/chiusure/**").hasRole("MEDICO") 
	        	    
	        	    // 3. Pagina Home Medico
	        	    .requestMatchers("/home").hasRole("MEDICO")

	        	    // 4. Pagina Home Staff
	        	    .requestMatchers("/home-staff").hasRole("STAFF")

	        	    // 5. Permetti a TUTTI (anche Pazienti) di SALVARE un appuntamento
	        	    .requestMatchers("/appuntamenti/salva").authenticated() 
	        	    
	        	    // 6. Tutto il resto della gestione appuntamenti (lista, elimina, modifica) SOLO Medico e Staff
	        	    .requestMatchers("/appuntamenti/**", "/pazienti/**", "/calendario").hasAnyRole("MEDICO", "STAFF")
	        	    
	        	    // 7. Area Protetta Paziente
	        	    .requestMatchers("/paziente/**").hasRole("PAZIENTE")
	        	    
	        	    .anyRequest().authenticated()
	        	)
	        .formLogin(form -> form
	            .loginPage("/login")
	            .loginProcessingUrl("/login")
	            .successHandler((request, response, authentication) -> {
	                String roles = authentication.getAuthorities().toString();
	                
	                // RE-INDIRIZZAMENTO DOPO IL LOGIN
	                if (roles.contains("ROLE_MEDICO")) {
	                    response.sendRedirect("/home"); 
	                } else if (roles.contains("ROLE_STAFF")) {
	                    response.sendRedirect("/home-staff"); 
	                } else if (roles.contains("ROLE_PAZIENTE")) {
	                    // QUI C'ERA L'ERRORE: l'URL corretto è /paziente/home
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