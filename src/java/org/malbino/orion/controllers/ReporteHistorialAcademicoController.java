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
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellCopyPolicy;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.malbino.orion.entities.Carrera;
import org.malbino.orion.entities.CarreraEstudiante;
import org.malbino.orion.entities.Estudiante;
import org.malbino.orion.entities.GestionAcademica;
import org.malbino.orion.entities.Inscrito;
import org.malbino.orion.entities.Log;
import org.malbino.orion.entities.Nota;
import org.malbino.orion.enums.EventoLog;
import org.malbino.orion.facades.CarreraEstudianteFacade;
import org.malbino.orion.facades.InscritoFacade;
import org.malbino.orion.facades.MateriaFacade;
import org.malbino.orion.facades.NotaFacade;
import org.malbino.orion.util.Fecha;
import org.malbino.orion.util.Redondeo;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author Tincho
 */
@Named("ReporteHistorialAcademicoController")
@SessionScoped
public class ReporteHistorialAcademicoController extends AbstractController implements Serializable {

    private static final String PATHNAME = File.separator + "resources" + File.separator + "uploads" + File.separator + "historial_academico.xlsx";

    @EJB
    NotaFacade notaFacade;
    @EJB
    InscritoFacade inscritoFacade;
    @EJB
    MateriaFacade materiaFacade;
    @EJB
    CarreraEstudianteFacade carreraEstudianteFacade;
    @Inject
    LoginController loginController;

    private Estudiante seleccionEstudiante;
    private CarreraEstudiante seleccionCarreraEstudiante;
    private Date fecha;

    private StreamedContent download;

    @PostConstruct
    public void init() {
        seleccionEstudiante = null;
        seleccionCarreraEstudiante = null;
        fecha = null;
    }

    public void reinit() {
        seleccionEstudiante = null;
        seleccionCarreraEstudiante = null;
        fecha = null;
    }

    public List<CarreraEstudiante> listaCarrerasEstudiante() {
        List<CarreraEstudiante> l = new ArrayList();
        if (seleccionEstudiante != null) {
            l = carreraEstudianteFacade.listaCarrerasEstudiante(seleccionEstudiante.getId_persona());
        }
        return l;
    }

