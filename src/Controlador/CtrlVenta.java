/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Modelo.entidad.Cliente;
import Modelo.entidad.Detalle;
import Modelo.entidad.Producto;
import Modelo.entidad.Venta;
import Modelo.daos.VentaDAO;
import Panels.PnlVenta;
import Panels.PnlVentaFactura;
import static Util.Util.modelo;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

// PDF
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import static Util.Util.*;

/**
 *
 * @author Manuel
 */
public class CtrlVenta implements ActionListener, ItemListener {

    private Venta venta = new Venta();
    private VentaDAO ventaDAO = new VentaDAO();
    private PnlVentaFactura pnl;
    Date fecha = new Date();
    SimpleDateFormat formmato = new SimpleDateFormat("yyyy-MM-dd");

    //DefaultTableModel modelo = new DefaultTableModel();
    List<Producto> lista = ventaDAO.listarProducto();

    public CtrlVenta(PnlVentaFactura pnl) {
        this.pnl = pnl;
        this.pnl.btnRegistrar.addActionListener(this);
        this.pnl.btnAgregar.addActionListener(this);
        this.pnl.btnEliminar.addActionListener(this);
        this.pnl.btnBuscarDni.addActionListener(this);
        this.pnl.cmbProductos.addItemListener(this);
        this.pnl.lblFecha.setText(this.formmato.format(fecha));
        this.pnl.lblVenta.setText(String.valueOf(ventaDAO.idVenta() + 1));
        this.pnl.lblFecha.setVisible(false);
        this.pnl.lblID.setVisible(false);
        this.pnl.lblDireccion.setVisible(false);
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
        int stock = Integer.parseInt(pnl.txtStock.getText().trim());
        boolean productoAgregado = true;

        for (int i = 0; i < pnl.tblProductos.getRowCount(); i++) {
            if (pnl.tblProductos.getValueAt(i, 0).toString().equals(cod)) {
                productoAgregado = false;
                break;
            }
        }

        if (productoAgregado) {
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
                limpiarCamposProducto();
            } else {
                JOptionPane.showMessageDialog(null, "Stock no Disponible");
            }
        } else {
            JOptionPane.showMessageDialog(null, "El producto ya fue agregado");
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
                        1);

