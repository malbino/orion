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
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.malbino.orion.entities.Carrera;
import org.malbino.orion.entities.Materia;
import org.malbino.orion.entities.Mencion;
import org.malbino.orion.enums.Nivel;
import org.malbino.orion.facades.MateriaFacade;

/**
 *
 * @author tincho
 */
@WebServlet(name = "MallaCurricular", urlPatterns = {"/reportes/mallaCurricular/MallaCurricular"})
public class MallaCurricular extends HttpServlet {

    private static final String CONTENIDO_PDF = "application/pdf";

    private static final Font TITULO = FontFactory.getFont(FontFactory.HELVETICA, 14, Font.BOLD, BaseColor.BLACK);
    private static final Font SUBTITULO = FontFactory.getFont(FontFactory.HELVETICA, 12, Font.NORMAL, BaseColor.BLACK);
    private static final Font NEGRITA = FontFactory.getFont(FontFactory.HELVETICA, 8, Font.BOLD, BaseColor.BLACK);
    private static final Font NORMAL = FontFactory.getFont(FontFactory.HELVETICA, 8, Font.NORMAL, BaseColor.BLACK);

    private static final int MARGEN_IZQUIERDO = -60;
    private static final int MARGEN_DERECHO = -60;
    private static final int MARGEN_SUPERIOR = 30;
    private static final int MARGEN_INFERIOR = 30;

    @EJB
    MateriaFacade materiaFacade;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.generarPDF(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        this.generarPDF(request, response);
    }

    public void generarPDF(HttpServletRequest request, HttpServletResponse response) {
        Carrera carrera = (Carrera) request.getSession().getAttribute("carrera");
        Mencion mencion = (Mencion) request.getSession().getAttribute("mencion");

        if (carrera != null) {
            try {
                response.setContentType(CONTENIDO_PDF);

                Document document = new Document(PageSize.LETTER.rotate(), MARGEN_IZQUIERDO, MARGEN_DERECHO, MARGEN_SUPERIOR, MARGEN_INFERIOR);
                PdfWriter.getInstance(document, response.getOutputStream());

                document.open();

                document.add(titulo(carrera, mencion));
                document.add(contenido(carrera, mencion));

                document.close();
            } catch (IOException | DocumentException ex) {
                Logger.getLogger(MallaCurricular.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    public PdfPTable titulo(Carrera carrera, Mencion mencion) throws BadElementException, IOException {
        PdfPTable table = new PdfPTable(100);

        //cabecera
        String realPath = getServletContext().getRealPath("/resources/uploads/" + carrera.getCampus().getInstituto().getLogo());
        Image image = Image.getInstance(realPath);
        image.scaleToFit(70, 70);
        image.setAlignment(Image.ALIGN_CENTER);
        PdfPCell cell = new PdfPCell();
        cell.addElement(image);
        cell.setRowspan(4);
        cell.setColspan(10);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("MALLA CURRICULAR,", SUBTITULO));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setColspan(90);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(carrera.toString(), TITULO));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setColspan(90);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        if (mencion != null) {
            cell = new PdfPCell(new Phrase(mencion.toString(), SUBTITULO));
            cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
            cell.setColspan(90);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
        }

        cell = new PdfPCell(new Phrase(carrera.getCampus().getInstituto().getNombreRegulador(), SUBTITULO));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setColspan(90);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        return table;
    }

    public PdfPTable contenido(Carrera carrera, Mencion mencion) throws BadElementException, IOException {
        Nivel[] niveles = Nivel.values(carrera.getRegimen());

        PdfPTable table = new PdfPTable(niveles.length);

        PdfPCell cell = new PdfPCell(new Phrase(" ", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setColspan(niveles.length);
        table.addCell(cell);

        for (Nivel nivel : niveles) {
            PdfPTable subtable = new PdfPTable(1);

            cell = new PdfPCell(new Phrase(nivel.getNombre(), NEGRITA));
            cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            subtable.addCell(cell);

            cell = new PdfPCell(new Phrase(" ", NEGRITA));
            cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            cell.setBorder(Rectangle.NO_BORDER);
            subtable.addCell(cell);

            Long cantidadMaximaMateriasNivel = materiaFacade.cantidadMaximaMateriasNivel(carrera, mencion);

            List<Materia> materias = materiaFacade.listaMaterias(carrera, mencion, nivel);
            Iterator<Materia> iterator = materias.iterator();
            for (int i = 0; i < cantidadMaximaMateriasNivel; i++) {
                if (iterator.hasNext()) {
                    Materia materia = iterator.next();

                    cell = new PdfPCell(new Phrase(materia.getCodigo(), NEGRITA));
                    cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                    cell.setBorder(Rectangle.LEFT | Rectangle.TOP | Rectangle.RIGHT);
                    subtable.addCell(cell);

                    cell = new PdfPCell(new Phrase(materia.getNombre(), NORMAL));
                    cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                    cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                    cell.setFixedHeight(25);
                    subtable.addCell(cell);

                    cell = new PdfPCell(new Phrase(materia.prerequisitosToString(), NEGRITA));
                    cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
                    cell.setBorder(Rectangle.LEFT | Rectangle.BOTTOM | Rectangle.RIGHT);
                    subtable.addCell(cell);
                } else {
                    cell = new PdfPCell(new Phrase(" ", NEGRITA));
                    cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                    cell.setBorder(Rectangle.LEFT | Rectangle.TOP | Rectangle.RIGHT);
                    subtable.addCell(cell);

                    cell = new PdfPCell(new Phrase(" ", NORMAL));
                    cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                    cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                    cell.setFixedHeight(25);
                    subtable.addCell(cell);

                    cell = new PdfPCell(new Phrase(" ", NEGRITA));
                    cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
                    cell.setBorder(Rectangle.LEFT | Rectangle.BOTTOM | Rectangle.RIGHT);
                    subtable.addCell(cell);
                }
            }

            cell = new PdfPCell(subtable);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
        }

        return table;
    }
}
