package Modelo;

/**
 *
 * @author feder
 */
import Conexion.ConexionMysql;
//import com.mysql.cj.jdbc.PreparedStatementWrapper;
import java.sql.Connection;
import javax.swing.JOptionPane;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProveedorDao {
    
    Conexion.ConexionMysql cn = new ConexionMysql();
    Connection con;
    PreparedStatement ps;
    ResultSet rs;
    
    public boolean RegistrarProveedor (Proveedor pr){
        
        
    
         String sql = "INSERT INTO proveedor (cuit, nombre, telefono, domicilio, email) VALUES (?,?,?,?,?)";
     
         try {
             con=cn.conectar();
             ps = con.prepareStatement(sql);
             
             ps.setString(1, pr.getCuit());
             ps.setString(2, pr.getNombre());
             ps.setString(3, pr.getTelefono());
             ps.setString(4, pr.getDomicilio());
             ps.setString(5, pr.getEmail());
             ps.execute();
             return true;
        } catch (SQLException e) {
             System.out.println(e.toString());
             return false;
        }finally{
             try {
                 con.close();
            } catch (SQLException ex) {
                 System.out.println(ex.toString());
            }
         }
    }
    
    public List ListarProvedor(){
        List<Proveedor> ListaPr = new ArrayList();
        String sql ="SELECT * FROM proveedor";
        
        try {
            con = cn.conectar();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while(rs.next()){
                Proveedor pr = new Proveedor();
               
                pr.setId(rs.getInt("id"));
                pr.setNombre(rs.getString("nombre"));
                pr.setCuit(rs.getString("cuit"));
                pr.setTelefono(rs.getString("telefono"));
                pr.setDomicilio(rs.getString("domicilio"));
                pr.setEmail(rs.getString("email"));
                ListaPr.add(pr);
            
            
            }
            
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
    
    return ListaPr;
    }
    
    public boolean EliminarProveedor(int id){
    
        String sql ="DELETE FROM proveedor WHERE id =?";
        
        try {
            con = cn.conectar();
            ps = con.prepareStatement(sql);
            ps.setInt(1,id);
            ps.execute();
            return true;
        
        } catch (SQLException e) {
            System.out.println(e.toString());
            return false;
        }finally{
            try {
                con.close();
            } catch (SQLException e) {
                System.out.println(e.toString());
            }
        
        }
    }
    
    public boolean ModificarProveedor(Proveedor pr){
    
        String sql = "UPDATE proveedor SET cuit=?, nombre=?, telefono=?, domicilio=?, email=? WHERE id=?";
        
        try {
            con = cn.conectar();
            ps = con.prepareStatement(sql);
            ps.setString(1, pr.getCuit());
            ps.setString(2, pr.getNombre());
            ps.setString(3, pr.getTelefono());
            ps.setString(4, pr.getDomicilio());
            ps.setString(5, pr.getEmail());
            ps.setInt(6, pr.getId());
            ps.execute();
            return true;
            
        } catch (SQLException e) {
            System.out.println(e.toString());
            return false;
        }finally{
            try {
                con.close();
            } catch (SQLException e) {
                System.out.println(e.toString());
            }
        
        
        }
    }
}
