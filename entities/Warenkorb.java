package entities;

import java.text.DecimalFormat;

//import domain.exceptions.*;

import java.util.*;

public class Warenkorb {

    DecimalFormat df = new DecimalFormat("0.00");
    private HashMap<Artikel, Integer> artikelMap;

    public Warenkorb() {
        artikelMap = new HashMap<>();
    }

    public void addArtikel(Artikel artikel, int anzahl) {
        if (artikelMap.containsKey(artikel)) {
            int aktuelleAnzahl = artikelMap.get(artikel);
            artikelMap.put(artikel, aktuelleAnzahl + anzahl);
        } else {
            artikelMap.put(artikel, anzahl);
        }
    }

    public boolean containsArtikel(Artikel artikel) {
        return artikelMap.containsKey(artikel);
    }

    public void removeArtikel(Artikel artikel) {
        if (artikelMap.containsKey(artikel)) {
            artikelMap.remove(artikel);
        }
    }

    public boolean removeAnzahl(Artikel artikel, int anzahlVerringern) {
        if (artikelMap.containsKey(artikel)) {
            int anzahl = artikelMap.get(artikel);
            if (anzahl >= anzahlVerringern) {
                artikelMap.put(artikel, anzahl - anzahlVerringern);
                return true;
            } else {
                artikelMap.remove(artikel);
                return true;
            }
        }
        return false;
    }

    public boolean updateArtikelAnzahl(Artikel artikel, int anzahlErhoehung) {
        if (artikelMap.containsKey(artikel)) {
            int aktuelleAnzahl = artikelMap.get(artikel);
            int neueAnzahl = aktuelleAnzahl + anzahlErhoehung;
            artikelMap.put(artikel, neueAnzahl);
            return true;
        } else {
            artikelMap.put(artikel, anzahlErhoehung);
            return true;
        }
    }

    public int getArtikelAnzahl(Artikel artikel) {
        if (artikelMap.containsKey(artikel)) {
            return artikelMap.get(artikel);
        } else {
            return 0;
        }
    }

    public void clearWarenkorb() {
        artikelMap.keySet().clear();
    }    

    public HashMap<Artikel, Integer> getArtikelMap() {
        return artikelMap;
    }

    public void setArtikelMap(HashMap<Artikel, Integer> artikelMap) {
        this.artikelMap = artikelMap;
    }
    
    @Override
    public String toString() {
        HashMap<Artikel, Integer> artikelMap = getArtikelMap();
        if (artikelMap.isEmpty()) {
            return "Warenkorb ist leer.";
        } else {
            StringBuilder output = new StringBuilder();
            double gesamtpreis = 0.0;
            for (Map.Entry<Artikel, Integer> entry : artikelMap.entrySet()) {
                Artikel artikel = entry.getKey();
                int anzahl = entry.getValue();
                output.append(artikel).append(" - Anzahl: ").append(anzahl).append("\n");
                double artikelPreis = artikel.getPreis();
                gesamtpreis += artikelPreis * anzahl;
            }
            output.append("Gesamtpreis: ").append(df.format(gesamtpreis)).append(" euro");
            return output.toString();
        }
    }
}
