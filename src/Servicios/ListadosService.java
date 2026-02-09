package Servicios;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import Modelo.Cliente;
import Modelo.Proveedor;
import Modelo.Usuarios;
import java.awt.HeadlessException;
import java.io.FileNotFoundException;

public class ListadosService {
    
    
        // Método para Listado de Clientes
    public void generarReporteClientes(List<Cliente> lista) throws FileNotFoundException {
        Document documento = new Document(PageSize.A4.rotate()); // Horizontal para que quepa más
        try {
            String ruta = obtenerRutaGuardar("Listado_Clientes.pdf");
            if (ruta == null) return;

            PdfWriter.getInstance(documento, new FileOutputStream(ruta));
            documento.open();

            // Encabezado
            documento.add(crearCabecera("LISTADO DE CLIENTES"));

            // Tabla (Ajusta las columnas según tu modelo Cliente)
            PdfPTable tabla = new PdfPTable(5);
            tabla.setWidthPercentage(100);
            tabla.setWidths(new float[]{10f, 20f, 30f, 20f, 20f});

            // Cabeceras de tabla
            tabla.addCell(celdaCabecera("DNI/RUC"));
            tabla.addCell(celdaCabecera("NOMBRE"));
            tabla.addCell(celdaCabecera("TELÉFONO"));
            tabla.addCell(celdaCabecera("DIRECCIÓN"));
            tabla.addCell(celdaCabecera("ESTADO"));

            for (Cliente c : lista) {
                tabla.addCell(new Phrase(String.valueOf(c.getDni()), FontFactory.getFont(FontFactory.HELVETICA, 10)));
                tabla.addCell(new Phrase(c.getNombre(), FontFactory.getFont(FontFactory.HELVETICA, 10)));
                tabla.addCell(new Phrase(c.getTelefono(), FontFactory.getFont(FontFactory.HELVETICA, 10)));
                tabla.addCell(new Phrase(c.getDomicilio(), FontFactory.getFont(FontFactory.HELVETICA, 10)));
                tabla.addCell(new Phrase("Activo", FontFactory.getFont(FontFactory.HELVETICA, 10))); 
            }

            documento.add(tabla);
            documento.close();
            JOptionPane.showMessageDialog(null, "Reporte de Clientes generado.");
        } catch (DocumentException | HeadlessException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    // --- LISTADO DE USUARIOS ---
    public void generarReporteUsuarios(List<Usuarios> lista) {
        Document documento = new Document(PageSize.A4);
        try {
            String ruta = obtenerRutaGuardar("Listado_Usuarios.pdf");
            if (ruta == null) return;

            PdfWriter.getInstance(documento, new FileOutputStream(ruta));
            documento.open();

            documento.add(crearCabecera("LISTADO DE USUARIOS DEL SISTEMA"));

            PdfPTable tabla = new PdfPTable(4);
            tabla.setWidthPercentage(100);
            tabla.setWidths(new float[]{10f, 35f, 35f, 20f});

            tabla.addCell(celdaCabecera("ID"));
            tabla.addCell(celdaCabecera("NOMBRE"));
            tabla.addCell(celdaCabecera("CORREO"));
            tabla.addCell(celdaCabecera("ROL"));

            for (Usuarios u : lista) {
                tabla.addCell(new Phrase(String.valueOf(u.getId()), FontFactory.getFont(FontFactory.HELVETICA, 10)));
                tabla.addCell(new Phrase(u.getNombre(), FontFactory.getFont(FontFactory.HELVETICA, 10)));
                tabla.addCell(new Phrase(u.getCorreo(), FontFactory.getFont(FontFactory.HELVETICA, 10)));
                tabla.addCell(new Phrase(u.getRol(), FontFactory.getFont(FontFactory.HELVETICA, 10)));
            }

            documento.add(tabla);
            documento.close();
            JOptionPane.showMessageDialog(null, "Listado de Usuarios generado con éxito.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al generar reporte: " + e.getMessage());
        }
    }

    // --- LISTADO DE PROVEEDORES ---
    public void generarReporteProveedores(List<Proveedor> lista) {
        Document documento = new Document(PageSize.A4.rotate()); // Horizontal por la cantidad de datos
        try {
            String ruta = obtenerRutaGuardar("Listado_Proveedores.pdf");
            if (ruta == null) return;

            PdfWriter.getInstance(documento, new FileOutputStream(ruta));
            documento.open();

            documento.add(crearCabecera("DIRECTORIO DE PROVEEDORES"));

            PdfPTable tabla = new PdfPTable(5);
            tabla.setWidthPercentage(100);
            tabla.setWidths(new float[]{15f, 30f, 15f, 25f, 15f});

            tabla.addCell(celdaCabecera("ID"));
            tabla.addCell(celdaCabecera("RAZÓN SOCIAL"));
            tabla.addCell(celdaCabecera("TELÉFONO"));
            tabla.addCell(celdaCabecera("DIRECCIÓN"));
            tabla.addCell(celdaCabecera("WEB/INSTAGRAM"));

            for (Proveedor p : lista) {
                tabla.addCell(new Phrase(String.valueOf(p.getId()), FontFactory.getFont(FontFactory.HELVETICA, 10)));
                tabla.addCell(new Phrase(p.getNombre(), FontFactory.getFont(FontFactory.HELVETICA, 10)));
                tabla.addCell(new Phrase(p.getTelefono(), FontFactory.getFont(FontFactory.HELVETICA, 10)));
                tabla.addCell(new Phrase(p.getDomicilio(), FontFactory.getFont(FontFactory.HELVETICA, 10)));
                tabla.addCell(new Phrase(p.getEmail(), FontFactory.getFont(FontFactory.HELVETICA, 10))); // Ejemplo por si no tienes ciudad
            }

            documento.add(tabla);
            documento.close();
            JOptionPane.showMessageDialog(null, "Listado de Proveedores generado con éxito.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al generar reporte: " + e.getMessage());
        }
    }

    // --- MÉTODOS AUXILIARES DE ESTILO ---
    private Paragraph crearCabecera(String titulo) {
        Font fTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, BaseColor.BLACK);
        Paragraph p = new Paragraph(titulo + "\n\n", fTitulo);
        p.setAlignment(Element.ALIGN_CENTER);
        return p;
    }

    private PdfPCell celdaCabecera(String texto) {
        PdfPCell celda = new PdfPCell(new Phrase(texto, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, BaseColor.WHITE)));
        celda.setBackgroundColor(new BaseColor(44, 62, 80)); // Un azul grisáceo elegante
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        celda.setPadding(6);
        return celda;
    }

