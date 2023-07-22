package domain;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;

import domain.exceptions.*;
import entities.Artikel;
import entities.Kunde;
import entities.Massengutartikel;
import entities.Mitarbeiter;
import entities.Rechnung;
import entities.Warenkorb;
import persistence.FilePersistenceManager;

public class EShop {

    private ArtikelVerwaltung artikelVW;
    private KundenVerwaltung kundenVW;
    private MitarbeiterVerwaltung mitarbeiterVW;
    private ShoppingService shoppingService;
    HashMap<Artikel, Integer> artikelMap;
    DecimalFormat df;

        // Persistenz-Schnittstelle, die für die Details des Dateizugriffs
    // verantwortlich ist
    private FilePersistenceManager pm = new FilePersistenceManager();

    /**
     * Konstruktor, der die Basisdaten aus Dateien einliest
     * (Initialisierung des EShops).
     *
     * @param datei Präfix für Dateien mit Basisdaten
     * @throws IOException       z.B. wenn eine der Dateien nicht existiert.
     * @throws DatenNichtGeladen
     */
    public EShop(String datei) throws IOException, DatenNichtGeladen {
        datei = "";

        // Artikelbestand aus Datei einlesen
        artikelVW = new ArtikelVerwaltung();
        kundenVW = new KundenVerwaltung();
        mitarbeiterVW = new MitarbeiterVerwaltung();
        shoppingService = new ShoppingService();
        artikelVW.liesDaten("ESHOP_A.txt");
        artikelMap = new HashMap<>();
        df = new DecimalFormat("0.00");
        kundenVW.ladeAlleKunden();
    }

    /* ***************** ARTIKELMETHODEN ******************* */
    /**
     * Methode zum Speichern des Artikelbestands in einer Datei.
     *
     * @throws IOException                   
     * @throws ArtikelMassengutartikelException
     */
    public void schreibeArtikel() throws IOException {
        artikelVW.schreibeDaten("ESHOP_A.txt");
    }

    /**
     * Methode, die eine Liste aller im Bestand befindlichen Artikel zurückgibt.
     *
     * @return Liste aller Artikel im Bestand des EShops
     */
    public List<Artikel> gibAlleArtikel() {
        return artikelVW.getArtikelBestand();
    }

    // Artikelmethode
    public List<String> gibArtikellisteAus(List<Artikel> liste) throws ListeLeerException {
        return artikelVW.gibArtikellisteAus(liste);
    }

    /**
     * Methode zum Suchen von Artikeln anhand der Bezeichnung und Artikelnummer. Es
     * wird eine Liste von Artikeln
     * zurückgegeben, die alle Artikel mit exakt übereinstimmendem Bezeichnung und
     * Artikelnummer enthält.
     *
     * @param bezeichnung Bezeichnung des gesuchten Artikels
     * @return Liste der gefundenen Artikeln (evtl. leer)
     */
    public List<Artikel> sucheNachArtikel(String bezeichnung) {
        // einfach delegieren an meineArtikel
        return artikelVW.sucheArtikelbezeichnung(bezeichnung);
    }

    public List<Artikel> sucheNachArtikelnummer(int artikelnummer) {
        // einfach delegieren an meineArtikel
        return artikelVW.sucheArtikelnummer(artikelnummer);
    }

    /**
     * Methode zum Einfügen eines neuen Artikels in den Bestand.
     * Wenn der Artikel bereits im Bestand ist, wird der Bestand nicht geändert.
     *
     * @param bezeichnung   Bezeichnung des Artikels
     * @param artikelnummer Artikelnummer des Artikels
     * @return Artikel-Objekt, das im Erfolgsfall eingefügt wurde
     * @throws ArtikelExistiertBereitsException wenn der Artikel bereits existiert
     * @throws IOException
     */
    public Artikel fuegeArtikelEin(String bezeichnung, int artikelnummer, boolean verfuegbar, double preis, int bestand,
            int packungsGroesse, String benutzername) throws ArtikelExistiertBereitsException, IOException {
        Artikel neuerArtikel;
        if (packungsGroesse > 1) {
            neuerArtikel = new Massengutartikel(bezeichnung, artikelnummer, preis, bestand, verfuegbar,
                    packungsGroesse);
        } else {
            neuerArtikel = new Artikel(bezeichnung, artikelnummer, preis, bestand, verfuegbar);
        }

        artikelVW.einfuegen(neuerArtikel);
        writeTimestampToFile(bezeichnung, bestand, benutzername, "Mitarbeiter-Artikel-hinzufuegen");
        return neuerArtikel;
    }

