
package Modelo;

import Conexion.ConexionMysql;
import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;

public class VentaDao {

    ConexionMysql cn = new ConexionMysql();

    public int IdVenta() {
        int id = 0;
        String sql = "SELECT MAX(id) FROM ventas";

        try (Connection con = cn.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                id = rs.getInt(1);
            }

        } catch (SQLException e) {
            System.out.println("Error IdVenta: " + e.toString());
        }

        return id;
    }

    public boolean RegistrarVenta(Venta V) {
        String sql = "INSERT INTO ventas (cliente, vendedor, total, fecha, pago, estado) VALUES (?, ?, ?, ?, ?, 'ACTIVA')";

        try (Connection con = cn.conectar(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, V.getCliente());
            ps.setString(2, V.getVendedor());
            ps.setBigDecimal(3, V.getTotal());
            ps.setString(4, V.getFecha());
            ps.setString(5, V.getPago());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error RegistrarVenta: " + e.getMessage());
            return false;
        }
    }
    
    public int registrarVenta(Venta v) {
        String sql = "INSERT INTO ventas (cliente, vendedor, total, fecha, estado) "
                + "VALUES (?, ?, ?, CURRENT_DATE, 'ACTIVA')";

        try (
            Connection con = cn.conectar();
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, v.getCliente());
            ps.setString(2, v.getVendedor());
            ps.setBigDecimal(3, v.getTotal());

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1); // ID de la venta
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1;
    }


    
    public int RegistrarDetalle(Detalle Dv) {
        String sql = "INSERT INTO detalles (cod_producto, cantidad, precio, id_venta) VALUES (?, ?, ?, ?)";

        try (Connection con = cn.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, Dv.getCod_pro());
            ps.setBigDecimal(2, Dv.getCant());
            ps.setBigDecimal(3, Dv.getPrecio());
            ps.setInt(4, Dv.getIdVenta());
            ps.execute();

        } catch (SQLException e) {
            System.out.println("Error RegistrarDetalle: " + e.toString());
        }

        return 1; // mismo comentario que arriba
    }

    public boolean ActualizarStock(BigDecimal cant, String cod) {
        String sql = "UPDATE productos SET stock = ? WHERE codigo = ?";

        try (Connection con = cn.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setBigDecimal(1, cant);
            ps.setString(2, cod);
            ps.execute();
            return true;

        } catch (SQLException e) {
            System.out.println("Error ActualizarStock: " + e.toString());
            return false;
        }
    }

    public List<Venta> ListarVentas() {
        List<Venta> ListaVentas = new ArrayList<>();
        String sql = "SELECT * FROM ventas";

        try (Connection con = cn.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Venta ven = new Venta();
                ven.setId(rs.getInt("id"));
                ven.setCliente(rs.getString("cliente"));
                ven.setVendedor(rs.getString("vendedor"));
                ven.setTotal(rs.getBigDecimal("total"));
                ven.setFecha(rs.getString("fecha"));
                ven.setPago(rs.getString("pago"));
                ven.setEstado(rs.getString("estado"));
                ListaVentas.add(ven);
            }

        } catch (SQLException e) {
            System.out.println("Error ListarVentas: " + e.toString()); 
        }

        return ListaVentas;
    }

    public void ConsultarUsuario(JComboBox usuario) {
        String sql = "SELECT nombre FROM usuarios";

        try (Connection con = cn.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                usuario.addItem(rs.getString("nombre"));
            }

        } catch (SQLException e) {
            System.out.println("Error ConsultarUsuario: " + e.toString());
        }
    }
    
    public BigDecimal ventasEfectivoHoy() {
        String sql = "SELECT IFNULL(SUM(total), 0) FROM ventas WHERE fecha = CURDATE() AND pago = 'efectivo'";
        BigDecimal total = BigDecimal.ZERO;

        try (Connection con = cn.conectar(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                total = rs.getBigDecimal(1);
                if (total == null) {
                    total = BigDecimal.ZERO;
                }
            }

        } catch (SQLException e) {
            System.out.println("Error ventasEfectivoHoy: " + e.getMessage());
        }

        return total;
    }

    public VentasResumen ventasResumenHoy() {
        VentasResumen r = new VentasResumen();

        String sql = "SELECT "
                + "IFNULL(SUM(CASE WHEN pago = 'efectivo' THEN total END),0) AS efectivo, "
                + "IFNULL(SUM(CASE WHEN pago = 'transferencia' THEN total END),0) AS transferencia, "
                + "IFNULL(SUM(CASE WHEN pago = 'credito' THEN total END),0) AS credito "
                + "FROM ventas WHERE fecha = CURDATE()";

        try (Connection con = cn.conectar(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                r.setEfectivo(rs.getBigDecimal("efectivo"));
                r.setTransferencia(rs.getBigDecimal("transferencia"));
                r.setCredito(rs.getBigDecimal("credito"));
            }

        } catch (SQLException e) {
            System.out.println("Error ventasResumenHoy: " + e.getMessage());
        }

        return r;
    }
    

