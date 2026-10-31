package gui;

import java.util.Map;

/** Die eingegebenen Werte einer Formelkarte, abrufbar über ihr Kürzel. */
public class Werte {

    private final Map<String, Double> werte;

    Werte(Map<String, Double> werte) {
        this.werte = werte;
    }

    /** Wert der Eingabe mit diesem Kürzel, z.B. get("m"). */
    public double get(String kuerzel) {
        Double wert = werte.get(kuerzel);
        if (wert == null) {
            throw new IllegalArgumentException(
                    "Unbekanntes Kürzel \"" + kuerzel + "\" – fehlt ein eingabe(\"" + kuerzel + "\", ...)?");
        }
        return wert;
    }
}
