package karten;

import Berechnungen.Stoffmenge;
import gui.Formelkarte;

public class StoffmengeKarte extends Formelkarte {

    public StoffmengeKarte() {
        super("Stoffmenge");
        beschreibung("n = m / M");
        eingabe("m", "Masse", "g");
        eingabe("M", "Molare Masse", "g/mol");
        ergebnis("Stoffmenge", "mol",
                w -> new Stoffmenge().berechneStoffmenge(w.get("m"), w.get("M")));
    }
}
