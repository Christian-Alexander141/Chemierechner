package Berechnungen;

public class Partialdruck {
    
    public double berechnePartialdruck(double gesamtDruck, double anteil) {
        // p = P_gesamt * x
        return gesamtDruck * anteil;
    }
}
