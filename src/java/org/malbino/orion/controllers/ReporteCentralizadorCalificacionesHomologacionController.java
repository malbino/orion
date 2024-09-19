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
import java.util.Iterator;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellCopyPolicy;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.PrintOrientation;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.malbino.orion.entities.Carrera;
import org.malbino.orion.entities.GestionAcademica;
import org.malbino.orion.entities.Log;
import org.malbino.orion.enums.EventoLog;
import org.malbino.orion.facades.NotaFacade;
import org.malbino.orion.facades.negocio.CentralizadorCalificacionesHomologacionFacade;
import org.malbino.orion.pojos.centralizador.Centralizador;
import org.malbino.orion.pojos.centralizador.EstudianteCentralizador;
import org.malbino.orion.pojos.centralizador.PaginaCentralizador;
import org.malbino.orion.pojos.centralizador.PaginaEstadisticas;
import org.malbino.orion.pojos.centralizador.PaginaNotas;
import org.malbino.orion.util.Fecha;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author Tincho
 */
@Named("ReporteCentralizadorCalificacionesHomologacionController")
@SessionScoped
public class ReporteCentralizadorCalificacionesHomologacionController extends AbstractController implements Serializable {

    private static final String PATHNAME = File.separator + "resources" + File.separator + "uploads" + File.separator + "centralizador.xlsx";

    @EJB
    CentralizadorCalificacionesHomologacionFacade centralizadorCalificacionesHomologacionFacade;
    @EJB
    NotaFacade notaFacade;
    @Inject
    LoginController loginController;

    private GestionAcademica seleccionGestionAcademica;
    private Carrera seleccionCarrera;
    private Integer numeroLibro;
    private Integer numeroFolio;

    private StreamedContent download;

    @PostConstruct
    public void init() {
        seleccionGestionAcademica = null;
        seleccionCarrera = null;
        numeroLibro = null;
        numeroFolio = null;
    }

    public void reinit() {
        seleccionGestionAcademica = null;
        seleccionCarrera = null;
        numeroLibro = null;
        numeroFolio = null;
    }

    @Override
    public List<Carrera> listaCarreras() {
        List<Carrera> l = new ArrayList();
        if (seleccionGestionAcademica != null) {
            l = carreraFacade.listaCarreras(seleccionGestionAcademica.getRegimen());
        }
        return l;
    }

