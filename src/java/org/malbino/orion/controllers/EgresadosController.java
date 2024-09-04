/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.malbino.orion.controllers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.malbino.orion.entities.Carrera;
import org.malbino.orion.entities.Egresado;
import org.malbino.orion.entities.GestionAcademica;
import org.malbino.orion.entities.Nota;
import org.malbino.orion.enums.Nivel;
import org.malbino.orion.enums.Regimen;
import org.malbino.orion.facades.EgresadoFacade;
import org.malbino.orion.facades.NotaFacade;
import org.malbino.orion.facades.negocio.TitulacionFacade;
import org.malbino.orion.util.Fecha;
import org.malbino.orion.util.NumberToLetterConverter;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Tincho
 */
@Named("EgresadosController")
@SessionScoped
public class EgresadosController extends AbstractController implements Serializable {

    private static final Logger log = LoggerFactory.getLogger(EgresadosController.class);

    private static final String PATHNAME_CE = File.separator + "resources" + File.separator + "uploads" + File.separator + "certificado_egreso.xlsx";

    private static final String PATHNAME_CC_SEMESTRAL = File.separator + "resources" + File.separator + "uploads" + File.separator + "certificado_calificaciones_semestral.xlsx";
    private static final String PATHNAME_CC_ANUAL = File.separator + "resources" + File.separator + "uploads" + File.separator + "certificado_calificaciones_anual.xlsx";

    @EJB
    EgresadoFacade egresadoFacade;
    @EJB
    TitulacionFacade titulacionFacade;
    @EJB
    NotaFacade notaFacade;

    @Inject
    LoginController loginController;

    private Carrera seleccionCarrera;

    private List<Egresado> egresados;
    private Egresado seleccionEgresado;
    private Date seleccionFecha;

    private String keyword;

    private StreamedContent download;

    @PostConstruct
    public void init() {
        seleccionCarrera = null;

        egresados = new ArrayList<>();
        seleccionEgresado = null;

        keyword = null;
    }

    public void reinit() {
        seleccionCarrera = null;

        egresados = new ArrayList<>();
        seleccionEgresado = null;

        keyword = null;
    }

    public void buscarSinKeyword() {
        keyword = "";

        if (seleccionCarrera == null) {
            egresados = egresadoFacade.buscar(keyword);
        } else {
            egresados = egresadoFacade.buscar(seleccionCarrera.getId_carrera(), keyword);
        }
    }

    public void buscarConKeyword() {
        if (seleccionCarrera == null) {
            egresados = egresadoFacade.buscar(keyword);
        } else {
            egresados = egresadoFacade.buscar(seleccionCarrera.getId_carrera(), keyword);
        }
    }

    public void actualizarEgresados() {
        if (seleccionCarrera != null) {
            titulacionFacade.actualizarEgresados(seleccionCarrera, null);
        }
    }

    public XSSFWorkbook leerArchivo(String pathname) {
        XSSFWorkbook workbook = null;

        try ( FileInputStream file = new FileInputStream(this.realPath() + pathname)) {
            workbook = new XSSFWorkbook(file);
        } catch (IOException e) {
            this.mensajeDeError("Error: No se pudo leer el archivo.");
        }

        return workbook;
    }

    public void descargarArchivo(XSSFWorkbook workbook, String nombre) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            workbook.write(baos);
            workbook.close();

            InputStream is = new ByteArrayInputStream(baos.toByteArray());

