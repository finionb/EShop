package domain.exceptions;

import entities.Artikel;

/**
 * Exception zur Signalisierung, dass ein Artikel bereits existiert (z.B. bei einem Einfügevorgang).
 */
public class ArtikelExistiertBereitsException extends Exception {

    public ArtikelExistiertBereitsException(Artikel artikel) {
        super("Die Artikelbezeichung " + artikel.getBezeichnung() + " und/oder die Artikelnummer " + artikel.getArtikelnummer()
                + " werden bereits verwendet." );
    }
}