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
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.malbino.orion.facades.negocio.CentralizadorCalificacionesFacade.Centralizador;
import org.malbino.orion.facades.negocio.CentralizadorCalificacionesFacade.EstudianteCentralizador;
import org.malbino.orion.facades.negocio.CentralizadorCalificacionesFacade.MateriaCentralizador;
import org.malbino.orion.facades.negocio.CentralizadorCalificacionesFacade.PaginaCentralizador;

/**
 *
 * @author tincho
 */
@WebServlet(name = "CentralizadorCalificaciones", urlPatterns = {"/reportes/CentralizadorCalificaciones"})
public class CentralizadorCalificaciones extends HttpServlet {

    private static final String CONTENIDO_PDF = "application/pdf";

    private static final Font TITULO = FontFactory.getFont(FontFactory.HELVETICA, 16, Font.BOLD, BaseColor.BLACK);
    private static final Font NEGRITA = FontFactory.getFont(FontFactory.HELVETICA, 8, Font.BOLD, BaseColor.BLACK);
    private static final Font NORMAL = FontFactory.getFont(FontFactory.HELVETICA, 8, Font.NORMAL, BaseColor.BLACK);
    private static final Font NEGRITA_PEQUENA = FontFactory.getFont(FontFactory.HELVETICA, 7, Font.BOLD, BaseColor.BLACK);
    private static final Font PEQUENA = FontFactory.getFont(FontFactory.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK);
    private static final Font NEGRITA_PLOMO = FontFactory.getFont(FontFactory.HELVETICA, 8, Font.BOLD, new BaseColor(214, 214, 214));

    //8.5 x 13 pulgadas (1 pulgada = 72 puntos)
    private static final Rectangle OFICIO_FOLIO = new Rectangle(612, 936);
    private static final int MARGEN_IZQUIERDO = -80;
    private static final int MARGEN_DERECHO = -80;
    private static final int MARGEN_SUPERIOR = 30;
    private static final int MARGEN_INFERIOR = 30;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.generarPDF(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.generarPDF(request, response);
    }

    public void generarPDF(HttpServletRequest request, HttpServletResponse response) {
        Centralizador centralizador = (Centralizador) request.getSession().getAttribute("centralizador");

        if (centralizador != null) {
            try {
                response.setContentType(CONTENIDO_PDF);

                Document document = new Document(OFICIO_FOLIO.rotate(), MARGEN_IZQUIERDO, MARGEN_DERECHO, MARGEN_SUPERIOR, MARGEN_INFERIOR);
                PdfWriter.getInstance(document, response.getOutputStream());

                generarDocumento(document, centralizador);
            } catch (IOException | DocumentException ex) {

            }
        }

    }

