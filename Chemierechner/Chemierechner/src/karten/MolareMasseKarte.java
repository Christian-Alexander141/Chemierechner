package karten;

import Berechnungen.molareMasse;
import gui.Formelkarte;

public class MolareMasseKarte extends Formelkarte {

    public MolareMasseKarte() {
        super("Molare Masse");
        beschreibung("M = m / n");
        eingabe("m", "Masse", "g");
        eingabe("n", "Stoffmenge", "mol");
        ergebnis("Molare Masse", "g/mol",
                w -> new molareMasse().berechneMolareMasse(w.get("m"), w.get("n")));
    }
}
