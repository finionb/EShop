package domain.exceptions;

public class KundeExistiertNicht extends Exception {

    public KundeExistiertNicht() {
        super("Sie sind noch nicht als Kunde registriert");
    }
}