
package Modelo;

import Conexion.ConexionMysql;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.math.BigDecimal;
import javax.swing.JOptionPane;


/**
 *
 * @author feder
 */
public class ConfigDao {
    Connection con;
    ConexionMysql cn = new ConexionMysql();
    PreparedStatement ps;
   // ResultSet rs;
    
    public boolean ModificarDatos(Config conf) {

        String sql = "UPDATE config SET nombre=?, cuit=?, telefono=?, direccion=?, razon=?, entrada=?, salida=?, efectivo=?, transferencia=?, credito=? WHERE id=?";

        try {
            con = cn.conectar();
            ps = con.prepareStatement(sql);
            ps.setString(1, conf.getNombre());
            ps.setString(2, conf.getCuit());
            ps.setString(3, conf.getTelefono());
            ps.setString(4, conf.getDireccion());
            ps.setString(5, conf.getRazon());
            ps.setString(6, conf.getEntrada());
            ps.setString(7, conf.getSalida());
            ps.setBigDecimal(8, conf.getEfectivo());
            ps.setBigDecimal(9, conf.getTransferencia());
            ps.setBigDecimal(10, conf.getCredito());
            ps.setInt(11,conf.getId());
            ps.execute();
            
            return true;

        } catch (SQLException e) {
            System.out.println(e.toString());
            return false;
        } finally {
            try {
                con.close();
            } catch (SQLException ex) {
                System.out.println(ex.toString());
            }
        }

    }
    
    
    public BigDecimal obtenerDescuentoEfectivo() {
        String sql = "SELECT efectivo FROM config LIMIT 1";
        BigDecimal descuento = BigDecimal.ZERO;

        try {
            con = cn.conectar();
            ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                descuento = rs.getBigDecimal("efectivo");
            }

        } catch (SQLException e) {
            System.out.println(e.toString());
        } finally {
            try {
                con.close();
            } catch (SQLException ex) {
                System.out.println(ex.toString());
            }
        }

        return descuento;
    }

    public BigDecimal obtenerDescuentoTransferencia() {
        String sql = "SELECT transferencia FROM config LIMIT 1";
        BigDecimal descuento = BigDecimal.ZERO;

        try {
            con = cn.conectar();
            ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                descuento = rs.getBigDecimal("transferencia");
            }

        } catch (SQLException e) {
            System.out.println(e.toString());
        } finally {
            try {
                con.close();
            } catch (SQLException ex) {
                System.out.println(ex.toString());
            }
        }

        return descuento;
    }

   
    public BigDecimal obtenerDescuentoCredito() {
        String sql = "SELECT credito FROM config LIMIT 1";
        BigDecimal descuento = BigDecimal.ZERO;

        try {
            con = cn.conectar();
            ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                descuento = rs.getBigDecimal("credito");
            }

        } catch (SQLException e) {
            System.out.println(e.toString());
        } finally {
            try {
                con.close();
            } catch (SQLException ex) {
                System.out.println(ex.toString());
            }
        }

        return descuento;
    }
    
    
    public Config obtenerConfig() {
        Config conf = new Config();
        String sql = "SELECT * FROM config";

        try  {
            
             con = cn.conectar();
             ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                conf.setId(rs.getInt("id"));
                conf.setNombre(rs.getString("nombre"));
                conf.setCuit(rs.getString("cuit"));
                conf.setTelefono(rs.getString("telefono"));
                conf.setDireccion(rs.getString("direccion"));
                conf.setRazon(rs.getString("razon"));
                conf.setEntrada(rs.getString("entrada"));
                conf.setSalida(rs.getString("salida"));
                conf.setEfectivo(rs.getBigDecimal("efectivo"));
                conf.setTransferencia(rs.getBigDecimal("transferencia"));
                conf.setCredito(rs.getBigDecimal("credito"));
            } else {
                JOptionPane.showMessageDialog(null, "Código no encontrado");
            }

        } catch (SQLException e) {
            System.out.println(e.toString());
        }

        return conf;
    }

}
