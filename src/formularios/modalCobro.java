/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package formularios;

import clases.DatabaseConnector;
import clases.NumeroALetras;
import clases.ReportPrinter;
import disenho.Formato;
import java.awt.Color;
import java.awt.Cursor;
import static java.awt.Cursor.HAND_CURSOR;
import static java.awt.Cursor.WAIT_CURSOR;
import java.awt.Dimension;
import java.awt.Font;
import static java.awt.Frame.MAXIMIZED_BOTH;
import java.awt.event.KeyEvent;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author loncharich
 */
public final class modalCobro extends javax.swing.JInternalFrame {

    int count;
    int c = 0;
    int venta = 0;
    int cod_timbra = 0;
    public static int clien = 0;

    public modalCobro() {
        initComponents();
        ((javax.swing.plaf.basic.BasicInternalFrameUI) this.getUI()).setNorthPane(null);
        getContentPane().setBackground(Color.WHITE);
        nom_cli.setEditable(false);
        ruc_cli.setEditable(false);
        excentas.setEditable(false);
        iva_10.setEditable(false);
        total_pagar.setEditable(false);
        nro_tim.setEditable(false);
        tabla();
        focus.setVisible(false);
        Calendar cal = Calendar.getInstance();
        int anho = cal.get(Calendar.YEAR);
        int mes = cal.get(Calendar.MONTH) + 01;
        int dia = cal.get(Calendar.DATE) + 00;
        String format = String.format("%02d", mes);
        String format1 = String.format("%02d", dia);
        String fech = "" + (format1) + "/" + (format) + "/" + anho;
        fechas.setText(fech);
        fechas.setEditable(false);
        //contener_guardar.setVisible(false);
        //guar.setVisible(false);
        InputMap map = new InputMap();
        map.put(KeyStroke.getKeyStroke(KeyEvent.VK_F4, 0, false),
                "pressed");
        map.put(KeyStroke.getKeyStroke(KeyEvent.VK_F4, 0, true),
                "released");
        guar.setInputMap(0, map);

        guar.setInputMap(0, map);
        codigo_producto.setVisible(false);
        jLabel9.setVisible(false);
        contado.setSelected(true);
        numFactu();
        jPanel5.setVisible(false);
        MostrarBanco();
        contener_guardar3.setVisible(false);

        codigo_venta.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                checkTextField();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                checkTextField();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                checkTextField();
            }

