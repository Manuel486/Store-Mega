/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Modelo.Cliente;
import Modelo.Detalle;
import Modelo.Producto;
import Modelo.Venta;
import Modelo.VentaDAO;
import Panels.PnlVenta;
import static Util.Util.modelo;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Manuel
 */
public class CtrlVenta implements ActionListener, ItemListener {

    private Venta venta = new Venta();
    private VentaDAO ventaDAO = new VentaDAO();
    private PnlVenta pnl;
    Date fecha = new Date();
    SimpleDateFormat formmato = new SimpleDateFormat("yyyy-MM-dd");

    //DefaultTableModel modelo = new DefaultTableModel();
    List<Producto> lista = ventaDAO.listarProducto();

    public CtrlVenta(PnlVenta pnl) {
        this.pnl = pnl;
        this.pnl.btnRegistrar.addActionListener(this);
        this.pnl.btnAgregar.addActionListener(this);
        this.pnl.btnEliminar.addActionListener(this);
        this.pnl.btnBuscarDni.addActionListener(this);
        this.pnl.cmbProductos.addItemListener(this);
        this.pnl.lblFecha.setText(this.formmato.format(fecha));
        this.pnl.lblVenta.setText(String.valueOf(ventaDAO.idVenta() + 1));
        this.pnl.lblID.setVisible(false);
        listarProductos(this.pnl.cmbProductos);
        recuperarInformacion();
    }

    public void recuperarInformacion() {
        if (modelo.getRowCount() > 0) {
            // Llenando tabla
            this.pnl.tblProductos.setModel(modelo); 
            // Actualizando lblTotalPagar
            totalPagar();
        }
    }

