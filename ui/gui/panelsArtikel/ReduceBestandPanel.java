package ui.gui.panelsArtikel;

import domain.EShop;
import domain.exceptions.ArtikelExistiertNichtException;
import domain.exceptions.ArtikelMassengutartikelException;
import domain.exceptions.BestandZuNiedrigException;
import entities.Mitarbeiter;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class ReduceBestandPanel extends JPanel {

    public interface ReduceBestandListener {
        void onBestandupdate(int artikelnummer, int neuerBestand);
    }

    private EShop eshop = null;
    private Mitarbeiter eingeloggterMitarbeiter = null;

    private JButton verringernButton;
    private JTextField artikelnummerTextField;
    private JTextField artikelbezeichnungTextField;
    private JTextField verringernTextField;
    private ReduceBestandListener reduceBestandListener;

    public ReduceBestandPanel(EShop eshop, ReduceBestandListener reduceBestandListener, Mitarbeiter eingeloggterMitarbeiter) {

        this.eshop = eshop;
        this.reduceBestandListener = reduceBestandListener;
        this.eingeloggterMitarbeiter = eingeloggterMitarbeiter;

        setupUI();
        setupEvents();
    }

    //Layout
    private void setupUI() {
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        Dimension borderMinSize = new Dimension(5, 10);
        Dimension borderPrefSize = new Dimension(5, 10);
        Dimension borderMaxSize = new Dimension(5, 10);
        Box.Filler filler = new Box.Filler(borderMinSize, borderPrefSize, borderMaxSize);
        add(filler);

        artikelnummerTextField = new JTextField();
        artikelbezeichnungTextField = new JTextField();
        verringernTextField = new JTextField();

        add(new JLabel("Artikelnummer:"));
        add(artikelnummerTextField);
        add(new JLabel("Artikelbezeichnung:"));
        add(artikelbezeichnungTextField);
        add(new JLabel("Bestand verringern um:"));
        add(verringernTextField);

        Dimension fillerMinSize = new Dimension(5, 20);
        Dimension fillerPrefSize = new Dimension(5, Short.MAX_VALUE);
        Dimension fillerMaxSize = new Dimension(5, Short.MAX_VALUE);
        filler = new Box.Filler(fillerMinSize, fillerPrefSize, fillerMaxSize);
        add(filler);

        verringernButton = new JButton("Bestand verringern");
        add(verringernButton);

        add(new Box.Filler(borderMinSize, borderPrefSize, borderMaxSize));

        setBorder(BorderFactory.createTitledBorder("Bestand verringern"));
    }

    private void setupEvents() {
        verringernButton.addActionListener(e -> {
            try {
                verringerBestand();
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        });
    }

    /*
	 * (non-Javadoc)
	 *
	 * Methode, um Bestand zu verringern
	 * 
	 */
    private void verringerBestand() throws IOException {
        String artikelnummerString = artikelnummerTextField.getText();
        String bezeichnung = artikelbezeichnungTextField.getText();
        String verringernString = verringernTextField.getText();

        if (!artikelnummerString.isEmpty() && !verringernString.isEmpty()) {
            try {
                int artikelnummer = Integer.parseInt(artikelnummerString);
                int verringern = Integer.parseInt(verringernString);

                int neuerBestand = eshop.verringereArtikelBestand(bezeichnung, artikelnummer, verringern, eingeloggterMitarbeiter.getBenutzername());
                
                artikelnummerTextField.setText("");
                artikelbezeichnungTextField.setText("");
                verringernTextField.setText("");

                // Benachrichtige den Listener über die Bestandserhöhung
                reduceBestandListener.onBestandupdate(artikelnummer, neuerBestand);

                JOptionPane.showMessageDialog(null, "Bestand wurde erfolgreich verringert.", "Meldung", JOptionPane.INFORMATION_MESSAGE);
            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(null, nfe.getMessage(), "Fehler",
                JOptionPane.ERROR_MESSAGE);
                System.err.println("Bitte gültige Zahlen eingeben.");
            } catch (ArtikelMassengutartikelException | ArtikelExistiertNichtException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Fehler",
                JOptionPane.ERROR_MESSAGE);
            } catch (BestandZuNiedrigException e) {
                JOptionPane.showMessageDialog(null, e.getMessage(), "Fehler",
                JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }
}

