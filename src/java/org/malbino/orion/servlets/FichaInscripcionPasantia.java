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
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.itextpdf.text.Chunk;
import org.malbino.orion.entities.NotaPasantia;
import org.malbino.orion.facades.NotaPasantiaFacade;
import org.malbino.orion.util.Fecha;

/**
 *
 * @author tincho
 */
@WebServlet(name = "FichaInscripcionPasantia", urlPatterns = {"/inscripciones/FichaInscripcionPasantia"})
public class FichaInscripcionPasantia extends HttpServlet {

    private static final String CONTENIDO_PDF = "application/pdf";

    private static final Font TITULO = FontFactory.getFont(FontFactory.HELVETICA, 14, Font.NORMAL, BaseColor.BLACK);
    private static final Font SUBTITULO = FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL, BaseColor.WHITE);
    private static final Font NEGRITA = FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD, BaseColor.BLACK);
    private static final Font NORMAL = FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK);
    private static final Font BOLDITALIC = FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLDITALIC, BaseColor.BLACK);
    private static final Font ITALIC = FontFactory.getFont(FontFactory.HELVETICA, 10, Font.ITALIC, BaseColor.BLACK);

    private static final int MARGEN_IZQUIERDO = -20;
    private static final int MARGEN_DERECHO = -20;
    private static final int MARGEN_SUPERIOR = 40;
    private static final int MARGEN_INFERIOR = 40;

    @EJB
    NotaPasantiaFacade notaPasantiaFacade;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.generarPDF(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        this.generarPDF(request, response);
    }

    public void generarPDF(HttpServletRequest request, HttpServletResponse response) {
        Integer id_notapasantia = (Integer) request.getSession().getAttribute("id_notapasantia");

        if (id_notapasantia != null) {
            NotaPasantia notaPasantia = notaPasantiaFacade.find(id_notapasantia);
            try {
                response.setContentType(CONTENIDO_PDF);

                Document document = new Document(PageSize.LETTER, MARGEN_IZQUIERDO, MARGEN_DERECHO, MARGEN_SUPERIOR, MARGEN_INFERIOR);
                PdfWriter.getInstance(document, response.getOutputStream());

                document.open();

                document.add(titulo(notaPasantia));
                document.add(contenido(notaPasantia));
                document.add(firma(notaPasantia));

                document.newPage();

                document.add(titulo(notaPasantia));
                document.add(contenido(notaPasantia));
                document.add(firma(notaPasantia));

                document.newPage();

                document.add(titulo(notaPasantia));
                document.add(contenido(notaPasantia));
                document.add(firma(notaPasantia));

                document.close();
            } catch (IOException | DocumentException ex) {
                Logger.getLogger(FichaInscripcionPasantia.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    public PdfPTable titulo(NotaPasantia notaPasantia) throws BadElementException, IOException {
        PdfPTable table = new PdfPTable(100);

        //cabecera
        String realPath = getServletContext().getRealPath("/resources/uploads/" + notaPasantia.getGrupoPasantia().getPasantia().getCarrera().getCampus().getInstituto().getLogo());
        Image image = Image.getInstance(realPath);
        image.scaleToFit(70f, 70f);
        image.setAlignment(Image.ALIGN_CENTER);
        PdfPCell cell = new PdfPCell();
        cell.addElement(image);
        cell.setRowspan(4);
        cell.setColspan(20);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(
                notaPasantia.getGrupoPasantia().getPasantia().getCarrera().getCampus().getInstituto().getNombreRegulador() + "\n"
                + notaPasantia.getGrupoPasantia().getPasantia().getCarrera().getNombre(), SUBTITULO));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(80);
        cell.setRowspan(2);
        cell.setFixedHeight(35f);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setPaddingLeft(30f);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("FICHA DE INSCRIPCIÓN PASANTÍA", TITULO));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(80);
        cell.setRowspan(2);
        cell.setFixedHeight(35f);
        cell.setBorder(Rectangle.TOP);
        cell.setBorderColor(BaseColor.WHITE);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setPaddingLeft(30f);
        table.addCell(cell);

        return table;
    }

    public PdfPTable contenido(NotaPasantia notaPasantia) throws BadElementException, IOException {
        PdfPTable table = new PdfPTable(100);

        PdfPCell cell = new PdfPCell(new Phrase(" ", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setColspan(100);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setFixedHeight(20f);
        table.addCell(cell);

        Phrase phrase = new Phrase();
        phrase.add(new Chunk("Codigo: ", NEGRITA));
        phrase.add(new Chunk(String.valueOf(notaPasantia.getCodigo()), NORMAL));
        cell = new PdfPCell(phrase);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setPaddingLeft(30f);
        cell.setPaddingRight(30f);
        cell.setColspan(100);
        cell.setFixedHeight(20f);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        phrase = new Phrase();
        phrase.add(new Chunk("Pasantía: ", NEGRITA));
        phrase.add(new Chunk(notaPasantia.getGrupoPasantia().getPasantia().getNombre(), NORMAL));
        cell = new PdfPCell(phrase);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setPaddingLeft(30f);
        cell.setPaddingRight(30f);
        cell.setColspan(100);
        cell.setFixedHeight(20f);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        phrase = new Phrase();
        phrase.add(new Chunk("Tutor: ", NEGRITA));
        if (notaPasantia.getGrupoPasantia().getEmpleado() != null) {
            phrase.add(new Chunk(notaPasantia.getGrupoPasantia().getEmpleado().toString(), NORMAL));
        }
        cell = new PdfPCell(phrase);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setPaddingLeft(30f);
        cell.setPaddingRight(30f);
        cell.setColspan(100);
        cell.setFixedHeight(20f);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(" ", SUBTITULO));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setPaddingLeft(30f);
        cell.setPaddingRight(30f);
        cell.setColspan(100);
        cell.setFixedHeight(20f);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("PASANTE", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setPaddingLeft(30f);
        cell.setPaddingRight(30f);
        cell.setColspan(100);
        cell.setFixedHeight(20f);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        phrase = new Phrase();
        phrase.add(new Chunk("Apellidos y Nombres: ", NEGRITA));
        phrase.add(new Chunk(notaPasantia.getEstudiante().toString(), NORMAL));
        cell = new PdfPCell(phrase);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setPaddingLeft(30f);
        cell.setPaddingRight(30f);
        cell.setColspan(100);
        cell.setFixedHeight(20f);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        phrase = new Phrase();
        phrase.add(new Chunk("Carnet de Identidad: ", NEGRITA));
        phrase.add(new Chunk(notaPasantia.getEstudiante().dniLugar(), NORMAL));
        cell = new PdfPCell(phrase);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setPaddingLeft(30f);
        cell.setPaddingRight(30f);
        cell.setColspan(100);
        cell.setFixedHeight(20f);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        phrase = new Phrase();
        phrase.add(new Chunk("Fecha de nacimiento: ", NEGRITA));
        phrase.add(new Chunk(notaPasantia.getEstudiante().fechaNacimiento_ddMMyyyy(), NORMAL));
        cell = new PdfPCell(phrase);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setPaddingLeft(30f);
        cell.setPaddingRight(30f);
        cell.setColspan(50);
        cell.setFixedHeight(20f);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        phrase = new Phrase();
        phrase.add(new Chunk("Edad: ", NEGRITA));
        phrase.add(new Chunk(String.valueOf(notaPasantia.getEstudiante().edad()), NORMAL));
        cell = new PdfPCell(phrase);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setPaddingLeft(30f);
        cell.setPaddingRight(30f);
        cell.setColspan(50);
        cell.setFixedHeight(20f);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        phrase = new Phrase();
        phrase.add(new Chunk("Lugar de nacimiento: ", NEGRITA));
        phrase.add(new Chunk(notaPasantia.getEstudiante().getLugarNacimiento(), NORMAL));
        cell = new PdfPCell(phrase);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setPaddingLeft(30f);
        cell.setPaddingRight(30f);
        cell.setColspan(100);
        cell.setFixedHeight(20f);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        phrase = new Phrase();
        phrase.add(new Chunk("Dirección: ", NEGRITA));
        if (notaPasantia.getEstudiante().getDireccion() != null) {
            phrase.add(new Chunk(notaPasantia.getEstudiante().getDireccion(), NORMAL));
        }
        cell = new PdfPCell(phrase);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setPaddingLeft(30f);
        cell.setPaddingRight(30f);
        cell.setColspan(100);
        cell.setFixedHeight(20f);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        phrase = new Phrase();
        phrase.add(new Chunk("Teléfono: ", NEGRITA));
        if (notaPasantia.getEstudiante().getTelefono() != null) {
            phrase.add(new Chunk(String.valueOf(notaPasantia.getEstudiante().getTelefono()), NORMAL));
        }
        cell = new PdfPCell(phrase);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setPaddingLeft(30f);
        cell.setPaddingRight(30f);
        cell.setColspan(50);
        cell.setFixedHeight(20f);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        phrase = new Phrase();
        phrase.add(new Chunk("Celular: ", NEGRITA));
        if (notaPasantia.getEstudiante().getCelular() != null) {
            phrase.add(new Chunk(String.valueOf(notaPasantia.getEstudiante().getCelular()), NORMAL));
        }
        cell = new PdfPCell(phrase);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setPaddingLeft(30f);
        cell.setPaddingRight(30f);
        cell.setColspan(50);
        cell.setFixedHeight(20f);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        phrase = new Phrase();
        phrase.add(new Chunk("Email: ", NEGRITA));
        if (notaPasantia.getEstudiante().getEmail() != null) {
            phrase.add(new Chunk(notaPasantia.getEstudiante().getEmail(), NORMAL));
        }
        cell = new PdfPCell(phrase);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setPaddingLeft(30f);
        cell.setPaddingRight(30f);
        cell.setColspan(100);
        cell.setFixedHeight(20f);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(" ", SUBTITULO));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setPaddingLeft(30f);
        cell.setPaddingRight(30f);
        cell.setColspan(100);
        cell.setFixedHeight(20f);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("EMPRESA", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setPaddingLeft(30f);
        cell.setPaddingRight(30f);
        cell.setColspan(100);
        cell.setFixedHeight(20f);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        phrase = new Phrase();
        phrase.add(new Chunk("Nombre o Razón Social: ", NEGRITA));
        phrase.add(new Chunk(notaPasantia.getEmpresa().getNombreEmpresa(), NORMAL));
        cell = new PdfPCell(phrase);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setPaddingLeft(30f);
        cell.setPaddingRight(30f);
        cell.setColspan(100);
        cell.setFixedHeight(20f);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        phrase = new Phrase();
        phrase.add(new Chunk("Actividad: ", NEGRITA));
        phrase.add(new Chunk(notaPasantia.getEmpresa().getActividad(), NORMAL));
        cell = new PdfPCell(phrase);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setPaddingLeft(30f);
        cell.setPaddingRight(30f);
        cell.setColspan(100);
        cell.setFixedHeight(20f);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        phrase = new Phrase();
        phrase.add(new Chunk("Tipo: ", NEGRITA));
        phrase.add(new Chunk(notaPasantia.getEmpresa().getTipoEmpresa().getNombre(), NORMAL));
        cell = new PdfPCell(phrase);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setPaddingLeft(30f);
        cell.setPaddingRight(30f);
        cell.setColspan(100);
        cell.setFixedHeight(20f);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        phrase = new Phrase();
        phrase.add(new Chunk("NIT: ", NEGRITA));
        phrase.add(new Chunk(notaPasantia.getEmpresa().getNit(), NORMAL));
        cell = new PdfPCell(phrase);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setPaddingLeft(30f);
        cell.setPaddingRight(30f);
        cell.setColspan(100);
        cell.setFixedHeight(20f);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        phrase = new Phrase();
        phrase.add(new Chunk("Dirección: ", NEGRITA));
        phrase.add(new Chunk(notaPasantia.getEmpresa().getDireccionEmpresa(), NORMAL));
        cell = new PdfPCell(phrase);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setPaddingLeft(30f);
        cell.setPaddingRight(30f);
        cell.setColspan(100);
        cell.setFixedHeight(20f);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        phrase = new Phrase();
        phrase.add(new Chunk("Teléfono: ", NEGRITA));
        if (notaPasantia.getEstudiante().getTelefono() != null) {
            phrase.add(new Chunk(String.valueOf(notaPasantia.getEmpresa().getTelefonoEmpresa()), NORMAL));
        }
        cell = new PdfPCell(phrase);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setPaddingLeft(30f);
        cell.setPaddingRight(30f);
        cell.setColspan(50);
        cell.setFixedHeight(20f);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        phrase = new Phrase();
        phrase.add(new Chunk("Celular: ", NEGRITA));
        if (notaPasantia.getEstudiante().getCelular() != null) {
            phrase.add(new Chunk(String.valueOf(notaPasantia.getEmpresa().getCelularEmpresa()), NORMAL));
        }
        cell = new PdfPCell(phrase);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setPaddingLeft(30f);
        cell.setPaddingRight(30f);
        cell.setColspan(50);
        cell.setFixedHeight(20f);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        phrase = new Phrase();
        phrase.add(new Chunk("Email: ", NEGRITA));
        phrase.add(new Chunk(notaPasantia.getEmpresa().getEmailEmpresa(), NORMAL));
        cell = new PdfPCell(phrase);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setPaddingLeft(30f);
        cell.setPaddingRight(30f);
        cell.setColspan(100);
        cell.setFixedHeight(20f);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        return table;
    }

    public PdfPTable firma(NotaPasantia notaPasantia) throws BadElementException, IOException {
        PdfPTable table = new PdfPTable(100);

        PdfPCell cell = new PdfPCell(new Phrase(" ", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setColspan(100);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setFixedHeight(40f);
        table.addCell(cell);

        Phrase phrase = new Phrase();
        phrase.add(new Chunk(notaPasantia.getGrupoPasantia().getPasantia().getCarrera().getCampus().getInstituto().getUbicacion() + ", " + Fecha.formatearFecha_ddMMMMyyyy(notaPasantia.getFecha()), NORMAL));
        cell = new PdfPCell(phrase);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setPaddingLeft(30f);
        cell.setPaddingRight(30f);
        cell.setColspan(100);
        cell.setFixedHeight(20f);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(" ", SUBTITULO));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setPaddingLeft(30f);
        cell.setPaddingRight(30f);
        cell.setColspan(100);
        cell.setFixedHeight(20f);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        phrase = new Phrase();
        phrase.add(new Chunk(" ", NORMAL));
        cell = new PdfPCell(phrase);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setPaddingLeft(30f);
        cell.setPaddingRight(30f);
        cell.setColspan(50);
        cell.setFixedHeight(40f);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        phrase = new Phrase();
        phrase.add(new Chunk("--------------------------------------------------\nFirma", NORMAL));
        cell = new PdfPCell(phrase);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setPaddingLeft(30f);
        cell.setPaddingRight(30f);
        cell.setColspan(50);
        cell.setFixedHeight(40f);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(" ", SUBTITULO));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setPaddingLeft(30f);
        cell.setPaddingRight(30f);
        cell.setColspan(100);
        cell.setFixedHeight(20f);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        String s = notaPasantia.getGrupoPasantia().getPasantia().getCarrera().getCampus().getNombre() + ", "
                + notaPasantia.getGrupoPasantia().getPasantia().getCarrera().getCampus().getDireccion();
        if (notaPasantia.getGrupoPasantia().getPasantia().getCarrera().getCampus().getTelefono() != null) {
            s += ", " + notaPasantia.getGrupoPasantia().getPasantia().getCarrera().getCampus().getTelefono();
        }
        cell = new PdfPCell(new Phrase(s, SUBTITULO));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setPaddingLeft(30f);
        cell.setPaddingRight(30f);
        cell.setColspan(100);
        cell.setFixedHeight(20f);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        table.addCell(cell);

        return table;
    }
}
