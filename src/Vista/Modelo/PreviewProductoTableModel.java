package Vista.Modelo;

import Modelo.PreviewProducto;
import javax.swing.table.AbstractTableModel;
import java.util.List;

public class PreviewProductoTableModel extends AbstractTableModel {

    private final String[] columnas = {
        "Código", "Descripción", "Precio", "Stock", "Categoría", "Proveedor"
    };

    private List<PreviewProducto> lista;

    public PreviewProductoTableModel(List<PreviewProducto> lista) {
        this.lista = lista;
    }

    @Override
    public int getRowCount() {
        return lista.size();
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

        PreviewProducto p = lista.get(row);

        switch (col) {
            case 0: return p.getCodigo();
            case 1: return p.getDescripcion();
            case 2: return p.getPrecio();
            case 3: return p.getStock();
            case 4: return p.getCategoria();
            case 5: return p.getProveedor();
        }
        return null;
    }
}
