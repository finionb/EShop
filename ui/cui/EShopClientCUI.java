package ui.cui;

import java.io.*;
import java.text.*;
import java.util.*;

import domain.exceptions.*;
import domain.EShop;
import entities.Artikel;
import entities.Kunde;
import entities.Mitarbeiter;
import entities.Personen;
import entities.Warenkorb;
import persistence.FilePersistenceManager;

public class EShopClientCUI {

    private EShop eshop; // EShop Objekt
    private BufferedReader in;
    DecimalFormat df;
    FilePersistenceManager file = new FilePersistenceManager();

    Kunde eingeloggterKunde = null;
    Mitarbeiter eingeloggterMitarbeiter = null;

    // HashMap für Accouts
    private static HashMap<String, String> kunden = new HashMap<String, String>();
    private static HashMap<String, String> mitarbeiter = new HashMap<String, String>();

    // Die main-Methode
    public static void main(String[] args) throws IOException, BestandZuNiedrigException,
            ArtikelExistiertNichtException, ArtikelMassengutartikelException, DatenNichtGeladen, NummerListeException,
            NummerNichtGespeichert, BestandMussPositivSeinException {
        EShopClientCUI cui;
        cui = new EShopClientCUI("ESHOP");
        cui.menuePosition();
    }

    public EShopClientCUI(String datei) throws IOException, DatenNichtGeladen {
        // die EShop-Verwaltung erledigt die Aufgaben,
        // die nichts mit Ein-/Ausgabe zu tun haben
        eshop = new EShop(datei);

        // Stream-Objekt fuer Texteingabe ueber Konsolenfenster erzeugen
        in = new BufferedReader(new InputStreamReader(System.in));
    }

    // Methode zum Einlesen von Benutzereingaben (nur String!)
    private String liesEingabe() throws IOException {
        return in.readLine();
    }

    // 1.Menüausgabe
    // Hier wird die Position (Kunde oder Mitarbeiter) abgefragt und anschließend
    // das entsprechende menü aufgerufen
    public void menuePosition() throws IOException, BestandZuNiedrigException,
            ArtikelExistiertNichtException, ArtikelMassengutartikelException, DatenNichtGeladen, NummerListeException,
            NummerNichtGespeichert, BestandMussPositivSeinException {
        System.out.println("---------------------------------------------");
        System.out.println("Fortfahren als: \n 1. Kunde \n 2. Mitarbeiter");
        System.out.println("Bitte geben Sie die jeweilige Zahl ein [1|2].");
        System.out.println("Geben Sie 0 zum Beenden ein.");
        System.out.println("---------------------------------------------");
        System.out.print("> "); // Prompt
        System.out.flush(); // ohne NL ausgeben

        String nummer1 = liesEingabe();
        try {
            int eingabePosition = Integer.parseInt(nummer1);

            switch (eingabePosition) {
                case 1: // Kunde
                    logInKunde(); // Einlogg-Menü vom Kunden (Fortfahren als Kunde)
                    break;
                case 2: // Mitarbeiter
                    logInMitarbeiter(); // Einlogg-Menü vom Mitarbeiter (Fortfahren als Mitarbeiter)
                    break;
                case 0: // Beenden
                    System.out.println("Programm wird beendet.");
                    System.exit(0); // Beendet das Programm
                    break;
                default:
                    System.out.println("Ungültige Eingabe.");
                    menuePosition();
                    break;
            }
        } catch (NumberFormatException e) {
            System.out.println("Ungültige Eingabe. Bitte geben Sie eine Zahl ein.");
            menuePosition();
        }
    }

