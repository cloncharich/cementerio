package disenho;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author Claudio Loncharich
 */
public class JTableColor extends DefaultTableCellRenderer {

    private int columna;

    public JTableColor(int valor) {
        this.columna = valor;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean selected, boolean focused, int row, int column) {
        //setBackground(Color.white);
        //table.setForeground(Color.black);
        super.getTableCellRendererComponent(table, value, selected, focused,
                row, column);

        if (table.getValueAt(row, columna).equals("VENDIDO")) {
            /*Color c1 = new Color(255,255,0);
         setBackground(c1);*/
            this.setForeground(Color.BLACK);
        }

        if (table.getValueAt(row, columna).equals("RECUPERADO")) {
            /*Color c1 = new Color(255,255,0);
         setBackground(c1);*/
            this.setForeground(Color.BLACK);
        }
        if (table.getValueAt(row, columna).equals("CON PROBLEMAS")) {
            /*Color c1 = new Color(255,255,0);
         setBackground(c1);*/
            this.setForeground(Color.BLACK);
        }
        if (table.getValueAt(row, columna).equals("LIBRE")) {
            /*Color c3 = new Color(0,204,0);
         setBackground(c3);*/
            this.setForeground(new Color(0, 153, 0));
        }

        if (!table.getValueAt(row, columna).equals("VENDIDO") && !table.getValueAt(row, columna).equals("RECUPERADO")
                && !table.getValueAt(row, columna).equals("CON PROBLEMAS") && !table.getValueAt(row, columna).equals("LIBRE")) {
            this.setForeground(Color.red);
        }
        
           if (table.getValueAt(row, columna).equals("EN MORA")) {
            /*Color c3 = new Color(0,204,0);
         setBackground(c3);*/
            this.setForeground(Color.red);
        }
        if (table.getValueAt(row, columna).equals("NO POSEE")) {
            /*Color c3 = new Color(0,204,0);
         setBackground(c3);*/
            this.setForeground(Color.BLACK);
        }
        if (table.getValueAt(row, columna).equals("AL DIA")) {
            /*Color c3 = new Color(0,204,0);
         setBackground(c3);*/
            this.setForeground(Color.BLACK);
        }

        return this;

    }
}
