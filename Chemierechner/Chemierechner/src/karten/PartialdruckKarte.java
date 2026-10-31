package karten;

import Berechnungen.Partialdruck;
import gui.Formelkarte;

public class PartialdruckKarte extends Formelkarte {

    public PartialdruckKarte() {
        super("Partialdruck");
        beschreibung("p = x · p_ges  –  Druck einer Komponente in einem Gasgemisch");
        eingabe("p", "Gesamtdruck", "bar");
        eingabe("x", "Stoffmengenanteil (0 – 1)");
        ergebnis("Partialdruck", "bar",
                w -> new Partialdruck().berechnePartialdruck(w.get("p"), w.get("x")));
    }
}