    // Einlogg-Menü Kunde
    public void logInKunde() throws IOException, BestandZuNiedrigException,
            ArtikelExistiertNichtException, ArtikelMassengutartikelException, DatenNichtGeladen, NummerListeException,
            NummerNichtGespeichert, BestandMussPositivSeinException {

        int eingabeKunde;
        String eingabeKundeString;

        System.out.println("---------------------------------------------");
        System.out.println("1. Registrieren");
        System.out.println("2. Einloggen");
        System.out.println("3. Account löschen");
        System.out.println("4. Zurück");
        System.out.println("Bitte geben Sie eine Zahl ein: ");
        System.out.println("---------------------------------------------");
        System.out.print("> "); // Prompt
        System.out.flush(); // ohne NL ausgeben
        try {
            String nummer2 = liesEingabe();
            int choice1 = Integer.parseInt(nummer2);

            switch (choice1) {

                case 1: // Registrierung
                    System.out.print("Geben Sie den Benutzernamen ein: ");
                    String Benutzername = liesEingabe();
                    System.out.print("Geben Sie das Passwort ein: ");
                    String passwort = liesEingabe();
                    if (kunden.containsKey(Benutzername)) {
                        System.out.println("Ein Account mit diesem Benutzernamen existiert bereits.");
                    } else {
                        kunden.put(Benutzername, passwort);
                        System.out.print("Vor- und Nachname: ");
                        String name = liesEingabe();
                        System.out.print("Strasse: ");
                        String strasse = liesEingabe();
                        System.out.print("PLZ: ");
                        String plz = liesEingabe();
                        System.out.print("Wohnort: ");
                        String wohnort = liesEingabe();
                        try {
                            eshop.registrieren(Benutzername, passwort, name, strasse, plz, wohnort);
                            System.out.println("Account erfolgreich erstellt.");
                        } catch (DatenNichtGespeichert e) {
                            System.out.println("Fehler" + e.getMessage());
                        }
                    }
                    logInKunde();
                    break;
                case 2: // Einloggen
                    System.out.print("Geben Sie den Benutzernamen ein: ");
                    String username = liesEingabe();
                    System.out.print("Geben Sie das Passwort ein: ");
                    String password = liesEingabe();
                    try {
                        eingeloggterKunde = eshop.anmeldenKunden(username, password);
                        do {
                            kundenMenue();
                            eingabeKundeString = liesEingabe();
                            eingabeKunde = Integer.parseInt(eingabeKundeString);
                            kundenOptionen(eingabeKunde, eingeloggterKunde);
                        } while (eingabeKunde != 12);
                    } catch (KundeExistiertNicht e) {
                        System.out.println("Fehler: " + e.getMessage());
                        logInKunde();
                    }

                    break;
                case 3: // zurück
                    menuePosition();
                    break;
                default:
                    System.out.println("Ungültige Eingabe.");
                    logInKunde();
                    break;
            }
        } catch (

        NumberFormatException e) {
            System.out.println("Ungültige Eingabe. Bitte geben Sie eine Zahl ein.");
            logInKunde();
        }
    }

    // Einlogg-Menü Mitarbeiter
    public void logInMitarbeiter() throws IOException, BestandZuNiedrigException,
            ArtikelExistiertNichtException, ArtikelMassengutartikelException, NummerNichtGespeichert,
            NummerListeException, DatenNichtGeladen, BestandMussPositivSeinException {

        int choice2;
        String choice2String;
        int eingabeMitarbeiter;
        String eingabeMitarbeiterString;
        String username;
        String password;

        System.out.println("---------------------------------------------");
        System.out.println("1. Einloggen");
        System.out.println("2. Zurück");
        System.out.println("Bitte geben Sie eine Zahl ein: ");
        System.out.println("---------------------------------------------");
        System.out.print("> "); // Prompt
        System.out.flush(); // ohne NL ausgeben
        try {
            choice2String = liesEingabe();
            choice2 = Integer.parseInt(choice2String);

            switch (choice2) {
                case 1: // Einloggen
                    try {
                        eshop.ersterMitarbeiter();
                    } catch (DatenNichtGespeichert e) {
                        System.out.println("Der oder die erste/r Mitarbeiter/in konnten nicht gespeichert werden");
                    } catch (ErsterMitarbeiter c) {
                        System.out.println("Fehler" + c.getMessage());
                    }
                    System.out.print("Geben Sie den Benutzernamen ein: ");
                    username = liesEingabe();
                    System.out.print("Geben Sie das Passwort ein: ");
                    password = liesEingabe();
                    try {
                        eingeloggterMitarbeiter = eshop.anmeldenMitarbeiter(username, password);
                        if (eingeloggterMitarbeiter instanceof Mitarbeiter) {
                            Mitarbeiter eingeloggtermitarbeiter = (Mitarbeiter) eingeloggterMitarbeiter;
                            System.out.println("Erfolgreich eingeloggt " + eingeloggtermitarbeiter.getBenutzername());
                            do {
                                mitarbeiterMenue();
                                eingabeMitarbeiterString = liesEingabe();
                                eingabeMitarbeiter = Integer.parseInt(eingabeMitarbeiterString);
                                mitarbeiterOptionen(eingabeMitarbeiter, eingeloggterMitarbeiter);
                            } while (eingabeMitarbeiter != 11);
                        }
                    } catch (MitarbeiterExistiertNicht e) {
                        System.out.println("Fehler: " + e.getMessage());
                        logInMitarbeiter();
                    }
                    break;
                case 2: // zurück
                    menuePosition();
                    break;
                default:
                    System.out.println("Ungültige Eingabe.");
                    logInMitarbeiter();
                    break;
            }
        } catch (NumberFormatException e) {
            System.out.println("Ungültige Eingabe. Bitte geben Sie eine Zahl ein.");
            logInMitarbeiter();
        }
    }

