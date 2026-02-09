package Modelo;

import Conexion.ConexionMysql;
import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class CajaMovimientoDao {

    private final ConexionMysql cn = new ConexionMysql();

    // =====================================================
    // REGISTRAR MOVIMIENTO
    // =====================================================
    public boolean registrarMovimiento(CajaMovimiento mov) {
        
        if (mov.getFecha() == null) {
            mov.setFecha(LocalDate.now());
        }
        if (mov.getHora() == null) {
            mov.setHora(LocalTime.now());
        }


        String sql = "INSERT INTO caja_movimientos\n" +
"            (id_apertura, fecha, hora, tipo, monto, descripcion, usuario)\n" +
"            VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = cn.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, mov.getIdApertura());
            ps.setDate(2, Date.valueOf(mov.getFecha()));
            ps.setTime(3, Time.valueOf(mov.getHora()));
            ps.setString(4, mov.getTipo());
            ps.setBigDecimal(5, mov.getMonto());
            ps.setString(6, mov.getDescripcion());
            ps.setString(7, mov.getUsuario());

            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("❌ Error registrarMovimiento: " + e.getMessage());
            return false;
        }
    }

    // =====================================================
    // TOTALES
    // =====================================================
    public BigDecimal totalEntradas(int idApertura) {
        return obtenerTotalPorTipo(idApertura, "ENTRADA");
    }

    public BigDecimal totalSalidas(int idApertura) {
        return obtenerTotalPorTipos(
                idApertura,
                List.of("SALIDA", "DEVOLUCION", "ANULACION")
        );
    }

    private BigDecimal obtenerTotalPorTipo(int idApertura, String tipo) {

        String sql = "SELECT COALESCE(SUM(monto), 0)\n" +
"            FROM caja_movimientos\n" +
"            WHERE id_apertura = ? AND tipo = ?";

        try (Connection con = cn.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idApertura);
            ps.setString(2, tipo);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getBigDecimal(1);

        } catch (SQLException e) {
            System.out.println("Error total por tipo: " + e.getMessage());
        }

        return BigDecimal.ZERO;
    }

    private BigDecimal obtenerTotalPorTipos(int idApertura, List<String> tipos) {

        String in = String.join(",", tipos.stream().map(t -> "?").toList());

        String sql ="SELECT COALESCE(SUM(monto),0)\n" +
"            FROM caja_movimientos\n" +
"            WHERE id_apertura = ?\n" +
"            AND tipo IN (" 
             + in + ")";

        try (Connection con = cn.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idApertura);

            for (int i = 0; i < tipos.size(); i++) {
                ps.setString(i + 2, tipos.get(i));
            }

            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getBigDecimal(1);

        } catch (SQLException e) {
            System.out.println("Error total múltiple: " + e.getMessage());
        }

        return BigDecimal.ZERO;
    }

    // =====================================================
    // LISTADO CON ACUMULADO
    // =====================================================
    public List<CajaMovimiento> listarMovimientos(int idApertura) {

        List<CajaMovimiento> lista = new ArrayList<>();

        String sql = " SELECT *,\n" +
"                   SUM(\n" +
"                       CASE \n" +
"                           WHEN tipo = 'ENTRADA' THEN monto\n" +
"                           ELSE -monto\n" +
"                       END\n" +
"                   ) OVER (ORDER BY fecha, hora) AS acumulado\n" +
"            FROM caja_movimientos\n" +
"            WHERE id_apertura = ?\n" +
"            ORDER BY fecha, hora";

        try (Connection con = cn.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idApertura);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                CajaMovimiento m = new CajaMovimiento();
                m.setId(rs.getInt("id"));
                m.setIdApertura(rs.getInt("id_apertura"));
                m.setFecha(rs.getDate("fecha").toLocalDate());
                m.setHora(rs.getTime("hora").toLocalTime());
                m.setTipo(rs.getString("tipo"));
                m.setMonto(rs.getBigDecimal("monto"));
                m.setDescripcion(rs.getString("descripcion"));
                m.setUsuario(rs.getString("usuario"));
                m.setAcumulado(rs.getBigDecimal("acumulado"));
                lista.add(m);
            }

        } catch (SQLException e) {
            System.out.println("Error listarMovimientos: " + e.getMessage());
        }

        return lista;
    }

    // =====================================================
    // LISTADO PARA REPORTES
    // =====================================================
    public List<CajaMovimiento> obtenerMovimientosPorFecha(LocalDate desde, LocalDate hasta) {

        List<CajaMovimiento> lista = new ArrayList<>();

        String sql = " SELECT fecha, tipo, monto, usuario, descripcion\n" +
"            FROM caja_movimientos\n" +
"            WHERE fecha BETWEEN ? AND ?\n" +
"            ORDER BY fecha, hora";

        try (Connection con = cn.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDate(1, Date.valueOf(desde));
            ps.setDate(2, Date.valueOf(hasta));

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                CajaMovimiento mov = new CajaMovimiento();
                mov.setFecha(rs.getDate("fecha").toLocalDate());
                mov.setTipo(rs.getString("tipo"));
                mov.setMonto(rs.getBigDecimal("monto"));
                mov.setUsuario(rs.getString("usuario"));
                mov.setDescripcion(rs.getString("descripcion"));
                lista.add(mov);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }
    
    public BigDecimal totalPorTipo(int idApertura, String tipo) {

        String sql = "SELECT COALESCE(SUM(monto),0)\n" +
"        FROM caja_movimientos\n" +
"        WHERE id_apertura = ? AND tipo = ?";

        try (Connection con = cn.conectar(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idApertura);
            ps.setString(2, tipo);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getBigDecimal(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return BigDecimal.ZERO;
    }
    
    public BigDecimal totalPorPago(String tipoPago, int idApertura) {
    String sql = "SELECT COALESCE(SUM(monto), 0) " +
                 "FROM caja_movimientos " +
                 "WHERE id_apertura = ? AND tipo = ?";

    try (Connection con = cn.conectar();
         PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setInt(1, idApertura);
        ps.setString(2, tipoPago);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return rs.getBigDecimal(1);
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }
    return BigDecimal.ZERO;
}

    public List<CajaMovimiento> listarRetiros() {

    List<CajaMovimiento> lista = new ArrayList<>();

    String sql = "SELECT * FROM caja_movimientos " +
                 "WHERE tipo = 'RETIRO' " +
                 "ORDER BY fecha DESC, hora DESC";

    try (Connection con = cn.conectar();
         PreparedStatement ps = con.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
            CajaMovimiento m = new CajaMovimiento();

            m.setId(rs.getInt("id"));
            m.setIdApertura(rs.getInt("id_apertura"));
            m.setFecha(rs.getDate("fecha").toLocalDate());
            m.setHora(rs.getTime("hora").toLocalTime());
            m.setTipo(rs.getString("tipo"));
            m.setMonto(rs.getBigDecimal("monto"));
            m.setDescripcion(rs.getString("descripcion"));
            m.setUsuario(rs.getString("usuario"));

            lista.add(m);
        }

    } catch (SQLException e) {
        System.out.println("Error listar retiros: " + e.getMessage());
    }

    return lista;
}

    public List<CajaMovimiento> listarRetiros(
        LocalDate desde,
        LocalDate hasta,
        String tipo,
        String usuario
) {

    List<CajaMovimiento> lista = new ArrayList<>();

    String sql =" SELECT * FROM caja_movimientos\n" +
"        WHERE tipo = 'RETIRO'\n" +
"        AND fecha BETWEEN ? AND ?";

    if (!"TODOS".equals(tipo)) {
        sql += " AND descripcion LIKE ?";
    }

    if (usuario != null && !usuario.isEmpty()) {
        sql += " AND usuario = ?";
    }

    sql += " ORDER BY fecha, hora";

    try (Connection con = cn.conectar();
         PreparedStatement ps = con.prepareStatement(sql)) {

        int i = 1;
        ps.setDate(i++, java.sql.Date.valueOf(desde));
        ps.setDate(i++, java.sql.Date.valueOf(hasta));

        if (!"TODOS".equals(tipo)) {
            ps.setString(i++, "%" + tipo + "%");
        }

        if (usuario != null && !usuario.isEmpty()) {
            ps.setString(i++, usuario);
        }

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            CajaMovimiento m = new CajaMovimiento();
            m.setId(rs.getInt("id"));
            m.setFecha(rs.getDate("fecha").toLocalDate());
            m.setHora(rs.getTime("hora").toLocalTime());
            m.setTipo(rs.getString("tipo"));
            m.setMonto(rs.getBigDecimal("monto"));
            m.setDescripcion(rs.getString("descripcion"));
            m.setUsuario(rs.getString("usuario"));

            lista.add(m);
        }

    } catch (Exception e) {
        System.out.println("Error listar retiros: " + e.getMessage());
    }

    return lista;
}


}

