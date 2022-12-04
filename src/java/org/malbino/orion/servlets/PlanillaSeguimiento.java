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
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
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
import org.malbino.orion.entities.Grupo;
import org.malbino.orion.entities.Nota;
import org.malbino.orion.enums.Condicion;
import org.malbino.orion.enums.Regimen;
import org.malbino.orion.facades.GrupoFacade;
import org.malbino.orion.facades.NotaFacade;
import org.malbino.orion.util.NumberToLetterConverter;

/**
 *
 * @author tincho
 */
@WebServlet(name = "PlanillaSeguimiento", urlPatterns = {"/registroDocente/PlanillaSeguimiento", "/reportes/PlanillaSeguimiento"})
public class PlanillaSeguimiento extends HttpServlet {

    private static final String CONTENIDO_PDF = "application/pdf";

    private static final Font TITULO = FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD, BaseColor.BLACK);
    private static final Font SUBTITULO = FontFactory.getFont(FontFactory.HELVETICA, 6.5f, Font.NORMAL, BaseColor.WHITE);
    private static final Font NEGRITA = FontFactory.getFont(FontFactory.HELVETICA, 6.5f, Font.BOLD, BaseColor.BLACK);
    private static final Font NORMAL = FontFactory.getFont(FontFactory.HELVETICA, 6.5f, Font.NORMAL, BaseColor.BLACK);
    private static final Font BOLDITALIC = FontFactory.getFont(FontFactory.HELVETICA, 6.5f, Font.BOLDITALIC, BaseColor.BLACK);
    private static final Font ITALIC = FontFactory.getFont(FontFactory.HELVETICA, 6.5f, Font.ITALIC, BaseColor.BLACK);

    private static final int MARGEN_IZQUIERDO = -30;
    private static final int MARGEN_DERECHO = -30;
    private static final int MARGEN_SUPERIOR = 30;
    private static final int MARGEN_INFERIOR = 30;

    @EJB
    GrupoFacade grupoFacade;
    @EJB
    NotaFacade notaFacade;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.generarPDF(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        this.generarPDF(request, response);
    }

    public void generarPDF(HttpServletRequest request, HttpServletResponse response) {
        Integer id_grupo = (Integer) request.getSession().getAttribute("id_grupo");

        if (id_grupo != null) {
            Grupo grupo = grupoFacade.find(id_grupo);
            try {
                response.setContentType(CONTENIDO_PDF);

                Document document = new Document(PageSize.LETTER, MARGEN_IZQUIERDO, MARGEN_DERECHO, MARGEN_SUPERIOR, MARGEN_INFERIOR);
                PdfWriter.getInstance(document, response.getOutputStream());

                document.open();

                document.add(cabecera(grupo));
                if (grupo.getGestionAcademica().getRegimen().equals(Regimen.SEMESTRAL)) {
                    document.add(contenidoSemestral(grupo));
                    document.add(pieSemestral(grupo));
                } else if (grupo.getGestionAcademica().getRegimen().equals(Regimen.ANUAL)) {
                    document.add(contenidoAnual(grupo));
                    document.add(pieAnual(grupo));
                }

                document.close();
            } catch (IOException | DocumentException ex) {
                Logger.getLogger(PlanillaSeguimiento.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    public PdfPTable cabecera(Grupo grupo) throws BadElementException, IOException {
        PdfPTable table = new PdfPTable(100);

        PdfPCell cell = new PdfPCell(new Phrase(grupo.getMateria().getCarrera().getCampus().getInstituto().getNombreRegulador(), TITULO));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(100);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("PLANILLA DE SEGUIMIENTO", TITULO));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(100);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(" ", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(100);
        table.addCell(cell);

        // fila 0
        cell = new PdfPCell(new Phrase("CARRERA:\n" + grupo.getMateria().getCarrera().getNombre(), NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(35);
        cell.setRowspan(3);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("ASIGNATURA:", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(12);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(grupo.getMateria().getNombre(), NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(28);
        cell.setBackgroundColor(new BaseColor(197, 217, 241));
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("CODIGO:", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(10);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(grupo.getMateria().getCodigo(), NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(15);
        cell.setBackgroundColor(new BaseColor(197, 217, 241));
        table.addCell(cell);

        // fila 1
        cell = new PdfPCell(new Phrase("NIVEL:", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(12);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(grupo.getMateria().getNivel().getNombre(), NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(28);
        cell.setBackgroundColor(new BaseColor(197, 217, 241));
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("PERIODO:", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(10);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(grupo.getGestionAcademica().toString(), NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(15);
        cell.setBackgroundColor(new BaseColor(197, 217, 241));
        table.addCell(cell);

        // fila 2
        cell = new PdfPCell(new Phrase("DOCENTE:", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(12);
        table.addCell(cell);

        if (grupo.getEmpleado() != null) {
            cell = new PdfPCell(new Phrase(grupo.getEmpleado().toString(), NEGRITA));
        } else {
            cell = new PdfPCell(new Phrase(" ", NEGRITA));
        }
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(28);
        cell.setBackgroundColor(new BaseColor(197, 217, 241));
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("PARALELO:", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(10);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(grupo.getCodigo(), NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(15);
        cell.setBackgroundColor(new BaseColor(197, 217, 241));
        table.addCell(cell);

        return table;
    }

    public PdfPTable contenidoSemestral(Grupo grupo) throws BadElementException, IOException {
        PdfPTable table = new PdfPTable(100);

        // fila 0
        PdfPCell cell = new PdfPCell(new Phrase(" ", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(100);

        table.addCell(cell);

        // fila 1
        cell = new PdfPCell(new Phrase("Nro", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(5);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("C.I.", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(10);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("APELLIDOS Y NOMBRES", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(20);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Puntaje\n1er. Parcial", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(5);
        cell.setRotation(90);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Puntaje\n2do. Parcial", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(5);
        cell.setRotation(90);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Puntaje\n3er. Parcial", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(5);
        cell.setRotation(90);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("PROMEDIO\nANUAL", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(10);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("LITERAL", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(15);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Prueba\nRecuperación", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(10);
        cell.setRotation(90);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("OBSERVACIONES", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(15);
        table.addCell(cell);

        List<Nota> notas = notaFacade.listaNotasGrupo(grupo.getId_grupo());
        for (int i = 0; i < notas.size(); i++) {
            Nota nota = notas.get(i);

            cell = new PdfPCell(new Phrase(String.valueOf(i + 1), NORMAL));
            cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
            cell.setColspan(5);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(nota.getEstudiante().dniLugar(), NORMAL));
            cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
            cell.setColspan(10);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(nota.getEstudiante().toString(), NORMAL));
            cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
            cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
            cell.setColspan(20);
            table.addCell(cell);

            if (nota.getNota1() != null) {
                cell = new PdfPCell(new Phrase(nota.getNota1().toString(), NORMAL));
            } else {
                cell = new PdfPCell(new Phrase(" ", NORMAL));
            }
            cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
            cell.setColspan(5);
            table.addCell(cell);

            if (nota.getNota2() != null) {
                cell = new PdfPCell(new Phrase(nota.getNota2().toString(), NORMAL));
            } else {
                cell = new PdfPCell(new Phrase(" ", NORMAL));
            }
            cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
            cell.setColspan(5);
            table.addCell(cell);

            if (nota.getNota3() != null) {
                cell = new PdfPCell(new Phrase(nota.getNota3().toString(), NORMAL));
            } else {
                cell = new PdfPCell(new Phrase(" ", NORMAL));
            }
            cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
            cell.setColspan(5);
            table.addCell(cell);

            if (nota.getNotaFinal() != null) {
                cell = new PdfPCell(new Phrase(nota.getNotaFinal().toString(), NORMAL));
            } else {
                cell = new PdfPCell(new Phrase(" ", NORMAL));
            }
            cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
            cell.setColspan(10);
            cell.setBackgroundColor(new BaseColor(197, 217, 241));
            table.addCell(cell);

            if (nota.getNotaFinal() != null) {
                cell = new PdfPCell(new Phrase(NumberToLetterConverter.convertNumberToLetter(nota.getNotaFinal()), NORMAL));
            } else {
                cell = new PdfPCell(new Phrase(" ", NORMAL));
            }
            cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
            cell.setColspan(15);
            table.addCell(cell);

            if (nota.getRecuperatorio() != null) {
                cell = new PdfPCell(new Phrase(nota.getRecuperatorio().toString(), NORMAL));
            } else {
                cell = new PdfPCell(new Phrase(" ", NORMAL));
            }
            cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
            cell.setColspan(10);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(nota.getCondicion().getNombre(), NORMAL));
            cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
            cell.setColspan(15);
            table.addCell(cell);
        }

        return table;
    }

    public PdfPTable pieSemestral(Grupo grupo) throws BadElementException, IOException {
        PdfPTable table = new PdfPTable(100);

        // fila 0
        PdfPCell cell = new PdfPCell(new Phrase(" ", NORMAL));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(100);
        cell.setBorder(PdfPCell.NO_BORDER);
        table.addCell(cell);

        // fila 1
        cell = new PdfPCell(new Phrase("Nota Mínima: 61", NORMAL));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(15);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("TOTAL INSCRITOS", NORMAL));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(30);
        table.addCell(cell);

        Long inscritos = notaFacade.cantidadInscritos(grupo.getId_grupo());
        cell = new PdfPCell(new Phrase(inscritos.toString(), NORMAL));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(5);
        cell.setBackgroundColor(new BaseColor(197, 217, 241));
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(" ", NORMAL));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(10);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("OBSERVACIONES", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(25);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(" ", NORMAL));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(15);
        cell.setBorder(PdfPCell.NO_BORDER);
        table.addCell(cell);

        // fila 2
        cell = new PdfPCell(new Phrase("Nota Máxima: 100", NORMAL));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(15);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("TOTAL APROBADOS", NORMAL));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(30);
        table.addCell(cell);

        Long aprobados = notaFacade.cantidadCondicion(grupo.getId_grupo(), Condicion.APROBADO);
        cell = new PdfPCell(new Phrase(aprobados.toString(), NORMAL));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(5);
        cell.setBackgroundColor(new BaseColor(197, 217, 241));
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(" ", NORMAL));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(10);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(" ", NORMAL));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(25);
        cell.setRowspan(4);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(" ", NORMAL));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(15);
        cell.setBorder(PdfPCell.NO_BORDER);
        table.addCell(cell);

        // fila 3
        cell = new PdfPCell(new Phrase(" ", NORMAL));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(15);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("TOTAL REPROBADOS", NORMAL));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(30);
        table.addCell(cell);

        Long reprobados = notaFacade.cantidadCondicion(grupo.getId_grupo(), Condicion.REPROBADO);
        cell = new PdfPCell(new Phrase(reprobados.toString(), NORMAL));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(5);
        cell.setBackgroundColor(new BaseColor(197, 217, 241));
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(" ", NORMAL));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(10);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(" ", NORMAL));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(15);
        cell.setBorder(PdfPCell.NO_BORDER);
        table.addCell(cell);

        // fila 4
        cell = new PdfPCell(new Phrase(" ", NORMAL));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(15);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("TOTAL ABANDONOS", NORMAL));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(30);
        table.addCell(cell);

        Long abandonos = notaFacade.cantidadCondicion(grupo.getId_grupo(), Condicion.ABANDONO);
        cell = new PdfPCell(new Phrase(abandonos.toString(), NORMAL));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(5);
        cell.setBackgroundColor(new BaseColor(197, 217, 241));
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(" ", NORMAL));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(10);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(" ", NORMAL));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(15);
        cell.setBorder(PdfPCell.NO_BORDER);
        table.addCell(cell);

        // fila 5
        cell = new PdfPCell(new Phrase(" ", NORMAL));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(15);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("TOTAL EFECTIVOS", NORMAL));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(30);
        table.addCell(cell);

        Long efectivos = inscritos - abandonos;
        cell = new PdfPCell(new Phrase(efectivos.toString(), NORMAL));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(5);
        cell.setBackgroundColor(new BaseColor(197, 217, 241));
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(" ", NORMAL));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(10);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(" ", NORMAL));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(15);
        cell.setBorder(PdfPCell.NO_BORDER);
        table.addCell(cell);

        // fila 0
        cell = new PdfPCell(new Phrase(" ", NORMAL));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(100);
        cell.setBorder(PdfPCell.NO_BORDER);
        table.addCell(cell);

        // fila 1
        cell = new PdfPCell(new Phrase(" ", NORMAL));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(15);
        cell.setBorder(PdfPCell.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("PARCIALES", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(20);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("FECHA", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(25);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Vo. Bo. DIR. ACADEMICA", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(25);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(" ", NORMAL));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(15);
        cell.setBorder(PdfPCell.NO_BORDER);
        table.addCell(cell);

        // fila 2
        cell = new PdfPCell(new Phrase(" ", NORMAL));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(15);
        cell.setBorder(PdfPCell.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("1er. Parcial", NORMAL));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(20);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(" ", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(25);
        cell.setBackgroundColor(new BaseColor(197, 217, 241));
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(" ", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(25);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(" ", NORMAL));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(15);
        cell.setBorder(PdfPCell.NO_BORDER);
        table.addCell(cell);

        // fila 3
        cell = new PdfPCell(new Phrase(" ", NORMAL));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(15);
        cell.setBorder(PdfPCell.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("2do. Parcial", NORMAL));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(20);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(" ", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(25);
        cell.setBackgroundColor(new BaseColor(197, 217, 241));
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(" ", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(25);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(" ", NORMAL));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(15);
        cell.setBorder(PdfPCell.NO_BORDER);
        table.addCell(cell);

        // fila 4
        cell = new PdfPCell(new Phrase(" ", NORMAL));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(15);
        cell.setBorder(PdfPCell.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("3er. Parcial", NORMAL));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(20);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(" ", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(25);
        cell.setBackgroundColor(new BaseColor(197, 217, 241));
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(" ", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(25);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(" ", NORMAL));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(15);
        cell.setBorder(PdfPCell.NO_BORDER);
        table.addCell(cell);

        // fila 0
        cell = new PdfPCell(new Phrase(" ", NORMAL));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(100);
        cell.setBorder(PdfPCell.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(" ", NORMAL));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(100);
        cell.setBorder(PdfPCell.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(" ", NORMAL));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(100);
        cell.setBorder(PdfPCell.NO_BORDER);
        table.addCell(cell);

        // fila 1
        cell = new PdfPCell(new Phrase(" ", NORMAL));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(5);
        cell.setBorder(PdfPCell.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(" ", NORMAL));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(20);
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setBackgroundColor(new BaseColor(197, 217, 241));
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(" ", NORMAL));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(10);
        cell.setBorder(PdfPCell.NO_BORDER);
        table.addCell(cell);

        if (grupo.getEmpleado() != null) {
            cell = new PdfPCell(new Phrase(grupo.getEmpleado().toString(), NORMAL));
        } else {
            cell = new PdfPCell(new Phrase(" ", NORMAL));
        }
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(30);
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setBackgroundColor(new BaseColor(197, 217, 241));
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(" ", NORMAL));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(10);
        cell.setBorder(PdfPCell.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(" ", NORMAL));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(20);
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setBackgroundColor(new BaseColor(197, 217, 241));
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(" ", NORMAL));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(5);
        cell.setBorder(PdfPCell.NO_BORDER);
        table.addCell(cell);

        // fila 2
        cell = new PdfPCell(new Phrase(" ", NORMAL));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(5);
        cell.setBorder(PdfPCell.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("FECHA  DE ENTREGA", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(20);
        cell.setBorder(PdfPCell.TOP);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(" ", NORMAL));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(10);
        cell.setBorder(PdfPCell.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("NOMBRE DEL DOCENTE", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(30);
        cell.setBorder(PdfPCell.TOP);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(" ", NORMAL));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(10);
        cell.setBorder(PdfPCell.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("FIRMA DEL DOCENTE", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(20);
        cell.setBorder(PdfPCell.TOP);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(" ", NORMAL));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(5);
        cell.setBorder(PdfPCell.NO_BORDER);
        table.addCell(cell);

        return table;
    }

    public PdfPTable contenidoAnual(Grupo grupo) throws BadElementException, IOException {
        PdfPTable table = new PdfPTable(100);

        // fila 0
        PdfPCell cell = new PdfPCell(new Phrase(" ", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(100);
        table.addCell(cell);

        // fila 1
        cell = new PdfPCell(new Phrase("Nro", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(5);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("C.I.", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(10);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("APELLIDOS Y NOMBRES", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(20);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Puntaje\n1er. Parcial", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(5);
        cell.setRotation(90);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Puntaje\n2do. Parcial", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(5);
        cell.setRotation(90);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Puntaje\n3er. Parcial", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(5);
        cell.setRotation(90);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Puntaje\n4to. Parcial", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(5);
        cell.setRotation(90);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("PROMEDIO\nANUAL", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(10);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("LITERAL", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(15);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Prueba\nRecuperación", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(5);
        cell.setRotation(90);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("OBSERVACIONES", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(15);
        table.addCell(cell);

        List<Nota> notas = notaFacade.listaNotasGrupo(grupo.getId_grupo());
        for (int i = 0; i < notas.size(); i++) {
            Nota nota = notas.get(i);

            cell = new PdfPCell(new Phrase(String.valueOf(i + 1), NORMAL));
            cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
            cell.setColspan(5);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(nota.getEstudiante().dniLugar(), NORMAL));
            cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
            cell.setColspan(10);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(nota.getEstudiante().toString(), NORMAL));
            cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
            cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
            cell.setColspan(20);
            table.addCell(cell);

            if (nota.getNota1() != null) {
                cell = new PdfPCell(new Phrase(nota.getNota1().toString(), NORMAL));
            } else {
                cell = new PdfPCell(new Phrase(" ", NORMAL));
            }
            cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
            cell.setColspan(5);
            table.addCell(cell);

            if (nota.getNota2() != null) {
                cell = new PdfPCell(new Phrase(nota.getNota2().toString(), NORMAL));
            } else {
                cell = new PdfPCell(new Phrase(" ", NORMAL));
            }
            cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
            cell.setColspan(5);
            table.addCell(cell);

            if (nota.getNota3() != null) {
                cell = new PdfPCell(new Phrase(nota.getNota3().toString(), NORMAL));
            } else {
                cell = new PdfPCell(new Phrase(" ", NORMAL));
            }
            cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
            cell.setColspan(5);
            table.addCell(cell);

            if (nota.getNota4() != null) {
                cell = new PdfPCell(new Phrase(nota.getNota4().toString(), NORMAL));
            } else {
                cell = new PdfPCell(new Phrase(" ", NORMAL));
            }
            cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
            cell.setColspan(5);
            table.addCell(cell);

            if (nota.getNotaFinal() != null) {
                cell = new PdfPCell(new Phrase(nota.getNotaFinal().toString(), NORMAL));
            } else {
                cell = new PdfPCell(new Phrase(" ", NORMAL));
            }
            cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
            cell.setColspan(10);
            cell.setBackgroundColor(new BaseColor(197, 217, 241));
            table.addCell(cell);

            if (nota.getNotaFinal() != null) {
                cell = new PdfPCell(new Phrase(NumberToLetterConverter.convertNumberToLetter(nota.getNotaFinal()), NORMAL));
            } else {
                cell = new PdfPCell(new Phrase(" ", NORMAL));
            }
            cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
            cell.setColspan(15);
            table.addCell(cell);

            if (nota.getRecuperatorio() != null) {
                cell = new PdfPCell(new Phrase(nota.getRecuperatorio().toString(), NORMAL));
            } else {
                cell = new PdfPCell(new Phrase(" ", NORMAL));
            }
            cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
            cell.setColspan(5);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(nota.getCondicion().getNombre(), NORMAL));
            cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
            cell.setColspan(15);
            table.addCell(cell);
        }

        return table;
    }

    public PdfPTable pieAnual(Grupo grupo) throws BadElementException, IOException {
        PdfPTable table = new PdfPTable(100);

        // fila 0
        PdfPCell cell = new PdfPCell(new Phrase(" ", NORMAL));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(100);
        cell.setBorder(PdfPCell.NO_BORDER);
        table.addCell(cell);

        // fila 1
        cell = new PdfPCell(new Phrase("Nota Mínima: 61", NORMAL));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(15);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("TOTAL INSCRITOS", NORMAL));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(30);
        table.addCell(cell);

        Long inscritos = notaFacade.cantidadInscritos(grupo.getId_grupo());
        cell = new PdfPCell(new Phrase(inscritos.toString(), NORMAL));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(5);
        cell.setBackgroundColor(new BaseColor(197, 217, 241));
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(" ", NORMAL));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(10);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("OBSERVACIONES", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(25);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(" ", NORMAL));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(15);
        cell.setBorder(PdfPCell.NO_BORDER);
        table.addCell(cell);

        // fila 2
        cell = new PdfPCell(new Phrase("Nota Máxima: 100", NORMAL));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(15);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("TOTAL APROBADOS", NORMAL));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(30);
        table.addCell(cell);

        Long aprobados = notaFacade.cantidadCondicion(grupo.getId_grupo(), Condicion.APROBADO);
        cell = new PdfPCell(new Phrase(aprobados.toString(), NORMAL));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(5);
        cell.setBackgroundColor(new BaseColor(197, 217, 241));
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(" ", NORMAL));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(10);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(" ", NORMAL));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(25);
        cell.setRowspan(4);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(" ", NORMAL));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(15);
        cell.setBorder(PdfPCell.NO_BORDER);
        table.addCell(cell);

        // fila 3
        cell = new PdfPCell(new Phrase(" ", NORMAL));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(15);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("TOTAL REPROBADOS", NORMAL));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(30);
        table.addCell(cell);

        Long reprobados = notaFacade.cantidadCondicion(grupo.getId_grupo(), Condicion.REPROBADO);
        cell = new PdfPCell(new Phrase(reprobados.toString(), NORMAL));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(5);
        cell.setBackgroundColor(new BaseColor(197, 217, 241));
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(" ", NORMAL));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(10);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(" ", NORMAL));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(15);
        cell.setBorder(PdfPCell.NO_BORDER);
        table.addCell(cell);

        // fila 4
        cell = new PdfPCell(new Phrase(" ", NORMAL));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(15);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("TOTAL ABANDONOS", NORMAL));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(30);
        table.addCell(cell);

        Long abandonos = notaFacade.cantidadCondicion(grupo.getId_grupo(), Condicion.ABANDONO);
        cell = new PdfPCell(new Phrase(abandonos.toString(), NORMAL));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(5);
        cell.setBackgroundColor(new BaseColor(197, 217, 241));
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(" ", NORMAL));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(10);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(" ", NORMAL));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(15);
        cell.setBorder(PdfPCell.NO_BORDER);
        table.addCell(cell);

        // fila 5
        cell = new PdfPCell(new Phrase(" ", NORMAL));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(15);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("TOTAL EFECTIVOS", NORMAL));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(30);
        table.addCell(cell);

        Long efectivos = inscritos - abandonos;
        cell = new PdfPCell(new Phrase(efectivos.toString(), NORMAL));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(5);
        cell.setBackgroundColor(new BaseColor(197, 217, 241));
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(" ", NORMAL));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(10);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(" ", NORMAL));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(15);
        cell.setBorder(PdfPCell.NO_BORDER);
        table.addCell(cell);

        // fila 0
        cell = new PdfPCell(new Phrase(" ", NORMAL));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(100);
        cell.setBorder(PdfPCell.NO_BORDER);
        table.addCell(cell);

        // fila 1
        cell = new PdfPCell(new Phrase(" ", NORMAL));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(15);
        cell.setBorder(PdfPCell.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("PARCIALES", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(20);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("FECHA", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(25);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Vo. Bo. DIR. ACADEMICA", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(25);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(" ", NORMAL));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(15);
        cell.setBorder(PdfPCell.NO_BORDER);
        table.addCell(cell);

        // fila 2
        cell = new PdfPCell(new Phrase(" ", NORMAL));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(15);
        cell.setBorder(PdfPCell.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("1er. Parcial", NORMAL));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(20);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(" ", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(25);
        cell.setBackgroundColor(new BaseColor(197, 217, 241));
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(" ", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(25);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(" ", NORMAL));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(15);
        cell.setBorder(PdfPCell.NO_BORDER);
        table.addCell(cell);

        // fila 3
        cell = new PdfPCell(new Phrase(" ", NORMAL));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(15);
        cell.setBorder(PdfPCell.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("2do. Parcial", NORMAL));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(20);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(" ", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(25);
        cell.setBackgroundColor(new BaseColor(197, 217, 241));
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(" ", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(25);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(" ", NORMAL));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(15);
        cell.setBorder(PdfPCell.NO_BORDER);
        table.addCell(cell);

        // fila 5
        cell = new PdfPCell(new Phrase(" ", NORMAL));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(15);
        cell.setBorder(PdfPCell.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("3er. Parcial", NORMAL));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(20);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(" ", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(25);
        cell.setBackgroundColor(new BaseColor(197, 217, 241));
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(" ", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(25);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(" ", NORMAL));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(15);
        cell.setBorder(PdfPCell.NO_BORDER);
        table.addCell(cell);

        // fila 5
        cell = new PdfPCell(new Phrase(" ", NORMAL));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(15);
        cell.setBorder(PdfPCell.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("4to. Parcial", NORMAL));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(20);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(" ", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(25);
        cell.setBackgroundColor(new BaseColor(197, 217, 241));
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(" ", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(25);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(" ", NORMAL));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(15);
        cell.setBorder(PdfPCell.NO_BORDER);
        table.addCell(cell);

        // fila 0
        cell = new PdfPCell(new Phrase(" ", NORMAL));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(100);
        cell.setBorder(PdfPCell.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(" ", NORMAL));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(100);
        cell.setBorder(PdfPCell.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(" ", NORMAL));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(100);
        cell.setBorder(PdfPCell.NO_BORDER);
        table.addCell(cell);

        // fila 1
        cell = new PdfPCell(new Phrase(" ", NORMAL));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(5);
        cell.setBorder(PdfPCell.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(" ", NORMAL));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(20);
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setBackgroundColor(new BaseColor(197, 217, 241));
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(" ", NORMAL));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(10);
        cell.setBorder(PdfPCell.NO_BORDER);
        table.addCell(cell);

        if (grupo.getEmpleado() != null) {
            cell = new PdfPCell(new Phrase(grupo.getEmpleado().toString(), NORMAL));
        } else {
            cell = new PdfPCell(new Phrase(" ", NORMAL));
        }
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(30);
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setBackgroundColor(new BaseColor(197, 217, 241));
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(" ", NORMAL));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(10);
        cell.setBorder(PdfPCell.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(" ", NORMAL));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(20);
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setBackgroundColor(new BaseColor(197, 217, 241));
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(" ", NORMAL));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(5);
        cell.setBorder(PdfPCell.NO_BORDER);
        table.addCell(cell);

        // fila 2
        cell = new PdfPCell(new Phrase(" ", NORMAL));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(5);
        cell.setBorder(PdfPCell.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("FECHA  DE ENTREGA", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(20);
        cell.setBorder(PdfPCell.TOP);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(" ", NORMAL));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(10);
        cell.setBorder(PdfPCell.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("NOMBRE DEL DOCENTE", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(30);
        cell.setBorder(PdfPCell.TOP);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(" ", NORMAL));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(10);
        cell.setBorder(PdfPCell.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("FIRMA DEL DOCENTE", NEGRITA));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(20);
        cell.setBorder(PdfPCell.TOP);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(" ", NORMAL));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setColspan(5);
        cell.setBorder(PdfPCell.NO_BORDER);
        table.addCell(cell);

        return table;
    }
}
