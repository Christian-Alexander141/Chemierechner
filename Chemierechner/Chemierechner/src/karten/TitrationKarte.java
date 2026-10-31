package karten;

import Titrationen.Titration;
import gui.Diagramm;
import gui.Fehlermeldung;
import gui.Karte;
import gui.Tabelle;
import gui.Zahlenfeld;

public class TitrationKarte extends Karte {

    private final Zahlenfeld volumen;
    private final Zahlenfeld pH;
    private final Tabelle messpunkte;
    private final Diagramm kurve;

    public TitrationKarte() {
        super("Titration");
        beschreibung("Messpunkte erfassen – die Titrationskurve aktualisiert sich automatisch.");

        abschnitt("Neuer Messpunkt");
        volumen = zahlenfeld("Zugegebenes Volumen", "mL");
        pH = zahlenfeld("pH-Wert");
        pH.beiEnter(this::messpunktHinzufuegen);
        button("Messpunkt hinzufügen", this::messpunktHinzufuegen);

        abschnitt("Messpunkte");
        messpunkte = tabelle("Volumen (mL)", "pH-Wert");
        button("Alle löschen", () -> messpunkte.leeren());

        abschnitt("Titrationskurve");
        kurve = diagramm("Volumen (mL)", "pH");

        messpunkte.beiAenderung(this::kurveAktualisieren);
    }

    private void messpunktHinzufuegen() {
        double v = volumen.wert();
        double p = pH.wert();
        if (p < 0 || p > 14) {
            throw new Fehlermeldung("Der pH-Wert muss zwischen 0 und 14 liegen.");
        }
        messpunkte.zeileHinzufuegen(v, p);
        volumen.leeren();
        pH.leeren();
        volumen.fokus();
    }

    private void kurveAktualisieren() {
        Titration titration = new Titration(0, 0);
        for (double[] punkt : messpunkte.zeilen()) {
            titration.addpH(punkt[1], punkt[0]);
        }
        kurve.zeichne("Titrationskurve", titration.getKurve());
    }
}
