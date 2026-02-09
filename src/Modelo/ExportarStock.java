package Modelo;

import java.sql.*;
import java.io.FileWriter;
import java.io.IOException;
import org.json.JSONArray;
import org.json.JSONObject;
import javax.swing.JOptionPane;
//import Vista.Sistema;
import Modelo.Config;
import Modelo.ProductosDao;

public class ExportarStock {
    
    
    
    
    
    public static void exportar() {
        Config C = new Config();
        ProductosDao PD = new ProductosDao();
        
        String url = "jdbc:h2:.\\Base\\sistemadeventas"; // Cambia esto a la ubicación real de tu base de datos H2
        String user = "root";
        String password = "";
        C = PD.BuscarDatos();
        
        String path = C.getSalida();
        
        //JOptionPane.showMessageDialog(null, path);
        String filePath = path;//"C:\\Users\\feder\\Mi unidad\\Json\\stock.json";//"G:\\MI unidad\\Json\\stock.json"; //"C:\\Users\\feder\\Mi unidad\\Json\\stock.json"

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            String query = "SELECT codigo, descripcion, proveedor, stock, precio, fecha FROM productos";
            try (PreparedStatement stmt = conn.prepareStatement(query);
                 ResultSet rs = stmt.executeQuery()) {

                JSONArray productosArray = new JSONArray();

                while (rs.next()) {
                    JSONObject producto = new JSONObject();
                    producto.put("codigo", rs.getString("codigo"));
                    producto.put("descripcion", rs.getString("descripcion"));
                    producto.put("proveedor", rs.getString("proveedor"));
                    producto.put("stock", rs.getInt("stock"));
                    producto.put("precio", rs.getBigDecimal("precio"));
                    producto.put("fecha", rs.getTimestamp("fecha").toString());

                    productosArray.put(producto);
                }

                JSONObject stockData = new JSONObject();
                stockData.put("store", "Tienda1"); // Cambia esto según corresponda
                stockData.put("lastUpdate", new Timestamp(System.currentTimeMillis()).toString());
                stockData.put("items", productosArray);

                try (FileWriter file = new FileWriter(filePath)) {
                    file.write(stockData.toString(4)); // Formato JSON con indentación
                }

               // JOptionPane.showMessageDialog(null, "Exportación completada: " + filePath);

            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al exportar stock: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}




