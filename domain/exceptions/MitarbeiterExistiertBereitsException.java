package domain.exceptions;

import entities.Mitarbeiter;

public class MitarbeiterExistiertBereitsException extends Exception {

    private Mitarbeiter mitarbeiter;

    public MitarbeiterExistiertBereitsException(Mitarbeiter mitarbeiter) {
        super("Benutzername " + mitarbeiter.getBenutzername() + " ist schon vergeben.");
        this.mitarbeiter = mitarbeiter;
    }

    public Mitarbeiter getMitarbeiter() {
        return mitarbeiter;
    }

}
