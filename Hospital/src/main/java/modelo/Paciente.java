package modelo;

public class Paciente {

    private int idPaciente;
    private String nombre;
    private String apellidoP;
    private String apellidoM;
    private int edad;
    private String genero;
    private int peso;

    public Paciente() {
    }

    public Paciente(int idPaciente, String nombre, String apellidoP,
            String apellidoM, int edad, String genero, int peso) {

        this.idPaciente = idPaciente;
        this.nombre = nombre;
        this.apellidoP = apellidoP;
        this.apellidoM = apellidoM;
        this.edad = edad;
        this.genero = genero;
        this.peso = peso;
    }

    public Paciente(String nombre, String apellidoP,
            String apellidoM, int edad, String genero, int peso) {

        this.nombre = nombre;
        this.apellidoP = apellidoP;
        this.apellidoM = apellidoM;
        this.edad = edad;
        this.genero = genero;
        this.peso = peso;
    }

    public int getIdPaciente() {
        return idPaciente;
    }

    public void setIdPaciente(int idPaciente) {
        this.idPaciente = idPaciente;
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

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public int getPeso() {
        return peso;
    }

    public void setPeso(int peso) {
        this.peso = peso;
    }

    public String getNombreCompleto() {
        return nombre + " " + apellidoP + " " + apellidoM;
    }

    @Override
    public String toString() {
        return idPaciente + " - " + getNombreCompleto();
    }
}