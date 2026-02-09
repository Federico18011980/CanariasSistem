package Servicios;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.awt.HeadlessException;
import java.io.FileOutputStream;
import javax.swing.JTable;
import java.io.File;
import javax.swing.JFileChooser;

// Librerías de Barcode4J para iText
import org.krysalis.barcode4j.impl.code128.Code128Bean;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;
import java.io.FileNotFoundException;
import javax.swing.JOptionPane;

public class CodigoBarrasService {

    public void generarReporteEtiquetas(JTable tablaSeleccionados) {
        Document documento = new Document(PageSize.A4, 30, 30, 30, 30);
        try {
            JFileChooser chooser = new JFileChooser();
            chooser.setSelectedFile(new File("Planilla_Etiquetas.pdf"));

            if (chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                PdfWriter writer = PdfWriter.getInstance(documento, new FileOutputStream(chooser.getSelectedFile()));
                documento.open();

                // Usamos 3 columnas para que las etiquetas tengan un buen tamaño
                PdfPTable tablaPdf = new PdfPTable(3);
                tablaPdf.setWidthPercentage(100);

                for (int i = 0; i < tablaSeleccionados.getRowCount(); i++) {
                    String codigo = tablaSeleccionados.getValueAt(i, 1).toString();
                    String nombre = tablaSeleccionados.getValueAt(i, 2).toString();
                    int cantidad = Integer.parseInt(tablaSeleccionados.getValueAt(i, 3).toString());
                    String precio = tablaSeleccionados.getValueAt(i, 4).toString();

                    for (int j = 0; j < cantidad; j++) {
                        tablaPdf.addCell(crearCeldaEtiqueta(writer, codigo, nombre, precio));
                    }
                }
                tablaPdf.completeRow();
                documento.add(tablaPdf);
                documento.close();
                JOptionPane.showMessageDialog(null, "Reporte generado. ¡Listo para imprimir!");
            }
        } catch (DocumentException | HeadlessException | FileNotFoundException | NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }

    private PdfPCell crearCeldaEtiqueta(PdfWriter writer, String codigo, String nombre, String precio) {
    PdfPCell celda = new PdfPCell();
    celda.setPadding(5);
    celda.setFixedHeight(130f); // Un poco más de aire
    celda.setHorizontalAlignment(Element.ALIGN_CENTER);
    
    // Creamos una tabla interna para organizar mejor los elementos
    PdfPTable interna = new PdfPTable(1);
    interna.setWidthPercentage(100);

    try {
        // 1. NOMBRE
        PdfPCell cNombre = new PdfPCell(new Phrase(nombre.toUpperCase(), FontFactory.getFont(FontFactory.HELVETICA_BOLD, 8)));
        cNombre.setBorder(Rectangle.NO_BORDER);
        cNombre.setHorizontalAlignment(Element.ALIGN_CENTER);
        interna.addCell(cNombre);

        // 2. CÓDIGO DE BARRAS
        Barcode128 code128 = new Barcode128();
        code128.setCode(codigo);
        code128.setBarHeight(35f);
        code128.setX(1f);
        code128.setFont(null); // Sin texto nativo debajo

        Image imgBarra = code128.createImageWithBarcode(writer.getDirectContent(), null, null);
        imgBarra.setAlignment(Element.ALIGN_CENTER);
        
        PdfPCell cBarra = new PdfPCell(imgBarra);
        cBarra.setBorder(Rectangle.NO_BORDER);
        cBarra.setPaddingTop(5f);
        cBarra.setHorizontalAlignment(Element.ALIGN_CENTER);
        interna.addCell(cBarra);

        // 3. TEXTO DEL CÓDIGO (Leíble)
        PdfPCell cTextoCod = new PdfPCell(new Phrase(codigo, FontFactory.getFont(FontFactory.HELVETICA, 7)));
        cTextoCod.setBorder(Rectangle.NO_BORDER);
        cTextoCod.setHorizontalAlignment(Element.ALIGN_CENTER);
        interna.addCell(cTextoCod);

        // 4. PRECIO
        PdfPCell cPrecio = new PdfPCell(new Phrase("$ " + precio, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12)));
        cPrecio.setBorder(Rectangle.NO_BORDER);
        cPrecio.setHorizontalAlignment(Element.ALIGN_CENTER);
        cPrecio.setPaddingTop(8f);
        interna.addCell(cPrecio);

        celda.addElement(interna);

    } catch (Exception e) {
        System.err.println("Error en celda: " + e.getMessage());
    }

    return celda;
}
}