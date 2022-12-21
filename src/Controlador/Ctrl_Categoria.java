package Controlador;

import Modelo.entidad.Categoria;
import Conexion.Conexion;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.SQLException;

public class Ctrl_Categoria extends Conexion  {

    //Método para registrar categoría
    public boolean guardar(Categoria objeto) {
        boolean respuesta = false;
        Connection cn = getConexion();
        try {

            PreparedStatement consulta = cn.prepareStatement("insert into tb_categoria values(?,?,?)");
            consulta.setInt(1, 0);
            consulta.setString(2, objeto.getDescripcion());
            consulta.setInt(3, objeto.getEstado());

            if (consulta.executeUpdate() > 0) {
                respuesta = true;
            }

            cn.close();

        } catch (SQLException e) {
            System.out.println("Error al guardar categoría: " + e);
        }

        return respuesta;
    }

    //Evalúa si la categoría ya existe o no
    public boolean existeCategoria(String categoria) {
        boolean respuesta = false;

        String sql = "select descripcion from tb_categoria where descripcion = '" + categoria + "';";
        Statement st;

        try {

            Connection cn = getConexion();
            st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                respuesta = true;
            }

        } catch (SQLException e) {
            System.out.println("Error al consultar la categoría: " + e);
        }

        return respuesta;
    }

    //Método para actualizar categoría
    public boolean actualizar(Categoria objeto, int idCategoria) {
        boolean respuesta = false;
        Connection cn = getConexion();
        try {

            PreparedStatement consulta = cn.prepareStatement("update tb_categoria set descripcion=? where idCategoria = '" + idCategoria + "'");
            consulta.setString(1, objeto.getDescripcion());

            if (consulta.executeUpdate() > 0) {
                respuesta = true;
            }

            cn.close();

        } catch (SQLException e) {
            System.out.println("Error al actualizar categoría: " + e);
        }

        return respuesta;
    }

    //Método para eliminar categoría
    public boolean eliminar(int idCategoria) {
        boolean respuesta = false;
        Connection cn = getConexion();
        try {

            PreparedStatement consulta = cn.prepareStatement("delete from tb_categoria where idCategoria = '" + idCategoria + "'");
            consulta.executeUpdate();

            if (consulta.executeUpdate() > 0) {
                respuesta = true;
            }

            cn.close();

        } catch (SQLException e) {
            System.out.println("Error al eliminar categoría: " + e);
        }

        return respuesta;
    }
}
