package formularios;

import clases.CustomSQLExceptionHandler;
import clases.DatabaseConnector;
import clases.DatabaseManager;
import clases.EventoTecladoUtil;
import clases.FileChooserHandler;
import clases.PathPrincipal;
import clases.ReportPrinter;
import disenho.Formato;
import disenho.JTableColor;
import disenho.TablaDesign;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Claudio Loncharich
 */
public final class modalDifunto extends javax.swing.JInternalFrame {

    String accionBoton = "insertar";
    int cod_difunto = 0;
    int contador = 0;
    String ruta = "";

    public modalDifunto() {
        initComponents();
        ((javax.swing.plaf.basic.BasicInternalFrameUI) this.getUI()).setNorthPane(null);
        setResizable(false);
        ruta = PathPrincipal.obtenerPathStorage();
        contener_detalle.setVisible(false);


    }

    void MostrarDetalleLotes() {
        int codigo_alqui;
        if (alquiler_cod.getText().isEmpty() || alquiler_cod.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Favor buscar primeramente el alquiler del titular");
        } else {
            codigo_alqui = Integer.parseInt(alquiler_cod.getText());
            DefaultTableModel modelo1 = new DefaultTableModel() {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return column == 4;
                }
            };
            modelo1.addColumn("Código Detalle Lote");
            modelo1.addColumn("Estado");
            modelo1.addColumn("DiFunto");
            modelo1.addColumn("Documento Difunto");
            tbl_lote_detalle.setModel(modelo1);
            String sql = "  select dt.cod_detalle_lote,\n"
                    + "case\n"
                    + "when dt.estado_registro='L' then 'LIBRE'\n"
                    + "when dt.estado_registro='O' then 'OCUPADO'\n"
                    + "end as estado,\n"
                    + "coalesce (d.nombres,'') AS difunto_nombre,\n"
                    + "d.documento\n"
                    + "from detalle_lote dt\n"
                    + "inner join lote l on l.cod_lote=dt.cod_lote\n"
                    + "inner join alquiler_lote_cliente a on l.cod_lote = a.cod_lote\n"
                    + "left join difunto d on d.cod_det_lote=dt.cod_detalle_lote\n"
                    + "Where a.cod_cabecera=" + codigo_alqui + " order by dt.cod_detalle_lote asc";
            String[] datos = new String[4];
            try (Connection conn = DatabaseConnector.getConnection();
                    Statement st = conn.createStatement();
                    ResultSet rs = st.executeQuery(sql)) {

                while (rs.next()) {
                    for (int i = 0; i < 4; i++) {
                        datos[i] = rs.getString(i + 1);
                    }
                    modelo1.addRow(datos);
                }
                tbl_lote_detalle.setModel(modelo1);
                TablaDesign.configurarTabla(tbl_lote_detalle, jScrollPane1);
                tbl_lote_detalle.getColumnModel().getColumn(0).setCellRenderer(new DefaultTableCellRenderer() {
                    @Override
                    public Component getTableCellRendererComponent(JTable jtable, Object o, boolean bln, boolean bln1, int i, int i1) {
                        setHorizontalAlignment(SwingConstants.RIGHT);
                        return super.getTableCellRendererComponent(jtable, o, bln, bln1, i, i1);
                    }
                });

                JTableColor pintar = new JTableColor(1);
                for (int i = 0; i < 4; i++) {
                    tbl_lote_detalle.getColumnModel().getColumn(i).setCellRenderer(pintar);

                }

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, ex);
            }
        }
    }

    private void GrabarDatos() {
        //  tabla en base difunto
        try {
            String queryInsert = "INSERT INTO public.difunto(\n"
                    + "	nombres, documento, nacionalidad, sexo, fecha_nacimiento, lugar_nacimiento, fecha_fallecimiento, motivo_deceso, nro_cer_defucion,cod_alquiler,residencia,edad)\n"
                    + "	VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?);";

            String queryUpdate = "UPDATE public.difunto\n"
                    + "	SET  nombres=?, documento=?, nacionalidad=?, sexo=?, fecha_nacimiento=?, lugar_nacimiento=?, fecha_fallecimiento=?, motivo_deceso=?, nro_cer_defucion=?,cod_alquiler=?,residencia=?,edad=?\n"
                    + "	WHERE cod_difunto=?;";

            String nom = difunto.getText().replaceAll("\\p{Punct}", "");//campo nombres
            String doc = documento_difunto.getText().replaceAll("\\p{Punct}", ""); // campo documento
            String nac = nacionalidad.getText();//campo nacionalidad
            String sex = ""; //campo sexo
            if (masculino.isSelected()) {
                sex = "M";
            } else {
                if (femenino.isSelected()) {
                    sex = "F";
                }
            }

            DateFormat dateFormat = new SimpleDateFormat("yyy-MM-dd");
            String fecha_nac = dateFormat.format(fecha_nacimiento.getDate());
            java.sql.Date fecNac = java.sql.Date.valueOf(fecha_nac);
            String lug = lugar_nacimiento.getText();
            String fecha_fall = dateFormat.format(fecha_fallecimiento.getDate());
            java.sql.Date fecFa = java.sql.Date.valueOf(fecha_fall);
            String mov = motivo_deceso.getText();
            String cer = certificado.getText();
            int al = Integer.parseInt(alquiler_cod.getText());
            String res=residencia.getText();
            int ed=Integer.parseInt(edad_difunto.getText());

            int rowsAffected = 0;
            if ("insertar".equalsIgnoreCase(accionBoton)) {
                rowsAffected = DatabaseManager.insert(queryInsert, nom, doc, nac, sex, fecNac, lug, fecFa, mov, cer, al,res,ed);
            } else if ("actualizar".equalsIgnoreCase(accionBoton)) {
                rowsAffected = DatabaseManager.update(queryUpdate,nom, doc, nac, sex, fecNac, lug, fecFa, mov, cer, al,res,ed, cod_difunto);
            }

            if (rowsAffected > 0) {
                contador = 1;
                btnGuardar.setEnabled(false);
                btnCancelar.setEnabled(false);
                if ("insertar".equalsIgnoreCase(accionBoton)) {
                    JOptionPane.showMessageDialog(this, "Difunto registrado con exito", "AVISO", JOptionPane.PLAIN_MESSAGE, Formato.icono("/imagenes/check.png", 40, 40));
                } else if ("actualizar".equalsIgnoreCase(accionBoton)) {
                    JOptionPane.showMessageDialog(this, "Datos actualizado con exito", "AVISO", JOptionPane.PLAIN_MESSAGE, Formato.icono("/imagenes/check.png", 40, 40));
                }
                dispose();
                tablaDifunto.mostrarTabla("");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            //JOptionPane.showMessageDialog(modalAlquiler.this, e.getMessage(), "Error al grabar/actualizar registro", JOptionPane.ERROR_MESSAGE);
            CustomSQLExceptionHandler.showCustomMessage(e);

        }
    }

 public void MostrarDatos(String opcion,int dif, int id, String accion) {
    accionBoton = accion;
    cod_difunto=dif;
    String query = "SELECT \n"
            + "    d.cod_difunto,\n"
            + "    d.nombres,\n"
            + "    d.documento,\n"
            + "    TO_CHAR(d.fecha_fallecimiento, 'DD/MM/YYYY') AS fecha_muerte,\n"
            + "    TO_CHAR(d.fecha_nacimiento, 'DD/MM/YYYY') AS fecha_nacimiento,\n"
            + "    d.nacionalidad,\n"
            + "    d.lugar_nacimiento,\n"
            + "    d.nro_cer_defucion,\n"
            + "    d.motivo_deceso,d.residencia,d.edad,\n"
            + "    CASE\n"
            + "        WHEN d.sexo = 'M' THEN 'MASCULINO'\n"
            + "        WHEN d.sexo = 'F' THEN 'FEMENINO'\n"
            + "    END AS sexo,\n"
            + "    CONCAT(m.codigo, '-', l.numero_lote, '-', l.serie) AS numero_lote,\n"
            + "    d.cod_alquiler,\n"
            + "    cl.nombres || ' ' || cl.apellidos AS nombre_completo_cliente,\n"
            + "    cl.documento,\n"
            + "    cl.celular,cl.documento as documento_titular\n"
            + "FROM \n"
            + "    difunto d\n"
            + "INNER JOIN \n"
            + "    alquiler_lote_cliente al ON al.cod_cabecera = d.cod_alquiler\n"
            + "INNER JOIN \n"
            + "    cliente cl ON cl.cod_cliente = al.cod_cliente\n"
            + "INNER JOIN \n"
            + "    lote l ON l.cod_lote = al.cod_lote\n"
            + "INNER JOIN\n"
            + "    manzana m ON m.cod_manzana = l.cod_manzana\n"
            + "WHERE d.cod_alquiler = '" + id + "'";

    try (Connection conn = DatabaseConnector.getConnection();
         Statement st = conn.createStatement();
         ResultSet rs = st.executeQuery(query)) {

        if (rs.next()) {
            alquiler_cod.setText(rs.getString("cod_alquiler"));
            titular.setText(rs.getString("nombre_completo_cliente"));
            documento_titular.setText(rs.getString("documento_titular"));
            lote.setText(rs.getString("numero_lote"));
            celular_titular.setText(rs.getString("celular"));
            difunto.setText(rs.getString("nombres"));
            SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
            String fecha = rs.getString("fecha_nacimiento");
            String fecha2 = rs.getString("fecha_muerte");
            Date d, d1 = null;
            try {
                d = formato.parse(fecha);
                d1 = formato.parse(fecha2);
                fecha_nacimiento.setDate(d);
                fecha_fallecimiento.setDate(d1);
            } catch (ParseException ex) {
                Logger.getLogger(modalDifunto.class.getName()).log(Level.SEVERE, null, ex);
            }
            documento_difunto.setText(rs.getString("documento"));
            lugar_nacimiento.setText(rs.getString("lugar_nacimiento"));
            nacionalidad.setText(rs.getString("nacionalidad"));
            certificado.setText(rs.getString("nro_cer_defucion"));
            motivo_deceso.setText(rs.getString("motivo_deceso"));

            String sexo = rs.getString("sexo");
            if (sexo.equals("MASCULINO")) {
                masculino.setSelected(true);
                femenino.setSelected(false);
            } else if (sexo.equals("FEMENINO")) {
                masculino.setSelected(false);
                femenino.setSelected(true);
            }
            residencia.setText(rs.getString("residencia"));
            edad_difunto.setText(rs.getString("edad"));
            MostrarDetalleLotes();

            if (opcion.equals("ver")) {
                Formato.habilitarCampos(getContentPane(), false);
                btnGuardar.setEnabled(false);
                btnCancelar.setEnabled(false);
                btnFotoDocumento.setEnabled(false);
                btnFotoServicios.setEnabled(false);
                contener_detalle.setVisible(true);
                 btnBuscarCliente.setEnabled(false);
            }
            
              if (opcion.equals("editar")) {
                  btnBuscarCliente.setEnabled(false);
                  
              }
            contador = 1;
        }

    } catch (SQLException e) {
        e.printStackTrace();
        System.err.println("Error al ejecutar la consulta SQL: " + e.getMessage());
    }
}

 
 void ImprimirReporte(){
      ReportPrinter reportPrinter = new ReportPrinter();
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("alquiler", cod_difunto);
        reportPrinter.printReport("/reportes/SolicitudIngresoLote.jasper", parameters);
 }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelModalCliente = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        contener_salir = new javax.swing.JPanel();
        salir = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        documento_titular = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        alquiler_cod = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        fecha_fallecimiento = new com.toedter.calendar.JDateChooser();
        anulacion = new javax.swing.JLabel();
        celular_titular = new javax.swing.JFormattedTextField();
        contener_buscarCliente = new javax.swing.JPanel();
        btnBuscarCliente = new javax.swing.JButton();
        motivo_deceso = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        contener_guardar = new javax.swing.JPanel();
        btnGuardar = new javax.swing.JButton();
        contener_cancelar = new javax.swing.JPanel();
        btnCancelar = new javax.swing.JButton();
        panelFoto = new javax.swing.JPanel();
        contener_fotoDocumento = new javax.swing.JPanel();
        btnFotoDocumento = new javax.swing.JButton();
        contener_serviciosFoto = new javax.swing.JPanel();
        btnFotoServicios = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        contener_verFotos = new javax.swing.JPanel();
        btnverFotos = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_lote_detalle = new javax.swing.JTable();
        jLabel16 = new javax.swing.JLabel();
        lote = new javax.swing.JTextField();
        titular = new javax.swing.JTextField();
        certificado = new javax.swing.JTextField();
        difunto = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        fecha_nacimiento = new com.toedter.calendar.JDateChooser();
        anulacion1 = new javax.swing.JLabel();
        lugar_nacimiento = new javax.swing.JTextField();
        masculino = new javax.swing.JCheckBox();
        femenino = new javax.swing.JCheckBox();
        jLabel18 = new javax.swing.JLabel();
        anulacion2 = new javax.swing.JLabel();
        nacionalidad = new javax.swing.JTextField();
        documento_difunto = new javax.swing.JTextField();
        contener_detalle = new javax.swing.JPanel();
        btnDetalle = new javax.swing.JButton();
        jLabel19 = new javax.swing.JLabel();
        residencia = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        edad_difunto = new javax.swing.JTextField();

        setBackground(new java.awt.Color(255, 255, 255));
        setBorder(javax.swing.BorderFactory.createEtchedBorder());
        setFrameIcon(null);
        setPreferredSize(new java.awt.Dimension(892, 718));

        panelModalCliente.setBackground(new java.awt.Color(255, 255, 255));
        panelModalCliente.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel3.setFont(new java.awt.Font("Roboto Light", 0, 14)); // NOI18N
        jLabel3.setText("Numero Alquiler:");
        panelModalCliente.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 130, 34));

        jLabel4.setFont(new java.awt.Font("Roboto Medium", 0, 18)); // NOI18N
        jLabel4.setText("ALTA DIFUNTO");
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

        panelModalCliente.add(contener_salir, new org.netbeans.lib.awtextra.AbsoluteConstraints(850, 0, -1, -1));

        jLabel5.setFont(new java.awt.Font("Roboto Light", 0, 14)); // NOI18N
        jLabel5.setText("Titular:");
        panelModalCliente.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 50, 130, 34));

        documento_titular.setFont(new java.awt.Font("Roboto Light", 0, 12)); // NOI18N
        documento_titular.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        documento_titular.setEnabled(false);
        documento_titular.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                documento_titularActionPerformed(evt);
            }
        });
        documento_titular.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                documento_titularKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                documento_titularKeyTyped(evt);
            }
        });
        panelModalCliente.add(documento_titular, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 140, 320, 32));

        jLabel7.setFont(new java.awt.Font("Roboto Light", 0, 14)); // NOI18N
        jLabel7.setText("Lote:");
        panelModalCliente.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, 80, 34));

        alquiler_cod.setFont(new java.awt.Font("Roboto Light", 0, 12)); // NOI18N
        alquiler_cod.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        alquiler_cod.setEnabled(false);
        alquiler_cod.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                alquiler_codFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                alquiler_codFocusLost(evt);
            }
        });
        alquiler_cod.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                alquiler_codActionPerformed(evt);
            }
        });
        alquiler_cod.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                alquiler_codKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                alquiler_codKeyTyped(evt);
            }
        });
        panelModalCliente.add(alquiler_cod, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, 270, 32));

        jLabel10.setFont(new java.awt.Font("Roboto Light", 0, 14)); // NOI18N
        jLabel10.setText("Documento Titular:");
        panelModalCliente.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 110, 130, 34));

        jLabel13.setFont(new java.awt.Font("Roboto Light", 0, 14)); // NOI18N
        jLabel13.setText("Fecha Fallecimiento:");
        panelModalCliente.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 290, 160, 34));

        fecha_fallecimiento.setBackground(new java.awt.Color(204, 204, 204));
        fecha_fallecimiento.setForeground(new java.awt.Color(204, 204, 204));
        fecha_fallecimiento.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                fecha_fallecimientoFocusLost(evt);
            }
        });
        panelModalCliente.add(fecha_fallecimiento, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 320, 310, 32));

        anulacion.setFont(new java.awt.Font("Roboto Light", 0, 14)); // NOI18N
        anulacion.setText("Difunto:");
        panelModalCliente.add(anulacion, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 170, 130, 40));

        celular_titular.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        celular_titular.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(java.text.NumberFormat.getIntegerInstance())));
        celular_titular.setEnabled(false);
        celular_titular.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                celular_titularFocusGained(evt);
            }
        });
        celular_titular.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                celular_titularKeyTyped(evt);
            }
        });
        panelModalCliente.add(celular_titular, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 200, 310, 32));

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

        motivo_deceso.setFont(new java.awt.Font("Roboto Light", 0, 12)); // NOI18N
        motivo_deceso.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        motivo_deceso.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                motivo_decesoActionPerformed(evt);
            }
        });
        motivo_deceso.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                motivo_decesoKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                motivo_decesoKeyTyped(evt);
            }
        });
        panelModalCliente.add(motivo_deceso, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 440, 400, 32));

        jLabel15.setFont(new java.awt.Font("Roboto Light", 0, 14)); // NOI18N
        jLabel15.setText("Celular del Titular:");
        panelModalCliente.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 170, 130, 34));

        jLabel11.setFont(new java.awt.Font("Roboto Light", 0, 14)); // NOI18N
        jLabel11.setText("Sexo:");
        panelModalCliente.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 354, 70, 30));

        jLabel12.setFont(new java.awt.Font("Roboto Light", 1, 14)); // NOI18N
        jLabel12.setText("Click aqui para ver lugares disponible para el Difunto:");
        jLabel12.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel12.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel12MouseClicked(evt);
            }
        });
        panelModalCliente.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 470, 500, 34));

        jLabel14.setFont(new java.awt.Font("Roboto Light", 0, 14)); // NOI18N
        jLabel14.setText("Certificado de defunsion:");
        panelModalCliente.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 350, 200, 34));

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

        panelModalCliente.add(contener_guardar, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 660, 110, -1));

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

        panelModalCliente.add(contener_cancelar, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 660, 110, -1));

        panelFoto.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panelFoto.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        contener_fotoDocumento.setBackground(new java.awt.Color(80, 90, 100));

        btnFotoDocumento.setBackground(new java.awt.Color(153, 204, 255));
        btnFotoDocumento.setFont(new java.awt.Font("Roboto Medium", 1, 12)); // NOI18N
        btnFotoDocumento.setForeground(new java.awt.Color(255, 255, 255));
        btnFotoDocumento.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/camera.png"))); // NOI18N
        btnFotoDocumento.setText("Acta de Defunsión");
        btnFotoDocumento.setBorder(null);
        btnFotoDocumento.setContentAreaFilled(false);
        btnFotoDocumento.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnFotoDocumento.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                btnFotoDocumentoFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                btnFotoDocumentoFocusLost(evt);
            }
        });
        btnFotoDocumento.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnFotoDocumentoMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnFotoDocumentoMouseExited(evt);
            }
        });
        btnFotoDocumento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotoDocumentoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout contener_fotoDocumentoLayout = new javax.swing.GroupLayout(contener_fotoDocumento);
        contener_fotoDocumento.setLayout(contener_fotoDocumentoLayout);
        contener_fotoDocumentoLayout.setHorizontalGroup(
            contener_fotoDocumentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, contener_fotoDocumentoLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnFotoDocumento, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        contener_fotoDocumentoLayout.setVerticalGroup(
            contener_fotoDocumentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnFotoDocumento, javax.swing.GroupLayout.DEFAULT_SIZE, 34, Short.MAX_VALUE)
        );

        panelFoto.add(contener_fotoDocumento, new org.netbeans.lib.awtextra.AbsoluteConstraints(8, 67, 175, -1));

        contener_serviciosFoto.setBackground(new java.awt.Color(80, 90, 100));

        btnFotoServicios.setBackground(new java.awt.Color(153, 204, 255));
        btnFotoServicios.setFont(new java.awt.Font("Roboto Medium", 1, 12)); // NOI18N
        btnFotoServicios.setForeground(new java.awt.Color(255, 255, 255));
        btnFotoServicios.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/camera.png"))); // NOI18N
        btnFotoServicios.setText("Acta Cedula");
        btnFotoServicios.setBorder(null);
        btnFotoServicios.setContentAreaFilled(false);
        btnFotoServicios.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnFotoServicios.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                btnFotoServiciosFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                btnFotoServiciosFocusLost(evt);
            }
        });
        btnFotoServicios.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnFotoServiciosMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnFotoServiciosMouseExited(evt);
            }
        });
        btnFotoServicios.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotoServiciosActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout contener_serviciosFotoLayout = new javax.swing.GroupLayout(contener_serviciosFoto);
        contener_serviciosFoto.setLayout(contener_serviciosFotoLayout);
        contener_serviciosFotoLayout.setHorizontalGroup(
            contener_serviciosFotoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, contener_serviciosFotoLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnFotoServicios, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        contener_serviciosFotoLayout.setVerticalGroup(
            contener_serviciosFotoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnFotoServicios, javax.swing.GroupLayout.DEFAULT_SIZE, 34, Short.MAX_VALUE)
        );

        panelFoto.add(contener_serviciosFoto, new org.netbeans.lib.awtextra.AbsoluteConstraints(8, 131, 175, -1));

        jLabel2.setFont(new java.awt.Font("Roboto Light", 1, 14)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Adjuntar Documentos");
        panelFoto.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(2, 2, 187, 34));

        contener_verFotos.setBackground(new java.awt.Color(80, 90, 100));

        btnverFotos.setBackground(new java.awt.Color(153, 204, 255));
        btnverFotos.setFont(new java.awt.Font("Roboto Medium", 1, 12)); // NOI18N
        btnverFotos.setForeground(new java.awt.Color(255, 255, 255));
        btnverFotos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/camera.png"))); // NOI18N
        btnverFotos.setText("Visualizar Fotos");
        btnverFotos.setBorder(null);
        btnverFotos.setContentAreaFilled(false);
        btnverFotos.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnverFotos.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                btnverFotosFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                btnverFotosFocusLost(evt);
            }
        });
        btnverFotos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnverFotosMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnverFotosMouseExited(evt);
            }
        });
        btnverFotos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnverFotosActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout contener_verFotosLayout = new javax.swing.GroupLayout(contener_verFotos);
        contener_verFotos.setLayout(contener_verFotosLayout);
        contener_verFotosLayout.setHorizontalGroup(
            contener_verFotosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, contener_verFotosLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnverFotos, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        contener_verFotosLayout.setVerticalGroup(
            contener_verFotosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnverFotos, javax.swing.GroupLayout.DEFAULT_SIZE, 34, Short.MAX_VALUE)
        );

        panelFoto.add(contener_verFotos, new org.netbeans.lib.awtextra.AbsoluteConstraints(8, 195, 175, -1));

        panelModalCliente.add(panelFoto, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 80, 190, 270));

        tbl_lote_detalle.setAutoCreateRowSorter(true);
        tbl_lote_detalle.setFont(new java.awt.Font("Roboto Medium", 0, 11)); // NOI18N
        tbl_lote_detalle.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tbl_lote_detalle.setFocusable(false);
        tbl_lote_detalle.setGridColor(new java.awt.Color(255, 255, 255));
        tbl_lote_detalle.setInheritsPopupMenu(true);
        tbl_lote_detalle.setOpaque(false);
        tbl_lote_detalle.setSelectionBackground(new java.awt.Color(204, 255, 204));
        tbl_lote_detalle.setSelectionForeground(new java.awt.Color(0, 0, 0));
        tbl_lote_detalle.getTableHeader().setReorderingAllowed(false);
        tbl_lote_detalle.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_lote_detalleMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbl_lote_detalle);

        panelModalCliente.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 500, 870, 150));

        jLabel16.setFont(new java.awt.Font("Roboto Light", 0, 14)); // NOI18N
        jLabel16.setText("Motivo deceso:");
        panelModalCliente.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 410, 200, 34));

        lote.setFont(new java.awt.Font("Roboto Light", 0, 12)); // NOI18N
        lote.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        lote.setEnabled(false);
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
        panelModalCliente.add(lote, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 140, 310, 32));

        titular.setFont(new java.awt.Font("Roboto Light", 0, 12)); // NOI18N
        titular.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        titular.setEnabled(false);
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

        certificado.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        certificado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                certificadoActionPerformed(evt);
            }
        });
        certificado.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                certificadoKeyTyped(evt);
            }
        });
        panelModalCliente.add(certificado, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 380, 320, 32));

        difunto.setFont(new java.awt.Font("Roboto Light", 0, 12)); // NOI18N
        difunto.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        difunto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                difuntoActionPerformed(evt);
            }
        });
        difunto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                difuntoKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                difuntoKeyTyped(evt);
            }
        });
        panelModalCliente.add(difunto, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 200, 320, 32));

        jLabel17.setFont(new java.awt.Font("Roboto Light", 0, 14)); // NOI18N
        jLabel17.setText("Fecha Nacimiento:");
        panelModalCliente.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 230, 160, 34));

        fecha_nacimiento.setBackground(new java.awt.Color(204, 204, 204));
        fecha_nacimiento.setForeground(new java.awt.Color(204, 204, 204));
        fecha_nacimiento.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                fecha_nacimientoFocusLost(evt);
            }
        });
        panelModalCliente.add(fecha_nacimiento, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 260, 310, 32));

        anulacion1.setFont(new java.awt.Font("Roboto Light", 0, 14)); // NOI18N
        anulacion1.setText("Lugar de Nacimiento:");
        panelModalCliente.add(anulacion1, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 290, 150, 40));

        lugar_nacimiento.setFont(new java.awt.Font("Roboto Light", 0, 12)); // NOI18N
        lugar_nacimiento.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        lugar_nacimiento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lugar_nacimientoActionPerformed(evt);
            }
        });
        lugar_nacimiento.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                lugar_nacimientoKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                lugar_nacimientoKeyTyped(evt);
            }
        });
        panelModalCliente.add(lugar_nacimiento, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 320, 320, 32));

        masculino.setSelected(true);
        masculino.setText("Masculino");
        masculino.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                masculinoActionPerformed(evt);
            }
        });
        panelModalCliente.add(masculino, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 380, 90, 32));

        femenino.setText("Femenino");
        femenino.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                femeninoActionPerformed(evt);
            }
        });
        panelModalCliente.add(femenino, new org.netbeans.lib.awtextra.AbsoluteConstraints(770, 380, 100, 32));

        jLabel18.setFont(new java.awt.Font("Roboto Light", 0, 14)); // NOI18N
        jLabel18.setText("Documento del Difunto:");
        panelModalCliente.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 230, 200, 34));

        anulacion2.setFont(new java.awt.Font("Roboto Light", 0, 14)); // NOI18N
        anulacion2.setText("Nacionalidad:");
        panelModalCliente.add(anulacion2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 350, 150, 40));

        nacionalidad.setFont(new java.awt.Font("Roboto Light", 0, 12)); // NOI18N
        nacionalidad.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        nacionalidad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nacionalidadActionPerformed(evt);
            }
        });
        nacionalidad.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                nacionalidadKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                nacionalidadKeyTyped(evt);
            }
        });
        panelModalCliente.add(nacionalidad, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 380, 320, 32));

        documento_difunto.setFont(new java.awt.Font("Roboto Light", 0, 12)); // NOI18N
        documento_difunto.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        documento_difunto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                documento_difuntoActionPerformed(evt);
            }
        });
        documento_difunto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                documento_difuntoKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                documento_difuntoKeyTyped(evt);
            }
        });
        panelModalCliente.add(documento_difunto, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 260, 320, 32));

        contener_detalle.setBackground(new java.awt.Color(80, 90, 100));

        btnDetalle.setBackground(new java.awt.Color(153, 204, 255));
        btnDetalle.setFont(new java.awt.Font("Roboto Medium", 1, 12)); // NOI18N
        btnDetalle.setForeground(new java.awt.Color(255, 255, 255));
        btnDetalle.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/printer.png"))); // NOI18N
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

        panelModalCliente.add(contener_detalle, new org.netbeans.lib.awtextra.AbsoluteConstraints(840, 660, 40, -1));

        jLabel19.setFont(new java.awt.Font("Roboto Light", 0, 14)); // NOI18N
        jLabel19.setText("Ciudad de Residencia:");
        panelModalCliente.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 410, 200, 34));

        residencia.setFont(new java.awt.Font("Roboto Light", 0, 12)); // NOI18N
        residencia.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        residencia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                residenciaActionPerformed(evt);
            }
        });
        residencia.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                residenciaKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                residenciaKeyTyped(evt);
            }
        });
        panelModalCliente.add(residencia, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 440, 240, 32));

        jLabel20.setFont(new java.awt.Font("Roboto Light", 0, 14)); // NOI18N
        jLabel20.setText("Edad:");
        panelModalCliente.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 410, 140, 34));

        edad_difunto.setFont(new java.awt.Font("Roboto Light", 0, 12)); // NOI18N
        edad_difunto.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        edad_difunto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                edad_difuntoActionPerformed(evt);
            }
        });
        edad_difunto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                edad_difuntoKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                edad_difuntoKeyTyped(evt);
            }
        });
        panelModalCliente.add(edad_difunto, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 440, 190, 32));

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
        // Formato.habilitarCampos(getContentPane(), true);
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

    private void documento_titularActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_documento_titularActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_documento_titularActionPerformed

    private void documento_titularKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_documento_titularKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_documento_titularKeyReleased

    private void documento_titularKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_documento_titularKeyTyped
        EventoTecladoUtil.permitirNumerosComasYGuiones(evt);        // TODO add your handling code here:
    }//GEN-LAST:event_documento_titularKeyTyped

    private void alquiler_codActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_alquiler_codActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_alquiler_codActionPerformed

    private void alquiler_codKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_alquiler_codKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_alquiler_codKeyReleased

    private void alquiler_codKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_alquiler_codKeyTyped
        EventoTecladoUtil.permitirNumerosComasYGuiones(evt);          // TODO add your handling code here:
    }//GEN-LAST:event_alquiler_codKeyTyped

    private void fecha_fallecimientoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_fecha_fallecimientoFocusLost

        // TODO add your handling code here:
    }//GEN-LAST:event_fecha_fallecimientoFocusLost

    private void celular_titularFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_celular_titularFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_celular_titularFocusGained

    private void celular_titularKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_celular_titularKeyTyped

    }//GEN-LAST:event_celular_titularKeyTyped

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
            ImprimirReporte();
        } else {
            JOptionPane.showMessageDialog(this, "Debe ingresar todos los campos", "Aviso", JOptionPane.INFORMATION_MESSAGE);
        }         // TODO add your handling code here:
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
        JTextField[] tfParam = new JTextField[5];
        tfParam[0] = alquiler_cod;
        tfParam[1] = titular;
        tfParam[2] = lote;
        tfParam[3] = documento_titular;
        tfParam[4] = celular_titular;

        String sql = "SELECT a.cod_cabecera,\n"
                + "CONCAT(t.nombres, ' ', t.apellidos) AS cliente_nombre,\n"
                + "CONCAT(m.codigo, '-', l.numero_lote, '-', l.serie) AS lote_numero,\n"
                + "TO_CHAR(CAST(t.documento AS NUMERIC), 'FM999G999G999G990') AS doc_cliente,\n"
                + "t.celular\n"
                + "FROM alquiler_lote_cliente AS a\n"
                + "INNER JOIN cliente AS t ON t.cod_cliente = a.cod_cliente\n"
                + "INNER JOIN lote AS l ON l.cod_lote = a.cod_lote\n"
                + "INNER JOIN manzana AS m ON m.cod_manzana = l.cod_manzana\n"
                + "where a.estado_registro='A' and EXISTS (select 1 from detalle_lote dt where dt.cod_lote=l.cod_lote\n"
                + "											   and dt.estado_registro='L') and t.documento like ";

        buscador pp = new buscador(sql, new String[]{"Codigo Alquiler", "Titular", "Numero Lote", "Documento", "Celular "}, 5, tfParam, "Order by cod_cabecera");
        Dimension desktopSize = principalMenu.escritorio.getSize();
        Dimension FrameSize = pp.getSize();
        pp.setLocation((desktopSize.width - FrameSize.width) / 2, (desktopSize.height - FrameSize.height) / 4);
        pp.setVisible(true);        // TODO add your handling code here:
    }//GEN-LAST:event_btnBuscarClienteActionPerformed

    private void motivo_decesoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_motivo_decesoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_motivo_decesoActionPerformed

    private void motivo_decesoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_motivo_decesoKeyReleased

    }//GEN-LAST:event_motivo_decesoKeyReleased

    private void motivo_decesoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_motivo_decesoKeyTyped
        EventoTecladoUtil.convertirAMayusculas(evt);       // TODO add your handling code here:
    }//GEN-LAST:event_motivo_decesoKeyTyped

    private void btnFotoDocumentoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_btnFotoDocumentoFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_btnFotoDocumentoFocusGained

    private void btnFotoDocumentoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_btnFotoDocumentoFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_btnFotoDocumentoFocusLost

    private void btnFotoDocumentoMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnFotoDocumentoMouseEntered
        contener_fotoDocumento.setBackground(new Color(51, 51, 51));          // TODO add your handling code here:
    }//GEN-LAST:event_btnFotoDocumentoMouseEntered

    private void btnFotoDocumentoMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnFotoDocumentoMouseExited
        contener_fotoDocumento.setBackground(new Color(80, 90, 100));  // TODO add your handling code here:
    }//GEN-LAST:event_btnFotoDocumentoMouseExited

    private void btnFotoDocumentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotoDocumentoActionPerformed
        if (contador > 0) {
            String ci = documento_difunto.getText().replaceAll("\\p{Punct}", "");
            FileChooserHandler.cargarArchivo(ci, "DEF", ruta);
        } else {
            JOptionPane.showMessageDialog(null, "Se debera guardar el registro para adjuntar el acta de defunción");
        }
    }//GEN-LAST:event_btnFotoDocumentoActionPerformed

    private void btnFotoServiciosFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_btnFotoServiciosFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_btnFotoServiciosFocusGained

    private void btnFotoServiciosFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_btnFotoServiciosFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_btnFotoServiciosFocusLost

    private void btnFotoServiciosMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnFotoServiciosMouseEntered
        contener_serviciosFoto.setBackground(new Color(51, 51, 51));    // TODO add your handling code here:
    }//GEN-LAST:event_btnFotoServiciosMouseEntered

    private void btnFotoServiciosMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnFotoServiciosMouseExited
        contener_serviciosFoto.setBackground(new Color(80, 90, 100));  // TODO add your handling code here:
    }//GEN-LAST:event_btnFotoServiciosMouseExited

    private void btnFotoServiciosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotoServiciosActionPerformed
        if (contador > 0) {
            String ci = documento_difunto.getText().replaceAll("\\p{Punct}", "");
            FileChooserHandler.cargarArchivo(ci, "CED", ruta);
        } else {
            JOptionPane.showMessageDialog(null, "Se debera guardar el registro para adjuntar el acta de la cedula del Difunto");
        }

    }//GEN-LAST:event_btnFotoServiciosActionPerformed

    private void btnverFotosFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_btnverFotosFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_btnverFotosFocusGained

    private void btnverFotosFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_btnverFotosFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_btnverFotosFocusLost

    private void btnverFotosMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnverFotosMouseEntered
        contener_verFotos.setBackground(new Color(51, 51, 51));        // TODO add your handling code here:
    }//GEN-LAST:event_btnverFotosMouseEntered

    private void btnverFotosMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnverFotosMouseExited
        contener_verFotos.setBackground(new Color(80, 90, 100));        // TODO add your handling code here:
    }//GEN-LAST:event_btnverFotosMouseExited

    private void btnverFotosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnverFotosActionPerformed
        if (contador > 0) {
            String ci = documento_difunto.getText().replaceAll("\\p{Punct}", "");
            File targetDir = new File(ruta + "/" + ci);
            if (targetDir.exists()) {
                FileChooserHandler.verArchivos(ruta + "/" + ci);

            } else {
                JOptionPane.showMessageDialog(null, "No se adjuntaron documentos para este difunto");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Aun no se ha guardado ningun registro para el difunto");
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_btnverFotosActionPerformed

    private void tbl_lote_detalleMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_lote_detalleMouseClicked

    }//GEN-LAST:event_tbl_lote_detalleMouseClicked

    private void loteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_loteActionPerformed

    private void loteKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_loteKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_loteKeyReleased

    private void loteKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_loteKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_loteKeyTyped

    private void titularActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_titularActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_titularActionPerformed

    private void titularKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_titularKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_titularKeyReleased

    private void titularKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_titularKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_titularKeyTyped

    private void alquiler_codFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_alquiler_codFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_alquiler_codFocusLost

    private void alquiler_codFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_alquiler_codFocusGained
        MostrarDetalleLotes();        // TODO add your handling code here:
    }//GEN-LAST:event_alquiler_codFocusGained

    private void jLabel12MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel12MouseClicked
        MostrarDetalleLotes();        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel12MouseClicked

    private void certificadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_certificadoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_certificadoActionPerformed

    private void certificadoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_certificadoKeyTyped
        EventoTecladoUtil.permitirMayusculasYNumeros(evt);         // TODO add your handling code here:
    }//GEN-LAST:event_certificadoKeyTyped

    private void difuntoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_difuntoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_difuntoActionPerformed

    private void difuntoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_difuntoKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_difuntoKeyReleased

    private void difuntoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_difuntoKeyTyped
        EventoTecladoUtil.convertirAMayusculas(evt);         // TODO add your handling code here:
    }//GEN-LAST:event_difuntoKeyTyped

    private void fecha_nacimientoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_fecha_nacimientoFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_fecha_nacimientoFocusLost

    private void lugar_nacimientoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lugar_nacimientoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_lugar_nacimientoActionPerformed

    private void lugar_nacimientoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_lugar_nacimientoKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_lugar_nacimientoKeyReleased

    private void lugar_nacimientoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_lugar_nacimientoKeyTyped
        EventoTecladoUtil.convertirAMayusculas(evt);          // TODO add your handling code here:
    }//GEN-LAST:event_lugar_nacimientoKeyTyped

    private void masculinoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_masculinoActionPerformed
        masculino.setSelected(true);
        femenino.setSelected(false);
    }//GEN-LAST:event_masculinoActionPerformed

    private void femeninoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_femeninoActionPerformed
        masculino.setSelected(false);
        femenino.setSelected(true);
        // TODO add your handling code here:
    }//GEN-LAST:event_femeninoActionPerformed

    private void nacionalidadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nacionalidadActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_nacionalidadActionPerformed

    private void nacionalidadKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_nacionalidadKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_nacionalidadKeyReleased

    private void nacionalidadKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_nacionalidadKeyTyped
        EventoTecladoUtil.convertirAMayusculas(evt);          // TODO add your handling code here:
    }//GEN-LAST:event_nacionalidadKeyTyped

    private void documento_difuntoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_documento_difuntoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_documento_difuntoActionPerformed

    private void documento_difuntoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_documento_difuntoKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_documento_difuntoKeyReleased

    private void documento_difuntoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_documento_difuntoKeyTyped
