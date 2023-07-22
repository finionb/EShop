package ui.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.io.IOException;

import javax.swing.*;

import domain.EShop;
import domain.exceptions.DatenNichtGeladen;
import entities.Mitarbeiter;
import ui.gui.panelsPersonen.EinloggenMitarbeiterPanel;
import ui.gui.panelsPersonen.EinloggenMitarbeiterPanel.EinloggenMitarbeiterListener;

public class LoginMitarbeiterGUI extends JFrame implements EinloggenMitarbeiterListener {
    private EinloggenMitarbeiterPanel einloggenPanel;
    private EShop eshop1;

    public LoginMitarbeiterGUI(EShop eshop1) {
        this.eshop1 = eshop1;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Mitarbeiter-Login");
        setSize(500, 250);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());

        // Erstelle das obere Panel mit dem Titel "Mitarbeiter-Login"
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel titleLabel = new JLabel("Mitarbeiter-Login");
        topPanel.add(titleLabel);
        mainPanel.add(topPanel, BorderLayout.NORTH);

        einloggenPanel = new EinloggenMitarbeiterPanel(eshop1, this);
        JPanel centerPanel = new JPanel(new BorderLayout());
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
                dispose(); // Schließen Sie das aktuelle LoginMitarbeitereGUI-Fenster
            } catch (IOException | DatenNichtGeladen ex) {
                System.out.println(ex.getMessage());
            }
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                EShop eshop1 = new EShop("mitarbeiter_data.txt"); // Erstelle eine Instanz von EShop mit dem Dateinamen
                GUIeins guiEins = new GUIeins();
                guiEins.logInMitarbeiter(eshop1); // Öffne das Login-Mitarbeiter-GUI mit dem EShop-Objekt
            } catch (IOException | DatenNichtGeladen e) {
                System.out.println(e.getMessage());
            }
        });
    }

    // Implementierung der EinloggenMitarbeiterListener-Schnittstelle
    @Override
    public void einloggenMitarbeiter(Mitarbeiter eingeloggterMitarbeiter) {
        System.out.println("Eingeloggter Mitarbeiter: " + eingeloggterMitarbeiter.getBenutzername());
        // Hier öffnen Sie die Fenster mit den GUI-Komponenten
        SwingUtilities.invokeLater(() -> {
            try {
                EShopGUIMitarbeiter guiMitarbeiter = new EShopGUIMitarbeiter("EShop", eingeloggterMitarbeiter);
            } catch (DatenNichtGeladen e) {
                JOptionPane.showMessageDialog(this, e.getMessage(), "Fehler",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
        dispose();
    }

}
