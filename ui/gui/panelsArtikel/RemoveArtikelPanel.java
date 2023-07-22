package ui.gui.panelsArtikel;

import domain.EShop;
import domain.exceptions.ArtikelExistiertNichtException;
import entities.Mitarbeiter;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class RemoveArtikelPanel extends JPanel {

    public interface RemoveArtikelListener {
        void onArtikelRemoved(String bezeichnung, int artikelnummer);
    }

    private EShop eshop = null;
    private RemoveArtikelListener removeArtikelListener = null;
    private Mitarbeiter eingeloggterMitarbeiter = null;
    private JButton entfernenButton;
    private JTextField bezeichnungTextFeld;

    private JTextField artikelnummerTextFeld;

    public RemoveArtikelPanel(EShop eshop, RemoveArtikelListener removeArtikelListener,
            Mitarbeiter eingeloggterMitarbeiter) {
        this.eshop = eshop;
        this.removeArtikelListener = removeArtikelListener;
        this.eingeloggterMitarbeiter = eingeloggterMitarbeiter;

        setupUI();
        setupEvents();
    }

    // Layout
    private void setupUI() {
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        Dimension borderMinSize = new Dimension(5, 10);
        Dimension borderPrefSize = new Dimension(5, 10);
        Dimension borderMaxSize = new Dimension(5, 10);
        Box.Filler filler = new Box.Filler(borderMinSize, borderPrefSize, borderMaxSize);
        add(filler);

        artikelnummerTextFeld = new JTextField();
        bezeichnungTextFeld = new JTextField();

        add(new JLabel("Artikelnummer:"));
        add(artikelnummerTextFeld);
        add(new JLabel("Artikelbezeichnung:"));
        add(bezeichnungTextFeld);

        Dimension fillerMinSize = new Dimension(5, 20);
        Dimension fillerPrefSize = new Dimension(5, Short.MAX_VALUE);
        Dimension fillerMaxSize = new Dimension(5, Short.MAX_VALUE);
        filler = new Box.Filler(fillerMinSize, fillerPrefSize, fillerMaxSize);
        add(filler);

        entfernenButton = new JButton("Entfernen");
        add(entfernenButton);

        add(new Box.Filler(borderMinSize, borderPrefSize, borderMaxSize));

        setBorder(BorderFactory.createTitledBorder("Entfernen"));
    }

    private void setupEvents() {
        entfernenButton.addActionListener(e -> {
            try {
                artikelEntfernen();
            } catch (IOException e1) {
                JOptionPane.showMessageDialog(null, "Artikel konnte nicht entfernt werden.", "Fehler",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    /*
     * (non-Javadoc)
     *
     * Methode, um einen Artikel zu löschen
     * 
     */
    private void artikelEntfernen() throws IOException {
        String artikelnummerString = artikelnummerTextFeld.getText();
        String bezeichnung = bezeichnungTextFeld.getText();

        if (!artikelnummerString.isEmpty() && !bezeichnung.isEmpty()) {
            try {
                int artikelnummer = Integer.parseInt(artikelnummerString);
                eshop.loescheArtikel(bezeichnung, artikelnummer, eingeloggterMitarbeiter.getBenutzername());
                artikelnummerTextFeld.setText("");
                bezeichnungTextFeld.setText("");

                removeArtikelListener.onArtikelRemoved(bezeichnung, artikelnummer);
                JOptionPane.showMessageDialog(null, "Artikel wurde erfolgreich entfernt.", "Meldung",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(null, "Bitte eine gültige Zahl eingeben.", "Fehler",
                        JOptionPane.ERROR_MESSAGE);
            } catch (ArtikelExistiertNichtException aene) {
                System.err.println(aene.getMessage());
            }
        }
    }
}


