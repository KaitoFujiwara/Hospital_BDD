package modelo;

public class Medicamento {

    private int idMedicamento;
    private String nombreMedicamento;
    private int dosis;
    private String descripcion;

    public Medicamento() {
    }

    public Medicamento(int idMedicamento, String nombreMedicamento,
            int dosis, String descripcion) {

        this.idMedicamento = idMedicamento;
        this.nombreMedicamento = nombreMedicamento;
        this.dosis = dosis;
        this.descripcion = descripcion;
    }

    public Medicamento(String nombreMedicamento,
            int dosis, String descripcion) {

        this.nombreMedicamento = nombreMedicamento;
        this.dosis = dosis;
        this.descripcion = descripcion;
    }

    public int getIdMedicamento() {
        return idMedicamento;
    }

    public void setIdMedicamento(int idMedicamento) {
        this.idMedicamento = idMedicamento;
    }

    public String getNombreMedicamento() {
        return nombreMedicamento;
    }

    public void setNombreMedicamento(String nombreMedicamento) {
        this.nombreMedicamento = nombreMedicamento;
    }

    public int getDosis() {
        return dosis;
    }

    public void setDosis(int dosis) {
        this.dosis = dosis;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return idMedicamento + " - "
                + nombreMedicamento + " - Dosis: " + dosis;
    }
}