package modelo;

import java.time.LocalDate;
import java.time.LocalTime;

public class Consulta {

    private int idConsulta;
    private int idPaciente;
    private int idCedula;

    private LocalDate fechaEntrada;
    private LocalDate fechaSalida;

    private LocalTime horaEntrada;
    private LocalTime horaSalida;

    private String observaciones;
    private String diagnostico;
    private String alergias;
    private String sintomas;
    private String tipoSangre;

    public Consulta() {
    }

    public Consulta(int idConsulta, int idPaciente, int idCedula,
            LocalDate fechaEntrada, LocalDate fechaSalida,
            LocalTime horaEntrada, LocalTime horaSalida,
            String observaciones, String diagnostico,
            String alergias, String sintomas, String tipoSangre) {

        this.idConsulta = idConsulta;
        this.idPaciente = idPaciente;
        this.idCedula = idCedula;
        this.fechaEntrada = fechaEntrada;
        this.fechaSalida = fechaSalida;
        this.horaEntrada = horaEntrada;
        this.horaSalida = horaSalida;
        this.observaciones = observaciones;
        this.diagnostico = diagnostico;
        this.alergias = alergias;
        this.sintomas = sintomas;
        this.tipoSangre = tipoSangre;
    }

    public Consulta(int idPaciente, int idCedula,
            LocalDate fechaEntrada, LocalDate fechaSalida,
            LocalTime horaEntrada, LocalTime horaSalida,
            String observaciones, String diagnostico,
            String alergias, String sintomas, String tipoSangre) {

        this.idPaciente = idPaciente;
        this.idCedula = idCedula;
        this.fechaEntrada = fechaEntrada;
        this.fechaSalida = fechaSalida;
        this.horaEntrada = horaEntrada;
        this.horaSalida = horaSalida;
        this.observaciones = observaciones;
        this.diagnostico = diagnostico;
        this.alergias = alergias;
        this.sintomas = sintomas;
        this.tipoSangre = tipoSangre;
    }

    public int getIdConsulta() {
        return idConsulta;
    }

    public void setIdConsulta(int idConsulta) {
        this.idConsulta = idConsulta;
    }

    public int getIdPaciente() {
        return idPaciente;
    }

    public void setIdPaciente(int idPaciente) {
        this.idPaciente = idPaciente;
    }

    public int getIdCedula() {
        return idCedula;
    }

    public void setIdCedula(int idCedula) {
        this.idCedula = idCedula;
    }

    public LocalDate getFechaEntrada() {
        return fechaEntrada;
    }

    public void setFechaEntrada(LocalDate fechaEntrada) {
        this.fechaEntrada = fechaEntrada;
    }

    public LocalDate getFechaSalida() {
        return fechaSalida;
    }

    public void setFechaSalida(LocalDate fechaSalida) {
        this.fechaSalida = fechaSalida;
    }

    public LocalTime getHoraEntrada() {
        return horaEntrada;
    }

    public void setHoraEntrada(LocalTime horaEntrada) {
        this.horaEntrada = horaEntrada;
    }

    public LocalTime getHoraSalida() {
        return horaSalida;
    }

    public void setHoraSalida(LocalTime horaSalida) {
        this.horaSalida = horaSalida;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getDiagnostico() {
        return diagnostico;
    }

    public void setDiagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
    }

    public String getAlergias() {
        return alergias;
    }

    public void setAlergias(String alergias) {
        this.alergias = alergias;
    }

    public String getSintomas() {
        return sintomas;
    }

    public void setSintomas(String sintomas) {
        this.sintomas = sintomas;
    }

    public String getTipoSangre() {
        return tipoSangre;
    }

    public void setTipoSangre(String tipoSangre) {
        this.tipoSangre = tipoSangre;
    }

    @Override
    public String toString() {
        return "Consulta " + idConsulta
                + " - Paciente: " + idPaciente
                + " - Doctor: " + idCedula;
    }
}