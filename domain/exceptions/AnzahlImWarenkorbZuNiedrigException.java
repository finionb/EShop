package domain.exceptions;

import entities.Artikel;

public class AnzahlImWarenkorbZuNiedrigException extends Exception {

    public AnzahlImWarenkorbZuNiedrigException(Artikel artikel) {
        super("Die angegebene Anzahl für den Artikel '" + artikel.getBezeichnung() + "' übersteigt die Anzahl im Warenkorb.");
    }
    
}

