package karten;

import Berechnungen.Masseanteil;
import gui.Formelkarte;

public class MasseanteilKarte extends Formelkarte {

    public MasseanteilKarte() {
        super("Masseanteil");
        beschreibung("w = m_Komponente / m_gesamt");
        eingabe("m", "Masse Komponente", "g");
        eingabe("mGes", "Gesamtmasse", "g");
        ergebnis("Masseanteil w",
                w -> new Masseanteil().berechneMasseanteil(w.get("m"), w.get("mGes")));
        ergebnis("in Prozent", "%",
                w -> new Masseanteil().berechneMasseanteil(w.get("m"), w.get("mGes")) * 100);
    }
}
