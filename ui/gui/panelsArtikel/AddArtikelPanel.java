package ui.gui.panelsArtikel;

import domain.EShop;
import domain.exceptions.*;
import entities.Artikel;
import entities.Mitarbeiter;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class AddArtikelPanel extends JPanel {

    public interface AddArtikelListener {
        void onArtikelAdded(Artikel artikel);
    }

    private EShop eshop = null;
    private AddArtikelListener addArtikelListener = null;

    private JButton hinzufuegenButton;
    private JTextField bezeichnungTextFeld;
    private JTextField artikelnummerTextFeld;
    private JTextField preisTextFeld;
    private JTextField bestandTextFeld;
    private JTextField packungsGroesseTextFeld;
    private Mitarbeiter eingeloggterMitarbeiter = null;

    public AddArtikelPanel(EShop eshop, AddArtikelListener addArtikelListener, Mitarbeiter eingeloggterMitarbeiter) {
        this.eshop = eshop;
        this.addArtikelListener = addArtikelListener;
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
        preisTextFeld = new JTextField();
        bestandTextFeld = new JTextField();
        packungsGroesseTextFeld = new JTextField();

        add(new JLabel("Artikelnummer:"));
        add(artikelnummerTextFeld);
        add(new JLabel("Artikelbezeichnung:"));
        add(bezeichnungTextFeld);
        add(new JLabel("Artikelpreis:"));
        add(preisTextFeld);
        add(new JLabel("Artikelbestand:"));
        add(bestandTextFeld);
        add(new JLabel("Packungsgröße (bei Einzelartikel 1 eingeben):"));
        add(packungsGroesseTextFeld);

        Dimension fillerMinSize = new Dimension(5, 20);
        Dimension fillerPrefSize = new Dimension(5, Short.MAX_VALUE);
        Dimension fillerMaxSize = new Dimension(5, Short.MAX_VALUE);
        filler = new Box.Filler(fillerMinSize, fillerPrefSize, fillerMaxSize);
        add(filler);

        hinzufuegenButton = new JButton("Hinzufügen");
        add(hinzufuegenButton);

        add(new Box.Filler(borderMinSize, borderPrefSize, borderMaxSize));

        setBorder(BorderFactory.createTitledBorder("Einfügen"));
    }

    private void setupEvents() {
        hinzufuegenButton.addActionListener(e -> {
            try {
                artikelHinzufuegen();
            } catch (IOException e1) {
                JOptionPane.showMessageDialog(null, "Artikel konnte nicht hinzugefügt werden.", "Fehler",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    /*
     * (non-Javadoc)
     *
     * Methode, um einen Artikel hinzuzufügen
     * 
     */
    private void artikelHinzufuegen() throws IOException {
        String artikelnummerString = artikelnummerTextFeld.getText();
        String bezeichnung = bezeichnungTextFeld.getText();
        String preisString = preisTextFeld.getText();
        String bestandString = bestandTextFeld.getText();
        String packungsGroesseString = packungsGroesseTextFeld.getText();

        if (!artikelnummerString.isEmpty() && !bezeichnung.isEmpty()
                && !preisString.isEmpty() && !bestandString.isEmpty()
                && !packungsGroesseString.isEmpty()) {
            try {
                int artikelnummer = Integer.parseInt(artikelnummerString);
                double preis = Double.parseDouble(preisString);
                int bestand = Integer.parseInt(bestandString);
                int packungsGroesse = Integer.parseInt(packungsGroesseString);
                boolean verfuegbar = (bestand > 0);

                if (bestand >= packungsGroesse) {
                    Artikel artikel = eshop.fuegeArtikelEin(bezeichnung,
                            artikelnummer,
                            verfuegbar, preis, bestand, packungsGroesse, eingeloggterMitarbeiter.getBenutzername());
                    artikelnummerTextFeld.setText("");
                    bezeichnungTextFeld.setText("");
                    preisTextFeld.setText("");
                    bestandTextFeld.setText("");
                    packungsGroesseTextFeld.setText("");

                    addArtikelListener.onArtikelAdded(artikel);

                    JOptionPane.showMessageDialog(null, "Artikel wurde erfolgreich hinzugefügt.", "Meldung",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Bestand muss größer oder gleich der Packungsgröße sein.",
                            "Fehler",
                            JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(null, nfe.getMessage(), "Fehler",
                        JOptionPane.ERROR_MESSAGE);
            } catch (ArtikelExistiertBereitsException aebe) {
                JOptionPane.showMessageDialog(null, aebe.getMessage(), "Fehler",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}


