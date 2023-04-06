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
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.malbino.orion.entities.CarreraEstudiante;
import org.malbino.orion.entities.CarreraEstudiante.CarreraEstudianteId;
import org.malbino.orion.entities.Inscrito;
import org.malbino.orion.entities.Materia;
import org.malbino.orion.entities.Nota;
import org.malbino.orion.enums.Regimen;
import org.malbino.orion.facades.CarreraEstudianteFacade;
import org.malbino.orion.facades.InscritoFacade;
import org.malbino.orion.facades.NotaFacade;
import org.malbino.orion.facades.negocio.InscripcionesFacade;
import org.malbino.orion.util.Fecha;
import org.malbino.orion.util.Redondeo;

/**
 *
 * @author tincho
 */
@WebServlet(name = "BoletinNotas", urlPatterns = {"/reportes/notas/boletinNotas/BoletinNotas"})
public class BoletinNotas extends HttpServlet {

    private static final String CONTENIDO_PDF = "application/pdf";

    private static final Font TITULO = FontFactory.getFont(FontFactory.HELVETICA, 16, Font.NORMAL, BaseColor.BLACK);
    private static final Font NEGRITA = FontFactory.getFont(FontFactory.HELVETICA, 7, Font.BOLD, BaseColor.BLACK);
    private static final Font NORMAL = FontFactory.getFont(FontFactory.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK);
    private static final Font ESPACIO = FontFactory.getFont(FontFactory.HELVETICA, 2, Font.NORMAL, BaseColor.BLACK);
    private static final Font NORMAL_ITALICA = FontFactory.getFont(FontFactory.HELVETICA, 7, Font.ITALIC, BaseColor.BLACK);
    private static final Font NEGRITA_ITALICA = FontFactory.getFont(FontFactory.HELVETICA, 7, Font.BOLDITALIC, BaseColor.BLACK);
    private static final Font NEGRITA_PLOMO = FontFactory.getFont(FontFactory.HELVETICA, 7, Font.BOLD, BaseColor.LIGHT_GRAY);

    private static final int MARGEN_IZQUIERDO = -20;
    private static final int MARGEN_DERECHO = -20;
    private static final int MARGEN_SUPERIOR = 20;
    private static final int MARGEN_INFERIOR = 20;

    @EJB
    InscritoFacade inscritoFacade;
    @EJB
    CarreraEstudianteFacade carreraEstudianteFacade;
    @EJB
    NotaFacade notaFacade;
    @EJB
    InscripcionesFacade inscripcionesFacade;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.generarPDF(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.generarPDF(request, response);
    }

    public void generarPDF(HttpServletRequest request, HttpServletResponse response) {
        Integer id_inscrito = (Integer) request.getSession().getAttribute("id_inscrito");

        if (id_inscrito != null) {
            Inscrito inscrito = inscritoFacade.find(id_inscrito);

            try {
                response.setContentType(CONTENIDO_PDF);

                Document document = new Document(PageSize.HALFLETTER, MARGEN_IZQUIERDO, MARGEN_DERECHO, MARGEN_SUPERIOR, MARGEN_INFERIOR);
                PdfWriter.getInstance(document, response.getOutputStream());

                document.open();

                document.add(cabecera(inscrito));

                if (inscrito.getGestionAcademica().getRegimen().equals(Regimen.SEMESTRAL)) {
                    document.add(cuerpoSemestral(inscrito));
                }
                if (inscrito.getGestionAcademica().getRegimen().equals(Regimen.ANUAL)) {
                    document.add(cuerpoAnual(inscrito));
                }

                document.add(oferta(inscrito));
                document.add(pie(inscrito));

                document.close();
            } catch (IOException | DocumentException ex) {

            }
        }
    }

