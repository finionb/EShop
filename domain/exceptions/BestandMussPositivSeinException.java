package domain.exceptions;

import entities.Artikel;

public class BestandMussPositivSeinException extends Exception {

    public BestandMussPositivSeinException(Artikel artikel) {
        super("Die Anzahl vom Artikel " + artikel.getBezeichnung() + " muss Positiv sein.");
    }

}