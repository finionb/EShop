package domain.exceptions;

public class ArtikelExistiertNichtException extends Exception {

    public ArtikelExistiertNichtException() {
        super("Der angefordet Artikel existiert nicht");
    }
    
}