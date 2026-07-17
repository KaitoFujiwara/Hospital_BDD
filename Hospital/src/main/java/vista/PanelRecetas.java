package vista;

import componentes.TablaHospital;
import controlador.ConsultaControl;
import controlador.DoctorControl;
import controlador.MedicamentoControl;
import controlador.PacienteControl;
import controlador.RecetaControl;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import modelo.Consulta;
import modelo.Doctor;
import modelo.Medicamento;
import modelo.Paciente;
import modelo.Receta;
import util.generadorRecetasPDF;

public class PanelRecetas extends JPanel {

    private final RecetaControl recetaControl;
    private final ConsultaControl consultaControl;
    private final MedicamentoControl medicamentoControl;
    private final PacienteControl pacienteControl;
    private final DoctorControl doctorControl;

    private final JTextField txtIdReceta;
    private final JComboBox<Consulta> cmbConsulta;
    private final JComboBox<Medicamento> cmbMedicamento;
    private final JTextField txtDescripcion;
    private final JTextField txtHoraSuministrar;

    private final JButton btnGuardar;
    private final JButton btnModificar;
    private final JButton btnEliminar;
    private final JButton btnLimpiar;
    private final JButton btnGenerarPDF; 

    private final TablaHospital tablaRecetas;

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

        txtIdReceta = new JTextField(15);
        cmbConsulta = new JComboBox<>();
        cmbMedicamento = new JComboBox<>();
        txtDescripcion = new JTextField(20);
        txtHoraSuministrar = new JTextField(10);

        btnGuardar = new JButton("Guardar");
        btnModificar = new JButton("Modificar");
        btnEliminar = new JButton("Eliminar");
        btnLimpiar = new JButton("Limpiar");
        btnGenerarPDF = new JButton("Generar PDF"); 

        tablaRecetas = new TablaHospital();

        configurarInterfaz();
        conectarEventos();
        
