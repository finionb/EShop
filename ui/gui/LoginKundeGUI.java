package ui.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.io.IOException;

import javax.swing.*;

import domain.EShop;
import domain.exceptions.DatenNichtGeladen;
import entities.Kunde;
import ui.gui.panelsPersonen.EinloggenKundePanel;
import ui.gui.panelsPersonen.RegistrierenKundePanels;
import ui.gui.panelsPersonen.EinloggenKundePanel.EinloggenKundeListener;
import ui.gui.panelsPersonen.RegistrierenKundePanels.RegistrierenKundeListener;

public class LoginKundeGUI extends JFrame implements EinloggenKundeListener, RegistrierenKundeListener {
    private RegistrierenKundePanels registrierenPanel;
    private EinloggenKundePanel einloggenPanel;
    private EShop eshop;

    public LoginKundeGUI(EShop eshop) {
        this.eshop = eshop;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Kunden-Login");
        setSize(515, 515);
        setLocationRelativeTo(null);
        JPanel mainPanel = new JPanel(new BorderLayout()); // erstellt ein neues JPanel mit einem BorderLayout als
                                                           // Layout-Manager

        // Erstelle das obere Panel mit dem Titel "Kunden-Login"
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel titleLabel = new JLabel("Kunden-Login");
        topPanel.add(titleLabel);
        mainPanel.add(topPanel, BorderLayout.NORTH);

        // Erstelle das Registrierungs- und Einloggen-Panel
        registrierenPanel = new RegistrierenKundePanels(eshop, this);
        einloggenPanel = new EinloggenKundePanel(eshop, this);
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(registrierenPanel, BorderLayout.NORTH);
        centerPanel.add(einloggenPanel, BorderLayout.CENTER);
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Erstelle das untere Panel mit dem "Zurück"-Button
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton zurueckButton = new JButton("Zurück");
        bottomPanel.add(zurueckButton);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);

        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowCloser());

        // ActionListener für den "Zurück"-Button
        zurueckButton.addActionListener(e -> {
            try {
                new GUIeins();
                dispose(); // Schließen Sie das aktuelle LoginKundeGUI-Fenster
            } catch (IOException | DatenNichtGeladen ex) {
                ex.printStackTrace();
            }
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                EShop eshop = new EShop("kunden_data.txt"); // Erstelle eine Instanz von EShop mit dem Dateinamen
                GUIeins guiEins = new GUIeins(); // eine Instanz der Klasse GUIeins mit dem Namen guiEins
                guiEins.logInKunde(eshop);
                // Die Methode logInKunde von guiEins wird aufgerufen und das EShop-Objekt wird
                // übergeben. Dadurch wird das GUI für die Kundeneinwahl geöffnet und mit dem
                // EShop-Objekt verknüpft, um auf die erforderlichen Daten zugreifen zu können.
            } catch (IOException | DatenNichtGeladen e) {
                System.out.println(e.getMessage());
            }
        });
    }

    // Implementierung der EinloggenKundeListener-Schnittstelle
    @Override
    public void onKundeAdded(Kunde kunde) {
        System.out.println("Kunde " + kunde.getName());
        // Schließe das LogInKundeGUI-Fenster
        dispose();

        // Öffne das GUIeins-Fenster erneut
        try {
            new GUIeins();
        } catch (IOException | DatenNichtGeladen e) {
            System.out.println(e.getMessage());
        }
    }

    // Implementierung der RegistrierenKundeListener-Schnittstelle
    @Override
    public void einloggenKunde(Kunde kunde) {
        System.out.println("Eingeloggter Kunde: " + kunde.getBenutzername());
        // Hier öffnen Sie die Fenster mit den GUI-Komponenten
        SwingUtilities.invokeLater(() -> {
            try {
                EShopGUIKunde guiKunde = new EShopGUIKunde(kunde, "EShop");
            } catch (DatenNichtGeladen e) {
                JOptionPane.showMessageDialog(this, e.getMessage(), "Fehler",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
        dispose();
    }
}
