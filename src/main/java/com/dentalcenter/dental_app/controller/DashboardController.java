package com.dentalcenter.dental_app.controller;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.dentalcenter.dental_app.model.*;
import com.dentalcenter.dental_app.repository.*;
import com.dentalcenter.dental_app.service.PdfService;

import jakarta.servlet.http.HttpServletResponse;

@Controller
public class DashboardController {

    private final PazienteRepository pazienteRepository;
    private final AppuntamentoRepository appuntamentoRepository;
    private final MovimentoContabileRepository movimentoRepository;
    private final ChiusuraStudioRepository chiusuraRepository;
    private final InterventoDenteRepository interventoRepository;
    private final PdfService pdfService;
    private final UserRepository userRepository;
    private final MedicoRepository medicoRepository;
    private final PasswordEncoder passwordEncoder;

    public DashboardController(PazienteRepository pazienteRepository, 
                               AppuntamentoRepository appuntamentoRepository,
                               MovimentoContabileRepository movimentoRepository,
                               ChiusuraStudioRepository chiusuraRepository,
                               InterventoDenteRepository interventoRepository,
                               PdfService pdfService,
                               UserRepository userRepository, 
                               MedicoRepository medicoRepository, 
                               PasswordEncoder passwordEncoder) {
        this.pazienteRepository = pazienteRepository;
        this.appuntamentoRepository = appuntamentoRepository;
        this.movimentoRepository = movimentoRepository;
        this.chiusuraRepository = chiusuraRepository;
        this.interventoRepository = interventoRepository;
        this.pdfService = pdfService;
        this.userRepository = userRepository;
        this.medicoRepository = medicoRepository; 
        this.passwordEncoder = passwordEncoder;
    }

    // --- DASHBOARD MEDICO (HOME) ---
    @GetMapping("/home")
    public String home(Model model, Principal principal) { 
        LocalDate oggi = LocalDate.now();
        LocalDateTime oraAttuale = LocalDateTime.now();
        LocalDate traUnaSettimana = oggi.plusDays(7);

        // --- LOGICA TITOLO MEDICO CON SPECIALIZZAZIONE SEPARATA ---
        String salutoNome = "Bentornato, Dottore";
        String salutoSpec = "Gestione quotidiana dello studio.";
        
        if (principal != null) {
            String email = principal.getName();
            User user = userRepository.findByEmail(email);
            if (user != null) {
                Medico medico = medicoRepository.findAll().stream()
                        .filter(m -> m.getUser() != null && m.getUser().getId().equals(user.getId()))
                        .findFirst()
                        .orElse(null);
                
                if (medico != null) {
                    salutoNome = "Bentornato, Dott. " + medico.getCognome();
                    if (medico.getSpecializzazione() != null && !medico.getSpecializzazione().isEmpty()) {
                        salutoSpec = medico.getSpecializzazione();
                    }
                }
            }
        }
        model.addAttribute("salutoNome", salutoNome);
        model.addAttribute("salutoSpec", salutoSpec);
        // ----------------------------------------------------------

        model.addAttribute("dataOggi", oggi.format(DateTimeFormatter.ofPattern("dd MMMM yyyy")));
        model.addAttribute("totalePazienti", pazienteRepository.count());
        
        List<Appuntamento> tuttiApp = appuntamentoRepository.findAll();
        
        // --- LOGICA AGENDA RAPIDA ---
        List<Appuntamento> prossimiAppuntamenti = tuttiApp.stream()
                .filter(a -> a.getDataOra() != null && a.getDataOra().isAfter(oraAttuale))
                .sorted((a1, a2) -> a1.getDataOra().compareTo(a2.getDataOra()))
                .limit(5)
                .collect(Collectors.toList());
                
        model.addAttribute("prossimiAppuntamenti", prossimiAppuntamenti);

        long appuntamentiOggi = tuttiApp.stream()
                .filter(a -> a.getDataOra() != null && a.getDataOra().toLocalDate().equals(oggi))
                .count();

        model.addAttribute("countOggi", appuntamentiOggi);
        
        List<InterventoDente> richiamiFiltrati = interventoRepository.findAll().stream()
                .filter(i -> i.getDataRichiamo() != null)
                .filter(i -> !i.getDataRichiamo().isBefore(oggi) && !i.getDataRichiamo().isAfter(traUnaSettimana))
                .collect(Collectors.toList());
                
        model.addAttribute("richiami", richiamiFiltrati); 
        
        return "home";
    }

