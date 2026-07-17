package vista;

import controlador.ConsultaControl;
import controlador.DoctorControl;
import controlador.MedicamentoControl;
import controlador.PacienteControl;
import controlador.RecetaControl;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import javax.swing.JOptionPane;
import modelo.Consulta;
import modelo.Doctor;
import modelo.Medicamento;
import modelo.Paciente;
import modelo.Receta;
import util.generadorRecetasPDF;

  

    public class PanelRecetas extends javax.swing.JPanel {

        private final RecetaControl recetaControl;
        private final ConsultaControl consultaControl;
        private final MedicamentoControl medicamentoControl;
        private final PacienteControl pacienteControl;
        private final DoctorControl doctorControl;

        public PanelRecetas(RecetaControl recetaControl,
        ConsultaControl consultaControl,
        MedicamentoControl medicamentoControl,
        PacienteControl pacienteControl,
        DoctorControl doctorControl) {

    this.recetaControl = recetaControl;
    this.consultaControl = consultaControl;
    this.medicamentoControl = medicamentoControl;
    this.pacienteControl = pacienteControl;
    this.doctorControl = doctorControl;

    // 1. Inicializa el diseño visual generado por NetBeans
    initComponents();

    // 2. Configuramos la tabla personalizada
    if (tablaHospital1 != null) {
        tablaHospital1.establecerColumnas(new String[]{
            "ID receta", "ID consulta", "ID medicamento", "Indicaciones", "Hora"
        });
    }

    if (txtIDReceta != null) {
        txtIDReceta.setEditable(false);
    }

    // 3. Conectamos los listeners a los botones de tu diseño
    conectarEventos();
    
    // 4. Cargamos los datos de la BD de forma segura controlando cualquier fallo inicial
    try {
        if (consultaControl != null && medicamentoControl != null) {
            actualizarCombos(); 
        }
        if (recetaControl != null) {
            actualizarTabla();  
        }
    } catch (Exception ex) {
        System.err.println("Advertencia: No se pudieron cargar los datos iniciales en el constructor: " + ex.getMessage());
    }
}

        private void conectarEventos() {
            btnGuardar.addActionListener(e -> guardarReceta());
            btnModificar.addActionListener(e -> modificarReceta());
            btnEliminar.addActionListener(e -> eliminarReceta());
            btnLimpiar.addActionListener(e -> limpiarCampos());
            btnPDF.addActionListener(e -> generarPDF());

            tablaHospital1.getSelectionModel().addListSelectionListener(e -> {
                if (!e.getValueIsAdjusting()) {
                    cargarRecetaSeleccionada();
                }
            });
        }

        public final void actualizarCombos() {
            try {
                comboConsulta.removeAllItems();
                for (Consulta consulta : consultaControl.verConsultas()) {
                    ((javax.swing.JComboBox) comboConsulta).addItem(consulta);
                }

                comboMedicamentos.removeAllItems();
                for (Medicamento medicamento : medicamentoControl.verMedicamentos()) {
                    ((javax.swing.JComboBox) comboMedicamentos).addItem(medicamento);
                }
            } catch (Exception ex) {
                System.err.println("Error al cargar los combobox: " + ex.getMessage());
            }
        }

        private void guardarReceta() {
            Consulta consulta = (Consulta) comboConsulta.getSelectedItem();
            Medicamento medicamento = (Medicamento) comboMedicamentos.getSelectedItem();

            if (consulta == null) {
                JOptionPane.showMessageDialog(this, "Primero registre una consulta");
                return;
            }

            if (medicamento == null) {
                JOptionPane.showMessageDialog(this, "Primero registre un medicamento");
                return;
            }

            String indicaciones = txtIndicaciones.getText().trim();
            if (indicaciones.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Las indicaciones de la receta son obligatorias.");
                return;
            }

            LocalTime horaSuministrar;
            try {
                horaSuministrar = LocalTime.parse(txtHora.getText().trim());
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(this, "Formato de hora incorrecto. Use HH:mm (Ej: 14:30)", "Error de formato", JOptionPane.ERROR_MESSAGE);
                return;
            }

            boolean guardado = recetaControl.crearReceta(
                    medicamento.getIdMedicamento(),
                    consulta.getIdConsulta(),
                    indicaciones,
                    horaSuministrar
            );

            if (guardado) {
                JOptionPane.showMessageDialog(this, "Receta registrada correctamente");
                actualizarTabla();
                limpiarCampos();
            } else {
                JOptionPane.showMessageDialog(this, "Error de negocio al guardar la receta.");
            }
        }

        private void modificarReceta() {
            if (txtIDReceta.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Seleccione una receta");
                return;
            }

            Consulta consulta = (Consulta) comboConsulta.getSelectedItem();
            Medicamento medicamento = (Medicamento) comboMedicamentos.getSelectedItem();

            if (consulta == null || medicamento == null) {
                JOptionPane.showMessageDialog(this, "Seleccione consulta y medicamento");
                return;
            }

            int idReceta;
            try {
                idReceta = Integer.parseInt(txtIDReceta.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "El ID de la receta no es válido");
                return;
            }

            String indicaciones = txtIndicaciones.getText().trim();
            if (indicaciones.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Las indicaciones son obligatorias.");
                return;
            }

            LocalTime horaSuministrar;
            try {
                horaSuministrar = LocalTime.parse(txtHora.getText().trim());
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(this, "Formato de hora incorrecto. Use HH:mm.");
                return;
            }

            boolean modificado = recetaControl.modificarReceta(
                    idReceta,
                    medicamento.getIdMedicamento(),
                    consulta.getIdConsulta(),
                    indicaciones,
                    horaSuministrar.toString()
            );

            if (modificado) {
                JOptionPane.showMessageDialog(this, "Receta modificada correctamente");
                actualizarTabla();
                limpiarCampos();
            }
        }

        private void eliminarReceta() {
            if (txtIDReceta.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Seleccione una receta");
                return;
            }

            int respuesta = JOptionPane.showConfirmDialog(
                    this,
                    "¿Desea eliminar la receta?",
                    "Confirmar",
                    JOptionPane.YES_NO_OPTION
            );

            if (respuesta != JOptionPane.YES_OPTION) {
                return;
            }

            int idReceta;
            try {
                idReceta = Integer.parseInt(txtIDReceta.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "El ID de la receta no es válido");
                return;
            }

            if (recetaControl.eliminarReceta(idReceta)) {
                JOptionPane.showMessageDialog(this, "Receta eliminada correctamente");
                actualizarTabla();
                limpiarCampos();
            }
        }

        private void cargarRecetaSeleccionada() {
            int fila = tablaHospital1.getSelectedRow();

            if (fila == -1) {
                return;
            }

            int idReceta = Integer.parseInt(
                    tablaHospital1.getValueAt(fila, 0).toString()
            );

            Receta receta = recetaControl.buscarReceta(idReceta);

            if (receta == null) {
                return;
            }

            txtIDReceta.setText(String.valueOf(receta.getIdReceta()));
            txtIndicaciones.setText(receta.getDescripcion());

            if (receta.getHoraSuministrar() != null) {
                txtHora.setText(receta.getHoraSuministrar().toString());
            }

            Consulta consulta = consultaControl.buscarConsulta(
                    receta.getIdConsulta()
            );

            Medicamento medicamento = medicamentoControl.buscarMedicamento(
                    receta.getIdMedicamento()
            );

            comboConsulta.setSelectedItem(consulta);
            comboMedicamentos.setSelectedItem(medicamento);
        }

        public final void actualizarTabla() {
            try {
                tablaHospital1.limpiarTabla();

                for (Receta receta : recetaControl.verRecetas()) {
                    tablaHospital1.agregarFila(new Object[]{
                        receta.getIdReceta(),
                        receta.getIdConsulta(),
                        receta.getIdMedicamento(),
                        receta.getDescripcion(),
                        receta.getHoraSuministrar()
                    });
                }
            } catch (Exception ex) {
                System.err.println("Error al actualizar la tabla: " + ex.getMessage());
            }
        }

        private void generarPDF() {
            if (txtIDReceta.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Seleccione una receta");
                return;
            }

            try {
                int idReceta = Integer.parseInt(txtIDReceta.getText());
                Receta receta = recetaControl.buscarReceta(idReceta);

                if (receta == null) {
                    JOptionPane.showMessageDialog(this, "La receta no existe");
                    return;
                }

                Consulta consulta = consultaControl.buscarConsulta(receta.getIdConsulta());
                Medicamento medicamento = medicamentoControl.buscarMedicamento(receta.getIdMedicamento());

                if (consulta == null || medicamento == null) {
                    JOptionPane.showMessageDialog(this, "No se encontró la consulta o el medicamento");
                    return;
                }

                Paciente paciente = pacienteControl.buscarPaciente(consulta.getIdPaciente());
                Doctor doctor = doctorControl.buscarDoctor(consulta.getIdCedula());

                if (paciente == null || doctor == null) {
                    JOptionPane.showMessageDialog(this, "No se encontró el paciente o el doctor");
                    return;
                }

                File archivo = generadorRecetasPDF.generar(
                        receta,
                        consulta,
                        paciente,
                        doctor,
                        medicamento
                );

                if (archivo != null && archivo.exists()) {
                    JOptionPane.showMessageDialog(this, "PDF generado en:\n" + archivo.getAbsolutePath());
                    generadorRecetasPDF.abrirPDF(archivo);
                } else {
                    JOptionPane.showMessageDialog(this, "El archivo no pudo ser creado.", "Error", JOptionPane.ERROR_MESSAGE);
                }

            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(this, "El ID de la receta no es válido");
            } catch (HeadlessException | IOException ex) {
                JOptionPane.showMessageDialog(
                        this,
                        "Error al generar PDF:\n" + ex.getMessage(),
                        "Error de sistema",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }

        private void limpiarCampos() {
            txtIDReceta.setText("");
            txtIndicaciones.setText("");
            txtHora.setText("");
            tablaHospital1.clearSelection();

            if (comboConsulta.getItemCount() > 0) {
                comboConsulta.setSelectedIndex(0);
            }

            if (comboMedicamentos.getItemCount() > 0) {
                comboMedicamentos.setSelectedIndex(0);
            }

            txtIndicaciones.requestFocus();
        }

        
        // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        lblIDreceta = new javax.swing.JLabel();
        lblConsulta = new javax.swing.JLabel();
        lbl_medicamento = new javax.swing.JLabel();
        lblIndicaciones = new javax.swing.JLabel();
        lblHora = new javax.swing.JLabel();
        txtHora = new javax.swing.JTextField();
        txtIndicaciones = new javax.swing.JTextField();
        comboMedicamentos = new javax.swing.JComboBox<>();
        comboConsulta = new javax.swing.JComboBox<>();
        txtIDReceta = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        tableInfoReceta = new javax.swing.JScrollPane();
        tablaHospital1 = new componentes.TablaHospital();
        lblTitulo = new javax.swing.JLabel();
        btnGuardar = new javax.swing.JButton();
        btnModificar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        btnLimpiar = new javax.swing.JButton();
        btnPDF = new javax.swing.JButton();

        setPreferredSize(new java.awt.Dimension(500, 550));

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos de la receta"));
        jPanel1.setFocusable(false);

        lblIDreceta.setText("ID receta:");

        lblConsulta.setText("Consulta:");

        lbl_medicamento.setText("Medicamento:");

        lblIndicaciones.setText("Indicaciones:");

        lblHora.setText("Hora HH:mm:");

        txtHora.addActionListener(evt -> txtHoraActionPerformed(evt));

        comboMedicamentos.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        comboConsulta.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblHora)
                    .addComponent(lblIDreceta)
                    .addComponent(lblConsulta)
                    .addComponent(lbl_medicamento)
                    .addComponent(lblIndicaciones))
                .addGap(26, 26, 26)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtHora)
                    .addComponent(comboMedicamentos, 0, 87, Short.MAX_VALUE)
                    .addComponent(txtIndicaciones)
                    .addComponent(txtIDReceta)
                    .addComponent(comboConsulta, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblIDreceta)
                    .addComponent(txtIDReceta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblConsulta)
                    .addComponent(comboConsulta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_medicamento)
                    .addComponent(comboMedicamentos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblIndicaciones)
                    .addComponent(txtIndicaciones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblHora)
                    .addComponent(txtHora, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(143, 143, 143))
        );

        tablaHospital1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "ID Receta", "ID Consulta", "ID Medicamento", "Indicaciones", "Hora"
            }
        ));
        tableInfoReceta.setViewportView(tablaHospital1);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(tableInfoReceta, javax.swing.GroupLayout.PREFERRED_SIZE, 292, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(tableInfoReceta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(37, Short.MAX_VALUE))
        );

        lblTitulo.setText("REGISTRO DE RECETAS");

        btnGuardar.setText("Guardar");

        btnModificar.setText("Modificar");

        btnEliminar.setText("Eliminar");

        btnLimpiar.setText("Limpiar");

        btnPDF.setText("Generar PDF");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(137, 137, 137)
                        .addComponent(lblTitulo)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(67, 67, 67)
                .addComponent(btnGuardar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnModificar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnEliminar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnLimpiar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnPDF)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblTitulo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnGuardar)
                    .addComponent(btnModificar)
                    .addComponent(btnEliminar)
                    .addComponent(btnLimpiar)
                    .addComponent(btnPDF))
                .addGap(10, 10, 10))
        );
    }// </editor-fold>
        
        
        
        
        
        
        
        
