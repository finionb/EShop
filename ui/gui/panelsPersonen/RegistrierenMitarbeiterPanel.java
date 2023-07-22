package ui.gui.panelsPersonen;

import domain.EShop;
import domain.exceptions.DatenNichtGeladen;
import domain.exceptions.DatenNichtGespeichert;
import domain.exceptions.MitarbeiterExistiertBereitsException;
import entities.Mitarbeiter;
import domain.exceptions.NummerListeException;
import domain.exceptions.NummerNichtGespeichert;
import java.awt.Dimension;
import java.awt.HeadlessException;

import javax.swing.*;

public class RegistrierenMitarbeiterPanel extends JPanel {

    // RegistrierenMitarbeiterListener-Schnittstelle zur Benachrichtigung über
    // hinzugefügte Mitarbeiter
    public interface RegistrierenMitarbeiterListener {
        void onMitarbeiterAdded(Mitarbeiter mitarbeiter);
    }

    private EShop eshop;
    private RegistrierenMitarbeiterListener rMitarbeiterlistener;
    private JTextField benutzernameTextField;
    private JTextField passwortTextField;
    private JButton hinzufuegenButton;

    public RegistrierenMitarbeiterPanel(EShop eshop, RegistrierenMitarbeiterListener rMitarbeiterListener) {
        this.eshop = eshop;
        this.rMitarbeiterlistener = rMitarbeiterListener;
        setupUI();
        setupEvents();
    }

    // UI-Komponenten einrichten
    private void setupUI() {
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        // Platzhalter-Box.Filler am Anfang hinzufügen
        Dimension borderMinSize = new Dimension(5, 10);
        Dimension borderPrefSize = new Dimension(5, 10);
        Dimension borderMaxSize = new Dimension(5, 10);
        Box.Filler filler = new Box.Filler(borderMinSize, borderPrefSize, borderMaxSize);
        add(filler);// Platzhalter-Box.Filler mit fester Größe für den Abstand zwischen den
                    // Komponenten

        benutzernameTextField = new JTextField();
        passwortTextField = new JTextField();

        // Benutzername- und Passwort-Eingabefelder hinzufügen
        add(new JLabel("Benutzername:"));
        add(benutzernameTextField);
        add(new JLabel("Passwort:"));
        add(passwortTextField);

        // Platzhalter-Box.Filler hinzufügen
        Dimension fillerMinSize = new Dimension(5, 20);
        Dimension fillerPrefSize = new Dimension(5, Short.MAX_VALUE);
        Dimension fillerMaxSize = new Dimension(5, Short.MAX_VALUE);
        filler = new Box.Filler(fillerMinSize, fillerPrefSize, fillerMaxSize);
        add(filler); // Platzhalter-Box.Filler mit fester Größe für den Abstand zum Ende des Panels

        // "Hinzufügen"-Button hinzufügen
        hinzufuegenButton = new JButton("Hinzufügen");
        add(hinzufuegenButton);

        // Platzhalter-Box.Filler am Ende hinzufügen
        add(new Box.Filler(borderMinSize, borderPrefSize, borderMaxSize));

        setBorder(BorderFactory.createTitledBorder("Hinzufügen"));
    }

    // Event-Handler einrichten
    private void setupEvents() {
        hinzufuegenButton.addActionListener(e -> {
            try {
                MitarbeiterRegistrieren();
            } catch (HeadlessException | DatenNichtGeladen e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        });
    }

    // Mitarbeiterregistrierung verarbeiten
    private void MitarbeiterRegistrieren() throws HeadlessException, DatenNichtGeladen {
        String benutzername = benutzernameTextField.getText();
        String passwort = passwortTextField.getText();

        // Überprüfen, ob alle Felder ausgefüllt sind
        if (!benutzername.isEmpty() && !passwort.isEmpty()) {
            try {
                if (!eshop.BenutzernamevorhandenMiatrbeiter(benutzername)) {
                    try {
                        Mitarbeiter mitarbeiter = eshop.mitarbeiterHinzufuegen(benutzername, passwort);
                        benutzernameTextField.setText("");
                        passwortTextField.setText("");
                        if (rMitarbeiterlistener != null) {
                            rMitarbeiterlistener.onMitarbeiterAdded(mitarbeiter);
                        }
                    } catch (NumberFormatException nfe) {
                        JOptionPane.showMessageDialog(this, "Bitte geben Sie eine gültige Zahl ein.", "Fehler",
                                JOptionPane.ERROR_MESSAGE);
                    } catch (DatenNichtGespeichert e) {
                        JOptionPane.showMessageDialog(this, "Fehler beim Speichern der Daten.", "Fehler",
                                JOptionPane.ERROR_MESSAGE);
                    } catch (NummerListeException | NummerNichtGespeichert e) {
                        JOptionPane.showMessageDialog(this, "Fehler mit den Nummern.", "Fehler",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    // Benutzername ist bereits vorhanden
                    JOptionPane.showMessageDialog(null, "Ein Account mit diesem Benutzernamen existiert bereits.",
                            "Fehler",
                            JOptionPane.ERROR_MESSAGE);
                }
            } catch (MitarbeiterExistiertBereitsException l) {
                JOptionPane.showMessageDialog(null, "Fehler: " + l.getMessage(), "Fehler",
                        JOptionPane.ERROR_MESSAGE);
            }
        } else {
            System.err.println("Bitte füllen Sie alle Felder aus.");
        }
    }
}
