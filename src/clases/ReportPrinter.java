/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clases;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author Claudio Loncharich
 */
public class ReportPrinter {

    public void printReport(String reportPath, Map<String, Object> parameters) {
        try (Connection connection = DatabaseConnector.getConnection()) {
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(getClass().getResource(reportPath));
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connection);

            JasperViewer viewer = new JasperViewer(jasperPrint, false);
            viewer.setVisible(true);
            viewer.setExtendedState(JasperViewer.MAXIMIZED_BOTH);

        } catch (JRException e) {
            JOptionPane.showMessageDialog(null, "Error al mostrar el reporte: " + e.getMessage());
            System.out.println( e.getMessage());
        } catch (SQLException e) {
            Logger.getLogger(ReportPrinter.class.getName()).log(Level.SEVERE, null, e);
        }
    }

}
