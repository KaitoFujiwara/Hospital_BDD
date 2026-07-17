package controlador;

import java.time.LocalTime;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import modelo.Receta;

public class RecetaControl {

    private final ArrayList<Receta> listaRecetas;
    private final ConsultaControl consultaControl;
    private final MedicamentoControl medicamentoControl;
    private int siguienteId;

    public RecetaControl(ConsultaControl consultaControl,
            MedicamentoControl medicamentoControl) {

        listaRecetas = new ArrayList<>();
        this.consultaControl = consultaControl;
        this.medicamentoControl = medicamentoControl;
        siguienteId = 1;
    }

    public boolean crearReceta(int idMedicamento, int idConsulta, 
        String descripcion, LocalTime horaSuministrar) {

    if (medicamentoControl.buscarMedicamento(idMedicamento) == null) {
        return false; // El negocio dice: no se puede recetar algo que no existe
    }

    if (consultaControl.buscarConsulta(idConsulta) == null) {
        return false;
    }

    descripcion = descripcion.trim();
    if (descripcion.isEmpty()) {
        return false;
    }

    Receta receta = new Receta(
            siguienteId,
            idMedicamento,
            idConsulta,
            descripcion,
            horaSuministrar
    );

    listaRecetas.add(receta);
    siguienteId++;

    return true;
}

    public Receta buscarReceta(int idReceta) {

        for (Receta receta : listaRecetas) {
            if (receta.getIdReceta() == idReceta) {
                return receta;
            }
        }

        return null;
    }

    public boolean modificarReceta(int idReceta,
            int idMedicamento, int idConsulta,
            String descripcion, String horaSuministrar) {

        Receta receta = buscarReceta(idReceta);

        if (receta == null) {
            JOptionPane.showMessageDialog(null,
                    "No se encontró la receta");
            return false;
        }

        if (medicamentoControl.buscarMedicamento(
                idMedicamento) == null) {

            JOptionPane.showMessageDialog(null,
                    "El medicamento no existe");
            return false;
        }

        if (consultaControl.buscarConsulta(idConsulta) == null) {
            JOptionPane.showMessageDialog(null,
                    "La consulta no existe");
            return false;
        }

        descripcion = descripcion.trim();
        horaSuministrar = horaSuministrar.trim();

        if (descripcion.isEmpty()) {
            JOptionPane.showMessageDialog(null,
                    "La descripción es obligatoria");
            return false;
        }

        LocalTime horaConvertida;

        try {
            horaConvertida =
                    LocalTime.parse(horaSuministrar);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "La hora debe tener el formato HH:mm");
            return false;
        }

        receta.setIdMedicamento(idMedicamento);
        receta.setIdConsulta(idConsulta);
        receta.setDescripcion(descripcion);
        receta.setHoraSuministrar(horaConvertida);

        return true;
    }

    public boolean eliminarReceta(int idReceta) {

        Receta receta = buscarReceta(idReceta);

        if (receta == null) {
            JOptionPane.showMessageDialog(null,
                    "No se encontró la receta");
            return false;
        }

        listaRecetas.remove(receta);
        return true;
    }

    public ArrayList<Receta> verRecetas() {
        return listaRecetas;
    }

    public ArrayList<Receta> verRecetasConsulta(
            int idConsulta) {

        ArrayList<Receta> recetasConsulta =
                new ArrayList<>();

        for (Receta receta : listaRecetas) {
            if (receta.getIdConsulta() == idConsulta) {
                recetasConsulta.add(receta);
            }
        }

        return recetasConsulta;
    }

    public int cantidadRecetas() {
        return listaRecetas.size();
    }
}