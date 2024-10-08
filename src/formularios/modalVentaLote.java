package formularios;

import clases.CustomSQLExceptionHandler;
import clases.DatabaseConnector;
import clases.DatabaseManager;
import clases.EventoTecladoUtil;
import clases.ReportPrinter;
import disenho.Formato;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/**
 *
 * @author Claudio Loncharich
 */
public final class modalVentaLote extends javax.swing.JInternalFrame {

    String accionBoton = "insertar";
    int cod_alquiler = 0;

    public modalVentaLote() {
        initComponents();
        ((javax.swing.plaf.basic.BasicInternalFrameUI) this.getUI()).setNorthPane(null);
        setResizable(false);
        titular.setEditable(false);
        lote_cod.setEditable(false);
        motivo_anulacion.setVisible(false);
        anulacion.setVisible(false);
        contener_detalle.setVisible(false);
        contener_detalle1.setVisible(false);
        contener_detalle2.setVisible(false);
        contener_detalle3.setVisible(true);
        MostrarTipoCobro();

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                documento_titular.requestFocusInWindow();
            }
        });

        monto_cuota.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                Formato.NomenclaturaNumero(monto_cuota);

            }
        });

        monto_entrega.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                Formato.NomenclaturaNumero(monto_entrega);

            }
        });

        monto_mantenimiento.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                Formato.NomenclaturaNumero(monto_mantenimiento);

            }
        });

    }

    private void BuscarTitular(String valor) {
        String query = "select documento AS documento,"
                + "CONCAT(cod_cliente,'-',nombres, ' ', apellidos) AS cliente_nombre from cliente\n"
                + "where documento='" + valor + "';";
        int contador = 0;

        Statement st1;
        try (Connection conn = DatabaseConnector.getConnection();
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery(query)) {
            while (rs.next()) {
                documento_titular.setText(rs.getString(1));
                titular.setText(rs.getString(2));
                contador = contador + 1;
            }
            if ((contador == 0) && (!documento_titular.getText().trim().isEmpty())) {
                JOptionPane.showMessageDialog(this, "El numero de documento ingresado no existe en la base de datos", "Aviso", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, ex);
        }
    }

    private void GrabarDatos() {
        //  tabla en base alquiler_lote_cliente
        try {
            String queryInsert = "INSERT INTO public.alquiler_lote_cliente(\n"
                    + "	cod_cliente, estado_registro,entrega, cuota, cod_lote, fecha_vencimiento,monto_mantenimiento,tipo,es_refinanciado,cuota_cant_ref,cantidad_cuota)\n"
                    + "	VALUES (?, ?, ?, ?, ?, ?,?,?,?,?,?);";

            String queryUpdate = "UPDATE public.alquiler_lote_cliente\n"
                    + "	SET  cod_cliente=?, entrega=?, cuota=?, cod_lote=?, fecha_vencimiento=?,monto_mantenimiento=?,es_refinanciado=?,cuota_cant_ref=?,cantidad_cuota=?\n"
                    + "	WHERE cod_cabecera=?;";

            String queryAnular = "UPDATE public.alquiler_lote_cliente\n"
                    + "	SET estado_registro='N',motivo_anula=?\n"
                    + "	WHERE cod_cabecera=?;";

            String cli = titular.getText();
            int indexCli = cli.indexOf('-');
            if (indexCli != -1) {
                cli = cli.substring(0, indexCli); // campo cod_cliente
            }
            String ent = monto_entrega.getText().replaceAll("\\p{Punct}", "");//campo entrega
            String cuo = monto_cuota.getText().replaceAll("\\p{Punct}", ""); // campo cuota
            String man = monto_mantenimiento.getText().replaceAll("\\p{Punct}", "");//campo monto_mantenimiento
            String lot = lote_cod.getText();
            int indexLote = lot.indexOf(')');
            if (indexLote != -1) {
                lot = lot.substring(0, indexLote); // campo cod_lote
            }
            String ref = "N";
            int cuoRef = 0;

            if (si.isSelected()) {
                ref = "S";
                cuoRef = Integer.parseInt(cuota_ref.getText());
            }

            DateFormat dateFormat = new SimpleDateFormat("yyy-MM-dd");
            String fecha_ven = dateFormat.format(fecha_venci.getDate());
            java.sql.Date fec = java.sql.Date.valueOf(fecha_ven);

            String canCu = cant_cuota.getText();

            int rowsAffected = 0;
            if ("insertar".equalsIgnoreCase(accionBoton)) {
                rowsAffected = DatabaseManager.insert(queryInsert, Integer.parseInt(cli), "A", Integer.parseInt(ent), Integer.parseInt(cuo), Integer.parseInt(lot), fec, Integer.parseInt(man), "V", ref, cuoRef, Integer.parseInt(canCu));
            } else if ("actualizar".equalsIgnoreCase(accionBoton)) {
                rowsAffected = DatabaseManager.update(queryUpdate, Integer.parseInt(cli), Integer.parseInt(ent), Integer.parseInt(cuo), Integer.parseInt(lot), fec, Integer.parseInt(man), ref, cuoRef, Integer.parseInt(canCu), cod_alquiler);
            } else if ("anular".equalsIgnoreCase(accionBoton)) {
                int valor = JOptionPane.showConfirmDialog(this, "Esta seguro de anular la venta del lote?", "Aviso!!", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if (valor == JOptionPane.YES_NO_OPTION) {
                    rowsAffected = DatabaseManager.update(queryAnular, motivo_anulacion.getText(), cod_alquiler);
                }

            }

            if (rowsAffected > 0) {
                if ("insertar".equalsIgnoreCase(accionBoton)) {
                    JOptionPane.showMessageDialog(this, "Venta de Lote registrada con exito", "AVISO", JOptionPane.PLAIN_MESSAGE, Formato.icono("/imagenes/check.png", 40, 40));
                } else if ("actualizar".equalsIgnoreCase(accionBoton)) {
                    JOptionPane.showMessageDialog(this, "Datos actualizado con exito", "AVISO", JOptionPane.PLAIN_MESSAGE, Formato.icono("/imagenes/check.png", 40, 40));
                }
                if ("anular".equalsIgnoreCase(accionBoton)) {
                    JOptionPane.showMessageDialog(this, "El Alquiler del Lote fue anulado", "AVISO", JOptionPane.PLAIN_MESSAGE, Formato.icono("/imagenes/check.png", 40, 40));
                }
                dispose();
                tablaVentaLote.mostrarTabla("");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            //JOptionPane.showMessageDialog(modalAlquiler.this, e.getMessage(), "Error al grabar/actualizar registro", JOptionPane.ERROR_MESSAGE);
            CustomSQLExceptionHandler.showCustomMessage(e);

        }
    }

    public void MostrarDatos(String opcion, int id, String accion) {
        cod_alquiler = id;
        accionBoton = accion;
        String query = "SELECT t.documento,CONCAT(t.cod_cliente,'-',t.nombres, ' ', t.apellidos) AS cliente_nombre,\n"
                + "CONCAT(l.cod_lote,') ',m.codigo, '-', l.numero_lote, '-', l.serie) AS lote_numero,\n"
                + "a.entrega,\n"
                + "a.cuota,\n"
                + "TO_CHAR(a.fecha_vencimiento, 'DD/MM/YYYY') AS fecha_vencimiento,\n"
                + "a.es_refinanciado,\n"
                + "a.cuota_cant_ref,\n"
                + "a.monto_mantenimiento,a.cantidad_cuota\n"
                + "FROM alquiler_lote_cliente AS a\n"
                + "INNER JOIN cliente AS t ON t.cod_cliente = a.cod_cliente\n"
                + "INNER JOIN lote AS l ON l.cod_lote = a.cod_lote\n"
                + "INNER JOIN manzana AS m ON m.cod_manzana = l.cod_manzana \n"
                + "where a.cod_cabecera =" + cod_alquiler + "";
        try (Connection conn = DatabaseConnector.getConnection();
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery(query)) {

            if (rs.next()) {
                double doc = Double.parseDouble(rs.getString(1));
                documento_titular.setValue(doc);
                titular.setText(rs.getString(2));
                lote_cod.setText(rs.getString(3));
                double ent = Double.parseDouble(rs.getString(4));
                monto_entrega.setValue(ent);
                double cuo = Double.parseDouble(rs.getString(5));
                monto_cuota.setValue(cuo);
                SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
                String fecha = rs.getString(6);
                Date dato = null;
                try {
                    dato = formato.parse(fecha);
                    fecha_venci.setDate(dato);
                } catch (ParseException ex) {
                    Logger.getLogger(modalVentaLote.class.getName()).log(Level.SEVERE, null, ex);
                }

                if (rs.getString(7).equals("S")) {
                    si.setSelected(true);
                    no.setSelected(false);
                } else if (rs.getString(7).equals("N")) {
                    no.setSelected(true);
                    si.setSelected(false);
                }

                cuota_ref.setText(rs.getString(8));
                double man = Double.parseDouble(rs.getString(9));
                monto_mantenimiento.setValue(man);

                int ente = Integer.parseInt(rs.getString(4));
                int cuot = Integer.parseInt(rs.getString(8));
                cant_cuota.setText(rs.getString(10));

                if (cuot != 0) {
                    int resul = ente / cuot;
                    DecimalFormat formatea = new DecimalFormat("#,##0");
                    monto_cuotaRef.setText("" + formatea.format(resul));
                }

                calcularMontoVentaTotal();

                if (opcion.equals("ver")) {
                    Formato.habilitarCampos(getContentPane(), false);
                    btnGuardar.setEnabled(false);
                    btnCancelar.setEnabled(false);
                    btnBuscarCliente.setEnabled(false);
                    btnBuscarLote.setEnabled(false);
                    contener_detalle.setVisible(true);
                    contener_detalle1.setVisible(true);
                    contener_detalle2.setVisible(true);
                    tipo_cobro.setEnabled(true);

                }
                if (opcion.equals("anulacion")) {
                    Formato.habilitarCampos(getContentPane(), false);
                    anulacion.setVisible(true);
                    motivo_anulacion.setVisible(true);
                    motivo_anulacion.setEnabled(true);
                    btnBuscarCliente.setEnabled(false);
                    btnBuscarLote.setEnabled(false);
                    btnCancelar.setEnabled(false);
                    btnGuardar.setEnabled(true);
                    motivo_anulacion.requestFocus();
                    jLabel4.setText("ANULACIÓN VENTA DE LOTES");
                    jLabel4.setForeground(Color.red);

                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error al ejecutar la consulta SQL: " + e.getMessage());
        }
    }

    void calcularMontoVentaTotal() {
        if (!monto_cuota.getText().isEmpty() && !monto_cuota.getText().equals("") && !cant_cuota.getText().isEmpty() && !cant_cuota.getText().equals("")) {
            int ent = Integer.parseInt(monto_cuota.getText().replaceAll("\\p{Punct}", ""));
            int cuo = Integer.parseInt(cant_cuota.getText());
            int resul = ent * cuo;
            DecimalFormat formatea = new DecimalFormat("#,##0");
            venta_total.setText("" + formatea.format(resul));

        }
    }
    
     void ImprimirReporte(String ruta){
      ReportPrinter reportPrinter = new ReportPrinter();
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("alquiler", cod_alquiler);
        reportPrinter.printReport(ruta, parameters);
 }
     
         
     void ImprimirReporteUsufructo(String ruta){
      ReportPrinter reportPrinter = new ReportPrinter();
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("CodAlquiler", cod_alquiler);
        parameters.put("letraDimension", "mmm");
        parameters.put("letraDimencion2", "mm");
        reportPrinter.printReport(ruta, parameters);
 }
     
     
      private void MostrarTipoCobro() {
        tipo_cobro.removeAllItems();
        String query = "Select descripcion from tipo_cobro where cod_tipo in(1,2,3,4)";
        Statement st1;
        int sum=0;
        try (Connection conn = DatabaseConnector.getConnection();
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery(query)) {
            while (rs.next()) {
                tipo_cobro.addItem(rs.getString(1));
                sum=sum+1;
            }
            tipo_cobro.addItem("TODOS");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, ex);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelModalCliente = new javax.swing.JPanel();
        contener_cancelar = new javax.swing.JPanel();
        btnCancelar = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        contener_salir = new javax.swing.JPanel();
        salir = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        titular = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        lote_cod = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        fecha_venci = new com.toedter.calendar.JDateChooser();
        anulacion = new javax.swing.JLabel();
        monto_cuota = new javax.swing.JFormattedTextField();
        documento_titular = new javax.swing.JFormattedTextField();
        monto_entrega = new javax.swing.JFormattedTextField();
        contener_guardar = new javax.swing.JPanel();
        btnGuardar = new javax.swing.JButton();
        contener_buscarCliente = new javax.swing.JPanel();
        btnBuscarCliente = new javax.swing.JButton();
        contener_buscarLote = new javax.swing.JPanel();
        btnBuscarLote = new javax.swing.JButton();
        motivo_anulacion = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        no = new javax.swing.JCheckBox();
        si = new javax.swing.JCheckBox();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        monto_mantenimiento = new javax.swing.JFormattedTextField();
        monto_cuotaRef = new javax.swing.JTextField();
        cuota_ref = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        cant_cuota = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        venta_total = new javax.swing.JTextField();
        contener_detalle = new javax.swing.JPanel();
        btnDetalle = new javax.swing.JButton();
        contener_detalle1 = new javax.swing.JPanel();
        btnDetalle1 = new javax.swing.JButton();
        contener_detalle2 = new javax.swing.JPanel();
        btnDetalle2 = new javax.swing.JButton();
        contener_detalle3 = new javax.swing.JPanel();
        btnDetalle3 = new javax.swing.JButton();
        tipo_cobro = new javax.swing.JComboBox<>();

        setBackground(new java.awt.Color(255, 255, 255));
        setBorder(javax.swing.BorderFactory.createEtchedBorder());
        setFrameIcon(null);
        setPreferredSize(new java.awt.Dimension(678, 519));

        panelModalCliente.setBackground(new java.awt.Color(255, 255, 255));
        panelModalCliente.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        contener_cancelar.setBackground(new java.awt.Color(80, 90, 100));

        btnCancelar.setBackground(new java.awt.Color(153, 204, 255));
        btnCancelar.setFont(new java.awt.Font("Roboto Medium", 1, 12)); // NOI18N
        btnCancelar.setForeground(new java.awt.Color(255, 255, 255));
        btnCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/cancel.png"))); // NOI18N
        btnCancelar.setText("CANCELAR");
        btnCancelar.setBorder(null);
        btnCancelar.setContentAreaFilled(false);
        btnCancelar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCancelar.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                btnCancelarFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                btnCancelarFocusLost(evt);
            }
        });
        btnCancelar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnCancelarMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnCancelarMouseExited(evt);
            }
        });
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout contener_cancelarLayout = new javax.swing.GroupLayout(contener_cancelar);
        contener_cancelar.setLayout(contener_cancelarLayout);
        contener_cancelarLayout.setHorizontalGroup(
            contener_cancelarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(contener_cancelarLayout.createSequentialGroup()
                .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        contener_cancelarLayout.setVerticalGroup(
            contener_cancelarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(contener_cancelarLayout.createSequentialGroup()
                .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        panelModalCliente.add(contener_cancelar, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 450, 110, -1));

        jLabel3.setFont(new java.awt.Font("Roboto Light", 0, 14)); // NOI18N
        jLabel3.setText("Documento Titular:");
        panelModalCliente.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 130, 34));

        jLabel4.setFont(new java.awt.Font("Roboto Medium", 0, 18)); // NOI18N
        jLabel4.setText("VENTA DE LOTE");
        panelModalCliente.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 540, -1));

        contener_salir.setBackground(new java.awt.Color(255, 255, 255));

        salir.setBackground(new java.awt.Color(255, 255, 255));
        salir.setFont(new java.awt.Font("Roboto Medium", 0, 18)); // NOI18N
        salir.setText("X");
        salir.setBorder(null);
        salir.setContentAreaFilled(false);
        salir.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        salir.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                salirMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                salirMouseExited(evt);
            }
        });
        salir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                salirActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout contener_salirLayout = new javax.swing.GroupLayout(contener_salir);
        contener_salir.setLayout(contener_salirLayout);
        contener_salirLayout.setHorizontalGroup(
            contener_salirLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 38, Short.MAX_VALUE)
            .addGroup(contener_salirLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(salir, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE))
        );
        contener_salirLayout.setVerticalGroup(
            contener_salirLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 26, Short.MAX_VALUE)
            .addGroup(contener_salirLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(salir, javax.swing.GroupLayout.DEFAULT_SIZE, 26, Short.MAX_VALUE))
        );

        panelModalCliente.add(contener_salir, new org.netbeans.lib.awtextra.AbsoluteConstraints(636, 0, -1, -1));

        jLabel5.setFont(new java.awt.Font("Roboto Light", 0, 14)); // NOI18N
        jLabel5.setText("Titular:");
        panelModalCliente.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 50, 130, 34));

        titular.setFont(new java.awt.Font("Roboto Light", 0, 12)); // NOI18N
        titular.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        titular.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                titularActionPerformed(evt);
            }
        });
        titular.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                titularKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                titularKeyTyped(evt);
            }
        });
        panelModalCliente.add(titular, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 80, 320, 32));

        jLabel7.setFont(new java.awt.Font("Roboto Light", 0, 14)); // NOI18N
        jLabel7.setText("Lote:");
        panelModalCliente.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, 80, 34));

        lote_cod.setFont(new java.awt.Font("Roboto Light", 0, 12)); // NOI18N
        lote_cod.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        lote_cod.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lote_codActionPerformed(evt);
            }
        });
        lote_cod.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                lote_codKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                lote_codKeyTyped(evt);
            }
        });
        panelModalCliente.add(lote_cod, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 140, 270, 32));

        jLabel10.setFont(new java.awt.Font("Roboto Light", 0, 14)); // NOI18N
        jLabel10.setText("Monto entrega:");
        panelModalCliente.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 230, 130, 34));

        jLabel13.setFont(new java.awt.Font("Roboto Light", 0, 14)); // NOI18N
        jLabel13.setText("Fecha vencimiento:");
        panelModalCliente.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 110, 130, 34));

        fecha_venci.setBackground(new java.awt.Color(204, 204, 204));
        fecha_venci.setForeground(new java.awt.Color(204, 204, 204));
        fecha_venci.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                fecha_venciFocusLost(evt);
            }
        });
        panelModalCliente.add(fecha_venci, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 140, 320, 32));

        anulacion.setFont(new java.awt.Font("Roboto Light", 0, 14)); // NOI18N
        anulacion.setText("Motivo Anulación:");
        panelModalCliente.add(anulacion, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 350, 130, 40));

        monto_cuota.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        monto_cuota.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(java.text.NumberFormat.getIntegerInstance())));
        monto_cuota.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                monto_cuotaFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                monto_cuotaFocusLost(evt);
            }
        });
        monto_cuota.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                monto_cuotaActionPerformed(evt);
            }
        });
        monto_cuota.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                monto_cuotaKeyTyped(evt);
            }
        });
        panelModalCliente.add(monto_cuota, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 200, 310, 32));

        documento_titular.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        documento_titular.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        documento_titular.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                documento_titularFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                documento_titularFocusLost(evt);
            }
        });
        documento_titular.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                documento_titularActionPerformed(evt);
            }
        });
        documento_titular.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                documento_titularKeyTyped(evt);
            }
        });
        panelModalCliente.add(documento_titular, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, 270, 32));

        monto_entrega.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        monto_entrega.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(java.text.NumberFormat.getIntegerInstance())));
        monto_entrega.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                monto_entregaFocusGained(evt);
            }
        });
        monto_entrega.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                monto_entregaKeyTyped(evt);
            }
        });
        panelModalCliente.add(monto_entrega, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 260, 320, 32));

        contener_guardar.setBackground(new java.awt.Color(80, 90, 100));

        btnGuardar.setBackground(new java.awt.Color(153, 204, 255));
        btnGuardar.setFont(new java.awt.Font("Roboto Medium", 1, 12)); // NOI18N
        btnGuardar.setForeground(new java.awt.Color(255, 255, 255));
        btnGuardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/save.png"))); // NOI18N
        btnGuardar.setText("GUARDAR");
        btnGuardar.setBorder(null);
        btnGuardar.setContentAreaFilled(false);
        btnGuardar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnGuardar.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                btnGuardarFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                btnGuardarFocusLost(evt);
            }
        });
        btnGuardar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnGuardarMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnGuardarMouseExited(evt);
            }
        });
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout contener_guardarLayout = new javax.swing.GroupLayout(contener_guardar);
        contener_guardar.setLayout(contener_guardarLayout);
        contener_guardarLayout.setHorizontalGroup(
            contener_guardarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(contener_guardarLayout.createSequentialGroup()
                .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        contener_guardarLayout.setVerticalGroup(
            contener_guardarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(contener_guardarLayout.createSequentialGroup()
                .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        panelModalCliente.add(contener_guardar, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 450, 110, -1));

        contener_buscarCliente.setBackground(new java.awt.Color(80, 90, 100));

        btnBuscarCliente.setBackground(new java.awt.Color(153, 204, 255));
        btnBuscarCliente.setFont(new java.awt.Font("Roboto Medium", 1, 12)); // NOI18N
        btnBuscarCliente.setForeground(new java.awt.Color(255, 255, 255));
        btnBuscarCliente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/buscar.png"))); // NOI18N
        btnBuscarCliente.setBorder(null);
        btnBuscarCliente.setContentAreaFilled(false);
        btnBuscarCliente.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnBuscarCliente.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                btnBuscarClienteFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                btnBuscarClienteFocusLost(evt);
            }
        });
        btnBuscarCliente.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnBuscarClienteMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnBuscarClienteMouseExited(evt);
            }
        });
        btnBuscarCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarClienteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout contener_buscarClienteLayout = new javax.swing.GroupLayout(contener_buscarCliente);
        contener_buscarCliente.setLayout(contener_buscarClienteLayout);
        contener_buscarClienteLayout.setHorizontalGroup(
            contener_buscarClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnBuscarCliente, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
        );
        contener_buscarClienteLayout.setVerticalGroup(
            contener_buscarClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(contener_buscarClienteLayout.createSequentialGroup()
                .addComponent(btnBuscarCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        panelModalCliente.add(contener_buscarCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 80, 40, -1));

        contener_buscarLote.setBackground(new java.awt.Color(80, 90, 100));

        btnBuscarLote.setBackground(new java.awt.Color(153, 204, 255));
        btnBuscarLote.setFont(new java.awt.Font("Roboto Medium", 1, 12)); // NOI18N
        btnBuscarLote.setForeground(new java.awt.Color(255, 255, 255));
        btnBuscarLote.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/buscar.png"))); // NOI18N
        btnBuscarLote.setBorder(null);
        btnBuscarLote.setContentAreaFilled(false);
        btnBuscarLote.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnBuscarLote.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                btnBuscarLoteFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                btnBuscarLoteFocusLost(evt);
            }
        });
        btnBuscarLote.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnBuscarLoteMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnBuscarLoteMouseExited(evt);
            }
        });
        btnBuscarLote.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarLoteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout contener_buscarLoteLayout = new javax.swing.GroupLayout(contener_buscarLote);
        contener_buscarLote.setLayout(contener_buscarLoteLayout);
        contener_buscarLoteLayout.setHorizontalGroup(
            contener_buscarLoteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnBuscarLote, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
        );
        contener_buscarLoteLayout.setVerticalGroup(
            contener_buscarLoteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(contener_buscarLoteLayout.createSequentialGroup()
                .addComponent(btnBuscarLote, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        panelModalCliente.add(contener_buscarLote, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 140, 40, -1));

        motivo_anulacion.setFont(new java.awt.Font("Roboto Light", 0, 12)); // NOI18N
        motivo_anulacion.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        motivo_anulacion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                motivo_anulacionActionPerformed(evt);
            }
        });
        motivo_anulacion.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                motivo_anulacionKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                motivo_anulacionKeyTyped(evt);
            }
        });
        panelModalCliente.add(motivo_anulacion, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 380, 650, 32));

        jLabel15.setFont(new java.awt.Font("Roboto Light", 0, 14)); // NOI18N
        jLabel15.setText("Monto cuota:");
        panelModalCliente.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 170, 130, 34));

        no.setSelected(true);
        no.setText("NO");
        no.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                noActionPerformed(evt);
            }
        });
        panelModalCliente.add(no, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 320, 60, 32));

        si.setText("SI");
        si.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                siActionPerformed(evt);
            }
        });
        panelModalCliente.add(si, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 320, 60, 32));

        jLabel11.setFont(new java.awt.Font("Roboto Light", 0, 14)); // NOI18N
        jLabel11.setText("Refinanciar la entrega?");
        panelModalCliente.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 290, 150, 30));

        jLabel12.setFont(new java.awt.Font("Roboto Light", 0, 14)); // NOI18N
        jLabel12.setText("Cantidad Cuota Ref.");
        panelModalCliente.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 284, 180, 40));

        jLabel14.setFont(new java.awt.Font("Roboto Light", 0, 14)); // NOI18N
        jLabel14.setText("Monto Refinanciación:");
        panelModalCliente.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 284, 200, 40));

        jLabel16.setFont(new java.awt.Font("Roboto Light", 0, 14)); // NOI18N
        jLabel16.setText("Monto mantenimiento:");
        panelModalCliente.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 230, 170, 34));

        monto_mantenimiento.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        monto_mantenimiento.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(java.text.NumberFormat.getIntegerInstance())));
        monto_mantenimiento.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                monto_mantenimientoFocusGained(evt);
            }
        });
        monto_mantenimiento.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                monto_mantenimientoKeyTyped(evt);
            }
        });
        panelModalCliente.add(monto_mantenimiento, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 260, 310, 32));

        monto_cuotaRef.setText("0");
        monto_cuotaRef.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        monto_cuotaRef.setEnabled(false);
        panelModalCliente.add(monto_cuotaRef, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 320, 320, 32));

        cuota_ref.setText("0");
        cuota_ref.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        cuota_ref.setEnabled(false);
        cuota_ref.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cuota_refActionPerformed(evt);
            }
        });
        cuota_ref.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                cuota_refKeyTyped(evt);
            }
        });
        panelModalCliente.add(cuota_ref, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 320, 160, 32));

        jLabel17.setFont(new java.awt.Font("Roboto Light", 0, 14)); // NOI18N
        jLabel17.setText("Cant. cuota");
        panelModalCliente.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 170, 130, 34));

        cant_cuota.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        cant_cuota.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                cant_cuotaFocusLost(evt);
            }
        });
        cant_cuota.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cant_cuotaActionPerformed(evt);
            }
        });
        cant_cuota.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                cant_cuotaKeyTyped(evt);
            }
        });
        panelModalCliente.add(cant_cuota, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 200, 130, 32));

        jLabel18.setFont(new java.awt.Font("Roboto Light", 0, 14)); // NOI18N
        jLabel18.setText("Monto Total Cuotas:");
        panelModalCliente.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 170, 130, 34));

        venta_total.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        venta_total.setEnabled(false);
        venta_total.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                venta_totalActionPerformed(evt);
            }
        });
        venta_total.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                venta_totalKeyTyped(evt);
            }
        });
        panelModalCliente.add(venta_total, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 200, 180, 32));

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
                .addComponent(btnDetalle, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        panelModalCliente.add(contener_detalle, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 450, 40, -1));

        contener_detalle1.setBackground(new java.awt.Color(80, 90, 100));

        btnDetalle1.setBackground(new java.awt.Color(153, 204, 255));
        btnDetalle1.setFont(new java.awt.Font("Roboto Medium", 1, 12)); // NOI18N
        btnDetalle1.setForeground(new java.awt.Color(255, 255, 255));
        btnDetalle1.setText("LIQ.VENTA");
        btnDetalle1.setBorder(null);
        btnDetalle1.setContentAreaFilled(false);
        btnDetalle1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnDetalle1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnDetalle1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                btnDetalle1FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                btnDetalle1FocusLost(evt);
            }
        });
        btnDetalle1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnDetalle1MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnDetalle1MouseExited(evt);
            }
        });
        btnDetalle1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDetalle1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout contener_detalle1Layout = new javax.swing.GroupLayout(contener_detalle1);
        contener_detalle1.setLayout(contener_detalle1Layout);
        contener_detalle1Layout.setHorizontalGroup(
            contener_detalle1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, contener_detalle1Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(btnDetalle1, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        contener_detalle1Layout.setVerticalGroup(
            contener_detalle1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(contener_detalle1Layout.createSequentialGroup()
                .addComponent(btnDetalle1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        panelModalCliente.add(contener_detalle1, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 450, 60, -1));

        contener_detalle2.setBackground(new java.awt.Color(80, 90, 100));

        btnDetalle2.setBackground(new java.awt.Color(153, 204, 255));
        btnDetalle2.setFont(new java.awt.Font("Roboto Medium", 1, 12)); // NOI18N
        btnDetalle2.setForeground(new java.awt.Color(255, 255, 255));
        btnDetalle2.setText("LIQ. MANTENIMIENTO");
        btnDetalle2.setBorder(null);
        btnDetalle2.setContentAreaFilled(false);
        btnDetalle2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnDetalle2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnDetalle2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                btnDetalle2FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                btnDetalle2FocusLost(evt);
            }
        });
        btnDetalle2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnDetalle2MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnDetalle2MouseExited(evt);
            }
        });
        btnDetalle2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDetalle2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout contener_detalle2Layout = new javax.swing.GroupLayout(contener_detalle2);
        contener_detalle2.setLayout(contener_detalle2Layout);
        contener_detalle2Layout.setHorizontalGroup(
            contener_detalle2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnDetalle2, javax.swing.GroupLayout.PREFERRED_SIZE, 70, Short.MAX_VALUE)
        );
        contener_detalle2Layout.setVerticalGroup(
            contener_detalle2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(contener_detalle2Layout.createSequentialGroup()
                .addComponent(btnDetalle2, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        panelModalCliente.add(contener_detalle2, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 450, 70, -1));

        contener_detalle3.setBackground(new java.awt.Color(80, 90, 100));

        btnDetalle3.setBackground(new java.awt.Color(153, 204, 255));
        btnDetalle3.setFont(new java.awt.Font("Roboto Medium", 1, 12)); // NOI18N
        btnDetalle3.setForeground(new java.awt.Color(255, 255, 255));
        btnDetalle3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/detalle.png"))); // NOI18N
        btnDetalle3.setBorder(null);
        btnDetalle3.setContentAreaFilled(false);
        btnDetalle3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnDetalle3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnDetalle3.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                btnDetalle3FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                btnDetalle3FocusLost(evt);
            }
        });
        btnDetalle3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnDetalle3MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnDetalle3MouseExited(evt);
            }
        });
        btnDetalle3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDetalle3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout contener_detalle3Layout = new javax.swing.GroupLayout(contener_detalle3);
        contener_detalle3.setLayout(contener_detalle3Layout);
        contener_detalle3Layout.setHorizontalGroup(
            contener_detalle3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(contener_detalle3Layout.createSequentialGroup()
                .addComponent(btnDetalle3, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        contener_detalle3Layout.setVerticalGroup(
            contener_detalle3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(contener_detalle3Layout.createSequentialGroup()
                .addComponent(btnDetalle3, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        panelModalCliente.add(contener_detalle3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 410, 40, -1));

        tipo_cobro.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        tipo_cobro.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tipo_cobroFocusGained(evt);
            }
        });
        tipo_cobro.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tipo_cobroMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                tipo_cobroMouseEntered(evt);
            }
        });
        tipo_cobro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tipo_cobroActionPerformed(evt);
            }
        });
        panelModalCliente.add(tipo_cobro, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 450, 190, 32));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelModalCliente, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelModalCliente, javax.swing.GroupLayout.DEFAULT_SIZE, 495, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCancelarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancelarMouseEntered
        contener_cancelar.setBackground(new Color(51, 51, 51));            // TODO add your handling code here:
    }//GEN-LAST:event_btnCancelarMouseEntered

    private void btnCancelarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancelarMouseExited
        contener_cancelar.setBackground(new Color(80, 90, 100));       // TODO add your handling code here:
    }//GEN-LAST:event_btnCancelarMouseExited

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        Formato.limpiarCampos(getContentPane());
        Formato.habilitarCampos(getContentPane(), true);
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnCancelarFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_btnCancelarFocusGained

        // TODO add your handling code here:
    }//GEN-LAST:event_btnCancelarFocusGained

    private void btnCancelarFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_btnCancelarFocusLost

        // TODO add your handling code here:
    }//GEN-LAST:event_btnCancelarFocusLost

    private void salirMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_salirMouseEntered
        contener_salir.setBackground(Color.red);        // TODO add your handling code here:
    }//GEN-LAST:event_salirMouseEntered

    private void salirMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_salirMouseExited
        contener_salir.setBackground(Color.white);         // TODO add your handling code here:
    }//GEN-LAST:event_salirMouseExited

    private void salirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_salirActionPerformed
        dispose();        // TODO add your handling code here:
    }//GEN-LAST:event_salirActionPerformed

    private void titularActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_titularActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_titularActionPerformed

    private void titularKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_titularKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_titularKeyReleased

    private void titularKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_titularKeyTyped
        EventoTecladoUtil.permitirNumerosComasYGuiones(evt);        // TODO add your handling code here:
    }//GEN-LAST:event_titularKeyTyped

    private void lote_codActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lote_codActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_lote_codActionPerformed

    private void lote_codKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_lote_codKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_lote_codKeyReleased

    private void lote_codKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_lote_codKeyTyped
        EventoTecladoUtil.permitirNumerosComasYGuiones(evt);          // TODO add your handling code here:
    }//GEN-LAST:event_lote_codKeyTyped

    private void fecha_venciFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_fecha_venciFocusLost

        // TODO add your handling code here:
    }//GEN-LAST:event_fecha_venciFocusLost

    private void monto_cuotaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_monto_cuotaFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_monto_cuotaFocusGained

    private void monto_cuotaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_monto_cuotaKeyTyped

    }//GEN-LAST:event_monto_cuotaKeyTyped

    private void documento_titularFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_documento_titularFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_documento_titularFocusGained

    private void documento_titularKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_documento_titularKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_documento_titularKeyTyped

    private void monto_entregaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_monto_entregaFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_monto_entregaFocusGained

    private void monto_entregaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_monto_entregaKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_monto_entregaKeyTyped

    private void btnGuardarFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_btnGuardarFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_btnGuardarFocusGained

    private void btnGuardarFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_btnGuardarFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_btnGuardarFocusLost

    private void btnGuardarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnGuardarMouseEntered
        contener_guardar.setBackground(new Color(51, 51, 51));         // TODO add your handling code here:
    }//GEN-LAST:event_btnGuardarMouseEntered

    private void btnGuardarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnGuardarMouseExited
        contener_guardar.setBackground(new Color(80, 90, 100));        // TODO add your handling code here:
    }//GEN-LAST:event_btnGuardarMouseExited

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        if (!accionBoton.equals("anular")) {
            motivo_anulacion.setText("0");
        }
        if (Formato.verificarCampos(getContentPane()) == true) {
            GrabarDatos();
            motivo_anulacion.setText("");
        } else {
            JOptionPane.showMessageDialog(this, "Debe ingresar todos los campos", "Aviso", JOptionPane.INFORMATION_MESSAGE);
        }        // TODO add your handling code here:
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void btnBuscarClienteFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_btnBuscarClienteFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_btnBuscarClienteFocusGained

    private void btnBuscarClienteFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_btnBuscarClienteFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_btnBuscarClienteFocusLost

    private void btnBuscarClienteMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBuscarClienteMouseEntered
        contener_buscarCliente.setBackground(new Color(51, 51, 51));        // TODO add your handling code here:
    }//GEN-LAST:event_btnBuscarClienteMouseEntered

    private void btnBuscarClienteMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBuscarClienteMouseExited
        contener_buscarCliente.setBackground(new Color(80, 90, 100));         // TODO add your handling code here:
    }//GEN-LAST:event_btnBuscarClienteMouseExited

    private void btnBuscarClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarClienteActionPerformed
        JTextField[] tfParam = new JTextField[2];
        tfParam[0] = documento_titular;
        tfParam[1] = titular;

