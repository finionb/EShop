package domain;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import domain.exceptions.*;
import entities.Artikel;
import entities.Kunde;
import entities.Massengutartikel;
import entities.Warenkorb;

/**
 * ShoppingService
 */
public class ShoppingService {
    DecimalFormat df = new DecimalFormat("0.00");
    private Warenkorb warenkorb; // Warenkorb Objekt

    public ShoppingService() throws IOException, DatenNichtGeladen {
        warenkorb = new Warenkorb(); // Hier wird der Warenkorb erstellt
    }

    public Warenkorb getWarenkorb() {
        return warenkorb;
    }

    public double berechneGesamtpreis() {
        HashMap<Artikel, Integer> artikelMap = warenkorb.getArtikelMap();
        double gesamtpreis = 0.0;
        for (Map.Entry<Artikel, Integer> entry : artikelMap.entrySet()) {
            Artikel artikel = entry.getKey();
            int anzahl = entry.getValue();
            double artikelPreis = artikel.getPreis();
            gesamtpreis += artikelPreis * anzahl;
        }
        return gesamtpreis;
    }

    // getArtikelMap
    public String zeigeWarenkorb(Kunde kunde) {
        Warenkorb warenkorb = kunde.getWarenkorb();
        HashMap<Artikel, Integer> artikelMap = warenkorb.getArtikelMap();
        if (artikelMap.isEmpty()) {
            return "Warenkorb ist leer.";
        } else {
            StringBuilder output = new StringBuilder();
            double gesamtpreis = 0.0;
            for (Map.Entry<Artikel, Integer> entry : artikelMap.entrySet()) {
                Artikel artikel = entry.getKey();
                int anzahl = entry.getValue();
                output.append(artikel.getBezeichnung()).append(" - Anzahl: ").append(anzahl).append("\n");
                double artikelPreis = artikel.getPreis();
                gesamtpreis += artikelPreis * anzahl;
            }
            output.append("Gesamtpreis: ").append(df.format(gesamtpreis)).append(" Euro");
            return output.toString();
        }
    }

    public HashMap<Artikel, Integer> getArtikelBestand1(Kunde kunde) {
        Warenkorb warenkorb = kunde.getWarenkorb();
        HashMap<Artikel, Integer> artikelMap = warenkorb.getArtikelMap();
        return new HashMap<>(artikelMap);
    }

    public List<Artikel> getArtikelBestand(Kunde kunde) {
        Warenkorb warenkorb = kunde.getWarenkorb();
        HashMap<Artikel, Integer> artikelMap = warenkorb.getArtikelMap();

        List<Artikel> artikelListe = new ArrayList<>(artikelMap.keySet());

        return artikelListe;
    }

    // Ersatz Methoden falls es nicht klappen sollte.
    // public void anzahlWarenkorb(Kunde kunde) {
    // Warenkorb warenkorb = kunde.getWarenkorb();
    // HashMap<Artikel, Integer> artikelMap = warenkorb.getArtikelMap();
    // }

    // public void bezeichnungWarenkorb(Kunde kunde) {
    // Warenkorb warenkorb = kunde.getWarenkorb();
    // HashMap<Artikel, Integer> artikelMap = warenkorb.getArtikelMap();
    // }

    public void artikelZuWarenkorbHinzufuegen(Kunde kunde, Artikel artikel, int anzahl)
            throws BestandZuNiedrigException, ArtikelExistiertNichtException, ArtikelMassengutartikelException,
            BestandMussPositivSeinException {
        Warenkorb warenkorb = kunde.getWarenkorb();

        if (artikel == null) {
            throw new ArtikelExistiertNichtException();
        }
        if (anzahl <= 0) {
            throw new BestandMussPositivSeinException(artikel);
        }
        if (artikel.getBestand() < anzahl) {
            throw new BestandZuNiedrigException(artikel, anzahl);
        }

        artikel.setBestand(artikel.getBestand() - anzahl);

        warenkorb.addArtikel(artikel, anzahl);
    }

