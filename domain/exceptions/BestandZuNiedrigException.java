package domain.exceptions;

import entities.Artikel;

public class BestandZuNiedrigException extends Exception {

    public BestandZuNiedrigException(Artikel artikel, int anzahl) {
        super("Artikelbestand zu niedrig: " + anzahl + " angefordert, Bestand ist " + artikel.getBestand());
    }

}