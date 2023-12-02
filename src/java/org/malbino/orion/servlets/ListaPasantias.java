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
import org.malbino.orion.entities.Carrera;
import org.malbino.orion.entities.GestionAcademica;
import org.malbino.orion.entities.NotaPasantia;
import org.malbino.orion.facades.CarreraFacade;
import org.malbino.orion.facades.GestionAcademicaFacade;
import org.malbino.orion.facades.NotaPasantiaFacade;
import org.malbino.orion.util.Fecha;

/**
 *
 * @author tincho
 */
@WebServlet(name = "ListaPasantias", urlPatterns = {"/reportes/pasantias/listaPasantias/ListaPasantias"})
public class ListaPasantias extends HttpServlet {

    private static final String CONTENIDO_PDF = "application/pdf";

    private static final Font TITULO = FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD, BaseColor.BLACK);
    private static final Font SUBTITULO = FontFactory.getFont(FontFactory.HELVETICA, 8, Font.NORMAL, BaseColor.BLACK);
    private static final Font NEGRITA = FontFactory.getFont(FontFactory.HELVETICA, 6, Font.BOLD, BaseColor.BLACK);
    private static final Font NORMAL = FontFactory.getFont(FontFactory.HELVETICA, 6, Font.NORMAL, BaseColor.BLACK);

    private static final int MARGEN_IZQUIERDO = -60;
    private static final int MARGEN_DERECHO = -60;
    private static final int MARGEN_SUPERIOR = 30;
    private static final int MARGEN_INFERIOR = 30;

    @EJB
    GestionAcademicaFacade gestionAcademicaFacade;
    @EJB
    CarreraFacade carreraFacade;
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
        Integer id_gestionacademica = (Integer) request.getSession().getAttribute("id_gestionacademica");
        Integer id_carrera = (Integer) request.getSession().getAttribute("id_carrera");

        if (id_gestionacademica != null && id_carrera != null) {
            GestionAcademica gestionAcademica = gestionAcademicaFacade.find(id_gestionacademica);
            Carrera carrera = carreraFacade.find(id_carrera);
            try {
                response.setContentType(CONTENIDO_PDF);

                Document document = new Document(PageSize.LETTER.rotate(), MARGEN_IZQUIERDO, MARGEN_DERECHO, MARGEN_SUPERIOR, MARGEN_INFERIOR);
                PdfWriter.getInstance(document, response.getOutputStream());

                document.open();

                document.add(titulo(gestionAcademica, carrera));
                document.add(contenido(gestionAcademica, carrera));

                document.close();
            } catch (IOException | DocumentException ex) {
                Logger.getLogger(ListaPasantias.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    public PdfPTable titulo(GestionAcademica gestionAcademica, Carrera carrera) throws BadElementException, IOException {
        PdfPTable table = new PdfPTable(100);

        //cabecera
        String realPath = getServletContext().getRealPath("/resources/uploads/" + carrera.getCampus().getInstituto().getLogo());
        Image image = Image.getInstance(realPath);
        image.scaleToFit(70, 70);
        image.setAlignment(Image.ALIGN_CENTER);
        PdfPCell cell = new PdfPCell();
        cell.addElement(image);
        cell.setRowspan(5);
        cell.setColspan(15);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("LISTA PASANTIAS,", TITULO));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setColspan(85);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(gestionAcademica.toString(), SUBTITULO));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setColspan(85);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(carrera.toString(), SUBTITULO));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setColspan(85);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        return table;
    }

    public PdfPTable contenido(GestionAcademica gestionAcademica, Carrera carrera) throws BadElementException, IOException {
        PdfPTable table = new PdfPTable(100);

        PdfPCell cell = new PdfPCell(new Phrase(" ", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setColspan(100);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Nro", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setColspan(3);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Codigo", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setColspan(7);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Estudiante", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setColspan(18);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Empresa", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setColspan(18);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Tutor", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setColspan(18);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Fecha Inicio", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setColspan(7);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Fecha Fin", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setColspan(7);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Nota Empresa", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setColspan(5);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Nota Tutor", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setColspan(5);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Nota Final", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setColspan(5);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Condicion", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setColspan(7);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        table.addCell(cell);

        List<NotaPasantia> listaNotasPasantias = notaPasantiaFacade.listaNotasPasantias(gestionAcademica.getId_gestionacademica(), carrera.getId_carrera());
        for (int i = 0; i < listaNotasPasantias.size(); i++) {
            NotaPasantia notaPasantia = listaNotasPasantias.get(i);

            cell = new PdfPCell(new Phrase(String.valueOf(i + 1), NORMAL));
            cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
            cell.setColspan(3);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(String.valueOf(notaPasantia.getCodigo()), NORMAL));
            cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
            cell.setColspan(7);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(notaPasantia.getEstudiante().toString(), NORMAL));
            cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
            cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
            cell.setColspan(18);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(String.valueOf(notaPasantia.getEmpresa().toString()), NORMAL));
            cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
            cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
            cell.setColspan(18);
            table.addCell(cell);

            if (notaPasantia.getGrupoPasantia().getEmpleado() != null) {
                cell = new PdfPCell(new Phrase(String.valueOf(notaPasantia.getGrupoPasantia().getEmpleado().toString()), NORMAL));
            } else {
                cell = new PdfPCell(new Phrase(" ", NORMAL));
            }
            cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
            cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
            cell.setColspan(18);
            table.addCell(cell);

            if (notaPasantia.getInicio() != null) {
                cell = new PdfPCell(new Phrase(Fecha.formatearFecha_ddMMyyyy(notaPasantia.getInicio()), NORMAL));
            } else {
                cell = new PdfPCell(new Phrase(" ", NORMAL));
            }
            cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
            cell.setColspan(7);
            table.addCell(cell);

            if (notaPasantia.getFin() != null) {
                cell = new PdfPCell(new Phrase(Fecha.formatearFecha_ddMMyyyy(notaPasantia.getFin()), NORMAL));
            } else {
                cell = new PdfPCell(new Phrase(" ", NORMAL));
            }
            cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
            cell.setColspan(7);
            table.addCell(cell);

            if (notaPasantia.getNotaEmpresa() != null) {
                cell = new PdfPCell(new Phrase(notaPasantia.getNotaEmpresa().toString(), NORMAL));
            } else {
                cell = new PdfPCell(new Phrase(" ", NORMAL));
            }
            cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
            cell.setColspan(5);
            table.addCell(cell);

            if (notaPasantia.getNotaTutor() != null) {
                cell = new PdfPCell(new Phrase(notaPasantia.getNotaTutor().toString(), NORMAL));
            } else {
                cell = new PdfPCell(new Phrase(" ", NORMAL));
            }
            cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
            cell.setColspan(5);
            table.addCell(cell);

            if (notaPasantia.getNotaFinal() != null) {
                cell = new PdfPCell(new Phrase(notaPasantia.getNotaFinal().toString(), NORMAL));
            } else {
                cell = new PdfPCell(new Phrase(" ", NORMAL));
            }
            cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
            cell.setColspan(5);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(notaPasantia.getCondicion().getNombre(), NORMAL));
            cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
            cell.setColspan(7);
            table.addCell(cell);
        }

        return table;
    }
}
