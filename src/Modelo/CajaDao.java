
package Modelo;

/**
 *
 * @author feder
 */
import java.sql.*;
import Conexion.ConexionMysql;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

public class CajaDao {
    
    Conexion.ConexionMysql cn = new ConexionMysql();
    Connection con;
    PreparedStatement ps;
    ResultSet rs;
    
    public void ingreso(){
    
    
    }
    
    public void egreso(){
    
    
    }
    
    public int idCaja() {
        //double monto = 0.00;
        int id=0;
        String sql = "SELECT MAX(id) FROM caja";

        try {
            con = cn.conectar();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            if (rs.next()) {
                id = rs.getInt(1);
                //monto = rs.getDouble(2);
            }

        } catch (SQLException e) {
            System.out.println(e.toString());
        }
        return id;
    }
    
    public BigDecimal obtenerMontoActual(int id) {
    String sql = "SELECT monto FROM caja WHERE id = ?";
    BigDecimal monto = BigDecimal.ZERO;

    try {
        con = cn.conectar();
        ps = con.prepareStatement(sql);
        ps.setInt(1, id);
        rs = ps.executeQuery();

        if (rs.next()) {
            monto = rs.getBigDecimal("monto");
            if (rs.wasNull()) {
                monto = BigDecimal.ZERO;
            }
        }

    } catch (SQLException e) {
        System.out.println("ObtenerMonto: " + e.toString());
    }

    return monto;
}

    
public BigDecimal CargarTxtMontoCaja() {
    int id = idCaja();
    return obtenerMontoActual(id);
}

    
    public void RegistrarMovimiento(Caja C){
        
        String sql = "INSERT INTO caja (entrada, salida, monto, fecha, usuario) VALUES (?, ?, ?, ?, ?)";
        
        try {
        
            con = cn.conectar();
            ps = con.prepareStatement(sql);
            
            ps.setBigDecimal(1,C.getEntrada());
            ps.setBigDecimal(2,C.getSalida());
            ps.setBigDecimal(3,C.getMonto());
            ps.setString(4, C.getFecha());
            ps.setString(5, C.getUsuario());
            ps.execute();
            
            
        } catch (SQLException e) {
            System.out.println("Caja "+e.toString() );
        }finally {
            
            try {
                con.close();
            } catch (SQLException e) {
                System.out.println(e.toString());
            }
        
        
        }
        
        
    
    }
    
    public List ListarCaja() {

        List<Caja> ListaCaja = new ArrayList();
        String sql = "SELECT * FROM caja";

        try {
            con = cn.conectar();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Caja C = new Caja();
                C.setId(rs.getInt("id"));
                C.setEntrada(rs.getBigDecimal("entrada"));
                C.setSalida(rs.getBigDecimal("salida"));
                C.setMonto(rs.getBigDecimal("monto"));
                C.setFecha(rs.getString("fecha"));
                C.setUsuario(rs.getString("usuario"));
                
                ListaCaja.add(C);

            }

        } catch (SQLException e) {
            System.out.println("Modelo.CajaDao.ListarCaja()" + e.toString());
        }

        return ListaCaja;
    }
    
    
   

}
