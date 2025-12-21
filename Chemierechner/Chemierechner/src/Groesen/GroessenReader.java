package Groesen;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class GroessenReader {

    public static void main(String[] args) {
        String dateipfad = "Groesen/groessen.txt";  // Pfad zur groessen.txt Datei
        try {
            BufferedReader reader = new BufferedReader(new FileReader(dateipfad));
            String line;

            // Überschrift der Tabelle
            System.out.printf("%-20s %-10s %-40s\n", "Größe", "Symbol", "Einheiten");
            System.out.println("------------------------------------------------------------");

            // Zeilenweise die Datei einlesen und ausgeben
            while ((line = reader.readLine()) != null) {
                // Aufteilen der Zeile nach Komma
                String[] teile = line.split(",");
                String groesse = teile[0];  // Größe (z.B. "masse", "Stoffmenge", etc.)
                String symbol = teile[1];   // Symbol der Größe (z.B. "m", "n", etc.)
                String einheiten = teile[2];  // Einheiten, die durch Semikolon getrennt sind (z.B. "kg;g")

                // Formatierte Ausgabe der Daten
                System.out.printf("%-20s %-10s %-40s\n", groesse, symbol, einheiten.replace(";", ", "));
            }

            reader.close();  // Reader schließen

        } catch (IOException e) {
            System.out.println("Fehler beim Lesen der Datei: " + e.getMessage());
        }
    }
}
