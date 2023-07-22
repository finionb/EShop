package ui.gui.panelsPersonen;

import domain.EShop;
import domain.exceptions.ArtikelMassengutartikelException;
import domain.exceptions.DatenNichtGeladen;
import domain.exceptions.MitarbeiterExistiertNicht;
import entities.Mitarbeiter;
import java.awt.BorderLayout;
import java.io.IOException;

import javax.swing.*;

public class EinloggenMitarbeiterPanel extends JPanel {

    public interface EinloggenMitarbeiterListener {
        void einloggenMitarbeiter(Mitarbeiter eingeloggterMitarbeiter) throws IOException, ArtikelMassengutartikelException;
    }

    private EShop eshop;
    private EinloggenMitarbeiterListener einloggenMitarbeiterListener;
    private JTextField benutzernameTextField;
    private JTextField passwortTextField;
    private JButton anmeldenButton;

    // EinloggenMitarbeiterListener-Schnittstelle zur Benachrichtigung des
    // Einloggens eines Mitarbeiters
    public EinloggenMitarbeiterPanel(EShop eshop, EinloggenMitarbeiterListener einloggenMitarbeiterListener) {
        this.eshop = eshop;
        this.einloggenMitarbeiterListener = einloggenMitarbeiterListener;
        setupUI();
        setupEvents();
    }

    // GUI-Komponenten einrichten
    private void setupUI() {
        setLayout(new BorderLayout());

        JPanel contentPanel = new JPanel(new BorderLayout());
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.PAGE_AXIS));

        benutzernameTextField = new JTextField();
        passwortTextField = new JTextField();
        anmeldenButton = new JButton("Anmelden");

        leftPanel.add(new JLabel("Benutzername:"));
        leftPanel.add(benutzernameTextField);
        leftPanel.add(new JLabel("Passwort:"));
        leftPanel.add(passwortTextField);
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.add(anmeldenButton, BorderLayout.EAST);

        contentPanel.add(leftPanel, BorderLayout.CENTER);
        contentPanel.add(rightPanel, BorderLayout.SOUTH);
        add(contentPanel, BorderLayout.CENTER);
        setBorder(BorderFactory.createTitledBorder("Anmelden"));
    }

    // Event-Handler einrichten
    private void setupEvents() {
        anmeldenButton.addActionListener(e -> {
            try {
                MitarbeiterAnmelden();
            } catch (IOException | ArtikelMassengutartikelException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        });
    }

    // Mitarbeiteranmeldung verarbeiten
    private void MitarbeiterAnmelden() throws IOException, ArtikelMassengutartikelException {
        String benutzername = benutzernameTextField.getText();
        String passwort = passwortTextField.getText();

        if (!benutzername.isEmpty() && !passwort.isEmpty()) {
            try {
                Mitarbeiter eingeloggtermitarbeiter = eshop.anmeldenMitarbeiter(benutzername, passwort);
                benutzernameTextField.setText("");
                passwortTextField.setText("");

                if (einloggenMitarbeiterListener != null) {
                    einloggenMitarbeiterListener.einloggenMitarbeiter(eingeloggtermitarbeiter);
                }
            } catch (MitarbeiterExistiertNicht ex) {
                JOptionPane.showMessageDialog(null, "Fehler: " + ex.getMessage(), "Fehler",
                        JOptionPane.ERROR_MESSAGE);
            } catch (DatenNichtGeladen e) {
                JOptionPane.showMessageDialog(null, "Fehler: " + e.getMessage(), "Fehler",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