    public void artikelAusWarenkorbEntfernen(Kunde kunde, Artikel artikel)
            throws ArtikelExistiertNichtException, ArtikelNichtImWarenkorbException, ArtikelMassengutartikelException {
        Warenkorb warenkorb = kunde.getWarenkorb();

        if (artikel == null) {
            throw new ArtikelExistiertNichtException();
        }

        if (!warenkorb.containsArtikel(artikel)) {
            throw new ArtikelNichtImWarenkorbException(artikel);
        }

        int anzahl = warenkorb.getArtikelAnzahl(artikel);
        warenkorb.removeArtikel(artikel); // Artikel entfernen

        artikel.setBestand(artikel.getBestand() + anzahl);
    }

    public void warenkorbAnzahl(Kunde kunde, Artikel artikel) {
        Warenkorb warenkorb = kunde.getWarenkorb();
        int anzahl = warenkorb.getArtikelAnzahl(artikel);
        System.out.println(artikel.getBezeichnung() + ". Die Anzahl im Warenkorb " + anzahl);
    }

    public void erhoeheArtikelanzahl(Kunde kunde, Artikel artikel, int anzahl)
            throws ArtikelMassengutartikelException, BestandZuNiedrigException, ArtikelExistiertNichtException,
            ArtikelNichtImWarenkorbException, BestandMussPositivSeinException {
        Warenkorb warenkorb = kunde.getWarenkorb();

        if (artikel != null) {
            if (artikel instanceof Massengutartikel) {
                int packungsGroesse = ((Massengutartikel) artikel).getPackungsGroesse();
                if (packungsGroesse > 0 && anzahl % packungsGroesse != 0) {
                    throw new ArtikelMassengutartikelException(artikel.getBezeichnung(), packungsGroesse);
                }
            }

            if (artikel.getBestand() < anzahl) {
                throw new BestandZuNiedrigException(artikel, anzahl);
            }

            if (anzahl <= 0) {
                throw new BestandMussPositivSeinException(artikel);
            }

            if (!warenkorb.containsArtikel(artikel)) {
                throw new ArtikelNichtImWarenkorbException(artikel);
            }

            warenkorb.updateArtikelAnzahl(artikel, anzahl);

            artikel.setBestand(artikel.getBestand() - anzahl);

        } else {
            throw new ArtikelExistiertNichtException();
        }
    }

    public void verringereArtikelanzahl(Kunde kunde, Artikel artikel, int anzahl)
            throws ArtikelMassengutartikelException, ArtikelExistiertNichtException, ArtikelNichtImWarenkorbException,
            AnzahlImWarenkorbZuNiedrigException, BestandMussPositivSeinException {
        Warenkorb warenkorb = kunde.getWarenkorb();

        if (artikel != null) {
            if (artikel instanceof Massengutartikel) {
                int packungsGroesse = ((Massengutartikel) artikel).getPackungsGroesse();
                if (packungsGroesse > 0 && anzahl % packungsGroesse != 0) {
                    throw new ArtikelMassengutartikelException(artikel.getBezeichnung(), packungsGroesse);
                }
            }

            if (!warenkorb.containsArtikel(artikel)) {
                throw new ArtikelNichtImWarenkorbException(artikel);
            }

            int aktuelleAnzahl = warenkorb.getArtikelAnzahl(artikel);
            if (anzahl > aktuelleAnzahl) {
                throw new AnzahlImWarenkorbZuNiedrigException(artikel);
            }

            if (anzahl <= 0) {
                throw new BestandMussPositivSeinException(artikel);
            }

            warenkorb.removeAnzahl(artikel, anzahl);

            artikel.setBestand(artikel.getBestand() + anzahl);
        } else {
            throw new ArtikelExistiertNichtException();
        }
    }

    public void leereWarenkorb(Kunde kunde) throws ArtikelMassengutartikelException {
        Warenkorb warenkorb = kunde.getWarenkorb();
        List<Artikel> artikelToRemove = new ArrayList<>();

        for (Artikel artikel : warenkorb.getArtikelMap().keySet()) {
            int anzahl = warenkorb.getArtikelAnzahl(artikel);
            artikelToRemove.add(artikel);

            artikel.setBestand(artikel.getBestand() + anzahl);
        }

        for (Artikel artikel : artikelToRemove) {
            warenkorb.removeArtikel(artikel);
        }

        warenkorb.clearWarenkorb();
    }
}