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


/**
 *
 * @author Claudio Loncharich
 */
public final class tablaAlquiler extends javax.swing.JInternalFrame {

    private static modalAlquiler ventanaAlquiler = null;

    public tablaAlquiler() {
        initComponents();
        ((javax.swing.plaf.basic.BasicInternalFrameUI) this.getUI()).setNorthPane(null);
        getContentPane().setBackground(Color.WHITE);
        mostrarTabla("");

    }

    public static void mostrarTabla(String valor) {
        DefaultTableModel modelo1 = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6;
            }
        };
        modelo1.addColumn("Número de Alquiler");
        modelo1.addColumn("Titular");
        modelo1.addColumn("Numero Lote");
        modelo1.addColumn("Monto Entrega");
        modelo1.addColumn("Monto Cuota");
        modelo1.addColumn("Fecha Vencimiento");
        modelo1.addColumn("Acción");
        tbl_alquiler.setModel(modelo1);
        String sql = " WITH cliente_info AS (\n"
                + "    SELECT a.cod_cabecera,\n"
                + "           CONCAT(t.nombres, ' ', t.apellidos) AS cliente_nombre,\n"
                + "           CONCAT(m.codigo, '-', l.numero_lote, '-', l.serie) AS lote_numero,\n"
                + "           TO_CHAR(a.entrega, 'FM999G999G999G990') AS entrega,\n"
                + "           TO_CHAR(a.cuota, 'FM999G999G999G990') AS cuota,\n"
                + "           TO_CHAR(a.fecha_vencimiento, 'DD/MM/YYYY') AS fecha_vencimiento\n"
                + "    FROM alquiler_lote_cliente AS a\n"
                + "    INNER JOIN cliente AS t ON t.cod_cliente = a.cod_cliente\n"
                + "    INNER JOIN lote AS l ON l.cod_lote = a.cod_lote\n"
                + "    INNER JOIN manzana AS m ON m.cod_manzana = l.cod_manzana "
                + "WHERE a.estado_registro = 'A'\n"
                + ")\n"
                + "SELECT * FROM cliente_info";
        if (!valor.equals("")) {
            sql += " where cliente_nombre LIKE '%" + valor + "%' or lote_numero LIKE '%" + valor + "%'";
        } else {
            sql += " order by cod_cabecera ASC";
        }
        String[] datos = new String[6];
        try (Connection conn = DatabaseConnector.getConnection();
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                for (int i = 0; i < 6; i++) {
                    datos[i] = rs.getString(i + 1);
                }
                modelo1.addRow(datos);
            }
            tbl_alquiler.setModel(modelo1);
            TablaDesign.configurarTabla(tbl_alquiler, jScrollPane1);
            TablaAccionEvento event = new TablaAccionEvento() {
                @Override
                public void onEdit(int row) {
                    modalAlquiler c = new modalAlquiler();
                    principalMenu.escritorio.add(c);
                    Dimension desktopSize = principalMenu.escritorio.getSize();
                    Dimension FrameSize = c.getSize();
                    c.setLocation((desktopSize.width - FrameSize.width) / 2, (desktopSize.height - FrameSize.height) / 6);
                    c.show();
                    String alquiler = tbl_alquiler.getValueAt(row, 0).toString();
                    c.MostrarDatos("editar", Integer.parseInt(alquiler), "actualizar");

                }

                @Override
                public void onView(int row) {
                    modalAlquiler c = new modalAlquiler();
                    principalMenu.escritorio.add(c);
                    Dimension desktopSize = principalMenu.escritorio.getSize();
                    Dimension FrameSize = c.getSize();
                    c.setLocation((desktopSize.width - FrameSize.width) / 2, (desktopSize.height - FrameSize.height) / 6);
                    c.show();
                    String alquiler = tbl_alquiler.getValueAt(row, 0).toString();
                    c.MostrarDatos("ver", Integer.parseInt(alquiler), "actualizar");

                }

                @Override
                public void onDelete(int row) {
                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }
            };
            tbl_alquiler.getColumnModel().getColumn(6).setCellRenderer(new TablaAccionCeldaRender());
            tbl_alquiler.getColumnModel().getColumn(6).setCellEditor(new TablaAccionCeldaEditar(event));
            tbl_alquiler.getColumnModel().getColumn(0).setCellRenderer(new DefaultTableCellRenderer() {
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
    
    void MostrarMorosos(){
        
          DefaultTableModel modelo1 = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6;
            }
        };
        modelo1.addColumn("Codigo de Alquiler");
        modelo1.addColumn("Documento Titular");
        modelo1.addColumn("Titular");
        modelo1.addColumn("Numero Lote");
        modelo1.addColumn("Monto Cuota");
        modelo1.addColumn("Ultimo Vencimiento");
        modelo1.addColumn("Cant.Cuota Mora");
        modelo1.addColumn("Monto Total Deuda");
        tbl_alquiler.setModel(modelo1);
        String sql = "SELECT * FROM verificar_mora_clientes()";
       
        String[] datos = new String[8];
        try (Connection conn = DatabaseConnector.getConnection();
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                for (int i = 0; i < 8; i++) {
                    datos[i] = rs.getString(i + 1);
                }
                modelo1.addRow(datos);
            }
            tbl_alquiler.setModel(modelo1);
            TablaDesign.configurarTabla(tbl_alquiler, jScrollPane1);
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
        contener_nuevo1 = new javax.swing.JPanel();
        nuevo1 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_alquiler = new javax.swing.JTable();

        setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        setPreferredSize(new java.awt.Dimension(791, 581));

        jLabel1.setFont(new java.awt.Font("Roboto Medium", 0, 18)); // NOI18N
        jLabel1.setText("ALQUILERES DE LOTES");

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

        contener_nuevo1.setBackground(new java.awt.Color(80, 90, 100));
        contener_nuevo1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        nuevo1.setBackground(new java.awt.Color(80, 90, 100));
        nuevo1.setFont(new java.awt.Font("Roboto Medium", 1, 12)); // NOI18N
        nuevo1.setForeground(new java.awt.Color(224, 224, 224));
        nuevo1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/nuevo.png"))); // NOI18N
        nuevo1.setText("VENCIDOS");
        nuevo1.setBorder(null);
        nuevo1.setContentAreaFilled(false);
        nuevo1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        nuevo1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                nuevo1FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                nuevo1FocusLost(evt);
            }
        });
        nuevo1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                nuevo1MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                nuevo1MouseExited(evt);
            }
        });
        nuevo1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nuevo1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout contener_nuevo1Layout = new javax.swing.GroupLayout(contener_nuevo1);
        contener_nuevo1.setLayout(contener_nuevo1Layout);
        contener_nuevo1Layout.setHorizontalGroup(
            contener_nuevo1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(contener_nuevo1Layout.createSequentialGroup()
                .addComponent(nuevo1, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        contener_nuevo1Layout.setVerticalGroup(
            contener_nuevo1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(contener_nuevo1Layout.createSequentialGroup()
                .addComponent(nuevo1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 1, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(contener_nuevo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(contener_nuevo1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 131, Short.MAX_VALUE)
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
                    .addComponent(contener_nuevo1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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

        tbl_alquiler.setAutoCreateRowSorter(true);
        tbl_alquiler.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        tbl_alquiler.setFont(new java.awt.Font("Roboto Medium", 0, 11)); // NOI18N
        tbl_alquiler.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tbl_alquiler.setFocusable(false);
        tbl_alquiler.setGridColor(new java.awt.Color(255, 255, 255));
        tbl_alquiler.setInheritsPopupMenu(true);
        tbl_alquiler.setOpaque(false);
        tbl_alquiler.setSelectionBackground(new java.awt.Color(204, 255, 204));
        tbl_alquiler.setSelectionForeground(new java.awt.Color(0, 0, 0));
        tbl_alquiler.getTableHeader().setReorderingAllowed(false);
        tbl_alquiler.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_alquilerMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbl_alquiler);

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
        if (ventanaAlquiler == null || ventanaAlquiler.isClosed()) {
            ventanaAlquiler = new modalAlquiler();
            try {
                ventanaAlquiler.setSelected(true);
                principalMenu.escritorio.add(ventanaAlquiler);
                Dimension desktopSize = principalMenu.escritorio.getSize();
                Dimension FrameSize = ventanaAlquiler.getSize();
                ventanaAlquiler.setLocation((desktopSize.width - FrameSize.width) / 2, (desktopSize.height - FrameSize.height) / 6);
                ventanaAlquiler.show();

            } catch (PropertyVetoException ex) {
                Logger.getLogger(tablaAlquiler.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            try {
                ventanaAlquiler.setIcon(false);
                ventanaAlquiler.setSelected(true);
                ventanaAlquiler.toFront();
            } catch (PropertyVetoException ex) {
                Logger.getLogger(tablaAlquiler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_nuevoActionPerformed

    private void tbl_alquilerMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_alquilerMouseClicked

    }//GEN-LAST:event_tbl_alquilerMouseClicked

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

    private void nuevo1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_nuevo1FocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_nuevo1FocusGained

    private void nuevo1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_nuevo1FocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_nuevo1FocusLost

    private void nuevo1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_nuevo1MouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_nuevo1MouseEntered

    private void nuevo1MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_nuevo1MouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_nuevo1MouseExited

    private void nuevo1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nuevo1ActionPerformed
MostrarMorosos();        // TODO add your handling code here:
    }//GEN-LAST:event_nuevo1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    public static javax.swing.JPanel contener_nuevo;
    public static javax.swing.JPanel contener_nuevo1;
    public static javax.swing.JButton jButton1;
    public static javax.swing.JButton jButton2;
    public static javax.swing.JLabel jLabel1;
    public static javax.swing.JLabel jLabel2;
    public static javax.swing.JPanel jPanel1;
    public static javax.swing.JPanel jPanel2;
    public static javax.swing.JPanel jPanel3;
    public static javax.swing.JScrollPane jScrollPane1;
    public static javax.swing.JButton nuevo;
    public static javax.swing.JButton nuevo1;
    public static disenho.BuscarTexto searchText1;
    public static javax.swing.JTable tbl_alquiler;
    // End of variables declaration//GEN-END:variables
}
