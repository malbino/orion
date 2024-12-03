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
import org.malbino.orion.entities.Nota;
import org.malbino.orion.enums.EventoLog;
import org.malbino.orion.enums.Regimen;
import org.malbino.orion.facades.NotaFacade;
import org.malbino.orion.facades.negocio.CentralizadorCalificacionesFacade;
import org.malbino.orion.pojos.centralizador.Centralizador;
import org.malbino.orion.pojos.centralizador.EstudianteCentralizador;
import org.malbino.orion.pojos.centralizador.GrupoCentralizador;
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
@Named("ReporteCentralizadorCalificacionesController")
@SessionScoped
public class ReporteCentralizadorCalificacionesController extends AbstractController implements Serializable {

    private static final String PATHNAME = File.separator + "resources" + File.separator + "uploads" + File.separator + "centralizador.xlsx";

    private static final String TITULO_CC = "CENTRALIZADOR DE CALIFICACIONES";

    @EJB
    CentralizadorCalificacionesFacade centralizadorCalificacionesFacade;
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

            List<Nota> notasFaltantes = new ArrayList<>();
            if (seleccionCarrera.getRegimen().equals(Regimen.SEMESTRAL)) {
                notasFaltantes = notaFacade.listaNotasFaltantesSemestral(seleccionGestionAcademica, seleccionCarrera.getId_carrera());
            }
            if (seleccionCarrera.getRegimen().equals(Regimen.ANUAL)) {
                notasFaltantes = notaFacade.listaNotasFaltantesAnual(seleccionGestionAcademica.getId_gestionacademica(), seleccionCarrera.getId_carrera());
            }

            if (notasFaltantes.isEmpty()) {
                //generamos el centralizador
                Centralizador centralizador = centralizadorCalificacionesFacade.centralizadorCalificaciones(seleccionGestionAcademica, seleccionCarrera, numeroLibro, numeroFolio);
                this.insertarParametro("centralizador", centralizador);

                toCentralizadorCalificaciones();

                //log
                logFacade.create(new Log(Fecha.getDate(), EventoLog.READ, "Generación reporte centralizador de calificaciones en formato PDF", loginController.getUsr().toString()));
            } else {
                this.mensajeDeError("¡Error al generar el centralizador!");
                this.mensajeDeError("Notas faltantes: " + notasFaltantes.size());
            }

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
        List<Nota> notasFaltantes = new ArrayList<>();
        if (seleccionCarrera.getRegimen().equals(Regimen.SEMESTRAL)) {
            notasFaltantes = notaFacade.listaNotasFaltantesSemestral(seleccionGestionAcademica, seleccionCarrera.getId_carrera());
        }
        if (seleccionCarrera.getRegimen().equals(Regimen.ANUAL)) {
            notasFaltantes = notaFacade.listaNotasFaltantesAnual(seleccionGestionAcademica.getId_gestionacademica(), seleccionCarrera.getId_carrera());
        }

