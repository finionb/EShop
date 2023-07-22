package ui.gui.panelsArtikel;

import domain.EShop;
import domain.exceptions.ArtikelExistiertNichtException;
import domain.exceptions.ArtikelMassengutartikelException;
import entities.Mitarbeiter;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class IncreaseBestandPanel extends JPanel {

    public interface IncreaseBestandListener {
        void onBestandupdate(int artikelnummer, int neuerBestand);
    }

    private EShop eshop = null;
    private Mitarbeiter eingeloggterMitarbeiter = null;
    private JButton erhoehenButton;
    private JTextField artikelnummerTextField;

    private JTextField artikelbezeichnungTextField;
    private JTextField erhoehungTextField;
    private IncreaseBestandListener increaseBestandListener;

    public IncreaseBestandPanel(EShop eshop, IncreaseBestandListener increaseBestandListener, Mitarbeiter eingeloggterMitarbeiter) {
        this.eshop = eshop;
        this.increaseBestandListener = increaseBestandListener;
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

        artikelnummerTextField = new JTextField();
        artikelbezeichnungTextField = new JTextField();
        erhoehungTextField = new JTextField();

        add(new JLabel("Artikelnummer:"));
        add(artikelnummerTextField);
        add(new JLabel("Artikelbezeichnung:"));
        add(artikelbezeichnungTextField);
        add(new JLabel("Bestand erhöhen um:"));
        add(erhoehungTextField);

        Dimension fillerMinSize = new Dimension(5, 20);
        Dimension fillerPrefSize = new Dimension(5, Short.MAX_VALUE);
        Dimension fillerMaxSize = new Dimension(5, Short.MAX_VALUE);
        filler = new Box.Filler(fillerMinSize, fillerPrefSize, fillerMaxSize);
        add(filler);

        erhoehenButton = new JButton("Bestand erhöhen");
        add(erhoehenButton);

        add(new Box.Filler(borderMinSize, borderPrefSize, borderMaxSize));

        setBorder(BorderFactory.createTitledBorder("Bestand ändern"));
    }

    private void setupEvents() {
        erhoehenButton.addActionListener(e -> {
            try {
                erhoeheBestand();
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        });
    }

    /*
     * (non-Javadoc)
     *
     * Methode, um den Bestand zu erhöhen
     * 
     */
    private void erhoeheBestand() throws IOException {
        String artikelnummerString = artikelnummerTextField.getText();
        String bezeichnung = artikelbezeichnungTextField.getText();
        String erhoehungString = erhoehungTextField.getText();

        if (!artikelnummerString.isEmpty() && !erhoehungString.isEmpty()) {
            try {
                int artikelnummer = Integer.parseInt(artikelnummerString);
                int erhoehung = Integer.parseInt(erhoehungString);

                int neuerBestand = eshop.erhoeheArtikelBestand(bezeichnung, artikelnummer, erhoehung, eingeloggterMitarbeiter.getBenutzername());
                // Bestand erfolgreich erhöht
                artikelnummerTextField.setText("");
                artikelbezeichnungTextField.setText("");
                erhoehungTextField.setText("");

                // Benachrichtige den Listener über die Bestandserhöhung
                increaseBestandListener.onBestandupdate(artikelnummer, neuerBestand);

                JOptionPane.showMessageDialog(null, "Bestand wurde erfolgreich erhöht.", "Meldung",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(null, nfe.getMessage(), "Fehler",
                        JOptionPane.ERROR_MESSAGE);
                System.err.println("Bitte gültige Zahlen eingeben.");
            } catch (ArtikelMassengutartikelException | ArtikelExistiertNichtException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Fehler",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
