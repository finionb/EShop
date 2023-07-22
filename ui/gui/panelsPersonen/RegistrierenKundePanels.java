package ui.gui.panelsPersonen;

import domain.EShop;
import domain.exceptions.DatenNichtGeladen;
import domain.exceptions.DatenNichtGespeichert;
import domain.exceptions.KundeExistiertBereitsException;
import entities.Kunde;
import domain.exceptions.NummerListeException;
import domain.exceptions.NummerNichtGespeichert;
import java.awt.BorderLayout;
import java.awt.HeadlessException;

import javax.swing.*;

public class RegistrierenKundePanels extends JPanel {

    // RegistrierenKundeListener-Schnittstelle zur Benachrichtigung über
    // hinzugefügte Kunden
    public interface RegistrierenKundeListener {
        void onKundeAdded(Kunde kunde);
    }

    private EShop eshop;
    private RegistrierenKundeListener rKundelistener;
    private JTextField benutzernameTextField;
    private JTextField passwortTextField;
    private JTextField nameTextField;
    private JTextField strasseTextField;
    private JTextField plzTextField;
    private JTextField wohnortTextField;
    private JButton hinzufuegenButton;

    public RegistrierenKundePanels(EShop eshop, RegistrierenKundeListener rKundeListener) {
        this.eshop = eshop;
        this.rKundelistener = rKundeListener;

        setupUI();
        setupEvents();
    }

    // UI-Komponenten einrichten
    private void setupUI() {
        setLayout(new BorderLayout());

        JPanel contentPanel = new JPanel(new BorderLayout());
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.PAGE_AXIS));

        benutzernameTextField = new JTextField();
        passwortTextField = new JTextField();
        nameTextField = new JTextField();
        strasseTextField = new JTextField();
        plzTextField = new JTextField();
        wohnortTextField = new JTextField();
        hinzufuegenButton = new JButton("Hinzufügen");

        leftPanel.add(new JLabel("Benutzername:"));
        leftPanel.add(benutzernameTextField);
        leftPanel.add(new JLabel("Passwort:"));
        leftPanel.add(passwortTextField);
        leftPanel.add(new JLabel("Vor- und Nachname:"));
        leftPanel.add(nameTextField);
        leftPanel.add(new JLabel("Strasse:"));
        leftPanel.add(strasseTextField);
        leftPanel.add(new JLabel("PLZ:"));
        leftPanel.add(plzTextField);
        leftPanel.add(new JLabel("Wohnort:"));
        leftPanel.add(wohnortTextField);
        leftPanel.add(hinzufuegenButton);
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.add(hinzufuegenButton, BorderLayout.EAST);

        contentPanel.add(leftPanel, BorderLayout.CENTER);
        contentPanel.add(rightPanel, BorderLayout.SOUTH);
        add(contentPanel, BorderLayout.CENTER);

        setBorder(BorderFactory.createTitledBorder("Registrieren"));
    }

    // Event-Handler einrichten
    private void setupEvents() {
        hinzufuegenButton.addActionListener(e -> {
            try {
                KundeRegistrieren();
            } catch (HeadlessException | DatenNichtGeladen e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        });
    }

    // Kundenregistrierung verarbeiten
    private void KundeRegistrieren() throws HeadlessException, DatenNichtGeladen {
        String benutzername = benutzernameTextField.getText();
        String passwort = passwortTextField.getText();
        String name = nameTextField.getText();
        String strasse = strasseTextField.getText();
        String plz = plzTextField.getText();
        String wohnort = wohnortTextField.getText();

        // Überprüfen, ob alle Felder ausgefüllt sind
        if (!benutzername.isEmpty() && !passwort.isEmpty()
                && !name.isEmpty() && !strasse.isEmpty()
                && !plz.isEmpty() && !wohnort.isEmpty()) {
            // Kundenregistrierung im EShop durchführen
            try {
                if (!eshop.Benutzernamevorhanden(benutzername)) {
                    try {
                        Kunde kunde = eshop.registrieren(benutzername, passwort, name, strasse, plz, wohnort);
                        benutzernameTextField.setText("");
                        passwortTextField.setText("");
                        nameTextField.setText("");
                        strasseTextField.setText("");
                        plzTextField.setText("");
                        wohnortTextField.setText("");
                        // Benachrichtigung des rKundelistener-Objekts über den hinzugefügten Kunden
                        if (rKundelistener != null) {
                            rKundelistener.onKundeAdded(kunde);
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
                    JOptionPane.showMessageDialog(null, "Ein Account mit diesem Benutzernamen existiert bereits.",
                            "Fehler",
                            JOptionPane.ERROR_MESSAGE);
                }
            } catch (KundeExistiertBereitsException l) {
                JOptionPane.showMessageDialog(null, "Fehler: " + l.getMessage(), "Fehler",
                        JOptionPane.ERROR_MESSAGE);
            }
        } else {
            System.err.println("Bitte füllen Sie alle Felder aus.");
        }
    }
}
