package gui;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Consumer;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * Basisklasse für jede Karte, also jeden Eintrag in der Seitenleiste.
 *
 * Eine eigene Karte erbt von dieser Klasse und baut ihren Inhalt im Konstruktor
 * aus Bausteinen zusammen: textfeld, zahlenfeld, button, textbereich, tabelle,
 * diagramm. Die Bausteine erscheinen in der Reihenfolge, in der sie erzeugt werden:
 *
 *  - mehrere zahlenfeld()-Aufrufe direkt hintereinander landen zweispaltig nebeneinander
 *  - mehrere button()-Aufrufe direkt hintereinander landen in einer Reihe
 *  - alles andere nimmt die volle Breite ein
 *  - abschnitt("Titel") beginnt einen neuen weißen Kasten
 *
 * Fehler: Wirft eine Aktion (Button-Klick, Enter in einem Feld) eine
 * {@link Fehlermeldung}, wird deren Text unten im Fenster angezeigt.
 * Zahlenfeld.wert() macht das bei ungültigen Eingaben automatisch.
 *
 * Für reine Formeln (Eingaben rein, Ergebnis raus) gibt es die einfachere
 * {@link Formelkarte}.
 */
public abstract class Karte {

    private final String titel;
    private final Label beschreibung = new Label();
    private final VBox abschnitte = new VBox(20);
    private final Meldung meldung = new Meldung();
    private final StackPane ansicht;
    private final List<Button> buttons = new ArrayList<>();
    private boolean beschaeftigt = false;

    // Wohin der nächste Baustein kommt
    private VBox abschnitt;
    private Feldraster feldraster;
    private HBox buttonreihe;

    protected Karte(String titel) {
        this.titel = titel;

        Label ueberschrift = new Label(titel);
        ueberschrift.getStyleClass().add("karte-titel");

        beschreibung.getStyleClass().add("karte-beschreibung");
        beschreibung.setWrapText(true);
        beschreibung.setVisible(false);
        beschreibung.setManaged(false);

        VBox kopf = new VBox(4, ueberschrift, beschreibung);

        VBox seite = new VBox(24, kopf, abschnitte);
        seite.getStyleClass().add("seite");
        seite.setMaxWidth(920);

        VBox zentriert = new VBox(seite);
        zentriert.setAlignment(Pos.TOP_CENTER);

        ScrollPane scroll = new ScrollPane(zentriert);
        scroll.setFitToWidth(true);
        scroll.getStyleClass().add("inhalt-scroll");

        StackPane.setAlignment(meldung, Pos.BOTTOM_CENTER);
        StackPane.setMargin(meldung, new Insets(0, 0, 28, 0));
        ansicht = new StackPane(scroll, meldung);
    }

    // =========================================================
    //  Aufbau – diese Methoden rufst du im Konstruktor auf
    // =========================================================

    /** Kurzer Text unter der Überschrift, z.B. die Formel. */
    protected void beschreibung(String text) {
        beschreibung.setText(text);
        beschreibung.setVisible(true);
        beschreibung.setManaged(true);
    }

    /** Beginnt einen neuen Kasten mit Überschrift. Alle folgenden Bausteine landen darin. */
    protected void abschnitt(String titel) {
        abschnitt = new VBox(16);
        abschnitt.getStyleClass().add("abschnitt");
        if (titel != null && !titel.isBlank()) {
            Label label = new Label(titel);
            label.getStyleClass().add("abschnitt-titel");
            abschnitt.getChildren().add(label);
        }
        abschnitte.getChildren().add(abschnitt);
        feldraster = null;
        buttonreihe = null;
    }

    /** Eingabefeld für eine Zahl, ohne Einheit. */
    protected Zahlenfeld zahlenfeld(String beschriftung) {
        return zahlenfeld(beschriftung, "");
    }

    /** Eingabefeld für eine Zahl, die Einheit steht rechts im Feld. */
    protected Zahlenfeld zahlenfeld(String beschriftung, String einheit) {
        Zahlenfeld feld = new Zahlenfeld(this, beschriftung, einheit);
        if (feldraster == null) {
            Feldraster neu = new Feldraster();
            hinzufuegen(neu);
            feldraster = neu;
        }
        feldraster.hinzufuegen(beschriftet(beschriftung, feld.knoten()));
        return feld;
    }

    /** Eingabefeld für Text über die volle Breite. */
    protected Eingabefeld textfeld(String beschriftung) {
        return textfeld(beschriftung, "");
    }

