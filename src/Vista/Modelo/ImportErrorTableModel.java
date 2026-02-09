package Vista.Modelo;

import Modelo.ImportError;
import javax.swing.table.AbstractTableModel;
import java.util.List;

public class ImportErrorTableModel extends AbstractTableModel {

    private final String[] columnas = {
        "Fila", "Columna", "Error"
    };

    private List<ImportError> errores;

    public ImportErrorTableModel(List<ImportError> errores) {
        this.errores = errores;
    }

    @Override
    public int getRowCount() {
        return errores.size();
    }

    @Override
    public int getColumnCount() {
        return columnas.length;
    }

    @Override
    public String getColumnName(int col) {
        return columnas[col];
    }

    @Override
    public Object getValueAt(int row, int col) {
        ImportError e = errores.get(row);

        switch (col) {
            case 0: return e.getFila();
            case 1: return e.getColumna();
            case 2: return e.getMensaje();
        }
        return null;
    }
}
