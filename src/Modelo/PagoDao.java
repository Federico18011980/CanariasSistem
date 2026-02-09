package Modelo;

import Conexion.ConexionMysql;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PagoDao {

    Connection con;
    ConexionMysql cn = new ConexionMysql();
    PreparedStatement ps;

    public boolean registrarPago(Pago pago) {

        String sql = "INSERT INTO pagos (id_venta, tipo, monto_bruto, descuento, monto_final) "
                   + "VALUES (?, ?, ?, ?, ?)";

        try {
            con = cn.conectar();
            ps = con.prepareStatement(sql);
            ps.setInt(1, pago.getIdVenta());
            ps.setString(2, pago.getTipo().name());
            ps.setBigDecimal(3, pago.getMontoBruto());
            ps.setBigDecimal(4, pago.getDescuento());
            ps.setBigDecimal(5, pago.getMontoFinal());
            ps.execute();
            return true;

        } catch (SQLException e) {
            System.out.println("Error registrarPago: " + e);
            return false;

        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                System.out.println(e.toString());
            }
        }
    }
}