    /**
     * Methode um den Bestand des Artikels zu erhöhen
     *
     * @param bezeichnung   Bezeichnung des Artikels
     * @param artikelnummer Artikelnummer des Artikels
     * @param anzahl        die Anzahl, um welche der Bestand erhöht werden soll
     *                      Liste wird durchsucht, falls das passende Objekt
     *                      vorhanden ist, wird der Bestand mit der Anzahl erhöht
     *                      und mit setBestand aktualisiert
     * @param benutzername  eingeloggter Mitarbeiter
     * 
     * @return
     */
    public int erhoeheArtikelBestand(String bezeichnung, int artikelnummer, int anzahl, String benutzername)
            throws ArtikelMassengutartikelException, ArtikelExistiertNichtException, IOException {

        Artikel a = artikelVW.getArtikelByBezeichnungUndArtikelnummer(bezeichnung, artikelnummer);
        artikelVW.artikelBestandErhoehen(artikelnummer, anzahl);
        writeTimestampToFile(a.getBezeichnung(), a.getBestand(), benutzername,
                "Mitarbeiter-Artikelbestand-erhoehen");
        return anzahl;
    }

    /************************************WIRD NICHT VERWENDET 
    public int artikelBestandAusgleich(int artikelnummer, int anzahl)
            throws ArtikelMassengutartikelException, ArtikelExistiertNichtException {

        artikelVW.artikelBestandAusgleich(artikelnummer, anzahl);
        return anzahl;
    }*/

    /**
     * Methode zum Löschen eines Artikels aus dem Bestand.
     * Es wird nur das erste Vorkommen des Artikels gelöscht.
     *
     * @param bezeichnung   Bezeichnung des Artikels
     * @param artikelnummer Artikelnummer des Artikels
     * @param benutzername  eingeloggter Mitarbeiter
     * 
     * @return
     */
    public void loescheArtikel(String bezeichnung, int artikelnummer, String benutzername) throws ArtikelExistiertNichtException, IOException {
        
        Artikel a = artikelVW.getArtikelByBezeichnungUndArtikelnummer(bezeichnung, artikelnummer);
        if (a == null) {
            throw new ArtikelExistiertNichtException();
        }
        artikelVW.loeschen(a);

        writeTimestampToFile(bezeichnung, a.getBestand(), benutzername, "Mitarbeiter-Artikel-entfernen");
    }

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
     * @return
     * @throws ArtikelExistiertNichtException
     * @throws IOException
     */
    public int verringereArtikelBestand(String bezeichnung, int artikelnummer, int anzahl, String benutzername)
            throws BestandZuNiedrigException, ArtikelExistiertNichtException, ArtikelMassengutartikelException, IOException {

        Artikel a = artikelVW.getArtikelByBezeichnungUndArtikelnummer(bezeichnung, artikelnummer);
        artikelVW.artikelBestandVerringern(artikelnummer, anzahl);

        writeTimestampToFile(a.getBezeichnung(), a.getBestand(), benutzername, "Mitarbeiter-Artikelbestand-verringern");

        return anzahl;
    }

    /* ***************** WARENKORB *************************** */

    public String gibWarenkorb(Kunde kunde) {
        return shoppingService.zeigeWarenkorb(kunde);
    }

    public List<Artikel> gibAllesWarenkorb(Kunde kunde) {
        return shoppingService.getArtikelBestand(kunde);
    }

    public void warenkorbAnzahl(Kunde kunde, Artikel artikel) {
        shoppingService.warenkorbAnzahl(kunde, artikel);
    }

    public Artikel warenkorbArtikelHinzufuegen(Kunde kunde, String bezeichnung, int anzahl)
            throws BestandZuNiedrigException,
            ArtikelExistiertNichtException, ArtikelMassengutartikelException, BestandMussPositivSeinException,
            IOException {

        Artikel artikel = artikelVW.artikelbezeichnungSuchen(bezeichnung);
        shoppingService.artikelZuWarenkorbHinzufuegen(kunde, artikel, anzahl);
        return artikel;
    }

    public Artikel warenkorbArtikelEntfernen(Kunde kunde, String bezeichnung)
            throws ArtikelExistiertNichtException, ArtikelNichtImWarenkorbException, ArtikelMassengutartikelException {

        Artikel artikel = artikelVW.artikelbezeichnungSuchen(bezeichnung);
        shoppingService.artikelAusWarenkorbEntfernen(kunde, artikel);

        return artikel;
    }

    public void warenkorbErhoeheArtikelanzahl(Kunde kunde, String bezeichnung, int anzahl)
            throws ArtikelMassengutartikelException, BestandZuNiedrigException, ArtikelExistiertNichtException,
            ArtikelNichtImWarenkorbException, BestandMussPositivSeinException {

        Artikel artikel = artikelVW.artikelbezeichnungSuchen(bezeichnung);
        shoppingService.erhoeheArtikelanzahl(kunde, artikel, anzahl);
    }

    public int warenkorbErhoeheArtikelanzahl2(Kunde kunde, String bezeichnung, int anzahl)
            throws ArtikelMassengutartikelException, BestandZuNiedrigException, ArtikelExistiertNichtException,
            ArtikelNichtImWarenkorbException, BestandMussPositivSeinException {

        Artikel artikel = artikelVW.artikelbezeichnungSuchen(bezeichnung);
        shoppingService.erhoeheArtikelanzahl(kunde, artikel, anzahl);
        return anzahl;
    }

