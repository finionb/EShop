package domain.exceptions;

public class DatenNichtGeladen extends Exception {
    public DatenNichtGeladen() {
        super("Nicht alle Daten konnten geladen werden");
    }
}
