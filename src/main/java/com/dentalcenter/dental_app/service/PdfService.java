package com.dentalcenter.dental_app.service;

import com.dentalcenter.dental_app.model.InterventoDente;
import com.dentalcenter.dental_app.model.Paziente;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class PdfService {

    public void generaReportPaziente(HttpServletResponse response, Paziente paziente, List<InterventoDente> interventi) throws IOException {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();

        // Font personalizzati
        Font fontTitolo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, Color.BLUE);
        Font fontSottoTitolo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, Color.DARK_GRAY);
        Font fontAllerta = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, Color.RED);

        // Intestazione
        Paragraph titolo = new Paragraph("DentalCenter - Scheda Clinica Paziente", fontTitolo);
        titolo.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(titolo);
        document.add(new Paragraph(" ")); // Spazio

        // Sezione Anagrafica
        document.add(new Paragraph("DATI ANAGRAFICI", fontSottoTitolo));
        document.add(new Paragraph("Paziente: " + paziente.getCognome() + " " + paziente.getNome()));
        document.add(new Paragraph("Codice Fiscale: " + paziente.getCodiceFiscale()));
        document.add(new Paragraph("Email: " + paziente.getEmail() + " | Tel: " + paziente.getTelefono()));
        document.add(new Paragraph(" "));

        // Sezione Anamnesi (Quadro Clinico)
        document.add(new Paragraph("QUADRO CLINICO / ANAMNESI", fontSottoTitolo));
        document.add(new Paragraph("Gruppo Sanguigno: " + (paziente.getGruppoSanguigno() != null ? paziente.getGruppoSanguigno() : "N.D.")));
        
        Paragraph pAllergie = new Paragraph("ALLERGIE: " + (paziente.getAllergie() != null ? paziente.getAllergie() : "Nessuna"), fontAllerta);
        document.add(pAllergie);
        
        document.add(new Paragraph("Patologie: " + (paziente.getPatologie() != null ? paziente.getPatologie() : "Nessuna")));
        document.add(new Paragraph(" "));

        // Tabella Interventi
        document.add(new Paragraph("CRONOLOGIA INTERVENTI", fontSottoTitolo));
        document.add(new Paragraph(" "));

        PdfPTable table = new PdfPTable(3); // 3 colonne
        table.setWidthPercentage(100);
        
        // Header Tabella
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(Color.LIGHT_GRAY);
        cell.setPadding(5);

        cell.setPhrase(new Phrase("Data"));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Dente"));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Descrizione"));
        table.addCell(cell);

     // Dati interventi protetti contro le date "null"
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        for (InterventoDente i : interventi) {
            String data = (i.getDataIntervento() != null) ? i.getDataIntervento().format(formatter) : "-";
            table.addCell(data);
            table.addCell(String.valueOf(i.getNumeroDente()));
            table.addCell(i.getDescrizione() != null ? i.getDescrizione() : "-");
        }

        document.add(table);
        document.close();
    }
}