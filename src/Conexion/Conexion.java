/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Conexion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Manuel
 */
public class Conexion {
    private final String base = "bd_sistema_ventas";
 
    // Cambiar su usuario y contraseña
    private final String user = "root";
    private final String password = "root1234";
    
    // Cambiar el puerto
    private final String url = "jdbc:mysql://localhost:2526/"+base;
    private Connection conexion;
    
    public Conexion(){   
    }
    
    public  Connection getConexion(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            conexion = (Connection) DriverManager.getConnection(this.url,this.user,this.password);
            return conexion;
        } catch(SQLException e){
            System.err.println(e);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
        return conexion;
    }
}