public BigDecimal totalPorPago(String pago, int idApertura) {

    String sql = "SELECT COALESCE(SUM(total),0) " +
             "FROM ventas " +
             "WHERE pago = ? " +
             "AND CAST(fecha AS DATE) = CURRENT_DATE";


    BigDecimal total = BigDecimal.ZERO;

    try (Connection con = cn.conectar();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setString(1, pago);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            total = rs.getBigDecimal(1);
            if (total == null) total = BigDecimal.ZERO;
        }

    } catch (SQLException e) {
        System.out.println("Error totalPorPago: " + e.getMessage());
    }

    return total;
}

public Venta buscarVentaPorId(int idVenta) {

    String sql = "SELECT * FROM ventas WHERE id = ?";
    Venta v = null;

    try (Connection con = cn.conectar();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setInt(1, idVenta);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            v = new Venta();
            v.setId(rs.getInt("id"));
            v.setCliente(rs.getString("cliente"));
            v.setVendedor(rs.getString("vendedor"));
            v.setTotal(rs.getBigDecimal("total"));
            v.setFecha(rs.getString("fecha"));
            v.setPago(rs.getString("pago"));
            v.setEstado(rs.getString("estado"));
        }

    } catch (SQLException e) {
        System.out.println("Error buscarVentaPorId: " + e.getMessage());
    }

    return v;
}



public List<Detalle> listarDetallesPorVenta(int idVenta) {

    List<Detalle> lista = new ArrayList<>();
    String sql = "SELECT * FROM detalles WHERE id_venta = ?";

    try (Connection con = cn.conectar();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setInt(1, idVenta);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Detalle d = new Detalle();
            d.setCod_pro(rs.getString("cod_producto"));
            d.setCant(rs.getBigDecimal("cantidad"));
            d.setPrecio(rs.getBigDecimal("precio"));
            d.setIdVenta(idVenta);
            lista.add(d);
        }

    } catch (SQLException e) {
        System.out.println("Error listarDetallesPorVenta: " + e.getMessage());
    }

    return lista;
}

public BigDecimal cantidadVendida(int idVenta, String codigo) {

    String sql = "SELECT SUM(cantidad)\n" +
"        FROM detalles\n" +
"        WHERE id_venta = ? AND cod_producto = ?";

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
        System.out.println("Error cantidadVendida: " + e.getMessage());
    }

    return BigDecimal.ZERO;
}


public boolean ventaAnulada(int idVenta) {

    String sql = "SELECT estado FROM ventas WHERE id = ?";

    try (Connection con = cn.conectar();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setInt(1, idVenta);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return "ANULADA".equalsIgnoreCase(rs.getString("estado"));
        }

    } catch (SQLException e) {
        System.out.println("Error ventaAnulada: " + e.getMessage());
    }

    return false;
}

public boolean anularVenta(int idVenta) {
    
    //----------------------------------------
    //VALIDACION, SI LA VENTA TIENE DEVOLUCION 
    //NO SE PODRÁ ANULART
    //----------------------------------------
    
    String sql = "UPDATE ventas SET estado = 'ANULADA' WHERE id = ?";

    try (Connection con = cn.conectar();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setInt(1, idVenta);
        ps.executeUpdate();
        return true;

    } catch (SQLException e) {
        System.out.println("Error anularVenta: " + e.getMessage());
        return false;
    }
}



public boolean marcarVentaAnulada(int idVenta) {
    String sql = "UPDATE ventas SET estado = 'ANULADA' WHERE id = ?";

    try (Connection con = cn.conectar();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setInt(1, idVenta);
        return ps.executeUpdate() > 0;

    } catch (SQLException e) {
        System.out.println("Error al anular la venta: " + e.getMessage());
        return false;
    }
}


public boolean tieneDevoluciones(int idVenta) {
    String sql = "SELECT COUNT(*) FROM devoluciones WHERE id_venta = ?";

    try (Connection con = cn.conectar();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setInt(1, idVenta);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return rs.getInt(1) > 0; // Si hay devoluciones, devuelve true
        }

    } catch (SQLException e) {
        System.out.println("Error al verificar devoluciones: " + e.getMessage());
    }

    return false;
}



