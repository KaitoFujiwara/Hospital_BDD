package controlador;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import modelo.Consulta;

public class ConsultaControl {

    private final ArrayList<Consulta> listaConsultas;
    private final PacienteControl pacienteControl;
    private final DoctorControl doctorControl;
    private int siguienteId;

    public ConsultaControl(PacienteControl pacienteControl,
            DoctorControl doctorControl) {

        listaConsultas = new ArrayList<>();
        this.pacienteControl = pacienteControl;
        this.doctorControl = doctorControl;
        siguienteId = 1;
    }

    public boolean crearConsulta(int idPaciente, int idCedula,
            String observaciones, String diagnostico,
            String alergias, String sintomas,
            String tipoSangre) {

        if (pacienteControl.buscarPaciente(idPaciente) == null) {
            JOptionPane.showMessageDialog(null,
                    "El paciente seleccionado no existe");
            return false;
        }

        if (doctorControl.buscarDoctor(idCedula) == null) {
            JOptionPane.showMessageDialog(null,
                    "El doctor seleccionado no existe");
            return false;
        }

        observaciones = observaciones.trim();
        diagnostico = diagnostico.trim();
        alergias = alergias.trim();
        sintomas = sintomas.trim();
        tipoSangre = tipoSangre.trim();

        if (sintomas.isEmpty()) {
            JOptionPane.showMessageDialog(null,
                    "Debe ingresar los síntomas");
            return false;
        }

        if (tipoSangre.isEmpty()) {
            JOptionPane.showMessageDialog(null,
                    "Debe seleccionar el tipo de sangre");
            return false;
        }

        Consulta consulta = new Consulta(
                siguienteId,
                idPaciente,
                idCedula,
                LocalDate.now(),
                null,
                LocalTime.now(),
                null,
                observaciones,
                diagnostico,
                alergias,
                sintomas,
                tipoSangre
        );

        listaConsultas.add(consulta);
        siguienteId++;

        return true;
    }

    public Consulta buscarConsulta(int idConsulta) {

        for (Consulta consulta : listaConsultas) {
            if (consulta.getIdConsulta() == idConsulta) {
                return consulta;
            }
        }

        return null;
    }

    public boolean modificarConsulta(int idConsulta,
            String observaciones, String diagnostico,
            String alergias, String sintomas,
            String tipoSangre) {

        Consulta consulta = buscarConsulta(idConsulta);

        if (consulta == null) {
            JOptionPane.showMessageDialog(null,
                    "No se encontró la consulta");
            return false;
        }

        observaciones = observaciones.trim();
        diagnostico = diagnostico.trim();
        alergias = alergias.trim();
        sintomas = sintomas.trim();
        tipoSangre = tipoSangre.trim();

        if (sintomas.isEmpty()) {
            JOptionPane.showMessageDialog(null,
                    "Los síntomas son obligatorios");
            return false;
        }

        if (tipoSangre.isEmpty()) {
            JOptionPane.showMessageDialog(null,
                    "El tipo de sangre es obligatorio");
            return false;
        }

        consulta.setObservaciones(observaciones);
        consulta.setDiagnostico(diagnostico);
        consulta.setAlergias(alergias);
        consulta.setSintomas(sintomas);
        consulta.setTipoSangre(tipoSangre);

        return true;
    }

    public boolean registrarSalida(int idConsulta) {

        Consulta consulta = buscarConsulta(idConsulta);

        if (consulta == null) {
            JOptionPane.showMessageDialog(null,
                    "No se encontró la consulta");
            return false;
        }

        if (consulta.getFechaSalida() != null) {
            JOptionPane.showMessageDialog(null,
                    "La salida ya fue registrada");
            return false;
        }

        consulta.setFechaSalida(LocalDate.now());
        consulta.setHoraSalida(LocalTime.now());

        return true;
    }

    public boolean eliminarConsulta(int idConsulta) {

        Consulta consulta = buscarConsulta(idConsulta);

        if (consulta == null) {
            JOptionPane.showMessageDialog(null,
                    "No se encontró la consulta");
            return false;
        }

        listaConsultas.remove(consulta);
        return true;
    }

    public ArrayList<Consulta> verConsultas() {
        return listaConsultas;
    }

    public ArrayList<Consulta> verConsultasPaciente(
            int idPaciente) {

        ArrayList<Consulta> consultasPaciente =
                new ArrayList<>();

        for (Consulta consulta : listaConsultas) {
            if (consulta.getIdPaciente() == idPaciente) {
                consultasPaciente.add(consulta);
            }
        }

        return consultasPaciente;
    }

    public ArrayList<Consulta> verConsultasDoctor(
            int idCedula) {

        ArrayList<Consulta> consultasDoctor =
                new ArrayList<>();

        for (Consulta consulta : listaConsultas) {
            if (consulta.getIdCedula() == idCedula) {
                consultasDoctor.add(consulta);
            }
        }

        return consultasDoctor;
    }

    public int cantidadConsultas() {
        return listaConsultas.size();
    }
}