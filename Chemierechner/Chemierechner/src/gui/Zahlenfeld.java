package gui;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

/** Eingabefeld für eine Zahl. Komma und Punkt werden beide akzeptiert. */
public class Zahlenfeld {

    private final Karte karte;
    private final String beschriftung;
    private final TextField feld = new TextField();
    private final HBox knoten;

    Zahlenfeld(Karte karte, String beschriftung, String einheit) {
        this.karte = karte;
        this.beschriftung = beschriftung;

        knoten = new HBox(feld);
        knoten.getStyleClass().add("feld-gruppe");
        HBox.setHgrow(feld, Priority.ALWAYS);
        if (einheit != null && !einheit.isBlank()) {
            Label label = new Label(einheit);
            label.getStyleClass().add("einheit");
            label.setMaxHeight(Double.MAX_VALUE);
            label.setMinWidth(Label.USE_PREF_SIZE);
            knoten.getChildren().add(label);
            knoten.getStyleClass().add("mit-einheit");
        }
    }

    /** Liefert die eingegebene Zahl oder zeigt eine Fehlermeldung an. */
    public double wert() {
        String text = feld.getText().trim();
        if (text.isEmpty()) {
            throw new Fehlermeldung("Bitte einen Wert für „" + beschriftung + "“ eingeben.");
        }
        try {
            return Zahlenformat.lesen(text);
        } catch (NumberFormatException e) {
            throw new Fehlermeldung("„" + text + "“ ist keine gültige Zahl (" + beschriftung + ").");
        }
    }

    public boolean istLeer() {
        return feld.getText().isBlank();
    }

    public void setWert(double wert) {
        feld.setText(Zahlenformat.fuerEingabe(wert));
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
        return knoten;
    }
}
