/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.malbino.orion.servlets;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
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
import org.malbino.orion.facades.ComprobanteFacade;
import org.malbino.orion.facades.DetalleFacade;
import org.malbino.orion.util.NumberToLetterConverter;
import org.malbino.orion.util.Redondeo;

/**
 *
 * @author tincho
 */
@WebServlet(name = "ComprobantePagoEstudiante", urlPatterns = {"/pagos/nuevoPagoEstudiante/ComprobantePagoEstudiante", "/pagos/pagos/ComprobantePagoEstudiante"})
public class ComprobantePagoEstudiante extends HttpServlet {

    private static final String CONTENIDO_PDF = "application/pdf";

    private static final Font TITULO = FontFactory.getFont(FontFactory.HELVETICA, 14, Font.BOLD, BaseColor.BLACK);
    private static final Font NEGRITA = FontFactory.getFont(FontFactory.HELVETICA, 8, Font.BOLD, BaseColor.BLACK);
    private static final Font NEGRITA_BLANCO = FontFactory.getFont(FontFactory.HELVETICA, 8, Font.BOLD, BaseColor.WHITE);
    private static final Font NORMAL = FontFactory.getFont(FontFactory.HELVETICA, 8, Font.NORMAL, BaseColor.BLACK);
    private static final Font BOLDITALIC = FontFactory.getFont(FontFactory.HELVETICA, 9, Font.BOLDITALIC, BaseColor.BLACK);
    private static final Font ITALIC = FontFactory.getFont(FontFactory.HELVETICA, 9, Font.ITALIC, BaseColor.BLACK);

    private static final int MARGEN_IZQUIERDO = 0;
    private static final int MARGEN_DERECHO = -40;
    private static final int MARGEN_SUPERIOR = 30;
    private static final int MARGEN_INFERIOR = 30;

    private static final int CANTIDAD_MINIMA_DETALLES = 4;

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

                Document document = new Document(PageSize.HALFLETTER.rotate(), MARGEN_IZQUIERDO, MARGEN_DERECHO, MARGEN_SUPERIOR, MARGEN_INFERIOR);
                PdfWriter.getInstance(document, response.getOutputStream());

                document.open();

                document.add(comprobante(comprobante, estudiante));

                document.newPage();

                document.add(comprobante(comprobante, estudiante));
                
                document.newPage();
                
                document.add(cuenta(request, estudiante));

