package domain.exceptions;

public class DatenNichtGespeichert extends Exception {
    public DatenNichtGespeichert() {
        super("Die Daten konnten nicht gespeichert werden");
    }
}
