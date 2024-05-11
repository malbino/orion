/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.malbino.orion.servlets;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.malbino.orion.entities.Egresado;
import org.malbino.orion.util.Conversiones;
import org.malbino.orion.util.Fecha;
import org.malbino.orion.util.NumberToLetterConverter;

/**
 *
 * @author tincho
 */
@WebServlet(name = "CertificadoEgreso", urlPatterns = {"/titulacion/egresados/CertificadoEgreso"})
public class CertificadoEgreso extends HttpServlet {

    private static final String CONTENIDO_PDF = "application/pdf";

    private static final Font NORMAL = FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLDITALIC, BaseColor.BLACK);
    private static final Font PEQUEÑA = FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLDITALIC, BaseColor.BLACK);

    //8.5 x 13 pulgadas (1 pulgada = 72 puntos)
    private static final Rectangle OFICIO_FOLIO = new Rectangle(612, 936);
    private static final int MARGEN_IZQUIERDO = -40;
    private static final int MARGEN_DERECHO = -40;
    private static final int MARGEN_SUPERIOR = 20;
    private static final int MARGEN_INFERIOR = 20;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.generarPDF(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.generarPDF(request, response);
    }

    public void generarPDF(HttpServletRequest request, HttpServletResponse response) {
        Egresado egresado = (Egresado) request.getSession().getAttribute("egresado");
        Date fecha = (Date) request.getSession().getAttribute("fecha");

        if (egresado != null && fecha != null) {

            try {
                response.setContentType(CONTENIDO_PDF);

                Document document = new Document(OFICIO_FOLIO, MARGEN_IZQUIERDO, MARGEN_DERECHO, MARGEN_SUPERIOR, MARGEN_INFERIOR);
                PdfWriter.getInstance(document, response.getOutputStream());

                document.open();

                document.add(contenido(egresado, fecha));

                document.close();
            } catch (IOException | DocumentException ex) {

            }
        }
    }

    public PdfPTable contenido(Egresado egresado, Date fecha) throws UnsupportedEncodingException, BadElementException, IOException {
        PdfPTable table = new PdfPTable(60);

        PdfPCell cell = new PdfPCell(new Phrase(" ", NORMAL));
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setFixedHeight(Conversiones.mmToPtos(66));
        cell.setColspan(60);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(egresado.getCarrera().getCampus().getInstituto().getNombreRegulador(), NORMAL));
        cell.setVerticalAlignment(PdfPCell.ALIGN_BOTTOM);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setFixedHeight(Conversiones.mmToPtos(14));
        cell.setColspan(60);
        cell.setPaddingLeft(120);
        cell.setPaddingRight(120);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(egresado.getCarrera().getCampus().getInstituto().getResolucionMinisterial(), NORMAL));
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setFixedHeight(Conversiones.mmToPtos(7));
        cell.setColspan(60);
        cell.setPaddingLeft(190);
        table.addCell(cell);

        //nombre egresado
        if (egresado.getEstudiante().getSegundoApellido() != null && !egresado.getEstudiante().getSegundoApellido().isEmpty()) {
            cell = new PdfPCell(new Phrase(egresado.getEstudiante().getPrimerApellido(), NORMAL));
            cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setFixedHeight(Conversiones.mmToPtos(17));
            cell.setPaddingLeft(50f);
            cell.setColspan(20);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(egresado.getEstudiante().getSegundoApellido(), NORMAL));
            cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setFixedHeight(Conversiones.mmToPtos(17));
            cell.setColspan(20);
            table.addCell(cell);
        } else {
            cell = new PdfPCell(new Phrase("", NORMAL));
            cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setFixedHeight(Conversiones.mmToPtos(17));
            cell.setPaddingLeft(50f);
            cell.setColspan(20);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(egresado.getEstudiante().getPrimerApellido(), NORMAL));
            cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setFixedHeight(Conversiones.mmToPtos(17));
            cell.setColspan(20);
            table.addCell(cell);
        }

        cell = new PdfPCell(new Phrase(egresado.getEstudiante().getNombre(), NORMAL));
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setFixedHeight(Conversiones.mmToPtos(17));
        cell.setColspan(20);
        table.addCell(cell);

        // nacimiento
        cell = new PdfPCell(new Phrase(egresado.getEstudiante().getNacionalidad().toUpperCase(), PEQUEÑA));
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setFixedHeight(Conversiones.mmToPtos(9));
        cell.setColspan(30);
        cell.setPaddingLeft(150f);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(egresado.getEstudiante().getLugarNacimiento().toUpperCase(), PEQUEÑA));
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setFixedHeight(Conversiones.mmToPtos(9));
        cell.setColspan(30);
        cell.setPaddingLeft(120f);
        table.addCell(cell);

        // fecha de nacimiento
        cell = new PdfPCell(new Phrase(Fecha.formatearFecha_dd(egresado.getEstudiante().getFechaNacimiento()), PEQUEÑA));
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setFixedHeight(Conversiones.mmToPtos(6));
        cell.setColspan(20);
        cell.setPaddingLeft(90f);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(Fecha.formatearFecha_MMMM(egresado.getEstudiante().getFechaNacimiento()).toUpperCase(), PEQUEÑA));
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setFixedHeight(Conversiones.mmToPtos(6));
        cell.setColspan(20);
        cell.setPaddingLeft(70f);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(Fecha.formatearFecha_yyyy(egresado.getEstudiante().getFechaNacimiento()), PEQUEÑA));
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setFixedHeight(Conversiones.mmToPtos(6));
        cell.setColspan(20);
        cell.setPaddingLeft(30f);
        table.addCell(cell);

        //ci
        cell = new PdfPCell(new Phrase(egresado.getEstudiante().dniLugar(), PEQUEÑA));
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setFixedHeight(Conversiones.mmToPtos(7));
        cell.setColspan(60);
        cell.setPaddingLeft(100f);
        table.addCell(cell);

        //carrera
        cell = new PdfPCell(new Phrase(egresado.getCarrera().getNombre(), NORMAL));
        cell.setVerticalAlignment(PdfPCell.ALIGN_BOTTOM);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setFixedHeight(Conversiones.mmToPtos(44));
        cell.setColspan(60);
        cell.setPaddingLeft(30f);
        table.addCell(cell);

        //nivel academico
        cell = new PdfPCell(new Phrase(egresado.getCarrera().getNivelAcademico().getNombre(), NORMAL));
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setFixedHeight(Conversiones.mmToPtos(14));
        cell.setColspan(60);
        cell.setPaddingLeft(30f);
        table.addCell(cell);

        //fecha
        cell = new PdfPCell(new Phrase(egresado.getCarrera().getCampus().getCiudad(), PEQUEÑA));
        cell.setVerticalAlignment(PdfPCell.ALIGN_BOTTOM);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setFixedHeight(Conversiones.mmToPtos(34));
        cell.setColspan(30);
        cell.setPaddingLeft(200f);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(Fecha.formatearFecha_dd(fecha), PEQUEÑA));
        cell.setVerticalAlignment(PdfPCell.ALIGN_BOTTOM);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setFixedHeight(Conversiones.mmToPtos(34));
        cell.setColspan(30);
        cell.setPaddingLeft(25f);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(Fecha.formatearFecha_MMMM(fecha).toUpperCase(), PEQUEÑA));
        cell.setVerticalAlignment(PdfPCell.ALIGN_BOTTOM);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setFixedHeight(Conversiones.mmToPtos(11));
        cell.setColspan(20);
        cell.setPaddingLeft(12f);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(Fecha.formatearFecha_yyyy(fecha), PEQUEÑA));
        cell.setVerticalAlignment(PdfPCell.ALIGN_BOTTOM);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setFixedHeight(Conversiones.mmToPtos(11));
        cell.setColspan(40);
        table.addCell(cell);

        return table;
    }

}
