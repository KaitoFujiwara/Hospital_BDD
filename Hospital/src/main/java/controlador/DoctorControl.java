package controlador;

import conexion.ConexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import modelo.Doctor;

public class DoctorControl {

    private ConexionBD conexionBD;

    public DoctorControl() {
        conexionBD = new ConexionBD();
    }

    public boolean crearDoctor(String nombre,String apellidoP,String apellidoM,String turno,String especialidad) {
        nombre = nombre.trim();
        apellidoP = apellidoP.trim();
        apellidoM = apellidoM.trim();
        turno = turno.trim();
        especialidad = especialidad.trim();
        
        if(!nombreValido(nombre)) {
            JOptionPane.showMessageDialog(null,"El nombre solo puede contener letras y espacios");
            return false;
        }

        if(!nombreValido(apellidoP)) {
            JOptionPane.showMessageDialog(null,"El apellido paterno solo puede contener letras");
            return false;
        }
        
        if(!nombreValido(especialidad)) {
            JOptionPane.showMessageDialog(null,"La especialidad solo puede contener letras y espacios");
            return false;
        }

        if(!nombreValido(apellidoM)) {
            JOptionPane.showMessageDialog(null,"El apellido materno solo puede contener letras");
            return false;
        }

        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(null,"El nombre del doctor es obligatorio");
            return false;
        }

        if (apellidoP.isEmpty()) {
            JOptionPane.showMessageDialog(null,"El apellido paterno es obligatorio");
            return false;
        }

        if (apellidoM.isEmpty()) {
            JOptionPane.showMessageDialog(null,"El apellido materno es obligatorio");
            return false;
        }

        if (turno.isEmpty()) {
            JOptionPane.showMessageDialog(null,"El turno es obligatorio");
            return false;
        }

        if (especialidad.isEmpty()) {
            JOptionPane.showMessageDialog(null,"La especialidad es obligatoria");
            return false;
        }

        int idCedula = obtenerSiguienteCedula();

        if (idCedula == -1) {
            return false;
        }

        String sql = "INSERT INTO \"Proyecto\".\"Doctor\" "
                + "(\"id_Cedula\",\"nombre(s)\",\"apellidoP\",\"apellidoM\",turno,especialidad) "
                + "VALUES (?,?,?,?,?,?)";

