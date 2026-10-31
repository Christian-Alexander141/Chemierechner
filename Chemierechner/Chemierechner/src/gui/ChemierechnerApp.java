package gui;

import java.io.InputStream;
import java.util.List;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import karten.Karten;

/**
 * Hauptfenster: Seitenleiste mit allen Karten links, die gewählte Karte rechts.
 * Welche Karten es gibt, steht in {@link karten.Karten}.
 */
public class ChemierechnerApp extends Application {

    @Override
    public void start(Stage stage) {
        List<Karte> karten = Karten.alle();

        StackPane inhalt = new StackPane();
        VBox navigation = new VBox(2);
        ToggleGroup gruppe = new ToggleGroup();

        for (Karte karte : karten) {
            ToggleButton eintrag = new ToggleButton(karte.titel());
            eintrag.getStyleClass().add("nav-eintrag");
            eintrag.setMaxWidth(Double.MAX_VALUE);
            eintrag.setFocusTraversable(false);
            eintrag.setToggleGroup(gruppe);
            eintrag.setUserData(karte);
            navigation.getChildren().add(eintrag);
        }

        gruppe.selectedToggleProperty().addListener((obs, alt, neu) -> {
            if (neu == null) {
                // Erneuter Klick auf den aktiven Eintrag soll ihn nicht abwählen
                gruppe.selectToggle(alt);
                return;
            }
            inhalt.getChildren().setAll(((Karte) neu.getUserData()).ansicht());
        });
        if (!gruppe.getToggles().isEmpty()) {
            gruppe.selectToggle(gruppe.getToggles().get(0));
        }

        ScrollPane navScroll = new ScrollPane(navigation);
        navScroll.setFitToWidth(true);
        navScroll.getStyleClass().add("nav-scroll");
        VBox.setVgrow(navScroll, Priority.ALWAYS);

        VBox seitenleiste = new VBox(24, kopfzeile(), navScroll);
        seitenleiste.getStyleClass().add("seitenleiste");

        BorderPane wurzel = new BorderPane(inhalt);
        wurzel.setLeft(seitenleiste);

        Scene scene = new Scene(wurzel, 1200, 780);
        scene.getStylesheets().add(getClass().getResource("/ress/style.css").toExternalForm());

        Image icon = ladeIcon();
        if (icon != null) {
            stage.getIcons().add(icon);
        }
        stage.setTitle("Chemierechner");
        stage.setMinWidth(860);
        stage.setMinHeight(560);
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }

    private HBox kopfzeile() {
        Label titel = new Label("Chemierechner");
        titel.getStyleClass().add("app-titel");

        HBox kopf = new HBox(10, titel);
        kopf.setAlignment(Pos.CENTER_LEFT);
        kopf.getStyleClass().add("app-kopf");

        Image icon = ladeIcon();
        if (icon != null) {
            ImageView bild = new ImageView(icon);
            bild.setFitHeight(32);
            bild.setPreserveRatio(true);
            bild.setSmooth(true);
            kopf.getChildren().add(0, bild);
        }
        return kopf;
    }

    private Image ladeIcon() {
        InputStream daten = getClass().getResourceAsStream("/icon.png");
        return daten == null ? null : new Image(daten);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
