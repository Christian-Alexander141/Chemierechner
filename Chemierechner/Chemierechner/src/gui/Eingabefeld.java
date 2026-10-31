package gui;

import javafx.scene.Node;
import javafx.scene.control.TextField;

/** Einzeiliges Eingabefeld für Text. */
public class Eingabefeld {

    private final Karte karte;
    private final TextField feld = new TextField();

    Eingabefeld(Karte karte, String platzhalter) {
        this.karte = karte;
        feld.setPromptText(platzhalter);
    }

    /** Der eingegebene Text ohne Leerzeichen am Anfang und Ende. */
    public String text() {
        return feld.getText().trim();
    }

    public void setText(String text) {
        feld.setText(text);
    }

    public void leeren() {
        feld.clear();
    }

    /** Setzt den Cursor in dieses Feld. */
    public void fokus() {
        feld.requestFocus();
    }

    /** Aktion, die beim Drücken der Enter-Taste in diesem Feld ausgeführt wird. */
    public void beiEnter(Runnable aktion) {
        feld.setOnAction(e -> karte.ausfuehren(aktion));
    }

    Node knoten() {
        return feld;
    }
}