        if (notasFaltantes.isEmpty()) {
            XSSFWorkbook workbook = leerArchivo(PATHNAME);

            //generamos el centralizador
            Centralizador centralizador = centralizadorCalificacionesFacade.centralizadorCalificaciones(seleccionGestionAcademica, seleccionCarrera, numeroLibro, numeroFolio);

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
                                            if (paginaNotas.getTitulo().equals(TITULO_CC)) {
                                                cell.setCellValue(cell.getStringCellValue().replace("<<*>>", "* N/P = No se Presento\nCuando el estudiante no se hubiera presentado a la asignatura"));
                                            } else {
                                                cell.setCellValue(cell.getStringCellValue().replace("<<*>>", ""));
                                            }
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

                                    if (cell.getCellTypeEnum() == CellType.STRING) {
                                        if (cell.getStringCellValue().contains("<<N>>")) {
                                            cell.setCellValue(cell.getStringCellValue().replace("<<N>>", estudianteCentralizador.getNumero()));
                                        } else if (cell.getStringCellValue().contains("<<ESTUDIANTE>>")) {
                                            cell.setCellValue(cell.getStringCellValue().replace("<<ESTUDIANTE>>", estudianteCentralizador.getNombre()));
                                        } else if (cell.getStringCellValue().contains("<<CI>>")) {
                                            cell.setCellValue(cell.getStringCellValue().replace("<<CI>>", estudianteCentralizador.getCi()));
                                        } else if (cell.getStringCellValue().contains("<<A>>")) {
                                            if (notas[0] != null) {
                                                cell.setCellValue(notas[0]);
                                            } else {
                                                cell.setCellValue("");
                                            }
                                        } else if (cell.getStringCellValue().contains("<<B>>")) {
                                            if (notas[1] != null) {
                                                cell.setCellValue(notas[1]);
                                            } else {
                                                cell.setCellValue("");
                                            }
                                        } else if (cell.getStringCellValue().contains("<<C>>")) {
                                            if (notas[2] != null) {
                                                cell.setCellValue(notas[2]);
                                            } else {
                                                cell.setCellValue("");
                                            }
                                        } else if (cell.getStringCellValue().contains("<<D>>")) {
                                            if (notas[3] != null) {
                                                cell.setCellValue(notas[3]);
                                            } else {
                                                cell.setCellValue("");
                                            }
                                        } else if (cell.getStringCellValue().contains("<<E>>")) {
                                            if (notas[4] != null) {
                                                cell.setCellValue(notas[4]);
                                            } else {
                                                cell.setCellValue("");
                                            }
                                        } else if (cell.getStringCellValue().contains("<<F>>")) {
                                            if (notas[5] != null) {
                                                cell.setCellValue(notas[5]);
                                            } else {
                                                cell.setCellValue("");
                                            }
                                        } else if (cell.getStringCellValue().contains("<<G>>")) {
                                            if (notas[6] != null) {
                                                cell.setCellValue(notas[6]);
                                            } else {
                                                cell.setCellValue("");
                                            }
                                        } else if (cell.getStringCellValue().contains("<<H>>")) {
                                            if (notas[7] != null) {
                                                cell.setCellValue(notas[7]);
                                            } else {
                                                cell.setCellValue("");
                                            }
                                        } else if (cell.getStringCellValue().contains("<<I>>")) {
                                            if (notas[8] != null) {
                                                cell.setCellValue(notas[8]);
                                            } else {
                                                cell.setCellValue("");
                                            }
                                        } else if (cell.getStringCellValue().contains("<<J>>")) {
                                            if (notas[9] != null) {
                                                cell.setCellValue(notas[9]);
                                            } else {
                                                cell.setCellValue("");
                                            }
                                        } else if (cell.getStringCellValue().contains("<<CONDICION>>")) {
                                            cell.setCellValue(cell.getStringCellValue().replace("<<CONDICION>>", estudianteCentralizador.getEstado()));
                                        } else if (cell.getStringCellValue().contains("<<OBSERVACION>>")) {
                                            if (estudianteCentralizador.getObservacion() != null) {
                                                cell.setCellValue(estudianteCentralizador.getObservacion());
                                            } else {
                                                cell.setCellValue("");
                                            }
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
                                    GrupoCentralizador[] gruposCentralizador = paginaEstadisticas.getGruposCentralizador();

                                    if (cell.getCellTypeEnum() == CellType.STRING) {
                                        if (cell.getStringCellValue().contains("<<D1>>")) {
                                            cell.setCellValue(gruposCentralizador[0].getDocente());
                                        } else if (cell.getStringCellValue().contains("<<D2>>")) {
                                            cell.setCellValue(gruposCentralizador[1].getDocente());
                                        } else if (cell.getStringCellValue().contains("<<D3>>")) {
                                            cell.setCellValue(gruposCentralizador[2].getDocente());
                                        } else if (cell.getStringCellValue().contains("<<D4>>")) {
                                            cell.setCellValue(gruposCentralizador[3].getDocente());
                                        } else if (cell.getStringCellValue().contains("<<D5>>")) {
                                            cell.setCellValue(gruposCentralizador[4].getDocente());
                                        } else if (cell.getStringCellValue().contains("<<D6>>")) {
                                            cell.setCellValue(gruposCentralizador[5].getDocente());
                                        } else if (cell.getStringCellValue().contains("<<D7>>")) {
                                            cell.setCellValue(gruposCentralizador[6].getDocente());
                                        } else if (cell.getStringCellValue().contains("<<D8>>")) {
                                            cell.setCellValue(gruposCentralizador[7].getDocente());
                                        } else if (cell.getStringCellValue().contains("<<D9>>")) {
                                            cell.setCellValue(gruposCentralizador[8].getDocente());
                                        } else if (cell.getStringCellValue().contains("<<D10>>")) {
                                            cell.setCellValue(gruposCentralizador[9].getDocente());
                                        } else if (cell.getStringCellValue().contains("<<D11>>")) {
                                            cell.setCellValue("");
                                        } else if (cell.getStringCellValue().contains("<<D12>>")) {
                                            cell.setCellValue("");
                                        } else if (cell.getStringCellValue().contains("<<M1>>")) {
                                            cell.setCellValue(gruposCentralizador[0].getMateria());
                                        } else if (cell.getStringCellValue().contains("<<M2>>")) {
                                            cell.setCellValue(gruposCentralizador[1].getMateria());
                                        } else if (cell.getStringCellValue().contains("<<M3>>")) {
                                            cell.setCellValue(gruposCentralizador[2].getMateria());
                                        } else if (cell.getStringCellValue().contains("<<M4>>")) {
                                            cell.setCellValue(gruposCentralizador[3].getMateria());
                                        } else if (cell.getStringCellValue().contains("<<M5>>")) {
                                            cell.setCellValue(gruposCentralizador[4].getMateria());
                                        } else if (cell.getStringCellValue().contains("<<M6>>")) {
                                            cell.setCellValue(gruposCentralizador[5].getMateria());
                                        } else if (cell.getStringCellValue().contains("<<M7>>")) {
                                            cell.setCellValue(gruposCentralizador[6].getMateria());
                                        } else if (cell.getStringCellValue().contains("<<M8>>")) {
                                            cell.setCellValue(gruposCentralizador[7].getMateria());
                                        } else if (cell.getStringCellValue().contains("<<M9>>")) {
                                            cell.setCellValue(gruposCentralizador[8].getMateria());
                                        } else if (cell.getStringCellValue().contains("<<M10>>")) {
                                            cell.setCellValue(gruposCentralizador[9].getMateria());
                                        } else if (cell.getStringCellValue().contains("<<M11>>")) {
                                            cell.setCellValue("");
                                        } else if (cell.getStringCellValue().contains("<<M12>>")) {
                                            cell.setCellValue("");
                                        }
                                    } else if (cell.getCellTypeEnum() == CellType.NUMERIC) {
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
        } else {
            this.mensajeDeError("¡Error al generar el centralizador!");
            this.mensajeDeError("Notas faltantes: " + notasFaltantes.size());
        }
    }

    public void toReporteCentralizadorCalificaciones() throws IOException {
        reinit();

        this.redireccionarViewId("/reportes/centralizadores/centralizadorCalificaciones/reporteCentralizadorCalificaciones");
    }

    public void toCentralizadorCalificaciones() throws IOException {
        this.redireccionarViewId("/reportes/centralizadores/centralizadorCalificaciones/centralizadorCalificaciones");
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
