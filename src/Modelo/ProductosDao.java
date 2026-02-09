package Modelo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;
import Conexion.ConexionMysql;
import java.math.RoundingMode;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;

public class ProductosDao {

    Connection con;
    PreparedStatement ps;
    ResultSet rs;
    ConexionMysql cn = new ConexionMysql();

    public boolean RegistrarProductos(Productos pro) {
        String sql = "INSERT INTO productos (codigo, descripcion, precio, stock, proveedor, id_categoria, id_talle, id_color) VALUES (?,?,?,?,?,?,?,?)";
        
        // VALIDACIÓN ANTIDUPLICADOS EXTRA
        String sqlCheck = "SELECT COUNT(*) FROM productos WHERE codigo = ?";
        try (Connection conValidar = cn.conectar(); PreparedStatement psCheck = conValidar.prepareStatement(sqlCheck)) {

            psCheck.setString(1, pro.getCodigo());
            ResultSet rs = psCheck.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                JOptionPane.showMessageDialog(null, "El código " + pro.getCodigo() + " ya existe. Genere uno nuevo.");
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Error validando duplicado: " + e.toString());
        }

        
        try {
            con = cn.conectar();
            ps = con.prepareStatement(sql);

            ps.setString(1, pro.getCodigo());
            ps.setString(2, pro.getDescripcion());
            ps.setBigDecimal(3, pro.getPrecio());
            ps.setBigDecimal(4, pro.getStock());
            ps.setString(5, pro.getProveedor());
            ps.setInt(6, pro.getIdCategoria());
            ps.setInt(7, pro.getIdTalle());
            ps.setInt(8, pro.getIdColor());

            ps.execute();
            return true;

        } catch (SQLException e) {
            System.out.println("Error RegistrarProductos: " + e.toString());
            return false;
        }
    }

    public List<Productos> ListarProductos() {
        List<Productos> lista = new ArrayList<>();

        String sql = "SELECT p.id, p.codigo, p.descripcion, p.stock, p.precio, "
                + "c.nombre AS categoria, t.nombre AS talle, co.nombre AS color, "
                + "p.proveedor "
                + "FROM productos p "
                + "LEFT JOIN categorias c ON p.id_categoria = c.id "
                + "LEFT JOIN talles t ON p.id_talle = t.id "
                + "LEFT JOIN colores co ON p.id_color = co.id "
                + "ORDER BY p.descripcion ASC";

        try {
            con = cn.conectar();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Productos pro = new Productos();

                pro.setId(rs.getInt("id"));
                pro.setCodigo(rs.getString("codigo"));
                pro.setDescripcion(rs.getString("descripcion"));
                pro.setStock(rs.getBigDecimal("stock"));
                pro.setPrecio(rs.getBigDecimal("precio"));

                // NUEVOS: nombres de tablas relacionadas
                pro.setCategoriaNombre(rs.getString("categoria"));
                pro.setTalleNombre(rs.getString("talle"));
                pro.setColorNombre(rs.getString("color"));

                pro.setProveedor(rs.getString("proveedor"));

                lista.add(pro);
            }
        } catch (SQLException e) {
            System.out.println("Error ListarProductos: " + e.getMessage());
        }

