package entities;

public class Personen {
    public String benutzername;
    public String passwort;
    public int identifikationsnummer;

    public Personen(String benutzername, String passwort, int identifikationsnummer) {
        this.benutzername = benutzername;
        this.passwort = passwort;
        this.identifikationsnummer = identifikationsnummer;
    }

    public String getBenutzername() {
        return benutzername;
    }

    public String getPasswort() {
        return passwort;
    }

    public int getIdentifikationsNummer() {
        return identifikationsnummer;
    }

    public void setIdentifikationsNummer(int identifikationsnummer) {
        this.identifikationsnummer = identifikationsnummer;
    }
}