        try (Connection conexion = conexionBD.conectar()) {
            if (conexion == null) {
                JOptionPane.showMessageDialog(null,"No fue posible conectar con la base de datos");
                return false;
            }

            try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
                sentencia.setInt(1,idCedula);
                sentencia.setString(2,nombre);
                sentencia.setString(3,apellidoP);
                sentencia.setString(4,apellidoM);
                sentencia.setString(5,turno);
                sentencia.setString(6,especialidad);

                return sentencia.executeUpdate() > 0;
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,"Error al registrar doctor:\n" + e.getMessage());
            return false;
        }
    }

    private int obtenerSiguienteCedula() {
        String sql = "SELECT COALESCE(MAX(\"id_Cedula\"),0)+1 AS siguiente_id "
                + "FROM \"Proyecto\".\"Doctor\"";

        try (Connection conexion = conexionBD.conectar()) {
            if (conexion == null) {
                return -1;
            }

            try (PreparedStatement sentencia = conexion.prepareStatement(sql);
                 ResultSet resultado = sentencia.executeQuery()) {

                if (resultado.next()) {
                    return resultado.getInt("siguiente_id");
                }
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,"Error al generar la cédula:\n" + e.getMessage());
        }

        return -1;
    }

    public Doctor buscarDoctor(int idCedula) {
        String sql = "SELECT \"id_Cedula\",\"nombre(s)\",\"apellidoP\",\"apellidoM\",turno,especialidad "
                + "FROM \"Proyecto\".\"Doctor\" "
                + "WHERE \"id_Cedula\"=?";

        try (Connection conexion = conexionBD.conectar()) {
            if (conexion == null) {
                return null;
            }

            try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
                sentencia.setInt(1,idCedula);

                try (ResultSet resultado = sentencia.executeQuery()) {
                    if (resultado.next()) {
                        return new Doctor(
                                resultado.getInt("id_Cedula"),
                                resultado.getString("nombre(s)"),
                                resultado.getString("apellidoP"),
                                resultado.getString("apellidoM"),
                                resultado.getString("turno"),
                                resultado.getString("especialidad")
                        );
                    }
                }
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,"Error al buscar doctor:\n" + e.getMessage());
        }

        return null;
    }

    public boolean modificarDoctor(int idCedula,String nombre,String apellidoP,String apellidoM,String turno,String especialidad) {
        nombre = nombre.trim();
        apellidoP = apellidoP.trim();
        apellidoM = apellidoM.trim();
        turno = turno.trim();
        especialidad = especialidad.trim();
        
        if(!nombreValido(nombre)) {
            JOptionPane.showMessageDialog(null,"El nombre solo puede contener letras y espacios");
            return false;
        }

        if(!nombreValido(apellidoP)) {
            JOptionPane.showMessageDialog(null,"El apellido paterno solo puede contener letras");
            return false;
        }
        
        if(!nombreValido(especialidad)) {
            JOptionPane.showMessageDialog(null,"La especialidad solo puede contener letras y espacios");
            return false;
        }
        
        if(!nombreValido(apellidoM)) {
            JOptionPane.showMessageDialog(null,"El apellido materno solo puede contener letras");
            return false;
        }

        if (nombre.isEmpty() || apellidoP.isEmpty() || apellidoM.isEmpty()
                || turno.isEmpty() || especialidad.isEmpty()) {
            JOptionPane.showMessageDialog(null,"Complete todos los datos del doctor");
            return false;
        }

        String sql = "UPDATE \"Proyecto\".\"Doctor\" SET "
                + "\"nombre(s)\"=?,\"apellidoP\"=?,\"apellidoM\"=?,turno=?,especialidad=? "
                + "WHERE \"id_Cedula\"=?";

        try (Connection conexion = conexionBD.conectar()) {
            if (conexion == null) {
                return false;
            }

            try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
                sentencia.setString(1,nombre);
                sentencia.setString(2,apellidoP);
                sentencia.setString(3,apellidoM);
                sentencia.setString(4,turno);
                sentencia.setString(5,especialidad);
                sentencia.setInt(6,idCedula);

                int filas = sentencia.executeUpdate();

                if (filas == 0) {
                    JOptionPane.showMessageDialog(null,"No se encontró el doctor");
                    return false;
                }

                return true;
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,"Error al modificar doctor:\n" + e.getMessage());
            return false;
        }
    }

    public boolean eliminarDoctor(int idCedula) {
        String sql = "DELETE FROM \"Proyecto\".\"Doctor\" WHERE \"id_Cedula\"=?";

        try (Connection conexion = conexionBD.conectar()) {
            if (conexion == null) {
                return false;
            }

            try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
                sentencia.setInt(1,idCedula);

                int filas = sentencia.executeUpdate();

                if (filas == 0) {
                    JOptionPane.showMessageDialog(null,"No se encontró el doctor");
                    return false;
                }

                return true;
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "No se pudo eliminar el doctor.\n"
                    + "Puede tener consultas relacionadas.\n\n"
                    + e.getMessage());
            return false;
        }
    }
    
    private boolean nombreValido(String texto) {
        return texto.matches("^[\\p{L} ]+$");
    }
    
    public ArrayList<Doctor> verDoctores() {
        ArrayList<Doctor> listaDoctores = new ArrayList<>();

        String sql = "SELECT \"id_Cedula\",\"nombre(s)\",\"apellidoP\",\"apellidoM\",turno,especialidad "
                + "FROM \"Proyecto\".\"Doctor\" "
                + "ORDER BY \"id_Cedula\"";

        try (Connection conexion = conexionBD.conectar()) {
            if (conexion == null) {
                return listaDoctores;
            }

            try (PreparedStatement sentencia = conexion.prepareStatement(sql);
                 ResultSet resultado = sentencia.executeQuery()) {

                while (resultado.next()) {
                    Doctor doctor = new Doctor(
                            resultado.getInt("id_Cedula"),
                            resultado.getString("nombre(s)"),
                            resultado.getString("apellidoP"),
                            resultado.getString("apellidoM"),
                            resultado.getString("turno"),
                            resultado.getString("especialidad")
                    );

                    listaDoctores.add(doctor);
                }
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,"Error al consultar doctores:\n" + e.getMessage());
        }

        return listaDoctores;
    }

    public int cantidadDoctores() {
        String sql = "SELECT COUNT(*) AS cantidad FROM \"Proyecto\".\"Doctor\"";

        try (Connection conexion = conexionBD.conectar()) {
            if (conexion == null) {
                return 0;
            }

            try (PreparedStatement sentencia = conexion.prepareStatement(sql);
                 ResultSet resultado = sentencia.executeQuery()) {

                if (resultado.next()) {
                    return resultado.getInt("cantidad");
                }
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,"Error al contar doctores:\n" + e.getMessage());
        }

        return 0;
    }
}