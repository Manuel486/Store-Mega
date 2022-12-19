package Controlador;

import Util.Conexion;
import java.sql.Statement;
import java.sql.SQLException;
//import conexion.Conexion;
import java.sql.Connection;
import javax.swing.JOptionPane;
import Modelo.entidad.Usuario;
import java.sql.ResultSet;

public class Ctrl_Usuario extends Conexion {

    //Método de inicio de sesión
    public boolean loginUser(Usuario objeto) {

        boolean respuesta = false;

        Connection cn = getConexion();
        String sql = "select usuario, password from tb_usuario where usuario = '" + objeto.getUsuario() + "' and password = '" + objeto.getPassword() + "'";
        Statement st;
        try {
            st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                respuesta = true;
            }

        } catch (SQLException e) {
            System.out.println("Error al iniciar sesión");
            JOptionPane.showMessageDialog(null, "Error al iniciar sesión");
        }

        return respuesta;
    }
}
