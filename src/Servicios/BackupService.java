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

    private ConexionMysql cn = new ConexionMysql();

    // Método para cuando el usuario elige dónde guardar (Manual)
    public void generarBackupManual() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Seleccione carpeta para guardar el respaldo");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            String rutaBase = chooser.getSelectedFile().getAbsolutePath();
            // Ejecutamos y avisamos
            if (ejecutarRespaldo(rutaBase)) {
                JOptionPane.showMessageDialog(null, "✅ Respaldo manual creado con éxito.");
            }
        }
    }

    // EL MOTOR: Ahora devuelve un boolean para saber si terminó bien
    public boolean ejecutarRespaldo(String rutaCarpeta) {
        String fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
        String nombreArchivo = "Respaldo_Canarias_" + fecha + ".sql";
        
        // IMPORTANTE: H2 necesita rutas con barras hacia adelante (/) incluso en Windows
        String rutaCompleta = new File(rutaCarpeta, nombreArchivo).getAbsolutePath().replace("\\", "/");

        // Sentencia SCRIPT de H2
        String sql = "SCRIPT TO '" + rutaCompleta + "'";

        // Usamos una nueva conexión solo para el backup para no interferir con la principal
        try (Connection con = cn.conectar(); 
             Statement st = con.createStatement()) {
            
            st.execute(sql);
            System.out.println("Backup finalizado en: " + rutaCompleta);
            return true;

        } catch (Exception e) {
            System.err.println("Error crítico en backup: " + e.getMessage());
            return false;
        }
    }
}