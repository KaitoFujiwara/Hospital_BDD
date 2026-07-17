package vista;

import componentes.TablaHospital;
import controlador.ConsultaControl;
import controlador.DoctorControl;
import controlador.PacienteControl;
import java.awt.BorderLayout;
import java.time.LocalDate;
import java.time.LocalTime;
import javax.swing.JOptionPane;
import modelo.Consulta;
import modelo.Doctor;
import modelo.Paciente;


public class PanelConsulta extends javax.swing.JPanel {

    private ConsultaControl consultaControl;
    private PacienteControl pacienteControl;
    private DoctorControl doctorControl;
    private TablaHospital tablaConsultas;

    public PanelConsulta() {
        initComponents();

        cmbTipoSangre.setModel(
                new javax.swing.DefaultComboBoxModel<>(
                        new String[]{
                            "Seleccione",
                            "A+",
                            "A-",
                            "B+",
                            "B-",
                            "AB+",
                            "AB-",
                            "O+",
                            "O-"
                        }
                )
        );

        configurarCampos();
        mostrarFechaHoraActual();
    }

    public PanelConsulta(
            ConsultaControl consultaControl,
            PacienteControl pacienteControl,
            DoctorControl doctorControl) {

        initComponents();

        this.consultaControl = consultaControl;
        this.pacienteControl = pacienteControl;
        this.doctorControl = doctorControl;

        cmbTipoSangre.setModel(
                new javax.swing.DefaultComboBoxModel<>(
                        new String[]{
                            "Seleccione",
                            "A+",
                            "A-",
                            "B+",
                            "B-",
                            "AB+",
                            "AB-",
                            "O+",
                            "O-"
                        }
                )
        );

        configurarTabla();
        configurarCampos();
        actualizarCombos();
        actualizarTabla();
        mostrarFechaHoraActual();
    }
    
private void configurarTabla() {

    tablaConsultas = new TablaHospital();

    tablaConsultas.establecerColumnas(new String[]{
        "ID consulta",
        "ID paciente",
        "Cédula doctor",
        "Fecha entrada",
        "Hora entrada",
        "Fecha salida",
        "Hora salida",
        "Tipo sangre"
    });

    panelTablaConsulta.setLayout(new BorderLayout());
    panelTablaConsulta.add(
            new javax.swing.JScrollPane(tablaConsultas),
            BorderLayout.CENTER
    );

    tablaConsultas.getSelectionModel().addListSelectionListener(e -> {
        if (!e.getValueIsAdjusting()) {
            cargarConsultaSeleccionada();
        }
    });
}
private void configurarCampos() {
    txtIdConsulta.setEditable(false);
    txtFechaEntrada.setEditable(false);
    txtHoraEntrada.setEditable(false);
    txtFechaSalida.setEditable(false);
    txtHoraSalida.setEditable(false);
}

public void actualizarCombos() {

    if (pacienteControl == null || doctorControl == null) {
        return;
    }

    cmbPaciente.removeAllItems();
    cmbDoctor.removeAllItems();

    for (Paciente paciente : pacienteControl.verPacientes()) {
        cmbPaciente.addItem(paciente);
    }

    for (Doctor doctor : doctorControl.verDoctores()) {
        cmbDoctor.addItem(doctor);
    }
}

private void mostrarFechaHoraActual() {
    txtFechaEntrada.setText(LocalDate.now().toString());
    txtHoraEntrada.setText(
            LocalTime.now().withSecond(0).withNano(0).toString()
    );
}

private void registrarConsulta() {

    if (consultaControl == null) {
        JOptionPane.showMessageDialog(
                this,
                "El controlador de consultas no está disponible"
        );
        return;
    }

    if (cmbPaciente.getSelectedItem() == null) {
        JOptionPane.showMessageDialog(
                this,
                "Seleccione un paciente"
        );
        return;
    }

    if (cmbDoctor.getSelectedItem() == null) {
        JOptionPane.showMessageDialog(
                this,
                "Seleccione un doctor"
        );
        return;
    }

    if (cmbTipoSangre.getSelectedIndex() == 0) {
        JOptionPane.showMessageDialog(
                this,
                "Seleccione el tipo de sangre"
        );
        return;
    }

    Paciente paciente =
            (Paciente) cmbPaciente.getSelectedItem();

    Doctor doctor =
            (Doctor) cmbDoctor.getSelectedItem();

    boolean registrado =
            consultaControl.crearConsulta(
                    paciente.getIdPaciente(),
                    doctor.getIdCedula(),
                    txtObservaciones.getText(),
                    txtDiagnostico.getText(),
                    txtAlergias.getText(),
                    txtSintomas.getText(),
                    cmbTipoSangre
                            .getSelectedItem()
                            .toString()
            );

    if (registrado) {
        JOptionPane.showMessageDialog(
                this,
                "Consulta registrada correctamente"
        );

        actualizarTabla();
        limpiarCampos();
    }
}
public void actualizarTabla() {

    if (tablaConsultas == null || consultaControl == null) {
        return;
    }

    tablaConsultas.limpiarTabla();

    for (Consulta consulta : consultaControl.verConsultas()) {

        tablaConsultas.agregarFila(new Object[]{
            consulta.getIdConsulta(),
            consulta.getIdPaciente(),
            consulta.getIdCedula(),
            consulta.getFechaEntrada(),
            consulta.getHoraEntrada(),
            consulta.getFechaSalida() == null
                    ? "Sin registrar"
                    : consulta.getFechaSalida(),
            consulta.getHoraSalida() == null
                    ? "Sin registrar"
                    : consulta.getHoraSalida(),
            consulta.getTipoSangre()
        });
    }
}

private void cargarConsultaSeleccionada() {
    
    if (tablaConsultas == null || consultaControl == null) {
    return;
    }
    int fila = tablaConsultas.getSelectedRow();

    if (fila == -1) {
        return;
    }

    int idConsulta = Integer.parseInt(
            tablaConsultas.getValueAt(fila, 0).toString()
    );

    Consulta consulta = consultaControl.buscarConsulta(idConsulta);

    if (consulta == null) {
        return;
    }

    txtIdConsulta.setText(String.valueOf(consulta.getIdConsulta()));
    txtFechaEntrada.setText(String.valueOf(consulta.getFechaEntrada()));
    txtHoraEntrada.setText(String.valueOf(consulta.getHoraEntrada()));

    txtFechaSalida.setText(
            consulta.getFechaSalida() == null ? "" : consulta.getFechaSalida().toString()
    );

    txtHoraSalida.setText(
            consulta.getHoraSalida() == null ? "" : consulta.getHoraSalida().toString()
    );

    txtObservaciones.setText(consulta.getObservaciones());
    txtDiagnostico.setText(consulta.getDiagnostico());
    txtAlergias.setText(consulta.getAlergias());
    txtSintomas.setText(consulta.getSintomas());

    cmbTipoSangre.setSelectedItem(consulta.getTipoSangre());

    seleccionarPaciente(consulta.getIdPaciente());
    seleccionarDoctor(consulta.getIdCedula());
}

private void seleccionarPaciente(int idPaciente) {
    for (int i = 0; i < cmbPaciente.getItemCount(); i++) {
        Paciente paciente = (Paciente) cmbPaciente.getItemAt(i);
        if (paciente.getIdPaciente() == idPaciente) {
            cmbPaciente.setSelectedIndex(i);
            return;
        }
    }
}
private void seleccionarDoctor(int idCedula) {
    for (int i = 0; i < cmbDoctor.getItemCount(); i++) {
        Doctor doctor = (Doctor) cmbDoctor.getItemAt(i);
        if (doctor.getIdCedula() == idCedula) {
            cmbDoctor.setSelectedIndex(i);
            return;
        }
    }
}

private void modificarConsulta() {
    if (consultaControl == null) {
    JOptionPane.showMessageDialog(
            this,
            "El controlador de consultas no está disponible"
    );
    return;
    }
    if (txtIdConsulta.getText().isEmpty()) {
        JOptionPane.showMessageDialog(this,
                "Seleccione una consulta");
        return;
    }

    if (cmbTipoSangre.getSelectedIndex() == 0) {
        JOptionPane.showMessageDialog(this,
                "Seleccione el tipo de sangre");
        return;
    }

    int idConsulta = Integer.parseInt(txtIdConsulta.getText());

    boolean modificado = consultaControl.modificarConsulta(
            idConsulta,
            txtObservaciones.getText(),
            txtDiagnostico.getText(),
            txtAlergias.getText(),
            txtSintomas.getText(),
            cmbTipoSangre.getSelectedItem().toString()
    );

    if (modificado) {
        JOptionPane.showMessageDialog(this,
                "Consulta modificada correctamente");
        actualizarTabla();
        limpiarCampos();
    }
}
private void registrarSalida() {

    if (txtIdConsulta.getText().isEmpty()) {
        JOptionPane.showMessageDialog(this,
                "Seleccione una consulta");
        return;
    }

    int idConsulta = Integer.parseInt(txtIdConsulta.getText());

    boolean salida = consultaControl.registrarSalida(idConsulta);

    if (salida) {
        Consulta consulta = consultaControl.buscarConsulta(idConsulta);

        txtFechaSalida.setText(String.valueOf(consulta.getFechaSalida()));
        txtHoraSalida.setText(String.valueOf(consulta.getHoraSalida()));

        JOptionPane.showMessageDialog(this,
                "Salida registrada correctamente");

        actualizarTabla();
    }
}
private void eliminarConsulta() {

    if (consultaControl == null) {
        return;
    }

    if (txtIdConsulta.getText().isEmpty()) {
        JOptionPane.showMessageDialog(
                this,
                "Seleccione una consulta"
        );
        return;
    }

    int respuesta =
            JOptionPane.showConfirmDialog(
                    this,
                    "¿Desea eliminar la consulta?",
                    "Eliminar consulta",
                    JOptionPane.YES_NO_OPTION
            );

    if (respuesta != JOptionPane.YES_OPTION) {
        return;
    }

    int idConsulta =
            Integer.parseInt(txtIdConsulta.getText());

    boolean eliminado =
            consultaControl.eliminarConsulta(idConsulta);

    if (eliminado) {
        JOptionPane.showMessageDialog(
                this,
                "Consulta eliminada correctamente"
        );

        actualizarTabla();
        limpiarCampos();
    }
}
private void limpiarCampos() {

    txtIdConsulta.setText("");
    txtObservaciones.setText("");
    txtDiagnostico.setText("");
    txtAlergias.setText("");
    txtSintomas.setText("");

    txtFechaSalida.setText("");
    txtHoraSalida.setText("");

    if (cmbPaciente.getItemCount() > 0) {
        cmbPaciente.setSelectedIndex(0);
    }

    if (cmbDoctor.getItemCount() > 0) {
        cmbDoctor.setSelectedIndex(0);
    }

    if (cmbTipoSangre.getItemCount() > 0) {
        cmbTipoSangre.setSelectedIndex(0);
    }

    mostrarFechaHoraActual();

    if (tablaConsultas != null) {
        tablaConsultas.clearSelection();
    }
}
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        cmbPaciente = new javax.swing.JComboBox<>();
        cmbDoctor = new javax.swing.JComboBox<>();
        cmbTipoSangre = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtIdConsulta = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txtFechaEntrada = new javax.swing.JTextField();
        txtHoraEntrada = new javax.swing.JTextField();
        txtFechaSalida = new javax.swing.JTextField();
        txtHoraSalida = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtSintomas = new javax.swing.JTextArea();
        jLabel11 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtDiagnostico = new javax.swing.JTextArea();
        jLabel12 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtAlergias = new javax.swing.JTextArea();
        jLabel13 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        txtObservaciones = new javax.swing.JTextArea();
        btnRegistrar = new javax.swing.JButton();
        btnModificar = new javax.swing.JButton();
        btnSalida = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        btnLimpiar = new javax.swing.JButton();
        panelTablaConsulta = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();

