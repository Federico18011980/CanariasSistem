package Modelo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import Conexion.ConexionMysql;
import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.poi.ss.usermodel.*;

public class EditarProductosDao {

    private final ConexionMysql cn = new ConexionMysql();

    // 🔹 LISTAR TODOS
    public List<Productos> listarTodos() {
        List<Productos> lista = new ArrayList<>();

        String sql = "  SELECT p.id, p.codigo, p.descripcion, p.precio, p.stock,\n"
                + "                   p.proveedor,\n"
                + "                   c.nombre AS categoria,\n"
                + "                   t.nombre AS talle,\n"
                + "                   co.nombre AS color\n"
                + "            FROM productos p\n"
                + "            LEFT JOIN categorias c ON p.id_categoria = c.id\n"
                + "            LEFT JOIN talles t ON p.id_talle = t.id\n"
                + "            LEFT JOIN colores co ON p.id_color = co.id\n"
                + "            ORDER BY p.descripcion";

        try (Connection con = cn.conectar(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Productos p = new Productos();

                p.setId(rs.getInt("id"));
                p.setCodigo(rs.getString("codigo"));
                p.setDescripcion(rs.getString("descripcion"));
                p.setPrecio(rs.getBigDecimal("precio"));
                p.setStock(rs.getBigDecimal("stock"));
                p.setProveedor(rs.getString("proveedor"));
                p.setCategoriaNombre(rs.getString("categoria"));
                p.setTalleNombre(rs.getString("talle"));
                p.setColorNombre(rs.getString("color"));

                lista.add(p);
            }

        } catch (Exception e) {
            System.out.println("Error listarTodos: " + e.getMessage());
        }

        return lista;
    }

    // 🔹 FILTRO GENERAL
    public List<Productos> listarPorFiltro(String filtro) {
        List<Productos> lista = new ArrayList<>();

        String sql = "  SELECT * FROM productos\n"
                + "            WHERE LOWER(descripcion) LIKE ?\n"
                + "               OR LOWER(codigo) LIKE ?\n"
                + "            ORDER BY descripcion";

        try (Connection con = cn.conectar(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, "%" + filtro.toLowerCase() + "%");
            ps.setString(2, "%" + filtro.toLowerCase() + "%");

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Productos p = new Productos();
                p.setId(rs.getInt("id"));
                p.setCodigo(rs.getString("codigo"));
                p.setDescripcion(rs.getString("descripcion"));
                p.setPrecio(rs.getBigDecimal("precio"));
                p.setStock(rs.getBigDecimal("stock"));
                p.setProveedor(rs.getString("proveedor"));
                lista.add(p);
            }

        } catch (Exception e) {
            System.out.println("Error listarPorFiltro: " + e.getMessage());
        }

