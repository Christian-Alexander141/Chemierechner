package karten;

import Berechnungen.molaresVolumen;
import gui.Formelkarte;

public class MolaresVolumenKarte extends Formelkarte {

    public MolaresVolumenKarte() {
        super("Molares Volumen");
        beschreibung("Vm = V / n");
        eingabe("V", "Volumen", "L");
        eingabe("n", "Stoffmenge", "mol");
        ergebnis("Molares Volumen", "L/mol",
                w -> new molaresVolumen().berechneMolaresVolumen(w.get("V"), w.get("n")));
    }
}
