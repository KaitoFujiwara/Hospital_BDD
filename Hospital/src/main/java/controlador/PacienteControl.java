package controlador;

import conexion.ConexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import modelo.Paciente;

public class PacienteControl {

    private ConexionBD conexionBD;

    public PacienteControl() {
        conexionBD=new ConexionBD();
    }

    public boolean crearPaciente(String nombre,String apellidoP,String apellidoM,int edad,String genero,int peso) {
        nombre=nombre.trim();
        apellidoP=apellidoP.trim();
        apellidoM=apellidoM.trim();
        genero=genero.trim();

        if(nombre.isEmpty()||apellidoP.isEmpty()||apellidoM.isEmpty()||genero.isEmpty()) {
            JOptionPane.showMessageDialog(null,"Complete todos los datos del paciente");
            return false;
        }

        if(edad<=0||edad>120) {
            JOptionPane.showMessageDialog(null,"La edad debe estar entre 1 y 120 años");
            return false;
        }

        if(peso<=0||peso>500) {
            JOptionPane.showMessageDialog(null,"El peso debe estar entre 1 y 500 kg");
            return false;
        }

        String sql="INSERT INTO \"Proyecto\".\"Paciente\" "
                +"(\"nombre(s)\",\"apellidoP\",\"apellidoM\",\"edad\",\"peso\",\"genero\") "
                +"VALUES(?,?,?,?,?,?)";

        Connection conexion=conexionBD.conectar();

        if(conexion==null) {
            JOptionPane.showMessageDialog(null,"No fue posible conectar con la base de datos");
            return false;
        }

        try(conexion;PreparedStatement sentencia=conexion.prepareStatement(sql)) {
            sentencia.setString(1,nombre);
            sentencia.setString(2,apellidoP);
            sentencia.setString(3,apellidoM);
            sentencia.setInt(4,edad);
            sentencia.setInt(5,peso);
            sentencia.setString(6,genero);

            return sentencia.executeUpdate()>0;

        }catch(SQLException e) {
            JOptionPane.showMessageDialog(null,"Error al registrar paciente:\n"+e.getMessage());
            return false;
        }
    }

    public Paciente buscarPaciente(int idPaciente) {
        String sql="SELECT \"id_Paciente\",\"nombre(s)\",\"apellidoP\",\"apellidoM\","
                +"\"edad\",\"peso\",\"genero\" "
                +"FROM \"Proyecto\".\"Paciente\" "
                +"WHERE \"id_Paciente\"=?";

        Connection conexion=conexionBD.conectar();

        if(conexion==null) {
            return null;
        }

        try(conexion;PreparedStatement sentencia=conexion.prepareStatement(sql)) {
            sentencia.setInt(1,idPaciente);

            try(ResultSet resultado=sentencia.executeQuery()) {
                if(resultado.next()) {
                    return convertirPaciente(resultado);
                }
            }

        }catch(SQLException e) {
            JOptionPane.showMessageDialog(null,"Error al buscar paciente:\n"+e.getMessage());
        }

        return null;
    }

