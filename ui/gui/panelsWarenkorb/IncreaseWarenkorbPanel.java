package ui.gui.panelsWarenkorb;

import domain.EShop;
import domain.exceptions.*;
import entities.Kunde;

import javax.swing.*;
import java.awt.*;

public class IncreaseWarenkorbPanel extends JPanel {

    public interface IncreaseWarenkorbListener {
        public void increaseWarenkorb(String artikelbezeichnung, int neueAnzahl);
    }

    private EShop eshop = null;
    private IncreaseWarenkorbListener increaseWarenkorbListener = null;

    private JButton hinzufuegenButton;
    private JTextField bezeichnungTextFeld = null;
    private JTextField anzahlTextFeld = null;

    public IncreaseWarenkorbPanel(Kunde kunde, EShop eshop, IncreaseWarenkorbListener increaseWarenkorbListener) {
        this.eshop = eshop;
        this.increaseWarenkorbListener = increaseWarenkorbListener;

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
        anzahlTextFeld = new JTextField();
        add(new JLabel("Bezeichnung:"));
        add(bezeichnungTextFeld);
        add(new JLabel("Anzahl:"));
        add(anzahlTextFeld);

        // Abstandhalter ("Filler") zwischen letztem Eingabefeld und Add-Button
        Dimension fillerMinSize = new Dimension(5, 20);
        Dimension fillerPrefSize = new Dimension(5, Short.MAX_VALUE);
        Dimension fillerMaxSize = new Dimension(5, Short.MAX_VALUE);
        filler = new Box.Filler(fillerMinSize, fillerPrefSize, fillerMaxSize);
        add(filler);

        hinzufuegenButton = new JButton("Erhöhen");
        add(hinzufuegenButton);

        // Abstandhalter ("Filler") zwischen letztem Element und Rand
        add(new Box.Filler(borderMinSize, borderPrefSize, borderMaxSize));

        // Rahmen definieren
        setBorder(BorderFactory.createTitledBorder("Anzahl Erhöhen"));
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
                increaseArtikel(kunde);
            } catch (BestandZuNiedrigException | ArtikelExistiertNichtException | ArtikelMassengutartikelException
                    | BestandMussPositivSeinException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        });
    }

    private void increaseArtikel(Kunde kunde) throws BestandZuNiedrigException, ArtikelExistiertNichtException,
            ArtikelMassengutartikelException, BestandMussPositivSeinException {
        String bezeichnung = bezeichnungTextFeld.getText();
        String anzahl = anzahlTextFeld.getText();

        if (!bezeichnung.isEmpty() && !anzahl.isEmpty()) {
            try {
                int anzahlAlsInt = Integer.parseInt(anzahl);
                int anzahlWarenkorb = eshop.warenkorbErhoeheArtikelanzahl2(kunde, bezeichnung,
                        anzahlAlsInt);
                increaseWarenkorbListener.increaseWarenkorb(bezeichnung, anzahlWarenkorb);
            } catch (BestandMussPositivSeinException e) {
                JOptionPane.showMessageDialog(null, "Fehler: " + e.getMessage(), "Fehler",
                        JOptionPane.ERROR_MESSAGE);
            } catch (ArtikelMassengutartikelException e) {
                JOptionPane.showMessageDialog(null, "Fehler: " + e.getMessage(), "Fehler",
                        JOptionPane.ERROR_MESSAGE);
            } catch (BestandZuNiedrigException e) {
                JOptionPane.showMessageDialog(null, "Fehler: " + e.getMessage(), "Fehler",
                        JOptionPane.ERROR_MESSAGE);
            } catch (ArtikelExistiertNichtException e) {
                JOptionPane.showMessageDialog(null, "Fehler: " + e.getMessage(), "Fehler",
                        JOptionPane.ERROR_MESSAGE);
            } catch (ArtikelNichtImWarenkorbException e) {
                JOptionPane.showMessageDialog(null, "Fehler: " + e.getMessage(), "Fehler",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