                if (ventaDAO.registrarVenta(venta1)) {
                    JOptionPane.showMessageDialog(null, "Venta registrada");
                    registrarDetalle();
                    Cliente cliente = new Cliente();
                    cliente.setDni(pnl.txtDNI.getText());
                    cliente.setNombre(pnl.txtNombre.getText());
                    cliente.setApellido(pnl.txtApellido.getText());
                    cliente.setTelefono(pnl.txtTelefono.getText());
                    cliente.setDireccion(pnl.lblDireccion.getText());
                    generarPDFVenta(Integer.parseInt(pnl.lblVenta.getText()), pnl.lblFecha.getText(), cliente, pnl.tblProductos);
                    limpiar();
                    lista = ventaDAO.listarProducto();
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
                    pnl.lblDireccion.setText(cliente.getDireccion());
                } else {
                    JOptionPane.showMessageDialog(null, "No se encontro el dni");
                }

            } else {
                JOptionPane.showMessageDialog(null, "Debe ingresar un DNI");
            }
        }

        if (e.getSource() == pnl.btnEliminar) {
            int fila = pnl.tblProductos.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(null, "Debe seleccionar una producto de la tabla");
            } else {
                modelo.removeRow(fila);
                totalPagar();
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
            totalPagar += Double.parseDouble(String.valueOf(pnl.tblProductos.getValueAt(i, 4)));
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
            ventaDAO.actualizarStock(cantidad, codigoProducto);
        }
    }

    public void limpiar() {
        // Seccion producto
        limpiarCamposProducto();

        // Seccion Cliente
        pnl.txtDNI.setText("");
        pnl.txtNombre.setText("");
        pnl.txtApellido.setText("");
        pnl.txtTelefono.setText("");

        pnl.lblVenta.setText(String.valueOf(ventaDAO.idVenta() + 1));
        pnl.lblTotalAPagar.setText("00.00");

        int filas = pnl.tblProductos.getRowCount();
        for (int i = 0; i < filas; i++) {
            modelo.removeRow(0);
        }

    }

    public void limpiarCamposProducto() {
        pnl.cmbProductos.setSelectedIndex(0);
        pnl.txtCodigo.setText("");
        pnl.txtDescripcion.setText("");
        pnl.txtPrecio.setText("");
        pnl.txtStock.setText("");
        pnl.txtCantidad.setText("");
    }

    public void generarPDFVenta(int nFactura, String fechaFactura, Cliente cliente, JTable tabla) {
        try {
            FileOutputStream archivo;
            File file = new File("src/Facturas/Venta_N" + nFactura + "_F" + fechaFactura + ".pdf");
            archivo = new FileOutputStream(file);
            Document doc = new Document();
            PdfWriter.getInstance(doc, archivo);
            doc.open();
            Image img = Image.getInstance("src/img/logo_pdf.png");

            Paragraph fecha = new Paragraph();
            Font negrita = new Font(Font.FontFamily.TIMES_ROMAN, 13, Font.BOLD, BaseColor.BLACK);
            fecha.add(Chunk.NEWLINE);
            //Date date = new Date();
            //new SimpleDateFormat("dd-MM-yyyy").format(date)
            fecha.add("Factura : " + nFactura + "\nFecha : " + fechaFactura + "\n\n");

            PdfPTable Encabezado = new PdfPTable(3);
            Encabezado.setWidthPercentage(100);
            Encabezado.getDefaultCell().setBorder(0);

            float[] ColumnaEncabezado = new float[]{50f, 70f, 40f};
            Encabezado.setWidths(ColumnaEncabezado);
            Encabezado.setHorizontalAlignment(Element.ALIGN_LEFT);

            Encabezado.addCell(img);

            Encabezado.addCell("        Ruc : " + RUC + "\n        Nombre : " + NOMBRE + "\n        Telefono: " + TELEFONO + "\n        Razon : " + RAZON);
            Encabezado.addCell(fecha);
            doc.add(Encabezado);

            Paragraph cli = new Paragraph();
            cli.add(Chunk.NEWLINE);
            cli.add("Datos del cliente: \n\n");
            doc.add(cli);

            PdfPTable tablacli = new PdfPTable(2);
            tablacli.setWidthPercentage(100);
            float[] Columnacli = new float[]{15f, 100f};
            tablacli.setWidths(Columnacli);

            PdfPCell cl1 = new PdfPCell(new Phrase("DNI : ", negrita));
            cl1.setBorder(0);
            tablacli.addCell(cl1);
            tablacli.addCell(cliente.getDni());

            PdfPCell cl2 = new PdfPCell(new Phrase("Nombre : ", negrita));
            cl2.setBorder(0);
            tablacli.addCell(cl2);
            tablacli.addCell(cliente.getApellido() + " " + cliente.getNombre());

            PdfPCell cl3 = new PdfPCell(new Phrase("Teléfono : ", negrita));
            cl3.setBorder(0);
            tablacli.addCell(cl3);
            tablacli.addCell(cliente.getTelefono());

            PdfPCell cl4 = new PdfPCell(new Phrase("Dirección : ", negrita));
            cl4.setBorder(0);
            tablacli.addCell(cl4);
            tablacli.addCell(cliente.getDireccion());

            doc.add(tablacli);

            Paragraph titulo = new Paragraph("\nDATOS DE LA COMPRA\n\n");
            titulo.setAlignment(1);

            doc.add(titulo);

            // productos
            PdfPTable tablapro = new PdfPTable(5);
            tablapro.setWidthPercentage(100);
            //tablapro.getDefaultCell().setBorder(0);
            float[] Columnapro = new float[]{15f, 50f, 10f, 15f, 20f};
            tablapro.setWidths(Columnapro);
            tablapro.setHorizontalAlignment(Element.ALIGN_LEFT);

            PdfPCell pro1 = new PdfPCell(new Phrase("\n  Código\n\n", negrita));
            PdfPCell pro2 = new PdfPCell(new Phrase("\n  Descripcion\n\n", negrita));
            PdfPCell pro3 = new PdfPCell(new Phrase("\n  Cant.\n\n", negrita));
            PdfPCell pro4 = new PdfPCell(new Phrase("\n  Precio U.\n\n", negrita));
            PdfPCell pro5 = new PdfPCell(new Phrase("\n  Precio Total\n\n", negrita));

            pro1.setBackgroundColor(new BaseColor(252, 148, 60));
            pro2.setBackgroundColor(new BaseColor(252, 148, 60));
            pro3.setBackgroundColor(new BaseColor(252, 148, 60));
            pro4.setBackgroundColor(new BaseColor(252, 148, 60));
            pro5.setBackgroundColor(new BaseColor(252, 148, 60));

            tablapro.addCell(pro1);
            tablapro.addCell(pro2);
            tablapro.addCell(pro3);
            tablapro.addCell(pro4);
            tablapro.addCell(pro5);

            double total = 0;
            for (int i = 0; i < tabla.getRowCount(); i++) {
                String codigo = "\n   " + tabla.getValueAt(i, 0) + "\n\n";
                String descripcion = "\n   " + tabla.getValueAt(i, 1) + "\n\n";
                String cantidad = "\n   " + tabla.getValueAt(i, 2) + "\n\n";
                String precioUnitario = "\n   S/." + tabla.getValueAt(i, 3) + "\n\n";
                String precioTotal = "\n   S/." + tabla.getValueAt(i, 4) + "\n\n";

                total += Double.parseDouble(tabla.getValueAt(i, 4).toString());

                tablapro.addCell(codigo);
                tablapro.addCell(descripcion);
                tablapro.addCell(cantidad);
                tablapro.addCell(precioUnitario);
                tablapro.addCell(precioTotal);
            }

            PdfPCell final1 = new PdfPCell(new Phrase(""));
            PdfPCell final2 = new PdfPCell(new Phrase(""));
            PdfPCell final3 = new PdfPCell(new Phrase(""));
            PdfPCell final4 = new PdfPCell(new Phrase("\n  TOTAL\n\n", negrita));
            PdfPCell final5 = new PdfPCell(new Phrase("\n  S/." + total + "\n\n", negrita));

            final1.setBorder(0);
            final2.setBorder(0);
            final3.setBorder(0);
            final4.setBackgroundColor(new BaseColor(252, 148, 60));

            tablapro.addCell(final1);
            tablapro.addCell(final2);
            tablapro.addCell(final3);
            tablapro.addCell(final4);
            tablapro.addCell(final5);

            doc.add(tablapro);

            doc.close();
            archivo.close();

            //abrirArchivo(file);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void abrirArchivo(File file) {
        try {
            Desktop.getDesktop().open(file);
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }
}
