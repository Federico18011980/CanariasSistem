package Modelo;

import Conexion.ConexionMysql;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class VentaAnuladaDao {

    ConexionMysql cn = new ConexionMysql();

    public boolean registrar(VentaAnulada va) {

    if (va == null || va.getIdVenta() <= 0) {
        return false;
    }

    String sql = "INSERT INTO ventas_anuladas " +
                 "(id_venta, total, motivo, fecha, usuario) " +
                 "VALUES (?,?,?,?,?)";

    try (Connection con = cn.conectar();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setInt(1, va.getIdVenta());
        ps.setBigDecimal(2, va.getTotal());
        ps.setString(3, va.getMotivo());
        ps.setString(4, va.getFecha());
        ps.setString(5, va.getUsuario());

        return ps.executeUpdate() > 0;

    } catch (SQLException e) {
        System.out.println("Error registrar venta anulada: " + e.getMessage());
        return false;
    }
}
    
public List<VentaAnulada> listarVentasAnuladasPorFecha(LocalDate desde, LocalDate hasta) {

    List<VentaAnulada> lista = new ArrayList<>();
    String sql = "SELECT * FROM ventas_anuladas WHERE fecha BETWEEN ? AND ? ORDER BY fecha";

    try (Connection con = cn.conectar();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setDate(1, java.sql.Date.valueOf(desde));
        ps.setDate(2, java.sql.Date.valueOf(hasta));

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            VentaAnulada va = new VentaAnulada();
            va.setId(rs.getInt("id"));
            va.setIdVenta(rs.getInt("id_venta"));
            va.setTotal(rs.getBigDecimal("total"));
            va.setMotivo(rs.getString("motivo"));
            va.setUsuario(rs.getString("usuario"));
            va.setFecha(rs.getString("fecha"));

            lista.add(va);
        }

    } catch (SQLException e) {
        System.out.println("Error listarVentasAnuladasPorFecha: " + e.getMessage());
    }

    return lista;
}    

}

