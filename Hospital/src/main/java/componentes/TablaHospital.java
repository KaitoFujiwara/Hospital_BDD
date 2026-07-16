package componentes;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class TablaHospital extends JTable {

    public TablaHospital() {
        setRowHeight(25);
    }

    public void establecerColumnas(String[] columnas) {
        setModel(new DefaultTableModel(null, columnas) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        });
    }

    public void limpiarTabla() {
        DefaultTableModel modelo = (DefaultTableModel) getModel();
        modelo.setRowCount(0);
    }

    public void agregarFila(Object[] datos) {
        DefaultTableModel modelo = (DefaultTableModel) getModel();
        modelo.addRow(datos);
    }
}