        cmbPaciente.addActionListener(this::cmbPacienteActionPerformed);

        cmbDoctor.addActionListener(this::cmbDoctorActionPerformed);

        cmbTipoSangre.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel1.setText("Tipo De Sangre");

        jLabel2.setText("Doctor:");

        jLabel3.setText("Paciente:");

        jLabel4.setText("Datos de la consulta");

        jLabel5.setText("ID Consulta");

        jLabel6.setText("Fecha de entrada:");

        jLabel7.setText("Hora de entrada");

        jLabel8.setText("Fecha de salida: ");

        jLabel9.setText("Hora de salida: ");

        jLabel10.setText("Sintomas:");

        txtSintomas.setColumns(20);
        txtSintomas.setRows(5);
        jScrollPane1.setViewportView(txtSintomas);

        jLabel11.setText("Diagnostico:");

        txtDiagnostico.setColumns(20);
        txtDiagnostico.setRows(5);
        jScrollPane2.setViewportView(txtDiagnostico);

        jLabel12.setText("Alergias:");

        txtAlergias.setColumns(20);
        txtAlergias.setRows(5);
        jScrollPane3.setViewportView(txtAlergias);

        jLabel13.setText("Observaciones");

        txtObservaciones.setColumns(20);
        txtObservaciones.setRows(5);
        jScrollPane4.setViewportView(txtObservaciones);

