/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Util;



import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.io.FileOutputStream;
import java.io.File;
import javax.swing.JTable;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class ReporteCajaPDF {

    public void generarReporteHistorico(JTable tabla, String fechaInicio, String fechaFin) {
        Document documento = new Document(PageSize.A4);
        try {
            JFileChooser chooser = new JFileChooser();
            chooser.setSelectedFile(new File("Reporte_Cierres_" + fechaInicio + ".pdf"));
            
            if (chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                PdfWriter.getInstance(documento, new FileOutputStream(chooser.getSelectedFile()));
                documento.open();

                // 1. Título y Encabezado
                Font fuenteTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.DARK_GRAY);
                Paragraph titulo = new Paragraph("HISTÓRICO DE CIERRES DE CAJA", fuenteTitulo);
                titulo.setAlignment(Element.ALIGN_CENTER);
                documento.add(titulo);

                documento.add(new Paragraph("Período: " + fechaInicio + " hasta " + fechaFin));
                documento.add(Chunk.NEWLINE);

                // 2. Crear la Tabla en el PDF
                PdfPTable tablaPdf = new PdfPTable(tabla.getColumnCount());
                tablaPdf.setWidthPercentage(100);

                // Configurar encabezados de columna
                Font fuenteEncabezado = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, BaseColor.WHITE);
                for (int i = 0; i < tabla.getColumnCount(); i++) {
                    PdfPCell celda = new PdfPCell(new Phrase(tabla.getColumnName(i), fuenteEncabezado));
                    celda.setBackgroundColor(BaseColor.GRAY);
                    celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                    tablaPdf.addCell(celda);
                }

                // 3. Agregar los datos de la JTable al PDF
                for (int filas = 0; filas < tabla.getRowCount(); filas++) {
                    for (int columnas = 0; columnas < tabla.getColumnCount(); columnas++) {
//                        String valor = tabla.getValueAt(filas, columnas).toString();
//                        tablaPdf.addCell(new Phrase(valor, FontFactory.getFont(FontFactory.HELVETICA, 9)));
                        // Dentro del bucle de columnas en ReporteCajaPDF.java
                        String valor = tabla.getValueAt(filas, columnas).toString();
                        PdfPCell celdaDato = new PdfPCell(new Phrase(valor, FontFactory.getFont(FontFactory.HELVETICA, 9)));

// Si es la columna de diferencia (supongamos que es la índice 4) y es negativa
                        if (columnas == 4 && valor.startsWith("-")) {
                            celdaDato.setPhrase(new Phrase(valor, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9, BaseColor.RED)));
                        }
                        tablaPdf.addCell(celdaDato);

                    }
                }

                documento.add(tablaPdf);
                documento.close();
                JOptionPane.showMessageDialog(null, "PDF generado con éxito.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al generar PDF: " + e.getMessage());
        }
    }
}