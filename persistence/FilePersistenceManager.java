package persistence;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import domain.exceptions.DatenNichtGeladen;
import domain.exceptions.DatenNichtGespeichert;
import domain.exceptions.ErsterMitarbeiter;
import domain.exceptions.NummerListeException;
import domain.exceptions.NummerNichtGespeichert;
import entities.Artikel;
import entities.Kunde;
import entities.Massengutartikel;
import entities.Mitarbeiter;

/**
 * Realisierung einer Schnittstelle zur persistenten Speicherung von
 * Daten in Dateien.
 * 
 * @see bib.local.persistence.PersistenceManager
 */
public class FilePersistenceManager {

    private static final String KUNDEN_DATEI = "kunden_data.txt";
    private static final String MITARBEITER_DATEI = "mitarbeiter_data.txt";
    private static final String NUMMER_DATEI = "nummer.txt";
    private static List<Integer> NrList;
    private static int nextnr;
    private List<Kunde> kListe = new ArrayList<>();
    private List<Mitarbeiter> mitarbeiterListe = new ArrayList<>();
    private String fileName;

    public FilePersistenceManager() {
        // leerer Konstruktor
    }

    // Konstruktor für das Protokoll
    public FilePersistenceManager(String fileName) {
        this.fileName = fileName;
    }

    public void schreibeProtokoll(String artikel, Integer anzahl, String person, String action) throws IOException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timestamp = dateFormat.format(new Date(System.currentTimeMillis()));

