package domain.exceptions;

public class NummerListeException extends Exception {
    public NummerListeException() {
        super("Die Liste der Identifikationsnummer konnte nicht geladen werden");
    }

}
