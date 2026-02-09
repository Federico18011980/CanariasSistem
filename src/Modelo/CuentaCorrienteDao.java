package Modelo;

import Conexion.ConexionMysql;
import java.sql.*;

public class CuentaCorrienteDao {

    ConexionMysql cn = new ConexionMysql();

    public boolean registrarCuenta(CuentaCorriente cc) {

        String sql = "INSERT INTO cuentas_corrientes "
                   + "(id_venta, cliente, total, saldo, fecha, estado) "
                   + "VALUES (?, ?, ?, ?, ?, 'ABIERTA')";

        try (Connection con = cn.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, cc.getIdVenta());
            ps.setString(2, cc.getCliente());
            ps.setBigDecimal(3, cc.getTotal());
            ps.setBigDecimal(4, cc.getSaldo());
            ps.setString(5, cc.getFecha());
            ps.execute();
            return true;

        } catch (SQLException e) {
            System.out.println("Error cuenta corriente: " + e.getMessage());
            return false;
        }
    }
}

