package gui;

import javafx.animation.PauseTransition;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.HBox;
import javafx.util.Duration;

/** Kleine Meldung unten im Fenster (Fehler, Hinweis oder "Bitte warten"). */
class Meldung extends HBox {

    private final Label text = new Label();
    private final ProgressIndicator laeuft = new ProgressIndicator();
    private final PauseTransition ausblenden = new PauseTransition();

    Meldung() {
        super(12);
        setAlignment(Pos.CENTER_LEFT);
        setMaxSize(USE_PREF_SIZE, USE_PREF_SIZE);
        text.setWrapText(true);
        text.setMaxWidth(520);
        laeuft.setPrefSize(18, 18);
        getChildren().addAll(laeuft, text);
        setOnMouseClicked(e -> verstecken());
        ausblenden.setOnFinished(e -> verstecken());
        setVisible(false);
    }

    void fehler(String meldung) {
        zeige(meldung, "toast-fehler", false);
        ausblenden.setDuration(Duration.seconds(6));
        ausblenden.playFromStart();
    }

    void info(String meldung) {
        zeige(meldung, "toast-info", false);
        ausblenden.setDuration(Duration.seconds(2.5));
        ausblenden.playFromStart();
    }

    void laeuft(String meldung) {
        zeige(meldung, "toast-info", true);
        ausblenden.stop();
    }

    void verstecken() {
        ausblenden.stop();
        setVisible(false);
    }

    private void zeige(String meldung, String stil, boolean mitSpinner) {
        getStyleClass().setAll("toast", stil);
        text.setText(meldung);
        laeuft.setVisible(mitSpinner);
        laeuft.setManaged(mitSpinner);
        setVisible(true);
    }
}
