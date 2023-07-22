import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class ArtikelBestandsverlauf extends JFrame {

    private Map<String, int[]> artikelBestaende;
    private String currentArtikel;
    private ArtikelChartPanel chartPanel;
    private JTable artikelTabelle;

    public ArtikelBestandsverlauf() {
        super("Artikel Bestandsverlauf");
        artikelBestaende = new HashMap<>();
        artikelBestaende.put("Artikel 1", new int[]{50, 45, 42, 38, 37, 35, 34, 30, 29, 25, 20, 18, 15, 12, 10, 8, 7, 5, 3, 2, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0});
        artikelBestaende.put("Artikel 2", new int[]{100, 95, 90, 87, 85, 80, 78, 75, 70, 65, 62, 60, 57, 55, 52, 50, 48, 45, 42, 40, 38, 36, 34, 30, 28, 25, 20, 15, 10, 5});
        currentArtikel = "Artikel 1";

        JPanel leftPanel = new JPanel(new BorderLayout());
        chartPanel = new ArtikelChartPanel(currentArtikel);
        leftPanel.add(chartPanel, BorderLayout.CENTER);

        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.addColumn("Artikelname");
        for (String artikel : artikelBestaende.keySet()) {
            tableModel.addRow(new Object[]{artikel});
        }
        artikelTabelle = new JTable(tableModel);
        artikelTabelle.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        artikelTabelle.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                int selectedRow = artikelTabelle.getSelectedRow();
                if (selectedRow >= 0) {
                    String selectedArtikel = (String) artikelTabelle.getValueAt(selectedRow, 0);
                    if (!selectedArtikel.equals(currentArtikel)) {
                        currentArtikel = selectedArtikel;
                        chartPanel.setArtikel(currentArtikel);
                        chartPanel.repaint();
                    }
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(artikelTabelle);

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.add(new JLabel("Artikelliste"), BorderLayout.NORTH);
        rightPanel.add(scrollPane, BorderLayout.CENTER);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        add(splitPane, BorderLayout.CENTER);
    }

    private class ArtikelChartPanel extends JPanel {
        private String artikelName;

        public ArtikelChartPanel(String artikelName) {
            this.artikelName = artikelName;
        }

        public void setArtikel(String artikelName) {
            this.artikelName = artikelName;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            int[] bestaende = artikelBestaende.get(artikelName);
            int width = getWidth();
            int height = getHeight();
            int barWidth = width / bestaende.length;
            int maxValue = 0;
            for (int bestand : bestaende) {
                maxValue = Math.max(maxValue, bestand);
            }
            int scale = height / (maxValue + 1);

            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(Color.BLUE);

            // Zeichne die Y-Achse
            g2d.drawLine(40, 30, 40, height - 40);
            for (int i = 0; i <= maxValue; i += 5) {
                int y = height - 30 - i * scale;
                g2d.drawString(String.valueOf(i), 20, y);
                g2d.drawLine(35, y, 45, y);
            }

            // Y-Achse Titel (um 90 Grad gedreht)
            Graphics2D g2dRotated = (Graphics2D) g2d.create();
            g2dRotated.rotate(-Math.PI / 2);
            g2dRotated.drawString("Bestand", -height / 2, 15);
            g2dRotated.dispose();

            // Zeichne die X-Achse
            g2d.drawLine(40, height - 40, width - 30, height - 40);
            for (int i = 0; i <= bestaende.length; i++) {
                int x = 40 + i * barWidth;
                g2d.drawString(String.valueOf(i + 1), x - 5, height - 25); // Zahl 30 ist jetzt sichtbar
                g2d.drawLine(x, height - 35, x, height - 45);
            }

            // X-Achse Titel
            g2d.drawString("Tag", width / 2, height - 5);

            for (int i = 0; i < bestaende.length; i++) {
                int x = 40 + i * barWidth;
                int barHeight = bestaende[i] * scale;
                int y = height - 40 - barHeight;
                g2d.fillRect(x, y, barWidth, barHeight);
            }
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(400, 300);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ArtikelBestandsverlauf frame = new ArtikelBestandsverlauf();
            frame.pack();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });
    }
}
