package controlador;

import conexion.ConexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import modelo.Consulta;

public class ConsultaControl {

    private PacienteControl pacienteControl;
    private DoctorControl doctorControl;
    private ConexionBD conexionBD;

    public ConsultaControl(PacienteControl pacienteControl, DoctorControl doctorControl) {
        this.pacienteControl = pacienteControl;
        this.doctorControl = doctorControl;
        conexionBD = new ConexionBD();
    }

    public boolean crearConsulta(int idPaciente, int idCedula, String observaciones, String diagnostico, String alergias, String sintomas, String tipoSangre) {
        if (pacienteControl.buscarPaciente(idPaciente) == null) {
            JOptionPane.showMessageDialog(null, "El paciente seleccionado no existe");
            return false;
        }

        if (doctorControl.buscarDoctor(idCedula) == null) {
            JOptionPane.showMessageDialog(null, "El doctor seleccionado no existe");
            return false;
        }

        observaciones = observaciones.trim();
        diagnostico = diagnostico.trim();
        alergias = alergias.trim();
        sintomas = sintomas.trim();
        tipoSangre = tipoSangre.trim();

        // --- VALIDACIONES DE CAMPOS VACÍOS ---
        if (sintomas.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe ingresar los síntomas");
            return false;
        }

        if (diagnostico.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe ingresar el diagnóstico médico");
            return false;
        }

        if (alergias.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe ingresar las alergias (o escribir 'Ninguna')");
            return false;
        }

        if (observaciones.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe ingresar las observaciones de la consulta");
            return false;
        }

        if (tipoSangre.isEmpty() || tipoSangre.equals("Seleccione")) {
            JOptionPane.showMessageDialog(null, "Debe seleccionar el tipo de sangre");
            return false;
        }

        int idConsulta = obtenerSiguienteId();

        if (idConsulta == -1) {
            return false;
        }

        String sql = "INSERT INTO \"Proyecto\".\"Consulta\" "
                + "(\"id_Consulta\",\"id_Paciente\",\"id_Cedula\",\"fecha_Entrada\","
                + "\"fecha_Salida\",\"hora_Entrada\",\"hora_Salida\",observaciones,"
                + "diagnostico,alergias,sintomas,\"tipoSangre\") "
                + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";

        try (Connection conexion = conexionBD.conectar()) {
            if (conexion == null) {
                JOptionPane.showMessageDialog(null, "No fue posible conectar con la base de datos");
                return false;
            }

            try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
                sentencia.setInt(1, idConsulta);
                sentencia.setInt(2, idPaciente);
                sentencia.setInt(3, idCedula);
                sentencia.setDate(4, java.sql.Date.valueOf(LocalDate.now()));
                sentencia.setNull(5, Types.DATE);
                sentencia.setTime(6, java.sql.Time.valueOf(LocalTime.now()));
                sentencia.setNull(7, Types.TIME);
                sentencia.setString(8, observaciones);
                sentencia.setString(9, diagnostico);
                sentencia.setString(10, alergias);
                sentencia.setString(11, sintomas);
                sentencia.setString(12, tipoSangre);

                return sentencia.executeUpdate() > 0;
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al registrar consulta:\n" + e.getMessage());
            return false;
        }
    }

    private int obtenerSiguienteId() {
        String sql = "SELECT COALESCE(MAX(\"id_Consulta\"),0)+1 AS siguiente_id "
                + "FROM \"Proyecto\".\"Consulta\"";

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
            JOptionPane.showMessageDialog(null, "Error al generar el ID de consulta:\n" + e.getMessage());
        }

