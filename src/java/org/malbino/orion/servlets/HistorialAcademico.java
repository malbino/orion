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
import com.itextpdf.text.Element;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.IOException;
import java.util.Date;
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
import org.malbino.orion.entities.Estudiante;
import org.malbino.orion.entities.GestionAcademica;
import org.malbino.orion.entities.Inscrito;
import org.malbino.orion.entities.Nota;
import org.malbino.orion.facades.CarreraFacade;
import org.malbino.orion.facades.EstudianteFacade;
import org.malbino.orion.facades.InscritoFacade;
import org.malbino.orion.facades.MateriaFacade;
import org.malbino.orion.facades.NotaFacade;
import org.malbino.orion.util.Fecha;
import org.malbino.orion.util.Redondeo;

/**
 *
 * @author tincho
 */
@WebServlet(name = "HistorialAcademico", urlPatterns = {"/reportes/HistorialAcademico"})
public class HistorialAcademico extends HttpServlet {

    private static final String CONTENIDO_PDF = "application/pdf";

    private static final Font TITULO = FontFactory.getFont(FontFactory.HELVETICA, 14, Font.BOLD, BaseColor.BLACK);
    private static final Font NEGRITA = FontFactory.getFont(FontFactory.HELVETICA, 7, Font.BOLD, BaseColor.BLACK);
    private static final Font NORMAL = FontFactory.getFont(FontFactory.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK);
    private static final Font ESPACIO = FontFactory.getFont(FontFactory.HELVETICA, 2, Font.NORMAL, BaseColor.BLACK);
    private static final Font NORMAL_ITALICA = FontFactory.getFont(FontFactory.HELVETICA, 7, Font.ITALIC, BaseColor.BLACK);
    private static final Font NEGRITA_ITALICA = FontFactory.getFont(FontFactory.HELVETICA, 7, Font.BOLDITALIC, BaseColor.BLACK);
    private static final Font NEGRITA_PLOMO = FontFactory.getFont(FontFactory.HELVETICA, 7, Font.BOLD, BaseColor.LIGHT_GRAY);

    private static final int MARGEN_IZQUIERDO = -40;
    private static final int MARGEN_DERECHO = -40;
    private static final int MARGEN_SUPERIOR = 95;
    private static final int MARGEN_INFERIOR = 20;

    @EJB
    EstudianteFacade estudianteFacade;
    @EJB
    CarreraFacade carreraFacade;
    @EJB
    NotaFacade notaFacade;
    @EJB
    MateriaFacade materiaFacade;
    @EJB
    InscritoFacade inscritoFacade;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.generarPDF(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.generarPDF(request, response);
    }

    public void generarPDF(HttpServletRequest request, HttpServletResponse response) {
        Integer id_persona = (Integer) request.getSession().getAttribute("id_persona");
        Integer id_carrera = (Integer) request.getSession().getAttribute("id_carrera");
        Date fecha = (Date) request.getSession().getAttribute("fecha");

        if (id_persona != null && id_carrera != null && fecha != null) {
            Estudiante estudiante = estudianteFacade.find(id_persona);
            Carrera carrera = carreraFacade.find(id_carrera);

            try {
                response.setContentType(CONTENIDO_PDF);

                Document document = new Document(PageSize.A4, MARGEN_IZQUIERDO, MARGEN_DERECHO, MARGEN_SUPERIOR, MARGEN_INFERIOR);
                PdfWriter writer = PdfWriter.getInstance(document, response.getOutputStream());
                writer.setPageEvent(new HeaderFooter(estudiante, carrera));

                document.open();

                document.add(cuerpo(estudiante, carrera));
                document.add(firmas(estudiante, carrera, fecha));
                document.add(resumen(estudiante, carrera));

                document.close();
            } catch (IOException | DocumentException ex) {

            }
        }
    }

