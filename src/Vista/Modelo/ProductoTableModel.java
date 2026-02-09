package Vista.Modelo;

import javax.swing.table.AbstractTableModel;
import java.util.List;
import Modelo.Productos;
import java.math.BigDecimal;

public class ProductoTableModel extends AbstractTableModel {

    private final String[] columnas = {
        "ID", "Código", "Descripción", "Precio", "Stock", "Proveedor"
    };

    private List<Productos> lista;

    public ProductoTableModel(List<Productos> lista) {
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
    public String getColumnName(int column) {
        return columnas[column];
    }

    @Override
    public Object getValueAt(int row, int col) {
        Productos p = lista.get(row);

        switch (col) {
            case 0:
                return p.getId();
            case 1:
                return p.getCodigo();
            case 2:
                return p.getDescripcion();
            case 3:
                return p.getPrecio();
            case 4:
                return p.getStock();
            case 5:
                return p.getProveedor();
            default:
                return null;
        }
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        return col >= 2; // editable desde descripción
    }

    @Override
    public void setValueAt(Object value, int row, int col) {
        Productos p = lista.get(row);

        try {
            switch (col) {
                case 2:
                    p.setDescripcion(value.toString());
                    break;
                case 3:
                    p.setPrecio(new BigDecimal(value.toString()));
                    break;
                case 4:
                    p.setStock(new BigDecimal(value.toString()));
                    break;
                case 5:
                    p.setProveedor(value.toString());
                    break;
            }
        } catch (Exception e) {
            // evita crash por valores inválidos
        }

        fireTableCellUpdated(row, col);
    }

    public List<Productos> getLista() {
        return lista;
    }
    
    public void actualizarDatos() {
        fireTableDataChanged();
    }

}