// Modifica la consulta SQL base
        String sql = "select documento AS documento,"
                + "CONCAT(cod_cliente,'-',nombres, ' ', apellidos) AS cliente_nombre from cliente";

// Especifica las columnas donde se realizará la búsqueda
        String[] searchColumns = {"documento", "nombres", "apellidos"};

        buscador pp = new buscador(sql, new String[]{"Documento", "Titular"}, 2, tfParam, "Order by cod_cliente", searchColumns);
        Dimension desktopSize = principalMenu.escritorio.getSize();
        Dimension FrameSize = pp.getSize();
        pp.setLocation((desktopSize.width - FrameSize.width) / 2, (desktopSize.height - FrameSize.height) / 4);
        pp.setVisible(true);
        // TODO add your handling code here:
    }//GEN-LAST:event_btnBuscarClienteActionPerformed

    private void btnBuscarLoteFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_btnBuscarLoteFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_btnBuscarLoteFocusGained

    private void btnBuscarLoteFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_btnBuscarLoteFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_btnBuscarLoteFocusLost

    private void btnBuscarLoteMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBuscarLoteMouseEntered
        contener_buscarLote.setBackground(new Color(51, 51, 51));          // TODO add your handling code here:
    }//GEN-LAST:event_btnBuscarLoteMouseEntered

    private void btnBuscarLoteMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBuscarLoteMouseExited
        contener_buscarLote.setBackground(new Color(80, 90, 100));         // TODO add your handling code here:
    }//GEN-LAST:event_btnBuscarLoteMouseExited

    private void btnBuscarLoteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarLoteActionPerformed
        JTextField[] tfParam = new JTextField[1];
        tfParam[0] = lote_cod;

        String sql = "SELECT (l.cod_lote || ') ' || m.codigo || '-' || l.numero_lote || '-' || l.serie) AS lote_numero "
                + "FROM lote AS l "
                + "INNER JOIN manzana AS m ON m.cod_manzana = l.cod_manzana "
                + "WHERE l.estado_registro = 'L' ";

        sql += " AND m.codigo || '-' || l.numero_lote || '-' || l.serie LIKE  ";

        buscadorLote pp = new buscadorLote(sql, new String[]{"Lotes Libres"}, 1, tfParam, "ORDER BY l.cod_lote", new String[]{"lote_numero"});

        Dimension desktopSize = principalMenu.escritorio.getSize();
        Dimension FrameSize = pp.getSize();
        pp.setLocation((desktopSize.width - FrameSize.width) / 2, (desktopSize.height - FrameSize.height) / 4);
        pp.setVisible(true);

        // TODO add your handling code here:
    }//GEN-LAST:event_btnBuscarLoteActionPerformed

    private void documento_titularActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_documento_titularActionPerformed
        titular.setText("");
        BuscarTitular(documento_titular.getText().replaceAll("\\p{Punct}", ""));        // TODO add your handling code here:
    }//GEN-LAST:event_documento_titularActionPerformed

    private void documento_titularFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_documento_titularFocusLost
        titular.setText("");
        BuscarTitular(documento_titular.getText().replaceAll("\\p{Punct}", ""));         // TODO add your handling code here:
    }//GEN-LAST:event_documento_titularFocusLost

    private void motivo_anulacionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_motivo_anulacionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_motivo_anulacionActionPerformed

    private void motivo_anulacionKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_motivo_anulacionKeyReleased

    }//GEN-LAST:event_motivo_anulacionKeyReleased

    private void motivo_anulacionKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_motivo_anulacionKeyTyped
        EventoTecladoUtil.convertirAMayusculas(evt);       // TODO add your handling code here:
    }//GEN-LAST:event_motivo_anulacionKeyTyped

    private void noActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_noActionPerformed
        si.setSelected(false);
        no.setSelected(true);
        cuota_ref.setEnabled(false);
        cuota_ref.setText("0");
        monto_cuotaRef.setText("0");
        // TODO add your handling code here:
    }//GEN-LAST:event_noActionPerformed

    private void siActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_siActionPerformed
        si.setSelected(true);
        no.setSelected(false);
        cuota_ref.setEnabled(true);
        cuota_ref.requestFocus();
        cuota_ref.setText("");
        monto_cuotaRef.setText("");
    }//GEN-LAST:event_siActionPerformed

    private void monto_mantenimientoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_monto_mantenimientoFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_monto_mantenimientoFocusGained

    private void monto_mantenimientoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_monto_mantenimientoKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_monto_mantenimientoKeyTyped

    private void cuota_refActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cuota_refActionPerformed
        if (!monto_entrega.getText().isEmpty() && !monto_entrega.getText().equals("")) {
            int ent = Integer.parseInt(monto_entrega.getText().replaceAll("\\p{Punct}", ""));
            int cuo = Integer.parseInt(cuota_ref.getText());
            int resul = ent / cuo;
            DecimalFormat formatea = new DecimalFormat("#,##0");
            monto_cuotaRef.setText("" + formatea.format(resul));

        }// TODO add your handling code here:
    }//GEN-LAST:event_cuota_refActionPerformed

    private void cuota_refKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cuota_refKeyTyped
        EventoTecladoUtil.permitirSoloDigitos(evt);            // TODO add your handling code here:
    }//GEN-LAST:event_cuota_refKeyTyped

    private void cant_cuotaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cant_cuotaActionPerformed
        calcularMontoVentaTotal();         // TODO add your handling code here:
    }//GEN-LAST:event_cant_cuotaActionPerformed

    private void cant_cuotaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cant_cuotaKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_cant_cuotaKeyTyped

    private void venta_totalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_venta_totalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_venta_totalActionPerformed

    private void venta_totalKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_venta_totalKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_venta_totalKeyTyped

    private void monto_cuotaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_monto_cuotaFocusLost
        calcularMontoVentaTotal();        // TODO add your handling code here:
    }//GEN-LAST:event_monto_cuotaFocusLost

    private void cant_cuotaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cant_cuotaFocusLost
        calcularMontoVentaTotal();        // TODO add your handling code here:
    }//GEN-LAST:event_cant_cuotaFocusLost

    private void monto_cuotaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_monto_cuotaActionPerformed
        calcularMontoVentaTotal();         // TODO add your handling code here:
    }//GEN-LAST:event_monto_cuotaActionPerformed

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
        modalDetalleCobro c = new modalDetalleCobro();
        principalMenu.escritorio.add(c);
        Dimension desktopSize = principalMenu.escritorio.getSize();
        Dimension FrameSize = c.getSize();
        c.setLocation((desktopSize.width - FrameSize.width) / 2, (desktopSize.height - FrameSize.height) / 6);
        c.show();
        c.mostrarTabla(cod_alquiler, "ver",1000);
        // TODO add your handling code here:
    }//GEN-LAST:event_btnDetalleActionPerformed

    private void btnDetalle1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_btnDetalle1FocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_btnDetalle1FocusGained

    private void btnDetalle1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_btnDetalle1FocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_btnDetalle1FocusLost

    private void btnDetalle1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDetalle1MouseEntered
 contener_detalle1.setBackground(new Color(51, 51, 51));        // TODO add your handling code here:
    }//GEN-LAST:event_btnDetalle1MouseEntered

    private void btnDetalle1MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDetalle1MouseExited
 contener_detalle1.setBackground(new Color(80, 90, 100));        // TODO add your handling code here:
    }//GEN-LAST:event_btnDetalle1MouseExited

    private void btnDetalle1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDetalle1ActionPerformed
