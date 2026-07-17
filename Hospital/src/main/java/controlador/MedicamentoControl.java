package controlador;

import conexion.ConexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import modelo.Medicamento;

public class MedicamentoControl {

    private ConexionBD conexionBD;

    public MedicamentoControl() {
        conexionBD=new ConexionBD();
    }

    public boolean crearMedicamento(String nombreMedicamento,String dosis,String descripcion) {
        nombreMedicamento=nombreMedicamento.trim();
        dosis=dosis.trim();
        descripcion=descripcion.trim();

        if(nombreMedicamento.isEmpty()) {
            JOptionPane.showMessageDialog(null,"El nombre del medicamento es obligatorio");
            return false;
        }

        if(dosis.isEmpty()) {
            JOptionPane.showMessageDialog(null,"La dosis es obligatoria");
            return false;
        }

        if(descripcion.isEmpty()) {
            JOptionPane.showMessageDialog(null,"La descripción es obligatoria");
            return false;
        }

        String sql="INSERT INTO \"Proyecto\".\"Medicamento\" "
                +"(\"nombreMedicamento\",\"descripcion\",\"dosis\") "
                +"VALUES(?,?,?)";

        Connection conexion=conexionBD.conectar();

        if(conexion==null) {
            JOptionPane.showMessageDialog(null,"No fue posible conectar con la base de datos");
            return false;
        }

        try(conexion;PreparedStatement sentencia=conexion.prepareStatement(sql)) {
            sentencia.setString(1,nombreMedicamento);
            sentencia.setString(2,descripcion);
            sentencia.setString(3,dosis);

            return sentencia.executeUpdate()>0;

        }catch(SQLException e) {
            JOptionPane.showMessageDialog(null,"Error al registrar medicamento:\n"+e.getMessage());
            return false;
        }
    }

    public Medicamento buscarMedicamento(int idMedicamento) {
        String sql="SELECT \"id_Medicamento\",\"nombreMedicamento\",\"descripcion\",\"dosis\" "
                +"FROM \"Proyecto\".\"Medicamento\" "
                +"WHERE \"id_Medicamento\"=?";

        Connection conexion=conexionBD.conectar();

        if(conexion==null) {
            return null;
        }

        try(conexion;PreparedStatement sentencia=conexion.prepareStatement(sql)) {
            sentencia.setInt(1,idMedicamento);

            try(ResultSet resultado=sentencia.executeQuery()) {
                if(resultado.next()) {
                    return new Medicamento(
                            resultado.getInt("id_Medicamento"),
                            resultado.getString("nombreMedicamento"),
                            resultado.getString("dosis"),
                            resultado.getString("descripcion")
                    );
                }
            }

        }catch(SQLException e) {
            JOptionPane.showMessageDialog(null,"Error al buscar medicamento:\n"+e.getMessage());
        }

        return null;
    }

    public boolean modificarMedicamento(int idMedicamento,String nombreMedicamento,String dosis,String descripcion) {
        nombreMedicamento=nombreMedicamento.trim();
        dosis=dosis.trim();
        descripcion=descripcion.trim();

        if(nombreMedicamento.isEmpty()||dosis.isEmpty()||descripcion.isEmpty()) {
            JOptionPane.showMessageDialog(null,"Complete todos los datos del medicamento");
            return false;
        }

        String sql="UPDATE \"Proyecto\".\"Medicamento\" SET "
                +"\"nombreMedicamento\"=?,"
                +"\"descripcion\"=?,"
                +"\"dosis\"=? "
                +"WHERE \"id_Medicamento\"=?";

        Connection conexion=conexionBD.conectar();

        if(conexion==null) {
            JOptionPane.showMessageDialog(null,"No fue posible conectar con la base de datos");
            return false;
        }

        try(conexion;PreparedStatement sentencia=conexion.prepareStatement(sql)) {
            sentencia.setString(1,nombreMedicamento);
            sentencia.setString(2,descripcion);
            sentencia.setString(3,dosis);
            sentencia.setInt(4,idMedicamento);

            int filas=sentencia.executeUpdate();

            if(filas==0) {
                JOptionPane.showMessageDialog(null,"No se encontró el medicamento");
                return false;
            }

            return true;

        }catch(SQLException e) {
            JOptionPane.showMessageDialog(null,"Error al modificar medicamento:\n"+e.getMessage());
            return false;
        }
    }

    public boolean eliminarMedicamento(int idMedicamento) {
        String sql="DELETE FROM \"Proyecto\".\"Medicamento\" "
                +"WHERE \"id_Medicamento\"=?";

        Connection conexion=conexionBD.conectar();

        if(conexion==null) {
            JOptionPane.showMessageDialog(null,"No fue posible conectar con la base de datos");
            return false;
        }

        try(conexion;PreparedStatement sentencia=conexion.prepareStatement(sql)) {
            sentencia.setInt(1,idMedicamento);

            int filas=sentencia.executeUpdate();

            if(filas==0) {
                JOptionPane.showMessageDialog(null,"No se encontró el medicamento");
                return false;
            }

            return true;

        }catch(SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "No se pudo eliminar el medicamento.\n"
                    +"Puede estar relacionado con una receta.\n\n"
                    +e.getMessage());
            return false;
        }
    }

    public ArrayList<Medicamento> verMedicamentos() {
        ArrayList<Medicamento> listaMedicamentos=new ArrayList<>();

        String sql="SELECT \"id_Medicamento\",\"nombreMedicamento\",\"descripcion\",\"dosis\" "
                +"FROM \"Proyecto\".\"Medicamento\" "
                +"ORDER BY \"id_Medicamento\"";

        Connection conexion=conexionBD.conectar();

        if(conexion==null) {
            return listaMedicamentos;
        }

        try(conexion;
            PreparedStatement sentencia=conexion.prepareStatement(sql);
            ResultSet resultado=sentencia.executeQuery()) {

            while(resultado.next()) {
                Medicamento medicamento=new Medicamento(
                        resultado.getInt("id_Medicamento"),
                        resultado.getString("nombreMedicamento"),
                        resultado.getString("dosis"),
                        resultado.getString("descripcion")
                );

                listaMedicamentos.add(medicamento);
            }

        }catch(SQLException e) {
            JOptionPane.showMessageDialog(null,"Error al consultar medicamentos:\n"+e.getMessage());
        }

        return listaMedicamentos;
    }

    public int cantidadMedicamentos() {
        String sql="SELECT COUNT(*) AS cantidad FROM \"Proyecto\".\"Medicamento\"";

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
            JOptionPane.showMessageDialog(null,"Error al contar medicamentos:\n"+e.getMessage());
        }

        return 0;
    }
}