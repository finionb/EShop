package domain.exceptions;

public class AccountNichtGefunden extends Exception {

    public AccountNichtGefunden() {
        super("Ihr account wurde nicht gefunden. Bitter versuchen Sie nochmal");
    }

}
