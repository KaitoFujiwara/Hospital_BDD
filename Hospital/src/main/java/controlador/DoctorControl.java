package controlador;

import java.util.ArrayList;
import javax.swing.JOptionPane;
import modelo.Doctor;

public class DoctorControl {

    private final ArrayList<Doctor> listaDoctores;
    private int siguienteCedula;

    public DoctorControl() {
        listaDoctores = new ArrayList<>();
        siguienteCedula = 1;
    }

    public boolean crearDoctor(String nombre, String apellidoP,
            String apellidoM, String turno, String especialidad) {

        nombre = nombre.trim();
        apellidoP = apellidoP.trim();
        apellidoM = apellidoM.trim();
        turno = turno.trim();
        especialidad = especialidad.trim();

        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(null,
                    "El nombre del doctor es obligatorio");
            return false;
        }

        if (apellidoP.isEmpty()) {
            JOptionPane.showMessageDialog(null,
                    "El apellido paterno es obligatorio");
            return false;
        }

        if (apellidoM.isEmpty()) {
            JOptionPane.showMessageDialog(null,
                    "El apellido materno es obligatorio");
            return false;
        }

        if (turno.isEmpty()) {
            JOptionPane.showMessageDialog(null,
                    "El turno es obligatorio");
            return false;
        }

        if (especialidad.isEmpty()) {
            JOptionPane.showMessageDialog(null,
                    "La especialidad es obligatoria");
            return false;
        }

        Doctor doctor = new Doctor(
                siguienteCedula,
                nombre,
                apellidoP,
                apellidoM,
                turno,
                especialidad
        );

        listaDoctores.add(doctor);
        siguienteCedula++;

        return true;
    }

    public Doctor buscarDoctor(int idCedula) {

        for (Doctor doctor : listaDoctores) {
            if (doctor.getIdCedula() == idCedula) {
                return doctor;
            }
        }

        return null;
    }

    public boolean modificarDoctor(int idCedula, String nombre,
            String apellidoP, String apellidoM,
            String turno, String especialidad) {

        Doctor doctor = buscarDoctor(idCedula);

        if (doctor == null) {
            JOptionPane.showMessageDialog(null,
                    "No se encontró el doctor");
            return false;
        }

        nombre = nombre.trim();
        apellidoP = apellidoP.trim();
        apellidoM = apellidoM.trim();
        turno = turno.trim();
        especialidad = especialidad.trim();

        if (nombre.isEmpty() || apellidoP.isEmpty()
                || apellidoM.isEmpty() || turno.isEmpty()
                || especialidad.isEmpty()) {

            JOptionPane.showMessageDialog(null,
                    "Complete todos los datos del doctor");
            return false;
        }

        doctor.setNombre(nombre);
        doctor.setApellidoP(apellidoP);
        doctor.setApellidoM(apellidoM);
        doctor.setTurno(turno);
        doctor.setEspecialidad(especialidad);

        return true;
    }

    public boolean eliminarDoctor(int idCedula) {

        Doctor doctor = buscarDoctor(idCedula);

        if (doctor == null) {
            JOptionPane.showMessageDialog(null,
                    "No se encontró el doctor");
            return false;
        }

        listaDoctores.remove(doctor);
        return true;
    }

    public ArrayList<Doctor> verDoctores() {
        return listaDoctores;
    }

    public int cantidadDoctores() {
        return listaDoctores.size();
    }
}