package ui.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class WindowCloser extends WindowAdapter {

    @Override
    public void windowClosing(WindowEvent e) {
        Window window = e.getWindow();
        int result = JOptionPane.showConfirmDialog
                        (window,
                        "Wollen Sie die Anwendung wirklich beenden?",
                        "Anwendung beenden?",
                        JOptionPane.YES_NO_OPTION);
        if (result == 0) {
            window.setVisible(false);
            window.dispose();
            System.exit(0);
        }
    }
}
