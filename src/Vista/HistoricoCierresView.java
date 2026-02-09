package Vista;

import Modelo.CajaCierreDao;
import Util.ReporteCajaPDF;
import com.toedter.calendar.JDateChooser;
import java.awt.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class HistoricoCierresView extends JFrame {

    private JTable tabla;
    private DefaultTableModel modelo;
    private JDateChooser dcInicio, dcFin; // Cambio a selector de fecha
    private JButton btnFiltrar, btnPDF;
    private CajaCierreDao cierreDao = new CajaCierreDao();

    // Paleta de colores "Business"
    private Color azulEmpresarial = new Color(41, 128, 185);
    private Color fondoClaro = new Color(248, 249, 249);

    public HistoricoCierresView() {
        setTitle("Historial de Cierres de Caja - Reportes Gerenciales");
        setSize(900, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        // --- PANEL SUPERIOR: FILTROS ELEGANTES ---
        JPanel pnlNorte = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 15));
        pnlNorte.setBackground(new Color(/*236, 240, 241*/21, 67, 96));
        //private java.awt.Color azulHeader = new java.awt.Color(21, 67, 96);
        pnlNorte.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));

        dcInicio = new JDateChooser();
        dcInicio.setPreferredSize(new Dimension(150, 30));
        dcInicio.setDate(java.sql.Date.valueOf(LocalDate.now().minusMonths(1))); // Por defecto hace un mes

        dcFin = new JDateChooser();
        dcFin.setPreferredSize(new Dimension(150, 30));
        dcFin.setDate(java.sql.Date.valueOf(LocalDate.now()));

        btnFiltrar = new JButton("Filtrar Historial");
        estiloBoton(btnFiltrar, azulEmpresarial);

        pnlNorte.add(crearEtiquetaBlanca("Desde:"));
        pnlNorte.add(dcInicio);
        pnlNorte.add(crearEtiquetaBlanca("Hasta:"));
        pnlNorte.add(dcFin);
        pnlNorte.add(btnFiltrar);

        add(pnlNorte, BorderLayout.NORTH);

        // --- PANEL CENTRAL: TABLA ---
        modelo = new DefaultTableModel(
                new String[]{"ID", "Fecha de Cierre", "Iniciado", "Cerrado", "Diferencia", "Responsable"}, 0
        ) { @Override public boolean isCellEditable(int r, int c) { return false; } };

        tabla = new JTable(modelo);
        configurarTabla();
        
        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(new EmptyBorder(15, 15, 15, 15));
        scroll.getViewport().setBackground(Color.WHITE);
        add(scroll, BorderLayout.CENTER);

        // --- PANEL SUR: EXPORTACIÓN ---
        JPanel pnlSur = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 15));
        pnlSur.setBackground(fondoClaro);

        btnPDF = new JButton("Exportar Reporte PDF");
        estiloBoton(btnPDF, new Color(192, 57, 43)); // Rojo PDF
        btnPDF.setPreferredSize(new Dimension(200, 40));

        pnlSur.add(btnPDF);
        add(pnlSur, BorderLayout.SOUTH);

        // Eventos
        btnFiltrar.addActionListener(e -> cargarDatos());
        btnPDF.addActionListener(e -> generarPDF());
    }

    private void estiloBoton(JButton b, Color c) {
        b.setBackground(c);
        b.setForeground(Color.WHITE);
        b.setFont(new Font("SansSerif", Font.BOLD, 12));
        b.setFocusPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void configurarTabla() {
        tabla.setRowHeight(30);
        tabla.setShowVerticalLines(false);
        tabla.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        tabla.getTableHeader().setBackground(new Color(242, 244, 244));
        
        DefaultTableCellRenderer render = new DefaultTableCellRenderer();
        render.setHorizontalAlignment(JLabel.CENTER);
        
        // Centrar columnas ID y Fecha
        tabla.getColumnModel().getColumn(0).setCellRenderer(render);
        tabla.getColumnModel().getColumn(1).setCellRenderer(render);
        
        // Formato moneda para montos
        DefaultTableCellRenderer renderMonto = new DefaultTableCellRenderer() {
            @Override
            public void setValue(Object value) {
                setHorizontalAlignment(JLabel.RIGHT);
                super.setValue((value != null) ? "$ " + value : "$ 0.00");
            }
        };
        tabla.getColumnModel().getColumn(2).setCellRenderer(renderMonto);
        tabla.getColumnModel().getColumn(3).setCellRenderer(renderMonto);
        tabla.getColumnModel().getColumn(4).setCellRenderer(renderMonto);
    }

    private void cargarDatos() {
        if (dcInicio.getDate() == null || dcFin.getDate() == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un rango de fechas válido.");
            return;
        }

        LocalDate inicio = dcInicio.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate fin = dcFin.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        List<Map<String, Object>> lista = cierreDao.listarHistoricoConDetalle(inicio, fin);
        modelo.setRowCount(0);

        for (Map<String, Object> m : lista) {
            modelo.addRow(new Object[]{
                m.get("id"), m.get("fecha"), m.get("monto_inicial"),
                m.get("monto_final"), m.get("diferencia"), m.get("usuario")
            });
        }
    }

    private void generarPDF() {
        if (tabla.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No hay datos para exportar. Primero filtre la información.");
            return;
        }
        
        String f1 = ((JTextField)dcInicio.getDateEditor().getUiComponent()).getText();
        String f2 = ((JTextField)dcFin.getDateEditor().getUiComponent()).getText();
        
        ReporteCajaPDF reporteService = new ReporteCajaPDF();
        reporteService.generarReporteHistorico(tabla, f1, f2);
    }
    
     private JLabel crearEtiquetaBlanca(String texto) {
        JLabel l = new JLabel(texto);
        l.setForeground(java.awt.Color.WHITE);
        l.setFont(new java.awt.Font("SansSerif", java.awt.Font.BOLD, 12));
        return l;
    }
}