package entities;

import java.util.Objects;

import domain.exceptions.ArtikelMassengutartikelException;

public class Artikel {

    // Attribute zur Beschreibung eines Artikels:
    private String bezeichnung;
    private int artikelnummer;
    private boolean verfuegbar;
    private double preis;
    private int bestand;

    // Konstrukter um den passenden Artikel mit der Bezeichnung und der
    // Artikelnummer zu finden
    public Artikel(String bezeichnung, int artikelnummer) {
        this.bezeichnung = bezeichnung;
        this.artikelnummer = artikelnummer;
    }

    // Konstrukter von Artikel um z.B neue Artikel zu erstellen
    public Artikel(String bezeichnung, int artikelnummer, double preis, int bestand, boolean verfuegbar) {
        this.bezeichnung = bezeichnung;
        this.artikelnummer = artikelnummer;
        this.verfuegbar = verfuegbar;
        this.preis = preis;
        this.bestand = bestand;
    }

    /*
     * Diese Methode vergleicht zwei Artikel-Objekte auf Gleichheit. Sie überprüft,
     * ob die beiden Objekte dasselbe sind und gibt true zurück. Andernfalls wird
     * überprüft, ob die Objekte null oder unterschiedliche Klassen haben, und false
     * wird zurückgegeben.
     * 
     * Wir verwenden diese Methoden um die Anzahl im Warenkorb verändern zu können.
     */
    @Override
    public boolean equals(Object andererArtikel) {
        if (this == andererArtikel) {
            return true;
        }
        if (andererArtikel == null || getClass() != andererArtikel.getClass()) {
            return false;
        }
        Artikel artikel = (Artikel) andererArtikel;
        return artikelnummer == artikel.artikelnummer &&
                Objects.equals(bezeichnung, artikel.bezeichnung);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bezeichnung, artikelnummer);
    }

    public boolean isMassengutartikel() {
        return false;
    }

    // Methode wird immer automatisch aufgerufen, wenn ein Artikel als String
    // benutzt wird (z.B in println(artikel);)

    public String toString() {
        String artikelTyp = isMassengutartikel() ? "Massengutartikel" : "Einzelartikel";

        if (bestand > 0) {
            verfuegbar = true;
        } else {
            verfuegbar = false;
        }
        String verfuegbarkeit = verfuegbar ? "verfuegbar" : "ausverkauft";

        return ("Artikelnummer: " + artikelnummer + " / Bezeichnung: " + bezeichnung + " / Preis: " + preis
                + " / Bestand: " + bestand + " / " + verfuegbarkeit + " / Typ: " + artikelTyp);
    }

    // Getter und Setter
    public String getBezeichnung() {
        return bezeichnung;
    }

    public void setBezeichnung(String bezeichnung) {
        this.bezeichnung = bezeichnung;
    }

    public int getArtikelnummer() {
        return artikelnummer;
    }

    public void setArtikelnummer(int artikelnummer) {
        this.artikelnummer = artikelnummer;
    }

    public boolean isVerfuegbar() {
        return verfuegbar;
    }

    public void setVerfuegbar(boolean verfuegbar) {
        this.verfuegbar = verfuegbar;
    }

    public double getPreis() {
        return preis;
    }

    public void setPreis(double preis) {
        this.preis = preis;
    }

    public int getBestand() {
        return bestand;
    }

    public void setBestand(int bestand) throws ArtikelMassengutartikelException {
        this.bestand = bestand;
    }

}