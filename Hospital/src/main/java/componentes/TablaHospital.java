package componentes;

import java.awt.Font;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

public class TablaHospital extends JTable {

    public TablaHospital() {
        setFont(new Font("Arial", Font.PLAIN, 13));
        setRowHeight(25);
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        setFillsViewportHeight(true);
    }

    public void establecerColumnas(String[] columnas) {
        setModel(new DefaultTableModel(null, columnas) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        });
    }

    public void agregarFila(Object[] fila) {
        DefaultTableModel modelo = (DefaultTableModel) getModel();
        modelo.addRow(fila);
    }

    public void limpiarTabla() {
        DefaultTableModel modelo = (DefaultTableModel) getModel();
        modelo.setRowCount(0);
    }
}