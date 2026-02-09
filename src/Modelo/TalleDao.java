package Modelo;

import Conexion.ConexionMysql;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComboBox;

public class TalleDao {

    Connection con;
    PreparedStatement ps;
    ResultSet rs;
    ConexionMysql cn = new ConexionMysql();

    public boolean registrar(Talle talle) {
        String sql = "INSERT INTO talles (nombre) VALUES (?)";

        try {
            con = cn.conectar();
            ps = con.prepareStatement(sql);
            ps.setString(1, talle.getNombre());
            ps.execute();
            return true;

        } catch (SQLException e) {
            System.out.println("Error registrar talle: " + e.getMessage());
            return false;

        } finally {
            try { con.close(); } catch (SQLException ex) {}
        }
    }

    public List<Talle> listar() {
        List<Talle> lista = new ArrayList<>();
        String sql = "SELECT * FROM talles ORDER BY nombre ASC";

        try {
            con = cn.conectar();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Talle t = new Talle();
                t.setId(rs.getInt("id"));
                t.setNombre(rs.getString("nombre"));
                lista.add(t);
            }

        } catch (SQLException e) {
            System.out.println("Error listar talles: " + e.getMessage());
        }
        return lista;
    }

    public boolean modificar(Talle talle) {
        String sql = "UPDATE talles SET nombre=? WHERE id=?";

        try {
            con = cn.conectar();
            ps = con.prepareStatement(sql);
            ps.setString(1, talle.getNombre());
            ps.setInt(2, talle.getId());
            ps.execute();
            return true;

        } catch (SQLException e) {
            System.out.println("Error modificar talle: " + e.getMessage());
            return false;

        } finally {
            try { con.close(); } catch (SQLException ex) {}
        }
    }

    public boolean eliminar(int id) {
        String sql = "DELETE FROM talles WHERE id=?";

        try {
            con = cn.conectar();
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ps.execute();
            return true;

        } catch (SQLException e) {
            System.out.println("Error eliminar talle: " + e.getMessage());
            return false;

        } finally {
            try { con.close(); } catch (SQLException ex) {}
        }
    }

    public void cargarCombo(JComboBox combo) {
        combo.removeAllItems();

        String sql = "SELECT nombre FROM talles ORDER BY nombre ASC";

        try {
            con = cn.conectar();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                combo.addItem(rs.getString("nombre"));
            }

        } catch (SQLException e) {
            System.out.println("Error cargar combo talles: " + e.getMessage());
        }
    }

    public Talle buscarPorId(int id) {
        Talle t = new Talle();
        String sql = "SELECT * FROM talles WHERE id=? LIMIT 1";

        try {
            con = cn.conectar();
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();

            if (rs.next()) {
                t.setId(rs.getInt("id"));
                t.setNombre(rs.getString("nombre"));
            }

        } catch (SQLException e) {
            System.out.println("Error buscar talle: " + e.getMessage());
        }
        return t;
    }
}
