package ui.gui.panelsWarenkorb;

import domain.EShop;
import domain.exceptions.*;
import entities.Artikel;
import entities.Kunde;

import javax.swing.*;
import java.awt.*;

public class RemoveWarenkorbPanel extends JPanel {

    public interface RemoveWarenkorbListener {
        public void WarenkorbRemove(Artikel artikel);
    }

    private EShop eshop = null;
    private RemoveWarenkorbListener removeWarenkorbListener = null;

    private JButton hinzufuegenButton;
    private JTextField bezeichnungTextFeld = null;

    public RemoveWarenkorbPanel(Kunde kunde, EShop eshop, RemoveWarenkorbListener removeWarenkorbListener) {
        this.eshop = eshop;
        this.removeWarenkorbListener = removeWarenkorbListener;

        setupUI();

        setupEvents(kunde);
    }

    private void setupUI() {

        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        // Abstandhalter ("Filler") zwischen Rand und erstem Element
        Dimension borderMinSize = new Dimension(5, 10);
        Dimension borderPrefSize = new Dimension(5, 10);
        Dimension borderMaxSize = new Dimension(5, 10);
        Box.Filler filler = new Box.Filler(borderMinSize, borderPrefSize, borderMaxSize);
        add(filler);

        bezeichnungTextFeld = new JTextField();
        add(new JLabel("Bezeichnung:"));
        add(bezeichnungTextFeld);

        // Abstandhalter ("Filler") zwischen letztem Eingabefeld und Add-Button
        Dimension fillerMinSize = new Dimension(5, 20);
        Dimension fillerPrefSize = new Dimension(5, Short.MAX_VALUE);
        Dimension fillerMaxSize = new Dimension(5, Short.MAX_VALUE);
        filler = new Box.Filler(fillerMinSize, fillerPrefSize, fillerMaxSize);
        add(filler);

        hinzufuegenButton = new JButton("Entfernen");
        add(hinzufuegenButton);

        // Abstandhalter ("Filler") zwischen letztem Element und Rand
        add(new Box.Filler(borderMinSize, borderPrefSize, borderMaxSize));

        // Rahmen definieren
        setBorder(BorderFactory.createTitledBorder("Entfernen"));
    }

    private void setupEvents(Kunde kunde) {
        // hinzufuegenButton.addActionListener(
        // new ActionListener() {
        // @Override
        // public void actionPerformed(ActionEvent ae) {
        // System.out.println("Event: " + ae.getActionCommand());
        // buchEinfügen();
        // }
        // });
        hinzufuegenButton.addActionListener(e -> {
            try {
                artikelEntfernen(kunde);
            } catch (ArtikelExistiertNichtException | ArtikelMassengutartikelException
                    | ArtikelNichtImWarenkorbException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        });
    }

    private void artikelEntfernen(Kunde kunde) throws ArtikelExistiertNichtException,
            ArtikelMassengutartikelException, ArtikelNichtImWarenkorbException {
        String bezeichnung = bezeichnungTextFeld.getText();

        if (!bezeichnung.isEmpty()) {
            try {
                Artikel hinzugefuegterArtikel = eshop.warenkorbArtikelEntfernen(kunde, bezeichnung);
                bezeichnungTextFeld.setText("");
                removeWarenkorbListener.WarenkorbRemove(hinzugefuegterArtikel);
            } catch (NumberFormatException nfe) {
                System.err.println("Bitte eine gültige Zahl eingeben.");
            } catch (ArtikelExistiertNichtException e) {
                JOptionPane.showMessageDialog(null, "Fehler: " + e.getMessage(), "Fehler",
                        JOptionPane.ERROR_MESSAGE);
            } catch (ArtikelNichtImWarenkorbException e) {
                JOptionPane.showMessageDialog(null, "Fehler: " + e.getMessage(), "Fehler",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public interface RemoveArtikelListener {
    }
}
