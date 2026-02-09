
package Util;

import Modelo.Proveedor;
import Modelo.ProveedorDao;
import Servicios.ListadosService;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class ListaProveedores extends JFrame  {
    
    private JTable tablaproveedores;
    private DefaultTableModel modelo;
    private JButton btnPdf, btnCerrar;
    private ProveedorDao proDao = new ProveedorDao();

    public ListaProveedores() {
        setTitle("LISTADO DE PROVEEDORES");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        // --- PANEL CABECERA ---
        JPanel pnlHeader = new JPanel();
        pnlHeader.setBackground(new Color(44, 62, 80)); // Azul oscuro elegante
        pnlHeader.setPreferredSize(new Dimension(800, 60));
        JLabel lblTitulo = new JLabel("PROVEEDORES REGISTRADOS");
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 20));
        pnlHeader.add(lblTitulo);
        add(pnlHeader, BorderLayout.NORTH);

        // --- TABLA ---
        String[] columnas = {"ID", "NOMBRE COMPLETO", "TELEFONO", "SITIO WEB", "DOMICILIO"};
        modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaproveedores = new JTable(modelo);
        configurarEstiloTabla();
        JScrollPane sp = new JScrollPane(tablaproveedores);
        sp.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        sp.getViewport().setBackground(Color.WHITE);
        add(sp, BorderLayout.CENTER);

        // --- PANEL BOTONES ---
        JPanel pnlBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 15));
        pnlBotones.setBackground(Color.WHITE);

        btnPdf = new JButton("Generar Listado PDF");
        btnPdf.setBackground(new Color(231, 76, 60)); // Rojo Alizarin
        btnPdf.setForeground(Color.WHITE);
        btnPdf.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnPdf.setFocusPainted(false);
        btnPdf.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnCerrar = new JButton("Cerrar");
        btnCerrar.setFont(new Font("SansSerif", Font.PLAIN, 13));

        pnlBotones.add(btnPdf);
        pnlBotones.add(btnCerrar);
        add(pnlBotones, BorderLayout.SOUTH);

        // --- EVENTOS ---
        btnPdf.addActionListener(e -> {
            List<Proveedor> lista = proDao.ListarProvedor();
            new ListadosService().generarReporteProveedores(lista);
        });

        btnCerrar.addActionListener(e -> dispose());

        listarProveedores(); // Carga los datos al iniciar
    }

    private void listarProveedores() {
        List<Proveedor> lista = proDao.ListarProvedor();
        modelo.setRowCount(0);
        for (Proveedor p : lista) {
            Object[] fila = {p.getId(), p.getNombre(), p.getTelefono(), p.getEmail(), p.getDomicilio()};
            modelo.addRow(fila);
        }
    }

    private void configurarEstiloTabla() {
        tablaproveedores.setRowHeight(30);
        tablaproveedores.setShowVerticalLines(false);
        tablaproveedores.setGridColor(new Color(230, 230, 230));
        tablaproveedores.setSelectionBackground(new Color(52, 152, 219)); // Azul claro al seleccionar

        // Estilo del Header
        JTableHeader header = tablaproveedores.getTableHeader();
        header.setFont(new Font("SansSerif", Font.BOLD, 13));
        header.setBackground(new Color(240, 240, 240));
        header.setForeground(new Color(50, 50, 50));
        header.setPreferredSize(new Dimension(0, 40));

        // Centrar columnas
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        tablaproveedores.getColumnModel().getColumn(0).setPreferredWidth(50);
        tablaproveedores.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        tablaproveedores.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
    }
    
}
