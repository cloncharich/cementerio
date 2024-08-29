/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clases;

import java.awt.Component;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author Claudio Loncharich
 */
public class CheckBoxRenderer extends JCheckBox implements TableCellRenderer {

  
    public CheckBoxRenderer() {
        setHorizontalAlignment(JLabel.CENTER);
        setOpaque(true);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        setSelected(value != null && (Boolean) value);
        setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
        setForeground(isSelected ? table.getSelectionForeground() : table.getForeground());
        return this;
    }
    
}
