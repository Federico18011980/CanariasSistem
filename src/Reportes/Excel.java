package Reportes;

import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import Conexion.ConexionMysql;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Excel {

    private static final Logger LOGGER = Logger.getLogger(Excel.class.getName());

    public static void reporte() {
        
        // Usamos try-with-resources para asegurar el cierre automático del Workbook
        try (Workbook book = new XSSFWorkbook()) {
            
            Sheet sheet = book.createSheet("Productos");

            // --- 1. CONFIGURACIÓN DE LOGO Y ESTILOS ---
            
            // Estilo para el Título
            CellStyle tituloEstilo = book.createCellStyle();
            tituloEstilo.setAlignment(HorizontalAlignment.CENTER);
            tituloEstilo.setVerticalAlignment(VerticalAlignment.CENTER);
            Font fuenteTitulo = book.createFont();
            fuenteTitulo.setFontName("Arial");
            fuenteTitulo.setBold(true);
            fuenteTitulo.setFontHeightInPoints((short) 14);
            tituloEstilo.setFont(fuenteTitulo);

            // Estilo para la Cabecera
            CellStyle headerStyle = book.createCellStyle();
            headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            
            // Bordes para la cabecera
            headerStyle.setBorderBottom(BorderStyle.THIN);
            headerStyle.setBorderLeft(BorderStyle.THIN);
            headerStyle.setBorderRight(BorderStyle.THIN);
            headerStyle.setBorderTop(BorderStyle.THIN);
            
            Font fontHeader = book.createFont();
            fontHeader.setFontName("Arial");
            fontHeader.setBold(true);
            fontHeader.setColor(IndexedColors.WHITE.getIndex());
            fontHeader.setFontHeightInPoints((short) 12);
            headerStyle.setFont(fontHeader);
            
            // Estilo para los datos con bordes
            CellStyle datosEstilo = book.createCellStyle();
            datosEstilo.setBorderBottom(BorderStyle.THIN);
            datosEstilo.setBorderLeft(BorderStyle.THIN);
            datosEstilo.setBorderRight(BorderStyle.THIN);
            datosEstilo.setBorderTop(BorderStyle.THIN);
            
            // Estilo específico para valores numéricos (Moneda, si es necesario)
            // Esto es crucial para que Excel reconozca los valores como números
            CellStyle numberStyle = book.createCellStyle();
            numberStyle.cloneStyleFrom(datosEstilo); // Copia los bordes
            
            // Formato de moneda para que se vea como tal en Excel
            CreationHelper createHelper = book.getCreationHelper();
            numberStyle.setDataFormat(createHelper.createDataFormat().getFormat("$#,##0.00"));

            // --- 2. LOGO ---
            try (InputStream is = new FileInputStream("src/img/LogoByG100.png")) {
                byte[] bytes = IOUtils.toByteArray(is);
                int imgIndex = book.addPicture(bytes, Workbook.PICTURE_TYPE_PNG);

                Drawing draw = sheet.createDrawingPatriarch();
                ClientAnchor anchor = book.getCreationHelper().createClientAnchor();
                
                // Posicionamiento del logo: A1
                anchor.setCol1(0); 
                anchor.setRow1(0); 
                Picture pict = draw.createPicture(anchor, imgIndex);
                
                // Ajustar tamaño del logo: 2 columnas de ancho por 2 filas de alto para un mejor layout
                pict.resize(1.0, 3.0);
                
            } catch (IOException ex) {
                // Si el logo no se encuentra, se registra el error y el programa continúa.
                LOGGER.log(Level.WARNING, "No se pudo cargar el logo. Continuando sin imagen.", ex);
            }


            // --- 3. TÍTULO DEL REPORTE ---
            
            Row filaTitulo = sheet.createRow(1); // Fila 2 (índice 1)
            Cell celdaTitulo = filaTitulo.createCell(1); // Columna B (índice 1)
            celdaTitulo.setCellStyle(tituloEstilo);
            celdaTitulo.setCellValue("Reporte de Productos");
            
            // Fusión de celdas: Fila 2 a Fila 3, Columna E a Columna H
            sheet.addMergedRegion(new CellRangeAddress(1, 2, 1, 4));

            // --- 4. CABECERA DE LA TABLA ---
            
            int filaCabecera = 4; // Fila 5
            Row filaEncabezados = sheet.createRow(filaCabecera);
            
            String[] cabecera = new String[]{"Código", "Nombre", "Precio U$D", "Precio AR$", "Stock"};

            for (int i = 0; i < cabecera.length; i++) {
                Cell celdaEnzabezado = filaEncabezados.createCell(i);
                celdaEnzabezado.setCellStyle(headerStyle);
                celdaEnzabezado.setCellValue(cabecera[i]);
            }
            
            // --- 5. LECTURA DE DATOS DE LA BASE DE DATOS ---
            
            // try-with-resources para asegurar el cierre de Connection, PreparedStatement y ResultSet
            
            
            
            try 
//                (   ConexionMysql con = new ConexionMysql();
//                    Connection conn = new ConexionMysql.conectar();
//                 PreparedStatement ps = conn.prepareStatement(
//                    "SELECT codigo, descripcion, precio, precioxdolar, stock FROM productos ORDER BY codigo");
//                 ResultSet rs = ps.executeQuery()) 
            {
            ConexionMysql con = new ConexionMysql();
            PreparedStatement ps;
            ResultSet rs;
            Connection conn = con.conectar();
            ps = conn.prepareStatement("SELECT codigo, descripcion, precio, precioxdolar, stock FROM productos ORDER BY codigo");
            rs = ps.executeQuery();    
            int numFilaDatos = filaCabecera + 1; // Empieza en la Fila 6

                while (rs.next()) {
                    Row filaDatos = sheet.createRow(numFilaDatos);

                    // Columna 0: Código (STRING)
                    Cell celdaCodigo = filaDatos.createCell(0);
                    celdaCodigo.setCellStyle(datosEstilo);
                    celdaCodigo.setCellValue(rs.getString("codigo"));

                    // Columna 1: Nombre/Descripción (STRING)
                    Cell celdaDescripcion = filaDatos.createCell(1);
                    celdaDescripcion.setCellStyle(datosEstilo);
                    celdaDescripcion.setCellValue(rs.getString("descripcion"));

                    // Columna 2: Precio U$D (NUMÉRICO)
                    // Uso de setCellValue(double) y numberStyle para el formato de moneda
                    Cell celdaPrecioUSD = filaDatos.createCell(2);
                    celdaPrecioUSD.setCellStyle(numberStyle);
                    celdaPrecioUSD.setCellValue(rs.getDouble("precio"));

                    // Columna 3: Precio AR$ (NUMÉRICO)
                    Cell celdaPrecioAR = filaDatos.createCell(3);
                    celdaPrecioAR.setCellStyle(numberStyle);
                    celdaPrecioAR.setCellValue(rs.getDouble("precioxdolar"));

                    // Columna 4: Stock/Existencia (NUMÉRICO ENTERO)
                    Cell celdaStock = filaDatos.createCell(4);
                    celdaStock.setCellStyle(datosEstilo);
                    // Usamos getInt o getLong, pero getDouble funciona bien para POI si no hay un estilo específico de entero
                    celdaStock.setCellValue(rs.getInt("stock")); 

                    numFilaDatos++;
                }

            } catch (SQLException e) {
                // Manejo de errores SQL
                LOGGER.log(Level.SEVERE, "Error de base de datos al generar reporte", e);
                JOptionPane.showMessageDialog(null, "Error al acceder a la base de datos: " + e.getMessage(), "Error SQL", JOptionPane.ERROR_MESSAGE);
                return; // Salir del método si hay un error de DB
            }

            // --- 6. AUTOAJUSTE DE COLUMNAS Y CIERRE ---
            
            // Autoajuste de las 5 columnas de datos (0 a 4)
            for (int i = 0; i < cabecera.length; i++) {
                sheet.autoSizeColumn(i);
            }
            
            // Ajuste de zoom
            sheet.setZoom(150);
            
            String fileName = "productos";
            String home = System.getProperty("user.home");
            File file = new File(home + "/Downloads/" + fileName + ".xlsx");

            // Usamos try-with-resources para asegurar el cierre de FileOutputStream
            try (FileOutputStream fileOut = new FileOutputStream(file)) {
                book.write(fileOut);
            } // fileOut y book se cierran automáticamente aquí

            // --- 7. ABRIR Y NOTIFICAR AL USUARIO ---
            
            Desktop.getDesktop().open(file);
            JOptionPane.showMessageDialog(null, "Reporte Generado exitosamente en: " + file.getAbsolutePath(), "Éxito", JOptionPane.INFORMATION_MESSAGE);

        } catch (IOException ex) {
            // Manejo de errores de Archivo/IO, incluyendo FileNotFound
            LOGGER.log(Level.SEVERE, "Error al crear o abrir el archivo Excel", ex);
            JOptionPane.showMessageDialog(null, "Error de Archivo/IO: " + ex.getMessage(), "Error de Sistema", JOptionPane.ERROR_MESSAGE);
        }
    }
}

