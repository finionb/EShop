package domain.exceptions;

public class ErsterMitarbeiter extends Exception {
    public ErsterMitarbeiter() {
        super("Es existiert schon eine/n Mitarbeiter/in");
    }

}
