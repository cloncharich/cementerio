package formularios;

import clases.AnimacionDelLogo;
import clases.DatabaseConnector;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingWorker;
import javax.swing.UIManager;

/**
 *
 * @author Claudio Loncharich
 */
public class login extends JFrame {

    int xmouse, ymouse;
    private JPanel panel;

    public login() {
        initComponents();
        ingresar.requestFocus();
        //this.salir.putClientProperty( SubstanceLookAndFeel.BUTTON_SIDE_PROPERTY, SubstanceConstants.Side.RIGHT);
        UIManager UI = new UIManager();
        UIManager.put("control", new Color(255, 255, 255));
        UI.put("OptionPane.background", new Color(255, 255, 255));
        UIManager.put("nimbusBase", new Color(255, 255, 255));
        UIManager.put("InternalFrame.titleFont", new Font("System", Font.PLAIN, 14));
        UIManager.put("OptionPane.messageFont", new Font("System", Font.PLAIN, 14));
        UIManager.put("OptionPane.buttonFont", new Font("System", Font.PLAIN, 14));
        //setIconImage(new ImageIcon(getClass().getResource("/IMAGENES/marco_logo2.png")).getImage());
        // logo.setVisible(false);

        ingresar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                // Mostrar el panel de carga en lugar de iniciar sesión directamente
                AnimacionDelLogo ani = new AnimacionDelLogo(logo);
                logo.setVisible(true);

                // Realizar la autenticación en segundo plano (ajusta esto según tus necesidades)
                SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {
                    @Override
                    protected Boolean doInBackground() throws Exception {
                        String usuario = user.getText();
                        char[] arrayC = pas.getPassword();
                        String password = new String(arrayC);
                        return authenticate(usuario, password);
                    }

                    @Override
                    protected void done() {
                        try {
                            boolean authResult = get();

                            // Cerrar el marco de carga
                            //ani.finalize();
                            logo.setVisible(false);

                            if (authResult) {
                                // Si la autenticación es exitosa, mostrar la pantalla principal
                                showMainScreen();
                            } else {
                                // Si la autenticación falla, mostrar un mensaje de error
                                JOptionPane.showMessageDialog(login.this, "Usuario o contraseña incorrecta", "Verificar credenciales", JOptionPane.ERROR_MESSAGE);
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                };

                // Iniciar el trabajador SwingWorker
                worker.execute();
            }
        });

    }