            download = DefaultStreamedContent.builder()
                    .name(nombre + ".xlsx")
                    .contentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                    .stream(() -> is)
                    .build();
        } catch (IOException ex) {
            this.mensajeDeError("Error: No se pudo descargar el archivo.");
        }
    }

    public void descargarCertificadoEgreso() {
        XSSFWorkbook workbook = leerArchivo(PATHNAME_CE);

        XSSFCellStyle styleBold = workbook.createCellStyle();
        styleBold.setAlignment(HorizontalAlignment.LEFT);
        styleBold.setVerticalAlignment(VerticalAlignment.CENTER);
        XSSFFont fontBold = workbook.createFont();
        fontBold.setFontName(HSSFFont.FONT_ARIAL);
        fontBold.setFontHeightInPoints((short) 8);
        fontBold.setBold(true);
        styleBold.setFont(fontBold);

        XSSFCellStyle styleSmall = workbook.createCellStyle();
        styleSmall.setAlignment(HorizontalAlignment.LEFT);
        styleSmall.setVerticalAlignment(VerticalAlignment.CENTER);
        XSSFFont fontSmall = workbook.createFont();
        fontSmall.setFontName(HSSFFont.FONT_ARIAL);
        fontSmall.setFontHeightInPoints((short) 6);
        styleSmall.setFont(fontSmall);

        XSSFSheet sheet = workbook.getSheetAt(0);
        if (sheet != null) {
            log.info("Sheet: " + sheet.getSheetName());
            if (sheet != null) {
                Iterator<Row> rowIterator = sheet.rowIterator();

                int rowNum = 0;
                while (rowIterator.hasNext()) {
                    Row row = rowIterator.next();

                    Iterator<Cell> cellIterator = row.cellIterator();
                    while (cellIterator.hasNext()) {
                        Cell cell = cellIterator.next();

                        if (cell.getCellTypeEnum() == CellType.STRING) {
                            if (cell.getStringCellValue().contains("<<INSTITUTO>>")) {
                                cell.setCellValue(cell.getStringCellValue().replace("<<INSTITUTO>>", seleccionEgresado.getCarrera().getCampus().getInstituto().getNombreRegulador()));
                            } else if (cell.getStringCellValue().contains("<<RM>>")) {
                                cell.setCellValue(cell.getStringCellValue().replace("<<RM>>", seleccionEgresado.getCarrera().getResolucionMinisterial1()));
                            } else if (cell.getStringCellValue().contains("<<PAPELLIDO>>")) {
                                cell.setCellValue(cell.getStringCellValue().replace("<<PAPELLIDO>>", seleccionEgresado.getEstudiante().getPrimerApellido()));
                            } else if (cell.getStringCellValue().contains("<<SAPELLIDO>>")) {
                                if (seleccionEgresado.getEstudiante().getSegundoApellido() != null && !seleccionEgresado.getEstudiante().getSegundoApellido().isEmpty()) {
                                    cell.setCellValue(cell.getStringCellValue().replace("<<SAPELLIDO>>", seleccionEgresado.getEstudiante().getSegundoApellido()));
                                } else {
                                    cell.setCellValue(cell.getStringCellValue().replace("<<SAPELLIDO>>", ""));
                                }
                            } else if (cell.getStringCellValue().contains("<<NOMBRE>>")) {
                                cell.setCellValue(cell.getStringCellValue().replace("<<NOMBRE>>", seleccionEgresado.getEstudiante().getNombre()));
                            } else if (cell.getStringCellValue().contains("<<NACIONALIDAD>>")) {
                                cell.setCellValue(cell.getStringCellValue().replace("<<NACIONALIDAD>>", seleccionEgresado.getEstudiante().getNacionalidad()));
                            } else if (cell.getStringCellValue().contains("<<CIUDAD>>")) {
                                cell.setCellValue(cell.getStringCellValue().replace("<<CIUDAD>>", seleccionEgresado.getEstudiante().getLugarNacimiento()));
                            } else if (cell.getStringCellValue().contains("<<DIA_N>>")) {
                                cell.setCellValue(cell.getStringCellValue().replace("<<DIA_N>>", Fecha.formatearFecha_dd(seleccionEgresado.getEstudiante().getFechaNacimiento())));
                            } else if (cell.getStringCellValue().contains("<<MES_N>>")) {
                                cell.setCellValue(cell.getStringCellValue().replace("<<MES_N>>", Fecha.formatearFecha_MMMM(seleccionEgresado.getEstudiante().getFechaNacimiento())));
                            } else if (cell.getStringCellValue().contains("<<AÑO_N>>")) {
                                cell.setCellValue(cell.getStringCellValue().replace("<<AÑO_N>>", Fecha.formatearFecha_yyyy(seleccionEgresado.getEstudiante().getFechaNacimiento())));
                            } else if (cell.getStringCellValue().contains("<<DNI>>")) {
                                cell.setCellValue(cell.getStringCellValue().replace("<<DNI>>", seleccionEgresado.getEstudiante().dniLugar()));
                            } else if (cell.getStringCellValue().contains("<<CARRERA>>")) {
                                cell.setCellValue(cell.getStringCellValue().replace("<<CARRERA>>", seleccionEgresado.getCarrera().getNombre()));
                            } else if (cell.getStringCellValue().contains("<<NIVEL_ACADEMICO>>")) {
                                cell.setCellValue(cell.getStringCellValue().replace("<<NIVEL_ACADEMICO>>", seleccionEgresado.getCarrera().getNivelAcademico().getNombre()));
                            } else if (cell.getStringCellValue().contains("<<CIUDAD>>")) {
                                cell.setCellValue(cell.getStringCellValue().replace("<<CIUDAD>>", seleccionEgresado.getCarrera().getCampus().getCiudad()));
                            } else if (cell.getStringCellValue().contains("<<DIA_I>>")) {
                                cell.setCellValue(cell.getStringCellValue().replace("<<DIA_I>>", Fecha.formatearFecha_dd(seleccionFecha)));
                            } else if (cell.getStringCellValue().contains("<<MES_I>>")) {
                                cell.setCellValue(cell.getStringCellValue().replace("<<MES_I>>", Fecha.formatearFecha_MMMM(seleccionFecha)));
                            } else if (cell.getStringCellValue().contains("<<AÑO_I>>")) {
                                cell.setCellValue(cell.getStringCellValue().replace("<<AÑO_I>>", Fecha.formatearFecha_yyyy(seleccionFecha)));
                            }
                        }
                    }
                }
            }

            String name = "CE - " + seleccionEgresado.getEstudiante().toString() + " - " + seleccionEgresado.getCarrera().getNombre();
            descargarArchivo(workbook, name);
        }
    }

    public void descargarCertificadoCalificaciones() {
        XSSFWorkbook workbook = null;
        if (seleccionEgresado.getCarrera().getRegimen().equals(Regimen.SEMESTRAL)) {
            workbook = leerArchivo(PATHNAME_CC_SEMESTRAL);
        } else if (seleccionEgresado.getCarrera().getRegimen().equals(Regimen.ANUAL)) {
            workbook = leerArchivo(PATHNAME_CC_ANUAL);
        }

        XSSFCellStyle styleBold = workbook.createCellStyle();
        styleBold.setAlignment(HorizontalAlignment.LEFT);
        styleBold.setVerticalAlignment(VerticalAlignment.CENTER);
        XSSFFont fontBold = workbook.createFont();
        fontBold.setFontName(HSSFFont.FONT_ARIAL);
        fontBold.setFontHeightInPoints((short) 8);
        fontBold.setBold(true);
        styleBold.setFont(fontBold);

        XSSFCellStyle styleSmall = workbook.createCellStyle();
        styleSmall.setAlignment(HorizontalAlignment.LEFT);
        styleSmall.setVerticalAlignment(VerticalAlignment.CENTER);
        XSSFFont fontSmall = workbook.createFont();
        fontSmall.setFontName(HSSFFont.FONT_ARIAL);
        fontSmall.setFontHeightInPoints((short) 6);
        styleSmall.setFont(fontSmall);

        Nivel[] niveles = Nivel.values(seleccionEgresado.getCarrera().getRegimen());
        for (int i = 0; i < niveles.length; i++) {
            Nivel nivel = niveles[i];
            log.info("Nivel: " + nivel.getNombre());

            List<Nota> historialAcademico = notaFacade.reporteHistorialAcademico(seleccionEgresado.getEstudiante(), seleccionEgresado.getCarrera(), seleccionEgresado.getMencion(), nivel);
            Iterator<Nota> iteratorHistorialAcademico = historialAcademico.iterator();
            log.info("Historial Academico: " + historialAcademico.size());
            List<Nota> historialAcademicoRecuperacion = notaFacade.reporteHistorialAcademicoRecuperatorio(seleccionEgresado.getEstudiante(), seleccionEgresado.getCarrera(), seleccionEgresado.getMencion(), nivel);
            Iterator<Nota> iteratorHistorialAcademicoRecuperacion = historialAcademicoRecuperacion.iterator();
            log.info("Historial Academico Recuperacion: " + historialAcademicoRecuperacion.size());

            XSSFSheet sheet = workbook.getSheetAt(i);
            log.info("Sheet: " + sheet.getSheetName());
            if (sheet != null) {
                Iterator<Row> rowIterator = sheet.rowIterator();

                int rowNum = 0;
                while (rowIterator.hasNext()) {
                    Row row = rowIterator.next();

                    Iterator<Cell> cellIterator = row.cellIterator();
                    while (cellIterator.hasNext()) {
                        Cell cell = cellIterator.next();

                        if (cell.getCellTypeEnum() == CellType.STRING) {
                            if (cell.getStringCellValue().contains("<<INSTITUTO>>")) {
                                cell.setCellValue(cell.getStringCellValue().replace("<<INSTITUTO>>", seleccionEgresado.getCarrera().getCampus().getInstituto().getNombreRegulador()));
                            } else if (cell.getStringCellValue().contains("<<PAPELLIDO>>")) {
                                cell.setCellValue(cell.getStringCellValue().replace("<<PAPELLIDO>>", seleccionEgresado.getEstudiante().getPrimerApellido()));
                            } else if (cell.getStringCellValue().contains("<<SAPELLIDO>>")) {
                                if (seleccionEgresado.getEstudiante().getSegundoApellido() != null && !seleccionEgresado.getEstudiante().getSegundoApellido().isEmpty()) {
                                    cell.setCellValue(cell.getStringCellValue().replace("<<SAPELLIDO>>", seleccionEgresado.getEstudiante().getSegundoApellido()));
                                } else {
                                    cell.setCellValue(cell.getStringCellValue().replace("<<SAPELLIDO>>", ""));
                                }
                            } else if (cell.getStringCellValue().contains("<<NOMBRE>>")) {
                                cell.setCellValue(cell.getStringCellValue().replace("<<NOMBRE>>", seleccionEgresado.getEstudiante().getNombre()));
                            } else if (cell.getStringCellValue().contains("<<DNI>>")) {
                                cell.setCellValue(cell.getStringCellValue().replace("<<DNI>>", seleccionEgresado.getEstudiante().dniLugar()));
                            } else if (cell.getStringCellValue().contains("<<AREA>>")) {
                                cell.setCellValue(cell.getStringCellValue().replace("<<AREA>>", seleccionEgresado.getCarrera().getArea()));
                            } else if (cell.getStringCellValue().contains("<<CARRERA>>")) {
                                cell.setCellValue(cell.getStringCellValue().replace("<<CARRERA>>", seleccionEgresado.getCarrera().getNombre()));
                            } else if (cell.getStringCellValue().contains("<<NA>>")) {
                                cell.setCellValue(cell.getStringCellValue().replace("<<NA>>", seleccionEgresado.getCarrera().getNivelAcademico().getNombre()));
                            } else if (cell.getStringCellValue().contains("<<MAT>>")) {
                                cell.setCellValue(cell.getStringCellValue().replace("<<MAT>>", seleccionEgresado.getEstudiante().getId_persona().toString()));
                            } else if (cell.getStringCellValue().contains("<<NE>>")) {
                                cell.setCellValue(cell.getStringCellValue().replace("<<NE>>", nivel.getOrdinal()));
                            } else if (cell.getStringCellValue().contains("<<GA>>")) {
                                List<GestionAcademica> gestionesAcademicas = gestionAcademicaFacade.listaGestionesAcademicas(seleccionEgresado.getEstudiante(), seleccionEgresado.getCarrera(), seleccionEgresado.getMencion(), nivel);
                                Iterator<GestionAcademica> iteratorGestionesAcademicas = gestionesAcademicas.iterator();
                                String s = "";
                                if (iteratorGestionesAcademicas.hasNext()) {
                                    GestionAcademica gestionAcademica = iteratorGestionesAcademicas.next();
                                    s = gestionAcademica.codigo();
                                }
                                while (iteratorGestionesAcademicas.hasNext()) {
                                    GestionAcademica gestionAcademica = iteratorGestionesAcademicas.next();
                                    s = s + ", " + gestionAcademica.codigo();
                                }
                                cell.setCellValue(cell.getStringCellValue().replace("<<GA>>", s));

                                rowNum = cell.getRow().getRowNum();
                            } else if (cell.getStringCellValue().contains("<<DIA>>")) {
                                cell.setCellValue(cell.getStringCellValue().replace("<<DIA>>", Fecha.formatearFecha_dd(seleccionFecha)));
                            } else if (cell.getStringCellValue().contains("<<MES>>")) {
                                cell.setCellValue(cell.getStringCellValue().replace("<<MES>>", Fecha.formatearFecha_MMMM(seleccionFecha)));
                            } else if (cell.getStringCellValue().contains("<<AÑO>>")) {
                                cell.setCellValue(cell.getStringCellValue().replace("<<AÑO>>", Fecha.formatearFecha_yyyy(seleccionFecha)));
                            }
                        }
                    }
                }

                int j = 0;

                // notas
                while (iteratorHistorialAcademico.hasNext()) {
                    Nota nota = iteratorHistorialAcademico.next();

                    if (nota.getRecuperatorio() == null) {
                        XSSFRow row = sheet.getRow(rowNum + 4 + j);
                        Iterator<Cell> cellIterator = row.cellIterator();

                        while (cellIterator.hasNext()) {
                            Cell cell = cellIterator.next();

                            if (cell.getCellTypeEnum() == CellType.STRING) {
                                if (cell.getStringCellValue().contains("<<CODIGO>>")) {
                                    cell.setCellValue(cell.getStringCellValue().replace("<<CODIGO>>", nota.getMateria().getCodigo()));
                                } else if (cell.getStringCellValue().contains("<<MATERIA>>")) {
                                    if (nota.getMateria().getNombre().length() <= 38) {
                                        cell.setCellValue(cell.getStringCellValue().replace("<<MATERIA>>", nota.getMateria().getNombre()));
                                    } else {
                                        cell.setCellValue(cell.getStringCellValue().replace("<<MATERIA>>", nota.getMateria().getNombre()));
                                        cell.setCellStyle(styleSmall);
                                    }
                                } else if (cell.getStringCellValue().contains("<<LITERAL>>")) {
                                    if (nota.getNotaFinal() != null) {
                                        cell.setCellValue(cell.getStringCellValue().replace("<<LITERAL>>", NumberToLetterConverter.convertNumberToLetter(nota.getNotaFinal())));
                                    } else {
                                        cell.setCellValue(cell.getStringCellValue().replace("<<LITERAL>>", ""));
                                    }
                                } else if (cell.getStringCellValue().contains("<<CONDICION>>")) {
                                    cell.setCellValue(cell.getStringCellValue().replace("<<CONDICION>>", nota.getCondicion().getNombre() + " - " + nota.getGestionAcademica().codigo()));
                                }
                            } else if (cell.getCellTypeEnum() == CellType.NUMERIC) {
                                if (cell.getNumericCellValue() == -100) {
                                    if (nota.getNotaFinal() != null) {
                                        cell.setCellValue(nota.getNotaFinal());
                                    } else {
                                        cell.setCellValue("");
                                    }
                                }
                            }
                        }
                    } else {
                        XSSFRow row = sheet.getRow(rowNum + 4 + j);
                        Iterator<Cell> cellIterator = row.cellIterator();

                        while (cellIterator.hasNext()) {
                            Cell cell = cellIterator.next();

                            if (cell.getCellTypeEnum() == CellType.STRING) {
                                if (cell.getStringCellValue().contains("<<CODIGO>>")) {
                                    cell.setCellValue(cell.getStringCellValue().replace("<<CODIGO>>", nota.getMateria().getCodigo()));
                                } else if (cell.getStringCellValue().contains("<<MATERIA>>")) {
                                    if (nota.getMateria().getNombre().length() <= 38) {
                                        cell.setCellValue(cell.getStringCellValue().replace("<<MATERIA>>", nota.getMateria().getNombre()));
                                    } else {
                                        cell.setCellValue(cell.getStringCellValue().replace("<<MATERIA>>", nota.getMateria().getNombre()));
                                        cell.setCellStyle(styleSmall);
                                    }
                                } else if (cell.getStringCellValue().contains("<<LITERAL>>")) {
                                    cell.setCellValue(cell.getStringCellValue().replace("<<LITERAL>>", NumberToLetterConverter.convertNumberToLetter(nota.getNotaFinal())));
                                } else if (cell.getStringCellValue().contains("<<CONDICION>>")) {
                                    cell.setCellValue(cell.getStringCellValue().replace("<<CONDICION>>", "RECUPERACIÓN - " + nota.getGestionAcademica().codigo()));
                                }
                            } else if (cell.getCellTypeEnum() == CellType.NUMERIC) {
                                if (cell.getNumericCellValue() == -100) {
                                    if (nota.getNotaFinal() != null) {
                                        cell.setCellValue(nota.getNotaFinal());
                                    } else {
                                        cell.setCellValue("");
                                    }
                                }
                            }
                        }
                    }

                    j++;
                }

                //recuperatorio
                if (!historialAcademicoRecuperacion.isEmpty()) {
                    XSSFRow row = sheet.getRow(rowNum + 4 + j);
                    Iterator<Cell> cellIterator = row.cellIterator();

                    while (cellIterator.hasNext()) {
                        Cell cell = cellIterator.next();

                        if (cell.getCellTypeEnum() == CellType.STRING) {
                            if (cell.getStringCellValue().contains("<<CODIGO>>")) {
                                cell.setCellValue(cell.getStringCellValue().replace("<<CODIGO>>", ""));
                            } else if (cell.getStringCellValue().contains("<<MATERIA>>")) {
                                cell.setCellValue(cell.getStringCellValue().replace("<<MATERIA>>", "PRUEBA DE RECUPERACIÓN"));
                                cell.setCellStyle(styleBold);
                            } else if (cell.getStringCellValue().contains("<<LITERAL>>")) {
                                cell.setCellValue(cell.getStringCellValue().replace("<<LITERAL>>", ""));
                            } else if (cell.getStringCellValue().contains("<<CONDICION>>")) {
                                cell.setCellValue(cell.getStringCellValue().replace("<<CONDICION>>", ""));
                            }
                        } else if (cell.getCellTypeEnum() == CellType.NUMERIC) {
                            if (cell.getNumericCellValue() == -100) {
                                cell.setCellValue("");
                            }
                        }
                    }

                    j++;

                    while (iteratorHistorialAcademicoRecuperacion.hasNext()) {
                        Nota nota = iteratorHistorialAcademicoRecuperacion.next();

                        row = sheet.getRow(rowNum + 4 + j);
                        cellIterator = row.cellIterator();

                        while (cellIterator.hasNext()) {
                            Cell cell = cellIterator.next();

                            if (cell.getCellTypeEnum() == CellType.STRING) {
                                if (cell.getStringCellValue().contains("<<CODIGO>>")) {
                                    cell.setCellValue(cell.getStringCellValue().replace("<<CODIGO>>", nota.getMateria().getCodigo()));
                                } else if (cell.getStringCellValue().contains("<<MATERIA>>")) {
                                    if (nota.getMateria().getNombre().length() <= 38) {
                                        cell.setCellValue(cell.getStringCellValue().replace("<<MATERIA>>", nota.getMateria().getNombre()));
                                    } else {
                                        cell.setCellValue(cell.getStringCellValue().replace("<<MATERIA>>", nota.getMateria().getNombre()));
                                        cell.setCellStyle(styleSmall);
                                    }
                                } else if (cell.getStringCellValue().contains("<<LITERAL>>")) {
                                    cell.setCellValue(cell.getStringCellValue().replace("<<LITERAL>>", NumberToLetterConverter.convertNumberToLetter(nota.getRecuperatorio())));
                                } else if (cell.getStringCellValue().contains("<<CONDICION>>")) {
                                    cell.setCellValue(cell.getStringCellValue().replace("<<CONDICION>>", nota.getCondicion().getNombre()));
                                }
                            } else if (cell.getCellTypeEnum() == CellType.NUMERIC) {
                                if (cell.getNumericCellValue() == -100) {
                                    if (nota.getNotaFinal() != null) {
                                        cell.setCellValue(nota.getRecuperatorio());
                                    } else {
                                        cell.setCellValue("");
                                    }
                                }
                            }
                        }

                        j++;
                    }
                }

                //vacios
                while (j < 14) {
                    XSSFRow row = sheet.getRow(rowNum + 4 + j);
                    Iterator<Cell> cellIterator = row.cellIterator();

                    while (cellIterator.hasNext()) {
                        Cell cell = cellIterator.next();

                        if (cell.getCellTypeEnum() == CellType.STRING) {
                            if (cell.getStringCellValue().contains("<<CODIGO>>")) {
                                cell.setCellValue(cell.getStringCellValue().replace("<<CODIGO>>", ""));
                            } else if (cell.getStringCellValue().contains("<<MATERIA>>")) {
                                cell.setCellValue(cell.getStringCellValue().replace("<<MATERIA>>", ""));
                            } else if (cell.getStringCellValue().contains("<<LITERAL>>")) {
                                cell.setCellValue(cell.getStringCellValue().replace("<<LITERAL>>", ""));
                            } else if (cell.getStringCellValue().contains("<<CONDICION>>")) {
                                cell.setCellValue(cell.getStringCellValue().replace("<<CONDICION>>", ""));
                            }
                        } else if (cell.getCellTypeEnum() == CellType.NUMERIC) {
                            if (cell.getNumericCellValue() == -100) {
                                cell.setCellValue("");
                            }
                        }
                    }

                    j++;
                }

            }
        }

        String name = "CC - " + seleccionEgresado.getEstudiante().toString() + " - " + seleccionEgresado.getCarrera().getNombre();
        descargarArchivo(workbook, name);
    }

    public void toEgresados() throws IOException {
        this.redireccionarViewId("/titulacion/egresados/egresados");
    }

    /**
     * @return the seleccionCarrera
     */
    public Carrera getSeleccionCarrera() {
        return seleccionCarrera;
    }

    /**
     * @param seleccionCarrera the seleccionCarrera to set
     */
    public void setSeleccionCarrera(Carrera seleccionCarrera) {
        this.seleccionCarrera = seleccionCarrera;
    }

    /**
     * @return the egresados
     */
    public List<Egresado> getEgresados() {
        return egresados;
    }

    /**
     * @param egresados the egresados to set
     */
    public void setEgresados(List<Egresado> egresados) {
        this.egresados = egresados;
    }

    /**
     * @return the seleccionEgresado
     */
    public Egresado getSeleccionEgresado() {
        return seleccionEgresado;
    }

    /**
     * @param seleccionEgresado the seleccionEgresado to set
     */
    public void setSeleccionEgresado(Egresado seleccionEgresado) {
        this.seleccionEgresado = seleccionEgresado;
    }

    /**
     * @return the keyword
     */
    public String getKeyword() {
        return keyword;
    }

    /**
     * @param keyword the keyword to set
     */
    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    /**
     * @return the download
     */
    public StreamedContent getDownload() {
        return download;
    }

    /**
     * @param download the download to set
     */
    public void setDownload(StreamedContent download) {
        this.download = download;
    }

    /**
     * @return the seleccionFecha
     */
    public Date getSeleccionFecha() {
        return seleccionFecha;
    }

    /**
     * @param seleccionFecha the seleccionFecha to set
     */
    public void setSeleccionFecha(Date seleccionFecha) {
        this.seleccionFecha = seleccionFecha;
    }

}
