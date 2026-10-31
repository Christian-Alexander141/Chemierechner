package gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Karte für eine Formel: Zahlen eingeben, "Berechnen" klicken, Ergebnis ablesen.
 *
 * Beispiel:
 *
 *     public class StoffmengeKarte extends Formelkarte {
 *         public StoffmengeKarte() {
 *             super("Stoffmenge");
 *             beschreibung("n = m / M");
 *             eingabe("m", "Masse", "g");
 *             eingabe("M", "Molare Masse", "g/mol");
 *             ergebnis("Stoffmenge", "mol",
 *                     w -> new Stoffmenge().berechneStoffmenge(w.get("m"), w.get("M")));
 *         }
 *     }
 *
 * Es darf mehrere ergebnis()-Aufrufe geben; alle werden mit einem Klick berechnet.
 */
public abstract class Formelkarte extends Karte {

    /** Rechnet aus den eingegebenen Werten ein Ergebnis aus. */
    @FunctionalInterface
    public interface Formel {
        double berechne(Werte werte);
    }

    private final Map<String, Zahlenfeld> eingaben = new LinkedHashMap<>();
    private final List<Ergebnis> ergebnisse = new ArrayList<>();
    private final Feldraster eingabeRaster = new Feldraster();
    private final Feldraster ergebnisRaster = new Feldraster();

    protected Formelkarte(String titel) {
        super(titel);
        hinzufuegen(eingabeRaster);
        button("Berechnen", this::berechnen);
        hinzufuegen(ergebnisRaster);
    }

    /** Eingabe ohne Einheit. Über das Kürzel holst du den Wert in der Formel ab. */
    protected void eingabe(String kuerzel, String beschriftung) {
        eingabe(kuerzel, beschriftung, "");
    }

    /** Eingabe mit Einheit. Über das Kürzel holst du den Wert in der Formel ab. */
    protected void eingabe(String kuerzel, String beschriftung, String einheit) {
        if (eingaben.containsKey(kuerzel)) {
            throw new IllegalArgumentException(
                    "Das Kürzel \"" + kuerzel + "\" ist in der Karte \"" + titel() + "\" doppelt vergeben.");
        }
        Zahlenfeld feld = new Zahlenfeld(this, beschriftung, einheit);
        feld.beiEnter(this::berechnen);
        eingaben.put(kuerzel, feld);
        eingabeRaster.hinzufuegen(beschriftet(beschriftung, feld.knoten()));
    }

    /** Ergebnis ohne Einheit. */
    protected void ergebnis(String beschriftung, Formel formel) {
        ergebnis(beschriftung, "", formel);
    }

    /** Ergebnis mit Einheit; die Formel bekommt die eingegebenen Werte. */
    protected void ergebnis(String beschriftung, String einheit, Formel formel) {
        Ergebnis ergebnis = new Ergebnis(beschriftung, einheit, formel);
        ergebnisse.add(ergebnis);
        ergebnisRaster.hinzufuegen(ergebnis.knoten);
    }

    private void berechnen() {
        ergebnisse.forEach(Ergebnis::zuruecksetzen);

        Map<String, Double> werte = new HashMap<>();
        eingaben.forEach((kuerzel, feld) -> werte.put(kuerzel, feld.wert()));
        Werte w = new Werte(werte);

        double[] resultate = new double[ergebnisse.size()];
        for (int i = 0; i < resultate.length; i++) {
            resultate[i] = ergebnisse.get(i).formel.berechne(w);
            if (!Double.isFinite(resultate[i])) {
                throw new Fehlermeldung("Das Ergebnis ist nicht definiert – wurde durch 0 geteilt?");
            }
        }
        for (int i = 0; i < resultate.length; i++) {
            ergebnisse.get(i).anzeigen(resultate[i]);
        }
    }

    /** Ein Ergebnisfeld; ein Klick darauf kopiert den Wert. */
    private class Ergebnis {

        private final Formel formel;
        private final Label wert = new Label("–");
        private final Node knoten;
        private Double letzterWert;

        Ergebnis(String beschriftung, String einheit, Formel formel) {
            this.formel = formel;

            Label titel = new Label(beschriftung);
            titel.getStyleClass().add("ergebnis-beschriftung");
            wert.getStyleClass().add("ergebnis-wert");
            Label einheitLabel = new Label(einheit);
            einheitLabel.getStyleClass().add("ergebnis-einheit");

            HBox zeile = new HBox(8, wert, einheitLabel);
            zeile.setAlignment(Pos.BASELINE_LEFT);

            VBox box = new VBox(4, titel, zeile);
            box.getStyleClass().add("ergebnis");
            box.setCursor(Cursor.HAND);
            Tooltip.install(box, new Tooltip("Klicken zum Kopieren"));
            box.setOnMouseClicked(e -> kopieren());
            knoten = box;
        }

        void anzeigen(double ergebnis) {
            letzterWert = ergebnis;
            wert.setText(Zahlenformat.anzeigen(ergebnis));
        }

        void zuruecksetzen() {
            letzterWert = null;
            wert.setText("–");
        }

        private void kopieren() {
            if (letzterWert == null) {
                return;
            }
            ClipboardContent inhalt = new ClipboardContent();
            inhalt.putString(Zahlenformat.fuerEingabe(letzterWert));
            Clipboard.getSystemClipboard().setContent(inhalt);
            info("Ergebnis kopiert");
        }
    }
}
