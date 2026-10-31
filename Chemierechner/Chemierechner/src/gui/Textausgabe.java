package gui;

import javafx.scene.Node;
import javafx.scene.control.TextArea;

/** Mehrzeiliger Textbereich zur Ausgabe. Der Text kann markiert und kopiert werden. */
public class Textausgabe {

    private final TextArea bereich = new TextArea();

    Textausgabe() {
        bereich.setEditable(false);
        bereich.setWrapText(true);
        bereich.setPrefRowCount(12);
    }

    public String text() {
        return bereich.getText();
    }

    public void setText(String text) {
        bereich.setText(text);
    }

    /** Hängt Text am Ende an. */
    public void anhaengen(String text) {
        bereich.appendText(text);
    }

    public void leeren() {
        bereich.clear();
    }

    Node knoten() {
        return bereich;
    }
}
