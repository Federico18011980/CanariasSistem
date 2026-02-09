
package Modelo;

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

public class ClienteDao {
    
    Conexion.ConexionMysql cn = new ConexionMysql();
    Connection con = cn.conectar();
    PreparedStatement ps;
    ResultSet rs;
    
    public boolean registrar(Cliente cl){
        String sql = "INSERT INTO clientes (nombre, dni, domicilio, telefono, email, razon_social) VALUES (?,?,?,?,?,?)";
        try {
         con = cn.conectar();
         ps = con.prepareStatement(sql);
         ps.setString(1,cl.getNombre());
         ps.setInt(2, cl.getDni());
         ps.setString(3,cl.getDomicilio());
         ps.setString(4,cl.getTelefono());
         ps.setString(5,cl.getEmail());
         ps.setString(6,cl.getRazon());
         ps.execute();
         return true;
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.toString());
            return false;
            
        }finally{
            try {
                con.close();
            } catch (SQLException e) {
                System.out.println(e.toString());
            }
            
        }
        
       
    }
    
    public List ListarClientes (){
        
        List<Cliente> ListaCL = new ArrayList();
        String sql  = "SELECT * FROM clientes";
        
        try {
         con = cn.conectar();
         ps = con.prepareStatement(sql);
         rs = ps.executeQuery();
         
         while (rs.next()){
             Cliente cl = new Cliente();
             cl.setId(rs.getInt("id"));
             cl.setNombre(rs.getString("nombre"));
             cl.setDni(rs.getInt("dni"));
             cl.setDomicilio(rs.getString("domicilio"));
             cl.setTelefono(rs.getString("telefono"));
             cl.setEmail(rs.getString("email"));
             cl.setRazon(rs.getString("razon_social"));
             ListaCL.add(cl);
            }
                
        } catch (SQLException e) {
            System.out.println("Modelo.ClienteDao.ListarClientes()" +e.toString());
        }
        
     return ListaCL; 
    }
    
    public boolean EliminarCliente(int id){
        String sql = "DELETE FROM clientes WHERE id = ?";
        
        try {
          ps = con.prepareStatement(sql);
          ps.setInt(1, id);
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
    
    public boolean ModificarCliente(Cliente cl){
    
        String sql ="UPDATE clientes SET nombre=?, dni=?, domicilio=?, telefono=?, email=?, razon_social=? WHERE id=?";
        
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1,cl.getNombre());
            ps.setInt(2,cl.getDni());
            ps.setString(3,cl.getDomicilio());
            ps.setString(4,cl.getTelefono());
            ps.setString(5,cl.getEmail());
            ps.setString(6, cl.getRazon());
            ps.setInt(7, cl.getId());
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
    
    public Cliente BuscarCliente(int dni){
        Cliente cl = new Cliente();
        String sql = "SELECT * FROM clientes WHERE dni=?";
        try {
            con = cn.conectar();
            ps = con.prepareStatement(sql);
            ps.setInt(1, dni);
            rs = ps.executeQuery();
            
            if(rs.next()){
                cl.setNombre(rs.getString("nombre"));
                cl.setDomicilio(rs.getString("Domicilio"));
                cl.setTelefono(rs.getString("telefono"));
                cl.setEmail(rs.getString("email"));
                cl.setRazon(rs.getString("razon_social"));
            
            }
            
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
    
    
        return cl;
    }
}

