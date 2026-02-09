package Util;

import Modelo.Usuarios;
import Modelo.UsuariosDao;
import Servicios.ListadosService;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class ListaUsuariosView extends JFrame {

    private JTable tablaUsuarios;
    private DefaultTableModel modelo;
    private JButton btnPdf, btnCerrar;
    private UsuariosDao usuDao = new UsuariosDao();

    public ListaUsuariosView() {
        setTitle("Gestión de Usuarios del Sistema");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        // --- PANEL CABECERA ---
        JPanel pnlHeader = new JPanel();
        pnlHeader.setBackground(new Color(44, 62, 80)); // Azul oscuro elegante
        pnlHeader.setPreferredSize(new Dimension(800, 60));
        JLabel lblTitulo = new JLabel("USUARIOS REGISTRADOS");
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 20));
        pnlHeader.add(lblTitulo);
        add(pnlHeader, BorderLayout.NORTH);

        // --- TABLA ---
        String[] columnas = {"ID", "NOMBRE COMPLETO", "CORREO ELECTRÓNICO", "ROL / ACCESO"};
        modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaUsuarios = new JTable(modelo);
        configurarEstiloTabla();
        JScrollPane sp = new JScrollPane(tablaUsuarios);
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
            List<Usuarios> lista = usuDao.ListarUsuarios();
            new ListadosService().generarReporteUsuarios(lista);
        });

        btnCerrar.addActionListener(e -> dispose());

        listarUsuarios(); // Carga los datos al iniciar
    }

    private void listarUsuarios() {
        List<Usuarios> lista = usuDao.ListarUsuarios();
        modelo.setRowCount(0);
        for (Usuarios u : lista) {
            Object[] fila = {u.getId(), u.getNombre(), u.getCorreo(), u.getRol()};
            modelo.addRow(fila);
        }
    }

    private void configurarEstiloTabla() {
        tablaUsuarios.setRowHeight(30);
        tablaUsuarios.setShowVerticalLines(false);
        tablaUsuarios.setGridColor(new Color(230, 230, 230));
        tablaUsuarios.setSelectionBackground(new Color(52, 152, 219)); // Azul claro al seleccionar

        // Estilo del Header
        JTableHeader header = tablaUsuarios.getTableHeader();
        header.setFont(new Font("SansSerif", Font.BOLD, 13));
        header.setBackground(new Color(240, 240, 240));
        header.setForeground(new Color(50, 50, 50));
        header.setPreferredSize(new Dimension(0, 40));

        // Centrar columnas
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        tablaUsuarios.getColumnModel().getColumn(0).setPreferredWidth(50);
        tablaUsuarios.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        tablaUsuarios.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
    }
}