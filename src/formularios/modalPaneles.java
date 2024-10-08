/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package formularios;

import clases.DatabaseConnector;
import clases.Model_Card;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.CategoryItemLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.RingPlot;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

/**
 *
 * @author loncharich
 */
public final class modalPaneles extends javax.swing.JInternalFrame {

    public modalPaneles() {
        initComponents();
        ((javax.swing.plaf.basic.BasicInternalFrameUI) this.getUI()).setNorthPane(null);
        getContentPane().setBackground(Color.WHITE);
        show5SecondlyUpdated3DPieChart();
        mostrarCards();
        add(jPanel1, BorderLayout.SOUTH);
        add(panel, BorderLayout.NORTH);

    }

    public void show5SecondlyUpdated3DPieChart() {
        // Crear un temporizador que ejecute la consulta cada 5 segundos
        Timer timer = new Timer(10000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Invocar la función para mostrar el gráfico
                showPieChart();
                //mostrarCards();
                //updateTableModel();
                showLineChart();
                //showLineChart2();
            }
        });
        timer.start(); // Iniciar el temporizador

        // Llamar a la función una vez al principio
        showPieChart();
        //mostrarCards();
        showLineChart();
    }

    public void showPieChart() {
        DefaultPieDataset dataset = new DefaultPieDataset();

        // Reemplaza "TU_QUERY_AQUI" con tu consulta SQL real
        String sql = "SELECT\n"
                + "    l.estado_registro,\n"
                + "    COUNT(*) AS cantidad_lotes,\n"
                + "    COALESCE(\n"
                + "        STRING_AGG(m.codigo::TEXT || '-' || l.numero_lote::TEXT || '-' || l.serie::TEXT, ', '),\n"
                + "        'NINGUN LOTE'\n"
                + "    ) AS lotes_concatenados\n"
                + "FROM\n"
                + "    lote l\n"
                + "JOIN\n"
                + "    manzana m ON l.cod_manzana = m.cod_manzana\n"
                + "WHERE\n"
                + "    l.estado_registro IN ('V', 'L')\n"
                + "GROUP BY\n"
                + "    l.estado_registro\n"
                + "HAVING\n"
                + "    COUNT(*) > 0\n"
                + "UNION ALL\n"
                + "SELECT\n"
                + "    estados.estado_registro,\n"
                + "    0 AS cantidad_lotes,\n"
                + "    'NINGUN LOTE' AS lotes_concatenados\n"
                + "FROM\n"
                + "    (SELECT 'V' AS estado_registro UNION ALL SELECT 'L') estados\n"
                + "LEFT JOIN\n"
                + "    lote l ON l.estado_registro = estados.estado_registro\n"
                + "GROUP BY\n"
                + "    estados.estado_registro\n"
                + "HAVING\n"
                + "    COUNT(l.numero_lote) = 0;";

        Statement st;
        JFreeChart chart = null;

        try (Connection cn = DatabaseConnector.getConnection()) {
            st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            // Colores diferentes para cada serie
            Color colorLibre = Color.GREEN;
            Color colorVendido = Color.RED;

            while (rs.next()) {
                String estado = rs.getString("estado_registro").equals("L") ? "LIBRE" : "VENDIDO";
                int cantidadLotes = rs.getInt("cantidad_lotes");
                String lotesConcatenados = rs.getString("lotes_concatenados");

                // Concatenar el texto para mostrar la cantidad total y los lotes concatenados
                String etiqueta = estado + ": TOTAL = " + cantidadLotes + " (" + lotesConcatenados + ")";
                dataset.setValue(etiqueta, cantidadLotes);
            }

            // Configuración de la apariencia del gráfico
            chart = ChartFactory.createRingChart("Estado de Lotes Vendidos/Libres", dataset, true, true, false);
            RingPlot ringPlot = (RingPlot) chart.getPlot();

            // Configuración del fondo del gráfico a blanco
            ringPlot.setBackgroundPaint(Color.WHITE);

            // Configuración de colores para cada sección del anillo
            for (int i = 0; i < dataset.getItemCount(); i++) {
                String key = dataset.getKey(i).toString();
                Color color = key.contains("LIBRE") ? colorLibre : colorVendido;
                ringPlot.setSectionPaint(key, color);
            }

            // Configuración de la apariencia de las etiquetas
            ringPlot.setLabelFont(new Font("SansSerif", Font.BOLD, 12));
            ringPlot.setLabelPaint(Color.BLACK);

            // Configuración para desactivar las líneas de conexión entre las etiquetas y las secciones
            ringPlot.setLabelLinksVisible(false);

            // Configuración de las etiquetas para aumentar la transparencia
            ringPlot.setLabelBackgroundPaint(new Color(255, 255, 255, 0)); // Configura el fondo de las etiquetas como transparente
            ringPlot.setLabelShadowPaint(new Color(255, 255, 255, 0)); // Configura la sombra de las etiquetas como transparente

            // Agrega el efecto de rotación
            Rotator rotator = new Rotator(ringPlot);
            rotator.start();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }

        if (chart != null) {
            ChartPanel ringChartPanel = new ChartPanel(chart);
            panelBarChart2.removeAll();
            panelBarChart2.add(ringChartPanel, BorderLayout.CENTER);
            panelBarChart2.validate();
        }
    }

    static class Rotator extends Thread {

        private PiePlot plot;

        public Rotator(PiePlot plot) {
            this.plot = plot;
        }

        public void run() {
            while (true) {
                for (int i = 0; i < 360; i++) {
                    plot.setStartAngle(i);
                    try {
                        Thread.sleep(500); // Ajusta la velocidad de rotación aquí
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    void mostrarCards() {

        String consulta = "SELECT\n"
                + "    total_ventas_mes,\n"
                + "    fecha_rango,\n"
                + "    cantidad_alquileres,\n"
                + "    suma_total_moras\n"
                + "FROM (\n"
                + "    SELECT\n"
                + "        COALESCE(TO_CHAR(SUM(total), 'FM999G999G999G990'), '0') AS total_ventas_mes,\n"
                + "        TO_CHAR(DATE_TRUNC('month', CURRENT_DATE), 'DD/MM/YYYY') || ' al ' || TO_CHAR(CURRENT_DATE, 'DD/MM/YYYY') AS fecha_rango\n"
                + "    FROM\n"
                + "        venta\n"
                + "    WHERE\n"
                + "        EXTRACT(YEAR FROM fecha) = EXTRACT(YEAR FROM CURRENT_DATE)\n"
                + "        AND EXTRACT(MONTH FROM fecha) = EXTRACT(MONTH FROM CURRENT_DATE)\n"
                + "        AND estado_registro = 'A'\n"
                + ") AS ventas\n"
                + "CROSS JOIN (\n"
                + "    WITH alquileres_en_mora AS (\n"
                + "        SELECT\n"
                + "            cod_cabecera\n"
                + "        FROM\n"
                + "            alquiler_lote_cliente where estado_registro='A'\n"
                + "    ),\n"
                + "    moras_detalle AS (\n"
                + "        SELECT\n"
                + "            f.mora,\n"
                + "            f.monto\n"
                + "        FROM\n"
                + "            alquileres_en_mora a\n"
                + "        JOIN\n"
                + "            LATERAL fn_obtener_cuotas_y_mantenimiento(a.cod_cabecera,1000) AS f ON TRUE\n"
                + "        WHERE\n"
                + "            f.mora = 'EN MORA'\n"
                + "    )\n"
                + "    SELECT\n"
                + "        COUNT(*) AS cantidad_alquileres,\n"
                + "        COALESCE(\n"
                + "            TO_CHAR(SUM(monto), 'FM999G999G999G990'),\n"
                + "            '0'\n"
                + "        ) AS suma_total_moras\n"
                + "    FROM\n"
                + "        moras_detalle\n"
                + ") AS moras;";
        Statement st2;
        try (Connection cn2 = DatabaseConnector.getConnection()) {
            st2 = (Statement) cn2.createStatement();
            ResultSet rs2 = (ResultSet) st2.executeQuery(consulta);
            if (rs2.next()) {
                String ven = rs2.getString(1);
                String fec = rs2.getString(2);
                String can = rs2.getString(3);
                String mor = rs2.getString(4);
                card5.setData(new Model_Card(new ImageIcon(getClass().getResource("/imagenes/profit.png")), "Total Cobros", ven + " " + "GUARANIES", "Cobros realizado desde " + fec));
                card4.setData(new Model_Card(new ImageIcon(getClass().getResource("/imagenes/stock_2.png")), "Total Compras", "0 GUARANIES", "Compras realizadas desde " + fec));
                card6.setData(new Model_Card(new ImageIcon(getClass().getResource("/imagenes/flag.png")), "Total Mora", mor + " " + "GUARANIES", "Cantidad de cobros en mora " + can));
                panel.setVisible(true);

            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "" + e);
        }

    }

   public void showLineChart() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        String sql = "SELECT\n"
                + "    c.nombres || ' ' || c.apellidos AS nombre_completo,\n"
                + "    m.codigo || '-' || l.numero_lote || '-' || l.serie AS lote,\n"
                + "    COALESCE(\n"
                + "        SUM(f.monto),\n"
                + "        0\n"
                + "    ) AS suma_deuda\n"
                + "FROM\n"
                + "    alquiler_lote_cliente al\n"
                + "JOIN\n"
                + "    cliente c ON al.cod_cliente = c.cod_cliente\n"
                + "JOIN\n"
                + "    lote l ON al.cod_lote = l.cod_lote\n"
                + "JOIN\n"
                + "    manzana m ON l.cod_manzana = m.cod_manzana\n"
                + "JOIN\n"
                + "    LATERAL fn_obtener_cuotas_y_mantenimiento(al.cod_cabecera,1000) AS f ON f.mora = 'EN MORA'\n"
                + "WHERE\n"
                + "    f.mora = 'EN MORA' AND al.estado_registro = 'A' \n"
                + "GROUP BY\n"
                + "    c.nombres, c.apellidos, m.codigo, l.numero_lote, l.serie\n"
                + "ORDER BY\n"
                + "    nombre_completo, lote;";

        try (Connection cn = DatabaseConnector.getConnection()) {
            try (Statement st = cn.createStatement()) {
                try (ResultSet rs = st.executeQuery(sql)) {
                    NumberFormat numberFormat = NumberFormat.getInstance();
                    while (rs.next()) {
                        String nombreLote = rs.getString(1) + " Lote: " + rs.getString(2);
                        int monto = rs.getInt(3);
                        String montoFormateado = numberFormat.format(monto);
                        dataset.addValue(monto, "Mora Activa", nombreLote);
                    }
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "ERROR, No se encontraron datos");
        }

        JFreeChart lineChart = ChartFactory.createLineChart("Mora Titulares por venta de Lote ", "Titular-Lote", "Monto Deuda",
                dataset, PlotOrientation.VERTICAL, true, true, false);

        CategoryPlot lineCategoryPlot = lineChart.getCategoryPlot();
        lineCategoryPlot.setBackgroundPaint(Color.WHITE);

        LineAndShapeRenderer lineRenderer = (LineAndShapeRenderer) lineCategoryPlot.getRenderer();
        Color lineChartColor = new Color(204, 0, 51);
        lineRenderer.setSeriesPaint(0, lineChartColor);

        // Configuración del formato de etiquetas
        lineRenderer.setBaseItemLabelGenerator(new CustomCategoryItemLabelGenerator());
        lineRenderer.setBaseItemLabelsVisible(true);

        ChartPanel lineChartPanel = new ChartPanel(lineChart);
        jPanel4.removeAll();
        jPanel4.add(lineChartPanel, BorderLayout.CENTER);
        jPanel4.validate();
    }

    public void showLineChart2() {
        //create dataset for the graph
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.setValue(200, "Amount", "january");
        dataset.setValue(150, "Amount", "february");
        dataset.setValue(18, "Amount", "march");
        dataset.setValue(100, "Amount", "april");
        dataset.setValue(80, "Amount", "may");
        dataset.setValue(250, "Amount", "june");

        //create chart
        JFreeChart linechart = ChartFactory.createLineChart("contribution", "monthly", "amount",
                dataset, PlotOrientation.VERTICAL, false, true, false);

        //create plot object
        CategoryPlot lineCategoryPlot = linechart.getCategoryPlot();
        // lineCategoryPlot.setRangeGridlinePaint(Color.BLUE);
        lineCategoryPlot.setBackgroundPaint(Color.white);

        //create render object to change the moficy the line properties like color
        LineAndShapeRenderer lineRenderer = (LineAndShapeRenderer) lineCategoryPlot.getRenderer();
        Color lineChartColor = new Color(204, 0, 51);
        lineRenderer.setSeriesPaint(0, lineChartColor);

        //create chartPanel to display chart(graph)
        ChartPanel lineChartPanel = new ChartPanel(linechart);
        jPanel4.removeAll();
        jPanel4.add(lineChartPanel, BorderLayout.CENTER);
        jPanel4.validate();
    }
    
    
     // Implementación personalizada de CategoryItemLabelGenerator
    private class CustomCategoryItemLabelGenerator implements CategoryItemLabelGenerator {
        private final NumberFormat numberFormat;

        public CustomCategoryItemLabelGenerator() {
            numberFormat = NumberFormat.getInstance();
        }

        @Override
        public String generateLabel(CategoryDataset dataset, int series, int category) {
            Number value = dataset.getValue(series, category);
            return numberFormat.format(value);
        }

        public String generateLabel(CategoryDataset dataset, Comparable rowKey, Comparable columnKey) {
            Number value = dataset.getValue(rowKey, columnKey);
            return numberFormat.format(value);
        }

        @Override
        public String generateRowLabel(CategoryDataset cd, int i) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public String generateColumnLabel(CategoryDataset cd, int i) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        panelBarChart2 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        panel = new javax.swing.JLayeredPane();
        card5 = new clases.Card();
        card6 = new clases.Card();
        card4 = new clases.Card();

        setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        setPreferredSize(new java.awt.Dimension(791, 581));

        jPanel1.setLayout(new java.awt.GridLayout(1, 0));

        panelBarChart2.setPreferredSize(new java.awt.Dimension(379, 177));
        panelBarChart2.setLayout(new java.awt.BorderLayout());
        jPanel1.add(panelBarChart2);

        jPanel4.setLayout(new java.awt.BorderLayout());
        jPanel1.add(jPanel4);

        panel.setLayout(new java.awt.GridLayout(1, 0, 10, 0));

        card5.setColor1(new java.awt.Color(241, 208, 62));
        card5.setColor2(new java.awt.Color(211, 184, 61));
        panel.add(card5);

        card6.setColor1(new java.awt.Color(142, 142, 250));
        card6.setColor2(new java.awt.Color(123, 123, 245));
        panel.add(card6);

        card4.setColor1(new java.awt.Color(183, 123, 247));
        card4.setColor2(new java.awt.Color(167, 94, 236));
        panel.add(card4);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 783, Short.MAX_VALUE)
                    .addComponent(panel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panel, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 322, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    public static clases.Card card4;
    public static clases.Card card5;
    public static clases.Card card6;
    public static javax.swing.JPanel jPanel1;
    public static javax.swing.JPanel jPanel4;
    public static javax.swing.JLayeredPane panel;
    public static javax.swing.JPanel panelBarChart2;
    // End of variables declaration//GEN-END:variables
}
