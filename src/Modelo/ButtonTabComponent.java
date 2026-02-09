package Modelo;

import javax.swing.*;
import java.awt.*;
import java.awt.Color;


public class ButtonTabComponent extends JPanel {
    private final JTabbedPane pane;

    public ButtonTabComponent(final JTabbedPane pane) {
        super(new FlowLayout(FlowLayout.LEFT, 0, 0));
        this.pane = pane;
        setOpaque(false);

        JLabel label = new JLabel() {
            public String getText() {
                int i = pane.indexOfTabComponent(ButtonTabComponent.this);
                if (i != -1) return pane.getTitleAt(i);
                return null;
            }
        };
        label.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 8));
        add(label);

        JButton closeButton = new JButton("✖") {
            @Override
            protected void paintComponent(Graphics g) {
                if (getModel().isRollover()) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(new Color(220, 50, 50));
                    g2.fillOval(0, 0, getWidth() - 1, getHeight() - 1);
                    g2.dispose();
                    setForeground(Color.WHITE);
                } else {
                    setForeground(Color.BLACK);
                }
                super.paintComponent(g);
            }
        };

        closeButton.setPreferredSize(new Dimension(20, 20));
        closeButton.setMargin(new Insets(0, 0, 0, 0));
        closeButton.setContentAreaFilled(false);
        closeButton.setBorderPainted(false);
        closeButton.setFocusable(false);

        closeButton.addActionListener(e -> {
            int i = pane.indexOfTabComponent(this);
            if (i != -1) {
                if (JOptionPane.showConfirmDialog(pane, "¿Cerrar pestaña?") == JOptionPane.YES_OPTION) {
                    pane.remove(i);
                }
            }
        });

        add(closeButton);
    }
}