EventoTecladoUtil.permitirSoloDigitos(evt);        // TODO add your handling code here:
    }//GEN-LAST:event_documento_difuntoKeyTyped

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
ImprimirReporte();
        // TODO add your handling code here:
    }//GEN-LAST:event_btnDetalleActionPerformed

    private void residenciaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_residenciaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_residenciaActionPerformed

    private void residenciaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_residenciaKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_residenciaKeyReleased

    private void residenciaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_residenciaKeyTyped
EventoTecladoUtil.convertirAMayusculas(evt);        // TODO add your handling code here:
    }//GEN-LAST:event_residenciaKeyTyped

    private void edad_difuntoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_edad_difuntoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_edad_difuntoActionPerformed

    private void edad_difuntoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_edad_difuntoKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_edad_difuntoKeyReleased

    private void edad_difuntoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_edad_difuntoKeyTyped
EventoTecladoUtil.permitirSoloDigitos(evt);        // TODO add your handling code here:
    }//GEN-LAST:event_edad_difuntoKeyTyped


    // Variables declaration - do not modify//GEN-BEGIN:variables
    public static javax.swing.JTextField alquiler_cod;
    public static javax.swing.JLabel anulacion;
    public static javax.swing.JLabel anulacion1;
    public static javax.swing.JLabel anulacion2;
    public static javax.swing.JButton btnBuscarCliente;
    public static javax.swing.JButton btnCancelar;
    public static javax.swing.JButton btnDetalle;
    public static javax.swing.JButton btnFotoDocumento;
    public static javax.swing.JButton btnFotoServicios;
    public static javax.swing.JButton btnGuardar;
    public static javax.swing.JButton btnverFotos;
    public static javax.swing.JFormattedTextField celular_titular;
    public static javax.swing.JTextField certificado;
    public static javax.swing.JPanel contener_buscarCliente;
    public static javax.swing.JPanel contener_cancelar;
    public static javax.swing.JPanel contener_detalle;
    public static javax.swing.JPanel contener_fotoDocumento;
    public static javax.swing.JPanel contener_guardar;
    public static javax.swing.JPanel contener_salir;
    public static javax.swing.JPanel contener_serviciosFoto;
    public static javax.swing.JPanel contener_verFotos;
    public static javax.swing.JTextField difunto;
    public static javax.swing.JTextField documento_difunto;
    public static javax.swing.JTextField documento_titular;
    public static javax.swing.JTextField edad_difunto;
    public static com.toedter.calendar.JDateChooser fecha_fallecimiento;
    public static com.toedter.calendar.JDateChooser fecha_nacimiento;
    public static javax.swing.JCheckBox femenino;
    public static javax.swing.JLabel jLabel10;
    public static javax.swing.JLabel jLabel11;
    public static javax.swing.JLabel jLabel12;
    public static javax.swing.JLabel jLabel13;
    public static javax.swing.JLabel jLabel14;
    public static javax.swing.JLabel jLabel15;
    public static javax.swing.JLabel jLabel16;
    public static javax.swing.JLabel jLabel17;
    public static javax.swing.JLabel jLabel18;
    public static javax.swing.JLabel jLabel19;
    public static javax.swing.JLabel jLabel2;
    public static javax.swing.JLabel jLabel20;
    public static javax.swing.JLabel jLabel3;
    public static javax.swing.JLabel jLabel4;
    public static javax.swing.JLabel jLabel5;
    public static javax.swing.JLabel jLabel7;
    public static javax.swing.JScrollPane jScrollPane1;
    public static javax.swing.JTextField lote;
    public static javax.swing.JTextField lugar_nacimiento;
    public static javax.swing.JCheckBox masculino;
    public static javax.swing.JTextField motivo_deceso;
    public static javax.swing.JTextField nacionalidad;
    public static javax.swing.JPanel panelFoto;
    public static javax.swing.JPanel panelModalCliente;
    public static javax.swing.JTextField residencia;
    public static javax.swing.JButton salir;
    public static javax.swing.JTable tbl_lote_detalle;
    public static javax.swing.JTextField titular;
    // End of variables declaration//GEN-END:variables
}