    public void generarPDF() throws IOException {
        if (seleccionCarreraEstudiante != null && fecha != null) {
            this.insertarParametro("carreraEstudiante", seleccionCarreraEstudiante);
            this.insertarParametro("fecha", fecha);

            toHistorialAcademico();

            //log
            logFacade.create(new Log(Fecha.getDate(), EventoLog.READ, "Generación reporte historial académico en formato PDF", loginController.getUsr().toString()));
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
        Carrera carrera = carreraFacade.find(seleccionCarreraEstudiante.getCarreraEstudianteId().getId_carrera());

        XSSFWorkbook workbook = leerArchivo(PATHNAME);

        XSSFSheet sheet = workbook.getSheetAt(0);
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
                            cell.setCellValue(cell.getStringCellValue().replace("<<INSTITUTO>>", carrera.getCampus().getInstituto().getNombreRegulador()));
                        } else if (cell.getStringCellValue().contains("<<CI>>")) {
                            cell.setCellValue(cell.getStringCellValue().replace("<<CI>>", seleccionEstudiante.dniLugar()));
                        } else if (cell.getStringCellValue().contains("<<ESTUDIANTE>>")) {
                            cell.setCellValue(cell.getStringCellValue().replace("<<ESTUDIANTE>>", seleccionEstudiante.toString()));
                        } else if (cell.getStringCellValue().contains("<<ADMICION>>")) {
                            GestionAcademica inicioFormacion = notaFacade.inicioFormacion(carrera, seleccionCarreraEstudiante.getMencion(), seleccionEstudiante);
                            if (inicioFormacion != null) {
                                cell.setCellValue(cell.getStringCellValue().replace("<<ADMICION>>", Fecha.formatearFecha_MMyyyy(inicioFormacion.getInicio())));
                            } else {
                                cell.setCellValue(cell.getStringCellValue().replace("<<ADMICION>>", " "));
                            }
                        } else if (cell.getStringCellValue().contains("<<CARRERA>>")) {
                            cell.setCellValue(cell.getStringCellValue().replace("<<CARRERA>>", carrera.getNombre()));
                        } else if (cell.getStringCellValue().contains("<<CONCLUSION>>")) {
                            GestionAcademica finFormacion = notaFacade.finFormacion(carrera, seleccionCarreraEstudiante.getMencion(), seleccionEstudiante);
                            if (finFormacion != null) {
                                cell.setCellValue(cell.getStringCellValue().replace("<<CONCLUSION>>", Fecha.formatearFecha_MMyyyy(finFormacion.getInicio())));
                            } else {
                                cell.setCellValue(cell.getStringCellValue().replace("<<CONCLUSION>>", " "));
                            }
                        } else if (cell.getStringCellValue().contains("<<MENCION>>")) {
                            if (seleccionCarreraEstudiante.getMencion() != null) {
                                cell.setCellValue(cell.getStringCellValue().replace("<<MENCION>>", seleccionCarreraEstudiante.getMencion().getNombre()));
                            } else {
                                cell.setCellValue(cell.getStringCellValue().replace("<<MENCION>>", " "));
                            }
                        } else if (cell.getStringCellValue().contains("<<NIVEL_ACADEMICO>>")) {
                            cell.setCellValue(cell.getStringCellValue().replace("<<NIVEL_ACADEMICO>>", carrera.getNivelAcademico().getNombre()));
                        } else if (cell.getStringCellValue().contains("<<REGIMEN>>")) {
                            cell.setCellValue(cell.getStringCellValue().replace("<<REGIMEN>>", carrera.getRegimen().getNombre()));
                        } else if (cell.getStringCellValue().contains("<<LUGAR_FECHA>>")) {
                            cell.setCellValue(cell.getStringCellValue().replace("<<LUGAR_FECHA>>", "Cochabamba, " + Fecha.formatearFecha_ddMMMMyyyy(fecha)));
                        } else if (cell.getStringCellValue().contains("<<RM_1>>")) {
                            cell.setCellValue(cell.getStringCellValue().replace("<<RM_1>>", "* PLAN DE ESTUDIOS SEGÚN R.M. " + carrera.getResolucionMinisterial1()));
                        } else if (cell.getStringCellValue().contains("<<RM_2>>")) {
                            if (carrera.getResolucionMinisterial2() != null && !carrera.getResolucionMinisterial2().isEmpty()) {
                                cell.setCellValue(cell.getStringCellValue().replace("<<RM_2>>", "* PLAN DE ESTUDIOS SEGÚN R.M. " + carrera.getResolucionMinisterial2()));
                            } else {
                                cell.setCellValue(cell.getStringCellValue().replace("<<RM_2>>", " "));
                            }
                        } else if (cell.getStringCellValue().contains("<<RM_3>>")) {
                            if (carrera.getResolucionMinisterial3() != null && !carrera.getResolucionMinisterial3().isEmpty()) {
                                cell.setCellValue(cell.getStringCellValue().replace("<<RM_3>>", "* PLAN DE ESTUDIOS SEGÚN R.M. " + carrera.getResolucionMinisterial3()));
                            } else {
                                cell.setCellValue(cell.getStringCellValue().replace("<<RM_3>>", " "));
                            }
                        } else if (cell.getStringCellValue().contains("<<MA_MC>>")) {
                            int materiasAprobadas = notaFacade.cantidadNotasAprobadas(carrera, seleccionCarreraEstudiante.getMencion(), seleccionEstudiante).intValue();
                            int materiasCarrera = materiaFacade.cantidadMateriasCurriculares(carrera, seleccionCarreraEstudiante.getMencion()).intValue();

                            cell.setCellValue(cell.getStringCellValue().replace("<<MA_MC>>", materiasAprobadas + "/" + materiasCarrera));
                        } else if (cell.getStringCellValue().contains("<<GA>>")) {
                            rowNum = row.getRowNum();
                        }
                    } else if (cell.getCellTypeEnum() == CellType.NUMERIC) {
                        if (cell.getNumericCellValue() == -1) {
                            GestionAcademica finFormacion = notaFacade.finFormacion(carrera, seleccionCarreraEstudiante.getMencion(), seleccionEstudiante);
                            if (finFormacion != null) {
                                Inscrito inscrito = inscritoFacade.buscarInscrito(seleccionEstudiante.getId_persona(), carrera.getId_carrera(), finFormacion.getId_gestionacademica());
                                if (inscrito != null) {
                                    cell.setCellValue(inscrito.getCodigo());
                                } else {
                                    cell.setCellValue(" ");
                                }
                            } else {
                                cell.setCellValue(" ");
                            }
                        } else if (cell.getNumericCellValue() == -2) {
                            if (seleccionEstudiante.getMatricula() != null) {
                                cell.setCellValue(seleccionEstudiante.getMatricula());
                            } else {
                                cell.setCellValue(" ");
                            }
                        } else if (cell.getNumericCellValue() == -4) {
                            Double promedioGeneral = notaFacade.promedioReporteHistorialAcademico(seleccionEstudiante, carrera, seleccionCarreraEstudiante.getMencion());
                            if (promedioGeneral != null) {
                                int promedioGeneralRedondeado = Redondeo.redondear_HALFUP(promedioGeneral, 0).intValue();
                                cell.setCellValue(promedioGeneralRedondeado);
                            } else {
                                cell.setCellValue(" ");
                            }
                        }
                    }
                }
            }

            List<Nota> historialAcademico = notaFacade.reporteHistorialAcademico(seleccionEstudiante, carrera, seleccionCarreraEstudiante.getMencion());

            sheet.shiftRows(rowNum + 1, sheet.getLastRowNum(), historialAcademico.size() - 1, true, true);

            CellCopyPolicy cellCopyPolicy = new CellCopyPolicy.Builder().cellStyle(true).build();
            for (int i = 0; i < historialAcademico.size() - 1; i++) {
                sheet.copyRows(rowNum, rowNum, rowNum + i + 1, cellCopyPolicy);
            }

            for (int i = 0; i < historialAcademico.size(); i++) {
                Nota nota = historialAcademico.get(i);

                XSSFRow row = sheet.getRow(rowNum + i);
                Iterator<Cell> cellIterator = row.cellIterator();

                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();

                    if (cell.getCellTypeEnum() == CellType.NUMERIC) {
                        /*
                        if (cell.getNumericCellValue() == -3) {
                            cell.setCellValue(i + 1);
                        } else if (cell.getNumericCellValue() == -50) {
                            if (nota.getNotaFinal() != null) {
                                cell.setCellValue(nota.getNotaFinal());
                            } else {
                                cell.setCellValue("");
                            }
                        } else if (cell.getNumericCellValue() == -51) {
                            if (nota.getRecuperatorio() != null) {
                                cell.setCellValue(nota.getRecuperatorio());
                            } else {
                                cell.setCellValue("");
                            }
                        }
                         */

                        if (nota.getRecuperatorio() == null) {
                            if (cell.getNumericCellValue() == -3) {
                                cell.setCellValue(i + 1);
                            } else if (cell.getNumericCellValue() == -50) {
                                if (nota.getNotaFinal() != null) {
                                    cell.setCellValue(nota.getNotaFinal());
                                } else {
                                    cell.setCellValue("");
                                }
                            } else if (cell.getNumericCellValue() == -51) {
                                if (nota.getRecuperatorio() != null) {
                                    cell.setCellValue(nota.getRecuperatorio());
                                } else {
                                    cell.setCellValue("");
                                }
                            }
                        } else {
                            if (cell.getNumericCellValue() == -3) {
                                cell.setCellValue(i + 1);
                            } else if (cell.getNumericCellValue() == -50) {
                                if (nota.getNotaFinal() != null) {
                                    cell.setCellValue(nota.getRecuperatorio());
                                } else {
                                    cell.setCellValue("");
                                }
                            } else if (cell.getNumericCellValue() == -51) {
                                if (nota.getRecuperatorio() != null) {
                                    cell.setCellValue(nota.getNotaFinal());
                                } else {
                                    cell.setCellValue("");
                                }
                            }
                        }
                    } else if (cell.getCellTypeEnum() == CellType.STRING) {
                        if (cell.getStringCellValue().contains("<<LIBRO>>")) {
                            if (nota.getNumeroLibro() != null) {
                                cell.setCellValue(cell.getStringCellValue().replace("<<LIBRO>>", nota.getNumeroLibro().toString()));
                            } else {
                                cell.setCellValue(cell.getStringCellValue().replace("<<LIBRO>>", " "));
                            }
                        } else if (cell.getStringCellValue().contains("<<FOLIO>>")) {
                            if (nota.getNumeroFolio() != null) {
                                cell.setCellValue(cell.getStringCellValue().replace("<<FOLIO>>", nota.getNumeroFolio().toString()));
                            } else {
                                cell.setCellValue(cell.getStringCellValue().replace("<<FOLIO>>", " "));
                            }
                        } else if (cell.getStringCellValue().contains("<<GA>>")) {
                            cell.setCellValue(cell.getStringCellValue().replace("<<GA>>", nota.getGestionAcademica().codigo()));
                        } else if (cell.getStringCellValue().contains("<<NIVEL>>")) {
                            cell.setCellValue(cell.getStringCellValue().replace("<<NIVEL>>", nota.getMateria().getNivel().getOrdinal()));
                        } else if (cell.getStringCellValue().contains("<<CODIGO>>")) {
                            cell.setCellValue(cell.getStringCellValue().replace("<<CODIGO>>", nota.getMateria().getCodigo()));
                        } else if (cell.getStringCellValue().contains("<<MATERIA>>")) {
                            cell.setCellValue(cell.getStringCellValue().replace("<<MATERIA>>", nota.getMateria().getNombre()));
                        } else if (cell.getStringCellValue().contains("<<COD_PRE>>")) {
                            cell.setCellValue(cell.getStringCellValue().replace("<<COD_PRE>>", nota.getMateria().prerequisitosToString()));
                        } else if (cell.getStringCellValue().contains("<<CONDICION>>")) {
                            cell.setCellValue(cell.getStringCellValue().replace("<<CONDICION>>", nota.getCondicion().getNombre()));
                        }
                    }
                }
            }

        }

        String name = seleccionEstudiante.toString() + " - " + carrera.getNombre();
        descargarArchivo(workbook, name);

        //log
        logFacade.create(new Log(Fecha.getDate(), EventoLog.READ, "Generación reporte historial académico en formato XLSX", loginController.getUsr().toString()));
    }

    public void toReporteHistorialAcademico() throws IOException {
        reinit();

        this.redireccionarViewId("/reportes/historialAcademico/reporteHistorialAcademico");
    }

    public void toHistorialAcademico() throws IOException {
        this.redireccionarViewId("/reportes/historialAcademico/historialAcademico");
    }

    /**
     * @return the seleccionEstudiante
     */
    public Estudiante getSeleccionEstudiante() {
        return seleccionEstudiante;
    }

    /**
     * @param seleccionEstudiante the seleccionEstudiante to set
     */
    public void setSeleccionEstudiante(Estudiante seleccionEstudiante) {
        this.seleccionEstudiante = seleccionEstudiante;
    }

    /**
     * @return the seleccionCarreraEstudiante
     */
    public CarreraEstudiante getSeleccionCarreraEstudiante() {
        return seleccionCarreraEstudiante;
    }

    /**
     * @param seleccionCarreraEstudiante the seleccionCarreraEstudiante to set
     */
    public void setSeleccionCarreraEstudiante(CarreraEstudiante seleccionCarreraEstudiante) {
        this.seleccionCarreraEstudiante = seleccionCarreraEstudiante;
    }

    /**
     * @return the fecha
     */
    public Date getFecha() {
        return fecha;
    }

    /**
     * @param fecha the fecha to set
     */
    public void setFecha(Date fecha) {
        this.fecha = fecha;
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

}