        FileWriter writer = new FileWriter(fileName, true);
        writer.write("# Timestamp: " + timestamp + " # Artikel: " + artikel + " # Anzahl: " + anzahl
                + " # Verantwortliche/r: " + person + " # Aktion: " + action + "\n");
        writer.close();
    }

    public void generateTableFromProtocol() {
        List<String[]> data = readDataFromFile("log.txt");
        // Header
        System.out.printf("%-12s%-20s%-20s%-20s\n", "Timestamp", "Artikel", "Verantwortliche/r", "Aktion");

        // Trennlinie
        for (int i = 0; i < 74; i++) {
            System.out.print("-");
        }
        System.out.println();

        // Daten
        for (String[] row : data) {
            System.out.printf("%-12s%-20s%-20s%-20s\n", row[0], row[1], row[2], row[3]);
        }
    }

    private BufferedReader reader = null;
    private PrintWriter writer = null;

    public void openForReading(String datei) throws FileNotFoundException {
        reader = new BufferedReader(new FileReader(datei));
    }

    public void openForWriting(String datei) throws IOException {
        writer = new PrintWriter(new BufferedWriter(new FileWriter(datei)));
    }

    public boolean close() {
        if (writer != null)
            writer.close();

        if (reader != null) {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        return true;
    }

    public Artikel ladeArtikel() throws IOException {
        String artikelData = liesZeile();
        if (artikelData == null) {
            return null;
        }

        String[] artikelValues = artikelData.split(" # ");
        String bezeichnung = artikelValues[0];
        int artikelnummer = Integer.parseInt(artikelValues[1]);
        double preis = Double.parseDouble(artikelValues[2]);
        int bestand = Integer.parseInt(artikelValues[3]);
        boolean verfuegbar = artikelValues[4].equals("t");
        boolean massengutartikel = artikelValues[5].equals("t");

        if (massengutartikel) {
            int packungsGroesse = Integer.parseInt(artikelValues[6]);
            return new Massengutartikel(bezeichnung, artikelnummer, preis, bestand, verfuegbar, packungsGroesse);
        } else {
            return new Artikel(bezeichnung, artikelnummer, preis, bestand, verfuegbar);
        }
    }

    public boolean speichereArtikel(Artikel a) throws IOException {
        StringBuilder artikelData = new StringBuilder();
        artikelData.append(a.getBezeichnung()).append(" # ")
                .append(a.getArtikelnummer()).append(" # ")
                .append(a.getPreis()).append(" # ")
                .append(a.getBestand()).append(" # ")
                .append(a.isVerfuegbar() ? "t" : "f").append(" # ");

        if (a instanceof Massengutartikel) {
            artikelData.append("t").append(" # ")
                    .append(((Massengutartikel) a).getPackungsGroesse());
        } else {
            artikelData.append("f");
        }

        schreibeZeile(artikelData.toString());
        return true;
    }

    public void speichereKundenData(Kunde kunde) throws DatenNichtGespeichert {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(KUNDEN_DATEI, true))) {
            writer.write(kunde.getBenutzername() + " # "
                    + kunde.getPasswort() + " # " + kunde.getName()
                    + " ," + kunde.getStrasse() + " # " + kunde.getPlz() + " # " + kunde.getWohnort() + " # "
                    + kunde.getIdentifikationsNummer());
            writer.newLine();
        } catch (IOException e) {
            throw new DatenNichtGespeichert();
            // DatenNichtGespeichert
        }
    }

    public List<Kunde> ladeAlleKunden() throws DatenNichtGeladen {
        try (BufferedReader reader = new BufferedReader(new FileReader(KUNDEN_DATEI))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" # ");
                String storedUsername = parts[0];
                String storedPassword = parts[1];
                String storedName = parts[2];
                String storedStreet = parts[3];
                String storedPLZ = parts[4];
                String storedCity = parts[5];
                int storedIdentificationNumber = Integer.parseInt(parts[6]);
                Kunde k = new Kunde(storedUsername, storedPassword, storedName, storedStreet, storedPLZ, storedCity,
                        storedIdentificationNumber);
                kListe.add(k);
            }
        } catch (IOException e) {
            throw new DatenNichtGeladen();
            // DatenNichtGeladen
        }

        return kListe;
    }

    public int nextnr() throws NummerListeException {
        int nextnr = 1; // Anfangswert, wenn die Datei leer ist
        try (BufferedReader reader = new BufferedReader(new FileReader(NUMMER_DATEI))) {
            NrList = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                NrList.add(Integer.parseInt(line));
            }

            if (!NrList.isEmpty()) {
                nextnr = NrList.get(NrList.size() - 1);
            } else {
                nextnr = 1;
            }
        } catch (IOException e) {
            throw new NummerListeException();
            // NummerListeException
        }

        return nextnr;
    }

    public void speichereNextNr() throws NummerNichtGespeichert, NummerListeException {
        nextnr = nextnr() + 1;
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(NUMMER_DATEI, true));
            writer.write(Integer.toString(nextnr) + "\n");
            writer.close();
        } catch (IOException e) {
            throw new NummerNichtGespeichert();
            // NummerNichtGespeichert
        }
    }

    public void speichereMitarbeiterData(Mitarbeiter mitarbeiter) throws DatenNichtGespeichert {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(MITARBEITER_DATEI, true))) {
            String data = mitarbeiter.getBenutzername() + " # " + mitarbeiter.getPasswort() + " # "
                    + mitarbeiter.getIdentifikationsNummer();
            writer.write(data);
            writer.newLine();
        } catch (IOException e) {
            throw new DatenNichtGespeichert();
            // DatenNichtGespeichert
        }
    }

    public List<Mitarbeiter> ladeAlleMitarbeiter() throws DatenNichtGeladen {

        try (BufferedReader reader = new BufferedReader(new FileReader(MITARBEITER_DATEI))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" # ");
                String storedUsername = parts[0];
                String storedPassword = parts[1];
                int storedIdentificationNumber = Integer.parseInt(parts[2]);
                Mitarbeiter m = new Mitarbeiter(storedUsername, storedPassword, storedIdentificationNumber);
                mitarbeiterListe.add(m);
            }
        } catch (IOException e) {
            throw new DatenNichtGeladen();
            // DatenNichtGeladen
        }

        return mitarbeiterListe;
    }

    public boolean ersterMitarbeiter() throws ErsterMitarbeiter {
        try (BufferedReader br = new BufferedReader(new FileReader(MITARBEITER_DATEI))) {
            String firstLine = br.readLine();

            if (firstLine == null) {
                return true;
            }
        } catch (IOException e) {
            throw new ErsterMitarbeiter();
            // NichtGefunden
        }
        return false;
    }

    private String liesZeile() throws IOException {
        if (reader != null)
            return reader.readLine();
        else
            return "";
    }

    private void schreibeZeile(String daten) {
        if (writer != null)
            writer.println(daten);
    }

    public void loescheMitarbeiter(Mitarbeiter mitarbeiter) throws IOException {
        List<String> zeilen = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(MITARBEITER_DATEI));
        String zeile;

        while ((zeile = reader.readLine()) != null) {
            String[] mitarbeiterData = zeile.split(" # ");
            String benutzername = mitarbeiterData[0];
            String passwort = mitarbeiterData[1];
            int identifikationsNummer = Integer.parseInt(mitarbeiterData[2]);

            if (!(benutzername.equals(mitarbeiter.getBenutzername()) &&
                    passwort.equals(mitarbeiter.getPasswort()) &&
                    identifikationsNummer == mitarbeiter.getIdentifikationsNummer())) {
                zeilen.add(zeile);
            }
        }

        reader.close();

        BufferedWriter writer = new BufferedWriter(new FileWriter(MITARBEITER_DATEI));
        for (String line : zeilen) {
            writer.write(line);
            writer.newLine();
        }

        writer.close();
    }

    public List<String> ladeAlleBenutzernamen() throws DatenNichtGeladen {
        List<String> benutzernamenListe = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(KUNDEN_DATEI))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" # ");
                String storedUsername = parts[0];
                benutzernamenListe.add(storedUsername);
            }
        } catch (IOException e) {
            throw new DatenNichtGeladen();
        }

        return benutzernamenListe;
    }

    public List<String> ladeAlleBenutzernamenMitarbeiter() throws DatenNichtGeladen {
        List<String> benutzernamenListe = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(MITARBEITER_DATEI))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" # ");
                String storedUsername = parts[0];
                benutzernamenListe.add(storedUsername);
            }
        } catch (IOException e) {
            throw new DatenNichtGeladen();
        }

        return benutzernamenListe;
    }

    private static List<String[]> readDataFromFile(String filename) {
        List<String[]> dataList = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String artikel = "";
                String verantwortliche = "";
                String aktion = "";
                String timestamp = "";

                String[] elements = line.split(" # ");
                for (String element : elements) {
                    if (element.startsWith("Timestamp:")) {
                        timestamp = element.split(": ")[1]; // Nur das Datum extrahieren
                    } else if (element.startsWith("Artikel:")) {
                        artikel = element.split(": ")[1];
                    } else if (element.startsWith("Verantwortliche/r:")) {
                        verantwortliche = element.split(": ")[1];
                    } else if (element.startsWith("Aktion:")) {
                        aktion = element.split(": ")[1];
                    }
                }

                dataList.add(new String[]{timestamp, artikel, verantwortliche, aktion});
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return dataList;
    }
    

}