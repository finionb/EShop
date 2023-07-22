package entities;

import java.io.IOException;
import java.sql.Date;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import domain.EShop;
import domain.exceptions.ArtikelExistiertNichtException;
import domain.exceptions.ArtikelMassengutartikelException;
import domain.exceptions.BestandZuNiedrigException;

public class Rechnung {

    private EShop eshop;
    private Kunde eingeloggterNutzer;
    private Warenkorb warenkorb;

    public Rechnung(EShop eshop, Kunde eingeloggterNutzer, Warenkorb warenkorb) {
        this.eshop = eshop;
        this.eingeloggterNutzer = eingeloggterNutzer;
        this.warenkorb = warenkorb;
    }

    public String erstelleRechnung(Kunde kunde) throws BestandZuNiedrigException, ArtikelExistiertNichtException,
            ArtikelMassengutartikelException, IOException {
        HashMap<Artikel, Integer> artikelMap = warenkorb.getArtikelMap();
        String ausgabe = "";
        DecimalFormat df = new DecimalFormat("0.00");
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String timestamp = dateFormat.format(new Date(System.currentTimeMillis()));
        double gesamtPreis = 0.0;

        if (artikelMap.isEmpty()) {
            ausgabe = "Warenkorb ist leer.";
        } else {
            ausgabe = "Kunde: " + ((Kunde) eingeloggterNutzer).getName() + " / Datum: " + timestamp + "\n";

            for (Map.Entry<Artikel, Integer> entry : artikelMap.entrySet()) {
                Artikel artikel = entry.getKey();
                int anzahl = entry.getValue();

                ausgabe += "Bezeichnung: " + artikel.getBezeichnung() + " / Preis: " + artikel.getPreis()
                        + " / Anzahl: " + anzahl + "\n";

                gesamtPreis += artikel.getPreis() * anzahl;
                eshop.writeTimestampToFile(artikel.getBezeichnung(), anzahl, eingeloggterNutzer.getName(), "Kunde-Artikel-kaufen");
            }
            eshop.schreibeArtikel();
            eshop.warenkorbLeeren(kunde);

            ausgabe += "Gesamtpreis: " + (df.format(gesamtPreis)) + " Euro";
        }
        return ausgabe;
    }

}
