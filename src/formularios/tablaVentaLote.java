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
import disenho.JTableColor;

/**
 *
 * @author Claudio Loncharich
 */
public final class tablaVentaLote extends javax.swing.JInternalFrame {

    private static modalVentaLote ventanaVentaLote = null;

    public tablaVentaLote() {
        initComponents();
        ((javax.swing.plaf.basic.BasicInternalFrameUI) this.getUI()).setNorthPane(null);
        getContentPane().setBackground(Color.WHITE);
        mostrarTabla("");
        //contener_nuevo2.setVisible(false);

    }

    public static void mostrarTabla(String valor) {
        DefaultTableModel modelo1 = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6;
            }
        };
        modelo1.addColumn("Número de Venta");
        modelo1.addColumn("Titular");
        modelo1.addColumn("Numero Lote");
        modelo1.addColumn("Monto Entrega");
        modelo1.addColumn("Monto Cuota");
        modelo1.addColumn("Primer Vencimiento");
        modelo1.addColumn("Acción");
        tbl_ventaLote.setModel(modelo1);
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
                + "WHERE a.estado_registro = 'A' and a.tipo='V'\n"
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
            tbl_ventaLote.setModel(modelo1);
            TablaDesign.configurarTabla(tbl_ventaLote, jScrollPane1);
            TablaAccionEvento event = new TablaAccionEvento() {
                @Override
                public void onEdit(int row) {
                    modalVentaLote c = new modalVentaLote();
                    principalMenu.escritorio.add(c);
                    Dimension desktopSize = principalMenu.escritorio.getSize();
                    Dimension FrameSize = c.getSize();
                    c.setLocation((desktopSize.width - FrameSize.width) / 2, (desktopSize.height - FrameSize.height) / 6);
                    c.show();
                    String alquiler = tbl_ventaLote.getValueAt(row, 0).toString();
                    c.MostrarDatos("editar", Integer.parseInt(alquiler), "actualizar");

                }

                @Override
                public void onView(int row) {
                    modalVentaLote c = new modalVentaLote();
                    principalMenu.escritorio.add(c);
                    Dimension desktopSize = principalMenu.escritorio.getSize();
                    Dimension FrameSize = c.getSize();
                    c.setLocation((desktopSize.width - FrameSize.width) / 2, (desktopSize.height - FrameSize.height) / 6);
                    c.show();
                    String alquiler = tbl_ventaLote.getValueAt(row, 0).toString();
                    c.MostrarDatos("ver", Integer.parseInt(alquiler), "actualizar");

                }

                @Override
                public void onDelete(int row) {
                    modalVentaLote c = new modalVentaLote();
                    principalMenu.escritorio.add(c);
                    Dimension desktopSize = principalMenu.escritorio.getSize();
                    Dimension FrameSize = c.getSize();
                    c.setLocation((desktopSize.width - FrameSize.width) / 2, (desktopSize.height - FrameSize.height) / 6);
                    c.show();
                    String alquiler = tbl_ventaLote.getValueAt(row, 0).toString();
                    c.MostrarDatos("anulacion", Integer.parseInt(alquiler), "anular");
                }
            };
            tbl_ventaLote.getColumnModel().getColumn(6).setCellRenderer(new TablaAccionCeldaRender());
            tbl_ventaLote.getColumnModel().getColumn(6).setCellEditor(new TablaAccionCeldaEditar(event));
            tbl_ventaLote.getColumnModel().getColumn(0).setCellRenderer(new DefaultTableCellRenderer() {
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

    void MostrarMorosos() {

        DefaultTableModel modelo1 = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 8;
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
        tbl_ventaLote.setModel(modelo1);
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
            tbl_ventaLote.setModel(modelo1);
            TablaDesign.configurarTabla(tbl_ventaLote, jScrollPane1);
            //searchText1.requestFocus();
            jPanel3.setVisible(false);

            JTableColor renderer = new JTableColor(4);

            for (int i = 0; i < tbl_ventaLote.getColumnCount(); i++) {
                tbl_ventaLote.getColumnModel().getColumn(i).setCellRenderer(renderer);
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }

    }

    public void mover() throws PropertyVetoException {
        int fila = tbl_ventaLote.getSelectedRow();
        if (fila >= 0) {
            modalCobro c = new modalCobro();
            principalMenu.escritorio.add(c);
            Dimension desktopSize = principalMenu.escritorio.getSize();
            Dimension FrameSize = c.getSize();
            c.setLocation((desktopSize.width - FrameSize.width) / 2, (desktopSize.height - FrameSize.height) / 2);
            c.show();
            c.setMaximum(true);
            modalCobro.MostrarDatos(Integer.parseInt(tbl_ventaLote.getValueAt(fila, 0).toString()), "ABRIR");
            modalCobro.focus.doClick();

        } else {
            JOptionPane.showMessageDialog(null, "Por favor seleccione un alquiler para poder generar el cobro", "AVISO", JOptionPane.INFORMATION_MESSAGE);
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
        contener_nuevo2 = new javax.swing.JPanel();
        nuevo2 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_ventaLote = new javax.swing.JTable();

        setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        setPreferredSize(new java.awt.Dimension(791, 581));

        jLabel1.setFont(new java.awt.Font("Roboto Medium", 0, 18)); // NOI18N
        jLabel1.setText("VENTA DE LOTES");

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

        contener_nuevo2.setBackground(new java.awt.Color(80, 90, 100));
        contener_nuevo2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        nuevo2.setBackground(new java.awt.Color(80, 90, 100));
        nuevo2.setFont(new java.awt.Font("Roboto Medium", 1, 12)); // NOI18N
        nuevo2.setForeground(new java.awt.Color(224, 224, 224));
        nuevo2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/venta.png"))); // NOI18N
        nuevo2.setText("COBROS");
        nuevo2.setBorder(null);
        nuevo2.setContentAreaFilled(false);
        nuevo2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        nuevo2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                nuevo2FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                nuevo2FocusLost(evt);
            }
        });
        nuevo2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                nuevo2MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                nuevo2MouseExited(evt);
            }
        });
        nuevo2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nuevo2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout contener_nuevo2Layout = new javax.swing.GroupLayout(contener_nuevo2);
        contener_nuevo2.setLayout(contener_nuevo2Layout);
        contener_nuevo2Layout.setHorizontalGroup(
            contener_nuevo2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(contener_nuevo2Layout.createSequentialGroup()
                .addComponent(nuevo2, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        contener_nuevo2Layout.setVerticalGroup(
            contener_nuevo2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(contener_nuevo2Layout.createSequentialGroup()
                .addComponent(nuevo2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                .addComponent(contener_nuevo2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(120, 120, 120)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 17, Short.MAX_VALUE)
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
                    .addComponent(contener_nuevo2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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

        tbl_ventaLote.setAutoCreateRowSorter(true);
        tbl_ventaLote.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        tbl_ventaLote.setFont(new java.awt.Font("Roboto Medium", 0, 11)); // NOI18N
        tbl_ventaLote.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tbl_ventaLote.setFocusable(false);
        tbl_ventaLote.setGridColor(new java.awt.Color(255, 255, 255));
        tbl_ventaLote.setInheritsPopupMenu(true);
        tbl_ventaLote.setOpaque(false);
        tbl_ventaLote.setSelectionBackground(new java.awt.Color(204, 255, 204));
        tbl_ventaLote.setSelectionForeground(new java.awt.Color(0, 0, 0));
        tbl_ventaLote.getTableHeader().setReorderingAllowed(false);
        tbl_ventaLote.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_ventaLoteMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbl_ventaLote);

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
        if (ventanaVentaLote == null || ventanaVentaLote.isClosed()) {
            ventanaVentaLote = new modalVentaLote();
            try {
                ventanaVentaLote.setSelected(true);
                principalMenu.escritorio.add(ventanaVentaLote);
                Dimension desktopSize = principalMenu.escritorio.getSize();
                Dimension FrameSize = ventanaVentaLote.getSize();
                ventanaVentaLote.setLocation((desktopSize.width - FrameSize.width) / 2, (desktopSize.height - FrameSize.height) / 6);
                ventanaVentaLote.show();

            } catch (PropertyVetoException ex) {
                Logger.getLogger(tablaVentaLote.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            try {
                ventanaVentaLote.setIcon(false);
                ventanaVentaLote.setSelected(true);
                ventanaVentaLote.toFront();
            } catch (PropertyVetoException ex) {
                Logger.getLogger(tablaVentaLote.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_nuevoActionPerformed

    private void tbl_ventaLoteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_ventaLoteMouseClicked

    }//GEN-LAST:event_tbl_ventaLoteMouseClicked

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

    private void nuevo2FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_nuevo2FocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_nuevo2FocusGained

    private void nuevo2FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_nuevo2FocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_nuevo2FocusLost

    private void nuevo2MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_nuevo2MouseEntered
        contener_nuevo2.setBackground(new Color(51, 51, 51));         // TODO add your handling code here:
    }//GEN-LAST:event_nuevo2MouseEntered

    private void nuevo2MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_nuevo2MouseExited
        contener_nuevo2.setBackground(new Color(80, 90, 100));        // TODO add your handling code here:
    }//GEN-LAST:event_nuevo2MouseExited

    private void nuevo2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nuevo2ActionPerformed
        try {
            mover();        // TODO add your handling code here:
        } catch (PropertyVetoException ex) {
            Logger.getLogger(tablaVentaLote.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_nuevo2ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    public static javax.swing.JPanel contener_nuevo;
    public static javax.swing.JPanel contener_nuevo2;
    public static javax.swing.JButton jButton1;
    public static javax.swing.JButton jButton2;
    public static javax.swing.JLabel jLabel1;
    public static javax.swing.JLabel jLabel2;
    public static javax.swing.JPanel jPanel1;
    public static javax.swing.JPanel jPanel2;
    public static javax.swing.JPanel jPanel3;
    public static javax.swing.JScrollPane jScrollPane1;
    public static javax.swing.JButton nuevo;
    public static javax.swing.JButton nuevo2;
    public static disenho.BuscarTexto searchText1;
    public static javax.swing.JTable tbl_ventaLote;
    // End of variables declaration//GEN-END:variables
}
