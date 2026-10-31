package karten;

import java.util.List;

import gui.Karte;

/**
 * Alle Karten, die in der Seitenleiste erscheinen – in dieser Reihenfolge.
 *
 * Neue Karte anlegen:
 *  1. Klasse in diesem Paket schreiben (Vorlage: StoffmengeKarte oder TitrationKarte)
 *  2. Hier eine Zeile ergänzen
 */
public class Karten {

    public static List<Karte> alle() {
        return List.of(
                new PartialdruckKarte(),
                new MasseanteilKarte(),
                new VolumenanteilKarte(),
                new MolaresVolumenKarte(),
                new StoffmengeKarte(),
                new MolareMasseKarte(),
                new TitrationKarte(),
                new AiKarte()
        );
    }
}
