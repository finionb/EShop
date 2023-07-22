package entities;

public class Mitarbeiter extends Personen {

    protected String mitarbeiterName;

    // Konstruktor
    public Mitarbeiter(String benutzername, String passwort, int identifikationsnummer) {
        super(benutzername, passwort, identifikationsnummer);
    }

    public boolean equals(Object andererMitarbeiter) {
        if (andererMitarbeiter instanceof Mitarbeiter)
            return ((this.getBenutzername().equals(((Mitarbeiter) andererMitarbeiter).getBenutzername())));
        else
            return false;
    }

    // Getter und Setter
    public String getMitarbeiterName() {
        return mitarbeiterName;
    }

    public int getIdentifikationsNummer() {
        return identifikationsnummer;
    }

    public void setKundeIdentifikationsNummer(int identifikationsNummer) {
        this.identifikationsnummer = identifikationsNummer;
    }
}
