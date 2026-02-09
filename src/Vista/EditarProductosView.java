package Vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;
import java.util.Map;
import Modelo.*;
import Vista.Modelo.ProductoTableModel;
import java.io.File;
import java.math.BigDecimal;

public class EditarProductosView extends JFrame {

    private JTable tabla;
    private JTextField txtBuscar;
    private JButton btnBuscar, btnGuardar, btnAumentar, btnExportar, btnImportar;
    private ProductoTableModel modelo;
    private EditarProductosDao dao;
    private Map<Integer, String> categorias;
    private JComboBox<String> cboCategoria;
    private JComboBox<String> cboProveedor;

    // Colores de Identidad Premium
    private java.awt.Color azulOscuro = new java.awt.Color(44, 62, 80);
    private java.awt.Color azulClaro = new java.awt.Color(52, 152, 219);
    private java.awt.Color verdeExito = new java.awt.Color(39, 174, 96);
    private java.awt.Color naranjaAlerta = new java.awt.Color(230, 126, 34);
    private java.awt.Color grisFondo = new java.awt.Color(245, 246, 250);

    public EditarProductosView() {
        setTitle("Canarias System - Centro de Control de Inventario");
        setSize(1300, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        dao = new EditarProductosDao();

        initComponents();
        cargarCombos();   
        cargarTabla();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        getContentPane().setBackground(java.awt.Color.WHITE);

        // --- PANEL SUPERIOR: TITULO Y HERRAMIENTAS ---
        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setBackground(azulOscuro);
        pnlHeader.setPreferredSize(new Dimension(1300, 120));
        pnlHeader.setBorder(new EmptyBorder(10, 20, 10, 20));

        JLabel lblTitulo = new JLabel("EDICIÓN MASIVA DE PRODUCTOS");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblTitulo.setForeground(java.awt.Color.WHITE);
        pnlHeader.add(lblTitulo, BorderLayout.NORTH);

        // Subpanel de filtros y acciones
        JPanel pnlAcciones = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        pnlAcciones.setOpaque(false);

        // Estilo para etiquetas blancas
        Font fontLabels = new Font("SansSerif", Font.BOLD, 12);
        
        JLabel l1 = new JLabel("Categoría:"); l1.setForeground(java.awt.Color.WHITE); l1.setFont(fontLabels);
        cboCategoria = new JComboBox<>();
        cboCategoria.setPreferredSize(new Dimension(150, 30));

        JLabel l2 = new JLabel("Proveedor:"); l2.setForeground(java.awt.Color.WHITE); l2.setFont(fontLabels);
        cboProveedor = new JComboBox<>();
        cboProveedor.setPreferredSize(new Dimension(150, 30));

        JLabel l3 = new JLabel("Buscar:"); l3.setForeground(java.awt.Color.WHITE); l3.setFont(fontLabels);
        txtBuscar = new JTextField(15);
        txtBuscar.setPreferredSize(new Dimension(150, 30));

        btnBuscar = new JButton("Filtrar");
        estiloBoton(btnBuscar, azulClaro);

        btnAumentar = new JButton("Actualizar %");
        estiloBoton(btnAumentar, naranjaAlerta);

        btnGuardar = new JButton("GUARDAR");
        estiloBoton(btnGuardar, verdeExito);

        btnExportar = new JButton("Exp Excel");
        estiloBoton(btnExportar, new java.awt.Color(46, 125, 50)); // Verde Excel

        btnImportar = new JButton("Imp Excel");
        estiloBoton(btnImportar, new java.awt.Color(106, 27, 154)); // Morado

        pnlAcciones.add(l1); pnlAcciones.add(cboCategoria);
        pnlAcciones.add(l2); pnlAcciones.add(cboProveedor);
        pnlAcciones.add(l3); pnlAcciones.add(txtBuscar);
        pnlAcciones.add(btnBuscar);
        pnlAcciones.add(btnAumentar);
        pnlAcciones.add(btnGuardar);
        pnlAcciones.add(btnExportar);
        pnlAcciones.add(btnImportar);

        pnlHeader.add(pnlAcciones, BorderLayout.CENTER);
        add(pnlHeader, BorderLayout.NORTH);

        // --- PANEL CENTRAL: LA TABLA ---
        JPanel pnlTabla = new JPanel(new BorderLayout());
        pnlTabla.setBackground(java.awt.Color.WHITE);
        pnlTabla.setBorder(new EmptyBorder(15, 20, 15, 20));

        tabla = new JTable();
        configurarTabla();
        
        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createLineBorder(new java.awt.Color(230, 230, 230)));
        scroll.getViewport().setBackground(java.awt.Color.WHITE);
        pnlTabla.add(scroll, BorderLayout.CENTER);

        add(pnlTabla, BorderLayout.CENTER);

        // --- EVENTOS ---
        btnBuscar.addActionListener(e -> filtrar());
        btnGuardar.addActionListener(e -> guardarCambios());
        btnAumentar.addActionListener(e -> aumentarPorcentaje());
        btnExportar.addActionListener(e -> exportarExcel());
        btnImportar.addActionListener(e -> importarExcel());
        txtBuscar.addActionListener(e -> filtrar());
    }

