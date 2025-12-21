Ah, perfekt! Dann können wir die GitHub-Beschreibung anpassen, damit deutlich wird, dass der **AI-Modus externe Abhängigkeiten** hat. Hier ist die aktualisierte Version:

---

# Chemierechner

**Chemierechner** ist eine JavaFX-Anwendung für Schüler, Studierende oder Chemie-Enthusiasten, die verschiedene chemische Berechnungen und Visualisierungen direkt am Rechner durchführen möchten.

## Features

* **Berechnungen:**

  * Partialdruck berechnen
  * Masseanteil und Volumenanteil bestimmen
  * Molares Volumen und Stoffmenge berechnen

* **Titrations-Tab:**

  * Messpunkte direkt in einer **Tabelle** eintragen
  * Inline-Editing der Volumen- und pH-Werte
  * Automatische Erstellung einer **Titrationskurve**
  * Interaktive Visualisierung mit **LineChart**

* **AI-Modus:**

  * Chemische Fragen an ein Modell stellen
  * Antworten werden in Echtzeit angezeigt
  * Anbindung über **Ollama** und Nutzung des Modells **cas/teuken**
  * Anfrage läuft im Hintergrundthread, sodass die GUI nicht einfriert

* **Moderne GUI:**

  * CSS-basiertes Styling
  * Intuitive Layouts für alle Berechnungen
  * Mehrere Tabs für unterschiedliche Funktionen

## Technologie-Stack

* Java 17+
* JavaFX 20+
* Maven/Gradle (abhängig vom Projektsetup)
* Modularer Aufbau mit Packages:

  * `Titrationen` für die Titrationslogik
  * `Berechnungen` für chemische Berechnungen
  * `APIcalls` für die AI-Anbindung

## Installation

1. Java Development Kit (JDK) 17 oder höher installieren
2. JavaFX 20+ Bibliotheken einbinden
3. Projekt mit Maven/Gradle bauen oder direkt in der IDE öffnen
4. `ChemierechnerGUI` als Hauptklasse ausführen

### Hinweis zum AI-Modus

* Für den AI-Modus wird **Ollama** benötigt
* Das Modell **cas/teuken** muss verfügbar sein
* Ohne diese Abhängigkeiten funktioniert der AI-Modus nicht

## Zielgruppe

* Schüler und Studierende der Chemie
* Lehrer, die interaktive Tools im Unterricht einsetzen wollen
* Alle, die chemische Berechnungen visualisieren möchten

## Zukunftspläne

* Automatische Aktualisierung der Titrationskurve beim Bearbeiten von Messpunkten
* Export der Kurve als Bild oder CSV
* Erweiterung um weitere chemische Berechnungen
* Verbesserte AI-Funktionen mit Kontextspeicherung

---

Wenn du willst, kann ich jetzt noch eine **kompakte README-Version** schreiben, die auf GitHub sofort die wichtigsten Infos zeigt und in der der AI-Modus klar hervorgehoben wird.

Willst du, dass ich diese kompakte Version erstelle?
