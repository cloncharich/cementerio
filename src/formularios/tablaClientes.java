package formularios;

import clases.DatabaseConnector;
import clases.EventoTecladoUtil;
import disenho.TablaAccionCeldaEditar;
import disenho.TablaAccionCeldaRender;
import disenho.TablaDesign;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import clases.TablaAccionEvento;
import disenho.Formato;
import java.text.DecimalFormat;
import java.text.ParseException;

/**
 *
 * @author Claudio Loncharich
 */
public final class tablaClientes extends javax.swing.JInternalFrame {

    private static modalCliente ventanaCliente = null;

    public tablaClientes() {
        initComponents();
        ((javax.swing.plaf.basic.BasicInternalFrameUI) this.getUI()).setNorthPane(null);
        getContentPane().setBackground(Color.WHITE);
        mostrarTabla("");

    }

    public static void mostrarTabla(String valor) {
        DefaultTableModel modelo1 = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5;
            }
        };
        modelo1.addColumn("Número de Cliente");
        modelo1.addColumn("Nombre Cliente");
        modelo1.addColumn("Apellido Cliente");
        modelo1.addColumn("Documento");
        modelo1.addColumn("Celular");
        modelo1.addColumn("Acción");
        tbl_cli.setModel(modelo1);
        String sql = "select cod_cliente, nombres, apellidos, documento, celular FROM cliente";
        if (!valor.equals("")) {
            sql += " where nombres like '%" + valor + "%' or apellidos like '%" + valor + "%' or documento like '%" + valor + "%'";
        } else {
            sql += " order by cod_cliente ASC";
        }
        DecimalFormat formateador = new DecimalFormat("#,##0");
        String[] datos = new String[5];
        try (Connection conn = DatabaseConnector.getConnection();
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                for (int i = 0; i < 5; i++) {
                    datos[i] = rs.getString(i + 1);
                }
                try {
                    double doc = Double.parseDouble(datos[3]); 
                    datos[3] = formateador.format(doc);
                } catch (NumberFormatException e) {
                    // Manejar la excepción si no se puede convertir a número
                    datos[3] = "Valor no válido";
                }
                modelo1.addRow(datos);
            }
            tbl_cli.setModel(modelo1);
            TablaDesign.configurarTabla(tbl_cli, jScrollPane1);
            TablaAccionEvento event = new TablaAccionEvento() {
                @Override
                public void onEdit(int row) {
                    modalCliente c = new modalCliente();
                    principalMenu.escritorio.add(c);
                    Dimension desktopSize = principalMenu.escritorio.getSize();
                    Dimension FrameSize = c.getSize();
                    c.setLocation((desktopSize.width - FrameSize.width) / 2, (desktopSize.height - FrameSize.height) / 6);
                    c.show();
                    String documen = tbl_cli.getValueAt(row, 3).toString();
                    String cliente = tbl_cli.getValueAt(row, 0).toString();
                    try {
                        c.MostrarDatos(documen.replaceAll("\\p{Punct}", ""), "editar", Integer.parseInt(cliente), "actualizar");
                    } catch (ParseException ex) {
                        Logger.getLogger(tablaClientes.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }

                @Override
                public void onView(int row) {
                    modalCliente c = new modalCliente();
                    principalMenu.escritorio.add(c);
                    Dimension desktopSize = principalMenu.escritorio.getSize();
                    Dimension FrameSize = c.getSize();
                    c.setLocation((desktopSize.width - FrameSize.width) / 2, (desktopSize.height - FrameSize.height) / 6);
                    c.show();
                    String documen = tbl_cli.getValueAt(row, 3).toString();
                    String cliente = tbl_cli.getValueAt(row, 0).toString();
                    try {
                        c.MostrarDatos(documen.replaceAll("\\p{Punct}", ""), "ver", Integer.parseInt(cliente), "actualizar");
                    } catch (ParseException ex) {
                        Logger.getLogger(tablaClientes.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }

                @Override
                public void onDelete(int row) {
          
                }
            };
            tbl_cli.getColumnModel().getColumn(5).setCellRenderer(new TablaAccionCeldaRender());
            tbl_cli.getColumnModel().getColumn(5).setCellEditor(new TablaAccionCeldaEditar(event));
            tbl_cli.getColumnModel().getColumn(0).setCellRenderer(new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable jtable, Object o, boolean bln, boolean bln1, int i, int i1) {
                    setHorizontalAlignment(SwingConstants.RIGHT);
                    return super.getTableCellRendererComponent(jtable, o, bln, bln1, i, i1);
                }
            });

            searchText1.requestFocus();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        contener_nuevo = new javax.swing.JPanel();
        nuevo = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        searchText1 = new disenho.BuscarTexto();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_cli = new javax.swing.JTable();

        setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        setPreferredSize(new java.awt.Dimension(791, 581));

        jLabel1.setFont(new java.awt.Font("Roboto Medium", 0, 18)); // NOI18N
        jLabel1.setText("TITULARES");

        jPanel1.setBackground(new java.awt.Color(250, 250, 250));
        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        contener_nuevo.setBackground(new java.awt.Color(80, 90, 100));
        contener_nuevo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        nuevo.setBackground(new java.awt.Color(80, 90, 100));
        nuevo.setFont(new java.awt.Font("Roboto Medium", 1, 12)); // NOI18N
        nuevo.setForeground(new java.awt.Color(224, 224, 224));
        nuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/nuevo.png"))); // NOI18N
        nuevo.setText("NUEVO");
        nuevo.setBorder(null);
        nuevo.setContentAreaFilled(false);
        nuevo.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        nuevo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                nuevoFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                nuevoFocusLost(evt);
            }
        });
        nuevo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                nuevoMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                nuevoMouseExited(evt);
            }
        });
        nuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nuevoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout contener_nuevoLayout = new javax.swing.GroupLayout(contener_nuevo);
        contener_nuevo.setLayout(contener_nuevoLayout);
        contener_nuevoLayout.setHorizontalGroup(
            contener_nuevoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(nuevo, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        contener_nuevoLayout.setVerticalGroup(
            contener_nuevoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(contener_nuevoLayout.createSequentialGroup()
                .addComponent(nuevo, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 1, Short.MAX_VALUE))
        );

        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/buscar.png"))); // NOI18N

        searchText1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchText1ActionPerformed(evt);
            }
        });
        searchText1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                searchText1KeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(searchText1, javax.swing.GroupLayout.PREFERRED_SIZE, 396, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
            .addComponent(searchText1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(contener_nuevo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(102, 102, 102)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 149, Short.MAX_VALUE)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(212, 212, 212)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(573, Short.MAX_VALUE)))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(contener_nuevo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(12, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                    .addContainerGap(24, Short.MAX_VALUE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(1, 1, 1)))
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        tbl_cli.setAutoCreateRowSorter(true);
        tbl_cli.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        tbl_cli.setFont(new java.awt.Font("Roboto Medium", 0, 11)); // NOI18N
        tbl_cli.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tbl_cli.setFocusable(false);
        tbl_cli.setGridColor(new java.awt.Color(255, 255, 255));
        tbl_cli.setInheritsPopupMenu(true);
        tbl_cli.setOpaque(false);
        tbl_cli.setSelectionBackground(new java.awt.Color(204, 255, 204));
        tbl_cli.setSelectionForeground(new java.awt.Color(0, 0, 0));
        tbl_cli.getTableHeader().setReorderingAllowed(false);
        tbl_cli.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_cliMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbl_cli);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jScrollPane1)
                .addGap(0, 0, 0))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 444, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void nuevoMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_nuevoMouseEntered
        contener_nuevo.setBackground(new Color(51, 51, 51));        // TODO add your handling code here:
    }//GEN-LAST:event_nuevoMouseEntered

    private void nuevoMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_nuevoMouseExited
        contener_nuevo.setBackground(new Color(80, 90, 100));        // TODO add your handling code here:
    }//GEN-LAST:event_nuevoMouseExited

    private void nuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nuevoActionPerformed
        if (ventanaCliente == null || ventanaCliente.isClosed()) {
            ventanaCliente = new modalCliente();
            try {
                ventanaCliente.setSelected(true);
                principalMenu.escritorio.add(ventanaCliente);
                Dimension desktopSize = principalMenu.escritorio.getSize();
                Dimension FrameSize = ventanaCliente.getSize();
                ventanaCliente.setLocation((desktopSize.width - FrameSize.width) / 2, (desktopSize.height - FrameSize.height) / 6);
                ventanaCliente.show();

            } catch (PropertyVetoException ex) {
                Logger.getLogger(tablaClientes.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            try {
                ventanaCliente.setIcon(false);
                ventanaCliente.setSelected(true);
                ventanaCliente.toFront();
            } catch (PropertyVetoException ex) {
                Logger.getLogger(tablaClientes.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_nuevoActionPerformed

    private void tbl_cliMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_cliMouseClicked

    }//GEN-LAST:event_tbl_cliMouseClicked

    private void nuevoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_nuevoFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_nuevoFocusGained

    private void nuevoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_nuevoFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_nuevoFocusLost

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed

    private void searchText1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchText1ActionPerformed
        mostrarTabla(searchText1.getText());
        searchText1.setText("");
        searchText1.requestFocus();        // TODO add your handling code here:
    }//GEN-LAST:event_searchText1ActionPerformed

    private void searchText1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchText1KeyTyped
 EventoTecladoUtil.permitirMayusculasYNumeros(evt);         // TODO add your handling code here:
    }//GEN-LAST:event_searchText1KeyTyped


    // Variables declaration - do not modify//GEN-BEGIN:variables
    public static javax.swing.JPanel contener_nuevo;
    public static javax.swing.JButton jButton1;
    public static javax.swing.JButton jButton2;
    public static javax.swing.JLabel jLabel1;
    public static javax.swing.JLabel jLabel2;
    public static javax.swing.JPanel jPanel1;
    public static javax.swing.JPanel jPanel2;
    public static javax.swing.JPanel jPanel3;
    public static javax.swing.JScrollPane jScrollPane1;
    public static javax.swing.JButton nuevo;
    public static disenho.BuscarTexto searchText1;
    public static javax.swing.JTable tbl_cli;
    // End of variables declaration//GEN-END:variables
}
