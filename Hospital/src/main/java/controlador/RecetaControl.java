package controlador;

import conexion.ConexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalTime;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import modelo.Receta;

public class RecetaControl {

    private ConsultaControl consultaControl;
    private MedicamentoControl medicamentoControl;
    private ConexionBD conexionBD;

    public RecetaControl(
            ConsultaControl consultaControl,
            MedicamentoControl medicamentoControl) {

        this.consultaControl = consultaControl;
        this.medicamentoControl = medicamentoControl;
        conexionBD = new ConexionBD();
    }

    // CREATE
    public boolean crearReceta(
            int idMedicamento,
            int idConsulta,
            String descripcion,
            LocalTime horaSuministrar) {

        if (medicamentoControl.buscarMedicamento(idMedicamento) == null) {
            JOptionPane.showMessageDialog(
                    null,
                    "El medicamento seleccionado no existe"
            );
            return false;
        }

        if (consultaControl.buscarConsulta(idConsulta) == null) {
            JOptionPane.showMessageDialog(
                    null,
                    "La consulta seleccionada no existe"
            );
            return false;
        }

        descripcion = descripcion.trim();

        if (descripcion.isEmpty()) {
            JOptionPane.showMessageDialog(
                    null,
                    "La descripción es obligatoria"
            );
            return false;
        }

        if (horaSuministrar == null) {
            JOptionPane.showMessageDialog(
                    null,
                    "La hora de suministro es obligatoria"
            );
            return false;
        }

        int siguienteId = obtenerSiguienteId();

        if (siguienteId == -1) {
            return false;
        }

        /*
         * IMPORTANTE:
         * En tu base de datos la columna aparece como "descripcion "
         * con un espacio al final. Por eso debe escribirse exactamente así.
         */
        String sql
                = "INSERT INTO \"Proyecto\".\"Receta\" "
                + "(\"id_Receta\", \"id_Medicamento\", "
                + "\"id_Consulta\", \"descripcion \", "
                + "\"horaSuministrar\") "
                + "VALUES (?, ?, ?, ?, ?)";

        try (Connection conexion = conexionBD.conectar()) {

            if (conexion == null) {
                JOptionPane.showMessageDialog(
                        null,
                        "No fue posible conectar con la base de datos"
                );
                return false;
            }

            try (PreparedStatement sentencia
                    = conexion.prepareStatement(sql)) {

                sentencia.setInt(1, siguienteId);
                sentencia.setInt(2, idMedicamento);
                sentencia.setInt(3, idConsulta);
                sentencia.setString(4, descripcion);
                sentencia.setTime(
                        5,
                        java.sql.Time.valueOf(horaSuministrar)
                );

                int filasAfectadas = sentencia.executeUpdate();

                return filasAfectadas > 0;
            }

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    null,
                    "Error al registrar la receta:\n"
                    + e.getMessage()
            );

            return false;
        }
    }

    private int obtenerSiguienteId() {

        String sql
                = "SELECT COALESCE(MAX(\"id_Receta\"), 0) + 1 "
                + "AS siguiente_id "
                + "FROM \"Proyecto\".\"Receta\"";

        try (Connection conexion = conexionBD.conectar()) {

            if (conexion == null) {
                JOptionPane.showMessageDialog(
                        null,
                        "No fue posible conectar con la base de datos"
                );
                return -1;
            }

            try (
                    PreparedStatement sentencia
                    = conexion.prepareStatement(sql);
                    ResultSet resultado
                    = sentencia.executeQuery()
            ) {

                if (resultado.next()) {
                    return resultado.getInt("siguiente_id");
                }
            }

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    null,
                    "Error al generar el ID de la receta:\n"
                    + e.getMessage()
            );
        }

        return -1;
    }

    // READ: buscar una receta
    public Receta buscarReceta(int idReceta) {

        String sql
                = "SELECT \"id_Receta\", \"id_Medicamento\", "
                + "\"id_Consulta\", \"descripcion \", "
                + "\"horaSuministrar\" "
                + "FROM \"Proyecto\".\"Receta\" "
                + "WHERE \"id_Receta\" = ?";

        try (Connection conexion = conexionBD.conectar()) {

            if (conexion == null) {
                return null;
            }

            try (PreparedStatement sentencia
                    = conexion.prepareStatement(sql)) {

                sentencia.setInt(1, idReceta);

                try (ResultSet resultado
                        = sentencia.executeQuery()) {

                    if (resultado.next()) {

                        LocalTime hora = null;

                        if (resultado.getTime("horaSuministrar") != null) {
                            hora = resultado
                                    .getTime("horaSuministrar")
                                    .toLocalTime();
                        }

                        return new Receta(
                                resultado.getInt("id_Receta"),
                                resultado.getInt("id_Medicamento"),
                                resultado.getInt("id_Consulta"),
                                resultado.getString("descripcion "),
                                hora
                        );
                    }
                }
            }

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    null,
                    "Error al buscar la receta:\n"
                    + e.getMessage()
            );
        }

        return null;
    }

    // UPDATE
    public boolean modificarReceta(
            int idReceta,
            int idMedicamento,
            int idConsulta,
            String descripcion,
            String horaSuministrar) {

        if (buscarReceta(idReceta) == null) {
            JOptionPane.showMessageDialog(
                    null,
                    "No se encontró la receta"
            );
            return false;
        }

        if (medicamentoControl.buscarMedicamento(idMedicamento) == null) {
            JOptionPane.showMessageDialog(
                    null,
                    "El medicamento no existe"
            );
            return false;
        }

        if (consultaControl.buscarConsulta(idConsulta) == null) {
            JOptionPane.showMessageDialog(
                    null,
                    "La consulta no existe"
            );
            return false;
        }

        descripcion = descripcion.trim();
        horaSuministrar = horaSuministrar.trim();

        if (descripcion.isEmpty()) {
            JOptionPane.showMessageDialog(
                    null,
                    "La descripción es obligatoria"
            );
            return false;
        }

        if (horaSuministrar.isEmpty()) {
            JOptionPane.showMessageDialog(
                    null,
                    "La hora de suministro es obligatoria"
            );
            return false;
        }

        LocalTime horaConvertida;

        try {
            horaConvertida = LocalTime.parse(horaSuministrar);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    null,
                    "La hora debe tener el formato HH:mm"
            );
            return false;
        }

        String sql
                = "UPDATE \"Proyecto\".\"Receta\" SET "
                + "\"id_Medicamento\" = ?, "
                + "\"id_Consulta\" = ?, "
                + "\"descripcion \" = ?, "
                + "\"horaSuministrar\" = ? "
                + "WHERE \"id_Receta\" = ?";

        try (Connection conexion = conexionBD.conectar()) {

            if (conexion == null) {
                JOptionPane.showMessageDialog(
                        null,
                        "No fue posible conectar con la base de datos"
                );
                return false;
            }

            try (PreparedStatement sentencia
                    = conexion.prepareStatement(sql)) {

                sentencia.setInt(1, idMedicamento);
                sentencia.setInt(2, idConsulta);
                sentencia.setString(3, descripcion);
                sentencia.setTime(
                        4,
                        java.sql.Time.valueOf(horaConvertida)
                );
                sentencia.setInt(5, idReceta);

                int filasAfectadas = sentencia.executeUpdate();

                if (filasAfectadas == 0) {
                    JOptionPane.showMessageDialog(
                            null,
                            "No se encontró la receta"
                    );
                    return false;
                }

                return true;
            }

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    null,
                    "Error al modificar la receta:\n"
                    + e.getMessage()
            );

            return false;
        }
    }

    // DELETE
    public boolean eliminarReceta(int idReceta) {

        String sql
                = "DELETE FROM \"Proyecto\".\"Receta\" "
                + "WHERE \"id_Receta\" = ?";

        try (Connection conexion = conexionBD.conectar()) {

            if (conexion == null) {
                JOptionPane.showMessageDialog(
                        null,
                        "No fue posible conectar con la base de datos"
                );
                return false;
            }

            try (PreparedStatement sentencia
                    = conexion.prepareStatement(sql)) {

                sentencia.setInt(1, idReceta);

                int filasAfectadas = sentencia.executeUpdate();

                if (filasAfectadas == 0) {
                    JOptionPane.showMessageDialog(
                            null,
                            "No se encontró la receta"
                    );
                    return false;
                }

                return true;
            }

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    null,
                    "Error al eliminar la receta:\n"
                    + e.getMessage()
            );

            return false;
        }
    }

    // READ: mostrar todas las recetas
    public ArrayList<Receta> verRecetas() {

        ArrayList<Receta> listaRecetas
                = new ArrayList<>();

        String sql
                = "SELECT \"id_Receta\", \"id_Medicamento\", "
                + "\"id_Consulta\", \"descripcion \", "
                + "\"horaSuministrar\" "
                + "FROM \"Proyecto\".\"Receta\" "
                + "ORDER BY \"id_Receta\"";

        try (Connection conexion = conexionBD.conectar()) {

            if (conexion == null) {
                return listaRecetas;
            }

            try (
                    PreparedStatement sentencia
                    = conexion.prepareStatement(sql);
                    ResultSet resultado
                    = sentencia.executeQuery()
            ) {

                while (resultado.next()) {

                    LocalTime hora = null;

                    if (resultado.getTime("horaSuministrar") != null) {
                        hora = resultado
                                .getTime("horaSuministrar")
                                .toLocalTime();
                    }

                    Receta receta = new Receta(
                            resultado.getInt("id_Receta"),
                            resultado.getInt("id_Medicamento"),
                            resultado.getInt("id_Consulta"),
                            resultado.getString("descripcion "),
                            hora
                    );

                    listaRecetas.add(receta);
                }
            }

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    null,
                    "Error al consultar las recetas:\n"
                    + e.getMessage()
            );
        }

        return listaRecetas;
    }

    // READ: recetas de una consulta específica
    public ArrayList<Receta> verRecetasConsulta(
            int idConsulta) {

        ArrayList<Receta> recetasConsulta
                = new ArrayList<>();

        String sql
                = "SELECT \"id_Receta\", \"id_Medicamento\", "
                + "\"id_Consulta\", \"descripcion \", "
                + "\"horaSuministrar\" "
                + "FROM \"Proyecto\".\"Receta\" "
                + "WHERE \"id_Consulta\" = ? "
                + "ORDER BY \"id_Receta\"";

        try (Connection conexion = conexionBD.conectar()) {

            if (conexion == null) {
                return recetasConsulta;
            }

            try (PreparedStatement sentencia
                    = conexion.prepareStatement(sql)) {

                sentencia.setInt(1, idConsulta);

                try (ResultSet resultado
                        = sentencia.executeQuery()) {

                    while (resultado.next()) {

                        LocalTime hora = null;

                        if (resultado.getTime(
                                "horaSuministrar") != null) {

                            hora = resultado
                                    .getTime("horaSuministrar")
                                    .toLocalTime();
                        }

                        Receta receta = new Receta(
                                resultado.getInt("id_Receta"),
                                resultado.getInt("id_Medicamento"),
                                resultado.getInt("id_Consulta"),
                                resultado.getString("descripcion "),
                                hora
                        );

                        recetasConsulta.add(receta);
                    }
                }
            }

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    null,
                    "Error al consultar las recetas:\n"
                    + e.getMessage()
            );
        }

        return recetasConsulta;
    }

    public int cantidadRecetas() {

        String sql
                = "SELECT COUNT(*) AS cantidad "
                + "FROM \"Proyecto\".\"Receta\"";

        try (Connection conexion = conexionBD.conectar()) {

            if (conexion == null) {
                return 0;
            }

            try (
                    PreparedStatement sentencia
                    = conexion.prepareStatement(sql);
                    ResultSet resultado
                    = sentencia.executeQuery()
            ) {

                if (resultado.next()) {
                    return resultado.getInt("cantidad");
                }
            }

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    null,
                    "Error al contar las recetas:\n"
                    + e.getMessage()
            );
        }

        return 0;
    }
}