    // --- DASHBOARD STAFF ---
    @GetMapping("/home-staff")
    public String homeStaff(Model model) {
        LocalDate oggi = LocalDate.now();
        
        // 1. Prendi tutti i pazienti per la rubrica
        List<Paziente> tuttiPazienti = pazienteRepository.findAll();
        
        // 2. Prendi TUTTI gli appuntamenti e ordinali per data/ora
        List<Appuntamento> tuttiAppuntamentiOrdinati = appuntamentoRepository.findAll()
                .stream()
                .filter(a -> a.getDataOra() != null)
                .sorted((a1, a2) -> a1.getDataOra().compareTo(a2.getDataOra()))
                .collect(Collectors.toList());

        // 3. Filtra quelli di oggi 
        List<Appuntamento> appuntamentiOggi = tuttiAppuntamentiOrdinati.stream()
                .filter(a -> a.getDataOra().toLocalDate().equals(oggi))
                .collect(Collectors.toList());

        // 4. Passa le liste ordinate al modello
        model.addAttribute("pazienti", tuttiPazienti);
        model.addAttribute("appuntamenti", tuttiAppuntamentiOrdinati); 
        model.addAttribute("appuntamentiOggi", appuntamentiOggi);       
        
        return "home-staff";
    }

    // --- AREA PAZIENTE ---
    @GetMapping("/paziente/home")
    public String homePaziente(Model model, Principal principal) {
        String email = principal.getName();
        Paziente p = pazienteRepository.findAll().stream()
                .filter(paz -> email.equalsIgnoreCase(paz.getEmail()))
                .findFirst()
                .orElse(null);

        if (p == null) return "redirect:/login?error=notfound";

        model.addAttribute("paziente", p);
        
        List<Appuntamento> iMieiAppuntamenti = appuntamentoRepository.findAll().stream()
                .filter(a -> a.getPaziente() != null && a.getPaziente().getId().equals(p.getId()))
                .collect(Collectors.toList());
        
        model.addAttribute("mieiAppuntamenti", iMieiAppuntamenti);
        model.addAttribute("tuttiAppuntamenti", appuntamentoRepository.findAll());
        model.addAttribute("chiusure", chiusuraRepository.findAll());

        return "area-paziente";
    }

    @PostMapping("/area-paziente/annulla")
    public String annullaAppuntamentoPaziente(@RequestParam("id") Long id, Principal principal) {
        Appuntamento app = appuntamentoRepository.findById(id).orElse(null);
        
        if (app != null && app.getPaziente() != null) {
            String emailLoggato = principal.getName();
            if (app.getPaziente().getEmail().equalsIgnoreCase(emailLoggato)) {
                appuntamentoRepository.deleteById(id);
                return "redirect:/paziente/home?deleted=true";
            }
        }
        return "redirect:/paziente/home?error=unauthorized";
    }

    // --- GESTIONE PAZIENTI (MEDICO/STAFF) ---
    @GetMapping("/pazienti")
    public String elencoPazienti(Model model) {
        model.addAttribute("pazienti", pazienteRepository.findAll());
        return "pazienti";
    }

    @GetMapping("/pazienti/cerca")
    public String cercaPazienti(@RequestParam("keyword") String keyword, Model model) {
        // Ora usiamo il metodo ottimizzato del Repository!
        List<Paziente> risultati = pazienteRepository.cercaPerKeyword(keyword);
        model.addAttribute("pazienti", risultati);
        return "pazienti";
    }

    @GetMapping("/pazienti/nuovo")
    public String nuovoPaziente(Model model) {
        model.addAttribute("paziente", new Paziente());
        return "nuovo-paziente";
    }

    @PostMapping("/pazienti/salva")
    public String salvaPaziente(@ModelAttribute Paziente paziente) {
        pazienteRepository.save(paziente);
        
        if (paziente.getEmail() != null && userRepository.findByEmail(paziente.getEmail()) == null) {
            User nuovoUtente = new User();
            nuovoUtente.setEmail(paziente.getEmail());
            String passwordDefault = (paziente.getCodiceFiscale() != null) ? paziente.getCodiceFiscale().toUpperCase() : "Dental123!";
            nuovoUtente.setPassword(passwordEncoder.encode(passwordDefault));
            nuovoUtente.setRole(Role.PAZIENTE); 
            userRepository.save(nuovoUtente);
        }
        return "redirect:/pazienti";
    }

    @GetMapping("/pazienti/modifica/{id}")
    public String modificaPaziente(@PathVariable Long id, Model model) {
        Paziente p = pazienteRepository.findById(id).orElse(new Paziente());
        model.addAttribute("paziente", p);
        return "nuovo-paziente";
    }

    @GetMapping("/pazienti/elimina/{id}")
    public String eliminaPaziente(@PathVariable Long id) {
        pazienteRepository.deleteById(id);
        return "redirect:/pazienti";
    }

