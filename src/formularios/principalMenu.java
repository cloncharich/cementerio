package formularios;

import disenho.Fondo;
import clases.VentanasPermisosUsuario;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.beans.PropertyVetoException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

/**
 *
 * @author Claudio Loncharich
 */
public final class principalMenu extends javax.swing.JFrame {

    public static String host = "";
    public static String usuarioBase = "";
    public static String contrasena = "";
    public InputStream foto1 = this.getClass().getResourceAsStream("/imagenes/fondomenu.png");
    private static List<String> ventanasNombre = new ArrayList<>();

    public principalMenu(String hostBase, String userBase, String contrasenaBase) {
        initComponents();
        setExtendedState(MAXIMIZED_BOTH);
        cargarImagen(escritorio, foto1);
        //setIconImage(new ImageIcon(getClass().getResource("/IMAGENES/marco_logo2.png")).getImage());
        UIManager UI = new UIManager();
        UIManager.put("control", new Color(255, 255, 255));
        UI.put("OptionPane.background", new Color(255, 255, 255));
        UIManager.put("nimbusBase", new Color(255, 255, 255));
        UIManager.put("InternalFrame.titleFont", new Font("System", Font.PLAIN, 14));
        UIManager.put("OptionPane.messageFont", new Font("System", Font.PLAIN, 14));
        UIManager.put("OptionPane.buttonFont", new Font("System", Font.PLAIN, 14));
        host = hostBase;
        usuarioBase = userBase;
        contrasena = contrasenaBase;
        usuario.setText(" Usuario: <<" + usuarioBase + ">> Version 1.0.0 Sistema de Gestión para Cementerio");
        mostrarPaneles();

    }

    private principalMenu() {
    }

