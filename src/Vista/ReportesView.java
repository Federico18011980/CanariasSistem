package Vista;

import Modelo.*;
import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.toedter.calendar.JDateChooser; // Requiere jcalendar.jar
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ReportesView extends JFrame {

    private JComboBox<String> cbTipoReporte;
    private JDateChooser jdDesde, jdHasta; // Reemplazamos JTextFields
    private JTable tabla;
    private DefaultTableModel modelo;
    private String tipoReporte = "Ventas";
    
    private JLabel lblTotalEntradas, lblTotalSalidas, lblSaldoFinal;
    
    // Colores Dashboard
    private java.awt.Color azulHeader = new java.awt.Color(21, 67, 96);
    private java.awt.Color grisFondo = new java.awt.Color(240, 243, 244);
    private java.awt.Color verdeExcel = new java.awt.Color(30, 132, 73);
    private java.awt.Color rojoPdf = new java.awt.Color(192, 57, 43);

    public ReportesView() {
        initComponents();
        getContentPane().setBackground(java.awt.Color.WHITE);
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        setTitle("Canarias System - Centro de Reportes");
        setSize(1100, 650);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // --- PANEL NORTE: FILTROS PREMIUM ---
        JPanel pnlFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 15));
        pnlFiltros.setBackground(azulHeader);
        pnlFiltros.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel lblRep = new JLabel("Tipo de Reporte:");
        lblRep.setForeground(java.awt.Color.WHITE);
        lblRep.setFont(new java.awt.Font("SansSerif", java.awt.Font.BOLD, 12));
        
        cbTipoReporte = new JComboBox<>(new String[]{"Ventas", "Devoluciones", "Ventas Anuladas", "Caja"});
        cbTipoReporte.setPreferredSize(new Dimension(180, 30));

        jdDesde = new JDateChooser();
        jdDesde.setPreferredSize(new Dimension(140, 30));
        jdDesde.setDate(new Date()); // Fecha hoy por defecto

        jdHasta = new JDateChooser();
        jdHasta.setPreferredSize(new Dimension(140, 30));
        jdHasta.setDate(new Date());

        JButton btnBuscar = new JButton("GENERAR REPORTE");
        estiloBoton(btnBuscar, new java.awt.Color(52, 152, 219));

        pnlFiltros.add(lblRep);
        pnlFiltros.add(cbTipoReporte);
        pnlFiltros.add(crearEtiquetaBlanca("Desde:"));
        pnlFiltros.add(jdDesde);
        pnlFiltros.add(crearEtiquetaBlanca("Hasta:"));
        pnlFiltros.add(jdHasta);
        pnlFiltros.add(btnBuscar);

        add(pnlFiltros, BorderLayout.NORTH);

        // --- PANEL CENTRAL: TABLA ---
        JPanel pnlTabla = new JPanel(new BorderLayout());
        pnlTabla.setBorder(new EmptyBorder(20, 20, 20, 20));
        pnlTabla.setBackground(java.awt.Color.WHITE);

        modelo = new DefaultTableModel();
        tabla = new JTable(modelo);
        configurarTabla();
        pnlTabla.add(new JScrollPane(tabla), BorderLayout.CENTER);
        add(pnlTabla, BorderLayout.CENTER);

        // --- PANEL SUR: TOTALES Y EXPORTACIÓN ---
        JPanel pnlSur = new JPanel(new BorderLayout());
        pnlSur.setBackground(grisFondo);
        pnlSur.setBorder(new EmptyBorder(15, 25, 15, 25));

        // Subpanel Totales
        JPanel pnlTotales = new JPanel(new FlowLayout(FlowLayout.LEFT, 30, 10));
        pnlTotales.setOpaque(false);
        
        lblTotalEntradas = new JLabel("Ingresos: $0.00");
        lblTotalSalidas = new JLabel("Egresos: $0.00");
        lblSaldoFinal = new JLabel("Balance: $0.00");
        
        Font fTotales = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD); // Para PDF, pero usemos awt.Font para UI
        java.awt.Font awtFont = new java.awt.Font("SansSerif", java.awt.Font.BOLD, 14);
        
        lblTotalEntradas.setFont(awtFont);
        lblTotalSalidas.setFont(awtFont);
        lblSaldoFinal.setFont(new java.awt.Font("SansSerif", java.awt.Font.BOLD, 16));
        lblSaldoFinal.setForeground(azulHeader);

        pnlTotales.add(lblTotalEntradas);
        pnlTotales.add(lblTotalSalidas);
        pnlTotales.add(lblSaldoFinal);

        // Subpanel Botones
        JPanel pnlBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 5));
        pnlBotones.setOpaque(false);
        
        JButton btnPdf = new JButton("Exportar PDF");
        estiloBoton(btnPdf, rojoPdf);
        
        JButton btnExcel = new JButton("Exportar Excel");
        estiloBoton(btnExcel, verdeExcel);

        pnlBotones.add(btnPdf);
        pnlBotones.add(btnExcel);

        pnlSur.add(pnlTotales, BorderLayout.WEST);
        pnlSur.add(pnlBotones, BorderLayout.EAST);
        add(pnlSur, BorderLayout.SOUTH);

        // Acciones
        btnBuscar.addActionListener(e -> cargarReporte());
        btnExcel.addActionListener(e -> exportarExcel(tabla, "Reporte de " + tipoReporte));
        btnPdf.addActionListener(e -> exportarPDF(tabla, "Reporte de " + tipoReporte));
    }

    private JLabel crearEtiquetaBlanca(String texto) {
        JLabel l = new JLabel(texto);
        l.setForeground(java.awt.Color.WHITE);
        l.setFont(new java.awt.Font("SansSerif", java.awt.Font.BOLD, 12));
        return l;
    }

    private void estiloBoton(JButton btn, java.awt.Color color) {
        btn.setBackground(color);
        btn.setForeground(java.awt.Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new java.awt.Font("SansSerif", java.awt.Font.BOLD, 12));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(150, 35));
    }

    private void configurarTabla() {
        tabla.setRowHeight(30);
        tabla.getTableHeader().setFont(new java.awt.Font("SansSerif", java.awt.Font.BOLD, 12));
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        tabla.setDefaultRenderer(Object.class, centerRenderer);
    }

    private LocalDate getFecha(JDateChooser jd) {
        if (jd.getDate() == null) return null;
        return jd.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    // ============================================
    // CARGA DE DATOS (Lógica adaptada a LocalDate)
    // ============================================
    
    private void cargarReporte() {
        tipoReporte = cbTipoReporte.getSelectedItem().toString();
        modelo.setRowCount(0);
        modelo.setColumnCount(0);
        
        LocalDate desde = getFecha(jdDesde);
        LocalDate hasta = getFecha(jdHasta);

        if (desde == null || hasta == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un rango de fechas válido");
            return;
        }

        switch (tipoReporte) {
            case "Ventas": cargarVentas(desde, hasta); break;
            case "Devoluciones": cargarDevoluciones(desde, hasta); break;
            case "Ventas Anuladas": cargarAnuladas(desde, hasta); break;
            case "Caja": cargarCaja(desde, hasta); break;
        }
    }

    private void cargarVentas(LocalDate desde, LocalDate hasta) {
        modelo.setColumnIdentifiers(new String[]{"ID", "Cliente", "Vendedor", "Total", "Pago", "Estado", "Fecha"});
        VentaDao dao = new VentaDao();
        List<Venta> lista = dao.listarVentasPorFecha(desde, hasta);
        for (Venta v : lista) {
            modelo.addRow(new Object[]{v.getId(), v.getCliente(), v.getVendedor(), v.getTotal(), v.getPago(), v.getEstado(), v.getFecha()});
        }
    }

    private void cargarCaja(LocalDate desde, LocalDate hasta) {
        modelo.setColumnIdentifiers(new String[]{"Fecha", "Tipo", "Monto", "Usuario", "Descripción"});
        CajaMovimientoDao cajaDao = new CajaMovimientoDao();
        List<CajaMovimiento> movimientos = cajaDao.obtenerMovimientosPorFecha(desde, hasta);
        
        BigDecimal entradas = BigDecimal.ZERO;
        BigDecimal salidas = BigDecimal.ZERO;

        for (CajaMovimiento mov : movimientos) {
            modelo.addRow(new Object[]{mov.getFecha(), mov.getTipo(), mov.getMonto(), mov.getUsuario(), mov.getDescripcion()});
            if ("ENTRADA".equalsIgnoreCase(mov.getTipo())) {
                entradas = entradas.add(mov.getMonto());
            } else {
                salidas = salidas.add(mov.getMonto());
            }
        }
        lblTotalEntradas.setText("Ingresos: $" + entradas);
        lblTotalSalidas.setText("Egresos: $" + salidas);
        lblSaldoFinal.setText("Balance: $" + entradas.add(salidas));
    }
    
    // (Métodos cargarDevoluciones y cargarAnuladas siguen la misma lógica adaptada...)
    private void cargarDevoluciones(LocalDate desde, LocalDate hasta) {
        modelo.setColumnIdentifiers(new String[]{"Venta", "Producto", "Cant.", "Precio", "Total", "Tipo", "Pago", "Fecha"});
        DevolucionesDao dao = new DevolucionesDao();
        List<Devolucion> lista = dao.listarDevolucionesPorFecha(desde, hasta);
        for (Devolucion d : lista) {
            modelo.addRow(new Object[]{d.getIdVenta(), d.getCodProducto(), d.getCantidad(), d.getPrecio(), d.getTotal(), d.getTipo(), d.getFormaPago(), d.getFecha()});
        }
    }

    private void cargarAnuladas(LocalDate desde, LocalDate hasta) {
        modelo.setColumnIdentifiers(new String[]{"Venta", "Total", "Motivo", "Usuario", "Fecha"});
        VentaAnuladaDao dao = new VentaAnuladaDao();
        List<VentaAnulada> lista = dao.listarVentasAnuladasPorFecha(desde, hasta);
        for (VentaAnulada va : lista) {
            modelo.addRow(new Object[]{va.getIdVenta(), va.getTotal(), va.getMotivo(), va.getUsuario(), va.getFecha()});
        }
    }

    // ============================================
    // EXPORTACIÓN (Se mantiene tu lógica sólida)
    // ============================================

    private void exportarExcel(JTable tabla, String titulo) {
        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(new File(titulo + ".xlsx"));
        if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try (Workbook wb = new XSSFWorkbook()) {
                Sheet hoja = wb.createSheet("Reporte");
                Row filaTitulo = hoja.createRow(0);
                Cell celdaTitulo = filaTitulo.createCell(0);
                celdaTitulo.setCellValue(titulo);
                
                // Encabezados
                Row encabezado = hoja.createRow(2);
                for (int i = 0; i < tabla.getColumnCount(); i++) {
                    encabezado.createCell(i).setCellValue(tabla.getColumnName(i));
                }
                
                // Datos
                for (int f = 0; f < tabla.getRowCount(); f++) {
                    Row fila = hoja.createRow(f + 3);
                    for (int c = 0; c < tabla.getColumnCount(); c++) {
                        Object v = tabla.getValueAt(f, c);
                        fila.createCell(c).setCellValue(v != null ? v.toString() : "");
                    }
                }
                
                try (FileOutputStream out = new FileOutputStream(chooser.getSelectedFile())) {
                    wb.write(out);
                    JOptionPane.showMessageDialog(this, "Excel generado!");
                }
            } catch (Exception e) { e.printStackTrace(); }
        }
    }

    private void exportarPDF(JTable tabla, String titulo) {
        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(new File(titulo + ".pdf"));
        if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                Document doc = new Document(PageSize.A4.rotate());
                PdfWriter.getInstance(doc, new FileOutputStream(chooser.getSelectedFile()));
                doc.open();
                
                com.itextpdf.text.Font f = new com.itextpdf.text.Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
                Paragraph p = new Paragraph(titulo, f);
                p.setAlignment(Element.ALIGN_CENTER);
                p.setSpacingAfter(20);
                doc.add(p);

                PdfPTable pdfTable = new PdfPTable(tabla.getColumnCount());
                pdfTable.setWidthPercentage(100);

                for (int i = 0; i < tabla.getColumnCount(); i++) {
                    PdfPCell cell = new PdfPCell(new Phrase(tabla.getColumnName(i)));
                    cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    pdfTable.addCell(cell);
                }

                for (int fIdx = 0; fIdx < tabla.getRowCount(); fIdx++) {
                    for (int c = 0; c < tabla.getColumnCount(); c++) {
                        Object valor = tabla.getValueAt(fIdx, c);
                        pdfTable.addCell(valor != null ? valor.toString() : "");
                    }
                }

                doc.add(pdfTable);
                doc.close();
                JOptionPane.showMessageDialog(this, "PDF generado!");
            } catch (Exception e) { e.printStackTrace(); }
        }
    }
}