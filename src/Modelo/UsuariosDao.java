
package Modelo;

import Conexion.ConexionMysql;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class UsuariosDao {
    
    Conexion.ConexionMysql cn = new ConexionMysql();
    Connection con = cn.conectar();
    PreparedStatement ps;
    ResultSet rs;
    
    
    public List ListarUsuarios (){
        
        List<Usuarios> ListaUs = new ArrayList();
        String sql  = "SELECT * FROM usuarios";
        
        try {
         con = cn.conectar();
         ps = con.prepareStatement(sql);
         rs = ps.executeQuery();
         
         while (rs.next()){
             Usuarios Us = new Usuarios();
             Us.setId(rs.getInt("id"));
             Us.setNombre(rs.getString("nombre"));
             Us.setCorreo(rs.getString("correo"));
             Us.setRol(rs.getString("rol"));
             ListaUs.add(Us);
            }
                
        } catch (SQLException e) {
            System.out.println("Modelo.ClienteDao.ListarClientes()" +e.toString());
        }
        
     return ListaUs; 
    }

}
