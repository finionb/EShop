package domain;

import entities.Mitarbeiter;
import persistence.FilePersistenceManager;
import domain.exceptions.AccountNichtGefunden;
import domain.exceptions.DatenNichtGeladen;
import domain.exceptions.DatenNichtGespeichert;
import domain.exceptions.ErsterMitarbeiter;
import domain.exceptions.MitarbeiterExistiertNicht;
import domain.exceptions.NummerListeException;
import domain.exceptions.NummerNichtGespeichert;

import java.util.Vector;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MitarbeiterVerwaltung {

    private List<Mitarbeiter> mitarbeiterListe = new ArrayList<>();
    FilePersistenceManager file = new FilePersistenceManager();

    // Konstrukor
    public MitarbeiterVerwaltung() throws DatenNichtGeladen {
        mitarbeiterListe = new Vector<>();
        mitarbeiterListe = file.ladeAlleMitarbeiter();

    }

    public int nummerBestimmen() throws NummerListeException, NummerNichtGespeichert {
        int mitarbeiterNr = file.nextnr();
        file.speichereNextNr();
        return mitarbeiterNr;
    }

    // Speicherung der Daten
    public Mitarbeiter mitarbeiterHinzufuegen(String username, String password, int mitarbeiterNr)
            throws DatenNichtGespeichert, NummerNichtGespeichert, NummerListeException {
        Mitarbeiter mitarbeiter = new Mitarbeiter(username, password, mitarbeiterNr);
        mitarbeiterListe.add(mitarbeiter);
        file.speichereMitarbeiterData(mitarbeiter);
        return mitarbeiter;

    }

    // Getter und Setter
    public List<Mitarbeiter> getMitarbeiterListe() {
        return mitarbeiterListe;
    }

    public Mitarbeiter einloggenMitarbeiter(String username, String password) throws MitarbeiterExistiertNicht {
        for (Mitarbeiter m : mitarbeiterListe) {
            if (m.getBenutzername().equals(username) && m.getPasswort().equals(password)) {
                return m;
            }
        }
        throw new MitarbeiterExistiertNicht();
    }

    public void initialisiereSystem()
            throws DatenNichtGespeichert, ErsterMitarbeiter, NummerNichtGespeichert, NummerListeException {
        String initialerBenutzername = "admin";
        String initialesPasswort = "admin123";
        int mitarbeiterNr = file.nextnr();
        Mitarbeiter mitarbeiter = new Mitarbeiter(initialerBenutzername, initialesPasswort, mitarbeiterNr);
        file.speichereNextNr();
        mitarbeiterListe.add(mitarbeiter);
        if (file.ersterMitarbeiter() == true) {
            file.speichereMitarbeiterData(mitarbeiter);
        }

    }

    /**
     * @param mitarbeiter
     * @throws AccountNichtGefunden
     * @throws IOException
     */
    public void loescheMitarbeiterAccount(Mitarbeiter mitarbeiter)
            throws AccountNichtGefunden, IOException {
        mitarbeiterListe.removeIf(m -> m.getBenutzername().equals(mitarbeiter.getBenutzername())
                && m.getPasswort().equals(mitarbeiter.getPasswort())
                && mitarbeiter.getIdentifikationsNummer() == (mitarbeiter.getIdentifikationsNummer()));
        file.loescheMitarbeiter(mitarbeiter);

    }

    public boolean benutzernameVorhanden(String benutzername) throws DatenNichtGeladen {
        List<String> benutzernameListe = file.ladeAlleBenutzernamenMitarbeiter();
        for (String b : benutzernameListe) {
            if (b.equals(benutzername)) {
                return true; // Der Benutzername wurde gefunden
            }
        }
        return false; // Der Benutzername wurde nicht gefunden
    }
}