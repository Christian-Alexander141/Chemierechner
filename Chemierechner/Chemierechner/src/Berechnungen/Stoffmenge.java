package Berechnungen;

public class Stoffmenge {
    // Diese Klasse kann erweitert werden, um spezifische Methoden und Attribute 
    // für die Stoffmenge zu enthalten
 
    public double berechneStoffmenge(double masse, double molareMasse) {
        // n = m / M
        return masse / molareMasse;
    }

}
