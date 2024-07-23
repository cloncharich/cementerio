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
public final class modalManzana extends javax.swing.JInternalFrame {

    String ruta = "", accionBoton = "insertar";
    int manzana = 0;

    public modalManzana() {
        initComponents();
        ((javax.swing.plaf.basic.BasicInternalFrameUI) this.getUI()).setNorthPane(null);
        setResizable(false);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                codigo_manzana.requestFocusInWindow();
            }
        });

    }

    private void GrabarDatos() {
        // tabla manzana en la base
        try {
            String queryInsert = "INSERT INTO public.manzana(\n"
                    + "	codigo, longitud)\n"
                    + "	VALUES (?, ?);";

            String queryUpdate = "UPDATE public.manzana\n"
                    + "	SET codigo=?, longitud=?\n"
                    + "	WHERE cod_manzana=?;";

            String cod = codigo_manzana.getText(); // campo codigo
            String dim = dimension.getText(); // campo dimension

            int rowsAffected = 0;
            if ("insertar".equalsIgnoreCase(accionBoton)) {
                rowsAffected = DatabaseManager.insert(queryInsert, cod, dim);
            } else if ("actualizar".equalsIgnoreCase(accionBoton)) {
                rowsAffected = DatabaseManager.update(queryUpdate, cod, dim, manzana);
            }

            if (rowsAffected > 0) {
                if ("insertar".equalsIgnoreCase(accionBoton)) {
                    JOptionPane.showMessageDialog(this, "Manzana registrada con exito", "AVISO", JOptionPane.PLAIN_MESSAGE, Formato.icono("/imagenes/check.png", 40, 40));
                } else if ("actualizar".equalsIgnoreCase(accionBoton)) {
                    JOptionPane.showMessageDialog(this, "Datos actualizado con exito", "AVISO", JOptionPane.PLAIN_MESSAGE, Formato.icono("/imagenes/check.png", 40, 40));
                }
                dispose();
                tablaManzana.mostrarTabla("");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(modalManzana.this, e.getMessage(), "Error al grabar/actualizar registro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void MostrarDatos(String opcion, int id, String accion) {
        manzana = id;
        accionBoton = accion;
        String query = "Select codigo,longitud from manzana where cod_manzana=" + manzana + "";
        try (Connection conn = DatabaseConnector.getConnection();
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery(query)) {

            if (rs.next()) {
                codigo_manzana.setText(rs.getString("codigo"));
                dimension.setText(rs.getString("longitud"));

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
        codigo_manzana = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        dimension = new javax.swing.JTextField();
        contener_guardar = new javax.swing.JPanel();
        btnGuardar = new javax.swing.JButton();

        setBackground(new java.awt.Color(255, 255, 255));
        setBorder(javax.swing.BorderFactory.createEtchedBorder());
        setFrameIcon(null);
        setPreferredSize(new java.awt.Dimension(692, 232));

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

        panelModalCliente.add(contener_cancelar, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 160, 110, 34));

        jLabel3.setFont(new java.awt.Font("Roboto Light", 0, 14)); // NOI18N
        jLabel3.setText("Codigo Manzana:");
        panelModalCliente.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 130, 34));

        jLabel4.setFont(new java.awt.Font("Roboto Medium", 0, 18)); // NOI18N
        jLabel4.setText("ALTA MANZANA");
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

        panelModalCliente.add(contener_salir, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 0, -1, -1));

        codigo_manzana.setFont(new java.awt.Font("Roboto Light", 0, 12)); // NOI18N
        codigo_manzana.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        codigo_manzana.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                codigo_manzanaActionPerformed(evt);
            }
        });
        codigo_manzana.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                codigo_manzanaKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                codigo_manzanaKeyTyped(evt);
            }
        });
        panelModalCliente.add(codigo_manzana, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, 320, 32));

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

        panelModalCliente.add(contener_guardar, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 160, 110, -1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(panelModalCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(panelModalCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
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

    private void codigo_manzanaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_codigo_manzanaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_codigo_manzanaActionPerformed

    private void codigo_manzanaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_codigo_manzanaKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_codigo_manzanaKeyReleased

    private void codigo_manzanaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_codigo_manzanaKeyTyped
        EventoTecladoUtil.permitirMayusculasYNumeros(evt);        // TODO add your handling code here:
    }//GEN-LAST:event_codigo_manzanaKeyTyped

    private void dimensionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dimensionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_dimensionActionPerformed

    private void dimensionKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_dimensionKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_dimensionKeyReleased

    private void dimensionKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_dimensionKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_dimensionKeyTyped

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
    public static javax.swing.JTextField codigo_manzana;
    public static javax.swing.JPanel contener_cancelar;
    public static javax.swing.JPanel contener_guardar;
    public static javax.swing.JPanel contener_salir;
    public static javax.swing.JTextField dimension;
    public static javax.swing.JLabel jLabel3;
    public static javax.swing.JLabel jLabel4;
    public static javax.swing.JLabel jLabel5;
    public static javax.swing.JPanel panelModalCliente;
    public static javax.swing.JButton salir;
    // End of variables declaration//GEN-END:variables
}
