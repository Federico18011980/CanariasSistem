package Modelo;

import Conexion.ConexionMysql;
import java.sql.*;

public class CajaAperturaDao {

    Connection con;
    ConexionMysql cn = new ConexionMysql();
    PreparedStatement ps;
    ResultSet rs;

    // ---------------------------------------------------
    //   ABRIR CAJA
    // ---------------------------------------------------
  public boolean abrirCaja(CajaApertura apertura) {
    String sql = "INSERT INTO caja_apertura (fecha, hora_inicio, monto_inicial, usuario, estado) "
               + "VALUES (?, ?, ?, ?, 'ABIERTA')";

    try {
        con = cn.conectar();
        ps = con.prepareStatement(sql);
        
        ps.setDate(1, Date.valueOf(apertura.getFecha()));
        ps.setTime(2, Time.valueOf(apertura.getHoraInicio()));
        ps.setBigDecimal(3, apertura.getMontoInicial());
        ps.setString(4, apertura.getUsuario());

        ps.executeUpdate();
        return true;

    } catch (SQLException e) {
        System.out.println("Error abrir caja: " + e.getMessage());
        return false;
    }
}


    // ---------------------------------------------------
    //   OBTENER APERTURA ABIERTA DEL DÍA
    // ---------------------------------------------------
   public CajaApertura cajaAbiertaDelDia() {

    String sql = "SELECT * FROM caja_apertura "
               + "WHERE fecha = CURRENT_DATE AND estado = 'ABIERTA' LIMIT 1";

    try {
            con = cn.conectar();
            ps = con.prepareStatement(sql); 
            rs = ps.executeQuery() ;

        if (rs.next()) {
            CajaApertura a = new CajaApertura();
            a.setId(rs.getInt("id"));
            a.setFecha(rs.getDate("fecha").toLocalDate());
            a.setHoraInicio(rs.getTime("hora_inicio").toLocalTime());
            a.setMontoInicial(rs.getBigDecimal("monto_inicial"));
            a.setUsuario(rs.getString("usuario"));
            a.setEstado(rs.getString("estado"));
            return a;
        }

    } catch (SQLException e) {
        System.out.println("Error lectura caja abierta: " + e.getMessage());
    }

    return null;
}


    // ---------------------------------------------------
    //   CERRAR APERTURA
    // ---------------------------------------------------
    public boolean cerrarApertura(int idApertura) {
        String sql = "UPDATE caja_apertura SET estado = 'CERRADA' WHERE id = ?";
        try {
            con = cn.conectar();
            ps = con.prepareStatement(sql);
            ps.setInt(1, idApertura);
            ps.execute();
            return true;
        } catch (SQLException e) {
            System.out.println("Error cerrar apertura: " + e.getMessage());
            return false;
        }
    }

    // ---------------------------------------------------
    //   ↓↓↓ ESTE ES EL MÉTODO QUE NECESITABA EL LOGIN ↓↓↓
    // ---------------------------------------------------
    public boolean cajaAbiertaHoy() {
        String sql = "SELECT id FROM caja_apertura "
                   + "WHERE fecha = CURDATE() AND estado = 'ABIERTA' LIMIT 1";

        try {
            con = cn.conectar();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            return rs.next();  // Si hay registro → true
        }
        catch (SQLException e) {
            System.out.println("Error verificando caja abierta hoy: " + e.getMessage());
            return false;
        }
    }
    
    //---------------------------------------------------
    // ↓↓↓ OBTENER ID DE LA CAJA ABIERTA ↓↓↓
    //---------------------------------------------------
    
    public Integer obtenerIdAperturaActiva() {
        String sql = "SELECT id FROM caja_apertura WHERE fecha = CURRENT_DATE AND estado = 'ABIERTA'";

        try {
            con = cn.conectar();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("id");
            }

        } catch (SQLException e) {
            System.out.println("Error obtener apertura activa: " + e.getMessage());
        }

        return null;
    }


}