//package Reportes;
//
//import java.awt.Desktop;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import javax.swing.JOptionPane;
////import Modelo.Conexion;
//import Conexion.ConexionMysql;
//import org.apache.poi.ss.usermodel.BorderStyle;
//import org.apache.poi.ss.usermodel.Cell;
//import org.apache.poi.ss.usermodel.CellStyle;
//import org.apache.poi.ss.usermodel.ClientAnchor;
//import org.apache.poi.ss.usermodel.CreationHelper;
//import org.apache.poi.ss.usermodel.Drawing;
//import org.apache.poi.ss.usermodel.FillPatternType;
//import org.apache.poi.ss.usermodel.Font;
//import org.apache.poi.ss.usermodel.HorizontalAlignment;
//import org.apache.poi.ss.usermodel.IndexedColors;
//import org.apache.poi.ss.usermodel.Picture;
//import org.apache.poi.ss.usermodel.Row;
//import org.apache.poi.ss.usermodel.Sheet;
//import org.apache.poi.ss.usermodel.VerticalAlignment;
//import org.apache.poi.ss.usermodel.Workbook;
//import org.apache.poi.ss.util.CellRangeAddress;
//import org.apache.poi.util.IOUtils;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
// 
//public class Excel {
//    public static void reporte() {
// 
//        Workbook book = new XSSFWorkbook();
//        Sheet sheet = book.createSheet("Productos");
// 
//        try {
//            InputStream is = new FileInputStream("src/img/Logo ByG gemini 200x200.png");
//            byte[] bytes = IOUtils.toByteArray(is);
//            int imgIndex = book.addPicture(bytes, Workbook.PICTURE_TYPE_PNG);
//            is.close();
// 
//            CreationHelper help = book.getCreationHelper();
//            Drawing draw = sheet.createDrawingPatriarch();
// 
//            ClientAnchor anchor = help.createClientAnchor();
//            anchor.setCol1(0);
//            anchor.setRow1(1);
//            Picture pict = draw.createPicture(anchor, imgIndex);
//            pict.resize(1, 3);
// 
//            CellStyle tituloEstilo = book.createCellStyle();
//            tituloEstilo.setAlignment(HorizontalAlignment.CENTER);
//            tituloEstilo.setVerticalAlignment(VerticalAlignment.CENTER);
//            Font fuenteTitulo = book.createFont();
//            fuenteTitulo.setFontName("Arial");
//            fuenteTitulo.setBold(true);
//            fuenteTitulo.setFontHeightInPoints((short) 14);
//            tituloEstilo.setFont(fuenteTitulo);
// 
//            Row filaTitulo = sheet.createRow(1);
//            Cell celdaTitulo = filaTitulo.createCell(1);
//            celdaTitulo.setCellStyle(tituloEstilo);
//            celdaTitulo.setCellValue("Reporte de Productos");
// 
//            sheet.addMergedRegion(new CellRangeAddress(1, 2, 1, 3));
// 
//            String[] cabecera = new String[]{"Código", "Nombre", "Precio U$D","Precio AR$", "Existencia"};
// 
//            CellStyle headerStyle = book.createCellStyle();
//            headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
//            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
//            headerStyle.setBorderBottom(BorderStyle.THIN);
//            headerStyle.setBorderLeft(BorderStyle.THIN);
//            headerStyle.setBorderRight(BorderStyle.THIN);
//            headerStyle.setBorderBottom(BorderStyle.THIN);
// 
//            Font font = book.createFont();
//            font.setFontName("Arial");
//            font.setBold(true);
//            font.setColor(IndexedColors.WHITE.getIndex());
//            font.setFontHeightInPoints((short) 12);
//            headerStyle.setFont(font);
// 
//            Row filaEncabezados = sheet.createRow(4);
// 
//            for (int i = 0; i < cabecera.length; i++) {
//                Cell celdaEnzabezado = filaEncabezados.createCell(i);
//                celdaEnzabezado.setCellStyle(headerStyle);
//                celdaEnzabezado.setCellValue(cabecera[i]);
//            }
// 
//            ConexionMysql con = new ConexionMysql();
//            PreparedStatement ps;
//            ResultSet rs;
//            Connection conn = con.conectar();
// 
//            int numFilaDatos = 6;
// 
//            CellStyle datosEstilo = book.createCellStyle();
//            datosEstilo.setBorderBottom(BorderStyle.THIN);
//            datosEstilo.setBorderLeft(BorderStyle.THIN);
//            datosEstilo.setBorderRight(BorderStyle.THIN);
//            datosEstilo.setBorderBottom(BorderStyle.THIN);
// 
//            ps = conn.prepareStatement("SELECT codigo, descripcion, precio, precioxdolar, stock FROM productos");
//            rs = ps.executeQuery();
// 
//            int numCol = rs.getMetaData().getColumnCount();
// 
//            while (rs.next()) {
//                Row filaDatos = sheet.createRow(numFilaDatos);
// 
//                for (int a = 0; a < numCol; a++) {
// 
//                    Cell CeldaDatos = filaDatos.createCell(a);
//                    CeldaDatos.setCellStyle(datosEstilo);
//                    CeldaDatos.setCellValue(rs.getString(a + 1));
//                }
// 
// 
//                numFilaDatos++;
//            }
//            sheet.autoSizeColumn(0);
//            sheet.autoSizeColumn(1);
//            sheet.autoSizeColumn(2);
//            sheet.autoSizeColumn(3);
//            sheet.autoSizeColumn(4);
//            sheet.autoSizeColumn(5);
//            
//            sheet.setZoom(150);
//            String fileName = "productos";
//            String home = System.getProperty("user.home");
//            File file = new File(home + "/Downloads/" + fileName + ".xlsx");
//            FileOutputStream fileOut = new FileOutputStream(file);
//            book.write(fileOut);
//            fileOut.close();
//            Desktop.getDesktop().open(file);
//            JOptionPane.showMessageDialog(null, "Reporte Generado");
// 
//        } catch (FileNotFoundException ex) {
//            Logger.getLogger(Excel.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (IOException | SQLException ex) {
//            Logger.getLogger(Excel.class.getName()).log(Level.SEVERE, null, ex);
//        }
// 
//    }
//}