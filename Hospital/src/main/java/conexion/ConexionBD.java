package conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {

    String url = "jdbc:postgresql://localhost:5432/Hospital?currentSchema=Proyecto";
    String usuario = "postgres";
    String contrasena = "24160536!";

    public Connection conectar() {

        try {
            Connection conexion =DriverManager.getConnection(url, usuario, contrasena);
            System.out.println("Conexión exitosa");
            return conexion;
        } catch (SQLException e) {
            System.out.println("Error al conectar: " + e.getMessage());
            return null;
        }
    }
}