    // --- CARTELLA CLINICA E PDF ---
    @GetMapping("/pazienti/cartella/{id}")
    public String cartellaPaziente(@PathVariable Long id, Model model) {
        Paziente p = pazienteRepository.findById(id).orElse(new Paziente());
        if (p.getPacemaker() == null) p.setPacemaker(false);
        
        List<InterventoDente> interventi = interventoRepository.findAll().stream()
                .filter(i -> i.getPaziente() != null && i.getPaziente().getId().equals(id))
                .collect(Collectors.toList());
                
        List<MovimentoContabile> movimenti = movimentoRepository.findAll().stream()
                .filter(m -> m.getPaziente() != null && m.getPaziente().getId().equals(id))
                .collect(Collectors.toList());

        double totaleLavori = interventi.stream().mapToDouble(i -> i.getCosto() != null ? i.getCosto() : 0.0).sum();
        double totaleVersato = movimenti.stream().mapToDouble(m -> m.getImporto() != null ? m.getImporto() : 0.0).sum();
        
        model.addAttribute("paziente", p);
        model.addAttribute("interventi", interventi);
        model.addAttribute("movimenti", movimenti); 
        model.addAttribute("totaleLavori", String.format("%.2f", totaleLavori).replace(",", "."));
        model.addAttribute("totaleVersato", String.format("%.2f", totaleVersato).replace(",", "."));
        model.addAttribute("saldoTotale", String.format("%.2f", totaleLavori - totaleVersato).replace(",", "."));
        
        return "cartella-paziente";
    }

    @GetMapping("/pazienti/cartella/{id}/pdf")
    public void scaricaPdfCartella(@PathVariable Long id, HttpServletResponse response) throws IOException {
        Paziente p = pazienteRepository.findById(id).orElseThrow();
        List<InterventoDente> interventi = interventoRepository.findAll().stream()
                .filter(i -> i.getPaziente() != null && i.getPaziente().getId().equals(id))
                .collect(Collectors.toList());

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=Scheda_Clinica_" + p.getCognome() + ".pdf");
        pdfService.generaReportPaziente(response, p, interventi);
    }

    @PostMapping("/pazienti/cartella/salva-intervento")
    public String salvaIntervento(@RequestParam("pazienteId") Long pazienteId, @RequestParam("numeroDente") Integer numeroDente,
                                  @RequestParam("descrizione") String descrizione, @RequestParam("costoIntervento") Double costoIntervento,
                                  @RequestParam("giorniRichiamo") Integer giorniRichiamo) {
        Paziente p = pazienteRepository.findById(pazienteId).orElseThrow();
        InterventoDente intervento = new InterventoDente();
        intervento.setPaziente(p);
        intervento.setNumeroDente(numeroDente);
        intervento.setDescrizione(descrizione);
        intervento.setCosto(costoIntervento);
        intervento.setDataIntervento(LocalDate.now()); 
        if (giorniRichiamo > 0) intervento.setDataRichiamo(LocalDate.now().plusDays(giorniRichiamo));
        interventoRepository.save(intervento);
        return "redirect:/pazienti/cartella/" + pazienteId;
    }

    @PostMapping("/pazienti/cartella/registra-pagamento")
    public String registraPagamento(@RequestParam("pazienteId") Long pazienteId, @RequestParam("importo") Double importo,
                                    @RequestParam("descrizione") String descrizione) {
        Paziente p = pazienteRepository.findById(pazienteId).orElseThrow();
        MovimentoContabile mov = new MovimentoContabile();
        mov.setPaziente(p);
        mov.setImporto(importo);
        mov.setDescrizione(descrizione);
        mov.setData(LocalDate.now());
        movimentoRepository.save(mov);
        return "redirect:/pazienti/cartella/" + pazienteId;
    }

    // --- AGENDA E CALENDARIO ---
    @GetMapping("/calendario")
    public String calendario(Model model) {
        model.addAttribute("appuntamenti", appuntamentoRepository.findAll());
        model.addAttribute("pazienti", pazienteRepository.findAll());
        model.addAttribute("chiusure", chiusuraRepository.findAll());
        return "calendario";
    }

    @PostMapping("/appuntamenti/salva")
    public String salvaAppuntamento(@RequestParam("pazienteId") Long pazienteId, @RequestParam("motivo") String motivo,
                                    @RequestParam("dataOra") String dataOraStr, @RequestParam("dataFine") String dataFineStr,
                                    Authentication authentication) {
        Paziente p = pazienteRepository.findById(pazienteId).orElseThrow();
        Appuntamento appuntamento = new Appuntamento();
        appuntamento.setPaziente(p);
        appuntamento.setMotivo(motivo);
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
            appuntamento.setDataOra(LocalDateTime.parse(dataOraStr, formatter));
            appuntamento.setDataFine(LocalDateTime.parse(dataFineStr, formatter));
        } catch (Exception e) { System.err.println("Errore parsing date: " + e.getMessage()); }
        
