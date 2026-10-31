package gui;

import java.util.List;

import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

/** Liniendiagramm. Punkte werden als double[] {x, y} übergeben. */
public class Diagramm {

    private final LineChart<Number, Number> diagramm;

    Diagramm(String xAchse, String yAchse) {
        NumberAxis x = new NumberAxis();
        x.setLabel(xAchse);
        NumberAxis y = new NumberAxis();
        y.setLabel(yAchse);

        diagramm = new LineChart<>(x, y);
        diagramm.setAnimated(false);
        diagramm.setLegendVisible(false);
        diagramm.setPrefHeight(400);
    }

    /** Ersetzt den Inhalt durch eine Kurve aus den Punkten {x, y}. */
    public void zeichne(String name, List<double[]> punkte) {
        XYChart.Series<Number, Number> reihe = new XYChart.Series<>();
        reihe.setName(name);
        for (double[] punkt : punkte) {
            reihe.getData().add(new XYChart.Data<>(punkt[0], punkt[1]));
        }
        diagramm.getData().setAll(List.of(reihe));
    }

    public void leeren() {
        diagramm.getData().clear();
    }

    Node knoten() {
        return diagramm;
    }
}