            // Método para verificar el contenido del JTextField
            private void checkTextField() {
                if (!codigo_venta.getText().trim().isEmpty()) {
                    //button.setVisible(true); // Mostrar botón si hay texto
                    Formato.habilitarCampos(getContentPane(), false);
                    tbl_venta.setEnabled(false);
                    contener_guardar3.setVisible(true);
                    venta = Integer.parseInt(codigo_venta.getText());
                    btnDetalle.setEnabled(false);
                } else {
                    //button.setVisible(false); // Ocultar botón si está vacío
                    Formato.habilitarCampos(getContentPane(), true);
                    tbl_venta.setEnabled(true);
                    contener_guardar3.setVisible(false);
                    btnDetalle.setEnabled(true);
                }
            }
        });

    }

    public static void MostrarDatos(int valor, String proceso) {
        String query = "SELECT \n"
                + "    a.cod_cabecera,\n"
                + "    CONCAT(t.nombres, ' ', t.apellidos) AS cliente_nombre,\n"
                + "    CONCAT(m.codigo, '-', l.numero_lote, '-', l.serie) AS lote_numero,\n"
                + "    TO_CHAR(a.entrega, 'FM999G999G999G990') AS entrega,\n"
                + "    TO_CHAR(a.cuota, 'FM999G999G999G990') AS cuota,\n"
                + "    t.documento,\n"
                + "    CASE \n"
                + "        WHEN EXISTS (\n"
                + "            SELECT 1 \n"
                + "            FROM cobros co \n"
                + "            WHERE co.cod_alquiler = a.cod_cabecera\n"
                + "        ) THEN 'PAGO DE CUOTA'\n"
                + "        ELSE 'ENTREGA INICIAL'\n"
                + "    END AS concepto, t.cod_cliente\n"
                + "FROM \n"
                + "    alquiler_lote_cliente AS a\n"
                + "INNER JOIN \n"
                + "    cliente AS t ON t.cod_cliente = a.cod_cliente\n"
                + "INNER JOIN \n"
                + "    lote AS l ON l.cod_lote = a.cod_lote\n"
                + "INNER JOIN \n"
                + "manzana AS m ON m.cod_manzana = l.cod_manzana "
                + "WHERE \n"
                + "    a.estado_registro = 'A' and a.cod_cabecera =" + valor + ";";
        try (Connection conn = DatabaseConnector.getConnection();
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery(query)) {

            if (rs.next()) {
                if (proceso.equals("ABRIR")) {
                    ruc_cli.setText(rs.getString(6));
                    nom_cli.setText(rs.getString(2));
                    codigo_producto.setText(rs.getString(1));
                    clien = rs.getInt(8);
                }

                if (proceso.equals("CARGAR")) {
                    DefaultTableModel modelo = (DefaultTableModel) tbl_venta.getModel();
                    Object[] dato = new Object[6];

                    dato[0] = rs.getString(1);
                    dato[1] = rs.getString(3);
                    dato[2] = rs.getString(7);
                    dato[4] = "";
                    dato[5] = "";

                    int cuota = Integer.parseInt(rs.getString(5).replace(",", "").replace(".", ""));
                    int entrega = Integer.parseInt(rs.getString(4).replace(",", "").replace(".", ""));
                    DecimalFormat formatea = new DecimalFormat("#,##0");
                    if (rs.getString(7).equals("PAGO DE CUOTA")) {
                        dato[4] = rs.getString(5);

                    }
                    if (rs.getString(7).equals("ENTREGA INICIAL")) {
                        dato[4] = rs.getString(4);
                    }
                    modelo.addRow(dato);
                    scroll.getVerticalScrollBar().setValue(scroll.getVerticalScrollBar().getMaximum());
                    tbl_venta.setModel(modelo);
                    tbl_venta.setRowHeight(30);
                    tbl_venta.setFont(new java.awt.Font("Roboto", 0, 12));
                    tbl_venta.getTableHeader().setFont(new Font("Roboto Medium", 0, 12));
                    tbl_venta.getTableHeader().setBackground(Color.white);
                    ((DefaultTableCellRenderer) tbl_venta.getTableHeader().getDefaultRenderer())
                            .setHorizontalAlignment(SwingConstants.CENTER);
                    calcularIva();
                }

            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error al ejecutar la consulta SQL: " + e.getMessage());
        }
    }

    public void tabla() {
        tbl_venta.setRowHeight(30);
        tbl_venta.setFont(new java.awt.Font("Roboto", 0, 12));
        tbl_venta.getTableHeader().setFont(new Font("Roboto Medium", 0, 12));
        tbl_venta.getTableHeader().setBackground(Color.white);
        ((DefaultTableCellRenderer) tbl_venta.getTableHeader().getDefaultRenderer())
                .setHorizontalAlignment(SwingConstants.CENTER);
    }

    void borrarfila() {
        DefaultTableModel modelo = (DefaultTableModel) tbl_venta.getModel();
        int fila = tbl_venta.getSelectedRow();
        modelo.removeRow(fila);
        if (tbl_venta.getRowCount() == 0) {
            excentas.setText("");
            iva_10.setText("");
            total_pagar.setText("");
        }
        codigo_producto.requestFocus();
    }

    public static void calcularIva() {
        double exc = 0;
        double iva = 0;
        int total = 0;
        for (int i = 0; i < tbl_venta.getRowCount(); i++) {
            String subto = tbl_venta.getValueAt(i, 3).toString();
            String result_subto = subto.replaceAll("\\p{Punct}", "");
            String descripcion = tbl_venta.getValueAt(i, 1).toString();
            int subtotal = Integer.parseInt(result_subto);
            if (descripcion.equals("PAGO DE CUOTA")) {
                exc = exc + (subtotal - (subtotal * 0.15));
                iva = iva + (subtotal * 0.15);
            } else {
                exc = exc + (subtotal - (subtotal * 0.10));
                iva = iva + (subtotal * 0.10);
            }
            total = total + subtotal;
            DecimalFormat formatea = new DecimalFormat("#,##0");
            excentas.setText("" + formatea.format(exc));
            iva_10.setText("" + formatea.format(iva));
            total_pagar.setText("" + formatea.format(total));
            tbl_venta.changeSelection(i, 1, false, false);

        }

    }

    void limpiarFactura() {
        excentas.setText("");
        iva_10.setText("");
        total_pagar.setText("");
        TranTarjeta.setText("0");
        contado.setSelected(true);
        credito.setSelected(false);
        jPanel5.setVisible(false);
        transferencia.setSelected(false);
        DefaultTableModel modelo1 = (DefaultTableModel) tbl_venta.getModel();
        modelo1.setRowCount(0);
        numFactu();
        Formato.habilitarCampos(getContentPane(), true);
    }

    public void numFactu() {
        String sql = "select * from fn_generar_numero_factura()";
        try (Connection conn = DatabaseConnector.getConnection();
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery(sql)) {

            if (rs.next()) {
                nro_factura.setText(rs.getString(1));
                nro_tim.setText(rs.getString(2));
                cod_timbra = rs.getInt(5);
                jLabel6.setText("Factura Nro:");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(modalCobro.this, e.getMessage(), "Error al retornar numero de factura", JOptionPane.ERROR_MESSAGE);
            factura_check.setEnabled(false);
            recibo_check.setSelected(true);
            numRecibo();
        }
    }

    public void numRecibo() {
        String sql = "SELECT LPAD((COALESCE(MAX(cab_venta), 0) + 1)::TEXT, 5, '0') AS nuevo_cod_recibo\n"
                + "FROM venta where tipo_comprobante ='R';";
        try (Connection conn = DatabaseConnector.getConnection();
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery(sql)) {

            if (rs.next()) {
                nro_factura.setText(rs.getString(1));
                nro_tim.setText("0");
                jLabel6.setText("Recibo Nro:");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(modalCobro.this, e.getMessage(), "Error al retornar numero de recibo", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void guardarVenta() {
        String u5 = "", u7, u8, u9, transf = null, cod_banco = null;

        try (Connection cn = DatabaseConnector.getConnection()) {

            // Determinar el tipo de pago
            if (contado.isSelected()) {
                u5 = "EFECTIVO";
            } else if (credito.isSelected()) {
                u5 = "TARJETA";
                transf = TranTarjeta.getText();

            } else if (transferencia.isSelected()) {
                u5 = "TRANSFERENCIA";
                String seleccion = BancoCuenta.getSelectedItem().toString();
                int posicionGuion = seleccion.indexOf(" - ");
                if (posicionGuion > 0) {
                    String numeroCuenta = seleccion.substring(0, posicionGuion);
                    cod_banco = numeroCuenta;
                }
            }

            // Limpiar los campos de exentas, IVA y total antes de convertir
            u7 = excentas.getText().replaceAll("\\p{Punct}", "");
            u8 = iva_10.getText().replaceAll("\\p{Punct}", "");
            u9 = total_pagar.getText().replaceAll("\\p{Punct}", "");

            int u77 = Integer.parseInt(u7);
            int u88 = Integer.parseInt(u8);
            int u99 = Integer.parseInt(u9);

            // Preparar la consulta SQL
            PreparedStatement cts = cn.prepareStatement(
                    "INSERT INTO public.venta(nro_factura, condicion, excentas, iva, total, estado_registro, cod_cliente, cod_timbrado, tipo_comprobante, transferencia, cod_banco) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) returning cab_venta",
                    Statement.RETURN_GENERATED_KEYS
            );

            // Asignar valores a la consulta
            cts.setString(1, nro_factura.getText());
            cts.setString(2, u5);
            cts.setInt(3, u77);
            cts.setInt(4, u88);
            cts.setInt(5, u99);
            cts.setString(6, "A");  // Estado de registro
            cts.setInt(7, clien);  // Código de cliente

            if (factura_check.isSelected()) {
                cts.setInt(8, cod_timbra);
                cts.setString(9, "F");
            } else if (recibo_check.isSelected()) {
                cts.setNull(8, java.sql.Types.INTEGER);
                cts.setString(9, "R");
            }

            cts.setString(10, transf);
            cts.setString(11, cod_banco);

            // Ejecutar la consulta e insertar la venta
            int opcion = cts.executeUpdate();

            if (opcion == 1) {
                ResultSet rs3 = cts.getGeneratedKeys();
                if (rs3.next()) {
                    int val = rs3.getInt(1);
                    System.out.println("Cabecera registrada, ID: " + val);

                    // Guardar detalles de la venta
                    guardarDetalle(val);
                    venta = val;

                    // Mostrar mensaje de éxito
                    if (c == 0) {
                        JOptionPane.showMessageDialog(this, "Cobro registrado con éxito", "AVISO", JOptionPane.PLAIN_MESSAGE, Formato.icono("/imagenes/check.png", 40, 40));
                    } else {
                        c = 0;
                    }
                }
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Error al convertir los datos numéricos: " + e.getMessage());
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al ejecutar la consulta SQL: " + e.getMessage());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ocurrió un error inesperado: " + e.getMessage());
        }
    }

    public void guardarDetalle(int valor) throws SQLException {
        try (Connection cn = DatabaseConnector.getConnection()) {
            for (int i = 0; i < tbl_venta.getRowCount(); i++) {
                String pre_tot = tbl_venta.getValueAt(i, 3).toString();
                String des = tbl_venta.getValueAt(i, 1).toString();
                String fec = tbl_venta.getValueAt(i, 2).toString();
                String det = tbl_venta.getValueAt(i, 0).toString();

                if (!fec.equals("") && !des.equals("") && !det.equals("") && !pre_tot.equals("")) {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    java.util.Date date = sdf.parse(fec);
                    java.sql.Date sqlDate = new java.sql.Date(date.getTime());

                    String detNumber = det.substring(2).replaceAll("^0+", "");

                    PreparedStatement cts = cn.prepareStatement(
                            "INSERT INTO public.det_venta(\n"
                            + "	precio_total, cab_venta, cod_alquiler, descripcion, fecha_vencimiento, cod_det_cobro)\n"
                            + "	VALUES (?, ?, ?, ?, ?, ?);"
                    );
                    cts.setInt(1, Integer.parseInt(pre_tot.replaceAll("\\p{Punct}", "")));
                    cts.setInt(2, valor);
                    cts.setInt(3, Integer.parseInt(codigo_producto.getText().replaceAll("\\p{Punct}", "")));
                    cts.setString(4, des);
                    cts.setDate(5, sqlDate);
                    cts.setInt(6, Integer.parseInt(detNumber)); // Asegúrate de tener el valor correcto para este campo

                    cts.executeUpdate();

                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
            c = c + 1;
        }
    }

    void AlbuscarVenta(boolean b) {
        tbl_venta.setEnabled(b);
    }

    public void BuscarVenta(int valor, String tipo) {

        String sql = "";
        if (tipo.equals("F")) {
            sql = "select v.cab_venta,v.fecha,v.nro_factura,c.nombres || ' ' || c.apellidos,v.excentas,v.iva,v.total,c.documento,v.tipo_comprobante,v.transferencia,v.cod_banco,v.condicion,t.numero\n"
                    + "from venta as v\n"
                    + "inner join cliente c on c.cod_cliente=v.cod_cliente\n"
                    + "inner join timbrado t on t.cod_timbrado = v.cod_timbrado\n"
                    + "where cab_venta = " + valor + " ";

        } else {
            if (tipo.equals("R")) {
                sql = "select v.cab_venta,v.fecha,v.nro_factura,c.nombres || ' ' || c.apellidos,v.excentas,v.iva,v.total,c.documento,v.tipo_comprobante,v.transferencia,v.cod_banco,v.condicion\n"
                        + "from venta as v\n"
                        + "inner join cliente c on c.cod_cliente=v.cod_cliente\n"
                        + "where cab_venta = " + valor + " ";
            }
        }

        Statement st;

        try (Connection cn = DatabaseConnector.getConnection()) {
            st = (Statement) cn.createStatement();
            ResultSet rs = (ResultSet) st.executeQuery(sql);
            if (rs.next()) {
                codigo_venta.setText(rs.getString(1));
                String cortar = ((rs.getString(2)).substring(0, 10));
                java.sql.Date d = java.sql.Date.valueOf(cortar);
                SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
                fechas.setText("" + formato.format(d));
                nro_factura.setText(rs.getString(3));
                nom_cli.setText(rs.getString(4));

                DecimalFormat formatea = new DecimalFormat("#,##0");
                excentas.setText("" + formatea.format(rs.getInt(5)));
                iva_10.setText("" + formatea.format(rs.getInt(6)));
                total_pagar.setText("" + formatea.format(rs.getInt(7)));
                ruc_cli.setText(rs.getString(8));

                String comprobante = rs.getString(9);
                if (comprobante.equals("F")) {
                    factura_check.setSelected(true);
                    recibo_check.setSelected(false);
                    nro_tim.setText(rs.getString(13));
                } else {
                    if (comprobante.equals("R")) {
                        factura_check.setSelected(false);
                        recibo_check.setSelected(true);
                        nro_tim.setText("0");
                    }

                }

                if (rs.getString("condicion").equals("EFECTIVO")) {
                    contado.setSelected(true);
                    credito.setSelected(false);
                    transferencia.setSelected(false);
                    jPanel5.setVisible(false);
                    TranTarjeta.setText("0");
                } else if (rs.getString("condicion").equals("TARJETA")) {
                    credito.setSelected(true);
                    contado.setSelected(false);
                    transferencia.setSelected(false);
                    TranTarjeta.setText(rs.getString("transferencia"));
                    jPanel5.setVisible(true);
                    jLabel4.setText("Numero Transacción:");
                    TranTarjeta.setVisible(true);
                    BancoCuenta.setVisible(false);
                    TranTarjeta.requestFocus();

                } else if (rs.getString("condicion").equals("TRANSFERENCIA")) {
                    transferencia.setSelected(true);
                    contado.setSelected(false);
                    credito.setSelected(false);
                    jPanel5.setVisible(true);
                    jLabel4.setText("Banco-Cuenta:");
                    TranTarjeta.setVisible(false);
                    BancoCuenta.setVisible(true);
                    BancoCuenta.requestFocus();
                    TranTarjeta.setText("0");
                    MostrarBancoCombo(rs.getString("cod_banco"));

                }

            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Detalle no encontrado", "ERROR", JOptionPane.ERROR_MESSAGE);
        }

    }

    public void MostrarTablaDet(Integer valor) {

        DefaultTableModel modelo1 = (DefaultTableModel) tbl_venta.getModel();
        modelo1.setRowCount(0);
        String sql = "";
        sql = "select CASE \n"
                + "        WHEN cod_det_cobro = 1 THEN 'A-00' || cod_det_cobro\n"
                + "        WHEN cod_det_cobro > 1 THEN 'D-' || cod_det_cobro\n"
                + "        END AS cod_det_cobro_concatenado, descripcion,\n"
                + "	   TO_CHAR(fecha_vencimiento, 'DD/MM/YYYY') AS fecha_vencimiento_formateada,\n"
                + "        TO_CHAR(precio_total, 'FM999G999G999G990') AS precio_total_formateado \n"
                + "	   from det_venta\n"
                + "where cab_venta= " + valor + "";

        String datos[] = new String[5];
        Statement st;
        try (Connection cn = DatabaseConnector.getConnection()) {
            st = (Statement) cn.createStatement();
            ResultSet rs = (ResultSet) st.executeQuery(sql);
            while (rs.next()) {
                datos[0] = rs.getString(1);
                datos[1] = rs.getString(2);
                datos[2] = rs.getString(3);
                datos[3] = rs.getString(4);
                datos[4] = "PAGADO";

                modelo1.addRow(datos);
            }

            tbl_venta.setModel(modelo1);
            tabla();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "" + ex);
        }

    }

    void ImprimirReporte(String ruta) {
        ReportPrinter reportPrinter = new ReportPrinter();
        Map<String, Object> parameters = new HashMap<>();
        String montoEnLetras = NumeroALetras.convertir(Integer.parseInt(total_pagar.getText().replaceAll("\\p{Punct}", "")));
        parameters.put("venta", venta);
        parameters.put("montoLetras", montoEnLetras);
        reportPrinter.printReport(ruta, parameters);
    }

    private void MostrarBanco() {
        BancoCuenta.removeAllItems();
        // numero_manzana.addItem("");
        String query = "SELECT  cuenta || ' - ' ||nombre_banco as banco FROM public.banco order by cod_banco asc;";
        Statement st1;
        try (Connection conn = DatabaseConnector.getConnection();
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery(query)) {
            while (rs.next()) {
                BancoCuenta.addItem(rs.getString(1));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, ex);
        }
    }

    private void MostrarBancoCombo(String Valor) {
        BancoCuenta.removeAllItems();
        // numero_manzana.addItem("");
        String query = "SELECT  cuenta || ' - ' ||nombre_banco as banco FROM public.banco where cuenta='" + Valor + "';";
        Statement st1;
        try (Connection conn = DatabaseConnector.getConnection();
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery(query)) {
            while (rs.next()) {
                BancoCuenta.addItem(rs.getString(1));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, ex);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        ruc_cli = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        nom_cli = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        codigo_producto = new javax.swing.JTextField();
        contener_guardar = new javax.swing.JPanel();
        guar = new javax.swing.JButton();
        focus = new javax.swing.JButton();
        nro_factura = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        fechas = new javax.swing.JTextField();
        excentas = new javax.swing.JTextField();
        iva_10 = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        total_pagar = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        contener_guardar1 = new javax.swing.JPanel();
        guar1 = new javax.swing.JButton();
        codigo_venta = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        contener_guardar2 = new javax.swing.JPanel();
        guar2 = new javax.swing.JButton();
        Condicion1 = new javax.swing.JLabel();
        contado = new javax.swing.JCheckBox();
        credito = new javax.swing.JCheckBox();
        contener_detalle = new javax.swing.JPanel();
        btnDetalle = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        nro_tim = new javax.swing.JTextField();
        Condicion2 = new javax.swing.JLabel();
        factura_check = new javax.swing.JCheckBox();
        recibo_check = new javax.swing.JCheckBox();
        transferencia = new javax.swing.JCheckBox();
        jPanel5 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        TranTarjeta = new javax.swing.JTextField();
        BancoCuenta = new javax.swing.JComboBox<>();
        jPanel6 = new javax.swing.JPanel();
        jButton3 = new javax.swing.JButton();
        contener_guardar3 = new javax.swing.JPanel();
        guar3 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        scroll = new javax.swing.JScrollPane();
        tbl_venta = new javax.swing.JTable();

        setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        setPreferredSize(new java.awt.Dimension(876, 581));

        jLabel1.setBackground(new java.awt.Color(80, 90, 100));
        jLabel1.setFont(new java.awt.Font("Roboto Medium", 0, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("GENERAR COBROS");
        jLabel1.setOpaque(true);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel5.setFont(new java.awt.Font("Roboto Light", 1, 12)); // NOI18N
        jLabel5.setText("RUC:");

        ruc_cli.setFont(new java.awt.Font("Roboto Light", 0, 12)); // NOI18N
        ruc_cli.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        ruc_cli.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ruc_cliActionPerformed(evt);
            }
        });
        ruc_cli.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                ruc_cliKeyReleased(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Roboto Light", 1, 12)); // NOI18N
        jLabel2.setText("Nombre /Razon social:");

        nom_cli.setFont(new java.awt.Font("Roboto Light", 0, 12)); // NOI18N
        nom_cli.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        nom_cli.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nom_cliActionPerformed(evt);
            }
        });
        nom_cli.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                nom_cliKeyReleased(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Roboto Light", 1, 14)); // NOI18N
        jLabel9.setText("Codigo:");

        codigo_producto.setFont(new java.awt.Font("Roboto Light", 0, 12)); // NOI18N
        codigo_producto.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        codigo_producto.setEnabled(false);
        codigo_producto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                codigo_productoActionPerformed(evt);
            }
        });
        codigo_producto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                codigo_productoKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                codigo_productoKeyTyped(evt);
            }
        });

        contener_guardar.setBackground(new java.awt.Color(80, 90, 100));

        guar.setBackground(new java.awt.Color(80, 90, 100));
        guar.setFont(new java.awt.Font("Roboto Medium", 1, 12)); // NOI18N
        guar.setForeground(new java.awt.Color(255, 255, 255));
        guar.setText("GUARDAR");
        guar.setBorder(null);
        guar.setContentAreaFilled(false);
        guar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        guar.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                guarFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                guarFocusLost(evt);
            }
        });
        guar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                guarMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                guarMouseExited(evt);
            }
        });
        guar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                guarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout contener_guardarLayout = new javax.swing.GroupLayout(contener_guardar);
        contener_guardar.setLayout(contener_guardarLayout);
        contener_guardarLayout.setHorizontalGroup(
            contener_guardarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, contener_guardarLayout.createSequentialGroup()
                .addComponent(guar, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        contener_guardarLayout.setVerticalGroup(
            contener_guardarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(guar, javax.swing.GroupLayout.DEFAULT_SIZE, 34, Short.MAX_VALUE)
        );

        focus.setText("jButton1");
        focus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                focusActionPerformed(evt);
            }
        });

        nro_factura.setEditable(false);
        nro_factura.setFont(new java.awt.Font("Roboto Light", 0, 12)); // NOI18N
        nro_factura.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        nro_factura.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nro_facturaActionPerformed(evt);
            }
        });
        nro_factura.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                nro_facturaKeyReleased(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Roboto Light", 1, 12)); // NOI18N
        jLabel6.setText("Factura Nro:");

        jLabel7.setFont(new java.awt.Font("Roboto Light", 1, 12)); // NOI18N
        jLabel7.setText("Fecha:");

        fechas.setFont(new java.awt.Font("Roboto Light", 0, 12)); // NOI18N
        fechas.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        fechas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fechasActionPerformed(evt);
            }
        });
        fechas.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                fechasKeyReleased(evt);
            }
        });

        excentas.setFont(new java.awt.Font("Roboto Light", 1, 16)); // NOI18N
        excentas.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        excentas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                excentasActionPerformed(evt);
            }
        });
        excentas.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                excentasKeyReleased(evt);
            }
        });

        iva_10.setFont(new java.awt.Font("Roboto Light", 1, 16)); // NOI18N
        iva_10.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        iva_10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                iva_10ActionPerformed(evt);
            }
        });
        iva_10.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                iva_10KeyReleased(evt);
            }
        });

        jLabel12.setFont(new java.awt.Font("Roboto Light", 1, 14)); // NOI18N
        jLabel12.setText("Total a Pagar:");

        total_pagar.setFont(new java.awt.Font("Roboto Light", 1, 16)); // NOI18N
        total_pagar.setForeground(new java.awt.Color(204, 0, 0));
        total_pagar.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        total_pagar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                total_pagarActionPerformed(evt);
            }
        });
        total_pagar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                total_pagarKeyReleased(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Roboto Light", 1, 14)); // NOI18N
        jLabel10.setText("Exentas:");

        jLabel11.setFont(new java.awt.Font("Roboto Light", 1, 14)); // NOI18N
        jLabel11.setText("Total IVA (10%):");

        contener_guardar1.setBackground(new java.awt.Color(80, 90, 100));

        guar1.setBackground(new java.awt.Color(153, 204, 255));
        guar1.setFont(new java.awt.Font("Roboto Medium", 1, 12)); // NOI18N
        guar1.setForeground(new java.awt.Color(255, 255, 255));
        guar1.setText("LIMPIAR");
        guar1.setBorder(null);
        guar1.setContentAreaFilled(false);
        guar1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        guar1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                guar1FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                guar1FocusLost(evt);
            }
        });
        guar1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                guar1MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                guar1MouseExited(evt);
            }
        });
        guar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                guar1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout contener_guardar1Layout = new javax.swing.GroupLayout(contener_guardar1);
        contener_guardar1.setLayout(contener_guardar1Layout);
        contener_guardar1Layout.setHorizontalGroup(
            contener_guardar1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, contener_guardar1Layout.createSequentialGroup()
                .addComponent(guar1, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        contener_guardar1Layout.setVerticalGroup(
            contener_guardar1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(guar1, javax.swing.GroupLayout.DEFAULT_SIZE, 34, Short.MAX_VALUE)
        );

        codigo_venta.setEditable(false);
        codigo_venta.setFont(new java.awt.Font("Roboto Light", 1, 16)); // NOI18N
        codigo_venta.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        codigo_venta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                codigo_ventaActionPerformed(evt);
            }
        });
        codigo_venta.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                codigo_ventaKeyReleased(evt);
            }
        });

        jPanel3.setBackground(new java.awt.Color(80, 90, 100));

        jButton1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("<");
        jButton1.setContentAreaFilled(false);
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton1MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButton1MouseExited(evt);
            }
        });
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jButton1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jPanel4.setBackground(new java.awt.Color(80, 90, 100));

        jButton2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText(">");
        jButton2.setContentAreaFilled(false);
        jButton2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton2MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButton2MouseExited(evt);
            }
        });
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jLabel8.setFont(new java.awt.Font("Roboto Light", 1, 12)); // NOI18N
        jLabel8.setText("Numero venta:");

        contener_guardar2.setBackground(new java.awt.Color(204, 0, 0));

        guar2.setBackground(new java.awt.Color(153, 204, 255));
        guar2.setFont(new java.awt.Font("Roboto Medium", 1, 12)); // NOI18N
        guar2.setForeground(new java.awt.Color(255, 255, 255));
        guar2.setText("ANULAR COBRO");
        guar2.setBorder(null);
        guar2.setContentAreaFilled(false);
        guar2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        guar2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                guar2FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                guar2FocusLost(evt);
            }
        });
        guar2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                guar2MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                guar2MouseExited(evt);
            }
        });
        guar2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                guar2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout contener_guardar2Layout = new javax.swing.GroupLayout(contener_guardar2);
        contener_guardar2.setLayout(contener_guardar2Layout);
        contener_guardar2Layout.setHorizontalGroup(
            contener_guardar2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(contener_guardar2Layout.createSequentialGroup()
                .addComponent(guar2, javax.swing.GroupLayout.PREFERRED_SIZE, 274, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        contener_guardar2Layout.setVerticalGroup(
            contener_guardar2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(guar2, javax.swing.GroupLayout.DEFAULT_SIZE, 34, Short.MAX_VALUE)
        );

        Condicion1.setFont(new java.awt.Font("Roboto Light", 1, 12)); // NOI18N
        Condicion1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Condicion1.setText("Forma de pago");

        contado.setBackground(new java.awt.Color(255, 255, 255));
        contado.setFont(new java.awt.Font("Roboto Light", 0, 12)); // NOI18N
        contado.setText("EFECTIVO");
        contado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                contadoActionPerformed(evt);
            }
        });

        credito.setBackground(new java.awt.Color(255, 255, 255));
        credito.setFont(new java.awt.Font("Roboto Light", 0, 12)); // NOI18N
        credito.setText("TARJETA");
        credito.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                creditoActionPerformed(evt);
            }
        });

        contener_detalle.setBackground(new java.awt.Color(80, 90, 100));

        btnDetalle.setBackground(new java.awt.Color(153, 204, 255));
        btnDetalle.setFont(new java.awt.Font("Roboto Medium", 1, 12)); // NOI18N
        btnDetalle.setForeground(new java.awt.Color(255, 255, 255));
        btnDetalle.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/detalle.png"))); // NOI18N
        btnDetalle.setBorder(null);
        btnDetalle.setContentAreaFilled(false);
        btnDetalle.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnDetalle.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnDetalle.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                btnDetalleFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                btnDetalleFocusLost(evt);
            }
        });
        btnDetalle.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnDetalleMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnDetalleMouseExited(evt);
            }
        });
        btnDetalle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDetalleActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout contener_detalleLayout = new javax.swing.GroupLayout(contener_detalle);
        contener_detalle.setLayout(contener_detalleLayout);
        contener_detalleLayout.setHorizontalGroup(
            contener_detalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(contener_detalleLayout.createSequentialGroup()
                .addComponent(btnDetalle, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        contener_detalleLayout.setVerticalGroup(
            contener_detalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(contener_detalleLayout.createSequentialGroup()
                .addComponent(btnDetalle, javax.swing.GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        jLabel3.setFont(new java.awt.Font("Roboto Light", 1, 12)); // NOI18N
        jLabel3.setText("Timbrado:");

        nro_tim.setFont(new java.awt.Font("Roboto Light", 0, 12)); // NOI18N
        nro_tim.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        nro_tim.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nro_timActionPerformed(evt);
            }
        });
        nro_tim.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                nro_timKeyReleased(evt);
            }
        });

        Condicion2.setFont(new java.awt.Font("Roboto Light", 1, 12)); // NOI18N
        Condicion2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Condicion2.setText("Tipo Comprobante");

        factura_check.setBackground(new java.awt.Color(255, 255, 255));
        factura_check.setFont(new java.awt.Font("Roboto Light", 0, 12)); // NOI18N
        factura_check.setSelected(true);
        factura_check.setText("FACTURA");
        factura_check.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                factura_checkActionPerformed(evt);
            }
        });

        recibo_check.setBackground(new java.awt.Color(255, 255, 255));
        recibo_check.setFont(new java.awt.Font("Roboto Light", 0, 12)); // NOI18N
        recibo_check.setText("RECIBO");
        recibo_check.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                recibo_checkActionPerformed(evt);
            }
        });

        transferencia.setBackground(new java.awt.Color(255, 255, 255));
        transferencia.setFont(new java.awt.Font("Roboto Light", 0, 12)); // NOI18N
        transferencia.setText("TRANSFERENCIA");
        transferencia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                transferenciaActionPerformed(evt);
            }
        });

        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel4.setFont(new java.awt.Font("Roboto Light", 1, 12)); // NOI18N
        jLabel4.setText("Numero Transaccion:");
        jPanel5.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 160, -1));

        TranTarjeta.setFont(new java.awt.Font("Dialog", 0, 10)); // NOI18N
        TranTarjeta.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel5.add(TranTarjeta, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 20, 160, 30));

        BancoCuenta.setFont(new java.awt.Font("Dialog", 1, 10)); // NOI18N
        BancoCuenta.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        BancoCuenta.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel5.add(BancoCuenta, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 20, 160, 30));

        jPanel6.setBackground(new java.awt.Color(80, 90, 100));

        jButton3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/buscar.png"))); // NOI18N
        jButton3.setContentAreaFilled(false);
        jButton3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton3MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButton3MouseExited(evt);
            }
        });
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        contener_guardar3.setBackground(new java.awt.Color(80, 90, 100));

        guar3.setBackground(new java.awt.Color(153, 204, 255));
        guar3.setFont(new java.awt.Font("Roboto Medium", 1, 12)); // NOI18N
        guar3.setForeground(new java.awt.Color(255, 255, 255));
        guar3.setText("REEMPRIMIR DOCUMENTO");
        guar3.setBorder(null);
        guar3.setContentAreaFilled(false);
        guar3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        guar3.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                guar3FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                guar3FocusLost(evt);
            }
        });
        guar3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                guar3MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                guar3MouseExited(evt);
            }
        });
        guar3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                guar3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout contener_guardar3Layout = new javax.swing.GroupLayout(contener_guardar3);
        contener_guardar3.setLayout(contener_guardar3Layout);
        contener_guardar3Layout.setHorizontalGroup(
            contener_guardar3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(contener_guardar3Layout.createSequentialGroup()
                .addComponent(guar3, javax.swing.GroupLayout.PREFERRED_SIZE, 274, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        contener_guardar3Layout.setVerticalGroup(
            contener_guardar3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(guar3, javax.swing.GroupLayout.DEFAULT_SIZE, 34, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(nom_cli, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ruc_cli, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(nro_tim, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addGap(6, 6, 6)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(nro_factura, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fechas, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Condicion2, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(factura_check)
                        .addGap(1, 1, 1)
                        .addComponent(recibo_check)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(contener_guardar2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(50, 50, 50)
                                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(102, 102, 102)
                                        .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, 121, Short.MAX_VALUE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(102, 102, 102)
                                        .addComponent(codigo_venta)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(contener_guardar3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(Condicion1, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(contado)
                        .addGap(1, 1, 1)
                        .addComponent(credito))
                    .addComponent(transferencia, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(contener_guardar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(contener_guardar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(contener_detalle, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(59, 59, 59)
                .addComponent(codigo_producto, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(95, 95, 95)
                .addComponent(focus)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 17, Short.MAX_VALUE)
                .addComponent(excentas, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(iva_10, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(total_pagar, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(105, 105, 105)
                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(48, 48, 48)
                .addComponent(jLabel11)
                .addGap(46, 46, 46)
                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(Condicion1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(contado)
                            .addComponent(credito))
                        .addGap(5, 5, 5)
                        .addComponent(transferencia, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(contener_guardar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(contener_guardar1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(4, 4, 4))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(90, 90, 90)
                                        .addComponent(nom_cli, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(30, 30, 30)
                                        .addComponent(ruc_cli, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(60, 60, 60)
                                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(30, 30, 30)
                                        .addComponent(nro_tim, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(91, 91, 91)
                                        .addComponent(nro_factura, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(60, 60, 60)
                                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(30, 30, 30)
                                        .addComponent(fechas, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(6, 6, 6)
                                .addComponent(Condicion2, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(6, 6, 6)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(factura_check)
                                    .addComponent(recibo_check)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(30, 30, 30)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(30, 30, 30)
                                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(contener_guardar2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(26, 26, 26)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(codigo_venta, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(contener_guardar3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(contener_detalle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(codigo_producto, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(focus))
                    .addComponent(excentas, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(iva_10, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(total_pagar, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 225, Short.MAX_VALUE)
        );

        tbl_venta.setAutoCreateRowSorter(true);
        tbl_venta.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        tbl_venta.setFont(new java.awt.Font("Roboto Medium", 0, 12)); // NOI18N
        tbl_venta.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Codigo Detalle", "Descripción", "Fecha Vencimiento", "Monto", "Estado"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbl_venta.setGridColor(new java.awt.Color(255, 255, 255));
        tbl_venta.setInheritsPopupMenu(true);
        tbl_venta.setOpaque(false);
        tbl_venta.setSelectionBackground(new java.awt.Color(204, 255, 204));
        tbl_venta.setSelectionForeground(new java.awt.Color(0, 0, 0));
        tbl_venta.getTableHeader().setResizingAllowed(false);
        tbl_venta.getTableHeader().setReorderingAllowed(false);
        tbl_venta.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_ventaMouseClicked(evt);
            }
        });
        tbl_venta.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tbl_ventaKeyReleased(evt);
            }
        });
        scroll.setViewportView(tbl_venta);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(scroll))
                .addGap(0, 0, 0)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(scroll, javax.swing.GroupLayout.DEFAULT_SIZE, 231, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tbl_ventaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_ventaMouseClicked

    }//GEN-LAST:event_tbl_ventaMouseClicked

    private void ruc_cliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ruc_cliActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ruc_cliActionPerformed

    private void ruc_cliKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ruc_cliKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_ruc_cliKeyReleased

    private void nro_facturaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nro_facturaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_nro_facturaActionPerformed

    private void nro_facturaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_nro_facturaKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_nro_facturaKeyReleased

    private void nom_cliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nom_cliActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_nom_cliActionPerformed

    private void nom_cliKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_nom_cliKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_nom_cliKeyReleased

    private void codigo_productoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_codigo_productoActionPerformed
        //ResultSet rs2 = (ResultSet) st.executeQuery("select controlar_existencia_producto(" + cod + "," + can + ")");
        // buscarProducto();        // TODO add your handling code here:
    }//GEN-LAST:event_codigo_productoActionPerformed

    private void codigo_productoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_codigo_productoKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_codigo_productoKeyReleased

    private void excentasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_excentasActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_excentasActionPerformed

    private void excentasKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_excentasKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_excentasKeyReleased

    private void iva_10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_iva_10ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_iva_10ActionPerformed

    private void iva_10KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_iva_10KeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_iva_10KeyReleased

    private void total_pagarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_total_pagarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_total_pagarActionPerformed

    private void total_pagarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_total_pagarKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_total_pagarKeyReleased

    private void guarFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_guarFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_guarFocusGained

    private void guarFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_guarFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_guarFocusLost

    private void guarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_guarMouseEntered
        contener_guardar.setBackground(new Color(51, 51, 51));           // TODO add your handling code here:
    }//GEN-LAST:event_guarMouseEntered

    private void guarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_guarMouseExited
        contener_guardar.setBackground(new Color(80, 90, 100));
    }//GEN-LAST:event_guarMouseExited

    private void guarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_guarActionPerformed
        if (codigo_venta.getText().equals("") || codigo_venta.getText().isEmpty()) {
            codigo_venta.setText("0");
            if (Formato.verificarCampos(getContentPane()) == true) {
                guardarVenta();
                if (factura_check.isSelected() == true) {
                    ImprimirReporte("/reportes/factura.jasper");
                } else {
                    if (recibo_check.isSelected() == true) {
                        ImprimirReporte("/reportes/recibo.jasper");
                    }
                }
                guar1.doClick();
                codigo_venta.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Debe ingresar todos los campos", "Aviso", JOptionPane.INFORMATION_MESSAGE);
                codigo_venta.setText("");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Este cobro ya fue realizado", "Aviso", JOptionPane.INFORMATION_MESSAGE);
        }

        // TODO add your handling code here:
    }//GEN-LAST:event_guarActionPerformed

    private void focusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_focusActionPerformed
        BuscarVenta(Integer.parseInt(codigo_venta.getText()), nom_cli.getText());
        MostrarTablaDet(Integer.parseInt(codigo_venta.getText()));
    }//GEN-LAST:event_focusActionPerformed

    private void fechasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fechasActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_fechasActionPerformed

    private void fechasKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_fechasKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_fechasKeyReleased

    private void tbl_ventaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbl_ventaKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_DELETE) {
            int valor = JOptionPane.showConfirmDialog(this, "Esta seguro que desea borrar el producto del detalle?", "Aviso!!", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (valor == JOptionPane.YES_NO_OPTION) {
                borrarfila();
                calcularIva();
            }
        }        // TODO add your handling code here:
    }//GEN-LAST:event_tbl_ventaKeyReleased

    private void guar1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_guar1FocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_guar1FocusGained

    private void guar1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_guar1FocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_guar1FocusLost

    private void guar1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_guar1MouseEntered
        contener_guardar1.setBackground(new Color(51, 51, 51));
    }//GEN-LAST:event_guar1MouseEntered

    private void guar1MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_guar1MouseExited
        contener_guardar1.setBackground(new Color(80, 90, 100));         // TODO add your handling code here:
    }//GEN-LAST:event_guar1MouseExited

    private void guar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_guar1ActionPerformed
        AlbuscarVenta(true);
        limpiarFactura();
        factura_check.setSelected(true);
        recibo_check.setSelected(false);
        numFactu();
        Calendar cal = Calendar.getInstance();
        int anho = cal.get(Calendar.YEAR);
        int mes = cal.get(Calendar.MONTH) + 01;
        int dia = cal.get(Calendar.DATE) + 00;
        String format = String.format("%02d", mes);
        String format1 = String.format("%02d", dia);
        String fech = "" + (format1) + "/" + (format) + "/" + anho;
        fechas.setText(fech);
        codigo_venta.setText("");

        // TODO add your handling code here:
    }//GEN-LAST:event_guar1ActionPerformed

    private void codigo_productoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_codigo_productoKeyTyped
        char c = evt.getKeyChar();
        if (Character.isLowerCase(c)) {
            String cad = ("" + c).toUpperCase();
            c = cad.charAt(0);
            evt.setKeyChar(c);
        }         // TODO add your handling code here:
    }//GEN-LAST:event_codigo_productoKeyTyped

    private void codigo_ventaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_codigo_ventaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_codigo_ventaActionPerformed

    private void codigo_ventaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_codigo_ventaKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_codigo_ventaKeyReleased

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        AlbuscarVenta(false);
        if (codigo_venta.getText().equals("")) {
            String sql = "SELECT v.cab_venta,v.tipo_comprobante FROM venta v WHERE v.cab_venta = (SELECT MAX(cab_venta) FROM venta WHERE estado_registro = 'A');";

            Statement st;

            try (Connection cn = DatabaseConnector.getConnection()) {
                st = (Statement) cn.createStatement();
                ResultSet rs = (ResultSet) st.executeQuery(sql);
                if (rs.next()) {
                    BuscarVenta(rs.getInt(1), rs.getString(2));
                    MostrarTablaDet(rs.getInt(1));
                    //traerpos();
                }

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "No se encontro el Maximo de la venta", "ERROR", JOptionPane.ERROR_MESSAGE);
            }

        } else {
            String sql = "SELECT cab_venta,tipo_comprobante\n"
                    + "FROM venta\n"
                    + "WHERE cab_venta < " + Integer.parseInt(codigo_venta.getText()) + " and estado_registro='A'\n"
                    + "ORDER BY cab_venta DESC\n"
                    + "LIMIT 1;";

            Statement st;

            try (Connection cn = DatabaseConnector.getConnection()) {
                st = (Statement) cn.createStatement();
                ResultSet rs = (ResultSet) st.executeQuery(sql);
                rs.next();
                BuscarVenta(rs.getInt(1), rs.getString(2));
                MostrarTablaDet(rs.getInt(1));
                //traerpos();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "No se encontro la siguiente venta", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        }        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        AlbuscarVenta(false);
        if (codigo_venta.getText().equals("")) {
            String sql = "SELECT v.cab_venta,v.tipo_comprobante FROM venta v WHERE v.cab_venta = (SELECT MAX(cab_venta) FROM venta WHERE estado_registro = 'A');";

            Statement st;

            try (Connection cn = DatabaseConnector.getConnection()) {
                st = (Statement) cn.createStatement();
                ResultSet rs = (ResultSet) st.executeQuery(sql);
                if (rs.next()) {
                    BuscarVenta(rs.getInt(1), rs.getString(2));
                    MostrarTablaDet(rs.getInt(1));
                    // traerpos();
                }

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "No se encontro el Max venta", "ERROR", JOptionPane.ERROR_MESSAGE);
            }

        } else {
            String sql = "SELECT cab_venta,tipo_comprobante\n"
                    + "FROM venta\n"
                    + "WHERE cab_venta > " + Integer.parseInt(codigo_venta.getText()) + " and estado_registro='A'\n"
                    + "ORDER BY cab_venta asc\n"
                    + "LIMIT 1;";

            Statement st;

            try (Connection cn = DatabaseConnector.getConnection()) {
                st = (Statement) cn.createStatement();
                ResultSet rs = (ResultSet) st.executeQuery(sql);
                rs.next();
                BuscarVenta(rs.getInt(1), rs.getString(2));
                MostrarTablaDet(rs.getInt(1));
                // traerpos();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "No se encontro la siguiente venta", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        }        // TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseExited
        jPanel3.setBackground(new Color(80, 90, 100));        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1MouseExited

    private void jButton1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseEntered
        jPanel3.setBackground(new Color(51, 51, 51));;         // TODO add your handling code here:
    }//GEN-LAST:event_jButton1MouseEntered

    private void jButton2MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton2MouseExited
        jPanel4.setBackground(new Color(80, 90, 100));
    }//GEN-LAST:event_jButton2MouseExited

    private void jButton2MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton2MouseEntered
        jPanel4.setBackground(new Color(51, 51, 51));         // TODO add your handling code here:
    }//GEN-LAST:event_jButton2MouseEntered

    private void guar2FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_guar2FocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_guar2FocusGained

    private void guar2FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_guar2FocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_guar2FocusLost

    private void guar2MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_guar2MouseEntered
        contener_guardar2.setBackground(new Color(153, 0, 0));           // TODO add your handling code here:
    }//GEN-LAST:event_guar2MouseEntered

    private void guar2MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_guar2MouseExited
        contener_guardar2.setBackground(new Color(204, 0, 0));        // TODO add your handling code here:
    }//GEN-LAST:event_guar2MouseExited

    private void guar2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_guar2ActionPerformed

        if (codigo_venta.getText().equals("")) {
            javax.swing.JOptionPane.showMessageDialog(this, "No hay ningun cobro seleccionado\n", "AVISO!", javax.swing.JOptionPane.INFORMATION_MESSAGE);
        } else {

            int valor = JOptionPane.showConfirmDialog(this, "Esta seguro de anular el Cobro?", "Aviso!!", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (valor == JOptionPane.YES_NO_OPTION) {
                try (Connection cn = DatabaseConnector.getConnection()) {
                    PreparedStatement pps = cn.prepareStatement("UPDATE venta SET estado_registro='N' WHERE cab_venta=" + Integer.parseInt(codigo_venta.getText()) + "");        // TODO add your handling code here:
                    pps.executeUpdate();
                    JOptionPane.showMessageDialog(this, "La anulación del Cobro fue realizado con exito", "AVISO", JOptionPane.PLAIN_MESSAGE, Formato.icono("/imagenes/check.png", 40, 40));
                    guar1.doClick();
                } catch (SQLException ex) {
                    Logger.getLogger(modalCobro.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }

    }//GEN-LAST:event_guar2ActionPerformed

    private void contadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_contadoActionPerformed
        contado.setSelected(true);
        credito.setSelected(false);
        transferencia.setSelected(false);
        jPanel5.setVisible(false);
        TranTarjeta.setText("0");
    }//GEN-LAST:event_contadoActionPerformed

    private void creditoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_creditoActionPerformed
        credito.setSelected(true);
        contado.setSelected(false);
        transferencia.setSelected(false);
        jPanel5.setVisible(true);
        jLabel4.setText("Numero Transacción:");
        TranTarjeta.setVisible(true);
        TranTarjeta.setText("");
        BancoCuenta.setVisible(false);
        TranTarjeta.requestFocus();
        // TODO add your handling code here:
    }//GEN-LAST:event_creditoActionPerformed

    private void btnDetalleFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_btnDetalleFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_btnDetalleFocusGained

    private void btnDetalleFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_btnDetalleFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_btnDetalleFocusLost

    private void btnDetalleMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDetalleMouseEntered
        contener_detalle.setBackground(new Color(51, 51, 51));          // TODO add your handling code here:
    }//GEN-LAST:event_btnDetalleMouseEntered

    private void btnDetalleMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDetalleMouseExited
        contener_detalle.setBackground(new Color(80, 90, 100));        // TODO add your handling code here:
    }//GEN-LAST:event_btnDetalleMouseExited

    private void btnDetalleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDetalleActionPerformed
        MostrarDatos(Integer.parseInt(codigo_producto.getText()), "ABRIR");
        limpiarFactura();
        codigo_venta.setText("");
        modalDetalleCobro c = new modalDetalleCobro();
        principalMenu.escritorio.add(c);
        Dimension desktopSize = principalMenu.escritorio.getSize();
        Dimension FrameSize = c.getSize();
        c.setLocation((desktopSize.width - FrameSize.width) / 2, (desktopSize.height - FrameSize.height) / 6);
        c.show();
        modalDetalleCobro.mostrarTabla(Integer.parseInt(codigo_producto.getText()), "mover",1000);
        // TODO add your handling code here:
    }//GEN-LAST:event_btnDetalleActionPerformed

    private void nro_timActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nro_timActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_nro_timActionPerformed

    private void nro_timKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_nro_timKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_nro_timKeyReleased

    private void factura_checkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_factura_checkActionPerformed
        factura_check.setSelected(true);
        recibo_check.setSelected(false);
        numFactu();
        // TODO add your handling code here:
    }//GEN-LAST:event_factura_checkActionPerformed

    private void recibo_checkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_recibo_checkActionPerformed
        factura_check.setSelected(false);
        recibo_check.setSelected(true);
        numRecibo();
        // TODO add your handling code here:
    }//GEN-LAST:event_recibo_checkActionPerformed

    private void transferenciaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_transferenciaActionPerformed
        credito.setSelected(false);
        contado.setSelected(false);
        transferencia.setSelected(true);
        jPanel5.setVisible(true);
        jLabel4.setText("Banco-Cuenta:");
        TranTarjeta.setVisible(false);
        BancoCuenta.setVisible(true);
        BancoCuenta.requestFocus();
        TranTarjeta.setText("0");
        MostrarBanco();// TODO add your handling code here:
    }//GEN-LAST:event_transferenciaActionPerformed

    private void jButton3MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton3MouseEntered
        jPanel6.setBackground(new Color(51, 51, 51));            // TODO add your handling code here:
    }//GEN-LAST:event_jButton3MouseEntered

    private void jButton3MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton3MouseExited
        jPanel6.setBackground(new Color(80, 90, 100));        // TODO add your handling code here:
    }//GEN-LAST:event_jButton3MouseExited

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        JTextField[] tfParam = new JTextField[2];
        tfParam[0] = codigo_venta;
        tfParam[1] = nom_cli;

        String sql = "select cab_venta,tipo_comprobante,nro_factura from venta where estado_registro ='A' and nro_factura like ";

        buscadorVenta pp = new buscadorVenta(sql, new String[]{"Venta", "Comprobante", "Numero Factura"}, 3, tfParam, focus);
        Dimension desktopSize = principalMenu.escritorio.getSize();
        Dimension FrameSize = pp.getSize();
        pp.setLocation((desktopSize.width - FrameSize.width) / 2, (desktopSize.height - FrameSize.height) / 4);
        pp.setVisible(true);         // TODO add your handling code here:
    }//GEN-LAST:event_jButton3ActionPerformed

    private void guar3FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_guar3FocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_guar3FocusGained

    private void guar3FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_guar3FocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_guar3FocusLost

    private void guar3MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_guar3MouseEntered
        contener_guardar3.setBackground(new Color(51, 51, 51));          // TODO add your handling code here:
    }//GEN-LAST:event_guar3MouseEntered

    private void guar3MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_guar3MouseExited
        contener_guardar3.setBackground(new Color(80, 90, 100));        // TODO add your handling code here:
    }//GEN-LAST:event_guar3MouseExited

    private void guar3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_guar3ActionPerformed
        if (factura_check.isSelected() == true) {
            ImprimirReporte("/reportes/factura.jasper");
        } else {
            if (recibo_check.isSelected() == true) {
                ImprimirReporte("/reportes/recibo.jasper");
            }
        }        // TODO add your handling code here:
    }//GEN-LAST:event_guar3ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    public static javax.swing.JComboBox<String> BancoCuenta;
    public static javax.swing.JLabel Condicion1;
    public static javax.swing.JLabel Condicion2;
    public static javax.swing.JTextField TranTarjeta;
    public static javax.swing.JButton btnDetalle;
    public static javax.swing.JTextField codigo_producto;
    public static javax.swing.JTextField codigo_venta;
    public static javax.swing.JCheckBox contado;
    public static javax.swing.JPanel contener_detalle;
    public static javax.swing.JPanel contener_guardar;
    public static javax.swing.JPanel contener_guardar1;
    public static javax.swing.JPanel contener_guardar2;
    public static javax.swing.JPanel contener_guardar3;
    public static javax.swing.JCheckBox credito;
    public static javax.swing.JTextField excentas;
    public static javax.swing.JCheckBox factura_check;
    public static javax.swing.JTextField fechas;
    public static javax.swing.JButton focus;
    public static javax.swing.JButton guar;
    public static javax.swing.JButton guar1;
    public static javax.swing.JButton guar2;
    public static javax.swing.JButton guar3;
    public static javax.swing.JTextField iva_10;
    public static javax.swing.JButton jButton1;
    public static javax.swing.JButton jButton2;
    public static javax.swing.JButton jButton3;
    public static javax.swing.JLabel jLabel1;
    public static javax.swing.JLabel jLabel10;
    public static javax.swing.JLabel jLabel11;
    public static javax.swing.JLabel jLabel12;
    public static javax.swing.JLabel jLabel2;
    public static javax.swing.JLabel jLabel3;
    public static javax.swing.JLabel jLabel4;
    public static javax.swing.JLabel jLabel5;
    public static javax.swing.JLabel jLabel6;
    public static javax.swing.JLabel jLabel7;
    public static javax.swing.JLabel jLabel8;
    public static javax.swing.JLabel jLabel9;
    public static javax.swing.JPanel jPanel1;
    public static javax.swing.JPanel jPanel2;
    public static javax.swing.JPanel jPanel3;
    public static javax.swing.JPanel jPanel4;
    public static javax.swing.JPanel jPanel5;
    public static javax.swing.JPanel jPanel6;
    public static javax.swing.JTextField nom_cli;
    public static javax.swing.JTextField nro_factura;
    public static javax.swing.JTextField nro_tim;
    public static javax.swing.JCheckBox recibo_check;
    public static javax.swing.JTextField ruc_cli;
    public static javax.swing.JScrollPane scroll;
    public static javax.swing.JTable tbl_venta;
    public static javax.swing.JTextField total_pagar;
    public static javax.swing.JCheckBox transferencia;
    // End of variables declaration//GEN-END:variables
}