        return lista;
    }

    public Productos BuscarProducto(String cod) {
        Productos pro = new Productos();
        String sql = "SELECT * FROM productos WHERE codigo=? LIMIT 1";

        try {
            con = cn.conectar();
            ps = con.prepareStatement(sql);
            ps.setString(1, cod);
            rs = ps.executeQuery();

            if (rs.next()) {
                pro.setId(rs.getInt("id"));
                pro.setCodigo(rs.getString("codigo"));
                pro.setDescripcion(rs.getString("descripcion"));
                pro.setPrecio(rs.getBigDecimal("precio"));
                pro.setStock(rs.getBigDecimal("stock"));
                pro.setProveedor(rs.getString("proveedor"));
                pro.setIdCategoria(rs.getInt("id_categoria"));
                pro.setIdCategoria(rs.getInt("id_talle"));
                pro.setIdCategoria(rs.getInt("id_color"));
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        }

        return pro;
    }

    public boolean ActualizarProductos(Productos pro) {
        String sql = "UPDATE productos SET descripcion=?, stock=?, proveedor=? WHERE codigo=?";
        try {
            con = cn.conectar();
            ps = con.prepareStatement(sql);
            ps.setString(1, pro.getDescripcion());
            ps.setBigDecimal(2, pro.getPrecio());
            ps.setBigDecimal(3, pro.getStock());
            ps.setString(4, pro.getProveedor());
            ps.setString(5, pro.getCodigo());
            ps.setInt(6, pro.getIdCategoria());
            ps.setInt(7, pro.getIdTalle());
            ps.setInt(6, pro.getIdColor());

            ps.execute();
            return true;

        } catch (SQLException e) {
            System.out.println(e.toString());
            return false;
        }
    }

    public boolean EliminarProducto(String codigo) {
        String sql = "DELETE FROM productos WHERE codigo=?";
        try {
            con = cn.conectar();
            ps = con.prepareStatement(sql);
            ps.setString(1, codigo);
            ps.execute();
            return true;

        } catch (SQLException e) {
            System.out.println(e.toString());
            return false;
        }
    }

    public boolean ActualizarStock(BigDecimal nuevoStock, String codigo) {
        String sql = "UPDATE productos SET stock=? WHERE codigo=?";

        try {
            con = cn.conectar();
            ps = con.prepareStatement(sql);
            ps.setBigDecimal(1, nuevoStock);
            ps.setString(2, codigo);
            ps.execute();
            return true;

        } catch (SQLException e) {
            System.out.println(e.toString());
            return false;
        }
    }

    public Config BuscarDatos() {
        Config conf = new Config();
        String sql = "SELECT * FROM config";

        try {

            con = cn.conectar();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

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

    public List<Productos> buscarPorDescripcion(String descripcion) {
        List<Productos> lista = new ArrayList<>();
        if (descripcion == null) {
            descripcion = "";
        }

        String sql = "SELECT * FROM productos WHERE LOWER(descripcion) LIKE LOWER(?)";

        try {
            con = cn.conectar();
            ps = con.prepareStatement(sql);
            ps.setString(1, "%" + descripcion + "%");
            rs = ps.executeQuery();

            while (rs.next()) {
                Productos p = new Productos();
                p.setId(rs.getInt("id"));
                p.setCodigo(rs.getString("codigo"));
                p.setDescripcion(rs.getString("descripcion"));
                p.setStock(rs.getBigDecimal("stock"));
                p.setPrecio(rs.getBigDecimal("precio"));
                p.setIdCategoria(rs.getInt("id_categoria"));
                p.setIdCategoria(rs.getInt("id_talle"));
                p.setIdCategoria(rs.getInt("id_color"));
                lista.add(p);
            }

            rs.close();
        } catch (SQLException e) {
            System.out.println("Error buscarPorDescripcion: " + e.toString());
        }

        return lista;
    }

    public void ConsultarProveedor(JComboBox proveedor) {
        String sql = "SELECT nombre FROM proveedor";

        try {
            con = cn.conectar();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                proveedor.addItem(rs.getString("nombre"));
            }

        } catch (SQLException e) {
            System.out.println(e.toString());
        }
    }

    public boolean ModificarProductos(Productos pro) {
        String sql = "UPDATE productos SET codigo=?, descripcion=?, proveedor=?, stock=?, precio=?, id_categoria=?, id_talle=?, id_color=? WHERE id=?";

        try {
            con = cn.conectar();
            ps = con.prepareStatement(sql);

            ps.setString(1, pro.getCodigo());
            ps.setString(2, pro.getDescripcion());
            ps.setString(3, pro.getProveedor());
            ps.setBigDecimal(4, pro.getStock());
            ps.setBigDecimal(5, pro.getPrecio());
            ps.setInt(6, pro.getIdCategoria());
            ps.setInt(7, pro.getIdTalle());
            ps.setInt(8, pro.getIdColor());
            ps.setInt(9, pro.getId());
            ps.execute();
            return true;

        } catch (SQLException e) {
            System.out.println(e.toString());
            return false;
        }
    }

    public boolean actualizarPrecioPorDolar(BigDecimal valorDolar) {
        String sql = "UPDATE productos SET precioxdolar = precio * ?";

        try {
            con = cn.conectar();
            ps = con.prepareStatement(sql);
            ps.setBigDecimal(1, valorDolar);
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Error actualizarPrecioPorDolar: " + e.toString());
            return false;
        }
    }

    public BigDecimal obtenerUltimoValorDolar() {
        String sql = "SELECT valor_peso_argentino FROM tipodecambio ORDER BY id DESC LIMIT 1";

        try {
            con = cn.conectar();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getBigDecimal(1);
            }
        } catch (SQLException e) {
            System.out.println("Error obtenerUltimoValorDolar: " + e.getMessage());
        }
        return BigDecimal.ONE.setScale(2, RoundingMode.HALF_UP);
    }

    public boolean registrarTipoDeCambio(BigDecimal valorPesoArg) {
        String sql = "INSERT INTO tipodecambio (fecha, valor_peso_argentino) VALUES (NOW(), ?)";

        try {
            con = cn.conectar();
            ps = con.prepareStatement(sql);
            ps.setBigDecimal(1, valorPesoArg);
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Error registrarTipoDeCambio: " + e.toString());
            return false;
        }
    }

    public boolean EliminarProducto(int id) {
        String sql = "DELETE FROM productos WHERE id = ?";

        try {
            con = cn.conectar();
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ps.execute();
            return true;

        } catch (SQLException e) {
            System.out.println(e.toString());
            return false;
        }
    }

    public Productos BuscarProductoPorId(int idProd) {
        Productos pro = new Productos();
        String sql = "SELECT * FROM productos WHERE id = ?";

        try {
            con = cn.conectar();
            ps = con.prepareStatement(sql);
            ps.setInt(1, idProd);
            rs = ps.executeQuery();

            if (rs.next()) {
                pro.setId(rs.getInt("id"));
                pro.setCodigo(rs.getString("codigo"));
                pro.setDescripcion(rs.getString("descripcion"));
                pro.setPrecio(rs.getBigDecimal("precio"));
                pro.setStock(rs.getBigDecimal("stock"));
                pro.setProveedor(rs.getString("proveedor"));
                pro.setIdCategoria(rs.getInt("id_categoria"));
                pro.setIdTalle(rs.getInt("id_talle"));
                pro.setIdColor(rs.getInt("id_color"));
            }

        } catch (SQLException e) {
            System.out.println("Error BuscarProductoPorId: " + e.toString());
        }

        return pro;
    }

    public List<Productos> buscarPorCodigoDescripcion(String filtro) {
        List<Productos> lista = new ArrayList<>();

        String sql = "SELECT * FROM productos "
                + "WHERE LOWER(codigo) LIKE ? OR LOWER(descripcion) LIKE ? "
                + "ORDER BY descripcion ASC";

        try {
            con = cn.conectar();
            ps = con.prepareStatement(sql);
            ps.setString(1, "%" + filtro + "%");
            ps.setString(2, "%" + filtro + "%");
            rs = ps.executeQuery();

            while (rs.next()) {
                Productos p = new Productos();

                p.setId(rs.getInt("id"));
                p.setCodigo(rs.getString("codigo"));
                p.setDescripcion(rs.getString("descripcion"));
                p.setStock(rs.getBigDecimal("stock"));
                p.setPrecio(rs.getBigDecimal("precio"));
                p.setProveedor(rs.getString("proveedor"));

                lista.add(p);
            }

        } catch (Exception e) {
            System.out.println("Error en buscarPorCodigoDescripcion: " + e.getMessage());
        }

        return lista;
    }

    public boolean sumarStock(String codigo, BigDecimal cantidad) {

        String sql = "UPDATE productos SET stock = stock + ? WHERE codigo = ?";

        try (Connection con = cn.conectar(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setBigDecimal(1, cantidad);
            ps.setString(2, codigo);
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Error sumarStock: " + e.getMessage());
            return false;
        }
    }

   
    
    public String generarCodigoConPrefijo(int idCategoria, String nombreCat) {
        // Tomamos las primeras 3 letras de la categoría como prefijo
        String prefijo = (nombreCat.length() >= 3) ? nombreCat.substring(0, 3).toUpperCase() : nombreCat.toUpperCase();

        // Buscamos el número más alto que empiece con ese prefijo
        String sql = "SELECT codigo FROM productos WHERE id_categoria = ? ORDER BY id DESC LIMIT 1";
        int correlativo = 1;

        try (Connection con = cn.conectar(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idCategoria);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String ultimoCodigo = rs.getString("codigo"); // Ejemplo: "PAN-005"
                // Separamos el número después del guion
                String[] partes = ultimoCodigo.split("-");
                if (partes.length > 1) {
                    correlativo = Integer.parseInt(partes[1]) + 1;
                }
            }
        } catch (Exception e) {
            System.out.println("Error al generar código por categoría: " + e.getMessage());
        }

        // Retorna formato PRE-001, PRE-002, etc.
        return String.format("%s-%03d", prefijo, correlativo);
    }

}