    public boolean modificarPaciente(int idPaciente,String nombre,String apellidoP,String apellidoM,String edad,String genero,String peso) {
        nombre=nombre.trim();
        apellidoP=apellidoP.trim();
        apellidoM=apellidoM.trim();
        edad=edad.trim();
        genero=genero.trim();
        peso=peso.trim();

        if(nombre.isEmpty()||apellidoP.isEmpty()||apellidoM.isEmpty()||edad.isEmpty()||genero.isEmpty()||peso.isEmpty()) {
            JOptionPane.showMessageDialog(null,"Complete todos los datos del paciente");
            return false;
        }

        int edadConvertida;
        int pesoConvertido;

        try {
            edadConvertida=Integer.parseInt(edad);
            pesoConvertido=Integer.parseInt(peso);
        }catch(NumberFormatException e) {
            JOptionPane.showMessageDialog(null,"La edad y el peso deben ser números enteros");
            return false;
        }

        if(edadConvertida<=0||edadConvertida>120) {
            JOptionPane.showMessageDialog(null,"La edad debe estar entre 1 y 120 años");
            return false;
        }

        if(pesoConvertido<=0||pesoConvertido>500) {
            JOptionPane.showMessageDialog(null,"El peso debe estar entre 1 y 500 kg");
            return false;
        }

        String sql="UPDATE \"Proyecto\".\"Paciente\" SET "
                +"\"nombre(s)\"=?,"
                +"\"apellidoP\"=?,"
                +"\"apellidoM\"=?,"
                +"\"edad\"=?,"
                +"\"genero\"=?,"
                +"\"peso\"=? "
                +"WHERE \"id_Paciente\"=?";

        Connection conexion=conexionBD.conectar();

        if(conexion==null) {
            JOptionPane.showMessageDialog(null,"No fue posible conectar con la base de datos");
            return false;
        }

        try(conexion;PreparedStatement sentencia=conexion.prepareStatement(sql)) {
            sentencia.setString(1,nombre);
            sentencia.setString(2,apellidoP);
            sentencia.setString(3,apellidoM);
            sentencia.setInt(4,edadConvertida);
            sentencia.setString(5,genero);
            sentencia.setInt(6,pesoConvertido);
            sentencia.setInt(7,idPaciente);

            int filas=sentencia.executeUpdate();

            if(filas==0) {
                JOptionPane.showMessageDialog(null,"No se encontró el paciente");
                return false;
            }

            return true;

        }catch(SQLException e) {
            JOptionPane.showMessageDialog(null,"Error al modificar paciente:\n"+e.getMessage());
            return false;
        }
    }

    public boolean eliminarPaciente(int idPaciente) {
        String sql="DELETE FROM \"Proyecto\".\"Paciente\" "
                +"WHERE \"id_Paciente\"=?";

        Connection conexion=conexionBD.conectar();

        if(conexion==null) {
            JOptionPane.showMessageDialog(null,"No fue posible conectar con la base de datos");
            return false;
        }

        try(conexion;PreparedStatement sentencia=conexion.prepareStatement(sql)) {
            sentencia.setInt(1,idPaciente);

            int filas=sentencia.executeUpdate();

            if(filas==0) {
                JOptionPane.showMessageDialog(null,"No se encontró el paciente");
                return false;
            }

            return true;

        }catch(SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "No se pudo eliminar el paciente.\n"
                    +"Puede tener consultas relacionadas.\n\n"
                    +e.getMessage());
            return false;
        }
    }

    public ArrayList<Paciente> verPacientes() {
        ArrayList<Paciente> listaPacientes=new ArrayList<>();

        String sql="SELECT \"id_Paciente\",\"nombre(s)\",\"apellidoP\",\"apellidoM\","
                +"\"edad\",\"peso\",\"genero\" "
                +"FROM \"Proyecto\".\"Paciente\" "
                +"ORDER BY \"id_Paciente\"";

        Connection conexion=conexionBD.conectar();

        if(conexion==null) {
            return listaPacientes;
        }

        try(conexion;
            PreparedStatement sentencia=conexion.prepareStatement(sql);
            ResultSet resultado=sentencia.executeQuery()) {

            while(resultado.next()) {
                listaPacientes.add(convertirPaciente(resultado));
            }

        }catch(SQLException e) {
            JOptionPane.showMessageDialog(null,"Error al consultar pacientes:\n"+e.getMessage());
        }

        return listaPacientes;
    }

    public int cantidadPacientes() {
        String sql="SELECT COUNT(*) AS cantidad "
                +"FROM \"Proyecto\".\"Paciente\"";

        Connection conexion=conexionBD.conectar();

        if(conexion==null) {
            return 0;
        }

        try(conexion;
            PreparedStatement sentencia=conexion.prepareStatement(sql);
            ResultSet resultado=sentencia.executeQuery()) {

            if(resultado.next()) {
                return resultado.getInt("cantidad");
            }

        }catch(SQLException e) {
            JOptionPane.showMessageDialog(null,"Error al contar pacientes:\n"+e.getMessage());
        }

        return 0;
    }

    private Paciente convertirPaciente(ResultSet resultado)throws SQLException {
        return new Paciente(
                resultado.getInt("id_Paciente"),
                resultado.getString("nombre(s)"),
                resultado.getString("apellidoP"),
                resultado.getString("apellidoM"),
                resultado.getInt("edad"),
                resultado.getString("genero"),
                resultado.getInt("peso")
        );
    }
}