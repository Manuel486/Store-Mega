package Controlador;

import Modelo.entidad.Cliente;
import Modelo.daos.ClienteDAO;
import Panels.PnlCliente;
import static Util.Util.modelo;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class CtrlCliente implements ActionListener, ItemListener, MouseListener {

    private Cliente cli = new Cliente();
    private ClienteDAO cliDAO = new ClienteDAO();
    private PnlCliente pnl;
    DefaultTableModel modelo = new DefaultTableModel();

    public CtrlCliente(PnlCliente pnl) {
        this.pnl = pnl;
        this.pnl.btnAgregar.addActionListener(this);
        this.pnl.btnEliminar.addActionListener(this);
        this.pnl.btnBuscar.addActionListener(this);
        this.pnl.btnLimpiar.addActionListener(this);
        this.pnl.btnModificar.addActionListener(this);
        //this.pnl.txtId.setVisible(false);
        //tabla
        //this.pnl.tblCliente.addMouseListener(this);
        this.listarCliente(this.pnl.tblCliente);
    }

    public void listarCliente(JTable tabla) {
        this.limpiarTabla();
        modelo = (DefaultTableModel) tabla.getModel();
        List<Cliente> lista = cliDAO.listarCliente();
        Object[] object = new Object[6];
        for (int i = 0; i < lista.size(); i++) {
            object[0] = lista.get(i).getIdCliente();
            object[1] = lista.get(i).getDni();
            object[2] = lista.get(i).getNombre();
            object[3] = lista.get(i).getApellido();
            object[4] = lista.get(i).getTelefono();
            object[5] = lista.get(i).getDireccion();
            modelo.addRow(object);
        }
        this.pnl.tblCliente.setModel(modelo);
    }

    /*
    public void recuperarInformacion() {
        if (modelo.getRowCount() > 0) {
            // Llenando tabla
            this.pnl.tblCliente.setModel(modelo);
        }
    
    
    
    public void agregarCliente(JTable tabla) {
        int id = Integer.parseInt(pnl.txtId.getText());
        String nombre = pnl.txtNombre.getText();
        String apellido = pnl.txtApellido.getText();
        String dni = pnl.txtDni.getText();
        String telefono = pnl.txtTelefono.getText();
        String direccion = pnl.txtDireccion.getText();
      
        if(id>0){
            modelo = (DefaultTableModel) tabla.getModel();
            ArrayList lista = new ArrayList();
            lista.add(id);
            lista.add(nombre);
            lista.add(apellido);
            lista.add(dni);
            lista.add(telefono);
            lista.add(direccion);
        }
        
    } }*/

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == pnl.btnAgregar) {
            cli.setNombre(pnl.txtNombre.getText());
            cli.setApellido(pnl.txtApellido.getText());
            cli.setDni(pnl.txtDni.getText());
            cli.setTelefono(pnl.txtTelefono.getText());
            cli.setDireccion(pnl.txtDireccion.getText());

            if (pnl.txtDni.getText().length() == 8) {
                if (cliDAO.registrarCliente(cli)) {
                    JOptionPane.showMessageDialog(null, "Registro Guardado");
                    limpiar();
                    this.listarCliente(this.pnl.tblCliente);
                } else {
                    JOptionPane.showMessageDialog(null, "Error al Guardar");
                    limpiar();
                }
            } else {
                JOptionPane.showMessageDialog(null, "El DNI debe contener 8 caracteres");
            }

        }

        if (e.getSource() == pnl.btnModificar) {
            cli.setIdCliente(Integer.parseInt(pnl.txtId.getText()));
            cli.setNombre(pnl.txtNombre.getText());
            cli.setApellido(pnl.txtApellido.getText());
            cli.setDni(pnl.txtDni.getText());
            cli.setTelefono(pnl.txtTelefono.getText());
            cli.setDireccion(pnl.txtDireccion.getText());

            if (cliDAO.modificarCliente(cli)) {
                JOptionPane.showMessageDialog(null, "Registro Modificado");
                limpiar();
                this.listarCliente(this.pnl.tblCliente);
            } else {
                JOptionPane.showMessageDialog(null, "Error al Modificar");
                limpiar();
            }
        }

        if (e.getSource() == pnl.btnEliminar) {
            int row = pnl.tblCliente.getSelectedRow();

            cli.setDni(String.valueOf(pnl.tblCliente.getValueAt(row, 1)));

            if (cliDAO.eliminarCliente(cli)) {
                JOptionPane.showMessageDialog(null, "Registro Eliminado");
                limpiar();
                this.listarCliente(this.pnl.tblCliente);
            } else {
                JOptionPane.showMessageDialog(null, "Error al Eliminar");
                limpiar();
            }
        }

        if (e.getSource() == pnl.btnBuscar) {
            cli.setDni(pnl.txtDni.getText());
            if (cliDAO.buscarCliente(cli)) {
                pnl.txtId.setText(String.valueOf(cli.getIdCliente()));
                pnl.txtNombre.setText(cli.getNombre());
                pnl.txtApellido.setText(cli.getApellido());
                pnl.txtDni.setText(cli.getDni());
                pnl.txtTelefono.setText(cli.getTelefono());
                pnl.txtDireccion.setText(cli.getDireccion());
            } else {
                JOptionPane.showMessageDialog(null, "No se encontró el resultado");
                //limpiar();
            }
        }

        if (e.getSource() == pnl.btnLimpiar) {
            limpiar();
        }

        //this.listarCliente(this.pnl.tblCliente);   
    }

    public void limpiar() {
        pnl.txtId.setText("");
        pnl.txtNombre.setText("");
        pnl.txtApellido.setText("");
        pnl.txtDni.setText("");
        pnl.txtTelefono.setText("");
        pnl.txtDireccion.setText("");
    }

    public void limpiarTabla() {
        this.modelo.setRowCount(0);
        /*
        for (int i = 0; i < tabla1.getRowCount(); i++) {
            this.modelo.removeRow(i);
            i -= 1;
        }*/
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void mousePressed(MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void mouseExited(MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