    public void confirmarSalida() {
        int valor = JOptionPane.showConfirmDialog(this, "Esta seguro de salir del sistema?", "Aviso!!", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (valor == JOptionPane.YES_NO_OPTION) {
            System.exit(0);
        }
    }

    public void cargarImagen(javax.swing.JDesktopPane JDeskp, InputStream fileImagen) {
        try {
            BufferedImage image = ImageIO.read(fileImagen);
            JDeskp.setBorder(new Fondo(image));

        } catch (Exception e) {
            System.out.println("Imagen no disponible");
        }
    }

    public static void abrirVentana(Class<?> claseVentana, String valor) {
        try {
            // Crear una instancia de la clase de la ventana especificada
            Object instanciaVentana = claseVentana.getDeclaredConstructor().newInstance();
            // Verificar si la instancia es una subclase de javax.swing.JInternalFrame
            if (instanciaVentana instanceof javax.swing.JInternalFrame) {
                javax.swing.JInternalFrame ventana = (javax.swing.JInternalFrame) instanciaVentana;
                escritorio.add(ventana);
                if (valor.equals("internal")) {

                    Dimension desktopSize = principalMenu.escritorio.getSize();
                    Dimension FrameSize = ventana.getSize();
                    ventana.setLocation((desktopSize.width - FrameSize.width) / 2, (desktopSize.height - FrameSize.height) / 6);
                    ventana.show();
                } else {
                    ventana.setVisible(true);
                    ventana.setMaximum(true);
                }
            } else {
                System.err.println("La clase especificada no es un subtipo de JInternalFrame.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.err.println("Error al abrir la ventana: " + ex.getMessage());
        }
    }

    public static void cerrarInternal() {
        // cierra todos los frames abiertos
        JInternalFrame[] activos = escritorio.getAllFrames();
        for (JInternalFrame frame : activos) {
            frame.dispose();
        }
    }
    
    
     void mostrarPaneles(){
        cerrarInternal();
        try {
            modalPaneles c = new modalPaneles();
            c.dispose();
            escritorio.add(c);
            Dimension desktopSize = principalMenu.escritorio.getSize();
            Dimension FrameSize = c.getSize();
            c.setLocation((desktopSize.width - FrameSize.width) / 2, (desktopSize.height - FrameSize.height) / 2);
            c.show();
            c.setMaximum(true);

        } catch (PropertyVetoException ex) {
            Logger.getLogger(principalMenu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel3 = new javax.swing.JPanel();
        escritorio = new javax.swing.JDesktopPane();
        jPanel2 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        contener_tipo_producto9 = new javax.swing.JPanel();
        clientes9 = new javax.swing.JButton();
        contener_cliente = new javax.swing.JPanel();
        btnCliente = new javax.swing.JButton();
        contener_manzana = new javax.swing.JPanel();
        btnManzana = new javax.swing.JButton();
        contener_tipo_producto7 = new javax.swing.JPanel();
        clientes7 = new javax.swing.JButton();
        contener_alquiler = new javax.swing.JPanel();
        btnAlquiler = new javax.swing.JButton();
        contener_registro = new javax.swing.JPanel();
        btnRegistro = new javax.swing.JButton();
        contener_tipo_producto5 = new javax.swing.JPanel();
        clientes5 = new javax.swing.JButton();
        contener_tipo_producto10 = new javax.swing.JPanel();
        clientes10 = new javax.swing.JButton();
        contener_tipo_producto8 = new javax.swing.JPanel();
        clientes8 = new javax.swing.JButton();
        contener_referencial2 = new javax.swing.JPanel();
        referencial2 = new javax.swing.JButton();
        contener_lote = new javax.swing.JPanel();
        btnLote = new javax.swing.JButton();
        contener_tipo_producto6 = new javax.swing.JPanel();
        clientes6 = new javax.swing.JButton();
        contener_difunto = new javax.swing.JPanel();
        difunto = new javax.swing.JButton();
        contener_referencial1 = new javax.swing.JPanel();
        referencial1 = new javax.swing.JButton();
        contener_mora = new javax.swing.JPanel();
        btnMora = new javax.swing.JButton();
        contener_ventaLote = new javax.swing.JPanel();
        btnVentaLote = new javax.swing.JButton();
        usuario = new javax.swing.JLabel();
        id_usu = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        contener_minimizar = new javax.swing.JPanel();
        mini = new javax.swing.JButton();
        confirmar_cerrar = new javax.swing.JPanel();
        salir = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));
        setLocationByPlatform(true);
        setUndecorated(true);
        setResizable(false);

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));

        escritorio.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout escritorioLayout = new javax.swing.GroupLayout(escritorio);
        escritorio.setLayout(escritorioLayout);
        escritorioLayout.setHorizontalGroup(
            escritorioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 479, Short.MAX_VALUE)
        );
        escritorioLayout.setVerticalGroup(
            escritorioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jPanel2.setBackground(new java.awt.Color(80, 90, 100));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/altech.png"))); // NOI18N
        jLabel4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel4MouseClicked(evt);
            }
        });

        jLabel1.setBackground(new java.awt.Color(80, 63, 65));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/cementerioLogo.png"))); // NOI18N

        contener_tipo_producto9.setBackground(new java.awt.Color(80, 90, 100));

        clientes9.setBackground(new java.awt.Color(0, 153, 153));
        clientes9.setFont(new java.awt.Font("Roboto Light", 1, 12)); // NOI18N
        clientes9.setForeground(new java.awt.Color(255, 255, 255));
        clientes9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/registros.png"))); // NOI18N
        clientes9.setText("Gestionar Usuarios");
        clientes9.setContentAreaFilled(false);
        clientes9.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        clientes9.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        clientes9.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        clientes9.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                clientes9MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                clientes9MouseExited(evt);
            }
        });
        clientes9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clientes9ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout contener_tipo_producto9Layout = new javax.swing.GroupLayout(contener_tipo_producto9);
        contener_tipo_producto9.setLayout(contener_tipo_producto9Layout);
        contener_tipo_producto9Layout.setHorizontalGroup(
            contener_tipo_producto9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(clientes9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        contener_tipo_producto9Layout.setVerticalGroup(
            contener_tipo_producto9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(clientes9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        contener_cliente.setBackground(new java.awt.Color(80, 90, 100));

        btnCliente.setBackground(new java.awt.Color(0, 153, 153));
        btnCliente.setFont(new java.awt.Font("Roboto Light", 1, 12)); // NOI18N
        btnCliente.setForeground(new java.awt.Color(255, 255, 255));
        btnCliente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/registros.png"))); // NOI18N
        btnCliente.setText("Cargar Titular");
        btnCliente.setContentAreaFilled(false);
        btnCliente.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCliente.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnCliente.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnCliente.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnClienteMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnClienteMouseExited(evt);
            }
        });
        btnCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClienteActionPerformed(evt);
            }
        });

        contener_manzana.setBackground(new java.awt.Color(80, 90, 100));

        btnManzana.setBackground(new java.awt.Color(0, 153, 153));
        btnManzana.setFont(new java.awt.Font("Roboto Light", 1, 12)); // NOI18N
        btnManzana.setForeground(new java.awt.Color(255, 255, 255));
        btnManzana.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/manzanas.png"))); // NOI18N
        btnManzana.setText("Cargar Manzanas");
        btnManzana.setContentAreaFilled(false);
        btnManzana.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnManzana.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnManzana.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnManzana.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnManzanaMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnManzanaMouseExited(evt);
            }
        });
        btnManzana.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnManzanaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout contener_manzanaLayout = new javax.swing.GroupLayout(contener_manzana);
        contener_manzana.setLayout(contener_manzanaLayout);
        contener_manzanaLayout.setHorizontalGroup(
            contener_manzanaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnManzana, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        contener_manzanaLayout.setVerticalGroup(
            contener_manzanaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnManzana, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout contener_clienteLayout = new javax.swing.GroupLayout(contener_cliente);
        contener_cliente.setLayout(contener_clienteLayout);
        contener_clienteLayout.setHorizontalGroup(
            contener_clienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnCliente, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(contener_manzana, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        contener_clienteLayout.setVerticalGroup(
            contener_clienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(contener_clienteLayout.createSequentialGroup()
                .addComponent(btnCliente, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(contener_manzana, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        contener_tipo_producto7.setBackground(new java.awt.Color(80, 90, 100));

        clientes7.setBackground(new java.awt.Color(0, 153, 153));
        clientes7.setFont(new java.awt.Font("Roboto Light", 1, 12)); // NOI18N
        clientes7.setForeground(new java.awt.Color(255, 255, 255));
        clientes7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/4.png"))); // NOI18N
        clientes7.setText("Reporte de Graficos");
        clientes7.setContentAreaFilled(false);
        clientes7.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        clientes7.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        clientes7.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        clientes7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                clientes7MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                clientes7MouseExited(evt);
            }
        });
        clientes7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clientes7ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout contener_tipo_producto7Layout = new javax.swing.GroupLayout(contener_tipo_producto7);
        contener_tipo_producto7.setLayout(contener_tipo_producto7Layout);
        contener_tipo_producto7Layout.setHorizontalGroup(
            contener_tipo_producto7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(clientes7, javax.swing.GroupLayout.DEFAULT_SIZE, 186, Short.MAX_VALUE)
        );
        contener_tipo_producto7Layout.setVerticalGroup(
            contener_tipo_producto7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(clientes7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        contener_alquiler.setBackground(new java.awt.Color(80, 90, 100));

        btnAlquiler.setBackground(new java.awt.Color(0, 153, 153));
        btnAlquiler.setFont(new java.awt.Font("Roboto Light", 1, 12)); // NOI18N
        btnAlquiler.setForeground(new java.awt.Color(255, 255, 255));
        btnAlquiler.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/7.png"))); // NOI18N
        btnAlquiler.setText("Alquileres de Lotes");
        btnAlquiler.setContentAreaFilled(false);
        btnAlquiler.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAlquiler.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnAlquiler.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnAlquiler.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnAlquilerMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnAlquilerMouseExited(evt);
            }
        });
        btnAlquiler.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAlquilerActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout contener_alquilerLayout = new javax.swing.GroupLayout(contener_alquiler);
        contener_alquiler.setLayout(contener_alquilerLayout);
        contener_alquilerLayout.setHorizontalGroup(
            contener_alquilerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnAlquiler, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        contener_alquilerLayout.setVerticalGroup(
            contener_alquilerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(contener_alquilerLayout.createSequentialGroup()
                .addComponent(btnAlquiler, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(93, 93, 93))
        );

        contener_registro.setBackground(new java.awt.Color(80, 90, 100));

        btnRegistro.setBackground(new java.awt.Color(255, 255, 255));
        btnRegistro.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        btnRegistro.setForeground(new java.awt.Color(255, 255, 255));
        btnRegistro.setText("REGISTROS");
        btnRegistro.setContentAreaFilled(false);
        btnRegistro.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnRegistro.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnRegistro.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnRegistro.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnRegistroMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnRegistroMouseExited(evt);
            }
        });
        btnRegistro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegistroActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout contener_registroLayout = new javax.swing.GroupLayout(contener_registro);
        contener_registro.setLayout(contener_registroLayout);
        contener_registroLayout.setHorizontalGroup(
            contener_registroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnRegistro, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        contener_registroLayout.setVerticalGroup(
            contener_registroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(contener_registroLayout.createSequentialGroup()
                .addComponent(btnRegistro)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        contener_tipo_producto5.setBackground(new java.awt.Color(80, 90, 100));

        clientes5.setBackground(new java.awt.Color(0, 153, 153));
        clientes5.setFont(new java.awt.Font("Roboto Light", 1, 12)); // NOI18N
        clientes5.setForeground(new java.awt.Color(255, 255, 255));
        clientes5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/1.png"))); // NOI18N
        clientes5.setText("Imprimir Recibos");
        clientes5.setContentAreaFilled(false);
        clientes5.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        clientes5.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        clientes5.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        clientes5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                clientes5MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                clientes5MouseExited(evt);
            }
        });
        clientes5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clientes5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout contener_tipo_producto5Layout = new javax.swing.GroupLayout(contener_tipo_producto5);
        contener_tipo_producto5.setLayout(contener_tipo_producto5Layout);
        contener_tipo_producto5Layout.setHorizontalGroup(
            contener_tipo_producto5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(clientes5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        contener_tipo_producto5Layout.setVerticalGroup(
            contener_tipo_producto5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(clientes5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        contener_tipo_producto10.setBackground(new java.awt.Color(80, 90, 100));

        clientes10.setBackground(new java.awt.Color(0, 153, 153));
        clientes10.setFont(new java.awt.Font("Roboto Light", 1, 12)); // NOI18N
        clientes10.setForeground(new java.awt.Color(255, 255, 255));
        clientes10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/manzanas.png"))); // NOI18N
        clientes10.setText("Gestionar Roles");
        clientes10.setContentAreaFilled(false);
        clientes10.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        clientes10.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        clientes10.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        clientes10.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                clientes10MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                clientes10MouseExited(evt);
            }
        });
        clientes10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clientes10ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout contener_tipo_producto10Layout = new javax.swing.GroupLayout(contener_tipo_producto10);
        contener_tipo_producto10.setLayout(contener_tipo_producto10Layout);
        contener_tipo_producto10Layout.setHorizontalGroup(
            contener_tipo_producto10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(clientes10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        contener_tipo_producto10Layout.setVerticalGroup(
            contener_tipo_producto10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(clientes10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        contener_tipo_producto8.setBackground(new java.awt.Color(80, 90, 100));

        clientes8.setBackground(new java.awt.Color(0, 153, 153));
        clientes8.setFont(new java.awt.Font("Roboto Light", 1, 12)); // NOI18N
        clientes8.setForeground(new java.awt.Color(255, 255, 255));
        clientes8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/3.png"))); // NOI18N
        clientes8.setText("Imprimir Facturas");
        clientes8.setContentAreaFilled(false);
        clientes8.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        clientes8.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        clientes8.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        clientes8.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                clientes8MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                clientes8MouseExited(evt);
            }
        });
        clientes8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clientes8ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout contener_tipo_producto8Layout = new javax.swing.GroupLayout(contener_tipo_producto8);
        contener_tipo_producto8.setLayout(contener_tipo_producto8Layout);
        contener_tipo_producto8Layout.setHorizontalGroup(
            contener_tipo_producto8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(clientes8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        contener_tipo_producto8Layout.setVerticalGroup(
            contener_tipo_producto8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(clientes8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        contener_referencial2.setBackground(new java.awt.Color(80, 90, 100));

        referencial2.setBackground(new java.awt.Color(255, 255, 255));
        referencial2.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        referencial2.setForeground(new java.awt.Color(255, 255, 255));
        referencial2.setText("SEGURIDAD");
        referencial2.setContentAreaFilled(false);
        referencial2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        referencial2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        referencial2.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        referencial2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                referencial2MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                referencial2MouseExited(evt);
            }
        });
        referencial2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                referencial2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout contener_referencial2Layout = new javax.swing.GroupLayout(contener_referencial2);
        contener_referencial2.setLayout(contener_referencial2Layout);
        contener_referencial2Layout.setHorizontalGroup(
            contener_referencial2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(referencial2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        contener_referencial2Layout.setVerticalGroup(
            contener_referencial2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(contener_referencial2Layout.createSequentialGroup()
                .addComponent(referencial2)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        contener_lote.setBackground(new java.awt.Color(80, 90, 100));

        btnLote.setBackground(new java.awt.Color(0, 153, 153));
        btnLote.setFont(new java.awt.Font("Roboto Light", 1, 12)); // NOI18N
        btnLote.setForeground(new java.awt.Color(255, 255, 255));
        btnLote.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/lote.png"))); // NOI18N
        btnLote.setText("Cargar Lotes");
        btnLote.setContentAreaFilled(false);
        btnLote.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnLote.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnLote.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnLote.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnLoteMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnLoteMouseExited(evt);
            }
        });
        btnLote.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLoteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout contener_loteLayout = new javax.swing.GroupLayout(contener_lote);
        contener_lote.setLayout(contener_loteLayout);
        contener_loteLayout.setHorizontalGroup(
            contener_loteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnLote, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        contener_loteLayout.setVerticalGroup(
            contener_loteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnLote, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        contener_tipo_producto6.setBackground(new java.awt.Color(80, 90, 100));

        clientes6.setBackground(new java.awt.Color(0, 153, 153));
        clientes6.setFont(new java.awt.Font("Roboto Light", 1, 12)); // NOI18N
        clientes6.setForeground(new java.awt.Color(255, 255, 255));
        clientes6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/9.png"))); // NOI18N
        clientes6.setText("Imprimir Balances");
        clientes6.setContentAreaFilled(false);
        clientes6.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        clientes6.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        clientes6.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        clientes6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                clientes6MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                clientes6MouseExited(evt);
            }
        });
        clientes6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clientes6ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout contener_tipo_producto6Layout = new javax.swing.GroupLayout(contener_tipo_producto6);
        contener_tipo_producto6.setLayout(contener_tipo_producto6Layout);
        contener_tipo_producto6Layout.setHorizontalGroup(
            contener_tipo_producto6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(clientes6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        contener_tipo_producto6Layout.setVerticalGroup(
            contener_tipo_producto6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(clientes6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        contener_difunto.setBackground(new java.awt.Color(80, 90, 100));

        difunto.setBackground(new java.awt.Color(0, 153, 153));
        difunto.setFont(new java.awt.Font("Roboto Light", 1, 12)); // NOI18N
        difunto.setForeground(new java.awt.Color(255, 255, 255));
        difunto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/registros.png"))); // NOI18N
        difunto.setText("Difuntos");
        difunto.setContentAreaFilled(false);
        difunto.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        difunto.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        difunto.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        difunto.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                difuntoMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                difuntoMouseExited(evt);
            }
        });
        difunto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                difuntoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout contener_difuntoLayout = new javax.swing.GroupLayout(contener_difunto);
        contener_difunto.setLayout(contener_difuntoLayout);
        contener_difuntoLayout.setHorizontalGroup(
            contener_difuntoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(difunto, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        contener_difuntoLayout.setVerticalGroup(
            contener_difuntoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(contener_difuntoLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(difunto, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        contener_referencial1.setBackground(new java.awt.Color(80, 90, 100));

        referencial1.setBackground(new java.awt.Color(255, 255, 255));
        referencial1.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        referencial1.setForeground(new java.awt.Color(255, 255, 255));
        referencial1.setText("PROCESOS");
        referencial1.setContentAreaFilled(false);
        referencial1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        referencial1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        referencial1.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        referencial1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                referencial1MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                referencial1MouseExited(evt);
            }
        });
        referencial1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                referencial1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout contener_referencial1Layout = new javax.swing.GroupLayout(contener_referencial1);
        contener_referencial1.setLayout(contener_referencial1Layout);
        contener_referencial1Layout.setHorizontalGroup(
            contener_referencial1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(referencial1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        contener_referencial1Layout.setVerticalGroup(
            contener_referencial1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(contener_referencial1Layout.createSequentialGroup()
                .addComponent(referencial1)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        contener_mora.setBackground(new java.awt.Color(80, 90, 100));

        btnMora.setBackground(new java.awt.Color(0, 153, 153));
        btnMora.setFont(new java.awt.Font("Roboto Light", 1, 12)); // NOI18N
        btnMora.setForeground(new java.awt.Color(255, 255, 255));
        btnMora.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/registros.png"))); // NOI18N
        btnMora.setText("Titulares con Mora");
        btnMora.setContentAreaFilled(false);
        btnMora.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnMora.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnMora.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnMora.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnMoraMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnMoraMouseExited(evt);
            }
        });
        btnMora.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMoraActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout contener_moraLayout = new javax.swing.GroupLayout(contener_mora);
        contener_mora.setLayout(contener_moraLayout);
        contener_moraLayout.setHorizontalGroup(
            contener_moraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnMora, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        contener_moraLayout.setVerticalGroup(
            contener_moraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(contener_moraLayout.createSequentialGroup()
                .addComponent(btnMora, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(93, 93, 93))
        );

        contener_ventaLote.setBackground(new java.awt.Color(80, 90, 100));

        btnVentaLote.setBackground(new java.awt.Color(0, 153, 153));
        btnVentaLote.setFont(new java.awt.Font("Roboto Light", 1, 12)); // NOI18N
        btnVentaLote.setForeground(new java.awt.Color(255, 255, 255));
        btnVentaLote.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/7.png"))); // NOI18N
        btnVentaLote.setText("Venta de  Lotes");
        btnVentaLote.setContentAreaFilled(false);
        btnVentaLote.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnVentaLote.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnVentaLote.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnVentaLote.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnVentaLoteMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnVentaLoteMouseExited(evt);
            }
        });
        btnVentaLote.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVentaLoteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout contener_ventaLoteLayout = new javax.swing.GroupLayout(contener_ventaLote);
        contener_ventaLote.setLayout(contener_ventaLoteLayout);
        contener_ventaLoteLayout.setHorizontalGroup(
            contener_ventaLoteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnVentaLote, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        contener_ventaLoteLayout.setVerticalGroup(
            contener_ventaLoteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(contener_ventaLoteLayout.createSequentialGroup()
                .addComponent(btnVentaLote, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(93, 93, 93))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(contener_registro, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(contener_cliente, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(contener_lote, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(contener_alquiler, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(contener_tipo_producto5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(contener_tipo_producto7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(contener_tipo_producto8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(contener_referencial2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(contener_tipo_producto9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(contener_tipo_producto10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(contener_tipo_producto6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(contener_difunto, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(contener_referencial1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(contener_mora, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(contener_ventaLote, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(contener_registro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(contener_cliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(contener_lote, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(contener_alquiler, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(contener_ventaLote, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(contener_mora, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(contener_referencial1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(contener_difunto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(contener_tipo_producto5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(contener_tipo_producto6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(contener_tipo_producto7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(contener_tipo_producto8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(contener_referencial2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(contener_tipo_producto9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(contener_tipo_producto10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, Short.MAX_VALUE)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        usuario.setBackground(new java.awt.Color(153, 204, 255));
        usuario.setFont(new java.awt.Font("Roboto Medium", 0, 12)); // NOI18N
        usuario.setPreferredSize(new java.awt.Dimension(34, 14));

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/altech.png"))); // NOI18N
        jLabel3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel3MouseClicked(evt);
            }
        });

        contener_minimizar.setBackground(new java.awt.Color(255, 255, 255));
        contener_minimizar.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        mini.setBackground(new java.awt.Color(0, 153, 153));
        mini.setFont(new java.awt.Font("Roboto Medium", 0, 18)); // NOI18N
        mini.setText("-");
        mini.setBorder(null);
        mini.setContentAreaFilled(false);
        mini.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        mini.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                miniMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                miniMouseExited(evt);
            }
        });
        mini.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miniActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout contener_minimizarLayout = new javax.swing.GroupLayout(contener_minimizar);
        contener_minimizar.setLayout(contener_minimizarLayout);
        contener_minimizarLayout.setHorizontalGroup(
            contener_minimizarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, contener_minimizarLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(mini, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        contener_minimizarLayout.setVerticalGroup(
            contener_minimizarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mini, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 26, Short.MAX_VALUE)
        );

        confirmar_cerrar.setBackground(new java.awt.Color(255, 255, 255));
        confirmar_cerrar.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        confirmar_cerrar.setPreferredSize(new java.awt.Dimension(40, 28));

        salir.setBackground(new java.awt.Color(255, 255, 255));
        salir.setFont(new java.awt.Font("Roboto Medium", 0, 18)); // NOI18N
        salir.setText("x");
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

        javax.swing.GroupLayout confirmar_cerrarLayout = new javax.swing.GroupLayout(confirmar_cerrar);
        confirmar_cerrar.setLayout(confirmar_cerrarLayout);
        confirmar_cerrarLayout.setHorizontalGroup(
            confirmar_cerrarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
            .addGroup(confirmar_cerrarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(confirmar_cerrarLayout.createSequentialGroup()
                    .addGap(1, 1, 1)
                    .addComponent(salir, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        confirmar_cerrarLayout.setVerticalGroup(
            confirmar_cerrarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
            .addGroup(confirmar_cerrarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(confirmar_cerrarLayout.createSequentialGroup()
                    .addComponent(salir)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(143, 143, 143)
                        .addComponent(id_usu)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(escritorio, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(usuario, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(contener_minimizar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)
                        .addComponent(confirmar_cerrar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1))))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(usuario, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(confirmar_cerrar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(contener_minimizar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(id_usu)
                    .addComponent(escritorio)))
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void miniActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miniActionPerformed
        this.setState(ICONIFIED);        // TODO add your handling code here:
    }//GEN-LAST:event_miniActionPerformed

    private void miniMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_miniMouseEntered
        contener_minimizar.setBackground(new Color(153, 153, 153));         // TODO add your handling code here:
    }//GEN-LAST:event_miniMouseEntered

    private void miniMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_miniMouseExited
        contener_minimizar.setBackground(Color.white);          // TODO add your handling code here:
    }//GEN-LAST:event_miniMouseExited

    private void salirMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_salirMouseEntered
        confirmar_cerrar.setBackground(Color.red);        // TODO add your handling code here:
    }//GEN-LAST:event_salirMouseEntered

    private void salirMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_salirMouseExited
        confirmar_cerrar.setBackground(Color.white);         // TODO add your handling code here:
    }//GEN-LAST:event_salirMouseExited

    private void salirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_salirActionPerformed
        confirmarSalida();        // TODO add your handling code here:
    }//GEN-LAST:event_salirActionPerformed

    private void btnRegistroMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRegistroMouseEntered

    }//GEN-LAST:event_btnRegistroMouseEntered

    private void btnRegistroMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRegistroMouseExited

    }//GEN-LAST:event_btnRegistroMouseExited

    private void btnRegistroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegistroActionPerformed

        // TODO add your handling code here:
    }//GEN-LAST:event_btnRegistroActionPerformed

    private void btnClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClienteActionPerformed
        cerrarInternal();
        ventanasNombre = VentanasPermisosUsuario.obtenerUsuarioVentana();
        if (VentanasPermisosUsuario.permisosVentana("CLIENTES", ventanasNombre)) {
            abrirVentana(tablaClientes.class, "");

        } else {
            abrirVentana(noTienePermiso.class, "");
        }
    }//GEN-LAST:event_btnClienteActionPerformed

    private void btnClienteMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnClienteMouseExited
        contener_cliente.setBackground(new Color(80, 90, 100));        // TODO add your handling code here:
    }//GEN-LAST:event_btnClienteMouseExited

    private void btnClienteMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnClienteMouseEntered
        contener_cliente.setBackground(new Color(51, 51, 51));        // TODO add your handling code here:
    }//GEN-LAST:event_btnClienteMouseEntered

    private void jLabel3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel3MouseClicked
 mostrarPaneles();
    }//GEN-LAST:event_jLabel3MouseClicked

    private void jLabel4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel4MouseClicked
 mostrarPaneles();
    }//GEN-LAST:event_jLabel4MouseClicked

    private void btnManzanaMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnManzanaMouseEntered
        contener_manzana.setBackground(new Color(51, 51, 51));         // TODO add your handling code here:
    }//GEN-LAST:event_btnManzanaMouseEntered

    private void btnManzanaMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnManzanaMouseExited
        contener_manzana.setBackground(new Color(80, 90, 100));         // TODO add your handling code here:
    }//GEN-LAST:event_btnManzanaMouseExited

    private void btnManzanaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnManzanaActionPerformed
        cerrarInternal();
        ventanasNombre = VentanasPermisosUsuario.obtenerUsuarioVentana();
        if (VentanasPermisosUsuario.permisosVentana("MANZANAS", ventanasNombre)) {
            abrirVentana(tablaManzana.class, "");

        } else {
            abrirVentana(noTienePermiso.class, "");
        }        // TODO add your handling code here:
    }//GEN-LAST:event_btnManzanaActionPerformed

    private void btnLoteMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLoteMouseEntered
        contener_lote.setBackground(new Color(51, 51, 51));          // TODO add your handling code here:
    }//GEN-LAST:event_btnLoteMouseEntered

    private void btnLoteMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLoteMouseExited
        contener_lote.setBackground(new Color(80, 90, 100));         // TODO add your handling code here:
    }//GEN-LAST:event_btnLoteMouseExited

    private void btnLoteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoteActionPerformed
        cerrarInternal();
        ventanasNombre = VentanasPermisosUsuario.obtenerUsuarioVentana();
        if (VentanasPermisosUsuario.permisosVentana("LOTES", ventanasNombre)) {
            abrirVentana(tablaLote.class, "");

        } else {
            abrirVentana(noTienePermiso.class, "");
        }        // TODO add your handling code here:
    }//GEN-LAST:event_btnLoteActionPerformed

    private void btnAlquilerMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAlquilerMouseEntered
        contener_alquiler.setBackground(new Color(51, 51, 51));        // TODO add your handling code here:
    }//GEN-LAST:event_btnAlquilerMouseEntered

    private void btnAlquilerMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAlquilerMouseExited
        contener_alquiler.setBackground(new Color(80, 90, 100));        // TODO add your handling code here:
    }//GEN-LAST:event_btnAlquilerMouseExited

    private void btnAlquilerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAlquilerActionPerformed
        cerrarInternal();
        ventanasNombre = VentanasPermisosUsuario.obtenerUsuarioVentana();
        if (VentanasPermisosUsuario.permisosVentana("ALQUILER", ventanasNombre)) {
            abrirVentana(tablaAlquiler.class, "");

        } else {
            abrirVentana(noTienePermiso.class, "");
        }         // TODO add your handling code here:
    }//GEN-LAST:event_btnAlquilerActionPerformed

    private void referencial1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_referencial1MouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_referencial1MouseEntered

    private void referencial1MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_referencial1MouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_referencial1MouseExited

    private void referencial1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_referencial1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_referencial1ActionPerformed

    private void difuntoMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_difuntoMouseEntered
        contener_difunto.setBackground(new Color(51, 51, 51));        // TODO add your handling code here:
    }//GEN-LAST:event_difuntoMouseEntered

    private void difuntoMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_difuntoMouseExited
        contener_difunto.setBackground(new Color(80, 90, 100));        // TODO add your handling code here:
    }//GEN-LAST:event_difuntoMouseExited

    private void difuntoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_difuntoActionPerformed
        cerrarInternal();
        ventanasNombre = VentanasPermisosUsuario.obtenerUsuarioVentana();
        if (VentanasPermisosUsuario.permisosVentana("DIFUNTO", ventanasNombre)) {
            abrirVentana(tablaDifunto.class, "");

        } else {
            abrirVentana(noTienePermiso.class, "");
        }          // TODO add your handling code here:
    }//GEN-LAST:event_difuntoActionPerformed

    private void clientes5MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_clientes5MouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_clientes5MouseEntered

    private void clientes5MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_clientes5MouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_clientes5MouseExited

    private void clientes5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clientes5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_clientes5ActionPerformed

    private void clientes6MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_clientes6MouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_clientes6MouseEntered

    private void clientes6MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_clientes6MouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_clientes6MouseExited

    private void clientes6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clientes6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_clientes6ActionPerformed

    private void clientes7MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_clientes7MouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_clientes7MouseEntered

    private void clientes7MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_clientes7MouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_clientes7MouseExited

    private void clientes7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clientes7ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_clientes7ActionPerformed

    private void clientes8MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_clientes8MouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_clientes8MouseEntered

    private void clientes8MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_clientes8MouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_clientes8MouseExited

    private void clientes8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clientes8ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_clientes8ActionPerformed

    private void referencial2MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_referencial2MouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_referencial2MouseEntered

    private void referencial2MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_referencial2MouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_referencial2MouseExited

    private void referencial2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_referencial2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_referencial2ActionPerformed

    private void clientes9MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_clientes9MouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_clientes9MouseEntered

    private void clientes9MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_clientes9MouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_clientes9MouseExited

    private void clientes9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clientes9ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_clientes9ActionPerformed

    private void clientes10MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_clientes10MouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_clientes10MouseEntered

    private void clientes10MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_clientes10MouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_clientes10MouseExited

    private void clientes10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clientes10ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_clientes10ActionPerformed

    private void btnMoraMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMoraMouseEntered
 contener_mora.setBackground(new Color(51, 51, 51));         // TODO add your handling code here:
    }//GEN-LAST:event_btnMoraMouseEntered

    private void btnMoraMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMoraMouseExited
 contener_mora.setBackground(new Color(80, 90, 100));          // TODO add your handling code here:
    }//GEN-LAST:event_btnMoraMouseExited

    private void btnMoraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMoraActionPerformed
        cerrarInternal();
        ventanasNombre = VentanasPermisosUsuario.obtenerUsuarioVentana();
        if (VentanasPermisosUsuario.permisosVentana("MOROSOS", ventanasNombre)) {
            abrirVentana(tablaMorosos.class, "");

        } else {
            abrirVentana(noTienePermiso.class, "");
        }        // TODO add your handling code here:
    }//GEN-LAST:event_btnMoraActionPerformed

    private void btnVentaLoteMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnVentaLoteMouseEntered
        contener_ventaLote.setBackground(new Color(51, 51, 51));          // TODO add your handling code here:
    }//GEN-LAST:event_btnVentaLoteMouseEntered

    private void btnVentaLoteMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnVentaLoteMouseExited
        contener_ventaLote.setBackground(new Color(80, 90, 100));        // TODO add your handling code here:
    }//GEN-LAST:event_btnVentaLoteMouseExited

    private void btnVentaLoteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVentaLoteActionPerformed
        cerrarInternal();
        ventanasNombre = VentanasPermisosUsuario.obtenerUsuarioVentana();
        if (VentanasPermisosUsuario.permisosVentana("VENTALOTE", ventanasNombre)) {
            abrirVentana(tablaVentaLote.class, "");

        } else {
            abrirVentana(noTienePermiso.class, "");
        }       // TODO add your handling code here:
    }//GEN-LAST:event_btnVentaLoteActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) throws SQLException {
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
            java.util.logging.Logger.getLogger(principalMenu.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(principalMenu.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(principalMenu.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(principalMenu.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {

                new principalMenu().setVisible(true);

            }
        });
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    public static javax.swing.JButton btnAlquiler;
    public static javax.swing.JButton btnCliente;
    public static javax.swing.JButton btnLote;
    public static javax.swing.JButton btnManzana;
    public static javax.swing.JButton btnMora;
    public static javax.swing.JButton btnRegistro;
    public static javax.swing.JButton btnVentaLote;
    public static javax.swing.JButton clientes10;
    public static javax.swing.JButton clientes5;
    public static javax.swing.JButton clientes6;
    public static javax.swing.JButton clientes7;
    public static javax.swing.JButton clientes8;
    public static javax.swing.JButton clientes9;
    public static javax.swing.JPanel confirmar_cerrar;
    public static javax.swing.JPanel contener_alquiler;
    public static javax.swing.JPanel contener_cliente;
    public static javax.swing.JPanel contener_difunto;
    public static javax.swing.JPanel contener_lote;
    public static javax.swing.JPanel contener_manzana;
    public static javax.swing.JPanel contener_minimizar;
    public static javax.swing.JPanel contener_mora;
    public static javax.swing.JPanel contener_referencial1;
    public static javax.swing.JPanel contener_referencial2;
    public static javax.swing.JPanel contener_registro;
    public static javax.swing.JPanel contener_tipo_producto10;
    public static javax.swing.JPanel contener_tipo_producto5;
    public static javax.swing.JPanel contener_tipo_producto6;
    public static javax.swing.JPanel contener_tipo_producto7;
    public static javax.swing.JPanel contener_tipo_producto8;
    public static javax.swing.JPanel contener_tipo_producto9;
    public static javax.swing.JPanel contener_ventaLote;
    public static javax.swing.JButton difunto;
    public static javax.swing.JDesktopPane escritorio;
    public static javax.swing.JLabel id_usu;
    public static javax.swing.JLabel jLabel1;
    public static javax.swing.JLabel jLabel3;
    public static javax.swing.JLabel jLabel4;
    public static javax.swing.JPanel jPanel2;
    public static javax.swing.JPanel jPanel3;
    public static javax.swing.JButton mini;
    public static javax.swing.JButton referencial1;
    public static javax.swing.JButton referencial2;
    public static javax.swing.JButton salir;
    public static javax.swing.JLabel usuario;
    // End of variables declaration//GEN-END:variables
}
