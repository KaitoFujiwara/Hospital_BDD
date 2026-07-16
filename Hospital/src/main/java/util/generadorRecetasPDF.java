package util;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import modelo.Consulta;
import modelo.Doctor;
import modelo.Medicamento;
import modelo.Paciente;
import modelo.Receta;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;

public class generadorRecetasPDF {

    public static File generar(Receta receta, Consulta consulta,
            Paciente paciente, Doctor doctor,
            Medicamento medicamento) throws IOException {

        File carpeta = new File("recetas");

        if (!carpeta.exists()) {
            carpeta.mkdirs();
        }

        File archivo = new File(
                carpeta,
                "Receta_" + receta.getIdReceta() + ".pdf"
        );

        try (PDDocument documento = new PDDocument()) {
            PDPage pagina = new PDPage(PDRectangle.A4);
            documento.addPage(pagina);

            PDType1Font normal = new PDType1Font(
                    Standard14Fonts.FontName.HELVETICA
            );

            PDType1Font negrita = new PDType1Font(
                    Standard14Fonts.FontName.HELVETICA_BOLD
            );

            try (PDPageContentStream contenido =
                    new PDPageContentStream(documento, pagina)) {

                float y = 790;

                y = escribir(contenido, negrita, 20,
                        "HOSPITAL GENERAL", 190, y);

                y = escribir(contenido, negrita, 15,
                        "RECETA MEDICA", 225, y);

                y -= 20;

                y = escribir(contenido, normal, 11,
                        "Fecha: " + LocalDate.now(), 60, y);

                y = escribir(contenido, normal, 11,
                        "Numero de receta: "
                        + receta.getIdReceta(), 60, y);

                y -= 10;

                y = escribir(contenido, negrita, 12,
                        "DATOS DEL PACIENTE", 60, y);

                y = escribir(contenido, normal, 11,
                        "Nombre: " + paciente.getNombreCompleto(), 60, y);

                y = escribir(contenido, normal, 11,
                        "Edad: " + paciente.getEdad(), 60, y);

                y = escribir(contenido, normal, 11,
                        "Genero: " + paciente.getGenero(), 60, y);

                y = escribir(contenido, normal, 11,
                        "Peso: " + paciente.getPeso(), 60, y);

                y -= 10;

                y = escribir(contenido, negrita, 12,
                        "DOCTOR RESPONSABLE", 60, y);

                y = escribir(contenido, normal, 11,
                        "Doctor: " + doctor.getNombreCompleto(), 60, y);

                y = escribir(contenido, normal, 11,
                        "Cedula: " + doctor.getIdCedula(), 60, y);

                y = escribir(contenido, normal, 11,
                        "Especialidad: "
                        + doctor.getEspecialidad(), 60, y);

                y -= 10;

                y = escribir(contenido, negrita, 12,
                        "CONSULTA", 60, y);

                y = escribir(contenido, normal, 11,
                        "Sintomas: " + consulta.getSintomas(), 60, y);

                y = escribir(contenido, normal, 11,
                        "Diagnostico: "
                        + consulta.getDiagnostico(), 60, y);

                y = escribir(contenido, normal, 11,
                        "Alergias: " + consulta.getAlergias(), 60, y);

                y -= 10;

                y = escribir(contenido, negrita, 12,
                        "MEDICAMENTO", 60, y);

                y = escribir(contenido, normal, 11,
                        "Nombre: "
                        + medicamento.getNombreMedicamento(), 60, y);

                y = escribir(contenido, normal, 11,
                        "Dosis: " + medicamento.getDosis(), 60, y);

                y = escribir(contenido, normal, 11,
                        "Descripcion: "
                        + medicamento.getDescripcion(), 60, y);

                y = escribir(contenido, normal, 11,
                        "Hora de suministro: "
                        + receta.getHoraSuministrar(), 60, y);

                y = escribir(contenido, normal, 11,
                        "Indicaciones: "
                        + receta.getDescripcion(), 60, y);

                y -= 50;

                y = escribir(contenido, normal, 11,
                        "______________________________", 200, y);

                escribir(contenido, normal, 11,
                        "Firma del doctor", 235, y);
            }

            documento.save(archivo);
        }

        return archivo;
    }

    private static float escribir(PDPageContentStream contenido,
            PDType1Font fuente, float tamano,
            String texto, float x, float y) throws IOException {

        contenido.beginText();
        contenido.setFont(fuente, tamano);
        contenido.newLineAtOffset(x, y);
        contenido.showText(limpiar(texto));
        contenido.endText();

        return y - 18;
    }

    private static String limpiar(String texto) {
        if (texto == null || texto.isBlank()) {
            return "Sin informacion";
        }

        return texto
                .replace("á", "a").replace("é", "e")
                .replace("í", "i").replace("ó", "o")
                .replace("ú", "u").replace("ñ", "n")
                .replace("Á", "A").replace("É", "E")
                .replace("Í", "I").replace("Ó", "O")
                .replace("Ú", "U").replace("Ñ", "N");
    }

    public static void abrirPDF(File archivo) throws IOException {
        if (Desktop.isDesktopSupported()) {
            Desktop.getDesktop().open(archivo);
        }
    }
}