        return -1;
    }

    public Consulta buscarConsulta(int idConsulta) {
        String sql = "SELECT * FROM \"Proyecto\".\"Consulta\" "
                + "WHERE \"id_Consulta\"=?";

        try (Connection conexion = conexionBD.conectar()) {
            if (conexion == null) {
                return null;
            }

            try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
                sentencia.setInt(1, idConsulta);

                try (ResultSet resultado = sentencia.executeQuery()) {
                    if (resultado.next()) {
                        return crearObjetoConsulta(resultado);
                    }
                }
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al buscar consulta:\n" + e.getMessage());
        }

        return null;
    }

    public boolean modificarConsulta(int idConsulta, String observaciones, String diagnostico, String alergias, String sintomas, String tipoSangre) {
        observaciones = observaciones.trim();
        diagnostico = diagnostico.trim();
        alergias = alergias.trim();
        sintomas = sintomas.trim();
        tipoSangre = tipoSangre.trim();

        // --- VALIDACIONES DE CAMPOS VACÍOS ---
        if (sintomas.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Los síntomas son obligatorios");
            return false;
        }

        if (diagnostico.isEmpty()) {
            JOptionPane.showMessageDialog(null, "El diagnóstico es obligatorio");
            return false;
        }

        if (alergias.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe especificar las alergias (o 'Ninguna')");
            return false;
        }

        if (observaciones.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Las observaciones son obligatorias");
            return false;
        }

        if (tipoSangre.isEmpty() || tipoSangre.equals("Seleccione")) {
            JOptionPane.showMessageDialog(null, "El tipo de sangre es obligatorio");
            return false;
        }

        String sql = "UPDATE \"Proyecto\".\"Consulta\" SET "
                + "observaciones=?,diagnostico=?,alergias=?,sintomas=?,\"tipoSangre\"=? "
                + "WHERE \"id_Consulta\"=?";

        try (Connection conexion = conexionBD.conectar()) {
            if (conexion == null) {
                return false;
            }

            try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
                sentencia.setString(1, observaciones);
                sentencia.setString(2, diagnostico);
                sentencia.setString(3, alergias);
                sentencia.setString(4, sintomas);
                sentencia.setString(5, tipoSangre);
                sentencia.setInt(6, idConsulta);

                int filas = sentencia.executeUpdate();

                if (filas == 0) {
                    JOptionPane.showMessageDialog(null, "No se encontró la consulta");
                    return false;
                }

                return true;
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al modificar consulta:\n" + e.getMessage());
            return false;
        }
    }

    public boolean registrarSalida(int idConsulta) {
        Consulta consulta = buscarConsulta(idConsulta);

        if (consulta == null) {
            JOptionPane.showMessageDialog(null, "No se encontró la consulta");
            return false;
        }

        if (consulta.getFechaSalida() != null) {
            JOptionPane.showMessageDialog(null, "La salida ya fue registrada");
            return false;
        }

        String sql = "UPDATE \"Proyecto\".\"Consulta\" SET "
                + "\"fecha_Salida\"=?,\"hora_Salida\"=? "
                + "WHERE \"id_Consulta\"=?";

        try (Connection conexion = conexionBD.conectar()) {
            if (conexion == null) {
                return false;
            }

            try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
                sentencia.setDate(1, java.sql.Date.valueOf(LocalDate.now()));
                sentencia.setTime(2, java.sql.Time.valueOf(LocalTime.now()));
                sentencia.setInt(3, idConsulta);

                return sentencia.executeUpdate() > 0;
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al registrar salida:\n" + e.getMessage());
            return false;
        }
    }

    public boolean eliminarConsulta(int idConsulta) {
        String sql = "DELETE FROM \"Proyecto\".\"Consulta\" "
                + "WHERE \"id_Consulta\"=?";

        try (Connection conexion = conexionBD.conectar()) {
            if (conexion == null) {
                return false;
            }

            try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
                sentencia.setInt(1, idConsulta);

                int filas = sentencia.executeUpdate();

                if (filas == 0) {
                    JOptionPane.showMessageDialog(null, "No se encontró la consulta");
                    return false;
                }

                return true;
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "No se pudo eliminar la consulta.\n"
                    + "Puede tener recetas relacionadas.\n\n"
                    + e.getMessage());
            return false;
        }
    }

    public ArrayList<Consulta> verConsultas() {
        ArrayList<Consulta> listaConsultas = new ArrayList<>();

        String sql = "SELECT * FROM \"Proyecto\".\"Consulta\" "
                + "ORDER BY \"id_Consulta\"";

        try (Connection conexion = conexionBD.conectar()) {
            if (conexion == null) {
                return listaConsultas;
            }

            try (PreparedStatement sentencia = conexion.prepareStatement(sql);
                 ResultSet resultado = sentencia.executeQuery()) {

                while (resultado.next()) {
                    listaConsultas.add(crearObjetoConsulta(resultado));
                }
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al consultar consultas:\n" + e.getMessage());
        }

        return listaConsultas;
    }

    public ArrayList<Consulta> verConsultasPaciente(int idPaciente) {
        ArrayList<Consulta> consultasPaciente = new ArrayList<>();

        String sql = "SELECT * FROM \"Proyecto\".\"Consulta\" "
                + "WHERE \"id_Paciente\"=? "
                + "ORDER BY \"id_Consulta\"";

        try (Connection conexion = conexionBD.conectar()) {
            if (conexion == null) {
                return consultasPaciente;
            }

            try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
                sentencia.setInt(1, idPaciente);

                try (ResultSet resultado = sentencia.executeQuery()) {
                    while (resultado.next()) {
                        consultasPaciente.add(crearObjetoConsulta(resultado));
                    }
                }
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al consultar datos del paciente:\n" + e.getMessage());
        }

        return consultasPaciente;
    }

    public ArrayList<Consulta> verConsultasDoctor(int idCedula) {
        ArrayList<Consulta> consultasDoctor = new ArrayList<>();

        String sql = "SELECT * FROM \"Proyecto\".\"Consulta\" "
                + "WHERE \"id_Cedula\"=? "
                + "ORDER BY \"id_Consulta\"";

        try (Connection conexion = conexionBD.conectar()) {
            if (conexion == null) {
                return consultasDoctor;
            }

            try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
                sentencia.setInt(1, idCedula);

                try (ResultSet resultado = sentencia.executeQuery()) {
                    while (resultado.next()) {
                        consultasDoctor.add(crearObjetoConsulta(resultado));
                    }
                }
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al consultar datos del doctor:\n" + e.getMessage());
        }

        return consultasDoctor;
    }

    public int cantidadConsultas() {
        String sql = "SELECT COUNT(*) AS cantidad FROM \"Proyecto\".\"Consulta\"";

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
            JOptionPane.showMessageDialog(null, "Error al contar consultas:\n" + e.getMessage());
        }

        return 0;
    }

    private Consulta crearObjetoConsulta(ResultSet resultado) throws SQLException {
        LocalDate fechaSalida = null;
        LocalTime horaSalida = null;

        if (resultado.getDate("fecha_Salida") != null) {
            fechaSalida = resultado.getDate("fecha_Salida").toLocalDate();
        }

        if (resultado.getTime("hora_Salida") != null) {
            horaSalida = resultado.getTime("hora_Salida").toLocalTime();
        }

        return new Consulta(
                resultado.getInt("id_Consulta"),
                resultado.getInt("id_Paciente"),
                resultado.getInt("id_Cedula"),
                resultado.getDate("fecha_Entrada").toLocalDate(),
                fechaSalida,
                resultado.getTime("hora_Entrada").toLocalTime(),
                horaSalida,
                resultado.getString("observaciones"),
                resultado.getString("diagnostico"),
                resultado.getString("alergias"),
                resultado.getString("sintomas"),
                resultado.getString("tipoSangre")
        );
    }
}