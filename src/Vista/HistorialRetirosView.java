package Vista;

import Modelo.CajaMovimiento;
import Modelo.CajaMovimientoDao;
import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.*;
import com.toedter.calendar.JDateChooser;
import java.awt.*;
import java.awt.Color;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class HistorialRetirosView extends JFrame {

    private JTable tabla;
    private DefaultTableModel modelo;
    private CajaMovimientoDao movDao = new CajaMovimientoDao();
    private JDateChooser dcDesde, dcHasta;
    private JComboBox<String> cbTipo;
    private JButton btnBuscar, btnPdf;

    // Colores corporativos
    private Color azulOscuro = new Color(33, 47, 61);
    private Color grisFondo = new Color(242, 244, 244);

    public HistorialRetirosView() {
        setTitle("Historial de Movimientos de Caja");
        setSize(900, 550);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        // --- PANEL SUPERIOR: FILTROS ---
        JPanel pnlFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
        pnlFiltros.setBackground(azulOscuro);

        dcDesde = new JDateChooser();
        dcDesde.setPreferredSize(new Dimension(130, 28));
        dcDesde.setDate(java.sql.Date.valueOf(LocalDate.now()));

        dcHasta = new JDateChooser();
        dcHasta.setPreferredSize(new Dimension(130, 28));
        dcHasta.setDate(java.sql.Date.valueOf(LocalDate.now()));

        cbTipo = new JComboBox<>(new String[]{"TODOS", "EFECTIVO", "TRANSFERENCIA"});
        cbTipo.setPreferredSize(new Dimension(140, 28));

        btnBuscar = new JButton("Filtrar");
        btnBuscar.setBackground(new Color(52, 152, 219));
        btnBuscar.setForeground(Color.WHITE);

        btnPdf = new JButton("Exportar PDF");
        btnPdf.setBackground(new Color(231, 76, 60));
        btnPdf.setForeground(Color.WHITE);

        pnlFiltros.add(crearLabelBlanco("Desde:"));
        pnlFiltros.add(dcDesde);
        pnlFiltros.add(crearLabelBlanco("Hasta:"));
        pnlFiltros.add(dcHasta);
        pnlFiltros.add(crearLabelBlanco("Tipo:"));
        pnlFiltros.add(cbTipo);
        pnlFiltros.add(btnBuscar);
        pnlFiltros.add(btnPdf);

        add(pnlFiltros, BorderLayout.NORTH);

        // --- PANEL CENTRAL: TABLA ---
        modelo = new DefaultTableModel(
                new String[]{"Fecha", "Hora", "Tipo", "Monto", "Usuario", "Descripción"}, 0
        ) { @Override public boolean isCellEditable(int r, int c) { return false; } };

        tabla = new JTable(modelo);
        configurarTabla();
        
        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(new EmptyBorder(10, 10, 10, 10));
        scroll.setBackground(Color.WHITE);
        add(scroll, BorderLayout.CENTER);

        // Eventos
        btnBuscar.addActionListener(e -> cargarRetiros());
        btnPdf.addActionListener(e -> exportarRetirosPDF());
    }

    private JLabel crearLabelBlanco(String t) {
        JLabel l = new JLabel(t);
        l.setForeground(Color.WHITE);
        l.setFont(new java.awt.Font("SansSerif", java.awt.Font.BOLD, 12));
        return l;
    }

    private void configurarTabla() {
        tabla.setRowHeight(25);
        tabla.getTableHeader().setFont(new java.awt.Font("SansSerif", java.awt.Font.BOLD, 12));
        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(JLabel.CENTER);
        
        // Formato para columna Monto
        tabla.getColumnModel().getColumn(3).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public void setValue(Object value) {
                setForeground(new Color(192, 57, 43)); // Rojo para retiros
                setHorizontalAlignment(JLabel.RIGHT);
                super.setValue(value != null ? "$ " + value : "");
            }
        });
    }

    private void cargarRetiros() {
        if (dcDesde.getDate() == null || dcHasta.getDate() == null) return;

        LocalDate desde = dcDesde.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate hasta = dcHasta.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        String tipo = cbTipo.getSelectedItem().toString();

        List<CajaMovimiento> lista = movDao.listarRetiros(desde, hasta, tipo, null);
        modelo.setRowCount(0);

        for (CajaMovimiento m : lista) {
            modelo.addRow(new Object[]{
                m.getFecha(), m.getHora(), m.getTipo(), m.getMonto(), m.getUsuario(), m.getDescripcion()
            });
        }
    }

    private void exportarRetirosPDF() {
        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(new java.io.File("Reporte_Retiros.pdf"));
        if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;

        String ruta = chooser.getSelectedFile().getAbsolutePath();
        if (!ruta.toLowerCase().endsWith(".pdf")) ruta += ".pdf";

        try {
            Document doc = new Document(PageSize.A4);
            PdfWriter.getInstance(doc, new FileOutputStream(ruta));
            doc.open();

            // Estilos de fuente
            Font fontTitulo = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
            Font fontSub = new Font(Font.FontFamily.HELVETICA, 12, Font.ITALIC);

            Paragraph p = new Paragraph("HISTORIAL DE MOVIMIENTOS DE CAJA", fontTitulo);
            p.setAlignment(Element.ALIGN_CENTER);
            doc.add(p);
            
            doc.add(new Paragraph("Rango: " + dcDesde.getDate() + " al " + dcHasta.getDate(), fontSub));
            doc.add(new Paragraph("Filtro Tipo: " + cbTipo.getSelectedItem(), fontSub));
            doc.add(new Paragraph(" "));

            // Tabla PDF (6 columnas ahora)
            PdfPTable table = new PdfPTable(6);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{2, 2, 2, 2, 2, 4}); // La descripción es más ancha

            String[] headers = {"Fecha", "Hora", "Tipo", "Monto", "Usuario", "Descripción"};
            for (String h : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(h, new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD)));
                cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
            }

            BigDecimal total = BigDecimal.ZERO;
            for (int i = 0; i < tabla.getRowCount(); i++) {
                for (int j = 0; j < 6; j++) {
                    table.addCell(new Phrase(tabla.getValueAt(i, j).toString(), new Font(Font.FontFamily.HELVETICA, 9)));
                }
                total = total.add(new BigDecimal(tabla.getValueAt(i, 3).toString()));
            }

            doc.add(table);
            Paragraph totalP = new Paragraph("\nTOTAL RETIRADO EN EL PERIODO: $" + total, fontTitulo);
            totalP.setAlignment(Element.ALIGN_RIGHT);
            doc.add(totalP);

            doc.close();
            JOptionPane.showMessageDialog(this, "Reporte generado en: " + ruta);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }
}