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
import java.io.File;
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
import org.malbino.orion.entities.Comprobante;
import org.malbino.orion.entities.Estudiante;
import org.malbino.orion.entities.Pago;
import org.malbino.orion.facades.CarreraFacade;
import org.malbino.orion.facades.ComprobanteFacade;
import org.malbino.orion.facades.EstudianteFacade;
import org.malbino.orion.facades.PagoFacade;

/**
 *
 * @author tincho
 */
@WebServlet(name = "HistorialEconomico", urlPatterns = {"/reportes/HistorialEconomico"})
public class HistorialEconomico extends HttpServlet {

    private static final String CONTENIDO_PDF = "application/pdf";

    private static final Font TITULO = FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD, BaseColor.BLACK);
    private static final Font SUBTITULO = FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK);
    private static final Font NEGRITA = FontFactory.getFont(FontFactory.HELVETICA, 8, Font.BOLD, BaseColor.BLACK);
    private static final Font NORMAL = FontFactory.getFont(FontFactory.HELVETICA, 8, Font.NORMAL, BaseColor.BLACK);

    private static final int MARGEN_IZQUIERDO = -40;
    private static final int MARGEN_DERECHO = -40;
    private static final int MARGEN_SUPERIOR = 20;
    private static final int MARGEN_INFERIOR = 20;

    @EJB
    EstudianteFacade estudianteFacade;
    @EJB
    CarreraFacade carreraFacade;
    @EJB
    PagoFacade pagoFacade;
    @EJB
    ComprobanteFacade comprobanteFacade;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.generarPDF(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        this.generarPDF(request, response);
    }

    public void generarPDF(HttpServletRequest request, HttpServletResponse response) {
        Integer id_persona = (Integer) request.getSession().getAttribute("id_persona");
        Integer id_carrera = (Integer) request.getSession().getAttribute("id_carrera");

        if (id_persona != null && id_carrera != null) {
            Estudiante estudiante = estudianteFacade.find(id_persona);
            Carrera carrera = carreraFacade.find(id_carrera);
            try {
                response.setContentType(CONTENIDO_PDF);

                Document document = new Document(PageSize.A4, MARGEN_IZQUIERDO, MARGEN_DERECHO, MARGEN_SUPERIOR, MARGEN_INFERIOR);
                PdfWriter.getInstance(document, response.getOutputStream());

                document.open();

                document.add(titulo(estudiante, carrera));
                document.add(contenido(estudiante, carrera));

                document.close();
            } catch (IOException | DocumentException ex) {
                Logger.getLogger(HistorialEconomico.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    public PdfPTable titulo(Estudiante estudiante, Carrera carrera) throws BadElementException, IOException {
        PdfPTable table = new PdfPTable(100);

        //cabecera
        String realPath = System.getProperty("catalina.base") + File.separator + "docroot" + File.separator + "oriondata" + File.separator + carrera.getCampus().getInstituto().getLogo();
        Image image = Image.getInstance(realPath);
        image.scaleToFit(60, 60);
        image.setAlignment(Image.ALIGN_CENTER);
        PdfPCell cell = new PdfPCell();
        cell.addElement(image);
        cell.setRowspan(4);
        cell.setColspan(10);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("HISTORIAL ECONOMICO,", SUBTITULO));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setColspan(90);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(estudiante.toString(), TITULO));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setColspan(90);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(carrera.toString(), TITULO));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setColspan(90);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(carrera.getCampus().getInstituto().getNombreRegulador(), SUBTITULO));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setColspan(90);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        return table;
    }

    public PdfPTable contenido(Estudiante estudiante, Carrera carrera) throws BadElementException, IOException {
        PdfPTable table = new PdfPTable(100);

        PdfPCell cell = new PdfPCell(new Phrase(" ", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setColspan(100);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Nro", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setColspan(5);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Gestion", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setColspan(15);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Codigo", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setColspan(15);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Concepto", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setColspan(15);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Pagado", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setColspan(15);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Comprobante", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setColspan(20);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Monto", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setColspan(15);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        table.addCell(cell);

        List<Pago> kardexEconomico = pagoFacade.kardexEconomico(estudiante.getId_persona(), carrera.getId_carrera());
        for (int i = 0; i < kardexEconomico.size(); i++) {
            Pago pago = kardexEconomico.get(i);

            cell = new PdfPCell(new Phrase(String.valueOf(i + 1), NORMAL));
            cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            cell.setColspan(5);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(pago.getInscrito().getGestionAcademica().toString(), NORMAL));
            cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            cell.setColspan(15);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(pago.getConcepto().getCodigo(), NORMAL));
            cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            cell.setColspan(15);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(pago.getConcepto().getNombre(), NORMAL));
            cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
            cell.setColspan(15);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(pago.pagadoToString(), NORMAL));
            cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            cell.setColspan(15);
            table.addCell(cell);

            Comprobante comprobante = comprobanteFacade.buscarComprobanteValido(pago.getId_pago());
            if (comprobante != null) {
                cell = new PdfPCell(new Phrase(comprobante.toString(), NORMAL));
            } else {
                cell = new PdfPCell(new Phrase(" ", NEGRITA));
            }
            cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            cell.setColspan(20);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(pago.getMonto().toString(), NORMAL));
            cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
            cell.setColspan(15);
            table.addCell(cell);
        }

        return table;
    }
}