    private boolean authenticate(String usuario, String password) {
        try {
            // Lógica de autenticación simulada
            Thread.sleep(2000); // Simula un proceso de autenticación
            return true;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void showMainScreen() throws SQLException {
        String usuario = user.getText();
        char[] arrayC = pas.getPassword();
        String password = new String(arrayC);
        String host = "jdbc:postgresql://localhost:5432/cementerio";

        DatabaseConnector.initializeConnection(host, usuario, password);

        String query = "SELECT COUNT(*) AS count FROM usuario WHERE usuario = ?";

        try (Connection conn = DatabaseConnector.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, usuario);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int contador = rs.getInt("count");
                    if (contador > 0) {
                        principalMenu pp = new principalMenu(host, usuario, password);
                        pp.setVisible(true);
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(login.this, "El usuario no se encuentra registrado en la tabla usuario", "Verificar credenciales", JOptionPane.ERROR_MESSAGE);
                    }

                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error al verificar el usuario en la base de datos: " + e.getMessage());
            JOptionPane.showMessageDialog(login.this, e.getMessage(), "Error al verificar el usuario en la base de datos", JOptionPane.ERROR_MESSAGE);
        }

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jToggleButton1 = new javax.swing.JToggleButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jPanel1 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        user = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jPanel4 = new javax.swing.JPanel();
        pas = new javax.swing.JPasswordField();
        contener_salir = new javax.swing.JPanel();
        salir = new javax.swing.JButton();
        contener_ingresar = new javax.swing.JPanel();
        ingresar = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        logo = new javax.swing.JPanel();

        jToggleButton1.setText("jToggleButton1");

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setLocationByPlatform(true);
        setUndecorated(true);
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(80, 90, 100));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 102, 102), 3));
        jPanel1.setForeground(new java.awt.Color(204, 204, 204));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel4.setBackground(new java.awt.Color(244, 1, 149));
        jLabel4.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("USUARIO");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 230, 210, -1));

        jSeparator1.setBackground(new java.awt.Color(102, 102, 102));
        jSeparator1.setForeground(new java.awt.Color(255, 255, 255));
        jSeparator1.setOpaque(true);
        jPanel1.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 280, 280, 2));

        user.setBackground(new java.awt.Color(80, 90, 100));
        user.setFont(new java.awt.Font("Roboto Light", 0, 12)); // NOI18N
        user.setForeground(new java.awt.Color(255, 255, 255));
        user.setText("Ingrese su usuario");
        user.setBorder(null);
        user.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                userFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                userFocusLost(evt);
            }
        });
        user.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                userMousePressed(evt);
            }
        });
        user.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                userActionPerformed(evt);
            }
        });
        user.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                userKeyTyped(evt);
            }
        });
        jPanel1.add(user, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 250, 280, 30));

        jLabel5.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("CONTRASEÑA");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 290, 210, -1));

        jSeparator2.setBackground(new java.awt.Color(102, 102, 102));
        jSeparator2.setForeground(new java.awt.Color(255, 255, 255));
        jSeparator2.setOpaque(true);
        jPanel1.add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 340, 280, 2));

        jPanel4.setBackground(new java.awt.Color(80, 90, 100));
        jPanel4.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                jPanel4MouseDragged(evt);
            }
        });
        jPanel4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jPanel4MousePressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 320, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );

        jPanel1.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 10, 320, 10));

        pas.setBackground(new java.awt.Color(80, 90, 100));
        pas.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        pas.setForeground(new java.awt.Color(255, 255, 255));
        pas.setText("********");
        pas.setBorder(null);
        pas.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                pasFocusGained(evt);
            }
        });
        pas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                pasMousePressed(evt);
            }
        });
        pas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pasActionPerformed(evt);
            }
        });
        pas.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                pasKeyReleased(evt);
            }
        });
        jPanel1.add(pas, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 310, 280, 30));

        contener_salir.setBackground(new java.awt.Color(80, 90, 100));
        contener_salir.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));

        salir.setBackground(new java.awt.Color(255, 255, 255));
        salir.setFont(new java.awt.Font("Roboto Medium", 0, 18)); // NOI18N
        salir.setForeground(new java.awt.Color(255, 255, 255));
        salir.setText("X");
        salir.setBorder(null);
        salir.setContentAreaFilled(false);
        salir.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        salir.setDebugGraphicsOptions(javax.swing.DebugGraphics.NONE_OPTION);
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
            .addComponent(salir, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
        );
        contener_salirLayout.setVerticalGroup(
            contener_salirLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, contener_salirLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(salir, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel1.add(contener_salir, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 40, -1));

        contener_ingresar.setBackground(new java.awt.Color(80, 90, 100));
        contener_ingresar.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        contener_ingresar.setForeground(new java.awt.Color(0, 0, 0));
        contener_ingresar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                contener_ingresarMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                contener_ingresarMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                contener_ingresarMouseExited(evt);
            }
        });

        ingresar.setBackground(new java.awt.Color(3, 11, 72));
        ingresar.setFont(new java.awt.Font("Roboto Condensed", 1, 14)); // NOI18N
        ingresar.setForeground(new java.awt.Color(255, 255, 255));
        ingresar.setText("INICIAR SESIÓN");
        ingresar.setActionCommand("");
        ingresar.setBorder(null);
        ingresar.setContentAreaFilled(false);
        ingresar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        ingresar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                ingresarMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                ingresarMouseExited(evt);
            }
        });
        ingresar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ingresarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout contener_ingresarLayout = new javax.swing.GroupLayout(contener_ingresar);
        contener_ingresar.setLayout(contener_ingresarLayout);
        contener_ingresarLayout.setHorizontalGroup(
            contener_ingresarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ingresar, javax.swing.GroupLayout.DEFAULT_SIZE, 188, Short.MAX_VALUE)
        );
        contener_ingresarLayout.setVerticalGroup(
            contener_ingresarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ingresar, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
        );

        jPanel1.add(contener_ingresar, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 430, 190, 40));

        jLabel1.setBackground(new java.awt.Color(255, 255, 255));
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/logoCementerio.png"))); // NOI18N
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 390, 220));

        logo.setBackground(new java.awt.Color(80, 90, 100));
        logo.setForeground(new java.awt.Color(0, 0, 0));

        javax.swing.GroupLayout logoLayout = new javax.swing.GroupLayout(logo);
        logo.setLayout(logoLayout);
        logoLayout.setHorizontalGroup(
            logoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 130, Short.MAX_VALUE)
        );
        logoLayout.setVerticalGroup(
            logoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
        );

        jPanel1.add(logo, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 370, 130, 50));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 502, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void userActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_userActionPerformed
        //ODO add your handling code here:
    }//GEN-LAST:event_userActionPerformed

    private void jPanel4MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel4MousePressed
        xmouse = evt.getX();
        ymouse = evt.getY();
    }//GEN-LAST:event_jPanel4MousePressed

    private void jPanel4MouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel4MouseDragged
        int x = evt.getXOnScreen();
        int y = evt.getYOnScreen();
        this.setLocation(x - xmouse, y - ymouse);

    }//GEN-LAST:event_jPanel4MouseDragged

    private void userMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_userMousePressed
        if (user.getText().equals("Ingrese su usuario")) {
            user.setText("");
            user.setForeground(Color.WHITE);
        }
        if (String.valueOf(pas.getPassword()).isEmpty()) {
            pas.setText("********");
            pas.setForeground(Color.WHITE);
        }
    }//GEN-LAST:event_userMousePressed

    private void pasMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pasMousePressed
        if (String.valueOf(pas.getPassword()).equals("********")) {
            pas.setText("");
            pas.setForeground(Color.WHITE);
        }
        if (user.getText().isEmpty()) {
            user.setText("Ingrese su usuario");
            user.setForeground(Color.WHITE);
        }

    }//GEN-LAST:event_pasMousePressed

    private void userFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_userFocusGained
        if (user.getText().equals("Ingrese su usuario")) {
            user.setText("");
            user.setForeground(Color.WHITE);
        }
        if (String.valueOf(pas.getPassword()).isEmpty()) {
            pas.setText("********");
            pas.setForeground(Color.WHITE);
        }
    }//GEN-LAST:event_userFocusGained

    private void pasFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_pasFocusGained

        if (String.valueOf(pas.getPassword()).equals("********")) {
            pas.setText("");
            pas.setForeground(Color.WHITE);
        }
        if (user.getText().isEmpty()) {
            user.setText("Ingrese su usuario");
            user.setForeground(Color.WHITE);
        }        // TODO add your handling code here:
    }//GEN-LAST:event_pasFocusGained

    private void userKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_userKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_userKeyTyped

    private void userFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_userFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_userFocusLost

    private void ingresarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ingresarActionPerformed

    }//GEN-LAST:event_ingresarActionPerformed

    private void ingresarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ingresarMouseEntered
        contener_ingresar.setBackground(new Color(51, 51, 51));
    }//GEN-LAST:event_ingresarMouseEntered

    private void ingresarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ingresarMouseExited
        contener_ingresar.setBackground(new Color(80,90,100));
    }//GEN-LAST:event_ingresarMouseExited

    private void salirMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_salirMouseEntered
        contener_salir.setBackground(Color.red);