    public PdfPTable cabecera(Inscrito inscrito) throws BadElementException, IOException {
        PdfPTable table = new PdfPTable(100);

        PdfPCell cell = new PdfPCell(new Phrase("BOLETÍN DE NOTAS", TITULO));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setColspan(80);
        table.addCell(cell);

        String realPath = getServletContext().getRealPath("/resources/uploads/" + inscrito.getCarrera().getCampus().getInstituto().getLogo());
        Image image = Image.getInstance(realPath);
        image.scaleToFit(40, 40);
        image.setAlignment(Image.ALIGN_CENTER);
        cell = new PdfPCell();
        cell.addElement(image);
        cell.setColspan(20);
        cell.setRowspan(2);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(inscrito.getCarrera().getCampus().getInstituto().getNombreRegulador(), NORMAL));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setColspan(80);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(" ", NORMAL));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setColspan(100);
        cell.setBackgroundColor(BaseColor.GRAY);
        table.addCell(cell);

        return table;
    }

    public PdfPTable cuerpoSemestral(Inscrito inscrito) throws BadElementException, IOException {
        PdfPTable table = new PdfPTable(100);

        //fila 1
        PdfPCell cell = new PdfPCell(new Phrase("Matricula:", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setColspan(33);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Estudiante:", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setColspan(34);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Carrera:", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setColspan(33);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(String.valueOf(inscrito.getEstudiante().getMatricula()), NORMAL));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_TOP);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setColspan(33);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(inscrito.getEstudiante().toString(), NORMAL));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_TOP);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setColspan(34);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(inscrito.getCarrera().toString(), NORMAL));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_TOP);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setColspan(33);
        table.addCell(cell);

        //fial 2
        cell = new PdfPCell(new Phrase("Mención:", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setColspan(33);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Gestión Académica:", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setColspan(34);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Régimen:", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setColspan(33);
        table.addCell(cell);

        CarreraEstudianteId carreraEstudianteId = new CarreraEstudianteId(inscrito.getCarrera().getId_carrera(), inscrito.getEstudiante().getId_persona());
        CarreraEstudiante carreraEstudiante = carreraEstudianteFacade.find(carreraEstudianteId);
        if (carreraEstudiante != null && carreraEstudiante.getMencion() != null) {
            cell = new PdfPCell(new Phrase(carreraEstudiante.getMencion().getNombre(), NORMAL));
        } else {
            cell = new PdfPCell(new Phrase(" ", NORMAL));
        }
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_TOP);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setColspan(33);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(inscrito.getGestionAcademica().codigo(), NORMAL));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_TOP);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setColspan(34);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(inscrito.getGestionAcademica().getRegimen().getNombre(), NORMAL));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_TOP);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setColspan(33);
        table.addCell(cell);

        //notas
        cell = new PdfPCell(new Phrase("NOTAS", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setColspan(100);
        cell.setPaddingTop(8);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Codigo", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setColspan(10);
        cell.setBackgroundColor(BaseColor.GRAY);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Materia", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setColspan(40);
        cell.setBackgroundColor(BaseColor.GRAY);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("P1", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setColspan(7);
        cell.setBackgroundColor(BaseColor.GRAY);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("P2", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setColspan(7);
        cell.setBackgroundColor(BaseColor.GRAY);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("P3", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setColspan(7);
        cell.setBackgroundColor(BaseColor.GRAY);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("NF", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setColspan(7);
        cell.setBackgroundColor(BaseColor.GRAY);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("RE", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setColspan(7);
        cell.setBackgroundColor(BaseColor.GRAY);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Condición", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setColspan(15);
        cell.setBackgroundColor(BaseColor.GRAY);
        table.addCell(cell);

        List<Nota> notas = notaFacade.listaNotas(inscrito.getId_inscrito());
        for (int i = 0; i < notas.size(); i++) {
            Nota nota = notas.get(i);

            if (i % 2 == 0) {
                cell = new PdfPCell(new Phrase(nota.getMateria().getCodigo(), NORMAL));
                cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setColspan(10);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(nota.getMateria().getNombre(), NORMAL));
                cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
                cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setColspan(40);
                table.addCell(cell);

                if (nota.getNota1() != null) {
                    cell = new PdfPCell(new Phrase(String.valueOf(nota.getNota1()), NORMAL));
                } else {
                    cell = new PdfPCell(new Phrase(" ", NORMAL));
                }
                cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setColspan(7);
                table.addCell(cell);

                if (nota.getNota2() != null) {
                    cell = new PdfPCell(new Phrase(String.valueOf(nota.getNota2()), NORMAL));
                } else {
                    cell = new PdfPCell(new Phrase(" ", NORMAL));
                }
                cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setColspan(7);
                table.addCell(cell);

                if (nota.getNota3() != null) {
                    cell = new PdfPCell(new Phrase(String.valueOf(nota.getNota3()), NORMAL));
                } else {
                    cell = new PdfPCell(new Phrase(" ", NORMAL));
                }
                cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setColspan(7);
                table.addCell(cell);

                if (nota.getNotaFinal() != null) {
                    cell = new PdfPCell(new Phrase(String.valueOf(nota.getNotaFinal()), NORMAL));
                } else {
                    cell = new PdfPCell(new Phrase(" ", NORMAL));
                }
                cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setColspan(7);
                table.addCell(cell);

                if (nota.getRecuperatorio() != null) {
                    cell = new PdfPCell(new Phrase(String.valueOf(nota.getRecuperatorio()), NORMAL));
                } else {
                    cell = new PdfPCell(new Phrase(" ", NORMAL));
                }
                cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setColspan(7);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(nota.getCondicion().getNombre(), NORMAL));
                cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setColspan(15);
                table.addCell(cell);
            } else {
                cell = new PdfPCell(new Phrase(nota.getMateria().getCodigo(), NORMAL));
                cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setColspan(10);
                cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(nota.getMateria().getNombre(), NORMAL));
                cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
                cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setColspan(40);
                cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                table.addCell(cell);

                if (nota.getNota1() != null) {
                    cell = new PdfPCell(new Phrase(String.valueOf(nota.getNota1()), NORMAL));
                } else {
                    cell = new PdfPCell(new Phrase(" ", NORMAL));
                }
                cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setColspan(7);
                cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                table.addCell(cell);

                if (nota.getNota2() != null) {
                    cell = new PdfPCell(new Phrase(String.valueOf(nota.getNota2()), NORMAL));
                } else {
                    cell = new PdfPCell(new Phrase(" ", NORMAL));
                }
                cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setColspan(7);
                cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                table.addCell(cell);

                if (nota.getNota3() != null) {
                    cell = new PdfPCell(new Phrase(String.valueOf(nota.getNota3()), NORMAL));
                } else {
                    cell = new PdfPCell(new Phrase(" ", NORMAL));
                }
                cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setColspan(7);
                cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                table.addCell(cell);

                if (nota.getNotaFinal() != null) {
                    cell = new PdfPCell(new Phrase(String.valueOf(nota.getNotaFinal()), NORMAL));
                } else {
                    cell = new PdfPCell(new Phrase(" ", NORMAL));
                }
                cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setColspan(7);
                cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                table.addCell(cell);

                if (nota.getRecuperatorio() != null) {
                    cell = new PdfPCell(new Phrase(String.valueOf(nota.getRecuperatorio()), NORMAL));
                } else {
                    cell = new PdfPCell(new Phrase(" ", NORMAL));
                }
                cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setColspan(7);
                cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(nota.getCondicion().getNombre(), NORMAL));
                cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setColspan(15);
                cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                table.addCell(cell);
            }
        }

        cell = new PdfPCell(new Phrase("Promedio:", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setColspan(71);
        cell.setBackgroundColor(BaseColor.GRAY);
        table.addCell(cell);

        if (carreraEstudiante != null) {
            Double promedio = notaFacade.promedioBoletinNotas(inscrito.getEstudiante(), inscrito.getCarrera(), carreraEstudiante.getMencion(), inscrito.getGestionAcademica());
            int promedioGeneralRedondeado = Redondeo.redondear_HALFUP(promedio, 0).intValue();
            cell = new PdfPCell(new Phrase(String.valueOf(promedioGeneralRedondeado), NORMAL));
        } else {
            cell = new PdfPCell(new Phrase(" ", NORMAL));
        }
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setColspan(7);
        cell.setBackgroundColor(BaseColor.GRAY);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(" ", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setColspan(22);
        cell.setBackgroundColor(BaseColor.GRAY);
        table.addCell(cell);

        return table;
    }

    public PdfPTable cuerpoAnual(Inscrito inscrito) throws BadElementException, IOException {
        PdfPTable table = new PdfPTable(100);

        //fila 1
        PdfPCell cell = new PdfPCell(new Phrase("Matricula:", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setColspan(33);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Estudiante:", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setColspan(34);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Carrera:", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setColspan(33);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(String.valueOf(inscrito.getEstudiante().getMatricula()), NORMAL));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_TOP);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setColspan(33);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(inscrito.getEstudiante().toString(), NORMAL));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_TOP);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setColspan(34);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(inscrito.getCarrera().getNombre(), NORMAL));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_TOP);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setColspan(33);
        table.addCell(cell);

        //fila 2
        cell = new PdfPCell(new Phrase("Mención:", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setColspan(33);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Gestión Académica:", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setColspan(34);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Régimen:", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setColspan(33);
        table.addCell(cell);

        CarreraEstudianteId carreraEstudianteId = new CarreraEstudianteId(inscrito.getCarrera().getId_carrera(), inscrito.getEstudiante().getId_persona());
        CarreraEstudiante carreraEstudiante = carreraEstudianteFacade.find(carreraEstudianteId);
        if (carreraEstudiante != null && carreraEstudiante.getMencion() != null) {
            cell = new PdfPCell(new Phrase(carreraEstudiante.getMencion().getNombre(), NORMAL));
        } else {
            cell = new PdfPCell(new Phrase(" ", NORMAL));
        }
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_TOP);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setColspan(33);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(inscrito.getGestionAcademica().codigo(), NORMAL));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_TOP);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setColspan(34);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(inscrito.getGestionAcademica().getRegimen().getNombre(), NORMAL));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_TOP);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setColspan(33);
        table.addCell(cell);

        //notas
        cell = new PdfPCell(new Phrase("NOTAS", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setColspan(100);
        cell.setPaddingTop(8);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Codigo", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setColspan(10);
        cell.setBackgroundColor(BaseColor.GRAY);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Materia", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setColspan(33);
        cell.setBackgroundColor(BaseColor.GRAY);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("P1", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setColspan(7);
        cell.setBackgroundColor(BaseColor.GRAY);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("P2", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setColspan(7);
        cell.setBackgroundColor(BaseColor.GRAY);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("P3", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setColspan(7);
        cell.setBackgroundColor(BaseColor.GRAY);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("P4", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setColspan(7);
        cell.setBackgroundColor(BaseColor.GRAY);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("NF", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setColspan(7);
        cell.setBackgroundColor(BaseColor.GRAY);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("RE", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setColspan(7);
        cell.setBackgroundColor(BaseColor.GRAY);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Condición", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setColspan(15);
        cell.setBackgroundColor(BaseColor.GRAY);
        table.addCell(cell);

        List<Nota> notas = notaFacade.listaNotas(inscrito.getId_inscrito());
        for (int i = 0; i < notas.size(); i++) {
            Nota nota = notas.get(i);

            if (i % 2 == 0) {
                cell = new PdfPCell(new Phrase(nota.getMateria().getCodigo(), NORMAL));
                cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setColspan(10);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(nota.getMateria().getNombre(), NORMAL));
                cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
                cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setColspan(33);
                table.addCell(cell);

                if (nota.getNota1() != null) {
                    cell = new PdfPCell(new Phrase(String.valueOf(nota.getNota1()), NORMAL));
                } else {
                    cell = new PdfPCell(new Phrase(" ", NORMAL));
                }
                cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setColspan(7);
                table.addCell(cell);

                if (nota.getNota2() != null) {
                    cell = new PdfPCell(new Phrase(String.valueOf(nota.getNota2()), NORMAL));
                } else {
                    cell = new PdfPCell(new Phrase(" ", NORMAL));
                }
                cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setColspan(7);
                table.addCell(cell);

                if (nota.getNota3() != null) {
                    cell = new PdfPCell(new Phrase(String.valueOf(nota.getNota3()), NORMAL));
                } else {
                    cell = new PdfPCell(new Phrase(" ", NORMAL));
                }
                cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setColspan(7);
                table.addCell(cell);

                if (nota.getNota4() != null) {
                    cell = new PdfPCell(new Phrase(String.valueOf(nota.getNota4()), NORMAL));
                } else {
                    cell = new PdfPCell(new Phrase(" ", NORMAL));
                }
                cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setColspan(7);
                table.addCell(cell);

                if (nota.getNotaFinal() != null) {
                    cell = new PdfPCell(new Phrase(String.valueOf(nota.getNotaFinal()), NORMAL));
                } else {
                    cell = new PdfPCell(new Phrase(" ", NORMAL));
                }
                cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setColspan(7);
                table.addCell(cell);

                if (nota.getRecuperatorio() != null) {
                    cell = new PdfPCell(new Phrase(String.valueOf(nota.getRecuperatorio()), NORMAL));
                } else {
                    cell = new PdfPCell(new Phrase(" ", NORMAL));
                }
                cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setColspan(7);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(nota.getCondicion().getNombre(), NORMAL));
                cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setColspan(15);
                table.addCell(cell);
            } else {
                cell = new PdfPCell(new Phrase(nota.getMateria().getCodigo(), NORMAL));
                cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setColspan(10);
                cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(nota.getMateria().getNombre(), NORMAL));
                cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
                cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setColspan(33);
                cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                table.addCell(cell);

                if (nota.getNota1() != null) {
                    cell = new PdfPCell(new Phrase(String.valueOf(nota.getNota1()), NORMAL));
                } else {
                    cell = new PdfPCell(new Phrase(" ", NORMAL));
                }
                cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setColspan(7);
                cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                table.addCell(cell);

                if (nota.getNota2() != null) {
                    cell = new PdfPCell(new Phrase(String.valueOf(nota.getNota2()), NORMAL));
                } else {
                    cell = new PdfPCell(new Phrase(" ", NORMAL));
                }
                cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setColspan(7);
                cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                table.addCell(cell);

                if (nota.getNota3() != null) {
                    cell = new PdfPCell(new Phrase(String.valueOf(nota.getNota3()), NORMAL));
                } else {
                    cell = new PdfPCell(new Phrase(" ", NORMAL));
                }
                cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setColspan(7);
                cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                table.addCell(cell);

                if (nota.getNota4() != null) {
                    cell = new PdfPCell(new Phrase(String.valueOf(nota.getNota4()), NORMAL));
                } else {
                    cell = new PdfPCell(new Phrase(" ", NORMAL));
                }
                cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setColspan(7);
                cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                table.addCell(cell);

                if (nota.getNotaFinal() != null) {
                    cell = new PdfPCell(new Phrase(String.valueOf(nota.getNotaFinal()), NORMAL));
                } else {
                    cell = new PdfPCell(new Phrase(" ", NORMAL));
                }
                cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setColspan(7);
                cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                table.addCell(cell);

                if (nota.getRecuperatorio() != null) {
                    cell = new PdfPCell(new Phrase(String.valueOf(nota.getRecuperatorio()), NORMAL));
                } else {
                    cell = new PdfPCell(new Phrase(" ", NORMAL));
                }
                cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setColspan(7);
                cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(nota.getCondicion().getNombre(), NORMAL));
                cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setColspan(15);
                cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                table.addCell(cell);
            }
        }

        cell = new PdfPCell(new Phrase("Promedio:", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setColspan(71);
        cell.setBackgroundColor(BaseColor.GRAY);
        table.addCell(cell);

        if (carreraEstudiante != null) {
            Double promedio = notaFacade.promedioBoletinNotas(inscrito.getEstudiante(), inscrito.getCarrera(), carreraEstudiante.getMencion(), inscrito.getGestionAcademica());
            int promedioGeneralRedondeado = Redondeo.redondear_HALFUP(promedio, 0).intValue();
            cell = new PdfPCell(new Phrase(String.valueOf(promedioGeneralRedondeado), NORMAL));
        } else {
            cell = new PdfPCell(new Phrase(" ", NORMAL));
        }
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setColspan(7);
        cell.setBackgroundColor(BaseColor.GRAY);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(" ", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setColspan(22);
        cell.setBackgroundColor(BaseColor.GRAY);
        table.addCell(cell);

        return table;
    }

    public PdfPTable oferta(Inscrito inscrito) throws BadElementException, IOException {
        PdfPTable table = new PdfPTable(100);

        //notas
        PdfPCell cell = new PdfPCell(new Phrase("OFERTA DE MATERIAS", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setColspan(100);
        cell.setPaddingTop(8);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Codigo", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setColspan(10);
        cell.setBackgroundColor(BaseColor.GRAY);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Materia", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setColspan(50);
        cell.setBackgroundColor(BaseColor.GRAY);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Nivel", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setColspan(20);
        cell.setBackgroundColor(BaseColor.GRAY);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Prerequisito", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setColspan(20);
        cell.setBackgroundColor(BaseColor.GRAY);
        table.addCell(cell);

        List<Materia> oferta = inscripcionesFacade.ofertaBoletinNotas(inscrito);
        for (int i = 0; i < oferta.size(); i++) {
            Materia materia = oferta.get(i);

            if (i % 2 == 0) {
                cell = new PdfPCell(new Phrase(materia.getCodigo(), NORMAL));
                cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setColspan(10);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(materia.getNombre(), NORMAL));
                cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
                cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setColspan(50);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(materia.getNivel().getNombre(), NORMAL));
                cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setColspan(20);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(materia.prerequisitosToString(), NORMAL));
                cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setColspan(20);
                table.addCell(cell);
            } else {
                cell = new PdfPCell(new Phrase(materia.getCodigo(), NORMAL));
                cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setColspan(10);
                cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(materia.getNombre(), NORMAL));
                cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
                cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setColspan(50);
                cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(materia.getNivel().getNombre(), NORMAL));
                cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setColspan(20);
                cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(materia.prerequisitosToString(), NORMAL));
                cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setColspan(20);
                cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                table.addCell(cell);
            }
        }

        cell = new PdfPCell(new Phrase(" ", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setColspan(100);
        cell.setBackgroundColor(BaseColor.GRAY);
        table.addCell(cell);

        return table;
    }

    public PdfPTable pie(Inscrito inscrito) throws BadElementException, IOException {
        PdfPTable table = new PdfPTable(100);

        PdfPCell cell = new PdfPCell(new Phrase("Cochabamba, " + Fecha.getFecha_ddMMMMyyyy(), NORMAL_ITALICA));
        cell.setColspan(100);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setFixedHeight(60f);
        cell.setPaddingTop(8);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(" ", NORMAL));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setColspan(100);
        cell.setBackgroundColor(BaseColor.GRAY);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Cualquier raspadura o enmienda invalida el presente documento", NORMAL_ITALICA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setColspan(100);
        table.addCell(cell);

        return table;
    }

}
