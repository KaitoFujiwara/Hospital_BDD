package modelo;

public class Doctor {

    private int idCedula;
    private String nombre;
    private String apellidoP;
    private String apellidoM;
    private String turno;
    private String especialidad;

    public Doctor() {
    }

    public Doctor(int idCedula, String nombre, String apellidoP,
            String apellidoM, String turno, String especialidad) {

        this.idCedula = idCedula;
        this.nombre = nombre;
        this.apellidoP = apellidoP;
        this.apellidoM = apellidoM;
        this.turno = turno;
        this.especialidad = especialidad;
    }

    public Doctor(String nombre, String apellidoP,
            String apellidoM, String turno, String especialidad) {

        this.nombre = nombre;
        this.apellidoP = apellidoP;
        this.apellidoM = apellidoM;
        this.turno = turno;
        this.especialidad = especialidad;
    }

    public int getIdCedula() {
        return idCedula;
    }

    public void setIdCedula(int idCedula) {
        this.idCedula = idCedula;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidoP() {
        return apellidoP;
    }

    public void setApellidoP(String apellidoP) {
        this.apellidoP = apellidoP;
    }

    public String getApellidoM() {
        return apellidoM;
    }

    public void setApellidoM(String apellidoM) {
        this.apellidoM = apellidoM;
    }

    public String getTurno() {
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public String getNombreCompleto() {
        return nombre + " " + apellidoP + " " + apellidoM;
    }

    @Override
    public String toString() {
        return idCedula + " - Dr. " + getNombreCompleto();
    }
}