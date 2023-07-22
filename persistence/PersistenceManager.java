package persistence;

import java.io.IOException;

import entities.Artikel;
// import entities.Kunde;
// import entities.Mitarbeiter;

/**
 * @author teschke
 *
 * Allgemeine Schnittstelle für den Zugriff auf ein Speichermedium
 * (z.B. Datei oder Datenbank) zum Ablegen von beispielsweise
 * Artikel- oder Kundendaten.
 *
 * Das Interface muss von Klassen implementiert werden, die eine
 * Persistenz-Schnittstelle realisieren wollen.
 */



//Veraltet -> Alle Methoden aus FilePersistenceManager müssen hier vernünftig definiert werden.
//sonst ist es unnötig ein Interface zu erstellen, es wäre aber besser und schöner. Nicht einfach COPY PASTA machen!
public interface PersistenceManager {

    public void openForReading(String datenquelle) throws IOException;

    public void openForWriting(String datenquelle) throws IOException;

    public boolean close();

    /**
     * Methode zum Einlesen der Artikeldaten aus einer externen Datenquelle.
     *
     * @return Artikel-Objekt, wenn Einlesen erfolgreich, false null
     */
    public Artikel ladeArtikel() throws IOException;

    /**
     * Methode zum Schreiben der Artikeldaten in eine externe Datenquelle.
     *
     * @param b Artikel-Objekt, das gespeichert werden soll
     * @return true, wenn Schreibvorgang erfolgreich, false sonst
     */
    public boolean speichereArtikel(Artikel a) throws IOException;
	
	// public Kunde ladeKunde() throws IOException;

	// public boolean speichereKunde(Kunde k) throws IOException;

    // public Mitarbeiter ladeMitarbeiter() throws IOException;

	// public boolean speichereMitarbeiter(Mitarbeiter m) throws IOException;
}
