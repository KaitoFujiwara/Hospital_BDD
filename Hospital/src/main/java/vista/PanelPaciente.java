package vista;
import componentes.TablaHospital;
import controlador.PacienteControl;
import java.awt.BorderLayout;
import javax.swing.JOptionPane;
import modelo.Paciente;

public class PanelPaciente extends javax.swing.JPanel {
    private final PacienteControl pacienteControl;
    private TablaHospital tablaPacientes;
    
    public PanelPaciente(PacienteControl pacienteControl) {
        initComponents();
        this.pacienteControl = pacienteControl;
        configurarTabla();
        conectarEventos();
        actualizarTabla();
    }
    
    private void configurarTabla() {
        tablaPacientes = new TablaHospital();
        tablaPacientes.establecerColumnas(new String[]{"ID", "Nombre", "Apellido paterno", "Apellido materno", "Edad", "Género", "Peso"});
        panelTabla.setLayout(new BorderLayout());
        panelTabla.add(new javax.swing.JScrollPane(tablaPacientes), BorderLayout.CENTER);
        panelTabla.revalidate();
        panelTabla.repaint();
    }

    private void conectarEventos() {
        // btnGuardar ya tiene su listener en initComponents() apuntando a btnGuardarActionPerformed.
        btnModificar.addActionListener(e -> modificarPaciente());
        btnEliminar.addActionListener(e -> eliminarPaciente());
        btnLimpiar.addActionListener(e -> limpiarCampos());

        tablaPacientes.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) { cargarPacienteSeleccionado(); }
        });
    }
    
    private void guardarPaciente() {
        if (cmbGenero.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un género");
            return;
        }
        
        String genero = cmbGenero.getSelectedItem().toString();
        if (genero.equals("Seleccione") || genero.equals("Selecione")) {
            JOptionPane.showMessageDialog(this, "Seleccione un género válido");
            return;
        }

        String nombre = txtNombre.getText().trim();
        String apeP = txtApellidoP.getText().trim();
        String apeM = txtApellidoM.getText().trim();
        
        if (nombre.isEmpty() || apeP.isEmpty() || apeM.isEmpty() 
                || txtEdad.getText().trim().isEmpty() || txtPeso.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "Campos Vacíos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Se inicializan con un valor por defecto para evitar problemas de compilación
        int edad = 0;
        int peso = 0;

        try {
            edad = Integer.parseInt(txtEdad.getText().trim());
            peso = Integer.parseInt(txtPeso.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "La edad y el peso deben ser números enteros válidos.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
            return; 
        }

        boolean guardado = pacienteControl.crearPaciente(nombre, apeP, apeM, edad, genero, peso);
        
        if (guardado) {
            JOptionPane.showMessageDialog(this, "Paciente registrado correctamente");
            actualizarTabla();
            limpiarCampos();
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo registrar. Verifique que la edad (1-120) y peso (1-500) sean lógicos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void actualizarTabla() {
        tablaPacientes.limpiarTabla();
        for (Paciente paciente : pacienteControl.verPacientes()) {
            tablaPacientes.agregarFila(new Object[]{
                paciente.getIdPaciente(),
                paciente.getNombre(),
                paciente.getApellidoP(),
                paciente.getApellidoM(),
                paciente.getEdad(),
                paciente.getGenero(),
                paciente.getPeso()
            });
        }
    }
    
    private void cargarPacienteSeleccionado() {
        int fila = tablaPacientes.getSelectedRow();
        if (fila == -1) {
            return;
        }
        txtIdPaciente.setText(tablaPacientes.getValueAt(fila, 0).toString());
        txtNombre.setText(tablaPacientes.getValueAt(fila, 1).toString());
        txtApellidoP.setText(tablaPacientes.getValueAt(fila, 2).toString());
        txtApellidoM.setText(tablaPacientes.getValueAt(fila, 3).toString());
        txtEdad.setText(tablaPacientes.getValueAt(fila, 4).toString());
        cmbGenero.setSelectedItem(tablaPacientes.getValueAt(fila, 5).toString());
        txtPeso.setText(tablaPacientes.getValueAt(fila, 6).toString());
    }
        
    private void modificarPaciente() {
        if (txtIdPaciente.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione un paciente de la tabla");
            return;
        }
        
        int idPaciente = Integer.parseInt(txtIdPaciente.getText());
        String nombre = txtNombre.getText().trim();
        String apeP = txtApellidoP.getText().trim();
        String apeM = txtApellidoM.getText().trim();
        String genero = cmbGenero.getSelectedItem() != null ? cmbGenero.getSelectedItem().toString() : "";
        
        if (nombre.isEmpty() || apeP.isEmpty() || apeM.isEmpty() || genero.equals("Seleccione")) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.");
            return;
        }

        // CORRECCIÓN AQUÍ: Inicializar las variables para que el compilador no marque error
        int edad = 0;
        int peso = 0;
        String edadS;
        String pesoS;

        try {
            edad = Integer.parseInt(txtEdad.getText().trim());
            peso = Integer.parseInt(txtPeso.getText().trim());
            edadS= "" + edad + "";
            pesoS = "" + peso + "";
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Edad y peso deben ser números válidos.");
            return;
        }

        boolean modificado = pacienteControl.modificarPaciente(
                    idPaciente,
                    nombre,
                    apeP,
                    apeM,
                    edadS,
                    genero,
                    pesoS
                );
                
        if (modificado) {
            JOptionPane.showMessageDialog(this, "Paciente modificado correctamente");
            actualizarTabla();
            limpiarCampos();
        } else {
            JOptionPane.showMessageDialog(this, "Error al modificar. Verifique los rangos de edad y peso.");
        }
    }
    
    private void eliminarPaciente() {
        if (txtIdPaciente.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione un paciente");
            return;
        }

        int respuesta = JOptionPane.showConfirmDialog(
                this,
                "¿Desea eliminar el paciente?",
                "Confirmar",
                JOptionPane.YES_NO_OPTION
        );

        if (respuesta != JOptionPane.YES_OPTION) {
            return;
        }

        int idPaciente = Integer.parseInt(txtIdPaciente.getText());
        boolean eliminado = pacienteControl.eliminarPaciente(idPaciente);

        if (eliminado) {
            JOptionPane.showMessageDialog(this, "Paciente eliminado");
            actualizarTabla();
            limpiarCampos();
        }
    }
    
    private void limpiarCampos() {
        txtIdPaciente.setText("");
        txtNombre.setText("");
        txtApellidoP.setText("");
        txtApellidoM.setText("");
        txtEdad.setText("");
        cmbGenero.setSelectedIndex(0);
        txtPeso.setText("");

        tablaPacientes.clearSelection();
        txtNombre.requestFocus();
    }
    
    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {                                           
        guardarPaciente();
    }                                          


    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        txtIdPaciente = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtNombre = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtApellidoP = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtApellidoM = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtEdad = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        cmbGenero = new javax.swing.JComboBox<>();
        jLabel8 = new javax.swing.JLabel();
        txtPeso = new javax.swing.JTextField();
        btnGuardar = new javax.swing.JButton();
        btnModificar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        btnLimpiar = new javax.swing.JButton();
        panelTabla = new javax.swing.JPanel();

        setLayout(new java.awt.BorderLayout());

        jPanel1.setPreferredSize(new java.awt.Dimension(500, 550));
        jPanel1.setRequestFocusEnabled(false);

        txtIdPaciente.setEditable(false);

        jLabel1.setFont(new java.awt.Font("Yu Gothic UI Semilight", 1, 18)); // NOI18N
        jLabel1.setText("REGISTRO DE PACIENTES ");

        jLabel2.setText("Numero de paciente:");

        jLabel3.setText("Nombre :");

        jLabel4.setText("Apellido Paterno :");

        jLabel5.setText("Apelldo Materno :");

        jLabel6.setText("Edad :");

        jLabel7.setText("Genero :");

        cmbGenero.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Seleccione", "Femenino", "Masculino", "Otro" }));

        jLabel8.setText("Peso :");

        btnGuardar.setText("Guardar");
        btnGuardar.addActionListener(this::btnGuardarActionPerformed);

        btnModificar.setText("Modificar");

        btnEliminar.setText("Eliminar");

        btnLimpiar.setText("Limpiar");

        javax.swing.GroupLayout panelTablaLayout = new javax.swing.GroupLayout(panelTabla);
        panelTabla.setLayout(panelTablaLayout);
        panelTablaLayout.setHorizontalGroup(
            panelTablaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 463, Short.MAX_VALUE)
        );
        panelTablaLayout.setVerticalGroup(
            panelTablaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 252, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap(37, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel7)
                                    .addComponent(jLabel6)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel5))
                                .addGap(20, 20, 20))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(btnGuardar)
                                .addGap(28, 28, 28)))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addGap(8, 8, 8)
                                    .addComponent(btnModificar)
                                    .addGap(30, 30, 30)
                                    .addComponent(btnEliminar)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(btnLimpiar))
                                .addComponent(txtIdPaciente, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(txtNombre, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(txtApellidoP, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(txtApellidoM, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(cmbGenero, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                    .addComponent(txtEdad, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(27, 27, 27)
                                    .addComponent(jLabel8)
                                    .addGap(18, 18, Short.MAX_VALUE)
                                    .addComponent(txtPeso, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(0, 30, Short.MAX_VALUE)
                        .addComponent(panelTabla, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 47, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addComponent(jLabel1)
                .addGap(27, 27, 27)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtIdPaciente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtApellidoP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtApellidoM, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtEdad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8)
                    .addComponent(txtPeso, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(cmbGenero, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnEliminar)
                    .addComponent(btnLimpiar)
                    .addComponent(btnModificar)
                    .addComponent(btnGuardar))
                .addGap(18, 18, 18)
                .addComponent(panelTabla, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(21, Short.MAX_VALUE))
        );

        add(jPanel1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents
/*
    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        
    }//GEN-LAST:event_btnGuardarActionPerformed
*/

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnLimpiar;
    private javax.swing.JButton btnModificar;
    private javax.swing.JComboBox<String> cmbGenero;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel panelTabla;
    private javax.swing.JTextField txtApellidoM;
    private javax.swing.JTextField txtApellidoP;
    private javax.swing.JTextField txtEdad;
    private javax.swing.JTextField txtIdPaciente;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JTextField txtPeso;
    // End of variables declaration//GEN-END:variables

}