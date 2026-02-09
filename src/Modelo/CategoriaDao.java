package Modelo;

import Conexion.ConexionMysql;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComboBox;

public class CategoriaDao {

    Connection con;
    PreparedStatement ps;
    ResultSet rs;
    ConexionMysql cn = new ConexionMysql();

    public boolean registrar(Categoria cat) {
        String sql = "INSERT INTO categorias (nombre) VALUES (?)";

        try {
            con = cn.conectar();
            ps = con.prepareStatement(sql);
            ps.setString(1, cat.getNombre());
            ps.execute();
            return true;

        } catch (SQLException e) {
            System.out.println("Error registrar categoría: " + e.getMessage());
            return false;

        } finally {
            try { con.close(); } catch (SQLException ex) {}
        }
    }

    public List<Categoria> listar() {
        List<Categoria> lista = new ArrayList<>();
        String sql = "SELECT * FROM categorias ORDER BY nombre ASC";

        try {
            con = cn.conectar();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Categoria c = new Categoria();
                c.setId(rs.getInt("id"));
                c.setNombre(rs.getString("nombre"));
                lista.add(c);
            }

        } catch (SQLException e) {
            System.out.println("Error listar categorías: " + e.getMessage());
        }
        return lista;
    }

    public boolean modificar(Categoria cat) {
        String sql = "UPDATE categorias SET nombre=? WHERE id=?";

        try {
            con = cn.conectar();
            ps = con.prepareStatement(sql);
            ps.setString(1, cat.getNombre());
            ps.setInt(2, cat.getId());
            ps.execute();
            return true;

        } catch (SQLException e) {
            System.out.println("Error modificar categoría: " + e.getMessage());
            return false;

        } finally {
            try { con.close(); } catch (SQLException ex) {}
        }
    }

    public boolean eliminar(int id) {
        String sql = "DELETE FROM categorias WHERE id=?";

        try {
            con = cn.conectar();
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ps.execute();
            return true;

        } catch (SQLException e) {
            System.out.println("Error eliminar categoría: " + e.getMessage());
            return false;

        } finally {
            try { con.close(); } catch (SQLException ex) {}
        }
    }

    public void cargarCombo(JComboBox combo) {
        combo.removeAllItems();

        String sql = "SELECT nombre FROM categorias ORDER BY nombre ASC";

        try {
            con = cn.conectar();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                combo.addItem(rs.getString("nombre"));
            }

        } catch (SQLException e) {
            System.out.println("Error cargar combo categorías: " + e.getMessage());
        }
    }

    public Categoria buscarPorId(int id) {
        Categoria c = new Categoria();
        String sql = "SELECT * FROM categorias WHERE id=? LIMIT 1";

        try {
            con = cn.conectar();
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();

            if (rs.next()) {
                c.setId(rs.getInt("id"));
                c.setNombre(rs.getString("nombre"));
            }

        } catch (SQLException e) {
            System.out.println("Error buscar categoría: " + e.getMessage());
        }
        return c;
    }
}
