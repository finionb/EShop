package ui.gui.panelsWarenkorb;

import entities.Artikel;
import entities.Kunde;
import ui.gui.models.WarenkorbTableModel;
import java.util.Collections;
import javax.swing.*;

public class WarenkorbTablePanel extends JTable {

    public WarenkorbTablePanel(Kunde kunde, java.util.List<Artikel> artikel) {
        super();

        // TableModel erzeugen ...
        WarenkorbTableModel tableModel = new WarenkorbTableModel(kunde, artikel);
        // ... bei JTable "anmelden" und ...
        setModel(tableModel);
        // ... Daten an Model übergeben (für Sortierung o.ä.)
        updateWarenkorb(artikel);
    }

    public void updateWarenkorb(java.util.List<Artikel> artikel) {
        Collections.sort(artikel, (b1, b2) -> b1.getBezeichnung().compareTo(b2.getBezeichnung()));
        WarenkorbTableModel tableModel = (WarenkorbTableModel) getModel();
        tableModel.setArtikelWarenkorb(artikel);
    }

}
