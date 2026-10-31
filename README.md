
# Chemierechner

starte mit :cd /home/alex/Dokumente/Github/Chemierechner/Chemierechner
./run.sh

**Chemierechner** ist eine JavaFX-Anwendung für Schüler, Studierende oder Chemie-Enthusiasten, die verschiedene chemische Berechnungen und Visualisierungen direkt am Rechner durchführen möchten.

## Features

* **Berechnungen:**

  * Partialdruck berechnen
  * Masseanteil und Volumenanteil bestimmen (auch in Prozent)
  * Molares Volumen, Stoffmenge und molare Masse berechnen
  * Eingabe mit Komma oder Punkt, Ergebnis per Klick kopieren

* **Titrations-Tab:**

  * Messpunkte direkt in einer **Tabelle** eintragen
  * Inline-Editing der Volumen- und pH-Werte
  * **Titrationskurve** aktualisiert sich automatisch bei jeder Änderung
  * Interaktive Visualisierung mit **LineChart**

* **AI-Modus:**

  * Chemische Fragen an ein Modell stellen
  * Antworten werden in Echtzeit angezeigt
  * Anbindung über **Ollama** und Nutzung des Modells **cas/teuken**
  * Anfrage läuft im Hintergrundthread, sodass die GUI nicht einfriert

* **Moderne GUI:**

  * Seitenleiste mit allen Rechnern, CSS-basiertes Styling (`src/ress/style.css`)
  * Neue Rechner ohne GUI-Code erweiterbar (siehe unten)

## Technologie-Stack

* Java 17+
* JavaFX 20+
* Maven/Gradle (abhängig vom Projektsetup)
* Modularer Aufbau mit Packages:

  * `gui` – Grundgerüst der Oberfläche (muss nicht angefasst werden)
  * `karten` – die einzelnen Rechner (Karten) in der Seitenleiste
  * `Titrationen` für die Titrationslogik
  * `Berechnungen` für chemische Berechnungen
  * `APIcalls` für die AI-Anbindung

## Installation

1. Java Development Kit (JDK) 17 oder höher installieren
2. JavaFX 20+ Bibliotheken einbinden
3. Projekt mit Maven/Gradle bauen oder direkt in der IDE öffnen
4. `gui.ChemierechnerApp` als Hauptklasse ausführen

### Hinweis zum AI-Modus

* Für den AI-Modus wird **Ollama** benötigt
* Das Modell **cas/teuken** muss verfügbar sein
* Ohne diese Abhängigkeiten funktioniert der AI-Modus nicht

## Neue Karte anlegen

Jeder Eintrag in der Seitenleiste ist eine **Karte** im Paket `karten`. Für eine neue Karte braucht es nur zwei Schritte, ohne JavaFX-Code:

1. Klasse in `src/karten/` anlegen
2. In `src/karten/Karten.java` eine Zeile `new MeineKarte(),` ergänzen

### Formelkarte (Eingaben → Ergebnis)

Für alles, was nach dem Muster „Zahlen eingeben, Ergebnis ausrechnen“ funktioniert:

```java
package karten;

import Berechnungen.Stoffmenge;
import gui.Formelkarte;

public class StoffmengeKarte extends Formelkarte {

    public StoffmengeKarte() {
        super("Stoffmenge");                      // Titel in der Seitenleiste
        beschreibung("n = m / M");                // Text unter der Überschrift
        eingabe("m", "Masse", "g");               // Kürzel, Beschriftung, Einheit
        eingabe("M", "Molare Masse", "g/mol");
        ergebnis("Stoffmenge", "mol",
                w -> new Stoffmenge().berechneStoffmenge(w.get("m"), w.get("M")));
    }
}
```

Über das Kürzel (`w.get("m")`) holt die Formel den eingegebenen Wert ab. Es darf mehrere `ergebnis(...)` geben (siehe `MasseanteilKarte`). Leere Felder, ungültige Zahlen und Division durch 0 werden automatisch abgefangen.

### Freie Karte (eigener Aufbau)

Für alles andere erbt die Karte von `gui.Karte` und setzt sich aus Bausteinen zusammen (Vorlagen: `TitrationKarte`, `AiKarte`):

| Baustein | Liefert | Wichtige Methoden |
|---|---|---|
| `abschnitt("Titel")` | – | beginnt einen neuen weißen Kasten |
| `zahlenfeld("Beschriftung", "Einheit")` | `Zahlenfeld` | `wert()`, `leeren()`, `fokus()`, `beiEnter(...)` |
| `textfeld("Beschriftung", "Platzhalter")` | `Eingabefeld` | `text()`, `leeren()`, `beiEnter(...)` |
| `button("Text", aktion)` | – | führt `aktion` beim Klick aus |
| `textbereich()` | `Textausgabe` | `setText(...)`, `anhaengen(...)` |
| `tabelle("Spalte 1", "Spalte 2")` | `Tabelle` | `zeileHinzufuegen(...)`, `zeilen()`, `beiAenderung(...)` |
| `diagramm("x-Achse", "y-Achse")` | `Diagramm` | `zeichne(name, punkte)`, `leeren()` |
| `imHintergrund(aufgabe, danach)` | – | langsame Aufgaben, ohne dass das Fenster einfriert |

Mehrere Zahlenfelder hintereinander stehen automatisch nebeneinander, ebenso mehrere Buttons.

**Fehler anzeigen:** `throw new Fehlermeldung("Text");` in einer Aktion zeigt den Text unten im Fenster an.

## Zielgruppe

* Schüler und Studierende der Chemie
* Lehrer, die interaktive Tools im Unterricht einsetzen wollen
* Alle, die chemische Berechnungen visualisieren möchten

## Zukunftspläne

* Export der Kurve als Bild oder CSV
* Erweiterung um weitere chemische Berechnungen
* Verbesserte AI-Funktionen mit Kontextspeicherung


