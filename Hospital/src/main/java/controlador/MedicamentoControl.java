package controlador;

import java.util.ArrayList;
import javax.swing.JOptionPane;
import modelo.Medicamento;

public class MedicamentoControl {

    private final ArrayList<Medicamento> listaMedicamentos;
    private int siguienteId;

    public MedicamentoControl() {
        listaMedicamentos = new ArrayList<>();
        siguienteId = 1;
    }

    public boolean crearMedicamento(String nombreMedicamento,
            String dosis, String descripcion) {

        nombreMedicamento = nombreMedicamento.trim();
        dosis = dosis.trim();
        descripcion = descripcion.trim();

        if (nombreMedicamento.isEmpty()) {
            JOptionPane.showMessageDialog(null,
                    "El nombre del medicamento es obligatorio");
            return false;
        }

        if (dosis.isEmpty()) {
            JOptionPane.showMessageDialog(null,
                    "La dosis es obligatoria");
            return false;
        }

        if (descripcion.isEmpty()) {
            JOptionPane.showMessageDialog(null,
                    "La descripción es obligatoria");
            return false;
        }

        int dosisConvertida;

        try {
            dosisConvertida = Integer.parseInt(dosis);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null,
                    "La dosis debe ser un número entero");
            return false;
        }

        if (dosisConvertida <= 0) {
            JOptionPane.showMessageDialog(null,
                    "La dosis debe ser mayor que cero");
            return false;
        }

        Medicamento medicamento = new Medicamento(
                siguienteId,
                nombreMedicamento,
                dosisConvertida,
                descripcion
        );

        listaMedicamentos.add(medicamento);
        siguienteId++;

        return true;
    }

    public Medicamento buscarMedicamento(int idMedicamento) {

        for (Medicamento medicamento : listaMedicamentos) {
            if (medicamento.getIdMedicamento() == idMedicamento) {
                return medicamento;
            }
        }

        return null;
    }

    public boolean modificarMedicamento(int idMedicamento,
            String nombreMedicamento, String dosis,
            String descripcion) {

        Medicamento medicamento =
                buscarMedicamento(idMedicamento);

        if (medicamento == null) {
            JOptionPane.showMessageDialog(null,
                    "No se encontró el medicamento");
            return false;
        }

        nombreMedicamento = nombreMedicamento.trim();
        dosis = dosis.trim();
        descripcion = descripcion.trim();

        if (nombreMedicamento.isEmpty()
                || dosis.isEmpty()
                || descripcion.isEmpty()) {

            JOptionPane.showMessageDialog(null,
                    "Complete todos los datos del medicamento");
            return false;
        }

        int dosisConvertida;

        try {
            dosisConvertida = Integer.parseInt(dosis);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null,
                    "La dosis debe ser un número entero");
            return false;
        }

        if (dosisConvertida <= 0) {
            JOptionPane.showMessageDialog(null,
                    "La dosis debe ser mayor que cero");
            return false;
        }

        medicamento.setNombreMedicamento(nombreMedicamento);
        medicamento.setDosis(dosisConvertida);
        medicamento.setDescripcion(descripcion);

        return true;
    }

    public boolean eliminarMedicamento(int idMedicamento) {

        Medicamento medicamento =
                buscarMedicamento(idMedicamento);

        if (medicamento == null) {
            JOptionPane.showMessageDialog(null,
                    "No se encontró el medicamento");
            return false;
        }

        listaMedicamentos.remove(medicamento);
        return true;
    }

    public ArrayList<Medicamento> verMedicamentos() {
        return listaMedicamentos;
    }

    public int cantidadMedicamentos() {
        return listaMedicamentos.size();
    }
}