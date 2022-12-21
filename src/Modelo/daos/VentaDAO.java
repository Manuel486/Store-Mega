/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo.daos;

import Modelo.entidad.Venta;
import Modelo.entidad.Detalle;
import Modelo.entidad.Producto;
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
 * @author Manuel
 */
public class VentaDAO extends Conexion {
    Connection con;
    PreparedStatement ps;
    ResultSet rs;
    
    public boolean registrarVenta(Venta v){
        String sql = "INSERT INTO tb_cabecera_venta (idCliente,valorPagar,fechaVenta,estado) VALUES (?,?,?,?)";
        try{
           con = getConexion();
           ps = con.prepareStatement(sql);
           ps.setInt(1, v.getIdCliente());
           ps.setDouble(2, v.getValorPagar());
           ps.setString(3,v.getFechaVenta());
           ps.setInt(4, v.getEstado());
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
    
    public boolean registrarDetalle(Detalle detalle){
        String sql = "INSERT INTO tb_detalle_venta (idCabeceraVenta,idProducto,cantidad,precioUnitario,subtotal,descuento,totalPagar,estado) VALUES (?,?,?,?,?,?,?,?)";
        try{
           con = getConexion();
           ps = con.prepareStatement(sql);
           ps.setInt(1, detalle.getIdCabeceraVenta());
           ps.setInt(2, detalle.getIdProducto());
           ps.setInt(3, detalle.getCantidad());
           ps.setDouble(4, detalle.getPrecioUnitario());
           ps.setDouble(5, detalle.getSubtotal());
           ps.setDouble(6, detalle.getDescuento());
           ps.setDouble(7, detalle.getTotalPagar());
           ps.setInt(8, detalle.getEstado());
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
    
    public List listarProducto(){
        List<Producto> datos=new ArrayList<>();
        String sql = "SELECT * FROM tb_producto";
        try{
           con = getConexion();
           ps = con.prepareStatement(sql);
           rs = ps.executeQuery(); 

           while (rs.next()){
               Producto p = new Producto();
               p.setIdProducto(Integer.parseInt(rs.getString(1)));
               p.setNombre(rs.getString(2));
               p.setCantidad(Integer.parseInt(rs.getString(3)));
               p.setPrecio(Double.parseDouble(rs.getString(4)));
               p.setDescripcion(rs.getString(5));
               p.setIgv(Integer.parseInt(rs.getString(  6)));
               p.setIdCategoria(Integer.parseInt(rs.getString(6)));
               p.setEstado(Integer.parseInt(rs.getString(7)));        
               datos.add(p);
               
           }
        } catch (SQLException e){
            System.out.println(e);
        }
        return datos;
    }
    
    public boolean buscarDNI(Cliente cliente){
        String sql = "SELECT * FROM tb_cliente WHERE dni=? ";

        try {
            con = getConexion();
            ps = con.prepareStatement(sql);
            ps.setString(1, cliente.getDni()); 
            rs = ps.executeQuery();
            if(rs.next()){
               cliente.setIdCliente(Integer.parseInt(rs.getString("idCliente")));
               cliente.setNombre(rs.getString("nombre"));
               cliente.setApellido(rs.getString("apellido"));
               cliente.setDni(rs.getString("dni"));
               cliente.setTelefono(rs.getString("telefono"));
               cliente.setDireccion(rs.getString("direccion"));
               cliente.setEstado(Integer.parseInt(rs.getString("estado")));
               return true;
            }
            return false;
        } catch (SQLException e) {
            System.err.println(e);
            return false;
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                System.err.println(e);
            }
        }
    }
    
    public int idVenta(){
        int id = 0;
        String sql = "SELECT MAX(idCabeceraVenta) FROM tb_cabecera_venta";
        try {
            con = getConexion();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();;
            if(rs.next()){
                id = rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println(e);

        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                System.err.println(e);
            }
        }
        
        return id;
    }
    
    public boolean actualizarStock(int cantidad,int idProducto){
        String sql = "UPDATE tb_producto SET cantidad=cantidad-? WHERE idProducto=?";
        try {
            con = getConexion();
            ps = con.prepareStatement(sql);
            ps.setInt(1, cantidad);
            ps.setInt(2, idProducto);
            ps.execute();
        } catch (SQLException e) {
            System.err.println(e);
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                System.err.println(e);
            }
        }
        return false;
    }
}
