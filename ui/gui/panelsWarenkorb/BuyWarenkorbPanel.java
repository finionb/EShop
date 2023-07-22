package ui.gui.panelsWarenkorb;

import domain.EShop;
import domain.exceptions.*;
import entities.Kunde;
import entities.Warenkorb;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class BuyWarenkorbPanel extends JPanel {

    public interface BuyWarenkorbListener {
        public void buyWarenkorb(Warenkorb warenkorb) throws IOException, ListeLeerException;
    }

    private EShop eshop = null;
    private BuyWarenkorbListener buyWarenkorbListener = null;

    private JButton kaufenButton;

    public BuyWarenkorbPanel(Kunde kunde, EShop eshop, BuyWarenkorbListener buyWarenkorbListener) {
        this.eshop = eshop;
        this.buyWarenkorbListener = buyWarenkorbListener;

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

        kaufenButton = new JButton("Warenkorb kaufen");
        add(kaufenButton);

        // Abstandhalter ("Filler") zwischen letztem Element und Rand
        add(new Box.Filler(borderMinSize, borderPrefSize, borderMaxSize));
    }

    private void setupEvents(Kunde kunde) {
        kaufenButton.addActionListener(e -> {
            try {
                kaufeWarenkorb(kunde);
            } catch (BestandZuNiedrigException | ArtikelExistiertNichtException | ArtikelMassengutartikelException
                    | IOException e1) {
                e1.printStackTrace();
            } catch (ListeLeerException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        });
    }

    private void kaufeWarenkorb(Kunde kunde) throws BestandZuNiedrigException,
            ArtikelExistiertNichtException, ArtikelMassengutartikelException, IOException, ListeLeerException {
        try {
            Warenkorb warenkorb = kunde.getWarenkorb();
            // for (Map.Entry<Artikel, Integer> entry : warenkorb.getArtikelMap().entrySet()) {
            //     Artikel artikel = entry.getKey();
            //     int anzahl = entry.getValue();
            //     //eshop.verringereArtikelBestand(artikel.getArtikelnummer(), anzahl);
            //     artikel.setBestand(artikel.getBestand() + anzahl);
            //     artikel.setBestand(artikel.getBestand()  anzahl);
            // }

            String ausgabe = eshop.printRechnung(eshop, kunde, warenkorb);
            System.out.println(ausgabe);

            // Neues Fenster mit der Rechnungsausgabe erstellen und anzeigen
            JFrame rechnungFrame = new JFrame("Rechnung");
            JTextArea rechnungTextArea = new JTextArea(ausgabe);
            rechnungTextArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(rechnungTextArea);
            rechnungFrame.getContentPane().add(scrollPane);
            rechnungFrame.setSize(600, 400);
            rechnungFrame.setVisible(true);

            buyWarenkorbListener.buyWarenkorb(warenkorb);
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