public boolean anularVenta(int idVenta, String motivo, String usuario) {

    Venta venta = buscarVentaPorId(idVenta);

    if (venta == null) {
        JOptionPane.showMessageDialog(null, "La venta no existe");
        return false;
    }

    if ("ANULADA".equalsIgnoreCase(venta.getEstado())) {
        JOptionPane.showMessageDialog(null, "La venta ya está anulada");
        return false;
    }

    // 1. Registrar anulación
    VentaAnulada va = new VentaAnulada();
    va.setIdVenta(venta.getId());
    va.setTotal(venta.getTotal());
    va.setMotivo(motivo);
    va.setUsuario(usuario);
    va.setFecha(java.time.LocalDate.now().toString());

    VentaAnuladaDao vaDao = new VentaAnuladaDao();
    if (!vaDao.registrar(va)) {
        JOptionPane.showMessageDialog(null, "Error registrando anulación");
        return false;
    }

    // 2. Marcar venta como ANULADA
    if (!marcarVentaAnulada(idVenta)) {
        JOptionPane.showMessageDialog(null, "Error actualizando venta");
        return false;
    }

    return true;
}

public List<Venta> listarVentasPorFecha(LocalDate desde, LocalDate hasta) {

    List<Venta> lista = new ArrayList<>();

    String sql = "SELECT id, cliente, vendedor, total, pago, estado, fecha " +
                 "FROM ventas " +
                 "WHERE fecha BETWEEN ? AND ? " +
                 "ORDER BY fecha ASC";

    try (Connection con = cn.conectar();
         PreparedStatement ps = con.prepareStatement(sql)) {
        
        ps.setDate(1, java.sql.Date.valueOf(desde));
        ps.setDate(2, java.sql.Date.valueOf(hasta));

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Venta v = new Venta();
            v.setId(rs.getInt("id"));
            v.setCliente(rs.getString("cliente"));
            v.setVendedor(rs.getString("vendedor"));
            v.setTotal(rs.getBigDecimal("total"));
            v.setPago(rs.getString("pago"));
            v.setEstado(rs.getString("estado"));
            v.setFecha(rs.getString("fecha"));
            lista.add(v);
        }

    } catch (SQLException e) {
        System.out.println("Error reporte ventas: " + e.getMessage());
    }

    return lista;
}


    public boolean registrarVentaConPagos(
        Venta venta,
        List<Detalle> detalles,
        List<Pago> pagos,
        int idApertura,
        String usuario
) {

    Connection con = null;

    try {
        con = cn.conectar();
        con.setAutoCommit(false);

        // 1. Venta
        String sqlVenta = "INSERT INTO ventas (cliente, vendedor, total, fecha, pago, estado) " +
                          "VALUES (?, ?, ?, CURRENT_DATE, ?, 'ACTIVA')";

        PreparedStatement psVenta =
                con.prepareStatement(sqlVenta, Statement.RETURN_GENERATED_KEYS);

        psVenta.setString(1, venta.getCliente());
        psVenta.setString(2, venta.getVendedor());
        psVenta.setBigDecimal(3, venta.getTotal());
        psVenta.setString(4, "MULTIPLE");

        psVenta.executeUpdate();

        ResultSet rs = psVenta.getGeneratedKeys();
        if (!rs.next()) throw new SQLException("No se generó ID venta");

        int idVenta = rs.getInt(1);

        // 2. Detalle
        String sqlDetalle =
                "INSERT INTO detalles (cod_producto, cantidad, precio, id_venta) VALUES (?, ?, ?, ?)";

        PreparedStatement psDet = con.prepareStatement(sqlDetalle);

        for (Detalle d : detalles) {
            psDet.setString(1, d.getCod_pro());
            psDet.setBigDecimal(2, d.getCant());
            psDet.setBigDecimal(3, d.getPrecio());
            psDet.setInt(4, idVenta);
            psDet.addBatch();
        }

        psDet.executeBatch();

        // 3. Caja
        String sqlCaja =
                "INSERT INTO caja_movimientos " +
                "(id_apertura, fecha, hora, tipo, monto, descripcion, usuario) " +
                "VALUES (?, CURRENT_DATE, CURRENT_TIME, ?, ?, ?, ?)";

        PreparedStatement psCaja = con.prepareStatement(sqlCaja);

        for (Pago p : pagos) {
            psCaja.setInt(1, idApertura);
            psCaja.setString(2, p.getTipo().name());
            psCaja.setBigDecimal(3, p.getMontoFinal());
            psCaja.setString(4, "Venta ID " + idVenta);
            psCaja.setString(5, usuario);
            psCaja.addBatch();
        }

        psCaja.executeBatch();

        con.commit();
        return true;

    } catch (SQLException e) {
        try {
            if (con != null) con.rollback();
        } catch (SQLException ex) {}

        e.printStackTrace();
        return false;
    }
}



    
}
