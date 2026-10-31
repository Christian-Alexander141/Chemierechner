package gui;

import java.math.BigDecimal;
import java.math.MathContext;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/** Liest und formatiert Zahlen im deutschen Format (Komma als Dezimaltrennzeichen). */
final class Zahlenformat {

    private static final String HOCHGESTELLT = "⁰¹²³⁴⁵⁶⁷⁸⁹";

    private Zahlenformat() {
    }

    /** Liest "0,5", "0.5" oder "6,022e23". */
    static double lesen(String text) {
        String t = text.trim().replace(',', '.');
        if (t.isEmpty()) {
            throw new NumberFormatException("leer");
        }
        double wert = Double.parseDouble(t);
        if (!Double.isFinite(wert)) {
            throw new NumberFormatException(text);
        }
        return wert;
    }

    /** Für die Ergebnisanzeige: gerundet, sehr große/kleine Zahlen als 6,022 · 10²³. */
    static String anzeigen(double wert) {
        double betrag = Math.abs(wert);
        if (wert == 0) {
            return "0";
        }
        if (betrag >= 1e-3 && betrag < 1e6) {
            return dezimal(BigDecimal.valueOf(wert).round(new MathContext(6)));
        }
        int exponent = (int) Math.floor(Math.log10(betrag));
        BigDecimal mantisse = BigDecimal.valueOf(wert / Math.pow(10, exponent)).round(new MathContext(4));
        if (mantisse.abs().compareTo(BigDecimal.TEN) >= 0) {
            mantisse = mantisse.movePointLeft(1);
            exponent++;
        }
        return dezimal(mantisse) + " · 10" + hochgestellt(exponent);
    }

    /** Ungerundet und so, dass lesen() die Zahl wieder versteht. */
    static String fuerEingabe(double wert) {
        return BigDecimal.valueOf(wert).stripTrailingZeros().toPlainString().replace('.', ',');
    }

    private static String dezimal(BigDecimal wert) {
        DecimalFormat format = new DecimalFormat("0.##########", DecimalFormatSymbols.getInstance(Locale.GERMANY));
        return format.format(wert);
    }

    private static String hochgestellt(int zahl) {
        StringBuilder ergebnis = new StringBuilder();
        for (char c : String.valueOf(zahl).toCharArray()) {
            ergebnis.append(c == '-' ? '⁻' : HOCHGESTELLT.charAt(c - '0'));
        }
        return ergebnis.toString();
    }
}
