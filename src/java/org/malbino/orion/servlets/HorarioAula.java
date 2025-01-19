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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.malbino.orion.entities.Aula;
import org.malbino.orion.entities.Campus;
import org.malbino.orion.entities.Clase;
import org.malbino.orion.entities.Periodo;
import org.malbino.orion.enums.Dia;
import org.malbino.orion.facades.AulaFacade;
import org.malbino.orion.facades.CampusFacade;
import org.malbino.orion.facades.ClaseFacade;
import org.malbino.orion.facades.PeriodoFacade;

/**
 *
 * @author tincho
 */
@WebServlet(name = "HorarioAula", urlPatterns = {"/reportes/horarios/horarioAula/HorarioAula"})
public class HorarioAula extends HttpServlet {

    private static final String CONTENIDO_PDF = "application/pdf";

    private static final Font TITULO = FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD, BaseColor.BLACK);
    private static final Font SUBTITULO = FontFactory.getFont(FontFactory.HELVETICA, 8, Font.NORMAL, BaseColor.BLACK);
    private static final Font NEGRITA = FontFactory.getFont(FontFactory.HELVETICA, 6, Font.BOLD, BaseColor.BLACK);
    private static final Font NORMAL = FontFactory.getFont(FontFactory.HELVETICA, 6, Font.NORMAL, BaseColor.BLACK);

    private static final int MARGEN_IZQUIERDO = -40;
    private static final int MARGEN_DERECHO = -40;
    private static final int MARGEN_SUPERIOR = 30;
    private static final int MARGEN_INFERIOR = 30;

    @EJB
    CampusFacade campusFacade;
    @EJB
    AulaFacade aulaFacade;
    @EJB
    PeriodoFacade periodoFacade;
    @EJB
    ClaseFacade claseFacade;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.generarPDF(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        this.generarPDF(request, response);
    }

    public void generarPDF(HttpServletRequest request, HttpServletResponse response) {
        Integer id_campus = (Integer) request.getSession().getAttribute("id_campus");
        Integer id_aula = (Integer) request.getSession().getAttribute("id_aula");

        if (id_campus != null && id_aula != null) {
            Campus campus = campusFacade.find(id_campus);
            Aula aula = aulaFacade.find(id_aula);
            try {
                response.setContentType(CONTENIDO_PDF);

                Document document = new Document(PageSize.LETTER, MARGEN_IZQUIERDO, MARGEN_DERECHO, MARGEN_SUPERIOR, MARGEN_INFERIOR);
                PdfWriter.getInstance(document, response.getOutputStream());

                document.open();

                document.add(titulo(campus, aula));
                document.add(contenido(campus, aula));

                document.close();
            } catch (IOException | DocumentException ex) {
                Logger.getLogger(HorarioAula.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    public PdfPTable titulo(Campus campus, Aula aula) throws BadElementException, IOException {
        PdfPTable table = new PdfPTable(100);

        //cabecera
        String realPath = getServletContext().getRealPath("/resources/uploads/" + campus.getInstituto().getLogo());
        Image image = Image.getInstance(realPath);
        image.scaleToFit(70, 70);
        image.setAlignment(Image.ALIGN_CENTER);
        PdfPCell cell = new PdfPCell();
        cell.addElement(image);
        cell.setRowspan(6);
        cell.setColspan(20);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("HORARIO AULA,", TITULO));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setColspan(80);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(campus.toString(), SUBTITULO));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setColspan(80);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(aula.toString(), SUBTITULO));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setColspan(80);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(" ", SUBTITULO));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setColspan(80);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(" ", SUBTITULO));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setColspan(80);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(" ", SUBTITULO));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setColspan(80);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        return table;
    }

    public PdfPTable contenido(Campus campus, Aula aula) throws BadElementException, IOException {
        PdfPTable table = new PdfPTable(80);

        PdfPCell cell = new PdfPCell(new Phrase(" ", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(10);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        table.addCell(cell);

        Dia[] dias = Dia.values();
        for (int j = 0; j < dias.length; j++) {
            Dia dia = dias[j];

            cell = new PdfPCell(new Phrase(dia.name(), NEGRITA));
            cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            cell.setColspan(10);
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(cell);
        }

        List<Periodo> periodos = periodoFacade.listaPeriodos();
        for (int i = 0; i < periodos.size(); i++) {
            Periodo periodo = periodos.get(i);

            cell = new PdfPCell(new Phrase(periodo.toString(), NEGRITA));
            cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
            cell.setColspan(10);
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            cell.setMinimumHeight(30);
            table.addCell(cell);

            for (int j = 0; j < dias.length; j++) {
                Dia dia = dias[j];

                Clase clase = claseFacade.buscar(periodo, dia, aula);
                if (clase != null) {
                    cell = new PdfPCell(new Phrase(clase.toString_Paralelo(), NORMAL));
                    cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                    cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
                    cell.setColspan(10);
                    cell.setMinimumHeight(30);
                    table.addCell(cell);
                } else {
                    cell = new PdfPCell(new Phrase(" ", NORMAL));
                    cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                    cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
                    cell.setColspan(10);
                    cell.setMinimumHeight(30);
                    table.addCell(cell);
                }
            }
        }

        return table;
    }
}
