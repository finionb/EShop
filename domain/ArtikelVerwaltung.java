package domain;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import domain.exceptions.*;
import persistence.FilePersistenceManager;
import entities.Artikel;
import entities.Massengutartikel;

public class ArtikelVerwaltung {

    // Verwaltung des Artikelbestands in einer verketteten Liste
    private List<Artikel> artikelBestand = new ArrayList<>();
    // Persistenz-Schnittstelle, die für die Details des Dateizugriffs
    // verantwortlich ist
    private FilePersistenceManager pm = new FilePersistenceManager();

    /**
     * Methode zum Einlesen von Artikeldaten aus einer Datei.
     *
     * @param datei Datei, die einzulesenden Artikelbestand enthält
     * @throws IOException
     */
    public void liesDaten(String datei) throws IOException {
        // PersistenzManager für Lesevorgänge öffnen
        pm.openForReading(datei);

        Artikel einArtikel;
        do {
            // Artikel-Objekt einlesen
            einArtikel = pm.ladeArtikel();
            if (einArtikel != null) {
                // Artikel in Liste einfügen
                try {
                    einfuegen(einArtikel);
                } catch (ArtikelExistiertBereitsException e1) {
                    // Kann hier eigentlich nicht auftreten,
                    // daher auch keine Fehlerbehandlung...
                }
            }
        } while (einArtikel != null);

        // Persistenz-Schnittstelle wieder schließen
        pm.close();
    }

    /**
     * Methode zum Schreiben der Artikeldaten in eine Datei.
     *
     * @param datei Datei, in die der Artikelbestand geschrieben werden soll
     * @throws IOException
     * @throws ArtikelMassengutartikelException
     */
    public void schreibeDaten(String datei) throws IOException {
        // PersistenzManager für Schreibvorgänge öffnen
        pm.openForWriting(datei);

        for (Artikel artikel : artikelBestand) {
            // speichern
            pm.speichereArtikel(artikel);
        }

        // Persistenz-Schnittstelle wieder schließen
        pm.close();
    }

    // Artikelmethode
    public List<String> gibArtikellisteAus(List<Artikel> liste) throws ListeLeerException {
        List<String> ausgabeListe = new ArrayList<>();

        if (liste.isEmpty()) {
            // wenn die Liste leer ist wird folgende exception ausgegeben:
            throw new ListeLeerException();
        } else {
            for (Artikel artikel : liste) {
                ausgabeListe.add(artikel.toString());
            }
        }
        return ausgabeListe;
    }

