package Servicios;

import Conexion.ConexionMysql;
import java.io.File;
import java.sql.Connection;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class BackupService {

   private final ConexionMysql cn = new ConexionMysql();

    public void generarBackupManual() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Seleccione carpeta para guardar el respaldo");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            String rutaBase = chooser.getSelectedFile().getAbsolutePath();
            ejecutarRespaldo(rutaBase);
        }
    }

    public void ejecutarRespaldo(String rutaCarpeta) {
        // Nombre del archivo con fecha y hora: backup_2024-05-20_15-30.sql
        String fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm"));
        String nombreArchivo = "Respaldo_Canarias_" + fecha + ".sql";
        String rutaCompleta = rutaCarpeta + File.separator + nombreArchivo;

        // Sentencia mágica de H2 para clonar la base de datos a texto SQL
        String sql = "SCRIPT TO '" + rutaCompleta + "'";

        try (Connection con = cn.conectar(); 
             Statement st = con.createStatement()) {
            
            st.execute(sql);
            
            JOptionPane.showMessageDialog(null, 
                "✅ Respaldo creado con éxito en:\n" + nombreArchivo, 
                "Copia de Seguridad", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, 
                "❌ Error al crear el respaldo: " + e.getMessage(), 
                "Error de Seguridad", JOptionPane.ERROR_MESSAGE);
        }
    }
}