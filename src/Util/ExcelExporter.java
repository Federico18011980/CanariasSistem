package Util;

import Modelo.Productos;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

public class ExcelExporter {

    public static boolean exportarProductos(List<Productos> lista, File archivo) {

        try (Workbook workbook = new XSSFWorkbook()) {

            Sheet sheet = workbook.createSheet("Productos");

            // CABECERA
            Row header = sheet.createRow(0);
            String[] columnas = {
                "ID", "Código", "Descripción",
                "Precio", "Stock",
                "Proveedor", "Categoría", "Talle", "Color"
            };

            for (int i = 0; i < columnas.length; i++) {
                header.createCell(i).setCellValue(columnas[i]);
            }

            // DATOS
            int fila = 1;
            for (Productos p : lista) {
                Row row = sheet.createRow(fila++);

                row.createCell(0).setCellValue(p.getId());
                row.createCell(1).setCellValue(p.getCodigo());
                row.createCell(2).setCellValue(p.getDescripcion());
                row.createCell(3).setCellValue(p.getPrecio().doubleValue());
                row.createCell(4).setCellValue(p.getStock().doubleValue());
                row.createCell(5).setCellValue(p.getProveedor());
                row.createCell(6).setCellValue(p.getCategoriaNombre());
                row.createCell(7).setCellValue(p.getTalleNombre());
                row.createCell(8).setCellValue(p.getColorNombre());
            }

            // Ajustar ancho
            for (int i = 0; i < columnas.length; i++) {
                sheet.autoSizeColumn(i);
            }

            try (FileOutputStream out = new FileOutputStream(archivo)) {
                workbook.write(out);
            }

            return true;

        } catch (Exception e) {
            System.out.println("Error exportando Excel: " + e.getMessage());
            return false;
        }
    }
}
