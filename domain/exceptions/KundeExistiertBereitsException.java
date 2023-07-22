package domain.exceptions;

import entities.Kunde;

public class KundeExistiertBereitsException extends Exception {

    public KundeExistiertBereitsException(Kunde kunde) {
        super("Benutzername " + " ist schon vergeben.");
    }

}
