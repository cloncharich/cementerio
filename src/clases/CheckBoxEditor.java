
package clases;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.table.*;

public class CheckBoxEditor extends DefaultCellEditor {
   private JCheckBox checkBox;

    public CheckBoxEditor() {
        super(new JCheckBox());
        checkBox = (JCheckBox) getComponent();
        checkBox.setHorizontalAlignment(JLabel.CENTER);
        checkBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fireEditingStopped();
            }
        });
    }

    @Override
    public Object getCellEditorValue() {
        return checkBox.isSelected();
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int row, int column) {
        checkBox.setSelected(value != null && (Boolean) value);
        return checkBox;
    }
}
