package Modelo;

import Conexion.ConexionMysql;
import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DevolucionesDao {

    ConexionMysql cn = new ConexionMysql();

    public boolean registrarDevolucion(Devolucion d) {
        String sql = "INSERT INTO devoluciones "
           + "(id_venta, cod_producto, cantidad, precio_unitario, total, tipo, forma_pago, fecha, usuario ) "
           + "VALUES (?,?,?,?,?,?,?,?,?)";

        try (Connection con = cn.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, d.getIdVenta());
            ps.setString(2, d.getCodProducto());
            ps.setBigDecimal(3, d.getCantidad());
            ps.setBigDecimal(4, d.getPrecio());
            ps.setBigDecimal(5, d.getTotal());
            ps.setString(6, d.getTipo());
            ps.setString(7, d.getFormaPago());
            ps.setString(8, d.getFecha());
            ps.setString(9, d.getUsuario());
           

            ps.execute();
            return true;

        } catch (SQLException e) {
            System.out.println("Error registrar devolución: " + e.getMessage());
            return false;
        }
    }
    
    public BigDecimal cantidadDevuelta(int idVenta, String codigo) {

    String sql = "SELECT SUM(cantidad) "
               + "FROM devoluciones "
               + "WHERE id_venta = ? AND cod_producto = ?";

    try (Connection con = cn.conectar();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setInt(1, idVenta);
        ps.setString(2, codigo);

        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            BigDecimal cant = rs.getBigDecimal(1);
            return cant != null ? cant : BigDecimal.ZERO;
        }

    } catch (SQLException e) {
        System.out.println("Error cantidadDevuelta: " + e.getMessage());
    }

    return BigDecimal.ZERO;
}

public BigDecimal cantidadVendida(int idVenta, String codigo) {
    String sql = "SELECT IFNULL(SUM(cantidad), 0) FROM detalles WHERE id_venta = ? AND cod_producto = ?";
    try (Connection con = cn.conectar();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setInt(1, idVenta);
        ps.setString(2, codigo);

        ResultSet rs = ps.executeQuery();
        if (rs.next()) return rs.getBigDecimal(1);

    } catch (SQLException e) {
        System.out.println("Error cantidad vendida: " + e.getMessage());
    }
    return BigDecimal.ZERO;
}

public boolean tieneDevoluciones(int idVenta) {

    String sql = "SELECT COUNT(*) FROM devoluciones WHERE id_venta = ?";

    try (Connection con = cn.conectar();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setInt(1, idVenta);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return rs.getInt(1) > 0;
        }

    } catch (SQLException e) {
        System.out.println("Error validando devoluciones: " + e.getMessage());
    }

    return false;
}

// En DevolucionesDao.java:

    public List<Devolucion> listarDevolucionesPorFecha(LocalDate desde, LocalDate hasta) {
        List<Devolucion> lista = new ArrayList<>();

        String sql = "SELECT * FROM devoluciones "
                + "WHERE fecha BETWEEN ? AND ? "
                + "ORDER BY fecha";

        try (Connection con = cn.conectar(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDate(1, java.sql.Date.valueOf(desde));
            ps.setDate(2, java.sql.Date.valueOf(hasta));

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Devolucion d = new Devolucion();
                d.setId(rs.getInt("id"));
                d.setIdVenta(rs.getInt("id_venta"));
                d.setCodProducto(rs.getString("cod_producto"));
                d.setCantidad(rs.getBigDecimal("cantidad"));
                d.setPrecio(rs.getBigDecimal("precio_unitario"));
                d.setTotal(rs.getBigDecimal("total"));
                d.setTipo(rs.getString("tipo"));
                d.setFormaPago(rs.getString("forma_pago"));

                // Comprobamos si la fecha es nula y asignamos un valor predeterminado si es necesario
                String fechaStr = rs.getString("fecha");
                if (fechaStr != null && !fechaStr.isEmpty()) {
                    d.setFecha(fechaStr);  // Asumimos que la fecha en la base de datos está en formato correcto
                } else {
                    d.setFecha("1970-01-01");  // Fecha predeterminada si es nula
                }

                d.setUsuario(rs.getString("usuario"));

                lista.add(d);
            }

        } catch (SQLException e) {
            System.out.println("Error listarDevolucionesPorFecha: " + e.getMessage());
        }

        return lista;
    }


//public List<Devolucion> listarDevolucionesPorFecha(LocalDate desde, LocalDate hasta) {
//
//    List<Devolucion> lista = new ArrayList<>();
//
//    String sql = "SELECT * FROM devoluciones "
//               + "WHERE fecha BETWEEN ? AND ? "
//               + "ORDER BY fecha";
//
//    try (Connection con = cn.conectar();
//         PreparedStatement ps = con.prepareStatement(sql)) {
//
//        ps.setDate(1, java.sql.Date.valueOf(desde));
//        ps.setDate(2, java.sql.Date.valueOf(hasta));
//
//        ResultSet rs = ps.executeQuery();
//
//        while (rs.next()) {
//            Devolucion d = new Devolucion();
//            d.setId(rs.getInt("id"));
//            d.setIdVenta(rs.getInt("id_venta"));
//            d.setCodProducto(rs.getString("cod_producto"));
//            d.setCantidad(rs.getBigDecimal("cantidad"));
//            d.setPrecio(rs.getBigDecimal("precio_unitario"));
//            d.setTotal(rs.getBigDecimal("total"));
//            d.setTipo(rs.getString("tipo"));
//            d.setFormaPago(rs.getString("forma_pago"));
//            d.setFecha(rs.getString("fecha"));
//            d.setUsuario(rs.getString("usuario"));
//
//            lista.add(d);
//        }
//
//    } catch (SQLException e) {
//        System.out.println("Error listarDevolucionesPorFecha: " + e.getMessage());
//    }
//
//    return lista;
//}


}