                document.close();
            } catch (IOException | DocumentException ex) {
                Logger.getLogger(ComprobantePagoEstudiante.class.getName()).log(Level.SEVERE, null, ex);
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
        cell = new PdfPCell(new Phrase("COMPROBANTE DE PAGO", TITULO));
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

        cell = new PdfPCell(new Phrase("NÚMERO", NEGRITA_BLANCO));
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

        cell = new PdfPCell(new Phrase(String.valueOf(comprobante.getCodigo()), NORMAL));
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
        if (comprobante.getInscrito().getCarrera().getCampus().getTelefono() != null) {
            cell = new PdfPCell(new Phrase(comprobante.getInscrito().getCarrera().getCampus().getTelefono().toString(), NORMAL));
        } else {
            cell = new PdfPCell(new Phrase(" ", NORMAL));
        }
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setColspan(50);
        cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(" ", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setColspan(5);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(comprobante.getDeposito(), NEGRITA));
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

        cell = new PdfPCell(new Phrase(comprobante.fecha_ddMMyyyy(), NORMAL));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
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
        cell.setBorder(Rectangle.LEFT);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(comprobante.getInscrito().getEstudiante().toString(), NORMAL));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setColspan(50);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Régimen:", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setColspan(15);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(comprobante.getInscrito().getGestionAcademica().getRegimen().getNombre(), NORMAL));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setColspan(15);
        cell.setBorder(Rectangle.RIGHT);
        table.addCell(cell);
        
        //fila 3
        cell = new PdfPCell(new Phrase("Becado:", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setColspan(20);
        cell.setBorder(Rectangle.LEFT | Rectangle.BOTTOM);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(comprobante.getInscrito().becado_siNo(), NORMAL));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setColspan(50);
        cell.setBorder(Rectangle.BOTTOM);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(" ", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setColspan(15);
        cell.setBorder(Rectangle.BOTTOM);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(" ", NORMAL));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setColspan(15);
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

        cell = new PdfPCell(new Phrase("Monto (Bs.)", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setColspan(20);
        cell.setBorder(Rectangle.TOP | Rectangle.RIGHT | Rectangle.BOTTOM);
        table.addCell(cell);

        List<Detalle> detalles = detalleFacade.listaDetalles(comprobante.getId_comprobante());
        Integer total = 0;
        for (int i = 0; i < CANTIDAD_MINIMA_DETALLES; i++) {
            if (i < detalles.size()) {
                Detalle detalle = detalles.get(i);

                cell = new PdfPCell(new Phrase(String.valueOf(i + 1), NORMAL));
                cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                cell.setColspan(10);
                cell.setBorder(Rectangle.LEFT);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(detalle.getConcepto().getCodigo(), NORMAL));
                cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                cell.setColspan(20);
                cell.setBorder(PdfPCell.NO_BORDER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(detalle.getConcepto().getNombre(), NORMAL));
                cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                cell.setColspan(50);
                cell.setBorder(PdfPCell.NO_BORDER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(Redondeo.formatear_csm(detalle.getMonto()), NORMAL));
                cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                cell.setColspan(20);
                cell.setBorder(Rectangle.RIGHT);
                table.addCell(cell);
                total += detalle.getMonto();
            } else {
                cell = new PdfPCell(new Phrase(" ", NORMAL));
                cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                cell.setColspan(10);
                cell.setBorder(Rectangle.LEFT);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(" ", NORMAL));
                cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                cell.setColspan(20);
                cell.setBorder(PdfPCell.NO_BORDER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(" ", NORMAL));
                cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                cell.setColspan(50);
                cell.setBorder(PdfPCell.NO_BORDER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(" ", NORMAL));
                cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                cell.setColspan(20);
                cell.setBorder(Rectangle.RIGHT);
                table.addCell(cell);
            }
        }

        cell = new PdfPCell(new Phrase("Total (Bs.):", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
        cell.setColspan(80);
        cell.setBorder(Rectangle.LEFT | Rectangle.TOP | Rectangle.BOTTOM);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(Redondeo.formatear_csm(total), NORMAL));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setColspan(20);
        cell.setBorder(Rectangle.TOP | Rectangle.RIGHT | Rectangle.BOTTOM);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("SON: ", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setColspan(10);
        cell.setBorder(Rectangle.LEFT | Rectangle.BOTTOM);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(NumberToLetterConverter.convertNumberToLetterComprobante(total), NORMAL));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setColspan(90);
        cell.setBorder(Rectangle.RIGHT | Rectangle.BOTTOM);
        table.addCell(cell);

        //firmas
        cell = new PdfPCell(new Phrase("______________________________", NORMAL));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setVerticalAlignment(PdfPCell.ALIGN_BOTTOM);
        cell.setColspan(50);
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setFixedHeight(50f);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("______________________________", NORMAL));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setVerticalAlignment(PdfPCell.ALIGN_BOTTOM);
        cell.setColspan(50);
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setFixedHeight(50f);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Firma Estudiante", NORMAL));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setColspan(50);
        cell.setBorder(PdfPCell.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Firma Cajero", NORMAL));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setColspan(50);
        cell.setBorder(PdfPCell.NO_BORDER);
        table.addCell(cell);

        //usuario
        if (comprobante.getUsuario() != null) {
            cell = new PdfPCell(new Phrase(comprobante.getUsuario().getUsuario(), ITALIC));
            cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
            cell.setColspan(100);
            cell.setBorder(PdfPCell.NO_BORDER);
            table.addCell(cell);
        }

        return table;
    }

    public PdfPTable cuenta(HttpServletRequest request, Estudiante estudiante) throws BadElementException, IOException {
        PdfPTable table = new PdfPTable(100);

        Phrase phrase = new Phrase();
        phrase.add(new Chunk("INSTRUCCIONES DE USO", BOLDITALIC));
        PdfPCell cell = new PdfPCell(phrase);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(100);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        phrase = new Phrase();
        phrase.add(new Chunk(" ", BOLDITALIC));
        cell = new PdfPCell(phrase);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(100);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        phrase = new Phrase();
        phrase.add(new Chunk("Para ingresar al sistema:\n\n", ITALIC));
        phrase.add(new Chunk("1) Abre un navegador web y dirigete a la siguiente dirección ", ITALIC));

        String requestURL = request.getRequestURL().toString().replaceAll(request.getRequestURI(), "");
        phrase.add(new Chunk(requestURL + "\n", BOLDITALIC));

        phrase.add(new Chunk("2) Ingresa tu usuario y contraseña\n\n", ITALIC));
        phrase.add(new Chunk("Usuario: ", BOLDITALIC));
        phrase.add(new Chunk(estudiante.getUsuario() + "\n", ITALIC));
        phrase.add(new Chunk("Contraseña: ", BOLDITALIC));
        phrase.add(new Chunk(estudiante.getContrasenaSinEncriptar() + "\n\n", ITALIC));
        phrase.add(new Chunk("3) Utiliza el menu para acceder a las opciones del sistema\n\n", ITALIC));
        phrase.add(new Chunk(
                "En el sistema pordras realizar tu Inscripción por Internet, revisar tu Historial Académico, "
                + "revisar tu Historial Económico, llenar tu cuadernillo de pasantía y actualizar tus datos personales.",
                BOLDITALIC));
        cell = new PdfPCell(phrase);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(100);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        return table;
    }
}
