package ui.gui;

import javax.swing.*;

import domain.EShop;
import domain.exceptions.DatenNichtGeladen;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class GUIeins extends JFrame {

    private LoginKundeGUI logKunde;
    private LoginMitarbeiterGUI logMitarbeiter;

    public GUIeins() throws IOException, DatenNichtGeladen {
        // Erstelle zwei EShop-Objekte für Kunden und Mitarbeiter
        EShop eshop = new EShop("kunden_data.txt");
        EShop eshop1 = new EShop("mitarbeiter_data.txt");

        // Fenstereinstellungen
        setTitle("Menü");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 200);
        setLocationRelativeTo(null); // zentriert das Fenster auf dem Bildschirm.
        setLayout(new GridLayout(4, 1));

        // Label zur Anzeige der Auswahlmöglichkeiten
        JLabel label = new JLabel("Fortfahren als:");
        add(label);

        // Button für den Kunden-Login
        JButton kundeButton = new JButton("Kunde");
        kundeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logInKunde(eshop);
            }
        });
        add(kundeButton);

        // Button für den Mitarbeiter-Login
        JButton mitarbeiterButton = new JButton("Mitarbeiter");
        mitarbeiterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logInMitarbeiter(eshop1);
            }
        });
        add(mitarbeiterButton);

        // Button zum Beenden des Programms
        JButton beendenButton = new JButton("Beenden");
        beendenButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Programm wird beendet.");
                System.exit(0);
            }
        });
        add(beendenButton);

        // Fensterverhalten beim Schließen
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowCloser());

        // Setze das Fenster auf sichtbar
        setVisible(true);
    }

    // Methode zum Starten des Kunden-Logins
    public void logInKunde(EShop eshop) {
        logKunde = new LoginKundeGUI(eshop); // Erstelle das LoginKundeGUI-Objekt mit dem EShop-Objekt
        logKunde.setVisible(true); // Setze das LoginKundeGUI-Fenster auf sichtbar
    }

    // Methode zum Starten des Mitarbeiter-Logins
    public void logInMitarbeiter(EShop eshop1) {
        logMitarbeiter = new LoginMitarbeiterGUI(eshop1);
        logMitarbeiter.setVisible(true);
    }

    // Einstiegspunkt des Programms
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    new GUIeins();
                } catch (IOException | DatenNichtGeladen e) {
                    System.out.println(e.getMessage());
                }

            }
        });
    }
}