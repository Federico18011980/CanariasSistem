
package Modelo;

import Conexion.ConexionMysql;
//import java.awt.HeadlessException;
import java.sql.Connection;
//import javax.swing.JOptionPane;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

public class LoginDao {
    
    Connection con;
    ConexionMysql cn = new ConexionMysql();
    PreparedStatement ps;
    ResultSet rs;
    
    public Loguearse ingresar (String nombre, String pass){
    
       Loguearse l = new Loguearse();  
        String sql = "SELECT * FROM usuarios WHERE nombre =? AND pass =?";
        
        try {
            con = cn.conectar();
            ps = con.prepareStatement(sql);
            ps.setString(1,nombre);
            ps.setString(2, pass);
            rs = ps.executeQuery();
            
            if (rs.next()){
                //JOptionPane.showMessageDialog(null, "Estoy en rs");
                l.setId(rs.getInt("id"));
                l.setNombre(rs.getString("nombre"));
                l.setCorreo(rs.getString("correo"));
                l.setPass(rs.getString("pass"));
                l.setRol(rs.getString("rol"));
            
            }
            
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
        
        return l;
    
    }
    
    public boolean registrar(Loguearse reg){
    
        String sql = "INSERT INTO usuarios (nombre, correo, pass, rol) VALUES (?, ?, ?, ?)";
        
        try {
            con = cn.conectar();
            ps = con.prepareStatement(sql);
            ps.setString(1, reg.getNombre());
            ps.setString(2, reg.getCorreo());
            ps.setString(3, reg.getPass());
            ps.setString(4, reg.getRol());
            ps.execute();
            return true;
            
            
            
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
        return false;
    }
    
}
 