ImprimirReporte("/reportes/LiquidacionTitular.jasper");         // TODO add your handling code here:
    }//GEN-LAST:event_btnDetalle1ActionPerformed

    private void btnDetalle2FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_btnDetalle2FocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_btnDetalle2FocusGained

    private void btnDetalle2FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_btnDetalle2FocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_btnDetalle2FocusLost

    private void btnDetalle2MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDetalle2MouseEntered
contener_detalle2.setBackground(new Color(51, 51, 51));         // TODO add your handling code here:
    }//GEN-LAST:event_btnDetalle2MouseEntered

    private void btnDetalle2MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDetalle2MouseExited
contener_detalle2.setBackground(new Color(80, 90, 100));           // TODO add your handling code here:
    }//GEN-LAST:event_btnDetalle2MouseExited

    private void btnDetalle2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDetalle2ActionPerformed
     if (tipo_cobro.getSelectedItem().equals("PAGO DE MANTENIMIENTO")){
        ImprimirReporte("/reportes/LiquidacionTitularMantenimiento.jasper");
     }
      if (tipo_cobro.getSelectedItem().equals("PAGO DE CUOTA")){
          ImprimirReporte("/reportes/LiquidacionTitular.jasper");  
      }// TODO add your handling code here:
    }//GEN-LAST:event_btnDetalle2ActionPerformed

    private void btnDetalle3FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_btnDetalle3FocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_btnDetalle3FocusGained

    private void btnDetalle3FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_btnDetalle3FocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_btnDetalle3FocusLost

    private void btnDetalle3MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDetalle3MouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_btnDetalle3MouseEntered

    private void btnDetalle3MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDetalle3MouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_btnDetalle3MouseExited

    private void btnDetalle3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDetalle3ActionPerformed
