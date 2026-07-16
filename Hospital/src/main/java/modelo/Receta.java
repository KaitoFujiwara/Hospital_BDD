package modelo;

import java.time.LocalTime;

public class Receta {

    private int idReceta;
    private int idMedicamento;
    private int idConsulta;
    private String descripcion;
    private LocalTime horaSuministrar;

    public Receta() {
    }

    public Receta(int idReceta, int idMedicamento,
            int idConsulta, String descripcion,
            LocalTime horaSuministrar) {

        this.idReceta = idReceta;
        this.idMedicamento = idMedicamento;
        this.idConsulta = idConsulta;
        this.descripcion = descripcion;
        this.horaSuministrar = horaSuministrar;
    }

    public Receta(int idMedicamento, int idConsulta,
            String descripcion, LocalTime horaSuministrar) {

        this.idMedicamento = idMedicamento;
        this.idConsulta = idConsulta;
        this.descripcion = descripcion;
        this.horaSuministrar = horaSuministrar;
    }

    public int getIdReceta() {
        return idReceta;
    }

    public void setIdReceta(int idReceta) {
        this.idReceta = idReceta;
    }

    public int getIdMedicamento() {
        return idMedicamento;
    }

    public void setIdMedicamento(int idMedicamento) {
        this.idMedicamento = idMedicamento;
    }

    public int getIdConsulta() {
        return idConsulta;
    }

    public void setIdConsulta(int idConsulta) {
        this.idConsulta = idConsulta;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalTime getHoraSuministrar() {
        return horaSuministrar;
    }

    public void setHoraSuministrar(LocalTime horaSuministrar) {
        this.horaSuministrar = horaSuministrar;
    }

    @Override
    public String toString() {
        return "Receta " + idReceta
                + " - Consulta: " + idConsulta
                + " - Medicamento: " + idMedicamento;
    }
}