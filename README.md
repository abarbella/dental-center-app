# DentalCenter - Sistema Gestionale Odontoiatrico
**Progetto Universitario (PW 16) - Corso di Informatica per le Aziende Digitali (L-31)**

## Descrizione del Progetto
**DentalCenter** è un'applicazione web full-stack API-based sviluppata per rispondere al tema della *"Digitalizzazione dell'impresa del settore sanitario"*. 
Il sistema mira a modernizzare e ottimizzare le operazioni quotidiane di una clinica odontoiatrica, eliminando la gestione cartacea e fornendo un accesso scalabile, sicuro e in tempo reale ai dati sanitari e amministrativi.

L'architettura moderna del software integra un backend RESTful robusto con un'interfaccia utente (UI) intuitiva, facilitando l'interazione tra i professionisti sanitari, lo staff amministrativo e i pazienti.

---

## Funzionalità e Casi d'Uso (Role-Based Access Control)
Il sistema implementa una rigorosa separazione dei privilegi tramite **Spring Security**, definendo tre attori principali:

### 1. Medico (Amministratore)
* **Dashboard Statistiche:** Visione d'insieme su pazienti totali, appuntamenti del giorno e richiami urgenti.
* **Gestione Clinica:** Accesso alle cartelle cliniche dei pazienti (anamnesi, presenza di pacemaker, storico interventi per singolo dente).
* **Area Contabilità:** Visualizzazione completa del bilancio aziendale, tracciamento entrate/uscite e grafici finanziari mensili.

### 2. Staff (Segreteria)
* **Anagrafica Pazienti:** Inserimento, modifica e ricerca ottimizzata dei pazienti nel database.
* **Gestione Agenda:** Inserimento e annullamento di appuntamenti.
* **Chiusure Studio:** Registrazione di ferie e giorni di chiusura per bloccare le prenotazioni.

### 3. Paziente
* **Area Riservata:** Accesso sicuro tramite credenziali personali.
* **Self-Service:** Visualizzazione dei propri appuntamenti (storico e futuri) con possibilità di disdetta autonoma.

---

## Stack Tecnologico e Architettura
Il progetto segue il design pattern **MVC (Model-View-Controller)** ed è strutturato con le seguenti tecnologie:
* **Backend / API:** Java 17, Spring Boot (Spring Web, Spring Data JPA, Spring Security).
* **Database:** MySQL (con autogenerazione e validazione dello schema tramite Hibernate).
* **Frontend:** HTML5, CSS3, JavaScript, Bootstrap 5, Thymeleaf (Server-side rendering).
* **Documentazione API:** OpenAPI 3 / Swagger UI.
* **Strumenti di Sviluppo:** Maven, Git.

---

## Istruzioni per l'Installazione e l'Avvio

**1. Clonare il repository:**
git clone [https://github.com/abarbella/dental-center-app.git](https://github.com/abarbella/dental-center-app.git)

**2. Configurazione Database:**
* Creare un database MySQL vuoto chiamato "dental_db".
* Verificare le credenziali nel file "src/main/resources/application.properties" (username: root, password: root).

**3. Avvio dell'Applicazione:**
* Tramite IDE (Eclipse, IntelliJ) avviando la classe "DentalAppApplication.java".
* Oppure tramite terminale usando Maven con il comando: mvn spring-boot:run

**4. Accesso al Sistema:**
L'applicazione sarà in ascolto sulla porta 8081. Aprire il browser all'indirizzo: http://localhost:8081

---

## Documentazione API (Swagger)
Il backend espone le sue logiche tramite API documentate automaticamente. A server avviato, è possibile ispezionare gli endpoint, i modelli dati e testare le richieste all'indirizzo:
* **Swagger UI:** http://localhost:8081/swagger-ui.html

---

## Autore
**Antonio Rosario Barbella**
*Progetto PW 16 - Laurea in Informatica per le Aziende Digitali (L-31)*
