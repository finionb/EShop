package ui.gui;

import domain.EShop;
import domain.exceptions.DatenNichtGeladen;
import domain.exceptions.ListeLeerException;
import entities.Artikel;
import entities.Kunde;
import entities.Warenkorb;
import ui.gui.panelsWarenkorb.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

public class EShopGUIKunde extends JFrame
		implements AddWarenkorbPanel.AddWarenkorbListener, SearchArtikelPanel.SearchResultListener,
		RemoveWarenkorbPanel.RemoveWarenkorbListener, IncreaseWarenkorbPanel.IncreaseWarenkorbListener,
		DecreaseWarenkorbPanel.DecreaseWarenkorbListener, BuyWarenkorbPanel.BuyWarenkorbListener,
		RemoveAllWarenkorbPanel.RemoveAllWarenkorbListener {

	private EShop eshop;

	private static Kunde kunde;

	private WarenkorbTablePanel warenkorbPanel;

	private ArtikelTablePanel artikelPanel;

	private SearchArtikelPanel searchPanel;
	private AddWarenkorbPanel addPanel;
	private RemoveWarenkorbPanel removePanel;
	private IncreaseWarenkorbPanel increasePanel;
	private DecreaseWarenkorbPanel decreasePanel;
	private BuyWarenkorbPanel buyPanel;
	private RemoveAllWarenkorbPanel removeAllPanel;

	public void updateWarenkorb(List<Artikel> ArtikelList) {
		artikelPanel.updateArtikelList(eshop.gibAlleArtikel());
	}

	@Override
	public void buyWarenkorb(Warenkorb warenkorb) throws IOException, ListeLeerException {
		java.util.List<Artikel> aktualisierterWarenkorb = eshop.gibAllesWarenkorb(kunde);
		warenkorbPanel.updateWarenkorb(aktualisierterWarenkorb);
		artikelPanel.updateArtikelList(eshop.gibAlleArtikel());
		List<String> artikelListe = eshop.gibArtikellisteAus(eshop.gibAlleArtikel());
		for (String artikel : artikelListe) {
			System.out.println(artikel);
		}
	}
	
	@Override
	public void WarenkorbAdded(Artikel artikel) throws ListeLeerException {
		java.util.List<Artikel> warenkorb = eshop.gibAllesWarenkorb(kunde);
		warenkorbPanel.updateWarenkorb(warenkorb);
		updateWarenkorb(warenkorb);
		List<String> artikelListe = eshop.gibArtikellisteAus(eshop.gibAlleArtikel());
		for (String artikel1 : artikelListe) {
			System.out.println(artikel1);
		}
	}

	@Override
	public void WarenkorbRemove(Artikel artikel) {
		java.util.List<Artikel> warenkorb = eshop.gibAllesWarenkorb(kunde);
		warenkorbPanel.updateWarenkorb(warenkorb);
		updateWarenkorb(warenkorb);
	}

	@Override
	public void increaseWarenkorb(String bezeichnung, int anzahl) {
		// Ich lade hier einfach alle Bücher neu und lasse sie anzeigen
		java.util.List<Artikel> warenkorb = eshop.gibAllesWarenkorb(kunde);
		warenkorbPanel.updateWarenkorb(warenkorb);
		updateWarenkorb(warenkorb);
	}

	@Override
	public void removeAllWarenkorb(Artikel artikel) {
		java.util.List<Artikel> warenkorb = eshop.gibAllesWarenkorb(kunde);
		warenkorbPanel.updateWarenkorb(warenkorb);
		updateWarenkorb(warenkorb);
	}

	@Override
	public void decreaseWarenkorb(Artikel artikel) {
		java.util.List<Artikel> warenkorb = eshop.gibAllesWarenkorb(kunde);
		warenkorbPanel.updateWarenkorb(warenkorb);
		updateWarenkorb(warenkorb);
	}

	public EShopGUIKunde(Kunde kunde, String titel) throws DatenNichtGeladen {
		super(titel);
		this.kunde = kunde;
		try {
			eshop = new EShop("ESHOP");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		initialize();
	}

	private void initialize() {
		setupMenu();

		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowCloser());

		this.setLayout(new BorderLayout());

		searchPanel = new SearchArtikelPanel(eshop, this);
		addPanel = new AddWarenkorbPanel(kunde, eshop, this);
		removePanel = new RemoveWarenkorbPanel(kunde, eshop, this);
		increasePanel = new IncreaseWarenkorbPanel(kunde, eshop, this);
		decreasePanel = new DecreaseWarenkorbPanel(kunde, eshop, this);
		buyPanel = new BuyWarenkorbPanel(kunde, eshop, this); 
		removeAllPanel = new RemoveAllWarenkorbPanel(kunde, eshop, null);

		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.addTab("Einfügen", addPanel);
		tabbedPane.addTab("Entfernen", removePanel);
		tabbedPane.addTab("Anzahl erhöhen", increasePanel);
		tabbedPane.addTab("Anzahl verringern", decreasePanel);

		java.util.List<Artikel> artikel = eshop.gibAlleArtikel();
		artikelPanel = new ArtikelTablePanel(artikel);
		JScrollPane scrollPane = new JScrollPane(artikelPanel);
		scrollPane.setBorder(BorderFactory.createTitledBorder("Artikel"));

		// EAST
		java.util.List<Artikel> warenkorb = eshop.gibAllesWarenkorb(kunde);
		warenkorbPanel = new WarenkorbTablePanel(kunde, warenkorb);
		JScrollPane warenkorbScrollPane = new JScrollPane(warenkorbPanel);
		warenkorbScrollPane.setBorder(BorderFactory.createTitledBorder("Warenkorb"));

		// Hinzufügen des BuyWarenkorbPanel unten rechts im Warenkorb-Panel
		JPanel buyWarenkorbPanel = new JPanel(new BorderLayout());
		buyWarenkorbPanel.add(buyPanel, BorderLayout.LINE_END);

		JPanel bottomPanel = new JPanel(new BorderLayout());
		bottomPanel.add(warenkorbScrollPane, BorderLayout.CENTER);
		bottomPanel.add(buyWarenkorbPanel, BorderLayout.SOUTH);

		getContentPane().add(searchPanel, BorderLayout.NORTH);
		add(tabbedPane, BorderLayout.WEST);
		add(scrollPane, BorderLayout.CENTER);
		add(bottomPanel, BorderLayout.EAST); // Aktualisierte Zeile

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
		this.setVisible(true);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					// TODO; warum muss Kunde static sein?
					EShopGUIKunde gui = new EShopGUIKunde(kunde, "EShop");
				} catch (DatenNichtGeladen e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	public void onSearchResult(List<Artikel> artikel) {
		artikelPanel.updateArtikelList(artikel);
	}

	private void setupMenu() {
		JMenuBar mBar = new JMenuBar();

		JMenu fileMenu = new FileMenu();
		mBar.add(fileMenu);

		JMenu helpMenu = new HelpMenu();
		mBar.add(helpMenu);

		this.setJMenuBar(mBar);
	}

	class FileMenu extends JMenu implements ActionListener {

		public FileMenu() {
			super("Optionen");

			JMenuItem saveItem = new JMenuItem("Abmelden");
			saveItem.addActionListener(this);
			add(saveItem);

			addSeparator();

			JMenuItem quitItem = new JMenuItem("Quit");
			quitItem.addActionListener(this);
			add(quitItem);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println("Klick auf MenuItem " + e.getActionCommand());

			switch (e.getActionCommand()) {
				case "Abmelden":
					kunde = null;
					EShopGUIKunde.this.setVisible(false);
					EShopGUIKunde.this.dispose();
					new LoginKundeGUI(eshop);
					setVisible(true);
					break;
				case "Quit":
					// Nur "this" ginge nicht, weil "this" auf das FileMenu-Objekt zeigt.
					// "BibGuiAusVL.this" zeigt auf das dieses (innere) FileMenu-Objekt
					// umgebende Objekt der Klasse BibGuiAusVL.
					EShopGUIKunde.this.setVisible(false);
					EShopGUIKunde.this.dispose();
					System.exit(0);

			}
		}
	}

	class HelpMenu extends JMenu implements ActionListener {

		public HelpMenu() {
			super("Help");

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
}
