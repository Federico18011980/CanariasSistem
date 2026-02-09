
package Modelo;

import Conexion.ConexionMysql;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class CajaCierreDao {

    private final ConexionMysql cn = new ConexionMysql();


    public boolean ejecutarCierreCompleto(CajaCierre c) {
        String sqlCheck = "SELECT COUNT(*) FROM caja_cierre WHERE id_apertura = ?";
        
        String sqlInsert = "INSERT INTO caja_cierre (fecha, hora_cierre, monto_final, total_ventas_efectivo, "
                + "total_ventas_transferencia, total_ventas_credito, total_salidas, diferencia, usuario, id_apertura) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        String sqlUpdateApertura = "UPDATE caja_apertura SET estado = 'CERRADA' WHERE id = ?";

        try (Connection con = cn.conectar()) {
            con.setAutoCommit(false); // Iniciamos transacción
            
            try (PreparedStatement psCheck = con.prepareStatement(sqlCheck)) {
                psCheck.setInt(1, c.getIdApertura());
                ResultSet rs = psCheck.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) {
                    System.out.println("⚠️ Esta apertura ya tiene un cierre registrado.");
                    return false;
                }
            }

            try (PreparedStatement psIns = con.prepareStatement(sqlInsert); PreparedStatement psUpd = con.prepareStatement(sqlUpdateApertura)) {

                // 1. Insertar el cierre
                psIns.setDate(1, java.sql.Date.valueOf(c.getFecha()));
                psIns.setTime(2, java.sql.Time.valueOf(c.getHoraCierre()));
                psIns.setBigDecimal(3, c.getMontoFinal());
                psIns.setBigDecimal(4, c.getTotalEfectivo());
                psIns.setBigDecimal(5, c.getTotalTransferencia());
                psIns.setBigDecimal(6, c.getTotalCredito());
                psIns.setBigDecimal(7, c.getTotalSalidas());
                psIns.setBigDecimal(8, c.getDiferencia());
                psIns.setString(9, c.getUsuario());
                psIns.setInt(10, c.getIdApertura());
                psIns.executeUpdate();

                // 2. Actualizar estado de apertura
                psUpd.setInt(1, c.getIdApertura());
                psUpd.executeUpdate();

                con.commit(); // Si todo salió bien, guardamos
                return true;
            } catch (SQLException e) {
                con.rollback(); // Si algo falló, deshacemos todo
                System.out.println("❌ Error en transacción de cierre: " + e.getMessage());
                return false;
            }
        } catch (SQLException e) {
            System.out.println("❌ Error de conexión: " + e.getMessage());
            return false;
        }
    }
    
    public List<CajaCierre> listarHistorico(LocalDate inicio, LocalDate fin) {
        List<CajaCierre> lista = new ArrayList<>();
        String sql = "SELECT * FROM caja_cierre WHERE fecha BETWEEN ? AND ? ORDER BY id DESC";

        try (Connection con = cn.conectar(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDate(1, java.sql.Date.valueOf(inicio));
            ps.setDate(2, java.sql.Date.valueOf(fin));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    CajaCierre c = new CajaCierre();
                    c.setId(rs.getInt("id"));
                    c.setFecha(rs.getDate("fecha").toLocalDate());
                    c.setHoraCierre(rs.getTime("hora_cierre").toLocalTime());
                    c.setMontoFinal(rs.getBigDecimal("monto_final"));
                    c.setTotalEfectivo(rs.getBigDecimal("total_ventas_efectivo"));
                    c.setDiferencia(rs.getBigDecimal("diferencia"));
                    c.setUsuario(rs.getString("usuario"));
                    lista.add(c);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al listar histórico: " + e.getMessage());
        }
        return lista;
    }
    
    public List<Map<String, Object>> listarHistoricoConDetalle(LocalDate inicio, LocalDate fin) {
        List<Map<String, Object>> lista = new ArrayList<>();
        // Usamos un JOIN para traer el monto_inicial de la tabla apertura
        String sql = "SELECT c.*, a.monto_inicial "
                + "FROM caja_cierre c "
                + "INNER JOIN caja_apertura a ON c.id_apertura = a.id "
                + "WHERE c.fecha BETWEEN ? AND ? ORDER BY c.id DESC";

        try (Connection con = cn.conectar(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDate(1, java.sql.Date.valueOf(inicio));
            ps.setDate(2, java.sql.Date.valueOf(fin));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> fila = new HashMap<>();
                    fila.put("id", rs.getInt("id"));
                    fila.put("fecha", rs.getDate("fecha").toLocalDate());
                    fila.put("monto_inicial", rs.getBigDecimal("monto_inicial"));
                    fila.put("monto_final", rs.getBigDecimal("monto_final"));
                    fila.put("diferencia", rs.getBigDecimal("diferencia"));
                    fila.put("usuario", rs.getString("usuario"));
                    lista.add(fila);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

}

