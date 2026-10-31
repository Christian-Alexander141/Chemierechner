package gui;

/**
 * Ein Fehler, dessen Text dem Benutzer angezeigt wird.
 *
 * Beispiel in einer Button-Aktion:
 *     if (pH < 0) throw new Fehlermeldung("Der pH-Wert darf nicht negativ sein.");
 */
public class Fehlermeldung extends RuntimeException {

    public Fehlermeldung(String text) {
        super(text);
    }
}
