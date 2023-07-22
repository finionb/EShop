package domain.exceptions;

public class ArtikelMassengutartikelException extends Exception {

    public ArtikelMassengutartikelException(String bezeichnung, int packungsGroesse) {
        super("Der Artikel mit der Bezeichnung " + bezeichnung + " hat eine Packungsgröße von " + packungsGroesse + ".");
    }

}