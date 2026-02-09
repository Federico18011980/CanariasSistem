package Vista;

import Modelo.PreviewProducto;
import Vista.Modelo.PreviewProductoTableModel;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class PreviewImportDialog extends JDialog {

    private boolean confirmado = false;

    public PreviewImportDialog(JFrame parent, List<PreviewProducto> lista) {
        super(parent, "Vista previa de importación", true);

        JTable tabla = new JTable(new PreviewProductoTableModel(lista));
        JScrollPane scroll = new JScrollPane(tabla);

        JButton btnConfirmar = new JButton("Importar");
        JButton btnCancelar = new JButton("Cancelar");

        btnConfirmar.addActionListener(e -> {
            confirmado = true;
            dispose();
        });

        btnCancelar.addActionListener(e -> dispose());

        JPanel panelBotones = new JPanel();
        panelBotones.add(btnConfirmar);
        panelBotones.add(btnCancelar);

        setLayout(new BorderLayout());
        add(scroll, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

        setSize(800, 400);
        setLocationRelativeTo(parent);
    }

    public boolean isConfirmado() {
        return confirmado;
    }
}