    public void generarPDF() throws IOException {
        if (seleccionGestionAcademica != null && seleccionCarrera != null && numeroLibro != null && numeroFolio != null) {
            //generamos el centralizador
            Centralizador centralizador = centralizadorCalificacionesHomologacionFacade.centralizadorCalificacionesHomologacion(seleccionGestionAcademica, seleccionCarrera, numeroLibro, numeroFolio);
            this.insertarParametro("centralizador", centralizador);

            toCentralizadorCalificacionesHomologacion();

            //log
            logFacade.create(new Log(Fecha.getDate(), EventoLog.READ, "Generación reporte centralizador de calificaciones en formato PDF", loginController.getUsr().toString()));
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

    public void generarXLSX() {
        XSSFWorkbook workbook = leerArchivo(PATHNAME);

        //generamos el centralizador
        Centralizador centralizador = centralizadorCalificacionesHomologacionFacade.centralizadorCalificacionesHomologacion(seleccionGestionAcademica, seleccionCarrera, numeroLibro, numeroFolio);

        if (centralizador.getPaginasCentralizador().size() > 0) {
            String sheetName = "";

            for (PaginaCentralizador paginaCentralizador : centralizador.getPaginasCentralizador()) { //paginas centralizador
                if (paginaCentralizador instanceof PaginaNotas) {
                    PaginaNotas paginaNotas = (PaginaNotas) paginaCentralizador;

                    XSSFSheet sheetNotas = workbook.getSheetAt(0); //notas
                    if (sheetNotas != null) {
                        sheetName = paginaNotas.getCurso() + " " + paginaNotas.getTurno();
                        XSSFSheet sheet = workbook.cloneSheet(0, sheetName + " " + paginaNotas.getNumeroFolio());

                        sheet.setFitToPage(true);
                        sheet.getPrintSetup().setOrientation(PrintOrientation.LANDSCAPE);

                        Iterator<Row> rowIterator = sheet.rowIterator();

                        int rowNum = 0;
                        while (rowIterator.hasNext()) {
                            Row row = rowIterator.next();

                            Iterator<Cell> cellIterator = row.cellIterator();
                            while (cellIterator.hasNext()) {
                                Cell cell = cellIterator.next();

                                if (cell.getCellTypeEnum() == CellType.STRING) {
                                    if (cell.getStringCellValue().contains("<<COD_REG>>")) {
                                        cell.setCellValue(cell.getStringCellValue().replace("<<COD_REG>>", paginaNotas.getCodigoRegistro()));
                                    } else if (cell.getStringCellValue().contains("<<TITULO>>")) {
                                        cell.getCellStyle().setWrapText(true);

                                        cell.setCellValue(cell.getStringCellValue().replace("<<TITULO>>", paginaNotas.getTitulo()));
                                    } else if (cell.getStringCellValue().contains("<<TURNO>>")) {
                                        cell.setCellValue(cell.getStringCellValue().replace("<<TURNO>>", paginaNotas.getTurno()));
                                    } else if (cell.getStringCellValue().contains("<<INSTITUTO>>")) {
                                        cell.setCellValue(cell.getStringCellValue().replace("<<INSTITUTO>>", centralizador.getInstitucion()));
                                    } else if (cell.getStringCellValue().contains("<<RM>>")) {
                                        cell.setCellValue(cell.getStringCellValue().replace("<<RM>>", centralizador.getResolucionMinisterial()));
                                    } else if (cell.getStringCellValue().contains("<<CARACTER>>")) {
                                        cell.setCellValue(cell.getStringCellValue().replace("<<CARACTER>>", centralizador.getCaracter()));
                                    } else if (cell.getStringCellValue().contains("<<GESTION>>")) {
                                        cell.setCellValue(cell.getStringCellValue().replace("<<GESTION>>", paginaNotas.getGestion()));
                                    } else if (cell.getStringCellValue().contains("<<NIVEL>>")) {
                                        cell.setCellValue(cell.getStringCellValue().replace("<<NIVEL>>", paginaNotas.getNivel()));
                                    } else if (cell.getStringCellValue().contains("<<CARRERA>>")) {
                                        cell.setCellValue(cell.getStringCellValue().replace("<<CARRERA>>", paginaNotas.getCarrera()));
                                    } else if (cell.getStringCellValue().contains("<<REGIMEN>>")) {
                                        cell.setCellValue(cell.getStringCellValue().replace("<<REGIMEN>>", paginaNotas.getRegimen()));
                                    } else if (cell.getStringCellValue().contains("<<CURSO>>")) {
                                        cell.setCellValue(cell.getStringCellValue().replace("<<CURSO>>", paginaNotas.getCurso()));
                                    } else if (cell.getStringCellValue().contains("<<C1>>")) {
                                        if (paginaNotas.getMateriasCentralizador()[0] != null) {
                                            cell.setCellValue(cell.getStringCellValue().replace("<<C1>>", paginaNotas.getMateriasCentralizador()[0].getCodigo()));
                                        } else {
                                            cell.setCellValue(cell.getStringCellValue().replace("<<C1>>", ""));
                                        }
                                    } else if (cell.getStringCellValue().contains("<<C2>>")) {
                                        if (paginaNotas.getMateriasCentralizador()[1] != null) {
                                            cell.setCellValue(cell.getStringCellValue().replace("<<C2>>", paginaNotas.getMateriasCentralizador()[1].getCodigo()));
                                        } else {
                                            cell.setCellValue(cell.getStringCellValue().replace("<<C2>>", ""));
                                        }
                                    } else if (cell.getStringCellValue().contains("<<C3>>")) {
                                        if (paginaNotas.getMateriasCentralizador()[2] != null) {
                                            cell.setCellValue(cell.getStringCellValue().replace("<<C3>>", paginaNotas.getMateriasCentralizador()[2].getCodigo()));
                                        } else {
                                            cell.setCellValue(cell.getStringCellValue().replace("<<C3>>", ""));
                                        }
                                    } else if (cell.getStringCellValue().contains("<<C4>>")) {
                                        if (paginaNotas.getMateriasCentralizador()[3] != null) {
                                            cell.setCellValue(cell.getStringCellValue().replace("<<C4>>", paginaNotas.getMateriasCentralizador()[3].getCodigo()));
                                        } else {
                                            cell.setCellValue(cell.getStringCellValue().replace("<<C4>>", ""));
                                        }
                                    } else if (cell.getStringCellValue().contains("<<C5>>")) {
                                        if (paginaNotas.getMateriasCentralizador()[4] != null) {
                                            cell.setCellValue(cell.getStringCellValue().replace("<<C5>>", paginaNotas.getMateriasCentralizador()[4].getCodigo()));
                                        } else {
                                            cell.setCellValue(cell.getStringCellValue().replace("<<C5>>", ""));
                                        }
                                    } else if (cell.getStringCellValue().contains("<<C6>>")) {
                                        if (paginaNotas.getMateriasCentralizador()[5] != null) {
                                            cell.setCellValue(cell.getStringCellValue().replace("<<C6>>", paginaNotas.getMateriasCentralizador()[5].getCodigo()));
                                        } else {
                                            cell.setCellValue(cell.getStringCellValue().replace("<<C6>>", ""));
                                        }
                                    } else if (cell.getStringCellValue().contains("<<C7>>")) {
                                        if (paginaNotas.getMateriasCentralizador()[6] != null) {
                                            cell.setCellValue(cell.getStringCellValue().replace("<<C7>>", paginaNotas.getMateriasCentralizador()[6].getCodigo()));
                                        } else {
                                            cell.setCellValue(cell.getStringCellValue().replace("<<C7>>", ""));
                                        }
                                    } else if (cell.getStringCellValue().contains("<<C8>>")) {
                                        if (paginaNotas.getMateriasCentralizador()[7] != null) {
                                            cell.setCellValue(cell.getStringCellValue().replace("<<C8>>", paginaNotas.getMateriasCentralizador()[7].getCodigo()));
                                        } else {
                                            cell.setCellValue(cell.getStringCellValue().replace("<<C8>>", ""));
                                        }
                                    } else if (cell.getStringCellValue().contains("<<C9>>")) {
                                        if (paginaNotas.getMateriasCentralizador()[8] != null) {
                                            cell.setCellValue(cell.getStringCellValue().replace("<<C9>>", paginaNotas.getMateriasCentralizador()[8].getCodigo()));
                                        } else {
                                            cell.setCellValue(cell.getStringCellValue().replace("<<C9>>", ""));
                                        }
                                    } else if (cell.getStringCellValue().contains("<<C10>>")) {
                                        if (paginaNotas.getMateriasCentralizador()[9] != null) {
                                            cell.setCellValue(cell.getStringCellValue().replace("<<C10>>", paginaNotas.getMateriasCentralizador()[9].getCodigo()));
                                        } else {
                                            cell.setCellValue(cell.getStringCellValue().replace("<<C10>>", ""));
                                        }
                                    } else if (cell.getStringCellValue().contains("<<M1>>")) {
                                        if (paginaNotas.getMateriasCentralizador()[0] != null) {
                                            cell.setCellValue(cell.getStringCellValue().replace("<<M1>>", paginaNotas.getMateriasCentralizador()[0].getNombre()));
                                        } else {
                                            cell.setCellValue(cell.getStringCellValue().replace("<<M1>>", ""));
                                        }
                                    } else if (cell.getStringCellValue().contains("<<M2>>")) {
                                        if (paginaNotas.getMateriasCentralizador()[1] != null) {
                                            cell.setCellValue(cell.getStringCellValue().replace("<<M2>>", paginaNotas.getMateriasCentralizador()[1].getNombre()));
                                        } else {
                                            cell.setCellValue(cell.getStringCellValue().replace("<<M2>>", ""));
                                        }
                                    } else if (cell.getStringCellValue().contains("<<M3>>")) {
                                        if (paginaNotas.getMateriasCentralizador()[2] != null) {
                                            cell.setCellValue(cell.getStringCellValue().replace("<<M3>>", paginaNotas.getMateriasCentralizador()[2].getNombre()));
                                        } else {
                                            cell.setCellValue(cell.getStringCellValue().replace("<<M3>>", ""));
                                        }
                                    } else if (cell.getStringCellValue().contains("<<M4>>")) {
                                        if (paginaNotas.getMateriasCentralizador()[3] != null) {
                                            cell.setCellValue(cell.getStringCellValue().replace("<<M4>>", paginaNotas.getMateriasCentralizador()[3].getNombre()));
                                        } else {
                                            cell.setCellValue(cell.getStringCellValue().replace("<<M4>>", ""));
                                        }
                                    } else if (cell.getStringCellValue().contains("<<M5>>")) {
                                        if (paginaNotas.getMateriasCentralizador()[4] != null) {
                                            cell.setCellValue(cell.getStringCellValue().replace("<<M5>>", paginaNotas.getMateriasCentralizador()[4].getNombre()));
                                        } else {
                                            cell.setCellValue(cell.getStringCellValue().replace("<<M5>>", ""));
                                        }
                                    } else if (cell.getStringCellValue().contains("<<M6>>")) {
                                        if (paginaNotas.getMateriasCentralizador()[5] != null) {
                                            cell.setCellValue(cell.getStringCellValue().replace("<<M6>>", paginaNotas.getMateriasCentralizador()[5].getNombre()));
                                        } else {
                                            cell.setCellValue(cell.getStringCellValue().replace("<<M6>>", ""));
                                        }
                                    } else if (cell.getStringCellValue().contains("<<M7>>")) {
                                        if (paginaNotas.getMateriasCentralizador()[6] != null) {
                                            cell.setCellValue(cell.getStringCellValue().replace("<<M7>>", paginaNotas.getMateriasCentralizador()[6].getNombre()));
                                        } else {
                                            cell.setCellValue(cell.getStringCellValue().replace("<<M7>>", ""));
                                        }
                                    } else if (cell.getStringCellValue().contains("<<M8>>")) {
                                        if (paginaNotas.getMateriasCentralizador()[7] != null) {
                                            cell.setCellValue(cell.getStringCellValue().replace("<<M8>>", paginaNotas.getMateriasCentralizador()[7].getNombre()));
                                        } else {
                                            cell.setCellValue(cell.getStringCellValue().replace("<<M8>>", ""));
                                        }
                                    } else if (cell.getStringCellValue().contains("<<M9>>")) {
                                        if (paginaNotas.getMateriasCentralizador()[8] != null) {
                                            cell.setCellValue(cell.getStringCellValue().replace("<<M9>>", paginaNotas.getMateriasCentralizador()[8].getNombre()));
                                        } else {
                                            cell.setCellValue(cell.getStringCellValue().replace("<<M9>>", ""));
                                        }
                                    } else if (cell.getStringCellValue().contains("<<M10>>")) {
                                        if (paginaNotas.getMateriasCentralizador()[9] != null) {
                                            cell.setCellValue(cell.getStringCellValue().replace("<<M10>>", paginaNotas.getMateriasCentralizador()[9].getNombre()));
                                        } else {
                                            cell.setCellValue(cell.getStringCellValue().replace("<<M10>>", ""));
                                        }
                                    } else if (cell.getStringCellValue().contains("<<ESTUDIANTE>>")) {
                                        rowNum = row.getRowNum();
                                    } else if (cell.getStringCellValue().contains("<<*>>")) {
                                        cell.setCellValue(cell.getStringCellValue().replace("<<*>>", paginaNotas.getNota()));
                                    }
                                } else if (cell.getCellTypeEnum() == CellType.NUMERIC) {
                                    if (cell.getNumericCellValue() == -1) {
                                        cell.setCellValue(paginaNotas.getNumeroLibro());
                                    } else if (cell.getNumericCellValue() == -2) {
                                        cell.setCellValue(paginaNotas.getNumeroFolio());
                                    }
                                }
                            }
                        }

                        EstudianteCentralizador[] estudiantesCentralizador = paginaNotas.getEstudiantesCentralizador();

                        sheet.shiftRows(rowNum + 1, sheet.getLastRowNum(), estudiantesCentralizador.length - 1, true, true);

                        CellCopyPolicy cellCopyPolicy = new CellCopyPolicy.Builder().cellStyle(true).build();
                        for (int i = 0; i < estudiantesCentralizador.length - 1; i++) {
                            sheet.copyRows(rowNum, rowNum, rowNum + i + 1, cellCopyPolicy);
                        }

                        for (int i = 0; i < estudiantesCentralizador.length; i++) {
                            EstudianteCentralizador estudianteCentralizador = estudiantesCentralizador[i];
                            String[] notas = estudianteCentralizador.getNotas();

                            XSSFRow row = sheet.getRow(rowNum + i);
                            Iterator<Cell> cellIterator = row.cellIterator();

                            while (cellIterator.hasNext()) {
                                Cell cell = cellIterator.next();

                                if (cell.getCellTypeEnum() == CellType.NUMERIC) {
                                    if (cell.getNumericCellValue() == -3) {
                                        if (!estudianteCentralizador.getNumero().equals(" ")) {
                                            cell.setCellValue(Integer.parseInt(estudianteCentralizador.getNumero()));
                                        } else {
                                            cell.setCellValue(estudianteCentralizador.getNumero());
                                        }
                                    } else if (cell.getNumericCellValue() == -10) {
                                        if (notas[0] != null && !notas[0].isEmpty() && !notas[0].equals(" ") && !notas[0].equals("N/P") && !notas[0].equals("-")) {
                                            cell.setCellValue(Integer.parseInt(notas[0]));
                                        } else {
                                            cell.setCellValue(notas[0]);
                                        }
                                    } else if (cell.getNumericCellValue() == -20) {
                                        if (notas[1] != null && !notas[1].isEmpty() && !notas[1].equals(" ") && !notas[1].equals("N/P") && !notas[1].equals("-")) {
                                            cell.setCellValue(Integer.parseInt(notas[1]));
                                        } else {
                                            cell.setCellValue(notas[1]);
                                        }
                                    } else if (cell.getNumericCellValue() == -30) {
                                        if (notas[2] != null && !notas[2].isEmpty() && !notas[2].equals(" ") && !notas[2].equals("N/P") && !notas[2].equals("-")) {
                                            cell.setCellValue(Integer.parseInt(notas[2]));
                                        } else {
                                            cell.setCellValue(notas[2]);
                                        }
                                    } else if (cell.getNumericCellValue() == -40) {
                                        if (notas[3] != null && !notas[3].isEmpty() && !notas[3].equals(" ") && !notas[3].equals("N/P") && !notas[3].equals("-")) {
                                            cell.setCellValue(Integer.parseInt(notas[3]));
                                        } else {
                                            cell.setCellValue(notas[3]);
                                        }
                                    } else if (cell.getNumericCellValue() == -50) {
                                        if (notas[4] != null && !notas[4].isEmpty() && !notas[4].equals(" ") && !notas[4].equals("N/P") && !notas[4].equals("-")) {
                                            cell.setCellValue(Integer.parseInt(notas[4]));
                                        } else {
                                            cell.setCellValue(notas[4]);
                                        }
                                    } else if (cell.getNumericCellValue() == -60) {
                                        if (notas[5] != null && !notas[5].isEmpty() && !notas[5].equals(" ") && !notas[5].equals("N/P") && !notas[5].equals("-")) {
                                            cell.setCellValue(Integer.parseInt(notas[5]));
                                        } else {
                                            cell.setCellValue(notas[5]);
                                        }
                                    } else if (cell.getNumericCellValue() == -70) {
                                        if (notas[6] != null && !notas[6].isEmpty() && !notas[6].equals(" ") && !notas[6].equals("N/P") && !notas[6].equals("-")) {
                                            cell.setCellValue(Integer.parseInt(notas[6]));
                                        } else {
                                            cell.setCellValue(notas[6]);
                                        }
                                    } else if (cell.getNumericCellValue() == -80) {
                                        if (notas[7] != null && !notas[7].isEmpty() && !notas[7].equals(" ") && !notas[7].equals("N/P") && !notas[7].equals("-")) {
                                            cell.setCellValue(Integer.parseInt(notas[7]));
                                        } else {
                                            cell.setCellValue(notas[7]);
                                        }
                                    } else if (cell.getNumericCellValue() == -90) {
                                        if (notas[8] != null && !notas[8].isEmpty() && !notas[8].equals(" ") && !notas[8].equals("N/P") && !notas[8].equals("-")) {
                                            cell.setCellValue(Integer.parseInt(notas[8]));
                                        } else {
                                            cell.setCellValue(notas[8]);
                                        }
                                    } else if (cell.getNumericCellValue() == -100) {
                                        if (notas[9] != null && !notas[9].isEmpty() && !notas[9].equals(" ") && !notas[9].equals("N/P") && !notas[9].equals("-")) {
                                            cell.setCellValue(Integer.parseInt(notas[9]));
                                        } else {
                                            cell.setCellValue(notas[9]);
                                        }
                                    }
                                } else if (cell.getCellTypeEnum() == CellType.STRING) {
                                    if (cell.getStringCellValue().contains("<<ESTUDIANTE>>")) {
                                        cell.setCellValue(cell.getStringCellValue().replace("<<ESTUDIANTE>>", estudianteCentralizador.getNombre()));
                                    } else if (cell.getStringCellValue().contains("<<CI>>")) {
                                        cell.setCellValue(cell.getStringCellValue().replace("<<CI>>", estudianteCentralizador.getCi()));
                                    } else if (cell.getStringCellValue().contains("<<CONDICION>>")) {
                                        cell.setCellValue(cell.getStringCellValue().replace("<<CONDICION>>", estudianteCentralizador.getObservaciones()));
                                    }
                                }
                            }
                        }

                    }
                } else if (paginaCentralizador instanceof PaginaEstadisticas) {
                    PaginaEstadisticas paginaEstadisticas = (PaginaEstadisticas) paginaCentralizador;

                    XSSFSheet sheetEstadisticas = workbook.getSheetAt(1); //estadisticas
                    if (sheetEstadisticas != null) {
                        XSSFSheet sheet = workbook.cloneSheet(1, sheetName + " E");

                        sheet.setFitToPage(true);
                        sheet.getPrintSetup().setOrientation(PrintOrientation.LANDSCAPE);

                        Iterator<Row> rowIterator = sheet.rowIterator();
                        while (rowIterator.hasNext()) {
                            Row row = rowIterator.next();

                            Iterator<Cell> cellIterator = row.cellIterator();
                            while (cellIterator.hasNext()) {
                                Cell cell = cellIterator.next();

                                if (cell.getCellTypeEnum() == CellType.NUMERIC) {
                                    if (cell.getNumericCellValue() == -10) {
                                        cell.setCellValue(paginaEstadisticas.getCantidadInscritos());
                                    } else if (cell.getNumericCellValue() == -0.2) {
                                        cell.setCellValue(paginaEstadisticas.getPorcentajeInscritos() / 100.0);
                                    } else if (cell.getNumericCellValue() == -30) {
                                        cell.setCellValue(paginaEstadisticas.getCantidadAprobados());
                                    } else if (cell.getNumericCellValue() == -0.4) {
                                        cell.setCellValue(paginaEstadisticas.getPorcentajeAprobados() / 100.0);
                                    } else if (cell.getNumericCellValue() == -50) {
                                        cell.setCellValue(paginaEstadisticas.getCantidadReprobados());
                                    } else if (cell.getNumericCellValue() == -0.6) {
                                        cell.setCellValue(paginaEstadisticas.getPorcentajeReprobados() / 100.0);
                                    } else if (cell.getNumericCellValue() == -70) {
                                        cell.setCellValue(paginaEstadisticas.getCantidadNoSePresento());
                                    } else if (cell.getNumericCellValue() == -0.8) {
                                        cell.setCellValue(paginaEstadisticas.getPorcentajeNoSePresento() / 100.0);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        workbook.removeSheetAt(0);
        workbook.removeSheetAt(0);

        String name = seleccionGestionAcademica.toString() + " - " + seleccionCarrera.getNombre();
        descargarArchivo(workbook, name);

        //log
        logFacade.create(new Log(Fecha.getDate(), EventoLog.READ, "Generación reporte centralizador de calificaciones en formato XLSX", loginController.getUsr().toString()));
    }

    public void toReporteCentralizadorCalificacionesHomologacion() throws IOException {
        reinit();

        this.redireccionarViewId("/reportes/centralizadores/centralizadorCalificacionesHomologacion/reporteCentralizadorCalificacionesHomologacion");
    }

    public void toCentralizadorCalificacionesHomologacion() throws IOException {
        this.redireccionarViewId("/reportes/centralizadores/centralizadorCalificacionesHomologacion/centralizadorCalificacionesHomologacion");
    }

    /**
     * @return the seleccionGestionAcademica
     */
    public GestionAcademica getSeleccionGestionAcademica() {
        return seleccionGestionAcademica;
    }

    /**
     * @param seleccionGestionAcademica the seleccionGestionAcademica to set
     */
    public void setSeleccionGestionAcademica(GestionAcademica seleccionGestionAcademica) {
        this.seleccionGestionAcademica = seleccionGestionAcademica;
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
     * @return the numeroLibro
     */
    public Integer getNumeroLibro() {
        return numeroLibro;
    }

    /**
     * @param numeroLibro the numeroLibro to set
     */
    public void setNumeroLibro(Integer numeroLibro) {
        this.numeroLibro = numeroLibro;
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
     * @return the numeroFolio
     */
    public Integer getNumeroFolio() {
        return numeroFolio;
    }

    /**
     * @param numeroFolio the numeroFolio to set
     */
    public void setNumeroFolio(Integer numeroFolio) {
        this.numeroFolio = numeroFolio;
    }

}
