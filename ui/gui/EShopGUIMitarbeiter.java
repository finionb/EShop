package ui.gui;

import domain.EShop;
import domain.exceptions.*;
import entities.Artikel;
import entities.Mitarbeiter;
import ui.gui.panelsArtikel.*;
import ui.gui.panelsPersonen.RegistrierenMitarbeiterPanel;
import ui.gui.panelsPersonen.RegistrierenMitarbeiterPanel.RegistrierenMitarbeiterListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class EShopGUIMitarbeiter extends JFrame
		implements AddArtikelPanel.AddArtikelListener, SearchArtikelPanel.SearchResultListener,
		RemoveArtikelPanel.RemoveArtikelListener, IncreaseBestandPanel.IncreaseBestandListener,
		ReduceBestandPanel.ReduceBestandListener, RegistrierenMitarbeiterListener {

	private EShop eshop;
	private RegistrierenMitarbeiterPanel registrierenPanel;
	private SearchArtikelPanel searchPanel;
	private AddArtikelPanel addPanel;
	private RemoveArtikelPanel removePanel;
	private IncreaseBestandPanel increasePanel;
	private ReduceBestandPanel reducePanel;
	private ArtikelTablePanel artikelPanel;
	private JTabbedPane tabbedPane;
	private Mitarbeiter eingeloggterMitarbeiter;

	public EShopGUIMitarbeiter(String titel, Mitarbeiter eingeloggterMitarbeiter) throws DatenNichtGeladen {
		super(titel);
		this.eingeloggterMitarbeiter = eingeloggterMitarbeiter;
		try {
			eshop = new EShop("ESHOP");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		initialize();
	}

	private void initialize() {

		// Menü initialisieren
		setupMenu();

		// Klick auf Kreuz / roten Kreis (Fenster schließen) behandeln lassen:
		// A) Mittels Default Close Operation
		// setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		// B) Mittels WindowAdapter (für Sicherheitsabfrage)
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowCloser());

		setLayout(new BorderLayout());

		// Center: Artikel Panel
		java.util.List<Artikel> artikel = eshop.gibAlleArtikel();
		artikelPanel = new ArtikelTablePanel(artikel);
		JScrollPane scrollPane = new JScrollPane(artikelPanel);
		scrollPane.setBorder(BorderFactory.createTitledBorder("Artikel"));
		add(scrollPane, BorderLayout.CENTER);

		// North: Search Panel
		searchPanel = new SearchArtikelPanel(eshop, this);
		add(searchPanel, BorderLayout.NORTH);

		// East: Mitarbeiter Panel
		// Mitarbeiter Hinzufügen Panel
		registrierenPanel = new RegistrierenMitarbeiterPanel(eshop, this);
		add(registrierenPanel, BorderLayout.EAST);

		// West: JTabbedPane
		addPanel = new AddArtikelPanel(eshop, this, eingeloggterMitarbeiter);
		removePanel = new RemoveArtikelPanel(eshop, this, eingeloggterMitarbeiter);
		increasePanel = new IncreaseBestandPanel(eshop, this, eingeloggterMitarbeiter);
		reducePanel = new ReduceBestandPanel(eshop, this, eingeloggterMitarbeiter);
		// Create JTabbedPane
		tabbedPane = new JTabbedPane();
		tabbedPane.addTab("Hinzufügen", addPanel);
		tabbedPane.addTab("Hinzufügen", addPanel);
		tabbedPane.addTab("Entfernen", removePanel);
		tabbedPane.addTab("Bestand erhöhen", increasePanel);
		tabbedPane.addTab("Bestand verringern", reducePanel);

		add(tabbedPane, BorderLayout.WEST);

		// South: Buttons für die Sortierung
		JButton sortiereNachAlphabetButton = new JButton("Nach Alphabet sortieren");
		JButton sortiereNachArtikelnummerButton = new JButton("Nach Artikelnummer sortieren");

		// ActionListener für die Sortier-Buttons hinzufügen
		sortiereNachAlphabetButton.addActionListener(e -> {
			java.util.List<Artikel> artikelA = eshop.gibAlleArtikel();
			artikelPanel.sortArtikelList(artikelA);
		});

		sortiereNachArtikelnummerButton.addActionListener(e -> {
			java.util.List<Artikel> artikelA = eshop.gibAlleArtikel();
			artikelPanel.updateArtikelList(artikelA);
		});

		// Panel für die Sortier-Buttons erstellen und Layout setzen
		JPanel sortierungsPanel = new JPanel();
		sortierungsPanel.setLayout(new FlowLayout());
		sortierungsPanel.add(sortiereNachAlphabetButton);
		sortierungsPanel.add(sortiereNachArtikelnummerButton);

		// Sortierungs-Panel zum Haupt-Container hinzufügen
		add(sortierungsPanel, BorderLayout.SOUTH);

		pack();

		this.setSize(1920, 600);
		setVisible(true);
	}

	// Main-Methode
	public static void main(String[] args) {
		// Start der Anwendung (per anonymer Klasse)
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					EShopGUIMitarbeiter gui = new EShopGUIMitarbeiter("EShop", null);
				} catch (DatenNichtGeladen e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		// // Start der Anwendung (per Lambda-Expression)
		// SwingUtilities.invokeLater(() -> { BibGuiAusVL gui = new
		// BibGuiAusVL("Bibliothek"); });
	}

	/*
	 * (non-Javadoc)
	 *
	 * Listener, der Benachrichtungen erhält, wenn im AddArtikelPanel ein Artikel
	 * eingefügt wurde.
	 * (Als Reaktion soll die Artikelliste aktualisiert werden.)
	 */
	@Override
	public void onArtikelAdded(Artikel artikel) {
		java.util.List<Artikel> artikelA = eshop.gibAlleArtikel();
		artikelPanel.updateArtikelList(artikelA);
	}

	/*
	 * (non-Javadoc)
	 *
	 * Listener, der Benachrichtungen erhält, wenn im RemoveArtikelPanel ein Artikel
	 * eingefügt wurde.
	 * (Als Reaktion soll die Artikelliste aktualisiert werden.)
	 */
	@Override
	public void onArtikelRemoved(String bezeichnung, int artikelnummer) {
		java.util.List<Artikel> artikelA = eshop.gibAlleArtikel();
		artikelPanel.updateArtikelList(artikelA);
	}

	/*
	 * (non-Javadoc)
	 *
	 * Listener, der Benachrichtungen erhält, wenn im IncreaseArtikelPanel der
	 * Bestand erhöht wurde.
	 * (Als Reaktion soll die Artikelliste aktualisiert werden.)
	 */
	@Override
	public void onBestandupdate(int artikelnummer, int neuerBestand) {
		java.util.List<Artikel> artikelA = eshop.gibAlleArtikel();
		artikelPanel.updateArtikelList(artikelA);
	}

	/*
	 * (non-Javadoc)
	 *
	 * Listener, der Benachrichtungen erhält, wenn das SearchArtikelPanel ein
	 * Suchergebnis bereitstellen möchte.
	 * (Als Reaktion soll die Artikelliste aktualisiert werden.)
	 */
	@Override
	public void onSearchResult(java.util.List<Artikel> artikel) {
		artikelPanel.updateArtikelList(artikel);
	}

	private void setupMenu() {
		// Menüleiste anlegen...
		JMenuBar mBar = new JMenuBar();

		JMenu fileMenu = new FileMenu();
		mBar.add(fileMenu);

		JMenu helpMenu = new HelpMenu();
		mBar.add(helpMenu);

		// ... und beim Fenster anmelden
		this.setJMenuBar(mBar);
	}

	/*
	 * (non-Javadoc)
	 *
	 * Mitgliedsklasse für File-Menü
	 *
	 */
	class FileMenu extends JMenu implements ActionListener {

		public FileMenu() {
			super("File");

			JMenuItem saveItem = new JMenuItem("Save");
			saveItem.addActionListener(this);
			add(saveItem);

			addSeparator();

			JMenuItem quitItem = new JMenuItem("Quit");
			quitItem.addActionListener(this);
			add(quitItem);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * wenn auf das Menü geklickt wird, sollen die jeweiligen Methoden ausgeführt
		 * werden (speichern/beenden)
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println("Klick auf MenuItem " + e.getActionCommand());

			switch (e.getActionCommand()) {
				case "Save":
					try {
						eshop.schreibeArtikel();
					} catch (IOException ex) {
						throw new RuntimeException(ex);
					}
					break;
				case "Quit":
					EShopGUIMitarbeiter.this.setVisible(false);
					EShopGUIMitarbeiter.this.dispose();
					System.exit(0);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * Mitgliedsklasse für Help-Menü
	 *
	 */
	class HelpMenu extends JMenu implements ActionListener {

		public HelpMenu() {
			super("Help");

			// Nur zu Testzwecken: Menü mit Untermenü
			JMenu m = new JMenu("About");
			JMenuItem mi = new JMenuItem("Programmers");
			mi.addActionListener(this);
			m.add(mi);
			mi = new JMenuItem("Stuff");
			mi.addActionListener(this);
			m.add(mi);
			this.add(m);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println("Klick auf Menü '" + e.getActionCommand() + "'.");
		}
	}

	@Override
	public void onMitarbeiterAdded(Mitarbeiter mitarbeiter) {
		System.out.println("Mitarbeiter " + mitarbeiter.getBenutzername());
	}
}
