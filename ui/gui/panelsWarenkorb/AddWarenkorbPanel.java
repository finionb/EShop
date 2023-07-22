package ui.gui.panelsWarenkorb;

import domain.EShop;
import domain.exceptions.*;
import entities.Artikel;
import entities.Kunde;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

// Wichtig: Das AddBookPanel _ist ein_ Panel und damit auch eine Component; 
// es kann daher in das Layout eines anderen Containers 
// (in unserer Anwendung des Frames) eingefügt werden.
public class AddWarenkorbPanel extends JPanel {

    // Über dieses Interface übermittelt das AddBookPanel
    // ein neu hinzugefügtes Buch an einen Empfänger.
    // In unserem Fall ist der Empfänger die BibGuiMitKomponenten,
    // die dieses Interface implementiert und auf ein neue hinzugefügtes
    // Buch reagiert, indem sie die Bücherliste aktualisiert.
    public interface AddWarenkorbListener {
        public void WarenkorbAdded(Artikel artikel) throws ListeLeerException;
    }

    private EShop eshop = null;
    private AddWarenkorbListener addWarenkorbListener = null;

    private JButton hinzufuegenButton;
    private JTextField bezeichnungTextFeld = null;
    private JTextField anzahlTextFeld = null;

    public AddWarenkorbPanel(Kunde kunde, EShop eshop, AddWarenkorbListener addWarenkorbListener) {
        this.eshop = eshop;
        this.addWarenkorbListener = addWarenkorbListener;

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

        hinzufuegenButton = new JButton("Hinzufügen");
        add(hinzufuegenButton);

        // Abstandhalter ("Filler") zwischen letztem Element und Rand
        add(new Box.Filler(borderMinSize, borderPrefSize, borderMaxSize));

        // Rahmen definieren
        setBorder(BorderFactory.createTitledBorder("Einfügen"));
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
                artikelEinfügen(kunde);
            } catch (BestandZuNiedrigException | ArtikelExistiertNichtException | ArtikelMassengutartikelException
                    | BestandMussPositivSeinException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (ListeLeerException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        });
    }

    private void artikelEinfügen(Kunde kunde) throws BestandZuNiedrigException, ArtikelExistiertNichtException,
            ArtikelMassengutartikelException, BestandMussPositivSeinException, ListeLeerException, IOException {
        String bezeichnung = bezeichnungTextFeld.getText();
        String anzahl = anzahlTextFeld.getText();

        if (!bezeichnung.isEmpty() && !anzahl.isEmpty()) {
            try {
                int anzahlAlsInt = Integer.parseInt(anzahl);
                Artikel hinzugefuegterArtikel = eshop.warenkorbArtikelHinzufuegen(kunde, bezeichnung, anzahlAlsInt);
                bezeichnungTextFeld.setText("");
                anzahlTextFeld.setText("");
                addWarenkorbListener.WarenkorbAdded(hinzugefuegterArtikel);

                System.out.println("Ihr Warenkorb:");
                String warenkorbAnzeige = eshop.gibWarenkorb(kunde);
                System.out.println(warenkorbAnzeige);
                System.out.println("Artikel erfolgreich zum Warenkorb hinzugefügt.");

            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(null, "Fehler: " + "Bitte eine gültige Zahl eingeben." , "Fehler",
                        JOptionPane.ERROR_MESSAGE);
            } catch (BestandMussPositivSeinException e) {
                JOptionPane.showMessageDialog(null, "Fehler: " + e.getMessage(), "Fehler",
                        JOptionPane.ERROR_MESSAGE);
            } catch (BestandZuNiedrigException e) {
                JOptionPane.showMessageDialog(null, "Fehler: " + e.getMessage(), "Fehler",
                        JOptionPane.ERROR_MESSAGE);
            } catch (ArtikelExistiertNichtException e) {
                JOptionPane.showMessageDialog(null, "Fehler: " + e.getMessage(), "Fehler",
                        JOptionPane.ERROR_MESSAGE);
            } catch (ArtikelMassengutartikelException e) {
                JOptionPane.showMessageDialog(null, "Fehler: " + e.getMessage(), "Fehler",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}