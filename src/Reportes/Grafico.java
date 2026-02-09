
package Reportes;

/**
 *
 * @author feder
 */

import java.sql.*;
import Conexion.ConexionMysql;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;


public class Grafico {

   
    
    public static void Graficar(String fecha) {
        
        Connection con;
        Conexion.ConexionMysql cn = new ConexionMysql();
        PreparedStatement ps;
        ResultSet rs;
        
        try {
            String sql = "SELECT total FROM ventas WHERE fecha = ? ";
            con = cn.conectar();
            ps = con.prepareStatement(sql);
            ps.setString(1,fecha);
            rs = ps.executeQuery();
            DefaultPieDataset dataset = new DefaultPieDataset();
            while(rs.next()){
                dataset.setValue(rs.getString("total"), rs.getDouble("total"));   
            
            }
            
            JFreeChart jf = ChartFactory.createPieChart("Reporte de ventas", dataset);
            ChartFrame Cf = new ChartFrame ("Total de Ventas por dia",jf);
            Cf.setSize(1000,500);
            Cf.setLocationRelativeTo(null);
            Cf.setVisible(true);
            
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
        
        
        

    }

}
