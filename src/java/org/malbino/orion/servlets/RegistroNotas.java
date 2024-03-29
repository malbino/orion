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
import org.malbino.orion.entities.Inscrito;
import org.malbino.orion.entities.Nota;
import org.malbino.orion.enums.ModalidadEvaluacion;
import org.malbino.orion.enums.Regimen;
import org.malbino.orion.enums.TipoNota;
import org.malbino.orion.facades.CarreraFacade;
import org.malbino.orion.facades.GestionAcademicaFacade;
import org.malbino.orion.facades.InscritoFacade;
import org.malbino.orion.facades.NotaFacade;

/**
 *
 * @author tincho
 */
@WebServlet(name = "RegistroNotas", urlPatterns = {"/reportes/notas/registroNotas/RegistroNotas"})
public class RegistroNotas extends HttpServlet {

    private static final String CONTENIDO_PDF = "application/pdf";

    private static final Font TITULO = FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD, BaseColor.BLACK);
    private static final Font SUBTITULO = FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK);
    private static final Font NEGRITA = FontFactory.getFont(FontFactory.HELVETICA, 8, Font.BOLD, BaseColor.BLACK);
    private static final Font NORMAL = FontFactory.getFont(FontFactory.HELVETICA, 8, Font.NORMAL, BaseColor.BLACK);

    private static final int MARGEN_IZQUIERDO = -40;
    private static final int MARGEN_DERECHO = -40;
    private static final int MARGEN_SUPERIOR = 30;
    private static final int MARGEN_INFERIOR = 30;

    @EJB
    GestionAcademicaFacade gestionAcademicaFacade;
    @EJB
    CarreraFacade carreraFacade;
    @EJB
    NotaFacade notaFacade;
    @EJB
    InscritoFacade inscritoFacade;

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
        TipoNota tipoNota = (TipoNota) request.getSession().getAttribute("tipoNota");

        if (id_gestionacademica != null && id_carrera != null && tipoNota != null) {
            GestionAcademica gestionAcademica = gestionAcademicaFacade.find(id_gestionacademica);
            Carrera carrera = carreraFacade.find(id_carrera);
            try {
                response.setContentType(CONTENIDO_PDF);

                Document document = new Document(PageSize.LETTER, MARGEN_IZQUIERDO, MARGEN_DERECHO, MARGEN_SUPERIOR, MARGEN_INFERIOR);
                PdfWriter.getInstance(document, response.getOutputStream());

                document.open();

                document.add(titulo(gestionAcademica, carrera, tipoNota));

                if (gestionAcademica.getModalidadEvaluacion().equals(ModalidadEvaluacion.SEMESTRAL_2P)) {
                    document.add(contenidoSemestral2P(gestionAcademica, carrera, tipoNota));
                }

                if (gestionAcademica.getModalidadEvaluacion().equals(ModalidadEvaluacion.SEMESTRAL_3P)) {
                    document.add(contenidoSemestral(gestionAcademica, carrera, tipoNota));
                }

                if (gestionAcademica.getModalidadEvaluacion().equals(ModalidadEvaluacion.ANUAL_4P)) {
                    document.add(contenidoAnual(gestionAcademica, carrera, tipoNota));
                }

                document.close();
            } catch (IOException | DocumentException ex) {
                Logger.getLogger(RegistroNotas.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    public PdfPTable titulo(GestionAcademica gestionAcademica, Carrera carrera, TipoNota tipoNota) throws BadElementException, IOException {
        PdfPTable table = new PdfPTable(100);

        //cabecera
        String realPath = getServletContext().getRealPath("/resources/uploads/" + carrera.getCampus().getInstituto().getLogo());
        Image image = Image.getInstance(realPath);
        image.scaleToFit(70, 70);
        image.setAlignment(Image.ALIGN_CENTER);
        PdfPCell cell = new PdfPCell();
        cell.addElement(image);
        cell.setRowspan(5);
        cell.setColspan(20);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("REGISTRO NOTAS,", TITULO));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setColspan(80);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(carrera.getCampus().getInstituto().getNombreRegulador(), SUBTITULO));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setColspan(80);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(gestionAcademica.toString(), SUBTITULO));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setColspan(80);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(carrera.toString(), SUBTITULO));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setColspan(80);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(tipoNota.getNombre(), SUBTITULO));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setColspan(80);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        return table;
    }

    public PdfPTable contenidoSemestral2P(GestionAcademica gestionAcademica, Carrera carrera, TipoNota tipoNota) throws BadElementException, IOException {
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

        cell = new PdfPCell(new Phrase("Estudiante", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setColspan(30);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Materia", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setColspan(35);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Docente", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setColspan(30);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        table.addCell(cell);

        if (tipoNota.equals(TipoNota.RECUPERATORIO_SEMESTRAL_2P)) {
            int contador = 1;

            List<Inscrito> listaInscritosPruebaRecuperacion = inscritoFacade.listaInscritosPruebaRecuperacion(gestionAcademica, carrera);
            for (Inscrito inscrito : listaInscritosPruebaRecuperacion) {

                List<Nota> listaRegistroNotasRecuperatorio = notaFacade.listaRegistroNotasRecuperatorio(inscrito);
                for (Nota nota : listaRegistroNotasRecuperatorio) {
                    cell = new PdfPCell(new Phrase(String.valueOf(contador), NORMAL));
                    cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                    cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
                    cell.setColspan(5);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase(String.valueOf(nota.getEstudiante().toString()), NORMAL));
                    cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
                    cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
                    cell.setColspan(30);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase(String.valueOf(nota.getMateria().toString()), NORMAL));
                    cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
                    cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
                    cell.setColspan(35);
                    table.addCell(cell);

                    if (nota.getGrupo().getEmpleado() != null) {
                        cell = new PdfPCell(new Phrase(nota.getGrupo().getEmpleado().toString(), NORMAL));
                    } else {
                        cell = new PdfPCell(new Phrase(" ", NORMAL));
                    }
                    cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
                    cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
                    cell.setColspan(30);
                    table.addCell(cell);

                    contador++;
                }
            }
        } else {
            List<Nota> notasFaltantes = notaFacade.listaRegistroNotasSemestral2P(gestionAcademica.getId_gestionacademica(), carrera.getId_carrera(), tipoNota);
            for (int i = 0; i < notasFaltantes.size(); i++) {
                Nota nota = notasFaltantes.get(i);

                cell = new PdfPCell(new Phrase(String.valueOf(i + 1), NORMAL));
                cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
                cell.setColspan(5);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(String.valueOf(nota.getEstudiante().toString()), NORMAL));
                cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
                cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
                cell.setColspan(30);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(String.valueOf(nota.getMateria().toString()), NORMAL));
                cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
                cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
                cell.setColspan(35);
                table.addCell(cell);

                if (nota.getGrupo().getEmpleado() != null) {
                    cell = new PdfPCell(new Phrase(nota.getGrupo().getEmpleado().toString(), NORMAL));
                } else {
                    cell = new PdfPCell(new Phrase(" ", NORMAL));
                }
                cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
                cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
                cell.setColspan(30);
                table.addCell(cell);
            }
        }

        return table;
    }

    public PdfPTable contenidoSemestral(GestionAcademica gestionAcademica, Carrera carrera, TipoNota tipoNota) throws BadElementException, IOException {
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

        cell = new PdfPCell(new Phrase("Estudiante", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setColspan(30);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Materia", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setColspan(35);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Docente", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setColspan(30);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        table.addCell(cell);

        if (tipoNota.equals(TipoNota.RECUPERATORIO_SEMESTRAL_3P)) {
            int contador = 1;

            List<Inscrito> listaInscritosPruebaRecuperacion = inscritoFacade.listaInscritosPruebaRecuperacion(gestionAcademica, carrera);
            for (Inscrito inscrito : listaInscritosPruebaRecuperacion) {

                List<Nota> listaRegistroNotasRecuperatorio = notaFacade.listaRegistroNotasRecuperatorio(inscrito);
                for (Nota nota : listaRegistroNotasRecuperatorio) {
                    cell = new PdfPCell(new Phrase(String.valueOf(contador), NORMAL));
                    cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                    cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
                    cell.setColspan(5);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase(String.valueOf(nota.getEstudiante().toString()), NORMAL));
                    cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
                    cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
                    cell.setColspan(30);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase(String.valueOf(nota.getMateria().toString()), NORMAL));
                    cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
                    cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
                    cell.setColspan(35);
                    table.addCell(cell);

                    if (nota.getGrupo().getEmpleado() != null) {
                        cell = new PdfPCell(new Phrase(nota.getGrupo().getEmpleado().toString(), NORMAL));
                    } else {
                        cell = new PdfPCell(new Phrase(" ", NORMAL));
                    }
                    cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
                    cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
                    cell.setColspan(30);
                    table.addCell(cell);

                    contador++;
                }
            }
        } else {
            List<Nota> notasFaltantes = notaFacade.listaRegistroNotasSemestral(gestionAcademica.getId_gestionacademica(), carrera.getId_carrera(), tipoNota);
            for (int i = 0; i < notasFaltantes.size(); i++) {
                Nota nota = notasFaltantes.get(i);

                cell = new PdfPCell(new Phrase(String.valueOf(i + 1), NORMAL));
                cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
                cell.setColspan(5);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(String.valueOf(nota.getEstudiante().toString()), NORMAL));
                cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
                cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
                cell.setColspan(30);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(String.valueOf(nota.getMateria().toString()), NORMAL));
                cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
                cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
                cell.setColspan(35);
                table.addCell(cell);

                if (nota.getGrupo().getEmpleado() != null) {
                    cell = new PdfPCell(new Phrase(nota.getGrupo().getEmpleado().toString(), NORMAL));
                } else {
                    cell = new PdfPCell(new Phrase(" ", NORMAL));
                }
                cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
                cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
                cell.setColspan(30);
                table.addCell(cell);
            }
        }

        return table;
    }

    public PdfPTable contenidoAnual(GestionAcademica gestionAcademica, Carrera carrera, TipoNota tipoNota) throws BadElementException, IOException {
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

        cell = new PdfPCell(new Phrase("Estudiante", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setColspan(30);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Materia", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setColspan(35);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Docente", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setColspan(30);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        table.addCell(cell);

        if (tipoNota.equals(TipoNota.RECUPERATORIO_ANUAL_4P)) {
            int contador = 1;

            List<Inscrito> listaInscritosPruebaRecuperacion = inscritoFacade.listaInscritosPruebaRecuperacion(gestionAcademica, carrera);
            for (Inscrito inscrito : listaInscritosPruebaRecuperacion) {
                List<Nota> listaRegistroNotasRecuperatorio = notaFacade.listaRegistroNotasRecuperatorio(inscrito);
                for (Nota nota : listaRegistroNotasRecuperatorio) {
                    cell = new PdfPCell(new Phrase(String.valueOf(contador), NORMAL));
                    cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                    cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
                    cell.setColspan(5);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase(String.valueOf(nota.getEstudiante().toString()), NORMAL));
                    cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
                    cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
                    cell.setColspan(30);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase(String.valueOf(nota.getMateria().toString()), NORMAL));
                    cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
                    cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
                    cell.setColspan(35);
                    table.addCell(cell);

                    if (nota.getGrupo().getEmpleado() != null) {
                        cell = new PdfPCell(new Phrase(nota.getGrupo().getEmpleado().toString(), NORMAL));
                    } else {
                        cell = new PdfPCell(new Phrase(" ", NORMAL));
                    }
                    cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
                    cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
                    cell.setColspan(30);
                    table.addCell(cell);

                    contador++;
                }
            }
        } else {
            List<Nota> notasFaltantes = notaFacade.listaRegistroNotasAnual(gestionAcademica.getId_gestionacademica(), carrera.getId_carrera(), tipoNota);
            for (int i = 0; i < notasFaltantes.size(); i++) {
                Nota nota = notasFaltantes.get(i);

                cell = new PdfPCell(new Phrase(String.valueOf(i + 1), NORMAL));
                cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
                cell.setColspan(5);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(String.valueOf(nota.getEstudiante().toString()), NORMAL));
                cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
                cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
                cell.setColspan(30);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(String.valueOf(nota.getMateria().toString()), NORMAL));
                cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
                cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
                cell.setColspan(35);
                table.addCell(cell);

                if (nota.getGrupo().getEmpleado() != null) {
                    cell = new PdfPCell(new Phrase(nota.getGrupo().getEmpleado().toString(), NORMAL));
                } else {
                    cell = new PdfPCell(new Phrase(" ", NORMAL));
                }
                cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
                cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
                cell.setColspan(30);
                table.addCell(cell);
            }
        }

        return table;
    }
}
