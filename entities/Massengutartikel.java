package entities;

import domain.exceptions.ArtikelMassengutartikelException;

public class Massengutartikel extends Artikel {
    private int packungsGroesse;

    public Massengutartikel(String bezeichnung, int artikelnummer, double preis, int bestand, boolean verfuegbar,
            int packungsGroesse) {
        super(bezeichnung, artikelnummer, preis, bestand, verfuegbar);
        this.packungsGroesse = packungsGroesse;
    }

    // Diese Methode ändert den Bestand eines Artikels. Bei Massengutartikeln muss
    // man die Änderung in Vielfachen der Packungsgröße angeben.
    public void setBestand(int aenderung) throws ArtikelMassengutartikelException {
        if (aenderung % this.getPackungsGroesse() != 0) {
            throw new ArtikelMassengutartikelException(getBezeichnung(), getPackungsGroesse());
        }
        super.setBestand(aenderung);
    }    

    @Override
    public boolean isMassengutartikel() {
        return true;
    }

    public int getPackungsGroesse() {
        return packungsGroesse;
    }

    public void setPackungsGroesse(int packungsGroesse) {
        this.packungsGroesse = packungsGroesse;
    }

    @Override
    public String toString() {
        return super.toString() + " / Packungsgröße: " + packungsGroesse;
    }
}