package Servicios;

import Vista.Sistema;
import java.awt.Desktop;
import java.awt.HeadlessException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import javax.swing.JOptionPane;

public class ActualizadorService {

    // Versión actual del programa (la que tienes tú ahora)
    private final String VERSION_ACTUAL = "1.1";
    
    // 1. Creamos una variable para guardar la referencia
    private Vista.Sistema vistaPrincipal;
    
    // URL del archivo version.txt (Reemplaza con tu link Raw de GitHub)
    private final String URL_VERSION = "https://gist.githubusercontent.com/federodriguez18011980-lab/a095d16849223f94a9417fcd6b1a86c5/raw/eda520f7168fa07c0fadc6f9819d646d6b94e5cb/version.txt";

   // 2. Modificamos el constructor para recibirla
    public ActualizadorService(Vista.Sistema vistaPrincipal) {
        this.vistaPrincipal = vistaPrincipal;
    }
    
    
    
    public void verificarVersion() {
    new Thread(() -> {
        try {
            URL url = new URL(URL_VERSION);
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            String versionRemota = reader.readLine().trim();
            reader.close();

            if (!VERSION_ACTUAL.equals(versionRemota)) {
                // Personalizamos el panel para que tenga botones de Sí/No
                int respuesta = JOptionPane.showConfirmDialog(null, 
                    "¡Nueva versión disponible (" + versionRemota + ")!\n" +
                    "¿Desea descargar la actualización ahora?", 
                    "Actualización del Sistema", 
                    JOptionPane.YES_NO_OPTION);

                if (respuesta == JOptionPane.YES_OPTION) {
                    // URL donde subiste el nuevo instalador (Ej: Dropbox, Drive, o tu Servidor)
                    String urlDescarga = "https://github.com/Federico18011980/CanariasSistem/releases/download/V1.1/CanariasSystem_Setup_v1.1.exe";
                    Desktop.getDesktop().browse(new java.net.URI(urlDescarga));
                    System.exit(0); // Cerramos el sistema para que puedan instalar
                    
                    vistaPrincipal.salirDelSistema();
                }
                
             
                
            }
        } catch (HeadlessException | IOException | URISyntaxException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }).start();
}
}