        btnRegistrar.setText("Registrar Consulta");
        btnRegistrar.addActionListener(this::btnRegistrarActionPerformed);

        btnModificar.setText("Modificar");
        btnModificar.addActionListener(this::btnModificarActionPerformed);

        btnSalida.setText("Registrar Salida");
        btnSalida.addActionListener(this::btnSalidaActionPerformed);

        btnEliminar.setText("Eliminar");
        btnEliminar.addActionListener(this::btnEliminarActionPerformed);

        btnLimpiar.setText("Limpiar");
        btnLimpiar.addActionListener(this::btnLimpiarActionPerformed);

        javax.swing.GroupLayout panelTablaConsultaLayout = new javax.swing.GroupLayout(panelTablaConsulta);
        panelTablaConsulta.setLayout(panelTablaConsultaLayout);
        panelTablaConsultaLayout.setHorizontalGroup(
            panelTablaConsultaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 392, Short.MAX_VALUE)
        );
        panelTablaConsultaLayout.setVerticalGroup(
            panelTablaConsultaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 760, Short.MAX_VALUE)
        );

        jLabel14.setText("Historial De Consultas");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addContainerGap()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabel2)
                                    .addGap(0, 268, Short.MAX_VALUE))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                            .addComponent(jLabel1)
                                            .addGap(18, 18, 18)
                                            .addComponent(cmbTipoSangre, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jLabel3)
                                                .addComponent(jLabel5))
                                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel1Layout.createSequentialGroup()
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                    .addComponent(txtIdConsulta))
                                                .addGroup(jPanel1Layout.createSequentialGroup()
                                                    .addGap(39, 39, 39)
                                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(cmbDoctor, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(cmbPaciente, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))))
                                    .addGap(51, 51, 51))))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addGap(50, 50, 50)
                                    .addComponent(jLabel4))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addContainerGap()
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                            .addComponent(jLabel9)
                                            .addGap(18, 18, 18)
                                            .addComponent(txtHoraSalida, javax.swing.GroupLayout.DEFAULT_SIZE, 155, Short.MAX_VALUE))
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                            .addComponent(jLabel8)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(txtFechaSalida))
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                            .addComponent(jLabel7)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(txtHoraEntrada))
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                            .addComponent(jLabel6)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(txtFechaEntrada))))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addGap(20, 20, 20)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel11)
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel10)
                                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 246, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 246, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGap(0, 43, Short.MAX_VALUE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel13)
                            .addComponent(jLabel12))
                        .addGap(207, 207, 207)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelTablaConsulta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14))
                .addGap(61, 61, 61))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(103, 103, 103)
                .addComponent(btnRegistrar)
                .addGap(18, 18, 18)
                .addComponent(btnModificar)
                .addGap(18, 18, 18)
                .addComponent(btnSalida)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnLimpiar)
                .addGap(18, 18, 18)
                .addComponent(btnEliminar)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addComponent(jLabel4)
                        .addGap(25, 25, 25)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtIdConsulta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5))
                        .addGap(11, 11, 11)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(cmbPaciente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cmbDoctor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cmbTipoSangre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(txtFechaEntrada, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(txtHoraEntrada, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(txtFechaSalida, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9)
                            .addComponent(txtHoraSalida, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel13)
                        .addGap(10, 10, 10)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(panelTablaConsulta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnRegistrar)
                    .addComponent(btnModificar)
                    .addComponent(btnSalida)
                    .addComponent(btnLimpiar)
                    .addComponent(btnEliminar))
                .addContainerGap(24, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void cmbDoctorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbDoctorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbDoctorActionPerformed

    private void cmbPacienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbPacienteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbPacienteActionPerformed

    private void btnRegistrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegistrarActionPerformed
        registrarConsulta();
    }//GEN-LAST:event_btnRegistrarActionPerformed

    private void btnModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificarActionPerformed
        modificarConsulta();
    }//GEN-LAST:event_btnModificarActionPerformed

    private void btnSalidaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalidaActionPerformed
        registrarSalida();
    }//GEN-LAST:event_btnSalidaActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        eliminarConsulta();
    }//GEN-LAST:event_btnEliminarActionPerformed

    private void btnLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarActionPerformed
        limpiarCampos();
    }//GEN-LAST:event_btnLimpiarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnLimpiar;
    private javax.swing.JButton btnModificar;
    private javax.swing.JButton btnRegistrar;
    private javax.swing.JButton btnSalida;
    private javax.swing.JComboBox<Doctor> cmbDoctor;
    private javax.swing.JComboBox<Paciente> cmbPaciente;
    private javax.swing.JComboBox<String> cmbTipoSangre;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JPanel panelTablaConsulta;
    private javax.swing.JTextArea txtAlergias;
    private javax.swing.JTextArea txtDiagnostico;
    private javax.swing.JTextField txtFechaEntrada;
    private javax.swing.JTextField txtFechaSalida;
    private javax.swing.JTextField txtHoraEntrada;
    private javax.swing.JTextField txtHoraSalida;
    private javax.swing.JTextField txtIdConsulta;
    private javax.swing.JTextArea txtObservaciones;
    private javax.swing.JTextArea txtSintomas;
    // End of variables declaration//GEN-END:variables
}
