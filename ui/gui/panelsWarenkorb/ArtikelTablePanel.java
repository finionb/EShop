package ui.gui.panelsWarenkorb;

import entities.Artikel;
import ui.gui.models.ArtikelTableModel;

import javax.swing.*;
import java.util.Collections;

public class ArtikelTablePanel extends JTable {

	public ArtikelTablePanel(java.util.List<Artikel> artikel) {
		super();

		// TableModel erzeugen ...
		ArtikelTableModel tableModel = new ArtikelTableModel(artikel);
		// ... bei JTable "anmelden" und ...
		setModel(tableModel);
		// ... Daten an Model übergeben (für Sortierung o.ä.)
		updateArtikelList(artikel);
	}

	public void updateArtikelList(java.util.List<Artikel> artikel) {

		// Sortierung (mit Lambda-Expression)
		// Collections.sort(buecher, (b1, b2) ->
		// b1.getTitel().compareTo(b2.getTitel())); // Sortierung nach Titel
		Collections.sort(artikel, (b1, b2) -> b1.getArtikelnummer() - b2.getArtikelnummer()); // Sortierung nach Nummer

		// TableModel von JTable holen und ...
		ArtikelTableModel tableModel = (ArtikelTableModel) getModel();
		// ... Inhalt aktualisieren
		tableModel.setArtikel(artikel);
	}

	public void sortArtikelList(java.util.List<Artikel> artikel) {

		// Sortierung nach Bezeichnung
		Collections.sort(artikel, (a1, a2) -> a1.getBezeichnung().compareToIgnoreCase(a2.getBezeichnung()));
		
		// TableModel von JTable holen und ...
		ArtikelTableModel tableModel = (ArtikelTableModel) getModel();
		// ... Inhalt aktualisieren
		tableModel.setArtikel(artikel);
	}
	
}
