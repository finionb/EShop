package domain.exceptions;

public class ListeLeerException extends Exception {
    public ListeLeerException() {
        super("Artikelliste ist leer.");
    }
}
