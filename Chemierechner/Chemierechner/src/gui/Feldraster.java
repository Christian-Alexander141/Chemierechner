package gui;

import javafx.scene.Node;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

/** Ordnet Felder zweispaltig an: links, rechts, nächste Zeile links, ... */
class Feldraster extends GridPane {

    private int anzahl = 0;

    Feldraster() {
        setHgap(16);
        setVgap(16);
        for (int i = 0; i < 2; i++) {
            ColumnConstraints spalte = new ColumnConstraints();
            spalte.setPercentWidth(50);
            spalte.setHgrow(Priority.ALWAYS);
            getColumnConstraints().add(spalte);
        }
    }

    void hinzufuegen(Node knoten) {
        add(knoten, anzahl % 2, anzahl / 2);
        anzahl++;
    }
}