    public void warenkorbVerringereArtikelanzahl(Kunde kunde, String bezeichnung, int anzahl)
            throws ArtikelMassengutartikelException, ArtikelExistiertNichtException, ArtikelNichtImWarenkorbException,
            AnzahlImWarenkorbZuNiedrigException, BestandMussPositivSeinException {

        Artikel artikel = artikelVW.artikelbezeichnungSuchen(bezeichnung);
        shoppingService.verringereArtikelanzahl(kunde, artikel, anzahl);

    }

    public Artikel warenkorbVerringereArtikelanzahl2(Kunde kunde, String bezeichnung, int anzahl)
            throws ArtikelMassengutartikelException, ArtikelExistiertNichtException, ArtikelNichtImWarenkorbException,
            AnzahlImWarenkorbZuNiedrigException, BestandMussPositivSeinException {

        Artikel artikel = artikelVW.artikelbezeichnungSuchen(bezeichnung);
        shoppingService.verringereArtikelanzahl(kunde, artikel, anzahl);
        return artikel;
    }

    public void warenkorbLeeren(Kunde kunde) throws ArtikelMassengutartikelException {

        shoppingService.leereWarenkorb(kunde);

    }

    public String printRechnung(EShop eshop, Kunde kunde, Warenkorb warenkorb)
            throws BestandZuNiedrigException,
            ArtikelExistiertNichtException, ArtikelMassengutartikelException, IOException {
        Rechnung rechnung = new Rechnung(eshop, kunde, warenkorb);
        String ausgabe = rechnung.erstelleRechnung(kunde);
        return ausgabe;
    }

    public void printTable() {
        pm.generateTableFromProtocol();
    }

    /* ***************** LOGIN *************************** */

    public Kunde anmeldenKunden(String username, String password)
            throws KundeExistiertNicht, DatenNichtGeladen {
        return kundenVW.einloggenKunde(username, password);
    }

    public Kunde registrieren(String benutzername,
            String passwort, String name, String strasse, String plz, String wohnort)
            throws DatenNichtGespeichert, NummerListeException, NummerNichtGespeichert {
        int kundenNr = kundenVW.nummerBestimmen();
        Kunde kunde = kundenVW.kundeHinzufuegen(benutzername, passwort, name, strasse, plz, wohnort, kundenNr);
        return kunde;
    }

    public Mitarbeiter mitarbeiterHinzufuegen(String username, String password)
            throws DatenNichtGespeichert, NummerNichtGespeichert, NummerListeException {
        int mitarbeiterNr = mitarbeiterVW.nummerBestimmen();
        Mitarbeiter mitarbeiter = mitarbeiterVW.mitarbeiterHinzufuegen(username, password, mitarbeiterNr);
        return mitarbeiter;
    }

    public Mitarbeiter anmeldenMitarbeiter(String username, String password)
            throws MitarbeiterExistiertNicht, DatenNichtGeladen {
        return mitarbeiterVW.einloggenMitarbeiter(username, password);
    }

    public void ersterMitarbeiter()
            throws DatenNichtGespeichert, ErsterMitarbeiter, NummerNichtGespeichert, NummerListeException {
        mitarbeiterVW.initialisiereSystem();
    }

    public void loescheAccountMitarbeiter(Mitarbeiter mitarbeiter) throws AccountNichtGefunden, IOException {
        mitarbeiterVW.loescheMitarbeiterAccount(mitarbeiter);
    }

    public int getKundeIdentifikationsNummer(String benutzername) throws KundeExistiertNicht {
        return kundenVW.getIdentifikationsNummerKunde(benutzername);
    }

    public boolean Benutzernamevorhanden(String benutzername) throws KundeExistiertBereitsException, DatenNichtGeladen {
        if (kundenVW.benutzernameVorhanden(benutzername) == true) {
            return true;
        } else {
            return false;
        }
    }

    public boolean BenutzernamevorhandenMiatrbeiter(String benutzername)
            throws MitarbeiterExistiertBereitsException, DatenNichtGeladen {
        if (mitarbeiterVW.benutzernameVorhanden(benutzername) == true) {
            return true;
        } else {
            return false;
        }
    }

    /***************** Protokoll *********************/
    /**
     * Methode um Einträge in der log.txt zu generieren
     *
     * @param artikel   Bezeichnung des Artikels
     * @param anzahl    Bestand des Artikels
     * @param person    Name des eingeloggten Nutzers
     * @param action    Ausgeführte Aktion
     * 
     */
    public void writeTimestampToFile(String artikel, Integer anzahl, String person, String action) throws IOException {
        
        FilePersistenceManager persistenceManager = new FilePersistenceManager("log.txt");
        persistenceManager.schreibeProtokoll(artikel, anzahl, person, action);

    }
}