package domain.exceptions;

public class MitarbeiterExistiertNicht extends Exception {
    public MitarbeiterExistiertNicht() {
        super("Sie sind noch nicht als Mitarbeiter registriert");
    }
}
