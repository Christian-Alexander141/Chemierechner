package karten;

import java.net.ConnectException;

import APIcalls.modelAdapter;
import gui.Eingabefeld;
import gui.Fehlermeldung;
import gui.Karte;
import gui.Textausgabe;

public class AiKarte extends Karte {

    private final Eingabefeld frage;
    private final Textausgabe antwort;

    public AiKarte() {
        super("AI-Modus");
        beschreibung("Chemische Fragen an ein lokales Sprachmodell (Ollama) stellen.");

        abschnitt("Frage");
        frage = textfeld("Deine Frage", "z.B. Was ist eine Pufferlösung?");
        frage.beiEnter(this::senden);
        button("Senden", this::senden);

        abschnitt("Antwort");
        antwort = textbereich();
    }

    private void senden() {
        String text = frage.text();
        if (text.isEmpty()) {
            throw new Fehlermeldung("Bitte zuerst eine Frage eingeben.");
        }
        imHintergrund(() -> frageModell(text), antwort::setText);
    }

    private String frageModell(String prompt) throws Exception {
        try {
            return new modelAdapter().generate(prompt);
        } catch (ConnectException e) {
            throw new Fehlermeldung("Keine Verbindung zu Ollama – läuft der Dienst? (ollama serve)");
        }
    }
}
