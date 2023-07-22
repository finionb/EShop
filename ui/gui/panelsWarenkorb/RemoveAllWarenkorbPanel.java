package ui.gui.panelsWarenkorb;

import domain.EShop;
import domain.exceptions.*;
import entities.Artikel;
import entities.Kunde;
import entities.Warenkorb;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class RemoveAllWarenkorbPanel extends JPanel {

    public interface RemoveAllWarenkorbListener {
        public void removeAllWarenkorb(Artikel artikel);
    }

    private EShop eshop = null;
    private RemoveAllWarenkorbListener removeAllWarenkorbListener = null;

    private JButton kaufenButton;

    public RemoveAllWarenkorbPanel(Kunde kunde, EShop eshop, RemoveAllWarenkorbListener removeAllWarenkorbListener) {
        this.eshop = eshop;
        this.removeAllWarenkorbListener = removeAllWarenkorbListener;

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

        kaufenButton = new JButton("Warenkorb leeren");
        add(kaufenButton);

        // Abstandhalter ("Filler") zwischen letztem Element und Rand
        add(new Box.Filler(borderMinSize, borderPrefSize, borderMaxSize));
    }

    private void setupEvents(Kunde kunde) {
        kaufenButton.addActionListener(e -> {
            try {
                leereWarenkorb(kunde);
            } catch (BestandZuNiedrigException | ArtikelExistiertNichtException | ArtikelMassengutartikelException
                    | IOException e1) {
                e1.printStackTrace();
            }
        });
    }

    private void leereWarenkorb(Kunde kunde) throws BestandZuNiedrigException,
            ArtikelExistiertNichtException, ArtikelMassengutartikelException, IOException {
        try {
            Warenkorb warenkorb = kunde.getWarenkorb();
            eshop.warenkorbLeeren(kunde);
            System.out.println("Warenkorb geleert.");

            warenkorb.clearWarenkorb();
            removeAllWarenkorbListener.removeAllWarenkorb(null);
        } catch (ArtikelMassengutartikelException e) {
            JOptionPane.showMessageDialog(null, "Fehler: " + e.getMessage(), "Fehler",
                        JOptionPane.ERROR_MESSAGE);
        }
    }
}
