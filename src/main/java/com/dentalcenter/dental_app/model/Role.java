package com.dentalcenter.dental_app.model;

public enum Role {
    MEDICO,      // Admin: Accesso totale (clinico e finanziario)
    STAFF,  // Staff: Accesso agenda e gestione fatture fornitori
    PAZIENTE     // User: Accesso alle proprie prenotazioni
}