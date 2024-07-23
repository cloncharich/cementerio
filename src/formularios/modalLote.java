package formularios;

import clases.DatabaseConnector;
import clases.DatabaseManager;
import clases.EventoTecladoUtil;
import disenho.Formato;
import java.awt.Color;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 *
 * @author Claudio Loncharich
 */
public final class modalLote extends javax.swing.JInternalFrame {

    String accionBoton = "insertar";
    int codi_lote = 0;

    public modalLote() {
        initComponents();
        ((javax.swing.plaf.basic.BasicInternalFrameUI) this.getUI()).setNorthPane(null);
        setResizable(false);
        MostrarManzanas();
        codigo_lote.setEditable(false);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                dimension.requestFocusInWindow();
            }
        });

    }

    private void GrabarDatos() {
        // tabla lote en la base
        try {
            String queryInsert = "INSERT INTO public.lote(\n"
                    + "	numero_lote, dimension, construido, estado_registro, cta_catastral, cant_dif_habilitados,cod_manzana,serie)\n"
                    + "	VALUES (?, ?, ?, ?, ?,?,?,?);";

            String queryUpdate = "UPDATE public.lote\n"
                    + "	SET  numero_lote=?, dimension=?, construido=?, estado_registro=?, cta_catastral=?, cant_dif_habilitados=?, cod_manzana=?,serie=?\n"
                    + "	WHERE cod_lote=?;";

            String lo = lote.getText();//campo numero_lote
            String dim = dimension.getText(); // campo dimension
            String cons = ""; //campo construido
            if (si.isSelected()) {
                cons = "S";
            } else {
                if (no.isSelected()) {
                    cons = "N";
                }
            }
            String cta = cuenta_catastral.getText(); // campo cta_catastral
            String cant = lugares.getText();// campo cant_dif_habilitados
            String man = ""; // campo cod_manzana
            String ser = serie.getText();

            String selectedItem = (String) numero_manzana.getSelectedItem();
            int index = selectedItem.indexOf('-');
            if (index != -1) {
                man = selectedItem.substring(0, index);
            }

            int rowsAffected = 0;
            if ("insertar".equalsIgnoreCase(accionBoton)) {
                rowsAffected = DatabaseManager.insert(queryInsert, lo, dim, cons, "L", cta, Integer.parseInt(cant), Integer.parseInt(man), ser);
            } else if ("actualizar".equalsIgnoreCase(accionBoton)) {
                rowsAffected = DatabaseManager.update(queryUpdate,  lo, dim, cons, "L", cta, Integer.parseInt(cant), Integer.parseInt(man), ser,codi_lote);
            }

            if (rowsAffected > 0) {
                if ("insertar".equalsIgnoreCase(accionBoton)) {
                    JOptionPane.showMessageDialog(this, "Lote registrado con exito", "AVISO", JOptionPane.PLAIN_MESSAGE, Formato.icono("/imagenes/check.png", 40, 40));
                } else if ("actualizar".equalsIgnoreCase(accionBoton)) {
                    JOptionPane.showMessageDialog(this, "Datos actualizado con exito", "AVISO", JOptionPane.PLAIN_MESSAGE, Formato.icono("/imagenes/check.png", 40, 40));
                }
                dispose();
                tablaLote.mostrarTabla("");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(modalLote.this, e.getMessage(), "Error al grabar/actualizar registro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void MostrarDatos(String opcion, int id, String accion) {
        codi_lote = id;
        accionBoton = accion;
        String query = "Select l.numero_lote, l.dimension, l.construido, l.estado_registro, "
                + "l.cta_catastral, l.cant_dif_habilitados,l.serie,m.codigo,l.cod_manzana"
                + " from lote as l "
                + "inner join manzana as m on m.cod_manzana=l.cod_manzana"
                + " where l.cod_lote=" + codi_lote + "";
        try (Connection conn = DatabaseConnector.getConnection();
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery(query)) {

            if (rs.next()) {
                lote.setText(rs.getString("numero_lote"));
                dimension.setText(rs.getString("dimension"));
                String con = rs.getString("construido");
                if (con.equals("S")) {
                    si.setSelected(true);
                    no.setSelected(false);
                } else {
                    if (con.equals("N")) {
                     no.setSelected(true);
                     si.setSelected(false);
                    }
                }
                cuenta_catastral.setText(rs.getString("cta_catastral"));
                lugares.setText(rs.getString("cant_dif_habilitados"));
                serie.setText(rs.getString("serie"));
                numero_manzana.setSelectedItem(rs.getString("cod_manzana")+"="+rs.getString("codigo"));
                ManzanaLote();
                

                if (opcion.equals("ver")) {
                    Formato.habilitarCampos(getContentPane(), false);
                    btnGuardar.setEnabled(false);
                    btnCancelar.setEnabled(false);

                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error al ejecutar la consulta SQL: " + e.getMessage());
        }
    }

    private void MostrarManzanas() {
        numero_manzana.removeAllItems();
        // numero_manzana.addItem("");
        String query = "Select cod_manzana,codigo from manzana order by cod_manzana ASC";
        Statement st1;
        try (Connection conn = DatabaseConnector.getConnection();
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery(query)) {
            while (rs.next()) {
                numero_manzana.addItem(rs.getString(1) + "-" + rs.getString(2));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, ex);
        }
    }

    private void ManzanaLote() {
        String selectedItem = (String) numero_manzana.getSelectedItem();
        if (selectedItem != null && !lote.getText().trim().isEmpty() && !serie.getText().trim().isEmpty()) {
            int index = selectedItem.indexOf('-');
            if (index != -1) {
                String numeroManzana = selectedItem.substring(index + 1);
                codigo_lote.setText(numeroManzana + "-" + lote.getText() + "-" + serie.getText());
            }
        } else {
            codigo_lote.setText("");
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
        codigo_lote = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        dimension = new javax.swing.JTextField();
        lugares = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        cuenta_catastral = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        si = new javax.swing.JCheckBox();
        no = new javax.swing.JCheckBox();
        jLabel9 = new javax.swing.JLabel();
        lote = new javax.swing.JTextField();
        numero_manzana = new javax.swing.JComboBox<>();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        serie = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        contener_guardar = new javax.swing.JPanel();
        btnGuardar = new javax.swing.JButton();

        setBackground(new java.awt.Color(255, 255, 255));
        setBorder(javax.swing.BorderFactory.createEtchedBorder());
        setFrameIcon(null);
        setPreferredSize(new java.awt.Dimension(678, 310));

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

        panelModalCliente.add(contener_cancelar, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 260, 110, -1));

        jLabel3.setFont(new java.awt.Font("Roboto Light", 0, 14)); // NOI18N
        jLabel3.setText("Codigo Lote:");
        panelModalCliente.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 130, 34));

        jLabel4.setFont(new java.awt.Font("Roboto Medium", 0, 18)); // NOI18N
        jLabel4.setText("ALTA LOTE");
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

        codigo_lote.setFont(new java.awt.Font("Roboto Light", 0, 12)); // NOI18N
        codigo_lote.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        codigo_lote.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                codigo_loteActionPerformed(evt);
            }
        });
        codigo_lote.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                codigo_loteKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                codigo_loteKeyTyped(evt);
            }
        });
        panelModalCliente.add(codigo_lote, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, 320, 32));

        jLabel5.setFont(new java.awt.Font("Roboto Light", 0, 14)); // NOI18N
        jLabel5.setText("Dimensión:");
        panelModalCliente.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 50, 130, 34));

        dimension.setFont(new java.awt.Font("Roboto Light", 0, 12)); // NOI18N
        dimension.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        dimension.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dimensionActionPerformed(evt);
            }
        });
        dimension.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                dimensionKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                dimensionKeyTyped(evt);
            }
        });
        panelModalCliente.add(dimension, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 80, 320, 32));

        lugares.setFont(new java.awt.Font("Roboto Light", 0, 12)); // NOI18N
        lugares.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        lugares.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lugaresActionPerformed(evt);
            }
        });
        lugares.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                lugaresKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                lugaresKeyTyped(evt);
            }
        });
        panelModalCliente.add(lugares, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 140, 320, 32));

        jLabel7.setFont(new java.awt.Font("Roboto Light", 0, 14)); // NOI18N
        jLabel7.setText("Cuenta Catastral:");
        panelModalCliente.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, 130, 34));

        cuenta_catastral.setFont(new java.awt.Font("Roboto Light", 0, 12)); // NOI18N
        cuenta_catastral.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        cuenta_catastral.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cuenta_catastralActionPerformed(evt);
            }
        });
        cuenta_catastral.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                cuenta_catastralKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                cuenta_catastralKeyTyped(evt);
            }
        });
        panelModalCliente.add(cuenta_catastral, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 140, 320, 32));

        jLabel8.setFont(new java.awt.Font("Roboto Light", 0, 14)); // NOI18N
        jLabel8.setText("Construido:");
        panelModalCliente.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 170, 120, 34));

        si.setSelected(true);
        si.setText("SI");
        si.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                siActionPerformed(evt);
            }
        });
        panelModalCliente.add(si, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 200, 50, 32));

        no.setText("NO");
        no.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                noActionPerformed(evt);
            }
        });
        panelModalCliente.add(no, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 200, 50, 32));

        jLabel9.setFont(new java.awt.Font("Roboto Light", 0, 14)); // NOI18N
        jLabel9.setText("Lote:");
        panelModalCliente.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 170, 90, 34));

        lote.setFont(new java.awt.Font("Roboto Light", 0, 12)); // NOI18N
        lote.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        lote.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                loteFocusLost(evt);
            }
        });
        lote.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loteActionPerformed(evt);
            }
        });
        lote.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                loteKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                loteKeyTyped(evt);
            }
        });
        panelModalCliente.add(lote, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 200, 90, 32));

        numero_manzana.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        numero_manzana.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                numero_manzanaFocusGained(evt);
            }
        });
        numero_manzana.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                numero_manzanaMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                numero_manzanaMouseEntered(evt);
            }
        });
        numero_manzana.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                numero_manzanaActionPerformed(evt);
            }
        });
        panelModalCliente.add(numero_manzana, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 200, 120, 32));

        jLabel10.setFont(new java.awt.Font("Roboto Light", 0, 14)); // NOI18N
        jLabel10.setText("Lugares Habilitados:");
        panelModalCliente.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 110, 130, 34));

        jLabel11.setFont(new java.awt.Font("Roboto Light", 0, 14)); // NOI18N
        jLabel11.setText("Serie:");
        panelModalCliente.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 170, 90, 34));

        serie.setFont(new java.awt.Font("Roboto Light", 0, 12)); // NOI18N
        serie.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        serie.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                serieFocusLost(evt);
            }
        });
        serie.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                serieActionPerformed(evt);
            }
        });
        serie.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                serieKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                serieKeyTyped(evt);
            }
        });
        panelModalCliente.add(serie, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 200, 90, 32));

        jLabel12.setFont(new java.awt.Font("Roboto Light", 0, 14)); // NOI18N
        jLabel12.setText("Manzana:");
        panelModalCliente.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 170, 110, 34));

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

        panelModalCliente.add(contener_guardar, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 260, 110, -1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelModalCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelModalCliente, javax.swing.GroupLayout.DEFAULT_SIZE, 306, Short.MAX_VALUE)
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

    private void codigo_loteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_codigo_loteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_codigo_loteActionPerformed

    private void codigo_loteKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_codigo_loteKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_codigo_loteKeyReleased

    private void codigo_loteKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_codigo_loteKeyTyped
        EventoTecladoUtil.permitirMayusculasYNumeros(evt);        // TODO add your handling code here:
    }//GEN-LAST:event_codigo_loteKeyTyped

    private void dimensionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dimensionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_dimensionActionPerformed

    private void dimensionKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_dimensionKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_dimensionKeyReleased

    private void dimensionKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_dimensionKeyTyped
        EventoTecladoUtil.permitirNumerosComasYGuiones(evt);        // TODO add your handling code here:
    }//GEN-LAST:event_dimensionKeyTyped

    private void lugaresActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lugaresActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_lugaresActionPerformed

    private void lugaresKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_lugaresKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_lugaresKeyReleased

    private void lugaresKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_lugaresKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_lugaresKeyTyped

    private void cuenta_catastralActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cuenta_catastralActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cuenta_catastralActionPerformed

    private void cuenta_catastralKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cuenta_catastralKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_cuenta_catastralKeyReleased

    private void cuenta_catastralKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cuenta_catastralKeyTyped
        EventoTecladoUtil.permitirNumerosComasYGuiones(evt);          // TODO add your handling code here:
    }//GEN-LAST:event_cuenta_catastralKeyTyped

    private void siActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_siActionPerformed
        si.setSelected(true);
        no.setSelected(false);
