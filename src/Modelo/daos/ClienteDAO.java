/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo.daos;

import Modelo.entidad.Cliente;
import Conexion.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Antonia
 */
public class ClienteDAO extends Conexion {
    Connection con;
    PreparedStatement ps;
    ResultSet rs;
    
    public boolean registrarCliente (Cliente c){
        String sql = "INSERT INTO tb_cliente (nombre,apellido,dni,telefono,direccion,estado) VALUES(?,?,?,?,?,?)";
        
        try{
           con = getConexion();
           ps = con.prepareStatement(sql);
          
           ps.setString(1,c.getNombre());
           ps.setString(2,c.getApellido());
           ps.setString(3,c.getDni());
           ps.setString(4,c.getTelefono());
           ps.setString(5,c.getDireccion());
           ps.setInt(6,c.getEstado());
           ps.execute();
           
           return true;
        } catch (SQLException e){
            System.out.println(e);    
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                System.err.println(e);
            }
        }
        return false;
    } 
    
     public boolean  modificarCliente (Cliente c){
        String sql = "UPDATE tb_cliente SET nombre=?,apellido=?,telefono=?,direccion=?,estado=? WHERE dni=?";
        
        try{
           con = getConexion();
           ps = con.prepareStatement(sql);
          
           ps.setString(1,c.getNombre());
           ps.setString(2,c.getApellido());
           ps.setString(3,c.getTelefono());
           ps.setString(4,c.getDireccion());
           ps.setInt(5,c.getEstado());
           ps.setString(6,c.getDni());
           ps.execute();
           
           return true;
        } catch (SQLException e){
            System.out.println(e);    
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                System.err.println(e);
            }
        }
        return false;
    } 
     
     public boolean  eliminarCliente (Cliente c){
        String sql = "DELETE FROM tb_cliente WHERE dni=?";
        
        try{
           con = getConexion();
           ps = con.prepareStatement(sql);        
           ps.setString(1,c.getDni());
           ps.execute();
           
           return true;
        } catch (SQLException e){
            System.out.println(e);    
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                System.err.println(e);
            }
        }
        return false;
    } 
    
    public boolean  buscarCliente(Cliente c){
        ResultSet rs=null;
        String sql = "SELECT * FROM tb_cliente WHERE dni=?";
        
        try{
           con = getConexion();
           ps = con.prepareStatement(sql);  
           ps.setString(1,c.getDni());
           rs = ps.executeQuery();
           
           if(rs.next()){
               c.setIdCliente(Integer.parseInt(rs.getString("idCliente")));
               c.setNombre(rs.getString("nombre"));
               c.setApellido(rs.getString("apellido"));
               c.setDni(rs.getString("dni"));
               c.setTelefono(rs.getString("telefono"));
               c.setDireccion(rs.getString("direccion"));
               c.setEstado(Integer.parseInt(rs.getString("estado")));
               return true;
           }           
           return false;
        } catch (SQLException e){
            System.out.println(e);    
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                System.err.println(e);
            }
        }
        return false;
    } 
     
    public List listarCliente(){
        List<Cliente> datos = new ArrayList<>();
        PreparedStatement ps=null;
        ResultSet rs = null;
        String sql = "SELECT * FROM tb_cliente";
        try{
           con = getConexion();
           ps = con.prepareStatement(sql);
           rs = ps.executeQuery(); 

           while (rs.next()){
               Cliente cli = new Cliente();
               cli.setIdCliente(Integer.parseInt(rs.getString(1)));
               cli.setNombre(rs.getString(2));
               cli.setApellido(rs.getString(3));
               cli.setDni(rs.getString(4));
               cli.setTelefono(rs.getString(5));
               cli.setDireccion(rs.getString(6));
              // cli.setEstado(Integer.parseInt(rs.getString(7)));
               
               datos.add(cli);              
               
           }
        }catch (SQLException e){
            System.out.println(e);
        }
        return datos;
    }
  
  
}
        
  