    public void agregarProducto(JTable tabla) {
        String cod = pnl.txtCodigo.getText();
        String descripcion = pnl.txtDescripcion.getText();
        int cantidad = Integer.parseInt(pnl.txtCantidad.getText());
        double precioUnitario = Double.parseDouble(pnl.txtPrecio.getText());
        double precioTotal = cantidad * precioUnitario;
        int stock = Integer.parseInt(pnl.txtStock.getText());

        if (stock >= cantidad) {
            modelo = (DefaultTableModel) tabla.getModel();
            ArrayList lista = new ArrayList();
            lista.add(cod);
            lista.add(descripcion);
            lista.add(cantidad);
            lista.add(precioUnitario);
            lista.add(precioTotal);

            Object[] objeto = new Object[5];
            objeto[0] = lista.get(0);
            objeto[1] = lista.get(1);
            objeto[2] = lista.get(2);
            objeto[3] = lista.get(3);
            objeto[4] = lista.get(4);
            modelo.addRow(objeto);
            tabla.setModel(modelo);
        } else {
            JOptionPane.showMessageDialog(null, "Stock no Disponible");
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == pnl.btnRegistrar) {
            int nfilas = pnl.tblProductos.getRowCount();
            if (!pnl.txtDNI.getText().equals("") && !pnl.txtNombre.getText().equals("") && nfilas > 0) {
                Venta venta1 = new Venta(
                        1,
                        Integer.parseInt(pnl.lblID.getText()),
                        Double.parseDouble(pnl.lblTotalAPagar.getText()),
                        this.formmato.format(fecha),
                        1,
                        15);

                if (ventaDAO.registrarVenta(venta1)) {
                    JOptionPane.showMessageDialog(null, "Venta registrada");
                    registrarDetalle();
                    limpiar();

                } else {
                    JOptionPane.showMessageDialog(null, "Error al Guardar");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Debe ingresar al menos un producto y completar los datos del cliente");
            }
        }

        if (e.getSource() == pnl.btnAgregar) {
            if (!pnl.txtCantidad.getText().equals("") && pnl.cmbProductos.getSelectedIndex() != 0) {
                agregarProducto(pnl.tblProductos);
                totalPagar();
            } else {
                JOptionPane.showMessageDialog(null, "Debe completar todos los campos");
            }
        }

        if (e.getSource() == pnl.btnBuscarDni) {
            if (!pnl.txtDNI.getText().equals("")) {
                Cliente cliente = new Cliente();
                cliente.setDni(pnl.txtDNI.getText());

                if (ventaDAO.buscarDNI(cliente)) {
                    pnl.lblID.setText(String.valueOf(cliente.getIdCliente()));
                    pnl.txtNombre.setText(cliente.getNombre());
                    pnl.txtApellido.setText(cliente.getApellido());
                    pnl.txtTelefono.setText(cliente.getTelefono());
                } else {
                    JOptionPane.showMessageDialog(null, "No se encontro el dni");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Debe ingresar un DNI");
            }
        }
    }

    public void itemStateChanged(ItemEvent e) {
        if (pnl.cmbProductos.getSelectedIndex() != 0) {
            int i = pnl.cmbProductos.getSelectedIndex() - 1;
            pnl.txtCodigo.setText(String.valueOf(lista.get(i).getIdProducto()));
            pnl.txtDescripcion.setText(lista.get(i).getDescripcion());
            pnl.txtPrecio.setText(String.valueOf(lista.get(i).getPrecio()));
            pnl.txtStock.setText(String.valueOf(lista.get(i).getCantidad()));
        }
    }

    private void totalPagar() {
        double totalPagar = 0;
        int numFila = pnl.tblProductos.getRowCount();
        for (int i = 0; i < numFila; i++) {
            totalPagar += (double) pnl.tblProductos.getValueAt(i, 4);
        }
        pnl.lblTotalAPagar.setText(String.valueOf(totalPagar));
    }

    public void listarProductos(JComboBox cmb) {
        cmb.addItem("Elija un producto");
        for (Producto pro : lista) {
            cmb.addItem(pro.getNombre());
        }
    }

    public void registrarDetalle() {
        int id = ventaDAO.idVenta();
        Detalle detalleVenta = new Detalle();
        for (int i = 0; i < pnl.tblProductos.getRowCount(); i++) {
            int codigoProducto = Integer.parseInt(pnl.tblProductos.getValueAt(i, 0).toString());
            int cantidad = Integer.parseInt(pnl.tblProductos.getValueAt(i, 2).toString());
            double precioUnitario = Double.parseDouble(pnl.tblProductos.getValueAt(i, 3).toString());
            double subtotal = Double.parseDouble(pnl.tblProductos.getValueAt(i, 4).toString());
            double descuento = 0;
            double totalPagar = Double.parseDouble(pnl.tblProductos.getValueAt(i, 4).toString());
            int estado = 1;

            detalleVenta.setIdCabeceraVenta(id);
            detalleVenta.setIdProducto(codigoProducto);
            detalleVenta.setCantidad(cantidad);
            detalleVenta.setPrecioUnitario(precioUnitario);
            detalleVenta.setSubtotal(subtotal);
            detalleVenta.setDescuento(descuento);
            detalleVenta.setTotalPagar(totalPagar);
            detalleVenta.setEstado(estado);
            ventaDAO.registrarDetalle(detalleVenta);
        }
    }

    public void limpiar() {
        // Seccion producto
        pnl.cmbProductos.setSelectedIndex(0);
        pnl.txtCodigo.setText("");
        pnl.txtDescripcion.setText("");
        pnl.txtPrecio.setText("");
        pnl.txtStock.setText("");
        pnl.txtCantidad.setText("");

        // Seccion Cliente
        pnl.txtDNI.setText("");
        pnl.txtNombre.setText("");
        pnl.txtApellido.setText("");
        pnl.txtTelefono.setText("");

        pnl.lblVenta.setText(String.valueOf(ventaDAO.idVenta() + 1));
        pnl.lblTotalAPagar.setText("00.00");

        int filas = pnl.tblProductos.getRowCount();
        for (int i = 0; i <= filas; i++) {
            modelo.removeRow(0);
        }

    }

}