/*
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        lblIDreceta = new javax.swing.JLabel();
        lblConsulta = new javax.swing.JLabel();
        lbl_medicamento = new javax.swing.JLabel();
        lblIndicaciones = new javax.swing.JLabel();
        lblHora = new javax.swing.JLabel();
        txtHora = new javax.swing.JTextField();
        txtIndicaciones = new javax.swing.JTextField();
        comboMedicamentos = new javax.swing.JComboBox<>();
        comboConsulta = new javax.swing.JComboBox<>();
        txtIDReceta = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        tableInfoReceta = new javax.swing.JScrollPane();
        tablaHospital1 = new componentes.TablaHospital();
        lblTitulo = new javax.swing.JLabel();
        btnGuardar = new javax.swing.JButton();
        btnModificar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        btnLimpiar = new javax.swing.JButton();
        btnPDF = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(500, 550));

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos de la receta"));
        jPanel1.setFocusable(false);

        lblIDreceta.setText("ID receta:");

        lblConsulta.setText("Consulta:");

        lbl_medicamento.setText("Medicamento:");

        lblIndicaciones.setText("Indicaciones:");

        lblHora.setText("Hora HH:mm:");

        txtHora.addActionListener(this::txtHoraActionPerformed);

        comboMedicamentos.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        comboConsulta.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblHora)
                    .addComponent(lblIDreceta)
                    .addComponent(lblConsulta)
                    .addComponent(lbl_medicamento)
                    .addComponent(lblIndicaciones))
                .addGap(26, 26, 26)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtHora)
                    .addComponent(comboMedicamentos, 0, 87, Short.MAX_VALUE)
                    .addComponent(txtIndicaciones)
                    .addComponent(txtIDReceta)
                    .addComponent(comboConsulta, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblIDreceta)
                    .addComponent(txtIDReceta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblConsulta)
                    .addComponent(comboConsulta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_medicamento)
                    .addComponent(comboMedicamentos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblIndicaciones)
                    .addComponent(txtIndicaciones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblHora)
                    .addComponent(txtHora, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(143, 143, 143))
        );

        tablaHospital1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "ID Receta", "ID Consulta", "ID Medicamento", "Indicaciones", "Hora"
            }
        ));
        tableInfoReceta.setViewportView(tablaHospital1);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(tableInfoReceta, javax.swing.GroupLayout.PREFERRED_SIZE, 292, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(tableInfoReceta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(37, Short.MAX_VALUE))
        );

        lblTitulo.setText("REGISTRO DE RECETAS");

        btnGuardar.setText("Guardar");

        btnModificar.setText("Modificar");

        btnEliminar.setText("Eliminar");

        btnLimpiar.setText("Limpiar");

        btnPDF.setText("Generar PDF");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(137, 137, 137)
                        .addComponent(lblTitulo)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(67, 67, 67)
                .addComponent(btnGuardar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnModificar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnEliminar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnLimpiar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnPDF)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblTitulo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnGuardar)
                    .addComponent(btnModificar)
                    .addComponent(btnEliminar)
                    .addComponent(btnLimpiar)
                    .addComponent(btnPDF))
                .addGap(10, 10, 10))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtHoraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtHoraActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtHoraActionPerformed
*/

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnLimpiar;
    private javax.swing.JButton btnModificar;
    private javax.swing.JButton btnPDF;
    private javax.swing.JComboBox<String> comboConsulta;
    private javax.swing.JComboBox<String> comboMedicamentos;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel lblConsulta;
    private javax.swing.JLabel lblHora;
    private javax.swing.JLabel lblIDreceta;
    private javax.swing.JLabel lblIndicaciones;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JLabel lbl_medicamento;
    private componentes.TablaHospital tablaHospital1;
    private javax.swing.JScrollPane tableInfoReceta;
    private javax.swing.JTextField txtHora;
    private javax.swing.JTextField txtIDReceta;
    private javax.swing.JTextField txtIndicaciones;
    // End of variables declaration//GEN-END:variables

    private void txtHoraActionPerformed(ActionEvent evt) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}