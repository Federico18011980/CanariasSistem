package Vista;

import Modelo.Productos;
import Modelo.ProductosDao;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.util.List;

public class VentaBuscarProducto extends JDialog {

    private JTable tabla;
    private JTextField txtBuscar;
    private DefaultTableModel modelo;
    private Productos productoSeleccionado = null;
    private ProductosDao dao = new ProductosDao();

    // Colores Sistema
    private Color azulCanarias = new Color(44, 62, 80);
    private Color verdeAccion = new Color(39, 174, 96);

    public VentaBuscarProducto(Frame parent) {
        super(parent, "Buscador de Productos - Canarias System", true);
        initComponents();
        configurarEventos();
        cargarProductos();
    }

    private void initComponents() {
        setSize(700, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        // --- PANEL NORTE: BUSCADOR ---
        JPanel pnlNorte = new JPanel(new BorderLayout());
        pnlNorte.setBackground(azulCanarias);
        pnlNorte.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel lblIcono = new JLabel("🔍 "); // Icono visual
        lblIcono.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));
        lblIcono.setForeground(Color.WHITE);

        txtBuscar = new JTextField();
        txtBuscar.setFont(new Font("SansSerif", Font.ITALIC, 16));
        txtBuscar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.WHITE, 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        txtBuscar.setToolTipText("Escriba el nombre o código del producto...");

        pnlNorte.add(lblIcono, BorderLayout.WEST);
        pnlNorte.add(txtBuscar, BorderLayout.CENTER);
        add(pnlNorte, BorderLayout.NORTH);

        // --- PANEL CENTRAL: TABLA ---
        JPanel pnlCentro = new JPanel(new BorderLayout());
        pnlCentro.setBorder(new EmptyBorder(10, 15, 10, 15));
        pnlCentro.setBackground(Color.WHITE);

        modelo = new DefaultTableModel(
                new String[]{"ID", "CÓDIGO", "DESCRIPCIÓN", "STOCK", "PRECIO"}, 0
        ) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        tabla = new JTable(modelo);
        configurarTabla();
        
        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));
        pnlCentro.add(scroll, BorderLayout.CENTER);
        add(pnlCentro, BorderLayout.CENTER);

        // --- PANEL SUR: BOTONES ---
        JPanel pnlSur = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        pnlSur.setBackground(new Color(245, 246, 250));

        JButton btnSeleccionar = new JButton("SELECCIONAR (Enter)");
        btnSeleccionar.setBackground(verdeAccion);
        btnSeleccionar.setForeground(Color.WHITE);
        btnSeleccionar.setFont(new Font("SansSerif", Font.BOLD, 12));
        btnSeleccionar.setFocusPainted(false);
        btnSeleccionar.setPreferredSize(new Dimension(180, 35));
        btnSeleccionar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btnSeleccionar.addActionListener(e -> seleccionarProducto());
        
        pnlSur.add(btnSeleccionar);
        add(pnlSur, BorderLayout.SOUTH);
    }

    private void configurarTabla() {
        tabla.setRowHeight(30);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        tabla.getTableHeader().setBackground(new Color(236, 240, 241));
        
        // Centrar columnas específicas
        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(JLabel.CENTER);
        tabla.getColumnModel().getColumn(0).setPreferredWidth(40); // ID
        tabla.getColumnModel().getColumn(1).setCellRenderer(center); // Código
        tabla.getColumnModel().getColumn(3).setCellRenderer(center); // Stock
    }

    private void configurarEventos() {
        // Búsqueda en tiempo real
        txtBuscar.getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { filtrar(); }
            @Override public void removeUpdate(DocumentEvent e) { filtrar(); }
            @Override public void changedUpdate(DocumentEvent e) { filtrar(); }
        });

        // Doble clic en tabla
        tabla.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) seleccionarProducto();
            }
        });

        // Enter en el campo de texto para bajar a la tabla
        txtBuscar.addActionListener(e -> {
            if (tabla.getRowCount() > 0) {
                tabla.setRowSelectionInterval(0, 0);
                tabla.requestFocus();
            }
        });

        // Enter en la tabla para seleccionar
        tabla.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    e.consume(); // Evita que el foco baje a la siguiente fila
                    seleccionarProducto();
                }
            }
        });
    }

    private void cargarProductos() {
        actualizarTabla(dao.ListarProductos());
    }

    private void filtrar() {
        String texto = txtBuscar.getText().trim();
        if (texto.isEmpty()) {
            cargarProductos();
        } else {
            actualizarTabla(dao.buscarPorCodigoDescripcion(texto));
        }
    }

    private void actualizarTabla(List<Productos> lista) {
        modelo.setRowCount(0);
        for (Productos p : lista) {
            modelo.addRow(new Object[]{
                p.getId(), p.getCodigo(), p.getDescripcion(), p.getStock(), p.getPrecio()
            });
        }
    }

    private void seleccionarProducto() {
        int fila = tabla.getSelectedRow();
        if (fila != -1) {
            productoSeleccionado = new Productos();
            productoSeleccionado.setId((int) modelo.getValueAt(fila, 0));
            productoSeleccionado.setCodigo(modelo.getValueAt(fila, 1).toString());
            productoSeleccionado.setDescripcion(modelo.getValueAt(fila, 2).toString());
            productoSeleccionado.setStock(new BigDecimal(modelo.getValueAt(fila, 3).toString()));
            productoSeleccionado.setPrecio(new BigDecimal(modelo.getValueAt(fila, 4).toString()));
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione un producto de la lista.");
        }
    }

    public Productos getProductoSeleccionado() {
        return productoSeleccionado;
    }
}