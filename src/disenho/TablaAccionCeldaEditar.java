/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package disenho;

import formularios.panelAccion;
import java.awt.Component;
import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import clases.TablaAccionEvento;


/**
 *
 * @author Claudio Loncharich
 */

public class TablaAccionCeldaEditar extends DefaultCellEditor{
      private TablaAccionEvento event;

    public TablaAccionCeldaEditar(TablaAccionEvento event) {
        super(new JCheckBox());
        this.event = event;
    }

    @Override
    public Component getTableCellEditorComponent(JTable jtable, Object o, boolean bln, int row, int column) {
        panelAccion action = new panelAccion();
        action.initEvent(event, row);
        action.setBackground(jtable.getSelectionBackground());
        return action;
    } 
}
