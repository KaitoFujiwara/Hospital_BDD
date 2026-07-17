package controlador;

import java.util.ArrayList;
import javax.swing.JOptionPane;
import modelo.Paciente;

public class PacienteControl {

    private final ArrayList<Paciente> listaPacientes;
    private int siguienteId;

    public PacienteControl() {
        listaPacientes = new ArrayList<>();
        siguienteId = 1;
    }

    public boolean crearPaciente(String nombre, String apellidoP,
        String apellidoM, int edad, String genero, int peso) {

        nombre = nombre.trim();
        apellidoP = apellidoP.trim();
        apellidoM = apellidoM.trim();
        genero = genero.trim();

        if (nombre.isEmpty() || apellidoP.isEmpty() || apellidoM.isEmpty() || genero.isEmpty()) {
            return false; // La vista se encargará de avisar que faltan campos
        }

        // Validación de lógica de negocio (los límites de edad/peso del hospital)
        if (edad <= 0 || edad > 120 || peso <= 0 || peso > 500) {
            return false;
        }

        Paciente paciente = new Paciente(
                siguienteId,
                nombre,
                apellidoP,
                apellidoM,
                edad,
                genero,
                peso
        );

        listaPacientes.add(paciente);
        siguienteId++;

        return true;
    }

    public Paciente buscarPaciente(int idPaciente) {

        for (Paciente paciente : listaPacientes) {
            if (paciente.getIdPaciente() == idPaciente) {
                return paciente;
            }
        }

        return null;
    }

    public boolean modificarPaciente(int idPaciente, String nombre,
            String apellidoP, String apellidoM, String edad,
            String genero, String peso) {

        Paciente paciente = buscarPaciente(idPaciente);

        if (paciente == null) {
            JOptionPane.showMessageDialog(null,
                    "No se encontró el paciente");
            return false;
        }

        nombre = nombre.trim();
        apellidoP = apellidoP.trim();
        apellidoM = apellidoM.trim();
        edad = edad.trim();
        genero = genero.trim();
        peso = peso.trim();

        if (nombre.isEmpty() || apellidoP.isEmpty()
                || apellidoM.isEmpty() || genero.isEmpty()) {

            JOptionPane.showMessageDialog(null,
                    "Complete todos los datos del paciente");
            return false;
        }

        int edadConvertida;
        int pesoConvertido;

        try {
            edadConvertida = Integer.parseInt(edad);
            pesoConvertido = Integer.parseInt(peso);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null,
                    "La edad y el peso deben ser números enteros");
            return false;
        }

        if (edadConvertida <= 0 || edadConvertida > 120) {
            JOptionPane.showMessageDialog(null,
                    "La edad no es válida");
            return false;
        }

        if (pesoConvertido <= 0 || pesoConvertido > 500) {
            JOptionPane.showMessageDialog(null,
                    "El peso no es válido");
            return false;
        }

        paciente.setNombre(nombre);
        paciente.setApellidoP(apellidoP);
        paciente.setApellidoM(apellidoM);
        paciente.setEdad(edadConvertida);
        paciente.setGenero(genero);
        paciente.setPeso(pesoConvertido);

        return true;
    }

    public boolean eliminarPaciente(int idPaciente) {

        Paciente paciente = buscarPaciente(idPaciente);

        if (paciente == null) {
            JOptionPane.showMessageDialog(null,
                    "No se encontró el paciente");
            return false;
        }

        listaPacientes.remove(paciente);
        return true;
    }

    public ArrayList<Paciente> verPacientes() {
        return listaPacientes;
    }

    public int cantidadPacientes() {
        return listaPacientes.size();
    }
}