        return lista;
    }

    // 🔹 ACTUALIZAR UN PRODUCTO
    public boolean actualizarProducto(Productos p) {

        String sql = "UPDATE productos \n"
                + "            SET descripcion = ?, precio = ?, stock = ?, proveedor = ?\n"
                + "            WHERE id = ?";

        try (Connection con = cn.conectar(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, p.getDescripcion());
            ps.setBigDecimal(2, p.getPrecio());
            ps.setBigDecimal(3, p.getStock());
            ps.setString(4, p.getProveedor());
            ps.setInt(5, p.getId());

            ps.executeUpdate();
            return true;

        } catch (Exception e) {
            System.out.println("Error actualizarProducto: " + e.getMessage());
            return false;
        }
    }

    // 🔹 ACTUALIZACIÓN MASIVA (CLAVE)
    public boolean actualizarMasivo(List<Productos> lista) {

        String sql = "\n"
                + "            UPDATE productos \n"
                + "            SET precio = ?, stock = ?, proveedor = ?\n"
                + "            WHERE id = ?";

        try (Connection con = cn.conectar(); PreparedStatement ps = con.prepareStatement(sql)) {

            con.setAutoCommit(false);

            for (Productos p : lista) {
                ps.setBigDecimal(1, p.getPrecio());
                ps.setBigDecimal(2, p.getStock());
                ps.setString(3, p.getProveedor());
                ps.setInt(4, p.getId());
                ps.addBatch();
            }

            ps.executeBatch();
            con.commit();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Productos> listarFiltrados(Integer idCategoria, String proveedor) {

        List<Productos> lista = new ArrayList<>();

        StringBuilder sql = new StringBuilder(
                "SELECT p.id, p.codigo, p.descripcion, p.precio, p.stock, "
                + "p.proveedor, p.id_categoria, p.id_talle, p.id_color "
                + "FROM productos p WHERE 1=1 "
        );

        if (idCategoria != null) {
            sql.append(" AND p.id_categoria = ? ");
        }

        if (proveedor != null && !proveedor.isEmpty()) {
            sql.append(" AND p.proveedor = ? ");
        }

        try {
            Connection con = cn.conectar();
            PreparedStatement ps = con.prepareStatement(sql.toString());

            int index = 1;

            if (idCategoria != null) {
                ps.setInt(index++, idCategoria);
            }

            if (proveedor != null && !proveedor.isEmpty()) {
                ps.setString(index++, proveedor);
            }

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Productos p = new Productos();

                p.setId(rs.getInt("id"));
                p.setCodigo(rs.getString("codigo"));
                p.setDescripcion(rs.getString("descripcion"));
                p.setPrecio(rs.getBigDecimal("precio"));
                p.setStock(rs.getBigDecimal("stock"));
                p.setProveedor(rs.getString("proveedor"));

                // 🔥 ESTO YA NO ROMPE
                p.setIdCategoria(rs.getInt("id_categoria"));
                p.setIdTalle(rs.getInt("id_talle"));
                p.setIdColor(rs.getInt("id_color"));

                lista.add(p);
            }

        } catch (SQLException e) {
            System.out.println("Error listarFiltrados: " + e.getMessage());
        }

        return lista;
    }

    public Map<Integer, String> listarCategorias() {

        Map<Integer, String> map = new LinkedHashMap<Integer, String>();

        String sql = "SELECT id, nombre FROM categorias ORDER BY nombre";

        try (Connection con = cn.conectar(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                map.put(rs.getInt("id"), rs.getString("nombre"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return map;
    }

    public List<String> listarProveedores() {

        List<String> lista = new ArrayList<String>();

        String sql = "SELECT DISTINCT proveedor FROM productos ORDER BY proveedor";

        try (Connection con = cn.conectar(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(rs.getString("proveedor"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

public boolean importarDesdeExcel(File archivo) {

    String sqlExiste = "SELECT id FROM productos WHERE codigo = ?";
    String sqlInsert = " INSERT INTO productos\n" +
"        (codigo, descripcion, precio, stock, proveedor, id_categoria, id_talle, id_color)\n" +
"        VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    String sqlUpdate = " UPDATE productos SET\n" +
"        descripcion = ?, precio = ?, stock = ?, proveedor = ?,\n" +
"        id_categoria = ?, id_talle = ?, id_color = ?\n" +
"        WHERE codigo = ?";

    try (Connection con = cn.conectar();
         FileInputStream fis = new FileInputStream(archivo);
         Workbook wb = WorkbookFactory.create(fis)) {

        con.setAutoCommit(false);

        Sheet sheet = wb.getSheetAt(0);

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {

            Row row = sheet.getRow(i);
            if (row == null) continue;

            String codigo = getString(row.getCell(1));
            String desc = getString(row.getCell(2));
            BigDecimal precio = getDecimal(row.getCell(3));
            int stock = getInt(row.getCell(4));
            String proveedor = getString(row.getCell(5));
            String categoria = getString(row.getCell(6));
            String talle = getString(row.getCell(7));
            String color = getString(row.getCell(8));

            Integer idCategoria = obtenerIdCategoria(con, categoria);
            Integer idTalle = obtenerIdTalle(con, talle);
            Integer idColor = obtenerIdColor(con, color);

            PreparedStatement check = con.prepareStatement(sqlExiste);
            check.setString(1, codigo);
            ResultSet rs = check.executeQuery();

            if (rs.next()) {
                PreparedStatement ps = con.prepareStatement(sqlUpdate);
                ps.setString(1, desc);
                ps.setBigDecimal(2, precio);
                ps.setInt(3, stock);
                ps.setString(4, proveedor);
               // ps.setInt(5, idCategoria);
                if (idCategoria != null)
                    ps.setInt(5, idCategoria);
                else
                    ps.setNull(5, Types.INTEGER);

                //ps.setInt(6, idTalle);
                if (idTalle != null) {
                    ps.setInt(6, idTalle);
                } else {
                    ps.setNull(6, Types.INTEGER);
                }

                //ps.setInt(7, idColor);
                if (idColor != null)
                    ps.setInt(7, idColor);
                else
                    ps.setNull(7, Types.INTEGER);

                ps.setString(8, codigo);
                ps.executeUpdate();
            } else {
                PreparedStatement ps = con.prepareStatement(sqlInsert);
                ps.setString(1, codigo);
                ps.setString(2, desc);
                ps.setBigDecimal(3, precio);
                ps.setInt(4, stock);
                ps.setString(5, proveedor);
                //ps.setInt(6, idCategoria);
                 if (idCategoria != null)
                    ps.setInt(6, idCategoria);
                else
                    ps.setNull(6, Types.INTEGER);
                //ps.setInt(7, idTalle);
                 if (idTalle != null) {
                    ps.setInt(7, idTalle);
                } else {
                    ps.setNull(7, Types.INTEGER);
                }
              //  ps.setInt(8, idColor);
              if (idColor != null)
                    ps.setInt(8, idColor);
                else
                    ps.setNull(8, Types.INTEGER);  
              ps.executeUpdate();
            }
        }

        con.commit();
        return true;

    } catch (Exception e) {
        e.printStackTrace();
        return false;
    }
}

    private void guardarOActualizar(Productos p) throws SQLException {

        String sqlExiste = "SELECT id FROM productos WHERE codigo = ?";
        Connection con = cn.conectar();
        PreparedStatement ps = con.prepareStatement(sqlExiste);
        ps.setString(1, p.getCodigo());

        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            // UPDATE
            String update = " UPDATE productos SET\n"
                    + "                descripcion=?,\n"
                    + "                precio=?,\n"
                    + "                stock=?,\n"
                    + "                proveedor=?,\n"
                    + "                id_categoria=?,\n"
                    + "                id_talle=?,\n"
                    + "                id_color=?\n"
                    + "            WHERE codigo=?";

            PreparedStatement psUpdate = con.prepareStatement(update);
            psUpdate.setString(1, p.getDescripcion());
            psUpdate.setBigDecimal(2, p.getPrecio());
            psUpdate.setBigDecimal(3, p.getStock());
            psUpdate.setString(4, p.getProveedor());
            psUpdate.setInt(5, p.getIdCategoria());
            psUpdate.setInt(6, p.getIdTalle());
            psUpdate.setInt(7, p.getIdColor());
            psUpdate.setString(8, p.getCodigo());
            psUpdate.executeUpdate();

        } else {
            // INSERT
            String insert = "INSERT INTO productos\n"
                    + "            (codigo, descripcion, precio, stock, proveedor, id_categoria, id_talle, id_color)\n"
                    + "            VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement psInsert = con.prepareStatement(insert);
            psInsert.setString(1, p.getCodigo());
            psInsert.setString(2, p.getDescripcion());
            psInsert.setBigDecimal(3, p.getPrecio());
            psInsert.setBigDecimal(4, p.getStock());
            psInsert.setString(5, p.getProveedor());
            psInsert.setInt(6, p.getIdCategoria());
            psInsert.setInt(7, p.getIdTalle());
            psInsert.setInt(8, p.getIdColor());
            psInsert.executeUpdate();
        }
    }

    public List<ImportError> validarExcel(File archivo) {

        List<ImportError> errores = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(archivo); Workbook workbook = WorkbookFactory.create(fis)) {

            Sheet hoja = workbook.getSheetAt(0);

            int filaNumero = 1;

            for (Row fila : hoja) {

                if (fila.getRowNum() == 0) {
                    continue;
                }

                Cell cCodigo = fila.getCell(1);
                Cell cDescripcion = fila.getCell(2);
                Cell cPrecio = fila.getCell(3);
                Cell cStock = fila.getCell(4);

                if (!esTexto(cCodigo)) {
                    errores.add(new ImportError(filaNumero, "Código", "Código inválido"));
                }

                if (!esTexto(cDescripcion)) {
                    errores.add(new ImportError(filaNumero, "Descripción", "Descripción inválida"));
                }

                if (!esNumero(cPrecio)) {
                    errores.add(new ImportError(filaNumero, "Precio", "Debe ser numérico"));
                }

                if (!esNumero(cStock)) {
                    errores.add(new ImportError(filaNumero, "Stock", "Debe ser numérico"));
                }

                filaNumero++;
            }

        } catch (Exception e) {
            errores.add(new ImportError(0, "Archivo", "No se pudo leer el archivo"));
            e.printStackTrace();
        }

        return errores;
    }

    public List<PreviewProducto> leerPreviewExcel(File archivo) {

        List<PreviewProducto> lista = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(archivo); Workbook wb = WorkbookFactory.create(fis)) {

            Sheet sheet = wb.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {

                Row row = sheet.getRow(i);
                if (row == null || row.getCell(1) == null) {
                    continue;
                }

                String codigo = getString(row.getCell(1));
                String descripcion = getString(row.getCell(2));
                BigDecimal precio = getDecimal(row.getCell(3));
                int stock = getInt(row.getCell(4));
                String proveedor = getString(row.getCell(5));
                String categoria = getString(row.getCell(6));
                String talle = getString(row.getCell(7));
                String color = getString(row.getCell(8));

                lista.add(new PreviewProducto(
                        codigo,
                        descripcion,
                        precio,
                        stock,
                        proveedor,
                        categoria,
                        talle,
                        color
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    private boolean esNumero(Cell cell) {
        if (cell == null) {
            return false;
        }

        DataFormatter formatter = new DataFormatter();
        String valor = formatter.formatCellValue(cell);

        if (valor == null || valor.trim().isEmpty()) {
            return false;
        }

        // Normalizar decimal
        valor = valor.replace(".", "").replace(",", ".");

        try {
            new BigDecimal(valor);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean esTexto(Cell cell) {
        if (cell == null) {
            return false;
        }

        DataFormatter formatter = new DataFormatter();
        String valor = formatter.formatCellValue(cell);

        return valor != null && !valor.trim().isEmpty();
    }

    private String getString(Cell cell) {
        if (cell == null) {
            return "";
        }
        DataFormatter f = new DataFormatter();
        return f.formatCellValue(cell).trim();
    }

    private BigDecimal getDecimal(Cell cell) {
        if (cell == null) {
            return BigDecimal.ZERO;
        }
        DataFormatter f = new DataFormatter();
        String v = f.formatCellValue(cell).replace(".", "").replace(",", ".");
        return new BigDecimal(v);
    }

    private int getInt(Cell cell) {
        if (cell == null) {
            return 0;
        }
        DataFormatter f = new DataFormatter();
        String v = f.formatCellValue(cell);
        return Integer.parseInt(v);
    }

   
    private int obtenerIdCategoria(Connection con, String nombre) throws SQLException {

        if (nombre == null || nombre.trim().isEmpty()) {
           return 12;
        }
    //   if (nombre == null || nombre.trim().isEmpty()) {return null;}


        String sqlBuscar = "SELECT id FROM categorias WHERE nombre = ?";
        try (PreparedStatement ps = con.prepareStatement(sqlBuscar)) {
            ps.setString(1, nombre);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }

        String sqlInsert = "INSERT INTO categorias (nombre) VALUES (?)";
        try (PreparedStatement ps = con.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, nombre);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }

        return 0;
    }

    private int obtenerIdTalle(Connection con, String nombre) throws SQLException {

        if (nombre == null || nombre.trim().isEmpty()) {
            return 29;
        }

        String sqlBuscar = "SELECT id FROM talles WHERE nombre = ?";
        try (PreparedStatement ps = con.prepareStatement(sqlBuscar)) {
            ps.setString(1, nombre);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }

        String sqlInsert = "INSERT INTO talles (nombre) VALUES (?)";
        try (PreparedStatement ps = con.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, nombre);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }

        return 0;
    }

    private int obtenerIdColor(Connection con, String nombre) throws SQLException {

        if (nombre == null || nombre.trim().isEmpty()) {
            return 2;
        }

        String sqlBuscar = "SELECT id FROM colores WHERE nombre = ?";
        try (PreparedStatement ps = con.prepareStatement(sqlBuscar)) {
            ps.setString(1, nombre);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }

        String sqlInsert = "INSERT INTO colores (nombre) VALUES (?)";
        try (PreparedStatement ps = con.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, nombre);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }

        return 0;
    }

}