// TODO add your handling code here:
    }//GEN-LAST:event_salirMouseEntered

    private void salirMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_salirMouseExited

        contener_salir.setBackground(new Color(80,90,100));

        // TODO add your handling code here:
    }//GEN-LAST:event_salirMouseExited

    private void salirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_salirActionPerformed
        dispose();        // TODO add your handling code here:
    }//GEN-LAST:event_salirActionPerformed

    private void contener_ingresarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_contener_ingresarMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_contener_ingresarMouseClicked

    private void contener_ingresarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_contener_ingresarMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_contener_ingresarMouseEntered

    private void contener_ingresarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_contener_ingresarMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_contener_ingresarMouseExited

    private void pasKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pasKeyReleased

        // TODO add your handling code here:
    }//GEN-LAST:event_pasKeyReleased

    private void pasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pasActionPerformed
        ingresar.doClick();        // TODO add your handling code here:
    }//GEN-LAST:event_pasActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new login().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel contener_ingresar;
    private javax.swing.JPanel contener_salir;
    private javax.swing.JButton ingresar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JToggleButton jToggleButton1;
    private javax.swing.JPanel logo;
    private javax.swing.JPasswordField pas;
    private javax.swing.JButton salir;
    private javax.swing.JTextField user;
    // End of variables declaration//GEN-END:variables
}
