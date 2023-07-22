package entities;

public class Kunde extends Personen {

    private String name;
    private float umsatz = 0.0f;
    private String strasse = "";
    private String plz = "";
    private String wohnort = "";

    private Warenkorb warenkorb;

    public Kunde(String benutzername, String passwort, String name, String strasse, String plz, String wohnort,
            int identifikationsnummer) {
        super(benutzername, passwort, identifikationsnummer);
        this.name = name;
        this.strasse = strasse;
        this.wohnort = wohnort;
        this.plz = plz;
        this.warenkorb = new Warenkorb();
    }

    public boolean equals(Object andererKunde) {
        if (andererKunde instanceof Kunde)
            return ((this.getBenutzername().equals(((Kunde) andererKunde).getBenutzername())));
        else
            return false;
    }

    // Getter und Setter für den Warenkorb
    public Warenkorb getWarenkorb() {
        return warenkorb;
    }

    // Getter und Setter
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getUmsatz() {
        return umsatz;
    }

    public void setUmsatz(float umsatz) {
        this.umsatz = umsatz;
    }

    public String getPlz() {
        return plz;
    }

    public void setPlz(String plz) {
        this.plz = plz;
    }

    public String getStrasse() {
        return strasse;
    }

    public void setStrasse(String strasse) {
        this.strasse = strasse;
    }

    public String getWohnort() {
        return wohnort;
    }

    public void setWohnort(String wohnort) {
        this.wohnort = wohnort;
    }

    public String toString() {
        String kundenDetails = "Kunde : " + name + "\n";
        kundenDetails += "Adresse: " + strasse + ", " + plz + " " + wohnort;
        return kundenDetails;
    }

    public int getIdentifikationsNummer() {
        return identifikationsnummer;
    }

    public void setKundeIdentifikationsNummer(int identifikationsNummer) {
        this.identifikationsnummer = identifikationsNummer;
    }
}