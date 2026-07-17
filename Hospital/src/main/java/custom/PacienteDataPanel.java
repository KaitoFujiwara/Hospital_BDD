package custom;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

// Este es tu componente Custom que se instala en la paleta de NetBeans
public class PacienteDataPanel extends JPanel {
    
    private JTable tablaPacientes;
    private DefaultTableModel modeloTabla;
    private JTextField txtBuscar;
    
    // Aquí conectarías con tu Bean de Persistencia / DAO
    // private PacienteDAO pacienteDao; 

    public PacienteDataPanel() {
        initComponents();
        cargarDatosDesdeBaseDatos();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));

        // 1. Panel Superior de Control e Interacción
        JPanel panelControl = new JPanel(new FlowLayout(FlowLayout.LEFT));
        txtBuscar = new JTextField(20);
        JButton btnBuscar = new JButton("Buscar Paciente");
        JButton btnNuevo = new JButton("Registrar Nuevo");
        
        panelControl.add(new JLabel("Filtrar: "));
        panelControl.add(txtBuscar);
        panelControl.add(btnBuscar);
        panelControl.add(btnNuevo);
        
        add(panelControl, BorderLayout.NORTH);

        // 2. Definición de las Columnas basadas en tu diagrama ER
        String[] columnas = {"ID Paciente", "Nombre", "Apellido P.", "Edad", "Sexo", "Peso"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Hace que la tabla sea de solo lectura (doble clic no edita celda directamente)
            }
        };
        
        tablaPacientes = new JTable(modeloTabla);
        
        // 3. Contenedor de Desplazamiento (Scroll Pane) indispensable para la Tabla
        JScrollPane scrollPane = new JScrollPane(tablaPacientes);
        add(scrollPane, BorderLayout.CENTER);
    }

    // Método interactivo que conecta con la Database
    public void cargarDatosDesdeBaseDatos() {
        // Limpiar tabla antes de cargar
        modeloTabla.setRowCount(0);
        
        // Simulación de los datos recuperados por el Bean de base de datos
        // En producción aquí harías: List<Paciente> lista = pacienteDao.listarTodos();
        Object[] filaSimulada1 = {"PAC-001", "Juan", "Pérez", 45, "M", 82.5};
        Object[] filaSimulada2 = {"PAC-002", "María", "García", 32, "F", 64.0};
        
        modeloTabla.addRow(filaSimulada1);
        modeloTabla.addRow(filaSimulada2);
    }
}