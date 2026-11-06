package logica;

import modelo.Turno;

import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.*;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ComprobanteTurnoPdf {
    private static final Color TEXTO = new Color(23, 50, 77);
    private static final Color VERDE = new Color(15, 118, 110);
    private static final Color GRIS = new Color(92, 110, 122);

    public void generar(Turno turno, Path destino, BufferedImage logo) throws IOException {
        if (turno == null || destino == null)
            throw new IllegalArgumentException("Falta el turno o el destino del PDF.");
        Path archivo = destino.toAbsolutePath();
        Path temporal = Files.createTempFile(archivo.getParent(), ".comprobante-", ".pdf");
        try {
            try (PDDocument doc = new PDDocument()) {
                PDPage pagina = new PDPage(PDRectangle.A4);
                doc.addPage(pagina);
                PDFont normal = fuente(doc, false), negrita = fuente(doc, true);
                doc.getDocumentInformation().setTitle("Comprobante de turno " + turno.getId());
                doc.getDocumentInformation().setAuthor("Clínica - Gestión de turnos");
                try (PDPageContentStream s = new PDPageContentStream(doc, pagina)) {
                    if (logo != null)
                        s.drawImage(LosslessFactory.createFromImage(doc, logo), 48, 744, 48, 48);
                    texto(s, negrita, 20, TEXTO, 110, 772, "Clínica");
                    texto(s, normal, 11, GRIS, 110, 752, "Gestión de turnos");
                    texto(s, negrita, 23, TEXTO, 48, 705, "Comprobante de turno");
                    texto(
                            s,
                            normal,
                            11,
                            GRIS,
                            48,
                            681,
                            "Reserva N° "
                                    + turno.getId()
                                    + "  |  Estado: "
                                    + turno.getEstado().name());
                    s.setNonStrokingColor(new Color(235, 247, 245));
                    s.addRect(48, 572, 499, 80);
                    s.fill();
                    texto(s, negrita, 10, VERDE, 65, 630, "FECHA");
                    texto(
                            s,
                            negrita,
                            21,
                            TEXTO,
                            65,
                            600,
                            turno.getFecha().format(DateTimeFormatter.ofPattern("dd/MM/uuuu")));
                    texto(s, negrita, 10, VERDE, 350, 630, "HORA");
                    texto(
                            s,
                            negrita,
                            21,
                            TEXTO,
                            350,
                            600,
                            turno.getHora().format(DateTimeFormatter.ofPattern("HH:mm")));
                    float y = 535;
                    y = campo(s, normal, negrita, y, "PACIENTE", turno.getPaciente().getNombre());
                    y = campo(s, normal, negrita, y, "DNI", turno.getPaciente().getDni());
                    y = campo(s, normal, negrita, y, "MÉDICO", turno.getMedico().getNombre());
                    y = campo(s, normal, negrita, y, "MATRÍCULA", turno.getMedico().getMatricula());
                    y =
                            campo(
                                    s,
                                    normal,
                                    negrita,
                                    y,
                                    "ESPECIALIDAD",
                                    turno.getMedico().getEspecialidad());
                    campo(s, normal, negrita, y, "CONSULTORIO", turno.getMedico().getConsultorio());
                    s.setStrokingColor(new Color(220, 230, 234));
                    s.moveTo(48, 85);
                    s.lineTo(547, 85);
                    s.stroke();
                    texto(
                            s,
                            normal,
                            9,
                            GRIS,
                            48,
                            64,
                            "Emitido el "
                                    + LocalDateTime.now()
                                            .format(
                                                    DateTimeFormatter.ofPattern(
                                                            "dd/MM/uuuu HH:mm")));
                    texto(
                            s,
                            normal,
                            9,
                            GRIS,
                            48,
                            48,
                            "Conservá este comprobante para consultar los datos de tu reserva.");
                }
                doc.save(temporal.toFile());
            }
            // El archivo de destino solo se reemplaza cuando el PDF quedó completo.
            try {
                Files.move(
                        temporal,
                        archivo,
                        StandardCopyOption.ATOMIC_MOVE,
                        StandardCopyOption.REPLACE_EXISTING);
            } catch (AtomicMoveNotSupportedException e) {
                Files.move(temporal, archivo, StandardCopyOption.REPLACE_EXISTING);
            }
        } finally {
            Files.deleteIfExists(temporal);
        }
    }

    private PDFont fuente(PDDocument doc, boolean negrita) throws IOException {
        Path arial =
                Path.of(
                        System.getenv().getOrDefault("WINDIR", "C:/Windows"),
                        "Fonts",
                        negrita ? "arialbd.ttf" : "arial.ttf");
        if (Files.isRegularFile(arial)) return PDType0Font.load(doc, arial.toFile());
        return new PDType1Font(
                negrita
                        ? Standard14Fonts.FontName.HELVETICA_BOLD
                        : Standard14Fonts.FontName.HELVETICA);
    }

    private String seguro(PDFont fuente, String valor) throws IOException {
        StringBuilder resultado = new StringBuilder();
        for (int cp : valor.codePoints().toArray()) {
            String letra = new String(Character.toChars(cp));
            if (Character.isISOControl(cp)) {
                resultado.append(' ');
                continue;
            }
            try {
                fuente.encode(letra);
                resultado.append(letra);
            } catch (IllegalArgumentException e) {
                resultado.append('?');
            }
        }
        return resultado.toString();
    }

    private void texto(
            PDPageContentStream s, PDFont f, float tam, Color color, float x, float y, String valor)
            throws IOException {
        s.beginText();
        s.setFont(f, tam);
        s.setNonStrokingColor(color);
        s.newLineAtOffset(x, y);
        s.showText(seguro(f, valor));
        s.endText();
    }

    private float campo(
            PDPageContentStream s,
            PDFont normal,
            PDFont negrita,
            float y,
            String etiqueta,
            String valor)
            throws IOException {
        texto(s, negrita, 9, GRIS, 48, y, etiqueta);
        y -= 18;
        String limpio = seguro(normal, valor);
        ArrayList<String> lineas = new ArrayList<String>();
        String linea = "";
        for (String palabra : limpio.split("\\s+")) {
            String candidato = linea.isEmpty() ? palabra : linea + " " + palabra;
            if (normal.getStringWidth(candidato) / 1000 * 11 > 499 && !linea.isEmpty()) {
                lineas.add(linea);
                linea = palabra;
            } else linea = candidato;
            while (normal.getStringWidth(linea) / 1000 * 11 > 499) {
                int corte = linea.length() - 1;
                while (corte > 1
                        && normal.getStringWidth(linea.substring(0, corte)) / 1000 * 11 > 499)
                    corte--;
                lineas.add(linea.substring(0, corte));
                linea = linea.substring(corte);
            }
        }
        lineas.add(linea);
        for (String l : lineas) {
            texto(s, normal, 11, TEXTO, 48, y, l);
            y -= 15;
        }
        return y - 13;
    }
}