    // 2.Menüausgabe
    // Mitarbeiter-Menü
    public void mitarbeiterMenue() throws IOException {
        System.out.println("---------------------------------------------");
        System.out.println("1. Artikel anzeigen");
        System.out.println("2. Artikel löschen");
        System.out.println("3. Artikel hinzufügen");
        System.out.println("4. Artikel suchen");
        System.out.println("5. Artikelbestand ändern - erhöhen");
        System.out.println("6. Artikelbestand ändern - verringern");
        System.out.println("7. Mitarbeiter hinzufügen");
        System.out.println("8. Account löschen");
        System.out.println("9. Daten sichern");
        System.out.println("10. Lagererignisse");
        System.out.println("11. Abmelden");
        System.out.println("12. Beenden");
        System.out.println("---------------------------------------------");
        System.out.println("Bitte geben Sie die jeweilige Zahl ein [1-12]:");
        System.out.print("> "); // Prompt
        System.out.flush(); // ohne NL ausgeben
    }

    // Methode für mögliche Aktivitäten des Mitarbeiters
    private void mitarbeiterOptionen(int eingabeMitarbeiter, Mitarbeiter eingeloggterMitarbeiter)
            throws IOException, BestandZuNiedrigException, ArtikelExistiertNichtException,
            ArtikelMassengutartikelException, NummerNichtGespeichert, NummerListeException, DatenNichtGeladen, BestandMussPositivSeinException {

        int artikelnummer;
        String artikelnummerString;
        String bezeichnung;
        boolean verfuegbar;
        String preisString;
        double preis;
        String bestandString;
        int bestand;
        try {
            String packungsGroesseString;
            int packungsGroesse;
            switch (eingabeMitarbeiter) {
                case 1: // Artikel anzeigen
                    try {
                        List<String> artikelListe = eshop.gibArtikellisteAus(eshop.gibAlleArtikel());
                        for (String artikel : artikelListe) {
                            System.out.println(artikel);
                        }
                    } catch (ListeLeerException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 2: // Artikel löschen
                    bestand = 0;
                    System.out.print("Artikelnummer: ");
                    artikelnummerString = liesEingabe();
                    artikelnummer = Integer.parseInt(artikelnummerString);
                    System.out.print("Artikelbezeichnung: ");
                    bezeichnung = liesEingabe();
                    try {
                        eshop.loescheArtikel(bezeichnung, artikelnummer, eingeloggterMitarbeiter.getBenutzername());
                    } catch (ArtikelExistiertNichtException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 3: // Artikel hinzufügen
                    System.out.print("Artikelnummer: ");
                    artikelnummerString = liesEingabe();
                    artikelnummer = Integer.parseInt(artikelnummerString);
                    System.out.print("Artikelbezeichnung: ");
                    bezeichnung = liesEingabe();
                    System.out.print("Artikelpreis: ");
                    preisString = liesEingabe();
                    preis = Double.parseDouble(preisString);
                    System.out.print("Artikelbestand: ");
                    bestandString = liesEingabe();
                    bestand = Integer.parseInt(bestandString);

                    if (bestand > 0) {
                        verfuegbar = true;
                    } else {
                        verfuegbar = false;
                    }

                    System.out.print("Packungsgröße (bei Einzelartikel 1 eingeben): ");
                    packungsGroesseString = liesEingabe();
                    packungsGroesse = Integer.parseInt(packungsGroesseString);

                    try {
                        eshop.fuegeArtikelEin(bezeichnung, artikelnummer, verfuegbar, preis, bestand, packungsGroesse, eingeloggterMitarbeiter.getBenutzername());
                        System.out.println("Artikel wurde hinzugefügt.");
                    } catch (ArtikelExistiertBereitsException e) {
                        System.out.println("Fehler beim Einfügen: " + e.getMessage());
                    }
                    break;
                case 4: // Artikel suchen
                    System.out.print("Artikelbezeichnung: ");
                    bezeichnung = liesEingabe();
                    try {
                        List<String> suchergebnisListe = eshop.gibArtikellisteAus(eshop.sucheNachArtikel(bezeichnung));
                        for (String artikel : suchergebnisListe) {
                            System.out.println(artikel);
                        }
                    } catch (ListeLeerException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 5: // Artikelbestand ändern - erhöhen
                    System.out.print("Artikelnummer: ");
                    artikelnummerString = liesEingabe();
                    System.out.print("Artikelbezeichnung: ");
                    bezeichnung = liesEingabe();
                    artikelnummer = Integer.parseInt(artikelnummerString);
                    System.out.print("Anzahl, um die der Bestand erhöht werden soll: ");
                    bestandString = liesEingabe();
                    bestand = Integer.parseInt(bestandString);

                    try {
                        eshop.erhoeheArtikelBestand(bezeichnung, artikelnummer, bestand, eingeloggterMitarbeiter.getBenutzername());
                        System.out.println("Bestand erfolgreich erhöht.");
                    } catch (ArtikelMassengutartikelException e) {
                        System.out.println("Ein- und Auslagerungen sind nur in Vielfachen der Packungsgröße möglich.");
                    } catch (ArtikelExistiertNichtException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 6: // Artikelbestand ändern - verringern
                    System.out.print("Artikelnummer: ");
                    artikelnummerString = liesEingabe();
                    System.out.print("Artikelbezeichnung: ");
                    bezeichnung = liesEingabe();
                    artikelnummer = Integer.parseInt(artikelnummerString);
                    System.out.print("Anzahl, um die der Bestand verringert werden soll: ");
                    bestandString = liesEingabe();
                    bestand = Integer.parseInt(bestandString);

                    try { 
                        eshop.verringereArtikelBestand(bezeichnung, artikelnummer, bestand, eingeloggterMitarbeiter.getBenutzername());
                        System.out.println("Bestand erfolgreich verringert.");
                    } catch (BestandZuNiedrigException e) {
                        System.out.println("Der Bestand kann nicht unter null fallen.");
                    } catch (ArtikelExistiertNichtException e) {
                        System.out.println(e.getMessage());
                    } catch (ArtikelMassengutartikelException e) {
                        System.out.println("Ein- und Auslagerungen sind nur in Vielfachen der Packungsgröße möglich.");
                    }
                    break;
                case 7: // Mitarbeiter hinzufügen
                    System.out.print("Geben Sie den Benutzernamen ein: ");
                    String username = liesEingabe();
                    System.out.print("Geben Sie das Passwort ein: ");
                    String password = liesEingabe();
                    if (mitarbeiter.containsKey(username)) {
                        System.out.println("Ein Account mit diesem Benutzernamen existiert bereits.");
                    } else {
                        mitarbeiter.put(username, password);
                        try {
                            eshop.mitarbeiterHinzufuegen(username, password);
                            System.out.println("Account erfolgreich erstellt.");
                        } catch (DatenNichtGespeichert e) {
                            System.out.println("Fehler:" + e.getMessage());
                        }
                    }

                    break;
                case 8: // Account löschen
                    try {
                        eshop.loescheAccountMitarbeiter(eingeloggterMitarbeiter);
                        System.out.println("Ihr Account wurde Erfolgreich gelöscht");
                    } catch (AccountNichtGefunden e) {
                        System.out.println(e.getMessage());
                    }
                    logInMitarbeiter();
                    break;
                case 9: // Daten sichern
                    eshop.schreibeArtikel();
                    break;
                case 10: //Lagerereignisse anzeigen
                    System.out.println("Lagereignisse: ");
                    eshop.printTable();
                    break;
                case 11:
                    eingeloggterMitarbeiter = null;
                    System.out.println("Erfolgreich ausgeloggt.");
                    logInMitarbeiter();
                    break;
                case 12:
                    System.out.println("Wir wünschen Ihnen noch einen schönen Tag!");
                    break;
                default:
                    System.out.println("Ungültige Eingabe.");
                    break;
            }
        } catch (NumberFormatException e) {
            System.out.println("Ungültige Eingabe. Bitte geben Sie eine Zahl ein.");
            mitarbeiterMenue();
        }
    }

    // Kunden-Menü
    public void kundenMenue() throws IOException {
        System.out.println("---------------------------------------------");
        System.out.println("1. Artikel anzeigen");
        System.out.println("2. Artikel suchen");
        System.out.println("3. Warenkorb anzeigen");
        System.out.println("4. Artikel zum Warenkorb hinzufügen");
        System.out.println("5. Artikel aus Warenkorb entfernen");
        System.out.println("6. Artikelanzahl im Warenkorb erhöhen");
        System.out.println("7. Artikelanzahl im Warenkorb verringern");
        System.out.println("8. Warenkorb leeren");
        System.out.println("9. Warenkorb kaufen");
        System.out.println("10. Account löschen");
        System.out.println("11. Abmelden");
        System.out.println("12. Beenden");
        System.out.println("---------------------------------------------");
        System.out.println("Bitte geben Sie die jeweilige Zahl ein [1-10]: ");
        System.out.print("> "); // Prompt
        System.out.flush(); // ohne NL ausgeben
    }

    // Methode für mögliche Aktivitäten des Kunden
    private void kundenOptionen(int eingabeKunde, Personen eingeloggterNutzer)
            throws IOException, BestandZuNiedrigException,
            ArtikelExistiertNichtException, ArtikelMassengutartikelException, BestandMussPositivSeinException, DatenNichtGeladen, NummerListeException, NummerNichtGespeichert {

        String bezeichnung = "";
        String anzahlString;
        int anzahl = 0;
        try {
            int anzahlVerringern;
            switch (eingabeKunde) {
                case 1: // Artikel anzeigen
                    try {
                        List<String> artikelListe = eshop.gibArtikellisteAus(eshop.gibAlleArtikel());
                        for (String artikel : artikelListe) {
                            System.out.println(artikel);
                        }
                    } catch (ListeLeerException e) {
                        System.out.println(e.getMessage());
                    }

                    break;
                case 2: // Artikel suchen
                    System.out.print("Artikelbezeichnung: ");
                    bezeichnung = liesEingabe();
                    try {
                        List<String> suchergebnisListe = eshop.gibArtikellisteAus(eshop.sucheNachArtikel(bezeichnung));
                        for (String artikel : suchergebnisListe) {
                            System.out.println(artikel);
                        }
                    } catch (ListeLeerException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 3: // Warenkorb anzeigen
                    System.out.println("Ihr Warenkorb:");
                    String warenkorbAnzeige = eshop.gibWarenkorb((Kunde) eingeloggterNutzer);
                    System.out.println(warenkorbAnzeige);
                    break;
                case 4: // Artikel zum Warenkorb hinzufügen
                    System.out.print("Artikelbezeichnung: ");
                    bezeichnung = liesEingabe();
                    System.out.print("Anzahl der Artikel: ");
                    anzahlString = liesEingabe();
                    anzahl = Integer.parseInt(anzahlString);
                    try {
                        Artikel hinzugefuegterArtikel = eshop.warenkorbArtikelHinzufuegen((Kunde) eingeloggterNutzer, bezeichnung,
                                anzahl);
                        if (hinzugefuegterArtikel != null) {
                            System.out.println("Artikel erfolgreich zum Warenkorb hinzugefügt.");
                            eshop.warenkorbAnzahl((Kunde) eingeloggterNutzer, hinzugefuegterArtikel);
                        } else {
                            System.out.println("Der Artikel wurde nicht zum Warenkorb hinzugefügt.");
                        }
                        // erfolgreichHinzugefuegt = eshop.warenkorbArtikelHinzufuegen((Kunde)
                        // eingeloggterNutzer,
                        // bezeichnung, anzahl);
                        // if (erfolgreichHinzugefuegt) {
                        // System.out.println("Artikel erfolgreich zum Warenkorb hinzugefügt.");
                        // } else {
                        // System.out.println("Der Artikel wurde nicht zum Warenkorb hinzugefügt.");
                        // }
                    } catch (BestandZuNiedrigException e) {
                        System.out.println(e.getMessage());
                        System.out.println("Der Artikel wurde nicht zum Warenkorb hinzugefügt.");
                    } catch (ArtikelExistiertNichtException e) {
                        System.out.println(e.getMessage());
                    } catch (ArtikelMassengutartikelException e) {
                        System.out.println(e.getMessage());
                        System.out.println("Der Artikel wurde nicht zum Warenkorb hinzugefügt.");
                    }
                    break;
                case 5: // Artikel aus Warenkorb entfernen
                    System.out.print("Artikelbezeichnung: ");
                    bezeichnung = liesEingabe();

                    try {
                        eshop.warenkorbArtikelEntfernen((Kunde) eingeloggterNutzer, bezeichnung);
                        System.out.println("Artikel erfolgreich aus dem Warenkorb entfernt.");
                    } catch (ArtikelExistiertNichtException e) {
                        System.out.println(e.getMessage());
                    } catch (ArtikelNichtImWarenkorbException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 6: // Artikelanzahl im Warenkorb erhöhen
                    System.out.print("Artikelbezeichnung: ");
                    bezeichnung = liesEingabe();
                    System.out.print("Erhöhung der Anzahl: ");
                    anzahlString = liesEingabe();
                    anzahl = Integer.parseInt(anzahlString);

                    try {
                        eshop.warenkorbErhoeheArtikelanzahl((Kunde) eingeloggterNutzer, bezeichnung, anzahl);
                        System.out.println("Artikelanzahl erfolgreich erhöht.");
                    } catch (ArtikelMassengutartikelException e) {
                        System.out.println(e.getMessage());
                        System.out.println("Die Artikelanzahl wurde nicht erhöht.");
                    } catch (BestandZuNiedrigException e) {
                        System.out.println(e.getMessage());
                        System.out.println("Die Artikelanzahl wurde nicht erhöht.");
                    } catch (ArtikelExistiertNichtException e) {
                        System.out.println(e.getMessage());
                    } catch (ArtikelNichtImWarenkorbException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 7: // Artikelanzahl im Warenkorb verringern
                    System.out.print("Artikelbezeichnung: ");
                    bezeichnung = liesEingabe();
                    System.out.print("Anzahl verringern um: ");
                    anzahlString = liesEingabe();
                    anzahlVerringern = Integer.parseInt(anzahlString);

                    try {
                        eshop.warenkorbVerringereArtikelanzahl((Kunde) eingeloggterNutzer, bezeichnung,
                                anzahlVerringern);
                        System.out.println("Artikelanzahl erfolgreich verringert.");
                    } catch (AnzahlImWarenkorbZuNiedrigException e) {
                        System.out.println(e.getMessage());
                    } catch (ArtikelMassengutartikelException e) {
                        System.out.println(e.getMessage());
                        System.out.println("Die Artikelanzahl wurde nicht erhöht.");
                    } catch (ArtikelExistiertNichtException e) {
                        System.out.println(e.getMessage());
                    } catch (ArtikelNichtImWarenkorbException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 8: // Warenkorb leeren
                    eshop.warenkorbLeeren((Kunde) eingeloggterNutzer);
                    System.out.println("Warenkorb geleert.");
                    break;
                case 9: // Warenkorb kaufen + Rechnung
                    Warenkorb warenkorb = ((Kunde) eingeloggterNutzer).getWarenkorb();
                    String ausgabe = eshop.printRechnung(eshop, (Kunde) eingeloggterNutzer, warenkorb);
                    System.out.println(ausgabe);
                    break;
                case 10: // Account löschen
                    System.out.println("Wird noch hinzugefügt");
                    logInKunde();
                    break;
                case 11:
                    eingeloggterKunde = null;
                    System.out.println("Erfolgreich ausgeloggt.");
                    logInKunde();
                    break;
                case 12:
                    System.out.println("Vielen Dank für Ihren Einkauf! \nWir wünschen Ihnen noch einen schönen Tag!");
                    break;
                default:
                    System.out.println("Ungültige Eingabe.");
                    break;
            }
        } catch (NumberFormatException e) {
            System.out.println("Ungültige Eingabe. Bitte geben Sie eine Zahl ein.");
            kundenMenue();
        }
    }

}