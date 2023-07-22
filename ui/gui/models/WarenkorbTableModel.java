package ui.gui.models;

import entities.Artikel;
import entities.Kunde;
import entities.Warenkorb;

import javax.swing.table.AbstractTableModel;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Vector;

public class WarenkorbTableModel extends AbstractTableModel {

    private Kunde kunde;
    private List<Artikel> artikel;
    private String[] spaltenNamen = { "Artikelbezeichnung", "Anzahl", "Preis" };

    public WarenkorbTableModel(Kunde kunde, List<Artikel> aktuellerWarenkorb) {
        super();
        this.kunde = kunde;
        // Ich erstelle eine Kopie der Bücherliste,
        // damit beim Aktualisieren (siehe Methode setBooks())
        // keine unerwarteten Seiteneffekte entstehen.
        artikel = new Vector<Artikel>();
        artikel.addAll(aktuellerWarenkorb);
    }

    public void setArtikelWarenkorb(List<Artikel> aktuellerWarenkorb) {
        artikel.clear();
        artikel.addAll(aktuellerWarenkorb);
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return artikel.size();
    }

    @Override
    public int getColumnCount() {
        return spaltenNamen.length;
    }

    @Override
    public String getColumnName(int col) {
        return spaltenNamen[col];
    }

    @Override
    public Object getValueAt(int row, int col) {
        Artikel gewaehlterArtikel = artikel.get(row);
        Warenkorb warenkorb = kunde.getWarenkorb();
        int anzahl = warenkorb.getArtikelAnzahl(gewaehlterArtikel);
        switch (col) {
            case 0:
                return gewaehlterArtikel.getBezeichnung();
            case 1:
                return anzahl;
            case 2:
                DecimalFormat df = new DecimalFormat("0.00"); // Formatierungsmuster
                String formattedNumber = df.format(anzahl * gewaehlterArtikel.getPreis());
                return formattedNumber + "€";
            default:
                return null;
        }
    }
}