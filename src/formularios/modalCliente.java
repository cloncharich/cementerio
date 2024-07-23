package formularios;

import clases.DatabaseConnector;
import clases.FileChooserHandler;
import clases.DatabaseManager;
import clases.EventoTecladoUtil;
import clases.PathPrincipal;
import disenho.Formato;
import java.awt.Color;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import javax.swing.JOptionPane;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;

/**
 *
 * @author Claudio Loncharich
 */
public final class modalCliente extends javax.swing.JInternalFrame {

    String ruta = "",accionBoton="insertar";
    int contador=0,cliente=0;

    public modalCliente() {
        initComponents();
        ((javax.swing.plaf.basic.BasicInternalFrameUI) this.getUI()).setNorthPane(null);
        setResizable(false);
        ruta = PathPrincipal.obtenerPathStorage();
        documento_titular.requestFocusInWindow();

        documento_titular.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                Formato.NomenclaturaNumero(documento_titular);
            }
        });

        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentShown(java.awt.event.ComponentEvent evt) {
                formShown(evt);
            }
        });

    }

    private void formShown(java.awt.event.ComponentEvent evt) {
        documento_titular.requestFocusInWindow(); // Establecer el foco en documento_titular
    }

   private void GrabarDatos() {
    // tabla cliente en la base
    try {
        String queryInsert = "INSERT INTO cliente (documento, nombres, apellidos, celular, familiar_relacionado, " +
                             "telefono_familiar, familiar_relacionado_su, telefono_familiar_su, direccion) " +
                             "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        String queryUpdate = "UPDATE cliente SET documento=?, nombres=?, apellidos=?, celular=?, familiar_relacionado=?, " +
                             "telefono_familiar=?, familiar_relacionado_su=?, telefono_familiar_su=?, direccion=? " +
                             "WHERE cod_cliente=?";

        String ci = documento_titular.getText().replaceAll("\\p{Punct}", ""); // campo documento
        String nom = nombre_titular.getText(); // campo nombres
        String ape = apellido_titular.getText(); // campo apellidos
        String cel = celular_titular.getText(); // campo celular
        String faRe = primer_familiar_nombre.getText(); // campo familiar_relacionado
        String teFa = telefono_primer.getText(); // campo telefono_familiar
        String faRe2 = segundo_familiar_nombre.getText(); // campo familiar_relacionado_su
        String teFa2 = telefono_segundo.getText(); // campo telefono_familiar_su
        String dir = direccion_titular.getText(); // campo direccion

        int rowsAffected = 0;
        if ("insertar".equalsIgnoreCase(accionBoton)) {
            rowsAffected = DatabaseManager.insert(queryInsert, ci, nom, ape, cel, faRe, teFa, faRe2, teFa2, dir);
        } else if ("actualizar".equalsIgnoreCase(accionBoton)) {
            // Supongamos que obtienes el ID del cliente de alguna manera (por ejemplo, desde un campo oculto)
            //int idCliente = obtenerIdCliente(); // Método ficticio para obtener el ID del cliente a actualizar
            rowsAffected = DatabaseManager.update(queryUpdate, ci, nom, ape, cel, faRe, teFa, faRe2, teFa2, dir, cliente);
        }

        if (rowsAffected > 0) {
            if ("insertar".equalsIgnoreCase(accionBoton)) {
                        JOptionPane.showMessageDialog(this, "Titular registrado con exito", "AVISO", JOptionPane.PLAIN_MESSAGE, Formato.icono("/imagenes/check.png", 40, 40));
            } else if ("actualizar".equalsIgnoreCase(accionBoton)) {
                        JOptionPane.showMessageDialog(this, "Datos actualizado con exito", "AVISO", JOptionPane.PLAIN_MESSAGE, Formato.icono("/imagenes/check.png", 40, 40));
            }
            Formato.habilitarCampos(getContentPane(), false);
            tablaClientes.mostrarTabla("");
        }
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(modalCliente.this, e.getMessage(), "Error al grabar/actualizar registro", JOptionPane.ERROR_MESSAGE);
    }
}


    public void MostrarDatos(String valor, String opcion,int id,String accion) throws ParseException {
        cliente=id;
        accionBoton=accion;
        String query = "select documento,nombres,apellidos,celular,familiar_relacionado,\n"
                + "telefono_familiar,familiar_relacionado_su,telefono_familiar_su,direccion \n"
                + "from cliente where documento='" + valor + "'";
        try (Connection conn = DatabaseConnector.getConnection();
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery(query)) {

            if (rs.next()) {
                nombre_titular.setText(rs.getString("nombres"));
                double doc = Double.parseDouble(rs.getString("documento"));
                documento_titular.setValue(doc);
                apellido_titular.setText(rs.getString("apellidos"));
                MaskFormatter formatter = new MaskFormatter("(+##) ####-###-###");
                String celular = rs.getString("celular").replaceAll("[^0-9]", "").trim();
                int cantidadNumeros = celular.length();
                if (cantidadNumeros < 13) {
                    celular_titular.setFormatterFactory(new DefaultFormatterFactory(formatter));
                    tercer_extra.setSelected(true);
                    tercer_parag.setSelected(false);
                }
                String primer_tel = rs.getString("telefono_familiar").replaceAll("[^0-9]", "").trim();
                int cantidadPrimer = primer_tel.length();
                if (cantidadPrimer < 13) {
                    telefono_primer.setFormatterFactory(new DefaultFormatterFactory(formatter));
                    primer_extra.setSelected(true);
                    primer_parag.setSelected(false);
                }

                String primer_segun = rs.getString("telefono_familiar_su").replaceAll("[^0-9]", "").trim();
                int cantidadSegun = primer_segun.length();
                if (cantidadSegun < 13) {
                    telefono_segundo.setFormatterFactory(new DefaultFormatterFactory(formatter));
                    segun_extra.setSelected(true);
                    segun_parag.setSelected(false);
                }
                celular_titular.setValue(rs.getString("celular"));
                direccion_titular.setText(rs.getString("direccion"));
                telefono_primer.setValue(rs.getString("telefono_familiar"));
                primer_familiar_nombre.setText(rs.getString("familiar_relacionado"));
                segundo_familiar_nombre.setText(rs.getString("familiar_relacionado_su"));
                telefono_segundo.setValue(rs.getString("telefono_familiar_su"));
                if (opcion.equals("ver")) {
                    Formato.habilitarCampos(getContentPane(), false);
                    btnGuardar.setEnabled(false);
                    btnCancelar.setEnabled(false);
                    btnFotoDocumento.setEnabled(false);
                    btnFotoServicios.setEnabled(false);
                    
                }
                contador = 1;
                

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
        jLabel1 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        apellido_titular = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        segundo_familiar_nombre = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        contener_cancelar = new javax.swing.JPanel();
        btnCancelar = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        primer_familiar_nombre = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        direccion_titular = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        panelFoto = new javax.swing.JPanel();
        contener_fotoDocumento = new javax.swing.JPanel();
        btnFotoDocumento = new javax.swing.JButton();
        contener_serviciosFoto = new javax.swing.JPanel();
        btnFotoServicios = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        contener_verFotos = new javax.swing.JPanel();
        btnverFotos = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        contener_salir = new javax.swing.JPanel();
        salir = new javax.swing.JButton();
        telefono_primer = new javax.swing.JFormattedTextField();
        primer_parag = new javax.swing.JCheckBox();
        primer_extra = new javax.swing.JCheckBox();
        telefono_segundo = new javax.swing.JFormattedTextField();
        segun_parag = new javax.swing.JCheckBox();
        segun_extra = new javax.swing.JCheckBox();
        tercer_parag = new javax.swing.JCheckBox();
        tercer_extra = new javax.swing.JCheckBox();
        celular_titular = new javax.swing.JFormattedTextField();
        nombre_titular = new javax.swing.JTextField();
        documento_titular = new javax.swing.JFormattedTextField();
        contener_guardar = new javax.swing.JPanel();
        btnGuardar = new javax.swing.JButton();

        setBackground(new java.awt.Color(255, 255, 255));
        setBorder(javax.swing.BorderFactory.createEtchedBorder());
        setFrameIcon(null);
        setPreferredSize(new java.awt.Dimension(880, 452));

        panelModalCliente.setBackground(new java.awt.Color(255, 255, 255));
        panelModalCliente.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Roboto Light", 0, 14)); // NOI18N
        jLabel1.setText("Documento del Titular:");
        panelModalCliente.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 170, 34));

        jLabel6.setFont(new java.awt.Font("Roboto Light", 0, 14)); // NOI18N
        jLabel6.setText("Apellidos del Titular:");
        panelModalCliente.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 100, 210, 34));

        apellido_titular.setFont(new java.awt.Font("Roboto Light", 0, 12)); // NOI18N
        apellido_titular.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        apellido_titular.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                apellido_titularActionPerformed(evt);
            }
        });
        apellido_titular.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                apellido_titularKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                apellido_titularKeyTyped(evt);
            }
        });
        panelModalCliente.add(apellido_titular, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 130, 310, 32));

        jLabel7.setFont(new java.awt.Font("Roboto Light", 0, 14)); // NOI18N
        jLabel7.setText("Segundo Familiar Relacionado:");
        panelModalCliente.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 280, 220, 34));

        segundo_familiar_nombre.setFont(new java.awt.Font("Roboto Light", 0, 12)); // NOI18N
        segundo_familiar_nombre.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        segundo_familiar_nombre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                segundo_familiar_nombreActionPerformed(evt);
            }
        });
        segundo_familiar_nombre.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                segundo_familiar_nombreKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                segundo_familiar_nombreKeyTyped(evt);
            }
        });
        panelModalCliente.add(segundo_familiar_nombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 310, 310, 32));

        jLabel8.setFont(new java.awt.Font("Roboto Light", 0, 14)); // NOI18N
        jLabel8.setText("Celular del Titular:");
        panelModalCliente.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 100, 120, 34));

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

        panelModalCliente.add(contener_cancelar, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 380, 110, 34));

        jLabel9.setFont(new java.awt.Font("Roboto Light", 0, 14)); // NOI18N
        jLabel9.setText("Telefono Primer Familiar:");
        panelModalCliente.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 220, 170, 34));

        jLabel10.setFont(new java.awt.Font("Roboto Light", 0, 14)); // NOI18N
        jLabel10.setText("Primer Familiar Relacionado:");
        panelModalCliente.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 220, 220, 34));

        primer_familiar_nombre.setFont(new java.awt.Font("Roboto Light", 0, 12)); // NOI18N
        primer_familiar_nombre.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        primer_familiar_nombre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                primer_familiar_nombreActionPerformed(evt);
            }
        });
        primer_familiar_nombre.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                primer_familiar_nombreKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                primer_familiar_nombreKeyTyped(evt);
            }
        });
        panelModalCliente.add(primer_familiar_nombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 250, 310, 32));

        jLabel11.setFont(new java.awt.Font("Roboto Light", 0, 14)); // NOI18N
        jLabel11.setText("Telefono Segundo Familiar:");
        panelModalCliente.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 280, 170, 34));

        direccion_titular.setFont(new java.awt.Font("Roboto Light", 0, 12)); // NOI18N
        direccion_titular.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        direccion_titular.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                direccion_titularActionPerformed(evt);
            }
        });
        direccion_titular.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                direccion_titularKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                direccion_titularKeyTyped(evt);
            }
        });
        panelModalCliente.add(direccion_titular, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 190, 640, 32));

        jLabel12.setFont(new java.awt.Font("Roboto Light", 0, 14)); // NOI18N
        jLabel12.setText("Direccion del Titular:");
        panelModalCliente.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 160, 170, 34));

        panelFoto.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        contener_fotoDocumento.setBackground(new java.awt.Color(80, 90, 100));

        btnFotoDocumento.setBackground(new java.awt.Color(153, 204, 255));
        btnFotoDocumento.setFont(new java.awt.Font("Roboto Medium", 1, 12)); // NOI18N
        btnFotoDocumento.setForeground(new java.awt.Color(255, 255, 255));
        btnFotoDocumento.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/camera.png"))); // NOI18N
        btnFotoDocumento.setText("Subir Foto Documento");
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

        contener_serviciosFoto.setBackground(new java.awt.Color(80, 90, 100));

        btnFotoServicios.setBackground(new java.awt.Color(153, 204, 255));
        btnFotoServicios.setFont(new java.awt.Font("Roboto Medium", 1, 12)); // NOI18N
        btnFotoServicios.setForeground(new java.awt.Color(255, 255, 255));
        btnFotoServicios.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/camera.png"))); // NOI18N
        btnFotoServicios.setText(" Subir Fotos Servicios");
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

        jLabel2.setFont(new java.awt.Font("Roboto Light", 1, 14)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Adjuntar Documentos");

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

        javax.swing.GroupLayout panelFotoLayout = new javax.swing.GroupLayout(panelFoto);
        panelFoto.setLayout(panelFotoLayout);
        panelFotoLayout.setHorizontalGroup(
            panelFotoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(panelFotoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelFotoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelFotoLayout.createSequentialGroup()
                        .addGroup(panelFotoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(contener_serviciosFoto, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(contener_fotoDocumento, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(contener_verFotos, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );
        panelFotoLayout.setVerticalGroup(
            panelFotoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelFotoLayout.createSequentialGroup()
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31)
                .addComponent(contener_fotoDocumento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(contener_serviciosFoto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(contener_verFotos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32))
        );

        panelModalCliente.add(panelFoto, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 70, -1, 270));

        jLabel3.setFont(new java.awt.Font("Roboto Light", 0, 14)); // NOI18N
        jLabel3.setText("Nombres del Titular:");
        panelModalCliente.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 40, 130, 34));

        jLabel4.setFont(new java.awt.Font("Roboto Medium", 0, 18)); // NOI18N
        jLabel4.setText("PERFIL DEL TITULAR");
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

        panelModalCliente.add(contener_salir, new org.netbeans.lib.awtextra.AbsoluteConstraints(840, 0, -1, -1));

        telefono_primer.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        try {
            telefono_primer.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("(+###) ####-###-###")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        telefono_primer.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                telefono_primerFocusGained(evt);
            }
        });
        panelModalCliente.add(telefono_primer, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 250, 320, 32));

        primer_parag.setSelected(true);
        primer_parag.setText("Parag.");
        primer_parag.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                primer_paragActionPerformed(evt);
            }
        });
        panelModalCliente.add(primer_parag, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 220, 70, 32));

        primer_extra.setText("Extra.");
        primer_extra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                primer_extraActionPerformed(evt);
            }
        });
        panelModalCliente.add(primer_extra, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 220, 70, 32));

        telefono_segundo.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        try {
            telefono_segundo.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("(+###) ####-###-###")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        telefono_segundo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                telefono_segundoFocusGained(evt);
            }
        });
        panelModalCliente.add(telefono_segundo, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 310, 320, 32));

        segun_parag.setSelected(true);
        segun_parag.setText("Parag.");
        segun_parag.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                segun_paragActionPerformed(evt);
            }
        });
        panelModalCliente.add(segun_parag, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 280, 70, 32));

        segun_extra.setText("Extra.");
        segun_extra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                segun_extraActionPerformed(evt);
            }
        });
        panelModalCliente.add(segun_extra, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 280, 70, 32));

        tercer_parag.setSelected(true);
        tercer_parag.setText("Parag.");
        tercer_parag.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tercer_paragActionPerformed(evt);
            }
        });
        panelModalCliente.add(tercer_parag, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 100, 70, 32));

        tercer_extra.setText("Extra.");
        tercer_extra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tercer_extraActionPerformed(evt);
            }
        });
        panelModalCliente.add(tercer_extra, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 100, 70, 32));

        celular_titular.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        try {
            celular_titular.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("(+###) ####-###-###")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        celular_titular.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                celular_titularFocusGained(evt);
            }
        });
        panelModalCliente.add(celular_titular, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 130, 320, 32));

        nombre_titular.setFont(new java.awt.Font("Roboto Light", 0, 12)); // NOI18N
        nombre_titular.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        nombre_titular.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nombre_titularActionPerformed(evt);
            }
        });
        nombre_titular.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                nombre_titularKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                nombre_titularKeyTyped(evt);
            }
        });
        panelModalCliente.add(nombre_titular, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 70, 320, 32));

        documento_titular.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        documento_titular.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(java.text.NumberFormat.getIntegerInstance())));
        documento_titular.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                documento_titularFocusGained(evt);
            }
        });
        documento_titular.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                documento_titularKeyTyped(evt);
            }
        });
        panelModalCliente.add(documento_titular, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, 310, 32));

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

        panelModalCliente.add(contener_guardar, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 380, 110, -1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(panelModalCliente, javax.swing.GroupLayout.DEFAULT_SIZE, 876, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelModalCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 430, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void apellido_titularActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_apellido_titularActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_apellido_titularActionPerformed

    private void apellido_titularKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_apellido_titularKeyReleased

    }//GEN-LAST:event_apellido_titularKeyReleased

    private void segundo_familiar_nombreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_segundo_familiar_nombreActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_segundo_familiar_nombreActionPerformed

    private void segundo_familiar_nombreKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_segundo_familiar_nombreKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_segundo_familiar_nombreKeyReleased

    private void apellido_titularKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_apellido_titularKeyTyped
        EventoTecladoUtil.convertirAMayusculas(evt);       // TODO add your handling code here:
    }//GEN-LAST:event_apellido_titularKeyTyped

    private void segundo_familiar_nombreKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_segundo_familiar_nombreKeyTyped
        EventoTecladoUtil.convertirAMayusculas(evt);        // TODO add your handling code here:
    }//GEN-LAST:event_segundo_familiar_nombreKeyTyped

    private void btnCancelarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancelarMouseEntered
        contener_cancelar.setBackground(new Color(51, 51, 51));            // TODO add your handling code here:
    }//GEN-LAST:event_btnCancelarMouseEntered

    private void btnCancelarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancelarMouseExited
        contener_cancelar.setBackground(new Color(80, 90, 100));       // TODO add your handling code here:
    }//GEN-LAST:event_btnCancelarMouseExited

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        Formato.limpiarCampos(getContentPane());
        Formato.habilitarCampos(getContentPane(), true);
        contador = 0;
        documento_titular.requestFocus();// TODO add your handling code here:
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
        contador = 0;
        dispose();        // TODO add your handling code here:
    }//GEN-LAST:event_salirActionPerformed

    private void primer_familiar_nombreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_primer_familiar_nombreActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_primer_familiar_nombreActionPerformed

    private void primer_familiar_nombreKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_primer_familiar_nombreKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_primer_familiar_nombreKeyReleased

    private void primer_familiar_nombreKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_primer_familiar_nombreKeyTyped
        EventoTecladoUtil.convertirAMayusculas(evt);         // TODO add your handling code here:
    }//GEN-LAST:event_primer_familiar_nombreKeyTyped

    private void direccion_titularActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_direccion_titularActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_direccion_titularActionPerformed

    private void direccion_titularKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_direccion_titularKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_direccion_titularKeyReleased

    private void direccion_titularKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_direccion_titularKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_direccion_titularKeyTyped

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
            String ci = documento_titular.getText().replaceAll("\\p{Punct}", "");
            FileChooserHandler.cargarArchivo(ci, "DOC", ruta);
        } else {
            JOptionPane.showMessageDialog(null, "Se debera guardar el registro para adjuntar el documento del titular");
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
        /*String ci = documento_titular.getText().replaceAll("\\p{Punct}", "");
        FileChooserHandler.verArchivos(ruta + "/" + ci);*/
        if (contador > 0) {
            String ci = documento_titular.getText().replaceAll("\\p{Punct}", "");
            FileChooserHandler.cargarArchivo(ci, "SER", ruta);
        } else {
            JOptionPane.showMessageDialog(null, "Se debera guardar el registro para adjuntar la boleta del servicio del titular");
        }

    }//GEN-LAST:event_btnFotoServiciosActionPerformed

    private void primer_paragActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_primer_paragActionPerformed
        primer_parag.setSelected(true);
        primer_extra.setSelected(false); // TODO add your handling code here:
        Formato.NomenclaturaTelefono(primer_parag, primer_extra, telefono_primer);
    }//GEN-LAST:event_primer_paragActionPerformed

    private void primer_extraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_primer_extraActionPerformed
        primer_extra.setSelected(true);
        primer_parag.setSelected(false);
        Formato.NomenclaturaTelefono(primer_parag, primer_extra, telefono_primer);
        // TODO add your handling code here:
    }//GEN-LAST:event_primer_extraActionPerformed

    private void segun_paragActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_segun_paragActionPerformed
        segun_parag.setSelected(true);
        segun_extra.setSelected(false);
        Formato.NomenclaturaTelefono(segun_parag, segun_extra, telefono_segundo);         // TODO add your handling code here:
    }//GEN-LAST:event_segun_paragActionPerformed

    private void segun_extraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_segun_extraActionPerformed
        segun_extra.setSelected(true);
        segun_parag.setSelected(false);
        Formato.NomenclaturaTelefono(segun_parag, segun_extra, telefono_segundo);         // TODO add your handling code here:
    }//GEN-LAST:event_segun_extraActionPerformed

    private void telefono_primerFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_telefono_primerFocusGained

    }//GEN-LAST:event_telefono_primerFocusGained

    private void telefono_segundoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_telefono_segundoFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_telefono_segundoFocusGained

    private void tercer_paragActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tercer_paragActionPerformed
        tercer_parag.setSelected(true);
        tercer_extra.setSelected(false);
        Formato.NomenclaturaTelefono(tercer_parag, tercer_extra, celular_titular);// TODO add your handling code here:
    }//GEN-LAST:event_tercer_paragActionPerformed

    private void tercer_extraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tercer_extraActionPerformed
        tercer_extra.setSelected(true);
        tercer_parag.setSelected(false);
        Formato.NomenclaturaTelefono(tercer_parag, tercer_extra, celular_titular);// TODO add your handling code here:
    }//GEN-LAST:event_tercer_extraActionPerformed

    private void celular_titularFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_celular_titularFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_celular_titularFocusGained

    private void nombre_titularActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nombre_titularActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_nombre_titularActionPerformed

    private void nombre_titularKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_nombre_titularKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_nombre_titularKeyReleased

    private void nombre_titularKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_nombre_titularKeyTyped
        EventoTecladoUtil.convertirAMayusculas(evt);        // TODO add your handling code here:
    }//GEN-LAST:event_nombre_titularKeyTyped

    private void documento_titularFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_documento_titularFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_documento_titularFocusGained

    private void documento_titularKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_documento_titularKeyTyped

    }//GEN-LAST:event_documento_titularKeyTyped

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
            String ci = documento_titular.getText().replaceAll("\\p{Punct}", "");
            File targetDir = new File(ruta + "/" + ci);
            if (targetDir.exists()) {
                FileChooserHandler.verArchivos(ruta + "/" + ci);

            } else {
                JOptionPane.showMessageDialog(null, "No se adjuntaron documentos para este titular");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Aun no se ha guardado ningun registro para el titular");
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_btnverFotosActionPerformed

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
    public static javax.swing.JTextField apellido_titular;
    public static javax.swing.JButton btnCancelar;
    public static javax.swing.JButton btnFotoDocumento;
    public static javax.swing.JButton btnFotoServicios;
    public static javax.swing.JButton btnGuardar;
    public static javax.swing.JButton btnverFotos;
    public static javax.swing.JFormattedTextField celular_titular;
    public static javax.swing.JPanel contener_cancelar;
    public static javax.swing.JPanel contener_fotoDocumento;
    public static javax.swing.JPanel contener_guardar;
    public static javax.swing.JPanel contener_salir;
    public static javax.swing.JPanel contener_serviciosFoto;
    public static javax.swing.JPanel contener_verFotos;
    public static javax.swing.JTextField direccion_titular;
    public static javax.swing.JFormattedTextField documento_titular;
    public static javax.swing.JLabel jLabel1;
    public static javax.swing.JLabel jLabel10;
    public static javax.swing.JLabel jLabel11;
    public static javax.swing.JLabel jLabel12;
    public static javax.swing.JLabel jLabel2;
    public static javax.swing.JLabel jLabel3;
    public static javax.swing.JLabel jLabel4;
    public static javax.swing.JLabel jLabel6;
    public static javax.swing.JLabel jLabel7;
    public static javax.swing.JLabel jLabel8;
    public static javax.swing.JLabel jLabel9;
    public static javax.swing.JTextField nombre_titular;
    public static javax.swing.JPanel panelFoto;
    public static javax.swing.JPanel panelModalCliente;
    public static javax.swing.JCheckBox primer_extra;
    public static javax.swing.JTextField primer_familiar_nombre;
    public static javax.swing.JCheckBox primer_parag;
    public static javax.swing.JButton salir;
    public static javax.swing.JCheckBox segun_extra;
    public static javax.swing.JCheckBox segun_parag;
    public static javax.swing.JTextField segundo_familiar_nombre;
    public static javax.swing.JFormattedTextField telefono_primer;
    public static javax.swing.JFormattedTextField telefono_segundo;
    public static javax.swing.JCheckBox tercer_extra;
    public static javax.swing.JCheckBox tercer_parag;
    // End of variables declaration//GEN-END:variables
}
