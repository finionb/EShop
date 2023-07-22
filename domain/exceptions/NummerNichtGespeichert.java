package domain.exceptions;

public class NummerNichtGespeichert extends Exception {
    public NummerNichtGespeichert() {
        super("Die Identifikationsnummer konnte nicht gespeichert werden");
    }

}
