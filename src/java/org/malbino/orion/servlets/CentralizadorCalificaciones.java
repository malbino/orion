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
import org.malbino.orion.pojos.centralizador.Centralizador;
import org.malbino.orion.pojos.centralizador.EstudianteCentralizador;
import org.malbino.orion.pojos.centralizador.GrupoCentralizador;
import org.malbino.orion.pojos.centralizador.MateriaCentralizador;
import org.malbino.orion.pojos.centralizador.PaginaCentralizador;
import org.malbino.orion.pojos.centralizador.PaginaEstadisticas;
import org.malbino.orion.pojos.centralizador.PaginaNotas;

/**
 *
 * @author tincho
 */
@WebServlet(name = "CentralizadorCalificaciones", urlPatterns = {"/reportes/centralizadores/centralizadorCalificaciones/CentralizadorCalificaciones", "/reportes/centralizadores/centralizadorCalificacionesPRAE/CentralizadorCalificaciones", "/reportes/centralizadores/centralizadorCalificacionesHomologacion/CentralizadorCalificaciones"})
public class CentralizadorCalificaciones extends HttpServlet {

    private static final String CONTENIDO_PDF = "application/pdf";

    private static final Font TITULO = FontFactory.getFont(FontFactory.HELVETICA, 14, Font.BOLD, BaseColor.BLACK);
    private static final Font SUBTITULO = FontFactory.getFont(FontFactory.HELVETICA, 9, Font.BOLD, BaseColor.BLACK);
    private static final Font NEGRITA = FontFactory.getFont(FontFactory.HELVETICA, 7, Font.BOLD, BaseColor.BLACK);
    private static final Font NORMAL = FontFactory.getFont(FontFactory.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK);
    private static final Font NEGRITA_PEQUENA = FontFactory.getFont(FontFactory.HELVETICA, 6, Font.BOLD, BaseColor.BLACK);

    //8.5 x 13 pulgadas (1 pulgada = 72 puntos)
    private static final Rectangle OFICIO_FOLIO = new Rectangle(612, 936);
    private static final int MARGEN_IZQUIERDO = -80;
    private static final int MARGEN_DERECHO = -80;
    private static final int MARGEN_SUPERIOR = 30;
    private static final int MARGEN_INFERIOR = 30;

