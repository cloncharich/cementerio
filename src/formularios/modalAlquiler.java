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
public final class modalAlquiler extends javax.swing.JInternalFrame {

    String accionBoton = "insertar";
    int codi_lote = 0;

    public modalAlquiler() {
        initComponents();
        ((javax.swing.plaf.basic.BasicInternalFrameUI) this.getUI()).setNorthPane(null);
        setResizable(false);
        codigo_cliente.setEditable(false);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                dimension.requestFocusInWindow();
            }
        });

    }

    

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelModalCliente = new javax.swing.JPanel();
        contener_guardar = new javax.swing.JPanel();
        btnGuardar = new javax.swing.JButton();
        contener_cancelar = new javax.swing.JPanel();
        btnCancelar = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        contener_salir = new javax.swing.JPanel();
        salir = new javax.swing.JButton();
        codigo_cliente = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        dimension = new javax.swing.JTextField();
        monto_entrega = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        lote_cod = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        monto_cuota = new javax.swing.JTextField();
        fecha_venci1 = new com.toedter.calendar.JDateChooser();
        jLabel14 = new javax.swing.JLabel();
        contener_guardar1 = new javax.swing.JPanel();
        btnGuardar1 = new javax.swing.JButton();

        setBackground(new java.awt.Color(255, 255, 255));
        setBorder(javax.swing.BorderFactory.createEtchedBorder());
        setFrameIcon(null);
        setPreferredSize(new java.awt.Dimension(678, 310));

        panelModalCliente.setBackground(new java.awt.Color(255, 255, 255));
        panelModalCliente.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        contener_guardar.setBackground(new java.awt.Color(80, 90, 100));

        btnGuardar.setBackground(new java.awt.Color(153, 204, 255));
        btnGuardar.setFont(new java.awt.Font("Roboto Medium", 1, 12)); // NOI18N
        btnGuardar.setForeground(new java.awt.Color(255, 255, 255));
        btnGuardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/save.png"))); // NOI18N
        btnGuardar.setText("GUARDAR");
        btnGuardar.setToolTipText("");
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
                .addComponent(btnGuardar, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        contener_guardarLayout.setVerticalGroup(
            contener_guardarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnGuardar, javax.swing.GroupLayout.DEFAULT_SIZE, 34, Short.MAX_VALUE)
        );

        panelModalCliente.add(contener_guardar, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 260, -1, -1));

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

        panelModalCliente.add(contener_cancelar, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 260, 110, -1));

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

        codigo_cliente.setFont(new java.awt.Font("Roboto Light", 0, 12)); // NOI18N
        codigo_cliente.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        codigo_cliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                codigo_clienteActionPerformed(evt);
            }
        });
        codigo_cliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                codigo_clienteKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                codigo_clienteKeyTyped(evt);
            }
        });
        panelModalCliente.add(codigo_cliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, 270, 32));

        jLabel5.setFont(new java.awt.Font("Roboto Light", 0, 14)); // NOI18N
        jLabel5.setText("Titular:");
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

        monto_entrega.setFont(new java.awt.Font("Roboto Light", 0, 12)); // NOI18N
        monto_entrega.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        monto_entrega.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                monto_entregaActionPerformed(evt);
            }
        });
        monto_entrega.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                monto_entregaKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                monto_entregaKeyTyped(evt);
            }
        });
        panelModalCliente.add(monto_entrega, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 140, 320, 32));

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

        monto_cuota.setFont(new java.awt.Font("Roboto Light", 0, 12)); // NOI18N
        monto_cuota.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        monto_cuota.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                monto_cuotaActionPerformed(evt);
            }
        });
        monto_cuota.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                monto_cuotaKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                monto_cuotaKeyTyped(evt);
            }
        });
        panelModalCliente.add(monto_cuota, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 200, 320, 32));

        fecha_venci1.setBackground(new java.awt.Color(204, 204, 204));
        fecha_venci1.setForeground(new java.awt.Color(204, 204, 204));
        fecha_venci1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                fecha_venci1FocusLost(evt);
            }
        });
        panelModalCliente.add(fecha_venci1, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 200, 320, 32));

        jLabel14.setFont(new java.awt.Font("Roboto Light", 0, 14)); // NOI18N
        jLabel14.setText("Monto cuota:");
        panelModalCliente.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 170, 130, 34));

        contener_guardar1.setBackground(new java.awt.Color(80, 90, 100));

        btnGuardar1.setBackground(new java.awt.Color(153, 204, 255));
        btnGuardar1.setFont(new java.awt.Font("Roboto Medium", 1, 12)); // NOI18N
        btnGuardar1.setForeground(new java.awt.Color(255, 255, 255));
        btnGuardar1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/buscar.png"))); // NOI18N
        btnGuardar1.setToolTipText("");
        btnGuardar1.setBorder(null);
        btnGuardar1.setContentAreaFilled(false);
        btnGuardar1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnGuardar1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                btnGuardar1FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                btnGuardar1FocusLost(evt);
            }
        });
        btnGuardar1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnGuardar1MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnGuardar1MouseExited(evt);
            }
        });
        btnGuardar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardar1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout contener_guardar1Layout = new javax.swing.GroupLayout(contener_guardar1);
        contener_guardar1.setLayout(contener_guardar1Layout);
        contener_guardar1Layout.setHorizontalGroup(
            contener_guardar1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(contener_guardar1Layout.createSequentialGroup()
                .addComponent(btnGuardar1, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        contener_guardar1Layout.setVerticalGroup(
            contener_guardar1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnGuardar1, javax.swing.GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE)
        );

        panelModalCliente.add(contener_guardar1, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 80, 40, 32));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelModalCliente, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(panelModalCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 305, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnGuardarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnGuardarMouseEntered
        contener_guardar.setBackground(new Color(51, 51, 51));         // TODO add your handling code here:
    }//GEN-LAST:event_btnGuardarMouseEntered

    private void btnGuardarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnGuardarMouseExited
        contener_guardar.setBackground(new Color(80, 90, 100));
    }//GEN-LAST:event_btnGuardarMouseExited

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        if (Formato.verificarCampos(getContentPane()) == true) {

        } else {
            JOptionPane.showMessageDialog(this, "Debe ingresar todos los campos", "Aviso", JOptionPane.INFORMATION_MESSAGE);
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_btnGuardarActionPerformed

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

    private void btnGuardarFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_btnGuardarFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_btnGuardarFocusGained

    private void btnGuardarFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_btnGuardarFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_btnGuardarFocusLost

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

    private void codigo_clienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_codigo_clienteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_codigo_clienteActionPerformed

    private void codigo_clienteKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_codigo_clienteKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_codigo_clienteKeyReleased

    private void codigo_clienteKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_codigo_clienteKeyTyped
        EventoTecladoUtil.permitirMayusculasYNumeros(evt);        // TODO add your handling code here:
    }//GEN-LAST:event_codigo_clienteKeyTyped

    private void dimensionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dimensionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_dimensionActionPerformed

    private void dimensionKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_dimensionKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_dimensionKeyReleased

    private void dimensionKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_dimensionKeyTyped
        EventoTecladoUtil.permitirNumerosComasYGuiones(evt);        // TODO add your handling code here:
    }//GEN-LAST:event_dimensionKeyTyped

    private void monto_entregaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_monto_entregaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_monto_entregaActionPerformed

    private void monto_entregaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_monto_entregaKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_monto_entregaKeyReleased

    private void monto_entregaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_monto_entregaKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_monto_entregaKeyTyped

    private void lote_codActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lote_codActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_lote_codActionPerformed

    private void lote_codKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_lote_codKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_lote_codKeyReleased

    private void lote_codKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_lote_codKeyTyped
        EventoTecladoUtil.permitirNumerosComasYGuiones(evt);          // TODO add your handling code here:
    }//GEN-LAST:event_lote_codKeyTyped

    private void monto_cuotaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_monto_cuotaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_monto_cuotaActionPerformed

    private void monto_cuotaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_monto_cuotaKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_monto_cuotaKeyReleased

    private void monto_cuotaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_monto_cuotaKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_monto_cuotaKeyTyped

    private void fecha_venci1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_fecha_venci1FocusLost

        // TODO add your handling code here:
    }//GEN-LAST:event_fecha_venci1FocusLost

    private void btnGuardar1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_btnGuardar1FocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_btnGuardar1FocusGained

    private void btnGuardar1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_btnGuardar1FocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_btnGuardar1FocusLost

    private void btnGuardar1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnGuardar1MouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_btnGuardar1MouseEntered

    private void btnGuardar1MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnGuardar1MouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_btnGuardar1MouseExited

    private void btnGuardar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardar1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnGuardar1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    public static javax.swing.JButton btnCancelar;
    public static javax.swing.JButton btnGuardar;
    public static javax.swing.JButton btnGuardar1;
    public static javax.swing.JTextField codigo_cliente;
    public static javax.swing.JPanel contener_cancelar;
    public static javax.swing.JPanel contener_guardar;
    public static javax.swing.JPanel contener_guardar1;
    public static javax.swing.JPanel contener_salir;
    public static javax.swing.JTextField dimension;
    public static com.toedter.calendar.JDateChooser fecha_venci;
    public static com.toedter.calendar.JDateChooser fecha_venci1;
    public static javax.swing.JLabel jLabel10;
    public static javax.swing.JLabel jLabel13;
    public static javax.swing.JLabel jLabel14;
    public static javax.swing.JLabel jLabel3;
    public static javax.swing.JLabel jLabel4;
    public static javax.swing.JLabel jLabel5;
    public static javax.swing.JLabel jLabel7;
    public static javax.swing.JTextField lote_cod;
    public static javax.swing.JTextField monto_cuota;
    public static javax.swing.JTextField monto_entrega;
    public static javax.swing.JPanel panelModalCliente;
    public static javax.swing.JButton salir;
    // End of variables declaration//GEN-END:variables
}