        // CORREGIDO: Se ejecutan de manera segura por si la BD arroja alguna excepción
        actualizarCombos(); 
        actualizarTabla();  
    }

    private void configurarInterfaz() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel titulo = new JLabel("REGISTRO DE RECETAS", JLabel.CENTER);

        JPanel formulario = new JPanel(new GridBagLayout());
        formulario.setBorder(BorderFactory.createTitledBorder("Datos de la receta"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtIdReceta.setEditable(false);

        agregarCampo(formulario, gbc, 0, "ID receta:", txtIdReceta);
        agregarCampo(formulario, gbc, 1, "Consulta:", cmbConsulta);
        agregarCampo(formulario, gbc, 2, "Medicamento:", cmbMedicamento);
        agregarCampo(formulario, gbc, 3, "Indicaciones:", txtDescripcion);
        agregarCampo(formulario, gbc, 4, "Hora HH:mm:", txtHoraSuministrar);

        tablaRecetas.establecerColumnas(new String[]{
            "ID receta", "ID consulta", "ID medicamento", "Indicaciones", "Hora"
        });

        JScrollPane scroll = new JScrollPane(tablaRecetas);

        JPanel centro = new JPanel(new BorderLayout(10, 10));
        centro.add(formulario, BorderLayout.WEST);
        centro.add(scroll, BorderLayout.CENTER);

        JPanel botones = new JPanel(new FlowLayout());
        botones.add(btnGuardar);
        botones.add(btnModificar);
        botones.add(btnEliminar);
        botones.add(btnLimpiar);
        botones.add(btnGenerarPDF); 

        add(titulo, BorderLayout.NORTH);
        add(centro, BorderLayout.CENTER);
        add(botones, BorderLayout.SOUTH);
    }

    private void agregarCampo(JPanel panel, GridBagConstraints gbc,
            int fila, String texto, java.awt.Component componente) {

        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.weightx = 0;
        panel.add(new JLabel(texto), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        panel.add(componente, gbc);
    }

    private void conectarEventos() {
        btnGuardar.addActionListener(e -> guardarReceta());
        btnModificar.addActionListener(e -> modificarReceta());
        btnEliminar.addActionListener(e -> eliminarReceta());
        btnLimpiar.addActionListener(e -> limpiarCampos());
        btnGenerarPDF.addActionListener(e -> generarPDF());

        tablaRecetas.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarRecetaSeleccionada();
            }
        });
    }

    public void actualizarCombos() {
        try {
            cmbConsulta.removeAllItems();
            for (Consulta consulta : consultaControl.verConsultas()) {
                cmbConsulta.addItem(consulta);
            }

            cmbMedicamento.removeAllItems();
            for (Medicamento medicamento : medicamentoControl.verMedicamentos()) {
                cmbMedicamento.addItem(medicamento);
            }
        } catch (Exception ex) {
            System.err.println("Error al cargar los combobox: " + ex.getMessage());
        }
    }

    private void guardarReceta() {
        Consulta consulta = (Consulta) cmbConsulta.getSelectedItem();
        Medicamento medicamento = (Medicamento) cmbMedicamento.getSelectedItem();

        if (consulta == null) {
            JOptionPane.showMessageDialog(this, "Primero registre una consulta");
            return;
        }

        if (medicamento == null) {
            JOptionPane.showMessageDialog(this, "Primero registre un medicamento");
            return;
        }

        String indicaciones = txtDescripcion.getText().trim();
        if (indicaciones.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Las indicaciones de la receta son obligatorias.");
            return;
        }

        LocalTime horaSuministrar;
        String horaSuministrarS;
        try {
            horaSuministrar = LocalTime.parse(txtHoraSuministrar.getText().trim());
            horaSuministrarS = txtHoraSuministrar.toString();
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Formato de hora incorrecto. Use HH:mm (Ej: 14:30)", "Error de formato", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // NOTA: Si tu backend aún no acepta 'LocalTime' directamente y te da error aquí, 
        // cambia el último parámetro por: horaSuministrar.toString()  O  java.sql.Time.valueOf(horaSuministrar)
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
        if (txtIdReceta.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione una receta");
            return;
        }

        Consulta consulta = (Consulta) cmbConsulta.getSelectedItem();
        Medicamento medicamento = (Medicamento) cmbMedicamento.getSelectedItem();

        if (consulta == null || medicamento == null) {
            JOptionPane.showMessageDialog(this, "Seleccione consulta y medicamento");
            return;
        }

        int idReceta;
        try {
            idReceta = Integer.parseInt(txtIdReceta.getText());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El ID de la receta no es válido");
            return;
        }

        String indicaciones = txtDescripcion.getText().trim();
        if (indicaciones.isEmpty()) {
             JOptionPane.showMessageDialog(this, "Las indicaciones son obligatorias.");
             return;
        }

        LocalTime horaSuministrar;
        String horaSuministrarS;
        try {
            horaSuministrar = LocalTime.parse(txtHoraSuministrar.getText().trim());
            horaSuministrarS = txtHoraSuministrar.toString();
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Formato de hora incorrecto. Use HH:mm.");
            return;
        }

        // CORREGIDO: Si tu controlador espera 'String' o 'java.sql.Time' en vez de LocalTime:
        // Opción A (Si espera String): use -> horaSuministrar.toString()
        // Opción B (Si espera java.sql.Time): use -> java.sql.Time.valueOf(horaSuministrar)
        boolean modificado = recetaControl.modificarReceta(
                idReceta,
                medicamento.getIdMedicamento(),
                consulta.getIdConsulta(),
                indicaciones,
                horaSuministrarS // Cambia a 'horaSuministrar.toString()' si tu método en el controlador pide String
        );

        if (modificado) {
            JOptionPane.showMessageDialog(this, "Receta modificada correctamente");
            actualizarTabla();
            limpiarCampos();
        }
    }

    private void eliminarReceta() {
        if (txtIdReceta.getText().isEmpty()) {
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
            idReceta = Integer.parseInt(txtIdReceta.getText());
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
        int fila = tablaRecetas.getSelectedRow();

        if (fila == -1) {
            return;
        }

        int idReceta = Integer.parseInt(
                tablaRecetas.getValueAt(fila, 0).toString()
        );

        Receta receta = recetaControl.buscarReceta(idReceta);

        if (receta == null) {
            return;
        }

        txtIdReceta.setText(String.valueOf(receta.getIdReceta()));
        txtDescripcion.setText(receta.getDescripcion());

        if (receta.getHoraSuministrar() != null) {
            txtHoraSuministrar.setText(receta.getHoraSuministrar().toString());
        }

        Consulta consulta = consultaControl.buscarConsulta(
                receta.getIdConsulta()
        );

        Medicamento medicamento = medicamentoControl.buscarMedicamento(
                receta.getIdMedicamento()
        );

        cmbConsulta.setSelectedItem(consulta);
        cmbMedicamento.setSelectedItem(medicamento);
    }

    public void actualizarTabla() {
        try {
            tablaRecetas.limpiarTabla();

            for (Receta receta : recetaControl.verRecetas()) {
                tablaRecetas.agregarFila(new Object[]{
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
        if (txtIdReceta.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione una receta");
            return;
        }

        try {
            int idReceta = Integer.parseInt(txtIdReceta.getText());
            Receta receta = recetaControl.buscarReceta(idReceta);

            if (receta == null) {
                JOptionPane.showMessageDialog(this, "La receta no existe");
                return;
            }

            Consulta consulta = consultaControl.buscarConsulta(
                    receta.getIdConsulta()
            );

            Medicamento medicamento = medicamentoControl.buscarMedicamento(
                    receta.getIdMedicamento()
            );

            if (consulta == null || medicamento == null) {
                JOptionPane.showMessageDialog(
                        this,
                        "No se encontró la consulta o el medicamento"
                );
                return;
            }

            Paciente paciente = pacienteControl.buscarPaciente(
                    consulta.getIdPaciente()
            );

            Doctor doctor = doctorControl.buscarDoctor(
                    consulta.getIdCedula()
            );

            if (paciente == null || doctor == null) {
                JOptionPane.showMessageDialog(
                        this,
                        "No se encontró el paciente o el doctor"
                );
                return;
            }

            File archivo = generadorRecetasPDF.generar(
                    receta,
                    consulta,
                    paciente,
                    doctor,
                    medicamento
            );

            JOptionPane.showMessageDialog(
                    this,
                    "PDF generado en:\n" + archivo.getAbsolutePath()
            );

            generadorRecetasPDF.abrirPDF(archivo);

        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(this, "El ID de la receta no es válido");
        } catch (Exception ex) { // CORREGIDO: Se cambió 'e' por 'ex' para evitar colisiones de variables
            JOptionPane.showMessageDialog(
                    this,
                    "Error al generar PDF:\n" + ex.getMessage()
            );
            ex.printStackTrace(); // CORREGIDO: Ahora usa 'ex'
        }
    }

    private void limpiarCampos() {
        txtIdReceta.setText("");
        txtDescripcion.setText("");
        txtHoraSuministrar.setText("");
        tablaRecetas.clearSelection();

        if (cmbConsulta.getItemCount() > 0) {
            cmbConsulta.setSelectedIndex(0);
        }

        if (cmbMedicamento.getItemCount() > 0) {
            cmbMedicamento.setSelectedIndex(0);
        }

        txtDescripcion.requestFocus();
    }
}