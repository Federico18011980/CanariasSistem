package Util;

import Modelo.Productos;
import Modelo.ProductosDao;
import Servicios.CodigoBarrasService;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class GeneradorCodigoBarrasView extends JFrame {

    private JTextField txtBuscar;
    private JButton btnBuscar, btnAgregarSeleccion, btnGenerarPDF, btnLimpiar;
    private JTable tablaProductosDisponibles, tablaProductosSeleccionados;
    private DefaultTableModel modeloDisponibles, modeloSeleccionados;
    private ProductosDao proDao = new ProductosDao();

    // Colores Estación de Trabajo
    private Color azulCanarias = new Color(44, 62, 80);
    private Color verdeGo = new Color(39, 174, 96);
    private Color fondoGris = new Color(245, 246, 250);

    public GeneradorCodigoBarrasView() {
        setTitle("Canarias System - Estación de Etiquetas");
        setSize(1000, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        getContentPane().setBackground(fondoGris);

        // --- PANEL NORTE: BUSCADOR ---
        JPanel pnlBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 15));
        pnlBusqueda.setBackground(azulCanarias);
        
        JLabel lblTitulo = new JLabel("CATÁLOGO DE PRODUCTOS:");
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 13));
        
        txtBuscar = new JTextField(25);
        txtBuscar.setFont(new Font("SansSerif", Font.PLAIN, 14));
        
        btnBuscar = new JButton("Buscar");
        estiloBoton(btnBuscar, new Color(52, 152, 219));

        pnlBusqueda.add(lblTitulo);
        pnlBusqueda.add(txtBuscar);
        pnlBusqueda.add(btnBuscar);
        add(pnlBusqueda, BorderLayout.NORTH);

        // --- PANEL CENTRAL: TABLAS DUALES ---
        JPanel pnlTablas = new JPanel(new GridLayout(1, 2, 15, 0));
        pnlTablas.setBackground(fondoGris);
        pnlTablas.setBorder(new EmptyBorder(15, 15, 15, 15));

        // 1. Panel Disponibles
       // 1. Panel Disponibles
        modeloDisponibles = new DefaultTableModel(new String[]{"ID", "Código", "Descripción", "Precio"}, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        tablaProductosDisponibles = new JTable(modeloDisponibles);
        configurarTabla(tablaProductosDisponibles); // Solo configura la tabla

        JScrollPane scrollDisp = new JScrollPane(tablaProductosDisponibles);
        // Aplicamos el borde directamente al SCROLL aquí mismo
        scrollDisp.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY), "Productos en Inventario",
                TitledBorder.LEFT, TitledBorder.TOP, new Font("SansSerif", Font.BOLD, 12), azulCanarias));
        pnlTablas.add(scrollDisp);

        // 2. Panel Seleccionados
    modeloSeleccionados = new DefaultTableModel(new String[]{"ID", "Código", "Descripción", "Cantidad", "Precio"}, 0) {
        @Override public boolean isCellEditable(int r, int c) { return c == 3; } 
    };
    tablaProductosSeleccionados = new JTable(modeloSeleccionados);
    configurarTabla(tablaProductosSeleccionados);
    
    JScrollPane scrollSel = new JScrollPane(tablaProductosSeleccionados);
    // Aplicamos el borde directamente al SCROLL aquí mismo
    scrollSel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.GRAY), "Lista para Impresión (Etiquetas)", 
            TitledBorder.LEFT, TitledBorder.TOP, new Font("SansSerif", Font.BOLD, 12), azulCanarias));
    pnlTablas.add(scrollSel);

    add(pnlTablas, BorderLayout.CENTER);

        // --- PANEL SUR: ACCIONES ---
        JPanel pnlAcciones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 15));
        pnlAcciones.setBackground(Color.WHITE);
        pnlAcciones.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY));

        btnAgregarSeleccion = new JButton("Añadir a Lista");
        estiloBoton(btnAgregarSeleccion, azulCanarias);
        
        btnLimpiar = new JButton("Vaciar Lista");
        estiloBoton(btnLimpiar, new Color(149, 165, 166));

        btnGenerarPDF = new JButton("GENERAR ETIQUETAS (PDF)");
        estiloBoton(btnGenerarPDF, verdeGo);
        btnGenerarPDF.setPreferredSize(new Dimension(220, 40));

        pnlAcciones.add(btnLimpiar);
        pnlAcciones.add(btnAgregarSeleccion);
        pnlAcciones.add(btnGenerarPDF);
        add(pnlAcciones, BorderLayout.SOUTH);

        configurarEventos();
    }

  private void configurarTabla(JTable t) {
    t.setRowHeight(28);
    t.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
    t.setSelectionBackground(new Color(235, 245, 251));
}

    private void estiloBoton(JButton b, Color c) {
        b.setBackground(c);
        b.setForeground(Color.WHITE);
        b.setFont(new Font("SansSerif", Font.BOLD, 12));
        b.setFocusPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setPreferredSize(new Dimension(150, 35));
    }

    private void configurarEventos() {
        // Búsqueda instantánea
        txtBuscar.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override public void keyReleased(java.awt.event.KeyEvent e) { buscarProductos(); }
        });

        // Doble clic para agregar
        tablaProductosDisponibles.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) agregarProductoASeleccionados();
            }
        });

        // Doble clic para quitar
        tablaProductosSeleccionados.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int fila = tablaProductosSeleccionados.getSelectedRow();
                    if (fila != -1) modeloSeleccionados.removeRow(fila);
                }
            }
        });

        btnBuscar.addActionListener(e -> buscarProductos());
        btnAgregarSeleccion.addActionListener(e -> agregarProductoASeleccionados());
        btnGenerarPDF.addActionListener(e -> generarPdfCodigos());
        
        btnLimpiar.addActionListener(e -> {
            if (JOptionPane.showConfirmDialog(this, "¿Vaciar la lista de impresión?", "Confirmar", 0) == 0)
                modeloSeleccionados.setRowCount(0);
        });

        buscarProductos();
    }

    private void buscarProductos() {
        String filtro = txtBuscar.getText().trim();
        List<Productos> lista = proDao.buscarPorCodigoDescripcion(filtro);
        modeloDisponibles.setRowCount(0);
        for (Productos p : lista) {
            modeloDisponibles.addRow(new Object[]{p.getId(), p.getCodigo(), p.getDescripcion(), p.getPrecio()});
        }
    }

    private void agregarProductoASeleccionados() {
        int fila = tablaProductosDisponibles.getSelectedRow();
        if (fila == -1) return;

        String codigo = modeloDisponibles.getValueAt(fila, 1).toString();
        
        // Si ya existe, aumentar cantidad
        for (int i = 0; i < modeloSeleccionados.getRowCount(); i++) {
            if (modeloSeleccionados.getValueAt(i, 1).equals(codigo)) {
                int cant = (int) modeloSeleccionados.getValueAt(i, 3);
                modeloSeleccionados.setValueAt(cant + 1, i, 3);
                return;
            }
        }

        Object[] nuevaFila = {
            modeloDisponibles.getValueAt(fila, 0),
            codigo,
            modeloDisponibles.getValueAt(fila, 2),
            1,
            modeloDisponibles.getValueAt(fila, 3)
        };
        modeloSeleccionados.addRow(nuevaFila);
    }

    private void generarPdfCodigos() {
        if (tablaProductosSeleccionados.getRowCount() > 0) {
            new CodigoBarrasService().generarReporteEtiquetas(tablaProductosSeleccionados);
        } else {
            JOptionPane.showMessageDialog(this, "La lista de impresión está vacía.");
        }
    }
}