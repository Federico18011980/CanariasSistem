package reportes;

import Modelo.Venta;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;

public class ExportadorVentasPDF {

    public void exportar(List<Venta> ventas, String archivo) {
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream(archivo));
            document.open();

            // Titulo
            Font fontTitulo = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
            Phrase titulo = new Phrase("Reporte de Ventas", fontTitulo);
            document.add(titulo);

            // Tabla de datos
            PdfPTable table = new PdfPTable(7);
            table.addCell("ID");
            table.addCell("Cliente");
            table.addCell("Vendedor");
            table.addCell("Total");
            table.addCell("Pago");
            table.addCell("Estado");
            table.addCell("Fecha");

            // Rellenar tabla
            for (Venta v : ventas) {
                table.addCell(String.valueOf(v.getId()));
                table.addCell(v.getCliente());
                table.addCell(v.getVendedor());
                table.addCell(v.getTotal().toString());
                table.addCell(v.getPago());
                table.addCell(v.getEstado());
                table.addCell(v.getFecha());
            }

            document.add(table);
            document.close();
        } catch (DocumentException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