// TODO add your handling code here:
    }//GEN-LAST:event_siActionPerformed

    private void noActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_noActionPerformed
        no.setSelected(true);
        si.setSelected(false);
        // TODO add your handling code here:
    }//GEN-LAST:event_noActionPerformed

    private void loteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loteActionPerformed
        ManzanaLote();        // TODO add your handling code here:
    }//GEN-LAST:event_loteActionPerformed

    private void loteKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_loteKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_loteKeyReleased

    private void loteKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_loteKeyTyped
        EventoTecladoUtil.permitirMayusculasYNumeros(evt);        // TODO add your handling code here:
    }//GEN-LAST:event_loteKeyTyped

    private void numero_manzanaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_numero_manzanaFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_numero_manzanaFocusGained

    private void numero_manzanaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_numero_manzanaMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_numero_manzanaMouseClicked

    private void numero_manzanaMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_numero_manzanaMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_numero_manzanaMouseEntered

    private void numero_manzanaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_numero_manzanaActionPerformed
        ManzanaLote();
    }//GEN-LAST:event_numero_manzanaActionPerformed

    private void loteFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_loteFocusLost
        ManzanaLote();         // TODO add your handling code here:
    }//GEN-LAST:event_loteFocusLost

    private void serieFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_serieFocusLost
        ManzanaLote();        // TODO add your handling code here:
    }//GEN-LAST:event_serieFocusLost

    private void serieActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_serieActionPerformed
        ManzanaLote();        // TODO add your handling code here:
    }//GEN-LAST:event_serieActionPerformed

    private void serieKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_serieKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_serieKeyReleased

    private void serieKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_serieKeyTyped
        EventoTecladoUtil.convertirAMayusculas(evt);        // TODO add your handling code here:
    }//GEN-LAST:event_serieKeyTyped

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
         if (Formato.verificarCampos(getContentPane()) == true) {
            GrabarDatos();
        } else {
            JOptionPane.showMessageDialog(this, "Debe ingresar todos los campos", "Aviso", JOptionPane.INFORMATION_MESSAGE);
        }       // TODO add your handling code here:
    }//GEN-LAST:event_btnGuardarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    public static javax.swing.JButton btnCancelar;
    public static javax.swing.JButton btnGuardar;
    public static javax.swing.JTextField codigo_lote;
    public static javax.swing.JPanel contener_cancelar;
    public static javax.swing.JPanel contener_guardar;
    public static javax.swing.JPanel contener_salir;
    public static javax.swing.JTextField cuenta_catastral;
    public static javax.swing.JTextField dimension;
    public static javax.swing.JLabel jLabel10;
    public static javax.swing.JLabel jLabel11;
    public static javax.swing.JLabel jLabel12;
    public static javax.swing.JLabel jLabel3;
    public static javax.swing.JLabel jLabel4;
    public static javax.swing.JLabel jLabel5;
    public static javax.swing.JLabel jLabel7;
    public static javax.swing.JLabel jLabel8;
    public static javax.swing.JLabel jLabel9;
    public static javax.swing.JTextField lote;
    public static javax.swing.JTextField lugares;
    public static javax.swing.JCheckBox no;
    public static javax.swing.JComboBox<String> numero_manzana;
    public static javax.swing.JPanel panelModalCliente;
    public static javax.swing.JButton salir;
    public static javax.swing.JTextField serie;
    public static javax.swing.JCheckBox si;
    // End of variables declaration//GEN-END:variables
}