    private void estiloBoton(JButton btn, java.awt.Color colorFondo) {
        btn.setBackground(colorFondo);
        btn.setForeground(java.awt.Color.WHITE);
        btn.setFont(new Font("SansSerif", Font.BOLD, 12));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) { btn.setBackground(colorFondo.brighter()); }
            public void mouseExited(java.awt.event.MouseEvent evt) { btn.setBackground(colorFondo); }
        });
    }

    private void configurarTabla() {
        tabla.setRowHeight(35);
        tabla.setFont(new Font("SansSerif", Font.PLAIN, 13));
        tabla.setSelectionBackground(new java.awt.Color(236, 240, 241));
        tabla.setSelectionForeground(java.awt.Color.BLACK);
        tabla.setShowVerticalLines(false);
        tabla.setGridColor(new java.awt.Color(240, 240, 240));

        JTableHeader header = tabla.getTableHeader();
        header.setFont(new Font("SansSerif", Font.BOLD, 13));
        header.setBackground(new java.awt.Color(236, 240, 241));
        header.setPreferredSize(new Dimension(0, 45));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        tabla.setDefaultRenderer(Object.class, centerRenderer);
    }

    // --- LOGICA REUTILIZADA (Impecable como la tenías) ---

    private void cargarTabla() {
        List<Productos> lista = dao.listarTodos();
        modelo = new ProductoTableModel(lista);
        tabla.setModel(modelo);
    }

    private void guardarCambios() {
        boolean ok = dao.actualizarMasivo(modelo.getLista());
        if (ok) {
            JOptionPane.showMessageDialog(this, "✅ Cambios guardados correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "❌ Error al guardar cambios", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void aumentarPorcentaje() {
        String input = JOptionPane.showInputDialog(this, "Ingrese el porcentaje a aumentar (ej: 10):", "Actualización de Precios", JOptionPane.QUESTION_MESSAGE);
        if (input == null || input.isEmpty()) return;
        try {
            double porcentaje = Double.parseDouble(input);
            for (Productos p : modelo.getLista()) {
                double nuevo = p.getPrecio().doubleValue() * (1 + porcentaje / 100);
                p.setPrecio(BigDecimal.valueOf(nuevo).setScale(2, java.math.RoundingMode.HALF_UP));
            }
            modelo.actualizarDatos();
            JOptionPane.showMessageDialog(this, "Precios actualizados en la tabla. No olvide 'Guardar Cambios'.");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Valor inválido");
        }
    }

    private void cargarCombos() {
        categorias = dao.listarCategorias();
        cboCategoria.removeAllItems();
        cboCategoria.addItem("Todas las Categorías");
        for (String nombre : categorias.values()) {
            cboCategoria.addItem(nombre);
        }

        cboProveedor.removeAllItems();
        cboProveedor.addItem("Todos los Proveedores");
        for (String prov : dao.listarProveedores()) {
            cboProveedor.addItem(prov);
        }
    }

    private void filtrar() {
        Integer idCategoria = null;
        if (cboCategoria.getSelectedIndex() > 0) {
            String nombre = cboCategoria.getSelectedItem().toString();
            for (Map.Entry<Integer, String> e : categorias.entrySet()) {
                if (e.getValue().equals(nombre)) {
                    idCategoria = e.getKey();
                    break;
                }
            }
        }

        String proveedor = null;
        if (cboProveedor.getSelectedIndex() > 0) {
            proveedor = cboProveedor.getSelectedItem().toString();
        }

        String busqueda = txtBuscar.getText().trim();
         List<Productos> lista = dao.listarFiltrados(idCategoria, proveedor);
       // List<Productos> lista = dao.listarFiltrados(idCategoria, proveedor, busqueda);
        modelo = new ProductoTableModel(lista);
        tabla.setModel(modelo);
    }

    private void exportarExcel() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Exportar Inventario a Excel");
        if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File archivo = chooser.getSelectedFile();
            if (!archivo.getName().endsWith(".xlsx")) {
                archivo = new File(archivo.getAbsolutePath() + ".xlsx");
            }
            if (Util.ExcelExporter.exportarProductos(modelo.getLista(), archivo)) {
                JOptionPane.showMessageDialog(this, "Archivo exportado con éxito");
            }
        }
    }

    private void importarExcel() {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File archivo = chooser.getSelectedFile();
            List<ImportError> errores = dao.validarExcel(archivo);
            if (!errores.isEmpty()) {
                new ErroresImportacionDialog(this, errores).setVisible(true);
                return;
            }
            if (dao.importarDesdeExcel(archivo)) {
                JOptionPane.showMessageDialog(this, "Importación exitosa");
                cargarTabla();
            }
        }
    }
}