        appuntamentoRepository.save(appuntamento);

        if (authentication != null) {
            String ruoli = authentication.getAuthorities().toString();
            if (ruoli.contains("ROLE_PAZIENTE")) {
                return "redirect:/paziente/home?success=prenotato";
            } else if (ruoli.contains("ROLE_STAFF")) {
                return "redirect:/home-staff?success=prenotato";
            }
        }
        return "redirect:/calendario";
    }

    @GetMapping("/appuntamenti/elimina/{id}")
    public String eliminaAppuntamento(@PathVariable Long id, Authentication authentication) {
        appuntamentoRepository.deleteById(id);
        if (authentication != null) {
            String ruoli = authentication.getAuthorities().toString();
            if (ruoli.contains("ROLE_STAFF")) return "redirect:/home-staff?deleted=true";
        }
        return "redirect:/calendario";
    }

    @PostMapping("/chiusure/salva")
    public String salvaChiusura(@RequestParam("motivo") String motivo, @RequestParam("inizio") String inizioStr, @RequestParam("fine") String fineStr) {
        ChiusuraStudio chiusura = new ChiusuraStudio();
        chiusura.setMotivo(motivo);
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
            chiusura.setInizio(LocalDateTime.parse(inizioStr, formatter));
            chiusura.setFine(LocalDateTime.parse(fineStr, formatter));
        } catch (Exception e) { System.err.println("Errore parsing ferie: " + e.getMessage()); }
        chiusuraRepository.save(chiusura);
        return "redirect:/calendario";
    }

    @GetMapping("/chiusure/elimina/{id}")
    public String eliminaChiusura(@PathVariable Long id) {
        chiusuraRepository.deleteById(id);
        return "redirect:/calendario";
    }

    // --- CONTABILITÀ ---
    @GetMapping("/contabilita")
    public String contabilitaGenerale(Model model) {
        return caricaPaginaContabilita(model, false);
    }

    @PostMapping("/contabilita/login")
    public String sbloccaContabilita(@RequestParam("pin") String pin, Model model) {
        if ("1234".equals(pin)) return caricaPaginaContabilita(model, true); 
        model.addAttribute("error", true);
        return caricaPaginaContabilita(model, false);
    }

    private String caricaPaginaContabilita(Model model, boolean sbloccato) {
        List<MovimentoContabile> movimenti = movimentoRepository.findAll();
        double entrate = movimenti.stream().filter(m -> m.getImporto() != null && m.getImporto() > 0).mapToDouble(MovimentoContabile::getImporto).sum();
        double uscite = movimenti.stream().filter(m -> m.getImporto() != null && m.getImporto() < 0).mapToDouble(m -> Math.abs(m.getImporto())).sum();
        
        List<Double> entrateMensili = new ArrayList<>(Collections.nCopies(12, 0.0));
        List<Double> usciteMensili = new ArrayList<>(Collections.nCopies(12, 0.0));
        
        int annoCorrente = LocalDate.now().getYear();

        for (MovimentoContabile m : movimenti) {
            if (m.getData() != null && m.getData().getYear() == annoCorrente) {
                int indiceMese = m.getData().getMonthValue() - 1;
                if (m.getImporto() > 0) entrateMensili.set(indiceMese, entrateMensili.get(indiceMese) + m.getImporto());
                else usciteMensili.set(indiceMese, usciteMensili.get(indiceMese) + Math.abs(m.getImporto()));
            }
        }
        
        model.addAttribute("movimenti", movimenti);
        model.addAttribute("entrate", entrate);
        model.addAttribute("uscite", uscite);
        model.addAttribute("accessGranted", sbloccato);
        model.addAttribute("entrateMensili", entrateMensili);
        model.addAttribute("usciteMensili", usciteMensili);
        
        return "contabilita";
    }

    @PostMapping("/contabilita/salva-uscita")
    public String salvaUscita(@RequestParam("importo") Double importo, @RequestParam(value = "fornitore", required = false) String fornitore,
                              @RequestParam("descrizione") String descrizione, @RequestParam("tipo") String tipo) {
        MovimentoContabile uscita = new MovimentoContabile();
        uscita.setImporto(-Math.abs(importo)); 
        uscita.setFornitore(fornitore);
        uscita.setDescrizione(descrizione);
        uscita.setTipo(tipo); 
        uscita.setData(LocalDate.now()); 
        movimentoRepository.save(uscita);
        return "redirect:/contabilita"; 
    }
}