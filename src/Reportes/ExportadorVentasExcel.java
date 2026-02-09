package reportes;

import Modelo.Venta;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class ExportadorVentasExcel {

    public void exportar(List<Venta> ventas, String archivo) {
        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("Ventas");
        Row row = sheet.createRow(0);

        // Titulos de las columnas
        row.createCell(0).setCellValue("ID");
        row.createCell(1).setCellValue("Cliente");
        row.createCell(2).setCellValue("Vendedor");
        row.createCell(3).setCellValue("Total");
        row.createCell(4).setCellValue("Pago");
        row.createCell(5).setCellValue("Estado");
        row.createCell(6).setCellValue("Fecha");

        // Rellenar los datos
        int rowNum = 1;
        for (Venta v : ventas) {
            row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(v.getId());
            row.createCell(1).setCellValue(v.getCliente());
            row.createCell(2).setCellValue(v.getVendedor());
            row.createCell(3).setCellValue(v.getTotal().toString());
            row.createCell(4).setCellValue(v.getPago());
            row.createCell(5).setCellValue(v.getEstado());
            row.createCell(6).setCellValue(v.getFecha());
        }

        try (FileOutputStream fileOut = new FileOutputStream(archivo)) {
            wb.write(fileOut);
            wb.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
