package gui;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.util.StringConverter;

/**
 * Tabelle mit Zahlen. Werte können per Doppelklick bearbeitet werden,
 * Zeilen lassen sich per Rechtsklick oder Entf-Taste löschen.
 */
public class Tabelle {

    private final Karte karte;
    private final int spalten;
    private final ObservableList<double[]> zeilen = FXCollections.observableArrayList();
    private final TableView<double[]> tabelle = new TableView<>(zeilen);
    private final List<Runnable> beiAenderung = new ArrayList<>();

    private static final StringConverter<Double> ZAHL = new StringConverter<>() {
        @Override
        public String toString(Double wert) {
            return wert == null ? "" : Zahlenformat.fuerEingabe(wert);
        }

        @Override
        public Double fromString(String text) {
            try {
                return Zahlenformat.lesen(text);
            } catch (NumberFormatException e) {
                return null;
            }
        }
    };

    Tabelle(Karte karte, String... spaltennamen) {
        this.karte = karte;
        this.spalten = spaltennamen.length;

        for (int i = 0; i < spalten; i++) {
            final int index = i;
            TableColumn<double[], Double> spalte = new TableColumn<>(spaltennamen[i]);
            spalte.setCellValueFactory(z -> new SimpleObjectProperty<>(z.getValue()[index]));
            spalte.setCellFactory(TextFieldTableCell.forTableColumn(ZAHL));
            spalte.setSortable(false);
            spalte.setOnEditCommit(e -> {
                if (e.getNewValue() == null) {
                    karte.fehler("Ungültige Zahl – der alte Wert wurde beibehalten.");
                    tabelle.refresh();
                    return;
                }
                e.getRowValue()[index] = e.getNewValue();
                geaendert();
            });
            tabelle.getColumns().add(spalte);
        }

        tabelle.setEditable(true);
        tabelle.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        tabelle.setPlaceholder(new Label("Noch keine Einträge"));
        tabelle.setPrefHeight(260);

        MenuItem loeschen = new MenuItem("Zeile löschen");
        loeschen.setOnAction(e -> ausgewaehlteLoeschen());
        tabelle.setContextMenu(new ContextMenu(loeschen));
        tabelle.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.DELETE && tabelle.getEditingCell() == null) {
                ausgewaehlteLoeschen();
            }
        });
    }

    /** Fügt eine Zeile am Ende an. Es müssen so viele Werte sein wie Spalten. */
    public void zeileHinzufuegen(double... werte) {
        if (werte.length != spalten) {
            throw new IllegalArgumentException(
                    "Die Tabelle hat " + spalten + " Spalten, übergeben wurden " + werte.length + " Werte.");
        }
        zeilen.add(werte.clone());
        tabelle.scrollTo(zeilen.size() - 1);
        geaendert();
    }

    /** Alle Zeilen als Kopie; jede Zeile ist ein double[] mit einem Wert pro Spalte. */
    public List<double[]> zeilen() {
        List<double[]> kopie = new ArrayList<>();
        for (double[] zeile : zeilen) {
            kopie.add(zeile.clone());
        }
        return kopie;
    }

    public void leeren() {
        zeilen.clear();
        geaendert();
    }

    /** Aktion, die nach jeder Änderung (hinzufügen, bearbeiten, löschen) ausgeführt wird. */
    public void beiAenderung(Runnable aktion) {
        beiAenderung.add(aktion);
    }

    Node knoten() {
        return tabelle;
    }

    private void ausgewaehlteLoeschen() {
        double[] zeile = tabelle.getSelectionModel().getSelectedItem();
        if (zeile != null) {
            zeilen.remove(zeile);
            geaendert();
        }
    }

    private void geaendert() {
        beiAenderung.forEach(karte::ausfuehren);
    }
}