    public void generarDocumento(Document document, Centralizador centralizador) throws DocumentException, BadElementException, IOException {
        document.open();

        if (centralizador.getPaginasCentralizador().size() > 0) {
            for (PaginaCentralizador paginaCentralizador : centralizador.getPaginasCentralizador()) { //paginas centralizador
                PdfPTable table = new PdfPTable(70);

                //titulo
                String realPath = getServletContext().getRealPath("/resources/images/logoMinisterio.jpg");
                Image image = Image.getInstance(realPath);
                image.scalePercent(28);
                image.setAlignment(Image.ALIGN_CENTER);
                PdfPCell cell = new PdfPCell();
                cell.addElement(image);
                cell.setColspan(10);
                cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
                cell.setBorder(PdfPCell.NO_BORDER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(" ", NEGRITA));
                cell.setColspan(45);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);

                Phrase phrase = new Phrase();
                phrase.add(new Chunk("Código de registro: ", NEGRITA));
                phrase.add(new Chunk(paginaCentralizador.getCodigoRegistro(), NORMAL));
                cell = new PdfPCell(phrase);
                cell.setColspan(15);
                cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(centralizador.getUbicacion(), NEGRITA));
                cell.setColspan(10);
                cell.setRowspan(4);
                cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("CENTRALIZADOR DE CALIFICACIONES", TITULO));
                cell.setColspan(45);
                cell.setRowspan(4);
                cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(" ", NEGRITA));
                cell.setColspan(15);
                cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                cell.setBorder(PdfPCell.LEFT | PdfPCell.TOP | PdfPCell.RIGHT);
                table.addCell(cell);

                phrase = new Phrase();
                phrase.add(new Chunk("Nº LIBRO: ", NEGRITA));
                phrase.add(new Chunk(paginaCentralizador.getNumeroLibro().toString(), NORMAL));
                cell = new PdfPCell(phrase);
                cell.setColspan(15);
                cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                cell.setBorder(PdfPCell.LEFT | PdfPCell.RIGHT);
                table.addCell(cell);

                phrase = new Phrase();
                phrase.add(new Chunk("Nº FOLIO: ", NEGRITA));
                phrase.add(new Chunk(paginaCentralizador.getNumeroFolio().toString(), NORMAL));
                cell = new PdfPCell(phrase);
                cell.setColspan(15);
                cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                cell.setBorder(PdfPCell.LEFT | PdfPCell.RIGHT);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(" ", NEGRITA));
                cell.setColspan(15);
                cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                cell.setBorder(PdfPCell.LEFT | PdfPCell.RIGHT | PdfPCell.BOTTOM);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(" ", NEGRITA));
                cell.setColspan(55);
                cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                cell.setBorder(PdfPCell.NO_BORDER);
                table.addCell(cell);

                phrase = new Phrase();
                phrase.add(new Chunk("TURNO: ", NEGRITA));
                phrase.add(new Chunk(paginaCentralizador.getTurno(), NORMAL));
                cell = new PdfPCell(phrase);
                cell.setColspan(15);
                cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);

                phrase = new Phrase();
                phrase.add(new Chunk("INSTITUCIÓN: ", NEGRITA));
                phrase.add(new Chunk(centralizador.getInstitucion(), NORMAL));
                cell = new PdfPCell(phrase);
                cell.setColspan(40);
                cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);

                phrase = new Phrase();
                phrase.add(new Chunk("R.M.: ", NEGRITA));
                phrase.add(new Chunk(centralizador.getResolucionMinisterial(), NORMAL));
                cell = new PdfPCell(phrase);
                cell.setColspan(15);
                cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);

                phrase = new Phrase();
                phrase.add(new Chunk("CARÁCTER: ", NEGRITA));
                phrase.add(new Chunk(centralizador.getCaracter(), NORMAL));
                cell = new PdfPCell(phrase);
                cell.setColspan(15);
                cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);

                //cuerpo
                phrase = new Phrase();
                phrase.add(new Chunk("GESTIÓN: ", NEGRITA));
                phrase.add(new Chunk(paginaCentralizador.getGestion(), NORMAL));
                cell = new PdfPCell(phrase);
                cell.setColspan(18);
                cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("CÉDULA DE IDENTIDAD", NEGRITA));
                cell.setColspan(6);
                cell.setRowspan(6);
                cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
                cell.setRotation(90);
                cell.setBackgroundColor(new BaseColor(214, 214, 214));
                table.addCell(cell);

                for (MateriaCentralizador materiaCentralizador : paginaCentralizador.getMateriasCentralizador()) {
                    cell = new PdfPCell(new Phrase(materiaCentralizador.getCodigo(), NEGRITA_PEQUENA));
                    cell.setColspan(4);
                    cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                    cell.setBackgroundColor(new BaseColor(186, 186, 186));
                    table.addCell(cell);
                }

                cell = new PdfPCell(new Phrase("OBSERVACIONES", NEGRITA));
                cell.setColspan(6);
                cell.setRowspan(6);
                cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
                cell.setRotation(90);
                cell.setBackgroundColor(new BaseColor(214, 214, 214));
                table.addCell(cell);

                phrase = new Phrase();
                phrase.add(new Chunk("NIVEL: ", NEGRITA));
                phrase.add(new Chunk(paginaCentralizador.getNivel(), NORMAL));
                cell = new PdfPCell(phrase);
                cell.setColspan(18);
                cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
                table.addCell(cell);

                for (MateriaCentralizador materiaCentralizador : paginaCentralizador.getMateriasCentralizador()) {
                    cell = new PdfPCell(new Phrase(materiaCentralizador.getNombre(), NEGRITA_PEQUENA));
                    cell.setColspan(4);
                    cell.setRowspan(5);
                    cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                    cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
                    cell.setRotation(90);
                    cell.setFixedHeight(90);
                    cell.setBackgroundColor(new BaseColor(214, 214, 214));
                    table.addCell(cell);
                }

                phrase = new Phrase();
                phrase.add(new Chunk("CARRERA: ", NEGRITA));
                phrase.add(new Chunk(paginaCentralizador.getCarrera(), NORMAL));
                cell = new PdfPCell(phrase);
                cell.setColspan(18);
                cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
                table.addCell(cell);

                phrase = new Phrase();
                phrase.add(new Chunk("RÉGIMEN: ", NEGRITA));
                phrase.add(new Chunk(paginaCentralizador.getRegimen(), NORMAL));
                cell = new PdfPCell(phrase);
                cell.setColspan(18);
                cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
                table.addCell(cell);

                phrase = new Phrase();
                phrase.add(new Chunk("CURSO: ", NEGRITA));
                phrase.add(new Chunk(paginaCentralizador.getCurso(), NORMAL));
                cell = new PdfPCell(phrase);
                cell.setColspan(18);
                cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("Nº", NEGRITA));
                cell.setColspan(2);
                cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                cell.setVerticalAlignment(PdfPCell.ALIGN_BOTTOM);
                cell.setBackgroundColor(new BaseColor(214, 214, 214));
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("NÓMINA ESTUDIANTES", NEGRITA));
                cell.setColspan(16);
                cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                cell.setVerticalAlignment(PdfPCell.ALIGN_BOTTOM);
                cell.setBackgroundColor(new BaseColor(214, 214, 214));
                table.addCell(cell);

                for (EstudianteCentralizador estudianteCentralizador : paginaCentralizador.getEstudiantesCentralizador()) {
                    cell = new PdfPCell(new Phrase(estudianteCentralizador.getNumero(), NORMAL));
                    cell.setColspan(2);
                    cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase(estudianteCentralizador.getNombre(), NORMAL));
                    cell.setColspan(16);
                    cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase(estudianteCentralizador.getCi(), NORMAL));
                    cell.setColspan(6);
                    cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                    table.addCell(cell);

                    for (String nota : estudianteCentralizador.getNotas()) {
                        cell = new PdfPCell(new Phrase(nota, NORMAL));
                        cell.setColspan(4);
                        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                        table.addCell(cell);
                    }

                    cell = new PdfPCell(new Phrase(estudianteCentralizador.getObservaciones(), NORMAL));
                    cell.setColspan(6);
                    cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                    table.addCell(cell);
                }

                cell = new PdfPCell(new Phrase("* En observaciones tomar en cuenta: APROBADO, REPROBADO y ABANDONO.", PEQUENA));
                cell.setColspan(70);
                cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
                cell.setBorder(PdfPCell.NO_BORDER);
                table.addCell(cell);

                //firmas
                cell = new PdfPCell(new Phrase("FIRMA JEFE DE CARRERA", NORMAL));
                cell.setColspan(17);
                cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                cell.setVerticalAlignment(PdfPCell.ALIGN_BOTTOM);
                cell.setBorder(PdfPCell.NO_BORDER);
                cell.setFixedHeight(80);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("FIRMA DIRECTOR ACADÉMICO", NORMAL));
                cell.setColspan(18);
                cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                cell.setVerticalAlignment(PdfPCell.ALIGN_BOTTOM);
                cell.setBorder(PdfPCell.NO_BORDER);
                cell.setFixedHeight(80);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("FIRMA RECTOR", NORMAL));
                cell.setColspan(18);
                cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                cell.setVerticalAlignment(PdfPCell.ALIGN_BOTTOM);
                cell.setBorder(PdfPCell.NO_BORDER);
                cell.setFixedHeight(80);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("SELLO INSTITUTO", NEGRITA_PLOMO));
                cell.setColspan(17);
                cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                cell.setVerticalAlignment(PdfPCell.ALIGN_BOTTOM);
                cell.setBorder(PdfPCell.NO_BORDER);
                cell.setFixedHeight(80);
                table.addCell(cell);

                document.add(table);
                document.newPage();
            }
        }
        
        document.close();
    }
}
