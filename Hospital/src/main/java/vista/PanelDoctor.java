package vista;

import componentes.TablaHospital;
import controlador.DoctorControl;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import modelo.Doctor;

public class PanelDoctor extends javax.swing.JPanel {

    private DoctorControl doctorControl;
    private TablaHospital tablaDoctores;
    private JPanel panelTabla;

    public PanelDoctor(DoctorControl doctorControl) {
        this.doctorControl = doctorControl;
        txtIdCedula.setEditable(false);
        configurarTabla();
        conectarEventos();
        actualizarTabla();
    }

    private void configurarTabla() {
        tablaDoctores = new TablaHospital();
        tablaDoctores.establecerColumnas(new String[]{
            "Cédula", "Nombre", "Apellido paterno",
            "Apellido materno", "Turno", "Especialidad"
        });

        panelTabla = new JPanel(new BorderLayout());
        panelTabla.setPreferredSize(new Dimension(0, 220));
        panelTabla.add(new JScrollPane(tablaDoctores), BorderLayout.CENTER);
        add(panelTabla, BorderLayout.SOUTH);
        revalidate();
        repaint();
    }

    private void conectarEventos() {
        btnGuardar.addActionListener(e -> guardarDoctor());
        btnModificar.addActionListener(e -> modificarDoctor());
        btnEliminar.addActionListener(e -> eliminarDoctor());
        btnLimpiar.addActionListener(e -> limpiarCampos());

        tablaDoctores.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarDoctorSeleccionado();
            }
        });
    }

    private void guardarDoctor() {
        String turno = cmbTurno.getSelectedItem().toString();

        if (turno.equals("Selecciona")) {
            JOptionPane.showMessageDialog(this, "Seleccione el turno del doctor");
            return;
        }

        boolean guardado = doctorControl.crearDoctor(
                txtNombre.getText(),
                txtApellidoP.getText(),
                txtApellidoM.getText(),
                turno,
                txtEspecialidad.getText()
        );

        if (guardado) {
            JOptionPane.showMessageDialog(this, "Doctor registrado correctamente");
            actualizarTabla();
            limpiarCampos();
        }
    }

    private void modificarDoctor() {
        if (txtIdCedula.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione un doctor de la tabla");
            return;
        }

        String turno = cmbTurno.getSelectedItem().toString();

        if (turno.equals("Selecciona")) {
            JOptionPane.showMessageDialog(this, "Seleccione el turno del doctor");
            return;
        }

        int idCedula;

        try {
            idCedula = Integer.parseInt(txtIdCedula.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "La cédula no es válida");
            return;
        }

        boolean modificado = doctorControl.modificarDoctor(
                idCedula,
                txtNombre.getText(),
                txtApellidoP.getText(),
                txtApellidoM.getText(),
                turno,
                txtEspecialidad.getText()
        );

        if (modificado) {
            JOptionPane.showMessageDialog(this, "Doctor modificado correctamente");
            actualizarTabla();
            limpiarCampos();
        }
    }

    private void eliminarDoctor() {
        if (txtIdCedula.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione un doctor de la tabla");
            return;
        }

        int respuesta = JOptionPane.showConfirmDialog(
                this,
                "¿Desea eliminar al doctor seleccionado?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION
        );

        if (respuesta != JOptionPane.YES_OPTION) {
            return;
        }

        int idCedula;

        try {
            idCedula = Integer.parseInt(txtIdCedula.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "La cédula no es válida");
            return;
        }

        if (doctorControl.eliminarDoctor(idCedula)) {
            JOptionPane.showMessageDialog(this, "Doctor eliminado correctamente");
            actualizarTabla();
            limpiarCampos();
        }
    }

    private void cargarDoctorSeleccionado() {
        int fila = tablaDoctores.getSelectedRow();

        if (fila == -1) {
            return;
        }

        txtIdCedula.setText(tablaDoctores.getValueAt(fila, 0).toString());
        txtNombre.setText(tablaDoctores.getValueAt(fila, 1).toString());
        txtApellidoP.setText(tablaDoctores.getValueAt(fila, 2).toString());
        txtApellidoM.setText(tablaDoctores.getValueAt(fila, 3).toString());
        cmbTurno.setSelectedItem(tablaDoctores.getValueAt(fila, 4).toString());
        txtEspecialidad.setText(tablaDoctores.getValueAt(fila, 5).toString());
    }

    public void actualizarTabla() {
        tablaDoctores.limpiarTabla();

        for (Doctor doctor : doctorControl.verDoctores()) {
            tablaDoctores.agregarFila(new Object[]{
                doctor.getIdCedula(),
                doctor.getNombre(),
                doctor.getApellidoP(),
                doctor.getApellidoM(),
                doctor.getTurno(),
                doctor.getEspecialidad()
            });
        }
    }

    private void limpiarCampos() {
        txtIdCedula.setText("");
        txtNombre.setText("");
        txtApellidoP.setText("");
        txtApellidoM.setText("");
        cmbTurno.setSelectedIndex(0);
        txtEspecialidad.setText("");
        tablaDoctores.clearSelection();
        txtNombre.requestFocus();
    }
   /*@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton3 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtIdCedula = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        btnGuardar = new javax.swing.JButton();
        btnModificar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        btnLimpiar = new javax.swing.JButton();
        txtNombre = new javax.swing.JTextField();
        txtApellidoP = new javax.swing.JTextField();
        txtApellidoM = new javax.swing.JTextField();
        cmbTurno = new javax.swing.JComboBox<>();
        txtEspecialidad = new javax.swing.JTextField();

        jButton3.setText("jButton3");

        setLayout(new java.awt.BorderLayout());

        jLabel1.setFont(new java.awt.Font("Yu Gothic UI", 1, 18)); // NOI18N
        jLabel1.setText("Registro Doctor ");

        jLabel2.setText("Número de Cédula:");

        txtIdCedula.addActionListener(this::txtIdCedulaActionPerformed);

        jLabel3.setText("Nombre:");

        jLabel4.setText("Apellido Paterno:");

        jLabel5.setText("Apellido Materno:");

        jLabel6.setText("Turno:");

        jLabel7.setText("Especialidad:");

        btnGuardar.setText("Guardar");

        btnModificar.setText("Modificar");
        btnModificar.addActionListener(this::btnModificarActionPerformed);

        btnEliminar.setText("Eliminar");

        btnLimpiar.setText("Limpiar");
        btnLimpiar.addActionListener(this::btnLimpiarActionPerformed);

        txtNombre.addActionListener(this::txtNombreActionPerformed);

        cmbTurno.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Selecciona", "Matutino", "Vespertino", "Nocturno" }));
        cmbTurno.addActionListener(this::cmbTurnoActionPerformed);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(276, 276, 276)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabel2)
                                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jLabel4))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtIdCedula)
                                    .addComponent(txtNombre)
                                    .addComponent(txtApellidoP, javax.swing.GroupLayout.DEFAULT_SIZE, 397, Short.MAX_VALUE)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel6)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel7))
                                .addGap(24, 24, 24)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtApellidoM)
                                    .addComponent(cmbTurno, 0, 397, Short.MAX_VALUE)
                                    .addComponent(txtEspecialidad)))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(78, 78, 78)
                        .addComponent(btnGuardar)
                        .addGap(42, 42, 42)
                        .addComponent(btnModificar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 50, Short.MAX_VALUE)
                        .addComponent(btnEliminar)
                        .addGap(36, 36, 36)
                        .addComponent(btnLimpiar)
                        .addGap(31, 31, 31)))
                .addContainerGap(88, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addComponent(jLabel1)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(txtIdCedula, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(txtApellidoP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(txtApellidoM, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(cmbTurno, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(txtEspecialidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(247, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnGuardar)
                            .addComponent(btnModificar)
                            .addComponent(btnEliminar)
                            .addComponent(btnLimpiar))
                        .addGap(184, 184, 184))))
        );

        add(jPanel1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents
*/
    private void txtIdCedulaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtIdCedulaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtIdCedulaActionPerformed

    private void txtNombreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNombreActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreActionPerformed

    private void cmbTurnoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbTurnoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbTurnoActionPerformed

    private void btnModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnModificarActionPerformed

    private void btnLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnLimpiarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnLimpiar;
    private javax.swing.JButton btnModificar;
    private javax.swing.JComboBox<String> cmbTurno;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField txtApellidoM;
    private javax.swing.JTextField txtApellidoP;
    private javax.swing.JTextField txtEspecialidad;
    private javax.swing.JTextField txtIdCedula;
    private javax.swing.JTextField txtNombre;
    // End of variables declaration//GEN-END:variables
