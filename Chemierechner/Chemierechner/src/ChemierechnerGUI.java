import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.converter.DoubleStringConverter;

import Titrationen.Titration;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import APIcalls.modelAdapter;
import Berechnungen.Masseanteil;
import Berechnungen.Partialdruck;
import Berechnungen.Stoffmenge;
import Berechnungen.Volumenanteil;
import Berechnungen.molaresVolumen;

import java.io.File;

public class ChemierechnerGUI extends Application {

    @Override
    public void start(Stage stage) {
        stage.setTitle("Chemierechner ");

        // Icon setzen
        try {
            stage.getIcons().add(new Image("file:icon.png"));
            System.out.println(new File("Icon/icon.png").getAbsolutePath());
            System.out.println("Icon erfolgreich geladen.");
        } catch (Exception e) {
            System.out.println("Kein Icon gefunden optional.");
        }

        TabPane tabs = new TabPane();
        tabs.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        tabs.getTabs().addAll(
                createPartialdruckTab(),
                createMasseanteilTab(),
                createVolumenanteilTab(),
                createMolaresVolumenTab(),
                createStoffmengeTab(),
                AImode(),
                createTitrationTab()
        );

        Scene scene = new Scene(tabs, 480, 360);

        // CSS einbinden
        scene.getStylesheets().add(
                getClass().getResource("/ress/style.css").toExternalForm()
        );

        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }

    private Tab createPartialdruckTab() {
        TextField gesamtDruck = new TextField();
        TextField anteil = new TextField();
        Label ergebnis = new Label();

        Button berechnen = greenButton("Berechnen");
        berechnen.setOnAction(e -> {
            try {
                double g = Double.parseDouble(gesamtDruck.getText());
                double a = Double.parseDouble(anteil.getText());
                double result = new Partialdruck().berechnePartialdruck(g, a);
                ergebnis.setText("Partialdruck: " + result);
            } catch (Exception ex) {
                ergebnis.setText("Fehlerhafte Eingabe");
            }
        });

        VBox layout = modernLayout(
                new Label("Gesamtdruck:"), gesamtDruck,
                new Label("Anteil:"), anteil,
                berechnen, ergebnis
        );

        return new Tab("Partialdruck", layout);
    }

    private Tab createMasseanteilTab() {
        TextField masseKomponente = new TextField();
        TextField gesamtMasse = new TextField();
        Label ergebnis = new Label();

        Button berechnen = greenButton("Berechnen");
        berechnen.setOnAction(e -> {
            try {
                double m = Double.parseDouble(masseKomponente.getText());
                double g = Double.parseDouble(gesamtMasse.getText());
                double result = new Masseanteil().berechneMasseanteil(m, g);
                ergebnis.setText("Masseanteil: " + result);
            } catch (Exception ex) {
                ergebnis.setText("Fehlerhafte Eingabe");
            }
        });

        VBox layout = modernLayout(
                new Label("Masse Komponente:"), masseKomponente,
                new Label("Gesamtmasse:"), gesamtMasse,
                berechnen, ergebnis
        );

        return new Tab("Masseanteil", layout);
    }

    private Tab createVolumenanteilTab() {
        TextField volumenKomponente = new TextField();
        TextField gesamtVolumen = new TextField();
        Label ergebnis = new Label();

        Button berechnen = greenButton("Berechnen");
        berechnen.setOnAction(e -> {
            try {
                double v = Double.parseDouble(volumenKomponente.getText());
                double g = Double.parseDouble(gesamtVolumen.getText());
                double result = new Volumenanteil().berechneVolumenanteil(v, g);
                ergebnis.setText("Volumenanteil: " + result);
            } catch (Exception ex) {
                ergebnis.setText("Fehlerhafte Eingabe");
            }
        });

        VBox layout = modernLayout(
                new Label("Volumen Komponente:"), volumenKomponente,
                new Label("Gesamtvolumen:"), gesamtVolumen,
                berechnen, ergebnis
        );

        return new Tab("Volumenanteil", layout);
    }

    private Tab createMolaresVolumenTab() {
        TextField volumen = new TextField();
        TextField stoffmenge = new TextField();
        Label ergebnis = new Label();

        Button berechnen = greenButton("Berechnen");
        berechnen.setOnAction(e -> {
            try {
                double v = Double.parseDouble(volumen.getText());
                double n = Double.parseDouble(stoffmenge.getText());
                double result = new molaresVolumen().berechneMolaresVolumen(v, n);
                ergebnis.setText("Molares Volumen: " + result);
            } catch (Exception ex) {
                ergebnis.setText("Fehlerhafte Eingabe");
            }
        });

        VBox layout = modernLayout(
                new Label("Volumen:"), volumen,
                new Label("Stoffmenge:"), stoffmenge,
                berechnen, ergebnis
        );

        return new Tab("Molares Volumen", layout);
    }

