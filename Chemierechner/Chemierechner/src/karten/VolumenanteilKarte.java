package karten;

import Berechnungen.Volumenanteil;
import gui.Formelkarte;

public class VolumenanteilKarte extends Formelkarte {

    public VolumenanteilKarte() {
        super("Volumenanteil");
        beschreibung("φ = V_Komponente / V_gesamt");
        eingabe("V", "Volumen Komponente", "mL");
        eingabe("VGes", "Gesamtvolumen", "mL");
        ergebnis("Volumenanteil φ",
                w -> new Volumenanteil().berechneVolumenanteil(w.get("V"), w.get("VGes")));
        ergebnis("in Prozent", "%",
                w -> new Volumenanteil().berechneVolumenanteil(w.get("V"), w.get("VGes")) * 100);
    }
}
