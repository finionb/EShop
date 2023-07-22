package ui.gui.panelsPersonen;

import domain.EShop;
import domain.exceptions.ArtikelMassengutartikelException;
import domain.exceptions.DatenNichtGeladen;
import domain.exceptions.KundeExistiertNicht;
import entities.Kunde;
import java.awt.BorderLayout;
import java.io.IOException;

import javax.swing.*;

public class EinloggenKundePanel extends JPanel {

    // EinloggenKundeListener-Schnittstelle zur Benachrichtigung des Einloggens
    // eines Kunden
    public interface EinloggenKundeListener {
        void einloggenKunde(Kunde eingeloggterKunde) throws IOException, ArtikelMassengutartikelException;
    }

    private EShop eshop;
    private EinloggenKundeListener einloggenKundeListener;
    private JTextField benutzernameTextField;
    private JTextField passwortTextField;
    private JButton anmeldenButton;

    public EinloggenKundePanel(EShop eshop, EinloggenKundeListener einloggenKundeListener) {
        this.eshop = eshop;
        this.einloggenKundeListener = einloggenKundeListener;
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
        leftPanel.add(new JLabel("Benutzername:"));
        leftPanel.add(benutzernameTextField);
        leftPanel.add(new JLabel("Passwort:"));
        leftPanel.add(passwortTextField);
        anmeldenButton = new JButton("Anmelden");
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
                KundeAnmelden();
            } catch (IOException | ArtikelMassengutartikelException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        });
    }

    // Kundenanmeldung verarbeiten
    private void KundeAnmelden() throws IOException, ArtikelMassengutartikelException {
        String benutzername = benutzernameTextField.getText();
        String passwort = passwortTextField.getText();

        if (!benutzername.isEmpty() && !passwort.isEmpty()) {
            try {
                Kunde eingeloggterKunde = eshop.anmeldenKunden(benutzername, passwort);
                benutzernameTextField.setText("");
                passwortTextField.setText("");

                if (einloggenKundeListener != null) {
                    einloggenKundeListener.einloggenKunde(eingeloggterKunde);
                }
            } catch (KundeExistiertNicht ex) {
                JOptionPane.showMessageDialog(null, "Fehler: " + ex.getMessage(), "Fehler",
                        JOptionPane.ERROR_MESSAGE);
            } catch (DatenNichtGeladen e) {
                JOptionPane.showMessageDialog(null, "Fehler: " + e.getMessage(), "Fehler",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