    private static final String TITULO_CC = "CENTRALIZADOR DE CALIFICACIONES";

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
                if (paginaCentralizador instanceof PaginaNotas) {
                    PaginaNotas paginaNotas = (PaginaNotas) paginaCentralizador;

                    PdfPTable table = new PdfPTable(90);

                    //titulo
                    Phrase phrase = new Phrase();
                    phrase.add(new Chunk("Código de registro: ", NEGRITA));
                    phrase.add(new Chunk(paginaNotas.getCodigoRegistro(), NORMAL));
                    PdfPCell cell = new PdfPCell(phrase);
                    cell.setColspan(90);
                    cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
                    cell.setBorder(Rectangle.NO_BORDER);
                    table.addCell(cell);

                    String realPath = getServletContext().getRealPath("/resources/images/nuevoLogoMinisterio.png");
                    Image image = Image.getInstance(realPath);
                    image.setAlignment(Image.ALIGN_CENTER);
                    cell = new PdfPCell();
                    cell.addElement(image);
                    cell.setColspan(20);
                    cell.setRowspan(2);
                    cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
                    cell.setBorder(PdfPCell.NO_BORDER);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase(paginaNotas.getTitulo(), TITULO));
                    cell.setColspan(55);
                    cell.setRowspan(2);
                    cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                    cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
                    cell.setBorder(Rectangle.NO_BORDER);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase(" ", SUBTITULO));
                    cell.setColspan(15);
                    cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                    cell.setBorder(PdfPCell.NO_BORDER);
                    table.addCell(cell);

                    phrase = new Phrase();
                    phrase.add(new Chunk("LIBRO Nº ", SUBTITULO));
                    phrase.add(new Chunk(paginaNotas.getNumeroLibro().toString(), SUBTITULO));
                    phrase.add(new Chunk("\nFOLIO Nº ", SUBTITULO));
                    phrase.add(new Chunk(paginaNotas.getNumeroFolio().toString(), SUBTITULO));
                    cell = new PdfPCell(phrase);
                    cell.setColspan(15);
                    cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                    cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase(centralizador.getUbicacion(), NEGRITA));
                    cell.setColspan(20);
                    cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                    cell.setBorder(Rectangle.NO_BORDER);
                    cell.setBackgroundColor(new BaseColor(255, 255, 0));
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase(" ", NEGRITA));
                    cell.setColspan(55);
                    cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                    cell.setBorder(PdfPCell.NO_BORDER);
                    table.addCell(cell);

                    phrase = new Phrase();
                    phrase.add(new Chunk("TURNO: ", NEGRITA));
                    phrase.add(new Chunk(paginaNotas.getTurno(), NORMAL));
                    cell = new PdfPCell(phrase);
                    cell.setColspan(15);
                    cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
                    cell.setBorder(Rectangle.NO_BORDER);
                    table.addCell(cell);

                    phrase = new Phrase();
                    phrase.add(new Chunk("INSTITUCIÓN: ", NEGRITA));
                    phrase.add(new Chunk(centralizador.getInstitucion(), NORMAL));
                    cell = new PdfPCell(phrase);
                    cell.setColspan(55);
                    cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
                    cell.setBorder(Rectangle.NO_BORDER);
                    table.addCell(cell);

                    phrase = new Phrase();
                    phrase.add(new Chunk("R.M.: ", NEGRITA));
                    phrase.add(new Chunk(centralizador.getResolucionMinisterial(), NORMAL));
                    cell = new PdfPCell(phrase);
                    cell.setColspan(20);
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
                    cell = new PdfPCell(new Phrase("GESTIÓN:", NEGRITA));
                    cell.setColspan(6);
                    cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
                    cell.setBorder(Rectangle.BOTTOM | Rectangle.LEFT | Rectangle.TOP);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase(paginaNotas.getGestion(), NORMAL));
                    cell.setColspan(18);
                    cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
                    cell.setBorder(Rectangle.TOP | Rectangle.RIGHT | Rectangle.BOTTOM);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase("CÉDULA DE IDENTIDAD", NEGRITA));
                    cell.setColspan(6);
                    cell.setRowspan(6);
                    cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                    cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
                    cell.setBackgroundColor(new BaseColor(216, 228, 188));
                    table.addCell(cell);

                    for (MateriaCentralizador materiaCentralizador : paginaNotas.getMateriasCentralizador()) {
                        cell = new PdfPCell(new Phrase(materiaCentralizador.getCodigo(), NEGRITA_PEQUENA));
                        cell.setColspan(4);
                        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                        cell.setBackgroundColor(new BaseColor(155, 187, 89));
                        table.addCell(cell);
                    }

                    cell = new PdfPCell(new Phrase("ESTADO", NEGRITA));
                    cell.setColspan(7);
                    cell.setRowspan(6);
                    cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                    cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
                    cell.setRotation(90);
                    cell.setBackgroundColor(new BaseColor(216, 228, 188));
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase("OBSERVACIONES", NEGRITA));
                    cell.setColspan(13);
                    cell.setRowspan(6);
                    cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                    cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
                    cell.setRotation(90);
                    cell.setBackgroundColor(new BaseColor(216, 228, 188));
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase("NIVEL:", NEGRITA));
                    cell.setColspan(6);
                    cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
                    cell.setBorder(Rectangle.BOTTOM | Rectangle.LEFT | Rectangle.TOP);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase(paginaNotas.getNivel(), NORMAL));
                    cell.setColspan(18);
                    cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
                    cell.setBorder(Rectangle.TOP | Rectangle.RIGHT | Rectangle.BOTTOM);
                    table.addCell(cell);

                    for (MateriaCentralizador materiaCentralizador : paginaNotas.getMateriasCentralizador()) {
                        cell = new PdfPCell(new Phrase(materiaCentralizador.getNombre(), NEGRITA_PEQUENA));
                        cell.setColspan(4);
                        cell.setRowspan(5);
                        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
                        cell.setRotation(90);
                        cell.setFixedHeight(90);
                        cell.setBackgroundColor(new BaseColor(216, 228, 188));
                        table.addCell(cell);
                    }

                    cell = new PdfPCell(new Phrase("CARRERA:", NEGRITA));
                    cell.setColspan(6);
                    cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
                    cell.setBorder(Rectangle.BOTTOM | Rectangle.LEFT | Rectangle.TOP);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase(paginaNotas.getCarrera(), NORMAL));
                    cell.setColspan(18);
                    cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
                    cell.setBorder(Rectangle.TOP | Rectangle.RIGHT | Rectangle.BOTTOM);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase("RÉGIMEN:", NEGRITA));
                    cell.setColspan(6);
                    cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
                    cell.setBorder(Rectangle.BOTTOM | Rectangle.LEFT | Rectangle.TOP);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase(paginaNotas.getRegimen(), NORMAL));
                    cell.setColspan(18);
                    cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
                    cell.setBorder(Rectangle.TOP | Rectangle.RIGHT | Rectangle.BOTTOM);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase("CURSO:", NEGRITA));
                    cell.setColspan(6);
                    cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
                    cell.setBorder(Rectangle.BOTTOM | Rectangle.LEFT | Rectangle.TOP);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase(paginaNotas.getCurso(), NORMAL));
                    cell.setColspan(18);
                    cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
                    cell.setBorder(Rectangle.TOP | Rectangle.RIGHT | Rectangle.BOTTOM);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase("Nº", NEGRITA));
                    cell.setColspan(2);
                    cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                    cell.setVerticalAlignment(PdfPCell.ALIGN_BOTTOM);
                    cell.setBackgroundColor(new BaseColor(216, 228, 188));
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase("NÓMINA ESTUDIANTES", NEGRITA));
                    cell.setColspan(22);
                    cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                    cell.setVerticalAlignment(PdfPCell.ALIGN_BOTTOM);
                    cell.setBackgroundColor(new BaseColor(216, 228, 188));
                    table.addCell(cell);

                    for (EstudianteCentralizador estudianteCentralizador : paginaNotas.getEstudiantesCentralizador()) {
                        cell = new PdfPCell(new Phrase(estudianteCentralizador.getNumero(), NORMAL));
                        cell.setColspan(2);
                        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
                        table.addCell(cell);

                        cell = new PdfPCell(new Phrase(estudianteCentralizador.getNombre(), NORMAL));
                        cell.setColspan(22);
                        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
                        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
                        table.addCell(cell);

                        cell = new PdfPCell(new Phrase(estudianteCentralizador.getCi(), NORMAL));
                        cell.setColspan(6);
                        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
                        table.addCell(cell);

                        for (String nota : estudianteCentralizador.getNotas()) {
                            cell = new PdfPCell(new Phrase(nota, NORMAL));
                            cell.setColspan(4);
                            cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                            cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
                            table.addCell(cell);
                        }

                        cell = new PdfPCell(new Phrase(estudianteCentralizador.getEstado(), NORMAL));
                        cell.setColspan(7);
                        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
                        table.addCell(cell);

                        cell = new PdfPCell(new Phrase(estudianteCentralizador.getObservacion(), NORMAL));
                        cell.setColspan(13);
                        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
                        table.addCell(cell);
                    }

                    if (paginaNotas.getTitulo().equals(TITULO_CC)) {
                        cell = new PdfPCell(new Phrase(paginaNotas.getNota(), NEGRITA_PEQUENA));
                        cell.setColspan(90);
                        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
                        cell.setBorder(PdfPCell.NO_BORDER);
                        table.addCell(cell);
                    }

                    //firmas
                    cell = new PdfPCell(new Phrase("......................................................................", NORMAL));
                    cell.setColspan(23);
                    cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                    cell.setVerticalAlignment(PdfPCell.ALIGN_BOTTOM);
                    cell.setBorder(PdfPCell.NO_BORDER);
                    cell.setFixedHeight(50);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase("......................................................................", NORMAL));
                    cell.setColspan(22);
                    cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                    cell.setVerticalAlignment(PdfPCell.ALIGN_BOTTOM);
                    cell.setBorder(PdfPCell.NO_BORDER);
                    cell.setFixedHeight(50);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase("......................................................................", NORMAL));
                    cell.setColspan(23);
                    cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                    cell.setVerticalAlignment(PdfPCell.ALIGN_BOTTOM);
                    cell.setBorder(PdfPCell.NO_BORDER);
                    cell.setFixedHeight(50);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase(" ", NORMAL));
                    cell.setColspan(22);
                    cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                    cell.setVerticalAlignment(PdfPCell.ALIGN_BOTTOM);
                    cell.setBorder(PdfPCell.NO_BORDER);
                    cell.setFixedHeight(50);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase("JEFE(A) DE CARRERA", NORMAL));
                    cell.setColspan(23);
                    cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                    cell.setVerticalAlignment(PdfPCell.ALIGN_TOP);
                    cell.setBorder(PdfPCell.NO_BORDER);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase("DIRECTOR(A) ACADÉMICO(A)", NORMAL));
                    cell.setColspan(22);
                    cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                    cell.setVerticalAlignment(PdfPCell.ALIGN_TOP);
                    cell.setBorder(PdfPCell.NO_BORDER);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase("RECTOR(A)", NORMAL));
                    cell.setColspan(23);
                    cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                    cell.setVerticalAlignment(PdfPCell.ALIGN_TOP);
                    cell.setBorder(PdfPCell.NO_BORDER);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase("SELLO S.E.S.", NORMAL));
                    cell.setColspan(22);
                    cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                    cell.setVerticalAlignment(PdfPCell.ALIGN_TOP);
                    cell.setBorder(PdfPCell.NO_BORDER);
                    table.addCell(cell);

                    document.add(table);
                    document.newPage();
                } else if (paginaCentralizador instanceof PaginaEstadisticas) {
                    PaginaEstadisticas paginaEstadisticas = (PaginaEstadisticas) paginaCentralizador;

                    document.add(new Phrase(" "));

                    PdfPTable table = new PdfPTable(100);

                    //firmas
                    GrupoCentralizador[] gruposCentralizador = paginaEstadisticas.getGruposCentralizador();
                    for (int i = 0; i < 12; i++) {
                        if (i < gruposCentralizador.length) {
                            Phrase phrase = new Phrase();
                            phrase.add(new Chunk(gruposCentralizador[i].getDocente(), NORMAL));
                            phrase.add(new Chunk("\n", NORMAL));
                            phrase.add(new Chunk(gruposCentralizador[i].getMateria(), NORMAL));
                            PdfPCell cell = new PdfPCell(phrase);
                            cell.setColspan(25);
                            cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                            cell.setVerticalAlignment(PdfPCell.ALIGN_TOP);
                            cell.setBorder(PdfPCell.NO_BORDER);
                            cell.setPaddingTop(100);
                            table.addCell(cell);
                        } else {
                            Phrase phrase = new Phrase();
                            phrase.add(new Chunk(" ", NORMAL));
                            phrase.add(new Chunk("\n", NORMAL));
                            phrase.add(new Chunk(" ", NORMAL));
                            PdfPCell cell = new PdfPCell(phrase);
                            cell.setColspan(25);
                            cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                            cell.setVerticalAlignment(PdfPCell.ALIGN_TOP);
                            cell.setBorder(PdfPCell.NO_BORDER);
                            cell.setPaddingTop(100);
                            table.addCell(cell);
                        }
                    }

                    //estadisticas
                    PdfPCell cell = new PdfPCell(new Phrase(" ", NORMAL));
                    cell.setColspan(35);
                    cell.setBorder(PdfPCell.NO_BORDER);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase("ESTADISTICAS", SUBTITULO));
                    cell.setColspan(30);
                    cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                    cell.setBorder(PdfPCell.NO_BORDER);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase(" ", NORMAL));
                    cell.setColspan(35);
                    cell.setBorder(PdfPCell.NO_BORDER);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase(" ", SUBTITULO));
                    cell.setColspan(100);
                    cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                    cell.setBorder(PdfPCell.NO_BORDER);
                    table.addCell(cell);

                    //titulos
                    cell = new PdfPCell(new Phrase(" ", NORMAL));
                    cell.setColspan(35);
                    cell.setBorder(PdfPCell.NO_BORDER);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase("DETALLE", NEGRITA));
                    cell.setColspan(20);
                    cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                    cell.setBackgroundColor(new BaseColor(216, 228, 188));
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase("CANTIDAD", NEGRITA));
                    cell.setColspan(5);
                    cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                    cell.setBackgroundColor(new BaseColor(216, 228, 188));
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase("%", NEGRITA));
                    cell.setColspan(5);
                    cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                    cell.setBackgroundColor(new BaseColor(216, 228, 188));
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase(" ", NORMAL));
                    cell.setColspan(35);
                    cell.setBorder(PdfPCell.NO_BORDER);
                    table.addCell(cell);

                    //inscritos
                    cell = new PdfPCell(new Phrase(" ", NORMAL));
                    cell.setColspan(35);
                    cell.setBorder(PdfPCell.NO_BORDER);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase("ESTUDIANTES INSCRITOS", NORMAL));
                    cell.setColspan(20);
                    cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase(String.valueOf(paginaEstadisticas.getCantidadInscritos()), NORMAL));
                    cell.setColspan(5);
                    cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase(paginaEstadisticas.getPorcentajeInscritos() + " %", NORMAL));
                    cell.setColspan(5);
                    cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase(" ", NORMAL));
                    cell.setColspan(35);
                    cell.setBorder(PdfPCell.NO_BORDER);
                    table.addCell(cell);

                    //aprobados
                    cell = new PdfPCell(new Phrase(" ", NORMAL));
                    cell.setColspan(35);
                    cell.setBorder(PdfPCell.NO_BORDER);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase("ESTUDIANTES APROBADOS", NORMAL));
                    cell.setColspan(20);
                    cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase(String.valueOf(paginaEstadisticas.getCantidadAprobados()), NORMAL));
                    cell.setColspan(5);
                    cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase(((PaginaEstadisticas) paginaCentralizador).getPorcentajeAprobados() + " %", NORMAL));
                    cell.setColspan(5);
                    cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase(" ", NORMAL));
                    cell.setColspan(35);
                    cell.setBorder(PdfPCell.NO_BORDER);
                    table.addCell(cell);

                    //reprobados
                    cell = new PdfPCell(new Phrase(" ", NORMAL));
                    cell.setColspan(35);
                    cell.setBorder(PdfPCell.NO_BORDER);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase("ESTUDIANTES REPROBADOS", NORMAL));
                    cell.setColspan(20);
                    cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase(String.valueOf(paginaEstadisticas.getCantidadReprobados()), NORMAL));
                    cell.setColspan(5);
                    cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase(paginaEstadisticas.getPorcentajeReprobados() + " %", NORMAL));
                    cell.setColspan(5);
                    cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase(" ", NORMAL));
                    cell.setColspan(35);
                    cell.setBorder(PdfPCell.NO_BORDER);
                    table.addCell(cell);

                    //no se presentaron
                    cell = new PdfPCell(new Phrase(" ", NORMAL));
                    cell.setColspan(35);
                    cell.setBorder(PdfPCell.NO_BORDER);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase("ABANDONO", NORMAL));
                    cell.setColspan(20);
                    cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase(String.valueOf(paginaEstadisticas.getCantidadNoSePresento()), NORMAL));
                    cell.setColspan(5);
                    cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase(paginaEstadisticas.getPorcentajeNoSePresento() + " %", NORMAL));
                    cell.setColspan(5);
                    cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase(" ", NORMAL));
                    cell.setColspan(35);
                    cell.setBorder(PdfPCell.NO_BORDER);
                    table.addCell(cell);

                    document.add(table);
                    document.newPage();
                }
            }
        }

        document.close();
    }
}
