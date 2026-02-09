package Vista;

import Modelo.ImportError;
import Vista.Modelo.ImportErrorTableModel;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ErroresImportacionDialog extends JDialog {

    public ErroresImportacionDialog(JFrame parent, List<ImportError> errores) {
        super(parent, "Errores en el archivo", true);

        JTable tabla = new JTable(new ImportErrorTableModel(errores));
        JScrollPane scroll = new JScrollPane(tabla);

        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> dispose());

        add(scroll, BorderLayout.CENTER);
        add(btnCerrar, BorderLayout.SOUTH);

        setSize(700, 350);
        setLocationRelativeTo(parent);
    }
}
