package Titrationen;

import java.util.ArrayList;
import java.util.List;

public class Titration {
    private double pH;
    private double volumen; 
    private List<double[]> kurve = new ArrayList<>();

    public Titration(double PH, double  volumen) {
        this.pH = PH;
        this.volumen = volumen;
    }

    public void addpH (double pH,double volumen) {
        this.kurve.add(new double[] {volumen, pH});
    }

    public List<double[]> getKurve() {
        return kurve;
    }

}
