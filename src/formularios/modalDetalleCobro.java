package formularios;

import clases.CheckBoxEditor;
import clases.CheckBoxRenderer;
import clases.DatabaseConnector;
import disenho.JTableColor;
import disenho.TablaDesign;
import java.awt.Color;
import java.awt.Component;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Claudio Loncharich
 */
public final class modalDetalleCobro extends javax.swing.JInternalFrame {

    String accionBoton = "insertar";
    public static int cod_alquiler = 0;

    public modalDetalleCobro() {
        initComponents();
        ((javax.swing.plaf.basic.BasicInternalFrameUI) this.getUI()).setNorthPane(null);
        setResizable(false);
        contener_pasar.setVisible(false);
        contener_pasar1.setVisible(false);

    }

    public static void mostrarTabla(int valor, String accion, int valor2) {
        DefaultTableModel modelo1 = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6; // La columna 6 (última columna) es editable
            }
        };
        modelo1.addColumn("Codigo");
        modelo1.addColumn("Concepto");
        modelo1.addColumn("Vencimiento");
        modelo1.addColumn("Monto");
        modelo1.addColumn("Estado");
        modelo1.addColumn("Mora");
        modelo1.addColumn("Check");

        tbl_pendiente.setModel(modelo1);
        String sql = "select * from fn_obtener_cuotas_y_mantenimiento(" + valor + "," + valor2 + ") where estado='PENDIENTE DE PAGO' order by vencimiento,concepto";
        Object[] datos = new Object[7];
        try (Connection conn = DatabaseConnector.getConnection();
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                for (int i = 0; i < 6; i++) {
                    datos[i] = rs.getObject(i + 1);
                }
                datos[6] = false; // Valor inicial para la casilla de verificación
                modelo1.addRow(datos);
            }

            TablaDesign.configurarTabla(tbl_pendiente, jScrollPane1);
            tbl_pendiente.setRowHeight(20);
            tbl_pendiente.getColumnModel().getColumn(1).setPreferredWidth(200);
            tbl_pendiente.getColumnModel().getColumn(4).setPreferredWidth(150);
            tbl_pendiente.getColumnModel().getColumn(2).setPreferredWidth(100);

            tbl_pendiente.getColumnModel().getColumn(0).setCellRenderer(new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable jtable, Object o, boolean bln, boolean bln1, int i, int i1) {
                    setHorizontalAlignment(SwingConstants.RIGHT);
                    return super.getTableCellRendererComponent(jtable, o, bln, bln1, i, i1);
                }
            });

            JTableColor renderer = new JTableColor(5);

            for (int i = 0; i < tbl_pendiente.getColumnCount(); i++) {
                tbl_pendiente.getColumnModel().getColumn(i).setCellRenderer(renderer);
            }

            tbl_pendiente.getColumnModel().getColumn(2).setCellRenderer(new DefaultTableCellRenderer() {
                private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

                @Override
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                    Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                    if (value instanceof Date) {
                        setText(dateFormat.format((Date) value));
                    }
                    return cell;
                }
            });

            tbl_pendiente.getColumnModel().getColumn(3).setCellRenderer(new DefaultTableCellRenderer() {
                private final NumberFormat numberFormat = NumberFormat.getNumberInstance();

                @Override
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                    Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                    if (value instanceof Number) {
                        setText(numberFormat.format(value));
                    }
                    return cell;
                }
            });

            // Configurar el renderizador y el editor para la columna de la casilla de verificación
            tbl_pendiente.getColumnModel().getColumn(6).setCellRenderer(new CheckBoxRenderer());
            tbl_pendiente.getColumnModel().getColumn(6).setCellEditor(new CheckBoxEditor());

            boolean enableCheckboxes = accion.equals("mover");
            for (int i = 0; i < tbl_pendiente.getRowCount(); i++) {
                JCheckBox checkBox = (JCheckBox) tbl_pendiente.getCellEditor(i, 6).getTableCellEditorComponent(tbl_pendiente, false, true, i, 6);
                checkBox.setEnabled(enableCheckboxes);
            }
            todos.setEnabled(enableCheckboxes);
            contener_pasar.setVisible(enableCheckboxes);

            cod_alquiler = valor;
            if (accion.equals("mover")) {
                contener_pasar1.setVisible(true);
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }

    /*static class CheckBoxEditor extends DefaultCellEditor {
    private final JCheckBox checkBox;
    private final boolean enableSequential;

    public CheckBoxEditor(boolean enableSequential) {
        super(new JCheckBox());
        this.checkBox = (JCheckBox) getComponent();
        this.enableSequential = enableSequential;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        if (enableSequential) {
            checkBox.setEnabled(row == 0 || (boolean) table.getValueAt(row - 1, column)); // Solo habilita si la anterior está seleccionada
        } else {
            checkBox.setEnabled(true); // Habilita siempre si no es secuencial
        }
        checkBox.setSelected((value != null && (boolean) value));
        return checkBox;
    }

    @Override
    public Object getCellEditorValue() {
        return checkBox.isSelected();
    }
}*/
    public static void transferirFilasSeleccionadas(JTable tablaOrigen, JTable tablaDestino) {
        DefaultTableModel modeloOrigen = (DefaultTableModel) tablaOrigen.getModel();
        DefaultTableModel modeloDestino = (DefaultTableModel) tablaDestino.getModel();

        boolean seleccionoAlguna = false;

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        NumberFormat numberFormat = NumberFormat.getNumberInstance();

        // Recorrer la tabla de origen
        for (int i = 0; i < modeloOrigen.getRowCount(); i++) {
            Boolean seleccionada = (Boolean) modeloOrigen.getValueAt(i, 6);

            if (seleccionada != null && seleccionada) {
                seleccionoAlguna = true;

                Object[] fila = new Object[5];
                for (int j = 0; j < 5; j++) {
                    Object valor = modeloOrigen.getValueAt(i, j);

                    if (j == 2 && valor instanceof Date) {
                        fila[j] = dateFormat.format((Date) valor);
                    } else if (j == 3 && valor instanceof Number) {
                        fila[j] = numberFormat.format(valor);
                    } else {
                        fila[j] = valor;
                    }
                }

                modeloDestino.addRow(fila);
            }
        }

        if (!seleccionoAlguna) {
            JOptionPane.showMessageDialog(null, "No se ha seleccionado ningún registro.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelModalCliente = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        contener_salir = new javax.swing.JPanel();
        salir = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_pendiente = new javax.swing.JTable();
        todos = new javax.swing.JCheckBox();
        contener_pasar = new javax.swing.JPanel();
        btnGuardar = new javax.swing.JButton();
        contener_pasar1 = new javax.swing.JPanel();
        btnGuardar1 = new javax.swing.JButton();

        setBackground(new java.awt.Color(255, 255, 255));
        setBorder(javax.swing.BorderFactory.createEtchedBorder());
        setFrameIcon(null);
        setPreferredSize(new java.awt.Dimension(794, 387));

        panelModalCliente.setBackground(new java.awt.Color(255, 255, 255));
        panelModalCliente.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel4.setFont(new java.awt.Font("Roboto Medium", 0, 18)); // NOI18N
        jLabel4.setText("DETALLES PENDIENTE COBROS");
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

        panelModalCliente.add(contener_salir, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 0, -1, -1));

        tbl_pendiente.setAutoCreateRowSorter(true);
        tbl_pendiente.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        tbl_pendiente.setFont(new java.awt.Font("Roboto Medium", 0, 10)); // NOI18N
        tbl_pendiente.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tbl_pendiente.setFocusable(false);
        tbl_pendiente.setGridColor(new java.awt.Color(255, 255, 255));
        tbl_pendiente.setInheritsPopupMenu(true);
        tbl_pendiente.setOpaque(false);
        tbl_pendiente.setSelectionBackground(new java.awt.Color(204, 255, 204));
        tbl_pendiente.setSelectionForeground(new java.awt.Color(0, 0, 0));
        tbl_pendiente.getTableHeader().setReorderingAllowed(false);
        tbl_pendiente.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_pendienteMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbl_pendiente);

        panelModalCliente.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, 770, 220));

        todos.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        todos.setText("Marcar Todos");
        todos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                todosActionPerformed(evt);
            }
        });
        panelModalCliente.add(todos, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 60, 110, 20));

        contener_pasar.setBackground(new java.awt.Color(80, 90, 100));

        btnGuardar.setBackground(new java.awt.Color(153, 204, 255));
        btnGuardar.setFont(new java.awt.Font("Roboto Medium", 1, 12)); // NOI18N
        btnGuardar.setForeground(new java.awt.Color(255, 255, 255));
        btnGuardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/enviar.png"))); // NOI18N
        btnGuardar.setText("ENVIAR");
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

        javax.swing.GroupLayout contener_pasarLayout = new javax.swing.GroupLayout(contener_pasar);
        contener_pasar.setLayout(contener_pasarLayout);
        contener_pasarLayout.setHorizontalGroup(
            contener_pasarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(contener_pasarLayout.createSequentialGroup()
                .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 1, Short.MAX_VALUE))
        );
        contener_pasarLayout.setVerticalGroup(
            contener_pasarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnGuardar, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
        );

        panelModalCliente.add(contener_pasar, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 310, 90, 30));

        contener_pasar1.setBackground(new java.awt.Color(80, 90, 100));

        btnGuardar1.setBackground(new java.awt.Color(153, 204, 255));
        btnGuardar1.setFont(new java.awt.Font("Roboto Medium", 1, 12)); // NOI18N
        btnGuardar1.setForeground(new java.awt.Color(255, 255, 255));
        btnGuardar1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/venta.png"))); // NOI18N
        btnGuardar1.setText("DESCONTAR MORA");
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

        javax.swing.GroupLayout contener_pasar1Layout = new javax.swing.GroupLayout(contener_pasar1);
        contener_pasar1.setLayout(contener_pasar1Layout);
        contener_pasar1Layout.setHorizontalGroup(
            contener_pasar1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnGuardar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        contener_pasar1Layout.setVerticalGroup(
            contener_pasar1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnGuardar1, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
        );

        panelModalCliente.add(contener_pasar1, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 310, 160, 30));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelModalCliente, javax.swing.GroupLayout.DEFAULT_SIZE, 790, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelModalCliente, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void salirMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_salirMouseEntered
        contener_salir.setBackground(Color.red);        // TODO add your handling code here:
    }//GEN-LAST:event_salirMouseEntered

    private void salirMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_salirMouseExited
        contener_salir.setBackground(Color.white);         // TODO add your handling code here:
    }//GEN-LAST:event_salirMouseExited

    private void salirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_salirActionPerformed
        dispose();        // TODO add your handling code here:
    }//GEN-LAST:event_salirActionPerformed

    private void tbl_pendienteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_pendienteMouseClicked

    }//GEN-LAST:event_tbl_pendienteMouseClicked

    private void todosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_todosActionPerformed
        boolean seleccionar = todos.isSelected();
        for (int i = 0; i < tbl_pendiente.getRowCount(); i++) {
            tbl_pendiente.setValueAt(seleccionar, i, 6); // 6 es la columna de las casillas de verificación
        }
    }//GEN-LAST:event_todosActionPerformed

    private void btnGuardarFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_btnGuardarFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_btnGuardarFocusGained

    private void btnGuardarFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_btnGuardarFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_btnGuardarFocusLost

    private void btnGuardarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnGuardarMouseEntered
        contener_pasar.setBackground(new Color(51, 51, 51));         // TODO add your handling code here:
    }//GEN-LAST:event_btnGuardarMouseEntered

    private void btnGuardarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnGuardarMouseExited
        contener_pasar.setBackground(new Color(80, 90, 100));        // TODO add your handling code here:
    }//GEN-LAST:event_btnGuardarMouseExited

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        transferirFilasSeleccionadas(tbl_pendiente, modalCobro.tbl_venta);
        modalCobro.calcularIva();
        dispose();// TODO add your handling code here:
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void btnGuardar1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_btnGuardar1FocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_btnGuardar1FocusGained

    private void btnGuardar1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_btnGuardar1FocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_btnGuardar1FocusLost

    private void btnGuardar1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnGuardar1MouseEntered
        contener_pasar1.setBackground(new Color(51, 51, 51));         // TODO add your handling code here:
    }//GEN-LAST:event_btnGuardar1MouseEntered

    private void btnGuardar1MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnGuardar1MouseExited
        contener_pasar1.setBackground(new Color(80, 90, 100));         // TODO add your handling code here:
    }//GEN-LAST:event_btnGuardar1MouseExited

    private void btnGuardar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardar1ActionPerformed
        mostrarTabla(cod_alquiler, "mover", 0); 

// TODO add your handling code here:
    }//GEN-LAST:event_btnGuardar1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    public static javax.swing.JButton btnGuardar;
    public static javax.swing.JButton btnGuardar1;
    public static javax.swing.JPanel contener_pasar;
    public static javax.swing.JPanel contener_pasar1;
    public static javax.swing.JPanel contener_salir;
    public static javax.swing.JLabel jLabel4;
    public static javax.swing.JScrollPane jScrollPane1;
    public static javax.swing.JPanel panelModalCliente;
    public static javax.swing.JButton salir;
    public static javax.swing.JTable tbl_pendiente;
    public static javax.swing.JCheckBox todos;
    // End of variables declaration//GEN-END:variables
}