ImprimirReporteUsufructo("/reportes/ContratoDeUsufructo.jasper");          // TODO add your handling code here:
    }//GEN-LAST:event_btnDetalle3ActionPerformed

    private void tipo_cobroFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tipo_cobroFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_tipo_cobroFocusGained

    private void tipo_cobroMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tipo_cobroMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tipo_cobroMouseClicked

    private void tipo_cobroMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tipo_cobroMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_tipo_cobroMouseEntered

    private void tipo_cobroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tipo_cobroActionPerformed

    }//GEN-LAST:event_tipo_cobroActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    public static javax.swing.JLabel anulacion;
    public static javax.swing.JButton btnBuscarCliente;
    public static javax.swing.JButton btnBuscarLote;
    public static javax.swing.JButton btnCancelar;
    public static javax.swing.JButton btnDetalle;
    public static javax.swing.JButton btnDetalle1;
    public static javax.swing.JButton btnDetalle2;
    public static javax.swing.JButton btnDetalle3;
    public static javax.swing.JButton btnGuardar;
    public static javax.swing.JTextField cant_cuota;
    public static javax.swing.JPanel contener_buscarCliente;
    public static javax.swing.JPanel contener_buscarLote;
    public static javax.swing.JPanel contener_cancelar;
    public static javax.swing.JPanel contener_detalle;
    public static javax.swing.JPanel contener_detalle1;
    public static javax.swing.JPanel contener_detalle2;
    public static javax.swing.JPanel contener_detalle3;
    public static javax.swing.JPanel contener_guardar;
    public static javax.swing.JPanel contener_salir;
    public static javax.swing.JTextField cuota_ref;
    public static javax.swing.JFormattedTextField documento_titular;
    public static com.toedter.calendar.JDateChooser fecha_venci;
    public static javax.swing.JLabel jLabel10;
    public static javax.swing.JLabel jLabel11;
    public static javax.swing.JLabel jLabel12;
    public static javax.swing.JLabel jLabel13;
    public static javax.swing.JLabel jLabel14;
    public static javax.swing.JLabel jLabel15;
    public static javax.swing.JLabel jLabel16;
    public static javax.swing.JLabel jLabel17;
    public static javax.swing.JLabel jLabel18;
    public static javax.swing.JLabel jLabel3;
    public static javax.swing.JLabel jLabel4;
    public static javax.swing.JLabel jLabel5;
    public static javax.swing.JLabel jLabel7;
    public static javax.swing.JTextField lote_cod;
    public static javax.swing.JFormattedTextField monto_cuota;
    public static javax.swing.JTextField monto_cuotaRef;
    public static javax.swing.JFormattedTextField monto_entrega;
    public static javax.swing.JFormattedTextField monto_mantenimiento;
    public static javax.swing.JTextField motivo_anulacion;
    public static javax.swing.JCheckBox no;
    public static javax.swing.JPanel panelModalCliente;
    public static javax.swing.JButton salir;
    public static javax.swing.JCheckBox si;
    public static javax.swing.JComboBox<String> tipo_cobro;
    public static javax.swing.JTextField titular;
    public static javax.swing.JTextField venta_total;
    // End of variables declaration//GEN-END:variables
}