    /** Eingabefeld für Text; der Platzhalter steht grau im leeren Feld. */
    protected Eingabefeld textfeld(String beschriftung, String platzhalter) {
        Eingabefeld feld = new Eingabefeld(this, platzhalter);
        hinzufuegen(beschriftet(beschriftung, feld.knoten()));
        return feld;
    }

    /** Button, der beim Klicken die Aktion ausführt. */
    protected void button(String text, Runnable aktion) {
        Button button = new Button(text);
        button.setOnAction(e -> ausfuehren(aktion));
        buttons.add(button);
        if (buttonreihe == null) {
            HBox neu = new HBox(10);
            hinzufuegen(neu);
            buttonreihe = neu;
        }
        buttonreihe.getChildren().add(button);
    }

    /** Mehrzeiliges Textfeld zur Ausgabe, z.B. für längere Antworten. */
    protected Textausgabe textbereich() {
        Textausgabe ausgabe = new Textausgabe();
        hinzufuegen(ausgabe.knoten());
        return ausgabe;
    }

    /** Tabelle mit Zahlen; jede Zeile hat so viele Werte wie es Spalten gibt. */
    protected Tabelle tabelle(String... spalten) {
        Tabelle tabelle = new Tabelle(this, spalten);
        hinzufuegen(tabelle.knoten());
        return tabelle;
    }

    /** Liniendiagramm mit Beschriftung der x- und y-Achse. */
    protected Diagramm diagramm(String xAchse, String yAchse) {
        Diagramm diagramm = new Diagramm(xAchse, yAchse);
        hinzufuegen(diagramm.knoten());
        return diagramm;
    }

    // =========================================================
    //  Aktionen – diese Methoden rufst du aus Button-Aktionen auf
    // =========================================================

    /**
     * Führt eine langsame Aufgabe (z.B. Netzwerk) im Hintergrund aus, damit das
     * Fenster nicht einfriert. Danach wird "danach" mit dem Ergebnis aufgerufen.
     * Solange die Aufgabe läuft, sind die Buttons dieser Karte gesperrt.
     */
    protected <T> void imHintergrund(Callable<T> aufgabe, Consumer<T> danach) {
        setBeschaeftigt(true);
        meldung.laeuft("Bitte warten …");
        Thread thread = new Thread(() -> {
            try {
                T ergebnis = aufgabe.call();
                Platform.runLater(() -> {
                    setBeschaeftigt(false);
                    ausfuehren(() -> danach.accept(ergebnis));
                });
            } catch (Exception e) {
                Platform.runLater(() -> {
                    setBeschaeftigt(false);
                    meldung.fehler(fehlertext(e));
                });
            }
        });
        thread.setDaemon(true);
        thread.start();
    }

    /** Zeigt kurz einen Hinweis unten im Fenster an. */
    protected void info(String text) {
        meldung.info(text);
    }

    // =========================================================
    //  Intern – wird vom Grundgerüst benutzt
    // =========================================================

    String titel() {
        return titel;
    }

    Node ansicht() {
        return ansicht;
    }

    /** Fügt einen Knoten über die volle Breite in den aktuellen Abschnitt ein. */
    void hinzufuegen(Node knoten) {
        if (abschnitt == null) {
            abschnitt(null);
        }
        abschnitt.getChildren().add(knoten);
        feldraster = null;
        buttonreihe = null;
    }

    /** Führt eine Aktion aus und zeigt Fehler als Meldung an. */
    void ausfuehren(Runnable aktion) {
        if (beschaeftigt) {
            return;
        }
        meldung.verstecken();
        try {
            aktion.run();
        } catch (Exception e) {
            meldung.fehler(fehlertext(e));
        }
    }

    void fehler(String text) {
        meldung.fehler(text);
    }

    static Node beschriftet(String beschriftung, Node feld) {
        Label label = new Label(beschriftung);
        label.getStyleClass().add("feld-beschriftung");
        return new VBox(6, label, feld);
    }

    private void setBeschaeftigt(boolean wert) {
        beschaeftigt = wert;
        buttons.forEach(b -> b.setDisable(wert));
    }

    private static String fehlertext(Throwable e) {
        if (e instanceof Fehlermeldung) {
            return e.getMessage();
        }
        e.printStackTrace();
        String text = e.getMessage();
        return "Unerwarteter Fehler: " + (text != null ? text : e.getClass().getSimpleName());
    }
}