    private String obtenerRutaGuardar(String nombreDefecto) {
        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(new File(nombreDefecto));
        if (chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            return chooser.getSelectedFile().getAbsolutePath();
        }
        return null;
    }
}

//
//package Servicios;
//
//import com.itextpdf.text.*;
//import com.itextpdf.text.pdf.*;
//import java.io.File;
//import java.io.FileOutputStream;
//import java.util.List;
//import javax.swing.JFileChooser;
//import javax.swing.JOptionPane;
//import Modelo.Cliente;
//import Modelo.Proveedor;
//import java.awt.HeadlessException;
//import java.io.FileNotFoundException;
////import Modelo.Usuarios;
//
//public class ListadosService {
//
//    // Método para Listado de Clientes
//    public void generarReporteClientes(List<Cliente> lista) {
//        Document documento = new Document(PageSize.A4.rotate()); // Horizontal para que quepa más
//        try {
//            String ruta = obtenerRutaGuardar("Listado_Clientes.pdf");
//            if (ruta == null) return;
//
//            PdfWriter.getInstance(documento, new FileOutputStream(ruta));
//            documento.open();
//
//            // Encabezado
//            documento.add(crearCabecera("LISTADO DE CLIENTES"));
//
//            // Tabla (Ajusta las columnas según tu modelo Cliente)
//            PdfPTable tabla = new PdfPTable(5);
//            tabla.setWidthPercentage(100);
//            tabla.setWidths(new float[]{10f, 20f, 30f, 20f, 20f});
//
//            // Cabeceras de tabla
//            tabla.addCell(celdaCabecera("DNI/RUC"));
//            tabla.addCell(celdaCabecera("NOMBRE"));
//            tabla.addCell(celdaCabecera("TELÉFONO"));
//            tabla.addCell(celdaCabecera("DIRECCIÓN"));
//            tabla.addCell(celdaCabecera("ESTADO"));
//
//            for (Cliente c : lista) {
//                tabla.addCell(new Phrase(String.valueOf(c.getDni()), FontFactory.getFont(FontFactory.HELVETICA, 10)));
//                tabla.addCell(new Phrase(c.getNombre(), FontFactory.getFont(FontFactory.HELVETICA, 10)));
//                tabla.addCell(new Phrase(c.getTelefono(), FontFactory.getFont(FontFactory.HELVETICA, 10)));
//                tabla.addCell(new Phrase(c.getDomicilio(), FontFactory.getFont(FontFactory.HELVETICA, 10)));
//                tabla.addCell(new Phrase("Activo", FontFactory.getFont(FontFactory.HELVETICA, 10))); 
//            }
//
//            documento.add(tabla);
//            documento.close();
//            JOptionPane.showMessageDialog(null, "Reporte de Clientes generado.");
//        } catch (DocumentException | HeadlessException | FileNotFoundException e) {
//            e.printStackTrace();
//        }
//    }
//
//    // Auxiliar para diseño de cabecera de página
//    private Paragraph crearCabecera(String titulo) {
//        Font fTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
//        Paragraph p = new Paragraph(titulo + "\n\n", fTitulo);
//        p.setAlignment(Element.ALIGN_CENTER);
//        return p;
//    }
//
//    // Auxiliar para diseño de celdas de título
//    private PdfPCell celdaCabecera(String texto) {
//        PdfPCell celda = new PdfPCell(new Phrase(texto, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, BaseColor.WHITE)));
//        celda.setBackgroundColor(BaseColor.DARK_GRAY);
//        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
//        celda.setPadding(5);
//        return celda;
//    }
//
//    private String obtenerRutaGuardar(String nombreDefecto) {
//        JFileChooser chooser = new JFileChooser();
//        chooser.setSelectedFile(new File(nombreDefecto));
//        if (chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
//            return chooser.getSelectedFile().getAbsolutePath();
//        }
//        return null;
//    }
//}