    private Tab createStoffmengeTab() {
        TextField masse = new TextField();
        TextField molareMasse = new TextField();
        Label ergebnis = new Label();

        Button berechnen = greenButton("Berechnen");
        berechnen.setOnAction(e -> {
            try {
                double m = Double.parseDouble(masse.getText());
                double M = Double.parseDouble(molareMasse.getText());
                double result = new Stoffmenge().berechneStoffmenge(m, M);
                ergebnis.setText("Stoffmenge: " + result);
            } catch (Exception ex) {
                ergebnis.setText("Fehlerhafte Eingabe");
            }
        });

        VBox layout = modernLayout(
                new Label("Masse:"), masse,
                new Label("Molare Masse:"), molareMasse,
                berechnen, ergebnis
        );

        return new Tab("Stoffmenge", layout);
    }

    private Tab AImode() {
        TextField promptInput = new TextField();
        promptInput.setPromptText("Stelle eine chemische Frage...");
        Button sendButton = greenButton("Senden");
        TextArea aiResponse = new TextArea();
        aiResponse.setWrapText(true);
        aiResponse.setEditable(false);
        aiResponse.setPrefHeight(180);

        sendButton.setOnAction(e -> {
            String prompt = promptInput.getText();

            // Hintergrund-Thread starten, damit GUI nicht einfriert
            new Thread(() -> {
                try {
                    String response = new modelAdapter().generate(prompt);
                    javafx.application.Platform.runLater(() -> aiResponse.setText(response));
                } catch (Exception ex) {
                    javafx.application.Platform.runLater(() -> aiResponse.setText("Fehler bei der AI-Anfrage"));
                }
            }).start();
        });

        VBox layout = modernLayout(
                new Label("Frage:"),
                promptInput,
                sendButton,
                new Label("Antwort:"),
                aiResponse
        );

        return new Tab("AI-Modus", layout);
    }

    private Tab createTitrationTab() {
        // Tabelle vorbereiten
        TableView<double[]> table = new TableView<>();
        ObservableList<double[]> data = FXCollections.observableArrayList();

        TableColumn<double[], Double> volumenCol = new TableColumn<>("Volumen (mL)");
        volumenCol.setCellValueFactory(param -> new javafx.beans.property.SimpleDoubleProperty(param.getValue()[0]).asObject());
        volumenCol.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        volumenCol.setOnEditCommit(event -> event.getRowValue()[0] = event.getNewValue());

        TableColumn<double[], Double> pHCol = new TableColumn<>("pH-Wert");
        pHCol.setCellValueFactory(param -> new javafx.beans.property.SimpleDoubleProperty(param.getValue()[1]).asObject());
        pHCol.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        pHCol.setOnEditCommit(event -> event.getRowValue()[1] = event.getNewValue());

        table.getColumns().addAll(volumenCol, pHCol);
        table.setItems(data);
        table.setEditable(true);

        // Eingabefelder für neue Messpunkte
        TextField volumenInput = new TextField();
        volumenInput.setPromptText("Volumen (mL)");

        TextField pHInput = new TextField();
        pHInput.setPromptText("pH-Wert");

        Button addButton = new Button("Messpunkt hinzufügen");
        Button plotButton = new Button("Kurve erstellen");

        Titration titration = new Titration(0, 0);

        // Chart vorbereiten
        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("Volumen (mL)");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("pH");

        LineChart<Number, Number> chart = new LineChart<>(xAxis, yAxis);
        chart.setTitle("Titrationskurve");
        chart.setAnimated(false);
        chart.setCreateSymbols(true);

        // Messpunkt hinzufügen
        addButton.setOnAction(e -> {
            try {
                double volumen = Double.parseDouble(volumenInput.getText());
                double pH = Double.parseDouble(pHInput.getText());
                double[] punkt = new double[]{volumen, pH};

                data.add(punkt);          // Tabelle aktualisieren
                titration.addpH(pH, volumen); // Titration-Klasse aktualisieren

                volumenInput.clear();
                pHInput.clear();
            } catch (NumberFormatException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Bitte gültige Zahlen eingeben!");
                alert.showAndWait();
            }
        });

        // Kurve erstellen
        plotButton.setOnAction(e -> {
            new Thread(() -> {
                XYChart.Series<Number, Number> series = new XYChart.Series<>();
                series.setName("Titrationskurve");

                for (double[] punkt : data) {
                    series.getData().add(new XYChart.Data<>(punkt[0], punkt[1]));
                }

                javafx.application.Platform.runLater(() -> {
                    chart.getData().clear();
                    chart.getData().add(series);
                });
            }).start();
        });

        VBox layout = modernLayout(
                new Label("Neue Messwerte:"),
                volumenInput, pHInput, addButton, plotButton,
                new Label("Messpunkte-Tabelle:"), table,
                chart
        );

        return new Tab("Titration", layout);
    }

    private VBox modernLayout(javafx.scene.Node... nodes) {
        VBox box = new VBox(12);
        box.setPadding(new Insets(20));
        box.getChildren().addAll(nodes);
        box.getStyleClass().add("content-box");
        return box;
    }

    private Button greenButton(String text) {
        Button btn = new Button(text);
        btn.getStyleClass().add("green-button");
        return btn;
    }

    public static void main(String[] args) {
        launch();
    }
}
