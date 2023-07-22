package ui.gui.models;

import entities.Artikel;
import entities.Massengutartikel;

import javax.swing.table.AbstractTableModel;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Vector;

public class ArtikelTableModel extends AbstractTableModel {

    private List<Artikel> artikel;
    // String bezeichnung, int artikelnummer, double preis, int bestand, boolean
    // verfuegbar
    private String[] spaltenNamen = { "Artikelbezeichnung", "Artikelnummer", "Preis", "Bestand", "Verfügbar",
            "Packungsgröße" };

    public ArtikelTableModel(List<Artikel> aktuelleArtikel) {
        super();
        // Ich erstelle eine Kopie der Bücherliste,
        // damit beim Aktualisieren (siehe Methode setBooks())
        // keine unerwarteten Seiteneffekte entstehen.
        artikel = new Vector<Artikel>();
        artikel.addAll(aktuelleArtikel);
    }

    public void setArtikel(List<Artikel> aktuelleArtikel) {
        artikel.clear();
        artikel.addAll(aktuelleArtikel);
        fireTableDataChanged();
    }

    /*
     * Ab hier überschriebene Methoden mit Informationen,
     * die eine JTable von jedem TableModel erwartet:
     * - Anzahl der Zeilen
     * - Anzahl der Spalten
     * - Benennung der Spalten
     * - Wert einer Zelle in Zeile / Spalte
     */

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

    // String bezeichnung, int artikelnummer, double preis, int bestand, boolean
    // verfuegbar
    @Override
    public Object getValueAt(int row, int col) {
        Artikel gewaehlterArtikel = artikel.get(row);
        switch (col) {
            case 0:
                return gewaehlterArtikel.getBezeichnung();
            case 1:
                return gewaehlterArtikel.getArtikelnummer();
            case 2:
                DecimalFormat df = new DecimalFormat("0.00"); // Formatierungsmuster
                String formattedNumber = df.format(gewaehlterArtikel.getPreis());
                return formattedNumber + "€";
            case 3:
                return gewaehlterArtikel.getBestand();
            case 4:
                boolean verfuegbar = gewaehlterArtikel.isVerfuegbar();
                if (gewaehlterArtikel.getBestand() > 0) {
                    verfuegbar = true;
                } else {
                    verfuegbar = false;
                }
                gewaehlterArtikel.setVerfuegbar(verfuegbar);
                return verfuegbar;
            case 5:
                if (gewaehlterArtikel instanceof Massengutartikel) {
                    Massengutartikel massengutartikel = (Massengutartikel) gewaehlterArtikel;
                    return massengutartikel.getPackungsGroesse();
                } else {
                    return "1";
                }
            default:
                return null;
        }
    }
}
