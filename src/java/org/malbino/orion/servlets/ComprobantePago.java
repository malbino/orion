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
import org.malbino.orion.entities.Comprobante;
import org.malbino.orion.entities.Detalle;
import org.malbino.orion.entities.Estudiante;
import org.malbino.orion.entities.Inscrito;
import org.malbino.orion.facades.ComprobanteFacade;
import org.malbino.orion.facades.DetalleFacade;

/**
 *
 * @author tincho
 */
@WebServlet(name = "ComprobantePago", urlPatterns = {"/pagos/ComprobantePago"})
public class ComprobantePago extends HttpServlet {

    private static final String CONTENIDO_PDF = "application/pdf";

    private static final Font TITULO = FontFactory.getFont(FontFactory.HELVETICA, 14, Font.BOLD, BaseColor.BLACK);
    private static final Font NEGRITA = FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD, BaseColor.BLACK);
    private static final Font NEGRITA_BLANCO = FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD, BaseColor.WHITE);
    private static final Font NORMAL = FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK);

    private static final int MARGEN_IZQUIERDO = -40;
    private static final int MARGEN_DERECHO = -40;
    private static final int MARGEN_SUPERIOR = 20;
    private static final int MARGEN_INFERIOR = 20;

    @EJB
    ComprobanteFacade comprobanteFacade;
    @EJB
    DetalleFacade detalleFacade;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.generarPDF(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        this.generarPDF(request, response);
    }

    public void generarPDF(HttpServletRequest request, HttpServletResponse response) {
        Integer id_comprobante = (Integer) request.getSession().getAttribute("id_comprobante");
        Estudiante estudiante = (Estudiante) request.getSession().getAttribute("est");

        if (id_comprobante != null) {
            Comprobante comprobante = comprobanteFacade.find(id_comprobante);
            try {
                response.setContentType(CONTENIDO_PDF);

                Document document = new Document(PageSize.A5.rotate(), MARGEN_IZQUIERDO, MARGEN_DERECHO, MARGEN_SUPERIOR, MARGEN_INFERIOR);
                PdfWriter.getInstance(document, response.getOutputStream());

                document.open();

                document.add(comprobante(comprobante, estudiante));

                document.close();
            } catch (IOException | DocumentException ex) {
                Logger.getLogger(ComprobantePago.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    public PdfPTable comprobante(Comprobante comprobante, Estudiante estudiante) throws BadElementException, IOException {
        PdfPTable table = new PdfPTable(100);

        //cabecera
        String realPath = getServletContext().getRealPath("/resources/uploads/" + comprobante.getInscrito().getCarrera().getCampus().getInstituto().getLogo());
        Image image = Image.getInstance(realPath);
        image.scaleToFit(160, 80);
        image.setAlignment(Image.ALIGN_CENTER);
        PdfPCell cell = new PdfPCell();
        cell.addElement(image);
        cell.setRowspan(6);
        cell.setColspan(20);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        //fila 1
        cell = new PdfPCell(new Phrase(comprobante.getInscrito().getCarrera().getCampus().getInstituto().getNombreImpuestos(), TITULO));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setColspan(50);
        cell.setBorder(Rectangle.LEFT | Rectangle.TOP | Rectangle.RIGHT);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(" ", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setColspan(5);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(" ", TITULO));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setColspan(25);
        cell.setBorder(Rectangle.LEFT | Rectangle.TOP | Rectangle.RIGHT);
        table.addCell(cell);

        //fial 2
        cell = new PdfPCell(new Phrase(" ", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setColspan(50);
        cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(" ", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setColspan(5);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("COMPROBANTE", NEGRITA_BLANCO));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setColspan(25);
        cell.setBackgroundColor(BaseColor.DARK_GRAY);
        cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
        table.addCell(cell);

        //fila 3
        cell = new PdfPCell(new Phrase(comprobante.getInscrito().getCarrera().getCampus().getSucursal(), NORMAL));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setColspan(50);
        cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(" ", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setColspan(5);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(comprobante.getCodigo(), NORMAL));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setColspan(25);
        cell.setBorder(Rectangle.LEFT | Rectangle.BOTTOM | Rectangle.RIGHT);
        table.addCell(cell);

        //fila 4
        cell = new PdfPCell(new Phrase(comprobante.getInscrito().getCarrera().getCampus().getDireccion(), NORMAL));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setColspan(50);
        cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(" ", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setColspan(5);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(" ", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setColspan(25);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        //fial 5
        cell = new PdfPCell(new Phrase(comprobante.getInscrito().getCarrera().getCampus().getTelefono().toString(), NORMAL));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setColspan(50);
        cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(" ", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setColspan(5);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(comprobante.fecha_ddMMyyyy(), NORMAL));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setColspan(25);
        cell.setBorder(Rectangle.LEFT | Rectangle.TOP | Rectangle.RIGHT);
        table.addCell(cell);

        //fila 6
        cell = new PdfPCell(new Phrase(comprobante.getInscrito().getCarrera().getCampus().getInstituto().getUbicacion(), NORMAL));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setColspan(50);
        cell.setBorder(Rectangle.LEFT | Rectangle.BOTTOM | Rectangle.RIGHT);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(" ", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setColspan(5);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(" ", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setColspan(25);
        cell.setBorder(Rectangle.LEFT | Rectangle.BOTTOM | Rectangle.RIGHT);
        table.addCell(cell);

        //inscripcion
        cell = new PdfPCell(new Phrase(" ", NORMAL));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setColspan(100);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        //fila 1
        cell = new PdfPCell(new Phrase("Carrera:", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setColspan(20);
        cell.setBorder(Rectangle.LEFT | Rectangle.TOP);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(comprobante.getInscrito().getCarrera().toString(), NORMAL));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setColspan(50);
        cell.setBorder(Rectangle.TOP);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Gestión Academica:", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setColspan(20);
        cell.setBorder(Rectangle.TOP);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(comprobante.getInscrito().getGestionAcademica().toString(), NORMAL));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setColspan(10);
        cell.setBorder(Rectangle.TOP | Rectangle.RIGHT);
        table.addCell(cell);

        //fila 2
        cell = new PdfPCell(new Phrase("Estudiante:", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setColspan(20);
        cell.setBorder(Rectangle.LEFT | Rectangle.BOTTOM);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(comprobante.getInscrito().getEstudiante().toString(), NORMAL));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setColspan(80);
        cell.setBorder(Rectangle.BOTTOM | Rectangle.RIGHT);
        table.addCell(cell);

        //detalles
        cell = new PdfPCell(new Phrase(" ", NORMAL));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setColspan(100);
        cell.setBorder(PdfPCell.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Nro", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setColspan(10);
        cell.setBorder(Rectangle.LEFT | Rectangle.TOP | Rectangle.BOTTOM);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Codigo", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setColspan(20);
        cell.setBorder(Rectangle.TOP | Rectangle.BOTTOM);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Concepto", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setColspan(50);
        cell.setBorder(Rectangle.TOP | Rectangle.BOTTOM);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Monto", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setColspan(20);
        cell.setBorder(Rectangle.TOP | Rectangle.RIGHT | Rectangle.BOTTOM);
        table.addCell(cell);

        List<Detalle> detalles = detalleFacade.listaDetalles(comprobante.getId_comprobante());
        Integer total = 0;
        for (int i = 0; i < detalles.size(); i++) {
            Detalle detalle = detalles.get(i);

            cell = new PdfPCell(new Phrase(String.valueOf(i + 1), NORMAL));
            cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            cell.setColspan(10);
            cell.setBorder(PdfPCell.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(detalle.getConcepto().getCodigo(), NORMAL));
            cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            cell.setColspan(20);
            cell.setBorder(PdfPCell.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(detalle.getConcepto().getNombre(), NORMAL));
            cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
            cell.setColspan(50);
            cell.setBorder(PdfPCell.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(detalle.getMonto().toString(), NORMAL));
            cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
            cell.setColspan(20);
            cell.setBorder(PdfPCell.NO_BORDER);
            table.addCell(cell);

            total += detalle.getMonto();
        }

        cell = new PdfPCell(new Phrase("Total:", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
        cell.setColspan(80);
        cell.setBorder(Rectangle.LEFT | Rectangle.TOP | Rectangle.BOTTOM);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(total.toString(), NORMAL));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
        cell.setColspan(20);
        cell.setBorder(Rectangle.TOP | Rectangle.RIGHT | Rectangle.BOTTOM);
        table.addCell(cell);

        //codigos
        if (estudiante != null) {
            cell = new PdfPCell(new Phrase(" ", NORMAL));
            cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            cell.setColspan(100);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            //fila 1
            cell = new PdfPCell(new Phrase(" ", NORMAL));
            cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            cell.setColspan(20);
            cell.setBorder(Rectangle.BOTTOM | Rectangle.LEFT | Rectangle.TOP);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Codigo: ", NEGRITA));
            cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            cell.setColspan(15);
            cell.setBorder(Rectangle.TOP | Rectangle.BOTTOM);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(String.valueOf(estudiante.getMatricula()), NORMAL));
            cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            cell.setColspan(15);
            cell.setBorder(Rectangle.TOP | Rectangle.BOTTOM);
            cell.setBackgroundColor(BaseColor.DARK_GRAY);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Contraseña: ", NEGRITA));
            cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            cell.setColspan(15);
            cell.setBorder(Rectangle.TOP | Rectangle.BOTTOM);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(estudiante.getContrasenaSinEncriptar(), NORMAL));
            cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            cell.setColspan(15);
            cell.setBorder(Rectangle.TOP | Rectangle.BOTTOM);
            cell.setBackgroundColor(BaseColor.DARK_GRAY);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(" ", NORMAL));
            cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            cell.setColspan(20);
            cell.setBorder(Rectangle.TOP | Rectangle.RIGHT | Rectangle.BOTTOM);
            table.addCell(cell);
        }

        return table;
    }
}
