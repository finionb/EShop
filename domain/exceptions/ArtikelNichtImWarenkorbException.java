package domain.exceptions;

import entities.Artikel;

public class ArtikelNichtImWarenkorbException extends Exception {

    public ArtikelNichtImWarenkorbException(Artikel artikel) {
        super("Artikel mit der Bezeichnung " + artikel.getBezeichnung() + " ist nicht im Warenkorb.");
    }

}