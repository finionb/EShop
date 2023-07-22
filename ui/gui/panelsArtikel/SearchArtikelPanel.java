package ui.gui.panelsArtikel;

import domain.EShop;
import entities.Artikel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

// Wichtig: Das SearchBooksPanel _ist ein_ Panel und damit auch eine Component;
// es kann daher in das Layout eines anderen Containers 
// (in unserer Anwendung des Frames) eingefügt werden.
public class SearchArtikelPanel extends JPanel {

	// Über dieses Interface übermittelt das SearchBooksPanel
	// Suchergebnisse an einen Empfänger.
	// In unserem Fall ist der Empfänger die BibGuiMitKomponenten,
	// die dieses Interface implementiert und auf ein neues
	// Suchergebnis reagiert, indem sie die Bücherliste aktualisiert.
	public interface SearchResultListener {
		void onSearchResult(List<Artikel> artikel);
	}
	
	
	private EShop eshop = null;
	private JTextField searchTextField;
	private JButton searchButton = null;
	private SearchResultListener searchResultListener;
	
	public SearchArtikelPanel(EShop eshop, SearchResultListener searchResultListener) {
		this.eshop = eshop;
		this.searchResultListener = searchResultListener;

		setupUI();
		
		setupEvents();
	}
	
	private void setupUI() {
		// GridBagLayout
		// (Hinweis: Das ist schon ein komplexerer LayoutManager, der mehr kann als hier gezeigt.
		//  Hervorzuheben ist hier die Idee, explizit Constraints (also Nebenbedindungen) für 
		//  die Positionierung / Ausrichtung / Größe von GUI-Komponenten anzugeben.)
		GridBagLayout gridBagLayout = new GridBagLayout(); 
		this.setLayout(gridBagLayout);
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridy = 0;	// Zeile 0

		JLabel searchLabel = new JLabel("Suchbegriff:");
		c.gridx = 0;	// Spalte 0
		c.weightx = 0.2;	// 20% der gesamten Breite
		c.anchor = GridBagConstraints.EAST;
		gridBagLayout.setConstraints(searchLabel, c);
		this.add(searchLabel);
		
		searchTextField = new JTextField();
		searchTextField.setToolTipText("Hier Suchbegriff eingeben.");
		c.gridx = 1;	// Spalte 1
		c.weightx = 0.6;	// 60% der gesamten Breite
		gridBagLayout.setConstraints(searchTextField, c);
		this.add(searchTextField);
		
		searchButton = new JButton("Such!");
		c.gridx = 2;	// Spalte 2
		c.weightx = 0.2;	// 20% der gesamten Breite
		gridBagLayout.setConstraints(searchButton, c);
		this.add(searchButton);
		
		// Rahmen definieren
		setBorder(BorderFactory.createTitledBorder("Suche"));
	}
	
	private void setupEvents() {
		searchButton.addActionListener(new SuchListener());
	}
	
	// Lokale Klasse für Reaktion auf Such-Klick
	class SuchListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent ae) {
			if (ae.getSource().equals(searchButton)) {
				String suchbegriff = searchTextField.getText();
				List<Artikel> suchErgebnis;
				if (suchbegriff.isEmpty()) {
					suchErgebnis = eshop.gibAlleArtikel();
				} else {
					suchErgebnis = eshop.sucheNachArtikel(suchbegriff);
				}
				searchResultListener.onSearchResult(suchErgebnis);
			}
		}
	}
}
