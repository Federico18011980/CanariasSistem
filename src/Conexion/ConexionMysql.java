
package Conexion;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionMysql {

    private Connection cn;

    public Connection conectar() {
        try {
            Class.forName("org.h2.Driver");

            // Carpeta del usuario
            String userHome = System.getProperty("user.home");

            // Ruta donde estará SIEMPRE la base
            String dbDir = userHome + File.separator + "CanariasSystem" + File.separator + "data";

            // Crear carpetas si no existen
            new File(dbDir).mkdirs();

            // Ruta completa al archivo de BD
            String dbPath = dbDir + File.separator + "sistemadeventas";

            // Conexión H2 definitiva
            String url = "jdbc:h2:file:" + dbPath + ";AUTO_SERVER=TRUE";

            cn = DriverManager.getConnection(url, "root", "");

        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("ERROR DE CONEXION: " + e.getMessage());
        }

        return cn;
    }
}
