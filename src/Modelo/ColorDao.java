package Modelo;

import Conexion.ConexionMysql;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComboBox;

public class ColorDao {

    Connection con;
    ConexionMysql cn = new ConexionMysql();
    PreparedStatement ps;
    ResultSet rs;

    // Registrar color
    public boolean Registrar(Color col) {
        String sql = "INSERT INTO colores (nombre) VALUES (?)";
        try {
            con = cn.conectar();
            ps = con.prepareStatement(sql);
            ps.setString(1, col.getNombre());
            ps.execute();
            return true;
        } catch (SQLException e) {
            System.out.println("Registrar Color: " + e.toString());
            return false;
        }
    }

    // Listar todos los colores
    public List<Color> Listar() {
        List<Color> lista = new ArrayList<>();
        String sql = "SELECT * FROM colores";

        try {
            con = cn.conectar();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Color col = new Color();
                col.setId(rs.getInt("id"));
                col.setNombre(rs.getString("nombre"));
                lista.add(col);
            }
        } catch (SQLException e) {
            System.out.println("Listar Colores: " + e.toString());
        }

        return lista;
    }

    // Buscar color por ID
    public Color Buscar(int id) {
        Color col = new Color();
        String sql = "SELECT * FROM colores WHERE id=?";

        try {
            con = cn.conectar();
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();

            if (rs.next()) {
                col.setId(rs.getInt("id"));
                col.setNombre(rs.getString("nombre"));
            }
        } catch (SQLException e) {
            System.out.println("Buscar Color: " + e.toString());
        }

        return col;
    }

    // Modificar color
    public boolean Modificar(Color col) {
        String sql = "UPDATE colores SET nombre=? WHERE id=?";

        try {
            con = cn.conectar();
            ps = con.prepareStatement(sql);
            ps.setString(1, col.getNombre());
            ps.setInt(2, col.getId());
            ps.execute();
            return true;

        } catch (SQLException e) {
            System.out.println("Modificar Color: " + e.toString());
            return false;
        }
    }

    // Eliminar color
    public boolean Eliminar(int id) {
        String sql = "DELETE FROM colores WHERE id=?";

        try {
            con = cn.conectar();
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ps.execute();
            return true;

        } catch (SQLException e) {
            System.out.println("Eliminar Color: " + e.toString());
            return false;
        }
    }

    // Cargar ComboBox
    public void CargarCombo(JComboBox box) {
        String sql = "SELECT nombre FROM colores";

        try {
            con = cn.conectar();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            box.removeAllItems();

            while (rs.next()) {
                box.addItem(rs.getString("nombre"));
            }

        } catch (SQLException e) {
            System.out.println("CargarCombo Color: " + e.toString());
        }
    }
}