    // Hier wird getestet, ob es die Artikelbezeichung oder die Artikelnummer schon
    // gibt, damit diese nicht zweimal vorkommen können.
    private boolean existiertBereits(Artikel einArtikel) {
        for (Artikel artikel : artikelBestand) {
            if (artikel.getArtikelnummer() == einArtikel.getArtikelnummer()
                    || artikel.getBezeichnung().equals(einArtikel.getBezeichnung())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Methode, die ein Artikel an das Ende der Artikelliste einfügt.
     *
     * @param einArtikel das einzufügende Artikel
     * @throws ArtikelExistiertBereitsException wenn der Artikel bereits existiert
     */
    public void einfuegen(Artikel einArtikel) throws ArtikelExistiertBereitsException {
        if (existiertBereits(einArtikel)) {
            throw new ArtikelExistiertBereitsException(einArtikel);
        }

        artikelBestand.add(einArtikel);
    }

    public Artikel getArtikelByBezeichnungUndArtikelnummer(String bezeichnung, int artikelnummer) {
        for (Artikel a : artikelBestand) {
            if (a.getBezeichnung().equals(bezeichnung) && a.getArtikelnummer() == artikelnummer) {
                return a;
            }
        }
        return null; // Artikel nicht gefunden
    }

    /**
     * Methode zum Löschen eines Artikels aus dem Bestand.
     *
     * @param einArtikel das löschende Artikel
     */
    public void loeschen(Artikel einArtikel) {
        // Das übernimmt die ArtikelListe:
        artikelBestand.removeIf(a -> a.getBezeichnung().equals(einArtikel.getBezeichnung())
                && a.getArtikelnummer() == einArtikel.getArtikelnummer());
    }
    /*
     * a steht für alle Elemente aus Artikel und einArtikel für das gesuchte. Es
     * müssen die Kriterien für Bezeichnung und Artikelnummer erfüllt sein, damit
     * der Artikel gelöscht wird
     */

    /**
     * Methode, die anhand einer Bezeichnung nach Artikeln sucht.
     * Es wird eine Liste von Artikeln
     * zurückgegeben, die alle Artikel mit exakt übereinstimmendem Bezeichnung und
     * Artikelnummer enthält.
     *
     * @param bezeichnung Bezeichnung des gesuchten Artikels
     * @return Liste der Artikel mit gesuchter Bezeichnung (evtl.
     *         leer)
     */
    public List<Artikel> sucheArtikelbezeichnung(String bezeichnung) {
        List<Artikel> suchErg = new ArrayList<>();
        for (Artikel artikel : artikelBestand) {
            if (artikel.getBezeichnung().equals(bezeichnung)) {
                // gefundenen Artikel in Suchergebnis eintragen
                suchErg.add(artikel);
            }

        }

        return suchErg;
    }

    /**
     * Methode, die anhand einer Artikelnummer nach Artikeln sucht.
     * Es wird eine Liste von Artikeln
     * zurückgegeben, die alle Artikel mit exakt übereinstimmendem Bezeichnung und
     * Artikelnummer enthält.
     *
     * @param artikelnummer Artikelnummer des gesuchten Artikels
     * @return Liste der Artikel mit gesuchter Artikelnummer (evtl.
     *         leer)
     */
    public List<Artikel> sucheArtikelnummer(int artikelnummer) {
        List<Artikel> suchErg = new ArrayList<>();
        for (Artikel artikel : artikelBestand) {
            if ((String.valueOf(artikel.getArtikelnummer()).equals(String.valueOf(artikelnummer)))) {
                // gefundenen Artikel in Suchergebnis eintragen
                suchErg.add(artikel);
            }
        }
        return suchErg;
    }

    public Artikel artikelbezeichnungSuchen(String bezeichnung) {
        Artikel artikel = null;
        for (Artikel a : artikelBestand) {
            if (a.getBezeichnung().equals(bezeichnung)) {
                artikel = a;
            }
        }
        return artikel;
    }

    public void artikelBestandErhoehen(int artikelnummer, int anzahl)
            throws ArtikelMassengutartikelException, ArtikelExistiertNichtException {
        List<Artikel> ergebnisse = sucheArtikelnummer(artikelnummer);
        if (ergebnisse.size() == 1) {
            Artikel a = ergebnisse.get(0);
            if (a instanceof Massengutartikel) {
                Massengutartikel massengutartikel = (Massengutartikel) a;
                if (anzahl % massengutartikel.getPackungsGroesse() != 0) {
                    throw new ArtikelMassengutartikelException(a.getBezeichnung(),
                            massengutartikel.getPackungsGroesse());
                }
            }
            int neuerBestand = a.getBestand() + anzahl;
            a.setBestand(neuerBestand);
        } else {
            throw new ArtikelExistiertNichtException();
        }
    }

    /* 
    public int artikelBestandAusgleich(int artikelnummer, int anzahl)
            throws ArtikelMassengutartikelException, ArtikelExistiertNichtException {
        List<Artikel> ergebnisse = sucheArtikelnummer(artikelnummer);
        if (ergebnisse.size() == 1) {
            Artikel a = ergebnisse.get(0);
            if (a instanceof Massengutartikel) {
                Massengutartikel massengutartikel = (Massengutartikel) a;
                if (anzahl % massengutartikel.getPackungsGroesse() != 0) {
                    throw new ArtikelMassengutartikelException(a.getBezeichnung(),
                            massengutartikel.getPackungsGroesse());
                }
            }
            int neuerBestand = a.getBestand() - anzahl;
            return neuerBestand;
        } else {
            throw new ArtikelExistiertNichtException();
        }
    }*/

    /**
     * Methode um den Bestand des Artikels zu verringern
     *
     * @param bezeichnung   Bezeichnung des Artikels
     * @param artikelnummer Artikelnummer des Artikels
     * @param anzahl        die Anzahl, um welche der Bestand verringert werden soll
     *                      Liste wird durchsucht, falls das passende Objekt
     *                      vorhanden ist, wird der Bestand mit der Anzahl
     *                      verringert
     *                      und mit setBestand aktualisiert
     * @throws ArtikelExistiertNichtException
     */
    public void artikelBestandVerringern(int artikelnummer, int anzahl)
            throws BestandZuNiedrigException, ArtikelExistiertNichtException, ArtikelMassengutartikelException {
        List<Artikel> ergebnisse = sucheArtikelnummer(artikelnummer);
        if (ergebnisse.size() == 1) {
            Artikel a = ergebnisse.get(0);
            if (a instanceof Massengutartikel) {
                Massengutartikel massengutartikel = (Massengutartikel) a;
                if (anzahl % massengutartikel.getPackungsGroesse() != 0) {
                    throw new ArtikelMassengutartikelException(a.getBezeichnung(),
                            massengutartikel.getPackungsGroesse());
                }
            }
            int neuerBestand = a.getBestand() - anzahl;
            if (neuerBestand >= 0) {
                a.setBestand(neuerBestand);
            } else {
                throw new BestandZuNiedrigException(a, anzahl);
            }
        } else {
            throw new ArtikelExistiertNichtException();
        }
    }

    /**
     * Methode, die eine KOPIE des Artikelbestands zurückgibt.
     * (Eine Kopie ist eine gute Idee, wenn ich dem Empfänger
     * der Daten nicht ermöglichen möchte, die Original-Daten
     * zu manipulieren.)
     * ABER ACHTUNG: Die in der kopierten Artikelliste referenzierten
     * sind nicht kopiert worden, d.h. ursprüngliche
     * Artikelliste und ihre Kopie verweisen auf dieselben
     * Artikel-Objekte. Eigentlich müssten die einzelnen Artikel-Objekte
     * auch kopiert werden.
     *
     * @return Liste aller Artikel im Artikelbestand (Kopie)
     */
    public List<Artikel> getArtikelBestand() {
        return new ArrayList<>(artikelBestand);
    }

}