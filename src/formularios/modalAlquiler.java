package formularios;

import clases.DatabaseConnector;
import clases.DatabaseManager;
import clases.EventoTecladoUtil;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/**
 *
 * @author Claudio Loncharich
 */
public final class modalAlquiler extends javax.swing.JInternalFrame {

    String accionBoton = "insertar";
    int cod_alquiler = 0;

    public modalAlquiler() {
        initComponents();
        ((javax.swing.plaf.basic.BasicInternalFrameUI) this.getUI()).setNorthPane(null);
        setResizable(false);
        titular.setEditable(false);
        lote_cod.setEditable(false);
        motivo_anulacion.setVisible(false);
        anulacion.setVisible(false);

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                documento_titular.requestFocusInWindow();
            }
        });

        documento_titular.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                Formato.NomenclaturaNumero(documento_titular);

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

    }

    private void BuscarTitular(String valor) {
        String query = "select TO_CHAR(CAST(documento AS numeric), 'FM999G999G999G990') AS documento,"
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
                    + "	cod_cliente, estado_registro,entrega, cuota, cod_lote, fecha_vencimiento)\n"
                    + "	VALUES (?, ?, ?, ?, ?, ?);";

            String queryUpdate = "UPDATE public.alquiler_lote_cliente\n"
                    + "	SET  cod_cliente=?, entrega=?, cuota=?, cod_lote=?, fecha_vencimiento=?\n"
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
            String lot = lote_cod.getText();
            int indexLote = lot.indexOf(')');
            if (indexLote != -1) {
                lot = lot.substring(0, indexLote); // campo cod_lote
            }

            DateFormat dateFormat = new SimpleDateFormat("yyy-MM-dd");
            String fecha_ven = dateFormat.format(fecha_venci.getDate());
            java.sql.Date fec = java.sql.Date.valueOf(fecha_ven);

            int rowsAffected = 0;
            if ("insertar".equalsIgnoreCase(accionBoton)) {
                rowsAffected = DatabaseManager.insert(queryInsert, Integer.parseInt(cli), "A", Integer.parseInt(ent), Integer.parseInt(cuo), Integer.parseInt(lot), fec);
            } else if ("actualizar".equalsIgnoreCase(accionBoton)) {
                rowsAffected = DatabaseManager.update(queryUpdate, Integer.parseInt(cli), Integer.parseInt(ent), Integer.parseInt(cuo), Integer.parseInt(lot), fec, cod_alquiler);
            } else if ("anular".equalsIgnoreCase(accionBoton)) {
                int valor = JOptionPane.showConfirmDialog(this, "Esta seguro de anular el Alquiler?", "Aviso!!", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if (valor == JOptionPane.YES_NO_OPTION) {
                    rowsAffected = DatabaseManager.update(queryAnular,motivo_anulacion.getText(), cod_alquiler);
                }
                
            }

            if (rowsAffected > 0) {
                if ("insertar".equalsIgnoreCase(accionBoton)) {
                    JOptionPane.showMessageDialog(this, "Alquiler registrado con exito", "AVISO", JOptionPane.PLAIN_MESSAGE, Formato.icono("/imagenes/check.png", 40, 40));
                } else if ("actualizar".equalsIgnoreCase(accionBoton)) {
                    JOptionPane.showMessageDialog(this, "Datos actualizado con exito", "AVISO", JOptionPane.PLAIN_MESSAGE, Formato.icono("/imagenes/check.png", 40, 40));
                }
                if ("anular".equalsIgnoreCase(accionBoton)) {
                    JOptionPane.showMessageDialog(this, "El Alquiler del Lote fue anulado", "AVISO", JOptionPane.PLAIN_MESSAGE, Formato.icono("/imagenes/check.png", 40, 40));
                }
                dispose();
                tablaAlquiler.mostrarTabla("");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(modalAlquiler.this, e.getMessage(), "Error al grabar/actualizar registro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void MostrarDatos(String opcion, int id, String accion) {
        cod_alquiler = id;
        accionBoton = accion;
        String query = "SELECT t.documento,CONCAT(t.cod_cliente,'-',t.nombres, ' ', t.apellidos) AS cliente_nombre,\n"
                + "CONCAT(l.cod_lote,') ',m.codigo, '-', l.numero_lote, '-', l.serie) AS lote_numero,\n"
                + "a.entrega,\n"
                + "a.cuota,\n"
                + "TO_CHAR(a.fecha_vencimiento, 'DD/MM/YYYY') AS fecha_vencimiento\n"
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
                    Logger.getLogger(modalAlquiler.class.getName()).log(Level.SEVERE, null, ex);
                }

                if (opcion.equals("ver")) {
                    Formato.habilitarCampos(getContentPane(), false);
                    btnGuardar.setEnabled(false);
                    btnCancelar.setEnabled(false);
                    btnBuscarCliente.setEnabled(false);
                    btnBuscarLote.setEnabled(false);

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

                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error al ejecutar la consulta SQL: " + e.getMessage());
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

        setBackground(new java.awt.Color(255, 255, 255));
        setBorder(javax.swing.BorderFactory.createEtchedBorder());
        setFrameIcon(null);
        setPreferredSize(new java.awt.Dimension(678, 358));

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

        panelModalCliente.add(contener_cancelar, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 300, 110, -1));

        jLabel3.setFont(new java.awt.Font("Roboto Light", 0, 14)); // NOI18N
        jLabel3.setText("Documento Titular:");
        panelModalCliente.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 130, 34));

        jLabel4.setFont(new java.awt.Font("Roboto Medium", 0, 18)); // NOI18N
        jLabel4.setText("ALQUILER DE LOTE");
        panelModalCliente.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 170, -1));

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
        panelModalCliente.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 110, 130, 34));

        jLabel13.setFont(new java.awt.Font("Roboto Light", 0, 14)); // NOI18N
        jLabel13.setText("Fecha vencimiento:");
        panelModalCliente.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 170, 130, 34));

        fecha_venci.setBackground(new java.awt.Color(204, 204, 204));
        fecha_venci.setForeground(new java.awt.Color(204, 204, 204));
        fecha_venci.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                fecha_venciFocusLost(evt);
            }
        });
        panelModalCliente.add(fecha_venci, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 200, 320, 32));

        anulacion.setFont(new java.awt.Font("Roboto Light", 0, 14)); // NOI18N
        anulacion.setText("Motivo Anulación:");
        panelModalCliente.add(anulacion, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 230, 130, 40));

        monto_cuota.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        monto_cuota.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(java.text.NumberFormat.getIntegerInstance())));
        monto_cuota.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                monto_cuotaFocusGained(evt);
            }
        });
        monto_cuota.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                monto_cuotaKeyTyped(evt);
            }
        });
        panelModalCliente.add(monto_cuota, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 200, 310, 32));

        documento_titular.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        documento_titular.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(java.text.NumberFormat.getIntegerInstance())));
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
        panelModalCliente.add(monto_entrega, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 140, 320, 32));

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

        panelModalCliente.add(contener_guardar, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 300, 110, -1));

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
        panelModalCliente.add(motivo_anulacion, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 260, 650, 32));

        jLabel15.setFont(new java.awt.Font("Roboto Light", 0, 14)); // NOI18N
        jLabel15.setText("Monto cuota:");
        panelModalCliente.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 170, 130, 34));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelModalCliente, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelModalCliente, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
        if (!accionBoton.equals("anular")){
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

        String sql = "select TO_CHAR(CAST(documento AS numeric), 'FM999G999G999G990') AS documento,"
                + "CONCAT(cod_cliente,'-',nombres, ' ', apellidos) AS cliente_nombre from cliente\n"
                + "where apellidos like ";

        buscador pp = new buscador(sql, new String[]{"Documento", "Titular"}, 2, tfParam,"Order by cod_cliente");
        Dimension desktopSize = principalMenu.escritorio.getSize();
        Dimension FrameSize = pp.getSize();
        pp.setLocation((desktopSize.width - FrameSize.width) / 2, (desktopSize.height - FrameSize.height) / 4);
        pp.setVisible(true);         // TODO add your handling code here:
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

        String sql = "  select  CONCAT(l.cod_lote,') ',m.codigo, '-', l.numero_lote, '-', l.serie) AS lote_numero\n"
                + "  from lote as l\n"
                + "   INNER JOIN manzana AS m ON m.cod_manzana = l.cod_manzana\n"
                + "   where l.estado_registro='L' and l.numero_lote like";

        buscador pp = new buscador(sql, new String[]{"Lotes Libres"}, 1, tfParam,"Order by l.cod_lote");
        Dimension desktopSize = principalMenu.escritorio.getSize();
        Dimension FrameSize = pp.getSize();
        pp.setLocation((desktopSize.width - FrameSize.width) / 2, (desktopSize.height - FrameSize.height) / 4);
        pp.setVisible(true);        // TODO add your handling code here:
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


    // Variables declaration - do not modify//GEN-BEGIN:variables
    public static javax.swing.JLabel anulacion;
    public static javax.swing.JButton btnBuscarCliente;
    public static javax.swing.JButton btnBuscarLote;
    public static javax.swing.JButton btnCancelar;
    public static javax.swing.JButton btnGuardar;
    public static javax.swing.JPanel contener_buscarCliente;
    public static javax.swing.JPanel contener_buscarLote;
    public static javax.swing.JPanel contener_cancelar;
    public static javax.swing.JPanel contener_guardar;
    public static javax.swing.JPanel contener_salir;
    public static javax.swing.JFormattedTextField documento_titular;
    public static com.toedter.calendar.JDateChooser fecha_venci;
    public static javax.swing.JLabel jLabel10;
    public static javax.swing.JLabel jLabel13;
    public static javax.swing.JLabel jLabel15;
    public static javax.swing.JLabel jLabel3;
    public static javax.swing.JLabel jLabel4;
    public static javax.swing.JLabel jLabel5;
    public static javax.swing.JLabel jLabel7;
    public static javax.swing.JTextField lote_cod;
    public static javax.swing.JFormattedTextField monto_cuota;
    public static javax.swing.JFormattedTextField monto_entrega;
    public static javax.swing.JTextField motivo_anulacion;
    public static javax.swing.JPanel panelModalCliente;
    public static javax.swing.JButton salir;
    public static javax.swing.JTextField titular;
    // End of variables declaration//GEN-END:variables
}
