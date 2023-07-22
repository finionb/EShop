package domain;

import domain.exceptions.DatenNichtGeladen;
import domain.exceptions.KundeExistiertNicht;
import domain.exceptions.NummerListeException;
import domain.exceptions.NummerNichtGespeichert;
import entities.Kunde;
import domain.exceptions.DatenNichtGespeichert;
import persistence.FilePersistenceManager;
import java.util.ArrayList;
import java.util.List;

public class KundenVerwaltung {

    FilePersistenceManager file = new FilePersistenceManager();
    public List<Kunde> kundenListe = new ArrayList<>();
    public List<String> BenutzernameListe = new ArrayList<>();
    private Kunde kunde;

    // Konstruktor
    public KundenVerwaltung() {

    }

    public int nummerBestimmen() throws NummerListeException, NummerNichtGespeichert {
        int kundenNr = file.nextnr();
        file.speichereNextNr();
        return kundenNr;
    }

    // Speicherung in der Datei
    public Kunde kundeHinzufuegen(String benutzername,
            String passwort, String name, String strasse, String plz, String wohnort, int kundenNr)
            throws DatenNichtGespeichert, NummerListeException, NummerNichtGespeichert {
        Kunde kunde = new Kunde(benutzername, passwort, name, strasse, plz, wohnort, kundenNr);
        file.speichereKundenData(kunde);
        return kunde;
    }

    // Getter und Setter
    // public List<Kunde> getKundenListe() {
    // return kundenListe;
    // }

    public void ladeAlleKunden() throws DatenNichtGeladen {
        kundenListe = file.ladeAlleKunden();
    }

    public boolean benutzernameVorhanden(String benutzername) throws DatenNichtGeladen {
        List<String> benutzernameListe = file.ladeAlleBenutzernamen();
        for (String b : benutzernameListe) {
            if (b.equals(benutzername)) {
                return true; // Der Benutzername wurde gefunden
            }
        }
        return false; // Der Benutzername wurde nicht gefunden
    }

    public Kunde einloggenKunde(String username, String password) throws KundeExistiertNicht, DatenNichtGeladen {

        for (Kunde k : kundenListe) {
            if (k.getBenutzername().equals(username) && k.getPasswort().equals(password)) {
                return k;
            }
        }
        throw new KundeExistiertNicht();
    }

    public int getIdentifikationsNummerKunde(String benutzername) throws KundeExistiertNicht {
        for (Kunde k : kundenListe) {
            if (k.getBenutzername().equals(benutzername)) {
                return k.getIdentifikationsNummer();
            }
        }
        throw new KundeExistiertNicht();
    }
}