    public PdfPTable cuerpo(Estudiante estudiante, Carrera carrera) {
        PdfPTable table = new PdfPTable(66);

        List<Nota> historialAcademico = notaFacade.reporteHistorialAcademico(estudiante.getId_persona(), carrera.getId_carrera());
        for (int i = 0; i < historialAcademico.size(); i++) {
            Nota nota = historialAcademico.get(i);

            PdfPCell cell = new PdfPCell(new Phrase(String.valueOf(i + 1), NORMAL));
            cell.setColspan(2);
            cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(nota.getGestionAcademica().toString(), NORMAL));
            cell.setColspan(4);
            cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(nota.getMateria().getNivel().getOrdinal(), NORMAL));
            cell.setColspan(6);
            cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(nota.getMateria().getCodigo(), NORMAL));
            cell.setColspan(5);
            cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(nota.getMateria().getNombre(), NORMAL));
            cell.setColspan(28);
            cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(nota.getMateria().prerequisitosToString(), NORMAL));
            cell.setColspan(6);
            cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            table.addCell(cell);

            PdfPCell cellNotaFinal;
            if (nota.getNotaFinal() != null) {
                cellNotaFinal = new PdfPCell(new Phrase(String.valueOf(nota.getNotaFinal()), NORMAL));
            } else {
                cellNotaFinal = new PdfPCell(new Phrase(" ", NORMAL));
            }
            cellNotaFinal.setColspan(4);
            cellNotaFinal.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);

            PdfPCell cellNotaIns;
            if (nota.getRecuperatorio() != null) {
                cellNotaIns = new PdfPCell(new Phrase(String.valueOf(nota.getRecuperatorio()), NORMAL));
            } else {
                cellNotaIns = new PdfPCell(new Phrase(" ", NORMAL));
            }
            cellNotaIns.setColspan(5);
            cellNotaIns.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);

            table.addCell(cellNotaFinal);
            table.addCell(cellNotaIns);

            cell = new PdfPCell(new Phrase(nota.getCondicion().toString(), NORMAL));
            cell.setColspan(6);
            cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            table.addCell(cell);
        }

        PdfPCell cell = new PdfPCell(new Phrase(" ", ESPACIO));
        cell.setColspan(66);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        return table;
    }

    public PdfPTable firmas(Estudiante estudiante, Carrera carrera, Date fecha) {
        PdfPTable table = new PdfPTable(60);

        Phrase phrase = new Phrase();
        phrase.add(new Chunk("Lugar y fecha: ", NEGRITA_ITALICA));
        phrase.add(new Chunk("Cochabamba, " + Fecha.formatearFecha_ddMMMMyyyy(fecha), NORMAL_ITALICA));
        PdfPCell cell = new PdfPCell(phrase);
        cell.setColspan(60);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
        
        cell = new PdfPCell(new Phrase(" ", ESPACIO));
        cell.setColspan(60);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        phrase = new Phrase();
        phrase.add(new Chunk("* PLAN DE ESTUDIOS SEGÚN R.M. ", NEGRITA));
        phrase.add(new Chunk(carrera.getResolucionMinisterial1(), NORMAL));
        cell = new PdfPCell(phrase);
        cell.setColspan(60);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        if (carrera.getResolucionMinisterial2() != null && !carrera.getResolucionMinisterial2().isEmpty()) {
            phrase = new Phrase();
            phrase.add(new Chunk("* PLAN DE ESTUDIOS SEGÚN R.M. ", NEGRITA));
            phrase.add(new Chunk(carrera.getResolucionMinisterial2(), NORMAL));
            cell = new PdfPCell(phrase);
            cell.setColspan(60);
            cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
        }

        if (carrera.getResolucionMinisterial3() != null && !carrera.getResolucionMinisterial3().isEmpty()) {
            phrase = new Phrase();
            phrase.add(new Chunk("* PLAN DE ESTUDIOS SEGÚN R.M. ", NEGRITA));
            phrase.add(new Chunk(carrera.getResolucionMinisterial3(), NORMAL));
            cell = new PdfPCell(phrase);
            cell.setColspan(60);
            cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
        }

        cell = new PdfPCell(new Phrase(" ", NEGRITA));
        cell.setColspan(20);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Firma de autoridad académica", NEGRITA));
        cell.setColspan(20);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setVerticalAlignment(PdfPCell.ALIGN_BOTTOM);
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setFixedHeight(30);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(" ", NEGRITA));
        cell.setColspan(20);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        return table;
    }

    public PdfPTable resumen(Estudiante estudiante, Carrera carrera) {
        PdfPTable table = new PdfPTable(60);

        PdfPCell cell = new PdfPCell(new Phrase(" ", ESPACIO));
        cell.setColspan(60);
        cell.setBorder(PdfPCell.NO_BORDER);
        table.addCell(cell);

        //fila 1
        cell = new PdfPCell(new Phrase("ESCALA DE VALORACIÓN", NEGRITA));
        cell.setColspan(20);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Sello del Instituto", NEGRITA_PLOMO));
        cell.setColspan(20);
        cell.setRowspan(4);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setBorder(PdfPCell.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Carga Horaria:", NEGRITA));
        cell.setColspan(15);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("3.600", NORMAL));
        cell.setColspan(5);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        table.addCell(cell);

        //fila 2
        cell = new PdfPCell(new Phrase(carrera.getRegimen().getNotaMinimaAprobacion() + " a 100", NORMAL));
        cell.setColspan(12);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("APROBADO", NORMAL));
        cell.setColspan(8);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Asignaturas aprobadas:", NEGRITA));
        cell.setColspan(15);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        table.addCell(cell);

        int materiasAprobadas = notaFacade.cantidadNotasAprobadas(carrera.getId_carrera(), estudiante.getId_persona()).intValue();
        int materiasCarrera = materiaFacade.cantidadMateriasCurriculares(carrera.getId_carrera()).intValue();
        cell = new PdfPCell(new Phrase(materiasAprobadas + "/" + materiasCarrera, NORMAL));
        cell.setColspan(5);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        table.addCell(cell);

        //fila 3
        cell = new PdfPCell(new Phrase("0 a " + (carrera.getRegimen().getNotaMinimaAprobacion() - 1), NORMAL));
        cell.setColspan(12);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("REPROBADO", NORMAL));
        cell.setColspan(8);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Promedio de calificaciones:", NEGRITA));
        cell.setColspan(15);
        cell.setRowspan(2);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        table.addCell(cell);

        Double promedioGeneral = notaFacade.promedioReporteHistorialAcademico(estudiante.getId_persona(), carrera.getId_carrera());
        if (promedioGeneral != null) {
            int promedioGeneralRedondeado = Redondeo.redondear_HALFUP(promedioGeneral, 0).intValue();
            cell = new PdfPCell(new Phrase(String.valueOf(promedioGeneralRedondeado), NORMAL));
        } else {
            cell = new PdfPCell(new Phrase(" ", NORMAL));
        }
        cell.setColspan(5);
        cell.setRowspan(2);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        table.addCell(cell);

        //fila 5
        cell = new PdfPCell(new Phrase(carrera.getRegimen().getNotaMinimaAprobacion().toString(), NORMAL));
        cell.setColspan(12);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("NOTA MÍNIMA", NORMAL));
        cell.setColspan(8);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        table.addCell(cell);

        //fila 6
        cell = new PdfPCell(new Phrase("Cualquier raspadura o enmienda invalida el presente documento", NORMAL_ITALICA));
        cell.setColspan(60);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        table.addCell(cell);

        return table;
    }

    class HeaderFooter extends PdfPageEventHelper {

        PdfTemplate t;
        Image total;
        Estudiante estudiante;
        Carrera carrera;

        public HeaderFooter(Estudiante estudiante, Carrera carrera) {
            this.estudiante = estudiante;
            this.carrera = carrera;
        }

        @Override
        public void onOpenDocument(PdfWriter writer, Document document) {
            t = writer.getDirectContent().createTemplate(30, 16);
            try {
                total = Image.getInstance(t);
                total.setRole(PdfName.ARTIFACT);
            } catch (DocumentException de) {
                throw new ExceptionConverter(de);
            }
        }

        @Override
        public void onStartPage(PdfWriter writer, Document document) {
            try {
                PdfPTable table = new PdfPTable(20);

                PdfPCell cell = new PdfPCell(new Phrase(" ", NEGRITA));
                cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setColspan(20);
                table.addCell(cell);

                Phrase phrase = new Phrase();
                phrase.add(new Chunk("Código de registro: ", NEGRITA));
                GestionAcademica finFormacion = notaFacade.finFormacion(carrera.getId_carrera(), estudiante.getId_persona());
                if (finFormacion != null) {
                    Inscrito inscrito = inscritoFacade.buscarInscrito(estudiante.getId_persona(), carrera.getId_carrera(), finFormacion.getId_gestionacademica());
                    if (inscrito != null) {
                        phrase.add(new Chunk(String.valueOf(inscrito.getCodigo()), NORMAL));
                    } else {
                        phrase.add(new Chunk(" ", NORMAL));
                    }
                } else {
                    phrase.add(new Chunk(" ", NORMAL));
                }
                cell = new PdfPCell(phrase);
                cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setColspan(20);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(" ", ESPACIO));
                cell.setColspan(20);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("HISTORIAL ACADÉMICO", TITULO));
                cell.setColspan(20);
                cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(" ", ESPACIO));
                cell.setColspan(20);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);

                //fila 0
                phrase = new Phrase();
                phrase.add(new Chunk("INSTITUTO: ", NEGRITA));
                phrase.add(new Chunk(carrera.getCampus().getInstituto().getNombreRegulador(), NORMAL));
                cell = new PdfPCell(phrase);
                cell.setColspan(20);
                cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);

                //fila 1
                phrase = new Phrase();
                phrase.add(new Chunk("MATRÍCULA: ", NEGRITA));
                if (estudiante.getMatricula() != null) {
                    phrase.add(new Chunk(String.valueOf(estudiante.getMatricula()), NORMAL));
                } else {
                    phrase.add(new Chunk(String.valueOf(" "), NORMAL));
                }
                cell = new PdfPCell(phrase);
                cell.setColspan(15);
                cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);

                phrase = new Phrase();
                phrase.add(new Chunk("Cédula de Identidad: ", NEGRITA));
                phrase.add(new Chunk(estudiante.dniLugar(), NORMAL));
                cell = new PdfPCell(phrase);
                cell.setColspan(5);
                cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);

                //fila 2
                phrase = new Phrase();
                phrase.add(new Chunk("ESTUDIANTE: ", NEGRITA));
                phrase.add(new Chunk(estudiante.toString(), NORMAL));
                cell = new PdfPCell(phrase);
                cell.setColspan(15);
                cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);

                phrase = new Phrase();
                phrase.add(new Chunk("FECHA DE ADMISIÓN: ", NEGRITA));
                GestionAcademica inicioFormacion = notaFacade.inicioFormacion(carrera.getId_carrera(), estudiante.getId_persona());
                if (inicioFormacion != null) {
                    phrase.add(new Chunk(Fecha.formatearFecha_ddMMyyyy(inicioFormacion.getInicio()), NORMAL));
                } else {
                    phrase.add(new Chunk(" ", NORMAL));
                }
                cell = new PdfPCell(phrase);
                cell.setColspan(5);
                cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);

                //fial 3
                phrase = new Phrase();
                phrase.add(new Chunk("CARRERA: ", NEGRITA));
                phrase.add(new Chunk(carrera.getNombre(), NORMAL));
                cell = new PdfPCell(phrase);
                cell.setColspan(15);
                cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);

                phrase = new Phrase();
                phrase.add(new Chunk("FECHA DE CONCLUSIÓN: ", NEGRITA));
                if (finFormacion != null) {
                    phrase.add(new Chunk(Fecha.formatearFecha_ddMMyyyy(finFormacion.getFin()), NORMAL));
                } else {
                    phrase.add(new Chunk(" ", NORMAL));
                }
                cell = new PdfPCell(phrase);
                cell.setColspan(5);
                cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);

                //fila 4
                phrase = new Phrase();
                phrase.add(new Chunk("MENCIÓN: ", NEGRITA));
                phrase.add(new Chunk(" ", NORMAL)); //orion no soporta menciones
                cell = new PdfPCell(phrase);
                cell.setColspan(20);
                cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);

                //fila 5
                phrase = new Phrase();
                phrase.add(new Chunk("NIVEL DE FORMACIÓN: ", NEGRITA));
                phrase.add(new Chunk(carrera.getNivelAcademico().getNombre(), NORMAL));
                cell = new PdfPCell(phrase);
                cell.setColspan(20);
                cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);

                //fila 6
                phrase = new Phrase();
                phrase.add(new Chunk("RÉGIMEN: ", NEGRITA));
                phrase.add(new Chunk(carrera.getRegimen().getNombre(), NORMAL));
                cell = new PdfPCell(phrase);
                cell.setColspan(20);
                cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(" ", ESPACIO));
                cell.setColspan(20);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);

                document.add(table);

                table = new PdfPTable(66);

                cell = new PdfPCell(new Phrase("Nº", NEGRITA));
                cell.setColspan(2);
                cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
                cell.setBackgroundColor(new BaseColor(183, 222, 232));
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("GEST. ACAD.", NEGRITA));
                cell.setColspan(4);
                cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
                cell.setBackgroundColor(new BaseColor(183, 222, 232));
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("SEMESTRE/AÑO", NEGRITA));
                cell.setColspan(6);
                cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
                cell.setBackgroundColor(new BaseColor(183, 222, 232));
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("CÓDIGO", NEGRITA));
                cell.setColspan(5);
                cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
                cell.setBackgroundColor(new BaseColor(183, 222, 232));
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("ASIGNATURA", NEGRITA));
                cell.setColspan(28);
                cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
                cell.setBackgroundColor(new BaseColor(183, 222, 232));
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("PRE REQUISITO", NEGRITA));
                cell.setColspan(6);
                cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
                cell.setBackgroundColor(new BaseColor(183, 222, 232));
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("NOTA", NEGRITA));
                cell.setColspan(4);
                cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
                cell.setBackgroundColor(new BaseColor(183, 222, 232));
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("PRUEBA RECUP.", NEGRITA));
                cell.setColspan(5);
                cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
                cell.setBackgroundColor(new BaseColor(183, 222, 232));
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("OBS.", NEGRITA));
                cell.setColspan(6);
                cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
                cell.setBackgroundColor(new BaseColor(183, 222, 232));
                table.addCell(cell);

                document.add(table);
            } catch (BadElementException ex) {
                Logger.getLogger(HistorialAcademico.class.getName()).log(Level.SEVERE, null, ex);
            } catch (DocumentException ex) {
                Logger.getLogger(HistorialAcademico.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        @Override
        public void onEndPage(PdfWriter writer, Document document) {
            PdfPTable table = new PdfPTable(3);
            try {
                table.setWidths(new int[]{24, 24, 2});
                table.setTotalWidth(522);
                table.getDefaultCell().setFixedHeight(20);
                table.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
                table.addCell(new Phrase(" ", NORMAL));
                table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
                table.addCell(new Phrase(String.format("Página %d de", writer.getPageNumber()), NORMAL));
                PdfPCell cell = new PdfPCell(total);
                cell.setBorder(PdfPCell.NO_BORDER);
                table.addCell(cell);
                PdfContentByte canvas = writer.getDirectContent();
                canvas.beginMarkedContentSequence(PdfName.ARTIFACT);
                table.writeSelectedRows(0, -1, 36, 30, canvas);
                canvas.endMarkedContentSequence();
            } catch (DocumentException de) {
                throw new ExceptionConverter(de);
            }
        }

        @Override
        public void onCloseDocument(PdfWriter writer, Document document) {
            ColumnText.showTextAligned(t, Element.ALIGN_LEFT,
                    new Phrase(String.valueOf((writer.getPageNumber())), NORMAL),
                    2, 7, 0);
        }
    }
}
