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
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.malbino.orion.entities.Carrera;
import org.malbino.orion.entities.GestionAcademica;
import org.malbino.orion.entities.Grupo;
import org.malbino.orion.entities.Log;
import org.malbino.orion.entities.Mencion;
import org.malbino.orion.entities.Nota;
import org.malbino.orion.enums.Condicion;
import org.malbino.orion.enums.EventoLog;
import org.malbino.orion.enums.Nivel;
import org.malbino.orion.enums.Turno;
import org.malbino.orion.facades.GrupoFacade;
import org.malbino.orion.facades.MencionFacade;
import org.malbino.orion.facades.NotaFacade;
import org.malbino.orion.util.Fecha;
import org.malbino.orion.util.NumberToLetterConverter;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author Tincho
 */
@Named("ReportePlanillaSeguimientoController")
@SessionScoped
public class ReportePlanillaSeguimientoController extends AbstractController implements Serializable {

    private static final String PATHNAME_SEMESTRAL2P = File.separator + "resources" + File.separator + "uploads" + File.separator + "planilla_seguimiento_semestral2p.xlsx";
    private static final String PATHNAME_SEMESTRAL = File.separator + "resources" + File.separator + "uploads" + File.separator + "planilla_seguimiento_semestral.xlsx";
    private static final String PATHNAME_ANUAL = File.separator + "resources" + File.separator + "uploads" + File.separator + "planilla_seguimiento_anual.xlsx";

    @EJB
    GrupoFacade grupoFacade;
    @EJB
    NotaFacade notaFacade;
    @EJB
    MencionFacade mencionFacade;
    @Inject
    LoginController loginController;

    private GestionAcademica seleccionGestionAcademica;
    private Carrera seleccionCarrera;
    private Mencion seleccionMencion;
    private Nivel seleccionNivel;
    private Turno seleccionTurno;
    private Grupo seleccionGrupo;

    private StreamedContent download;

    @PostConstruct
    public void init() {
        seleccionGestionAcademica = null;
        seleccionCarrera = null;
        seleccionMencion = null;
        seleccionNivel = null;
        seleccionTurno = null;
        seleccionGrupo = null;
    }

    public void reinit() {
        seleccionGestionAcademica = null;
        seleccionCarrera = null;
        seleccionMencion = null;
        seleccionNivel = null;
        seleccionTurno = null;
        seleccionGrupo = null;
    }

    @Override
    public List<Carrera> listaCarreras() {
        List<Carrera> l = new ArrayList();
        if (seleccionGestionAcademica != null) {
            l = carreraFacade.listaCarreras(seleccionGestionAcademica.getRegimen());
        }
        return l;
    }

    public List<Mencion> listaMenciones() {
        List<Mencion> l = new ArrayList<>();
        if (seleccionCarrera != null) {
            l = mencionFacade.listaMenciones(seleccionCarrera.getId_carrera());
        }
        return l;
    }

    public Nivel[] listaNiveles() {
        Nivel[] niveles = new Nivel[0];
        if (seleccionCarrera != null) {
            niveles = Nivel.values(seleccionCarrera.getRegimen());
        }
        return niveles;
    }

    @Override
    public Turno[] listaTurnos() {
        Turno[] turnos = new Turno[0];
        if (seleccionGestionAcademica != null && seleccionCarrera != null && seleccionNivel != null) {
            turnos = Turno.values();
        }
        return turnos;
    }

    public List<Grupo> listaGrupos() {
        List<Grupo> grupos = new ArrayList<>();
        if (seleccionNivel != null) {
            grupos = grupoFacade.listaGrupos(seleccionGestionAcademica.getId_gestionacademica(), seleccionCarrera.getId_carrera(), seleccionMencion, seleccionNivel, seleccionTurno);
        }
        return grupos;
    }

    public XSSFWorkbook leerArchivo(String pathname) {
        XSSFWorkbook workbook = null;

        try (FileInputStream file = new FileInputStream(this.realPath() + pathname)) {
            workbook = new XSSFWorkbook(file);
        } catch (IOException e) {
            this.mensajeDeError("Error: No se pudo leer el archivo.");
        }

        return workbook;
    }

    public void descargarArchivo(XSSFWorkbook workbook, Grupo seleccGrupo) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            workbook.write(baos);
            workbook.close();

            InputStream is = new ByteArrayInputStream(baos.toByteArray());

            download = DefaultStreamedContent.builder()
                    .name(seleccGrupo.toString_RegistroParcial() + ".xlsx")
                    .contentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                    .stream(() -> is)
                    .build();
        } catch (IOException ex) {
            this.mensajeDeError("Error: No se pudo descargar el archivo.");
        }
    }

    public void generarXLSX() {
        XSSFWorkbook workbook = null;

        if (seleccionGestionAcademica.getModalidadEvaluacion().getCantidadParciales() == 2) {
            workbook = leerArchivo(PATHNAME_SEMESTRAL2P);
        } else if (seleccionGestionAcademica.getModalidadEvaluacion().getCantidadParciales() == 3) {
            workbook = leerArchivo(PATHNAME_SEMESTRAL);
        } else if (seleccionGestionAcademica.getModalidadEvaluacion().getCantidadParciales() == 4) {
            workbook = leerArchivo(PATHNAME_ANUAL);
        }

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
                        if (cell.getStringCellValue().contains("<<MATERIA>>")) {
                            cell.setCellValue(cell.getStringCellValue().replace("<<MATERIA>>", seleccionGrupo.getMateria().getNombre()));
                        } else if (cell.getStringCellValue().contains("<<CARRERA>>")) {
                            cell.setCellValue(cell.getStringCellValue().replace("<<CARRERA>>", seleccionGrupo.getMateria().getCarrera().toString()));
                        } else if (cell.getStringCellValue().contains("<<NIVEL>>")) {
                            cell.setCellValue(cell.getStringCellValue().replace("<<NIVEL>>", seleccionGrupo.getMateria().getNivel().toString()));
                        } else if (cell.getStringCellValue().contains("<<GA>>")) {
                            cell.setCellValue(cell.getStringCellValue().replace("<<GA>>", seleccionGrupo.getGestionAcademica().toString()));
                        } else if (cell.getStringCellValue().contains("<<DOCENTE>>")) {
                            cell.setCellValue(cell.getStringCellValue().replace("<<DOCENTE>>", seleccionGrupo.getEmpleado().toString()));
                        } else if (cell.getStringCellValue().contains("<<COD_MAT>>")) {
                            cell.setCellValue(cell.getStringCellValue().replace("<<COD_MAT>>", seleccionGrupo.getMateria().getCodigo()));
                        } else if (cell.getStringCellValue().contains("<<INSTITUTO>>")) {
                            cell.setCellValue(cell.getStringCellValue().replace("<<INSTITUTO>>", seleccionGrupo.getMateria().getCarrera().getCampus().getInstituto().getNombreRegulador()));
                        } else if (cell.getStringCellValue().contains("<<NIVEL_ACADEMICO>>")) {
                            cell.setCellValue(cell.getStringCellValue().replace("<<NIVEL_ACADEMICO>>", seleccionGrupo.getMateria().getCarrera().getNivelAcademico().getNombre()));
                        } else if (cell.getStringCellValue().contains("<<NIVEL>>")) {
                            cell.setCellValue(cell.getStringCellValue().replace("<<NIVEL>>", seleccionGrupo.getMateria().getNivel().getNombre()));
                        } else if (cell.getStringCellValue().contains("<<GRUPO>>")) {
                            cell.setCellValue(cell.getStringCellValue().replace("<<GRUPO>>", seleccionGrupo.toString()));
                        } else if (cell.getStringCellValue().contains("<<NA>>")) {
                            cell.setCellValue(cell.getStringCellValue().replace("<<NA>>", seleccionGrupo.getMateria().getCarrera().getNivelAcademico().toString()));
                        } else if (cell.getStringCellValue().contains("<<ESTUDIANTE>>")) {
                            rowNum = row.getRowNum();
                        }
                    } else if (cell.getCellTypeEnum() == CellType.NUMERIC) {
                        if (cell.getNumericCellValue() == -100) {
                            Long inscritos = notaFacade.cantidadInscritos(seleccionGrupo.getId_grupo());
                            cell.setCellValue(inscritos);
                        } else if (cell.getNumericCellValue() == -101) {
                            Long aprobados = notaFacade.cantidadCondicion(seleccionGrupo.getId_grupo(), Condicion.APROBADO);
                            cell.setCellValue(aprobados);
                        } else if (cell.getNumericCellValue() == -102) {
                            Long reprobados = notaFacade.cantidadCondicion(seleccionGrupo.getId_grupo(), Condicion.REPROBADO);
                            cell.setCellValue(reprobados);
                        } else if (cell.getNumericCellValue() == -103) {
                            Long abandonos = notaFacade.cantidadCondicion(seleccionGrupo.getId_grupo(), Condicion.ABANDONO);
                            cell.setCellValue(abandonos);
                        } else if (cell.getNumericCellValue() == -104) {
                            Long inscritos = notaFacade.cantidadInscritos(seleccionGrupo.getId_grupo());
                            Long abandonos = notaFacade.cantidadCondicion(seleccionGrupo.getId_grupo(), Condicion.ABANDONO);
                            Long efectivos = inscritos - abandonos;
                            cell.setCellValue(efectivos);
                        }
                    }
                }
            }

            List<Nota> notas = notaFacade.listaNotasGrupo(seleccionGrupo.getId_grupo());

            sheet.shiftRows(rowNum + 1, sheet.getLastRowNum(), notas.size() - 1, true, true);

            CellCopyPolicy cellCopyPolicy = new CellCopyPolicy.Builder().cellStyle(true).build();
            for (int i = 0; i < notas.size() - 1; i++) {
                sheet.copyRows(rowNum, rowNum, rowNum + i + 1, cellCopyPolicy);
            }

            for (int i = 0; i < notas.size(); i++) {
                Nota nota = notas.get(i);

                XSSFRow row = sheet.getRow(rowNum + i);
                Iterator<Cell> cellIterator = row.cellIterator();

                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();

                    if (cell.getCellTypeEnum() == CellType.NUMERIC) {
                        if (cell.getNumericCellValue() == -1) {
                            cell.setCellValue(i + 1);
                        } else if (cell.getNumericCellValue() == -10) {
                            if (nota.getTeoria1() != null) {
                                cell.setCellValue(nota.getTeoria1());
                            } else {
                                cell.setCellValue("");
                            }
                        } else if (cell.getNumericCellValue() == -11) {
                            if (nota.getPractica1() != null) {
                                cell.setCellValue(nota.getPractica1());
                            } else {
                                cell.setCellValue("");
                            }
                        } else if (cell.getNumericCellValue() == -12) {
                            if (nota.getNota1() != null) {
                                cell.setCellValue(nota.getNota1());
                            } else {
                                cell.setCellValue("");
                            }
                        } else if (cell.getNumericCellValue() == -20) {
                            if (nota.getTeoria2() != null) {
                                cell.setCellValue(nota.getTeoria2());
                            } else {
                                cell.setCellValue("");
                            }
                        } else if (cell.getNumericCellValue() == -21) {
                            if (nota.getPractica2() != null) {
                                cell.setCellValue(nota.getPractica2());
                            } else {
                                cell.setCellValue("");
                            }
                        } else if (cell.getNumericCellValue() == -22) {
                            if (nota.getNota2() != null) {
                                cell.setCellValue(nota.getNota2());
                            } else {
                                cell.setCellValue("");
                            }
                        } else if (cell.getNumericCellValue() == -30) {
                            if (nota.getTeoria3() != null) {
                                cell.setCellValue(nota.getTeoria3());
                            } else {
                                cell.setCellValue("");
                            }
                        } else if (cell.getNumericCellValue() == -31) {
                            if (nota.getPractica3() != null) {
                                cell.setCellValue(nota.getPractica3());
                            } else {
                                cell.setCellValue("");
                            }
                        } else if (cell.getNumericCellValue() == -32) {
                            if (nota.getNota3() != null) {
                                cell.setCellValue(nota.getNota3());
                            } else {
                                cell.setCellValue("");
                            }
                        } else if (cell.getNumericCellValue() == -40) {
                            if (nota.getTeoria4() != null) {
                                cell.setCellValue(nota.getTeoria4());
                            } else {
                                cell.setCellValue("");
                            }
                        } else if (cell.getNumericCellValue() == -41) {
                            if (nota.getPractica4() != null) {
                                cell.setCellValue(nota.getPractica4());
                            } else {
                                cell.setCellValue("");
                            }
                        } else if (cell.getNumericCellValue() == -42) {
                            if (nota.getNota4() != null) {
                                cell.setCellValue(nota.getNota4());
                            } else {
                                cell.setCellValue("");
                            }
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

                    } else if (cell.getCellTypeEnum() == CellType.STRING) {
                        if (cell.getStringCellValue().contains("<<DNI>>")) {
                            cell.setCellValue(cell.getStringCellValue().replace("<<DNI>>", nota.getEstudiante().dniLugar()));
                        } else if (cell.getStringCellValue().contains("<<ESTUDIANTE>>")) {
                            cell.setCellValue(cell.getStringCellValue().replace("<<ESTUDIANTE>>", nota.getEstudiante().toString()));
                        } else if (cell.getStringCellValue().contains("<<LITERAL>>")) {
                            if (nota.getNotaFinal() != null) {
                                cell.setCellValue(cell.getStringCellValue().replace("<<LITERAL>>", NumberToLetterConverter.convertNumberToLetter(nota.getNotaFinal())));
                            }
                        } else if (cell.getStringCellValue().contains("<<CONDICION>>")) {
                            cell.setCellValue(cell.getStringCellValue().replace("<<CONDICION>>", nota.getCondicion().getNombre()));
                        }
                    }
                }
            }

        }

        descargarArchivo(workbook, seleccionGrupo);

        //log
        logFacade.create(new Log(Fecha.getDate(), EventoLog.READ, "Generación reporte planilla de seguimiento en formato XLSX", loginController.getUsr().toString()));
    }

    public void generarPDF() throws IOException {
        this.insertarParametro("id_grupo", seleccionGrupo.getId_grupo());

        toPlanillaSeguimiento();

        //log
        logFacade.create(new Log(Fecha.getDate(), EventoLog.READ, "Generación reporte planilla de seguimiento en formato PDF", loginController.getUsr().toString()));
    }

    public void toReportePlanillaSeguimiento() throws IOException {
        reinit();

        this.redireccionarViewId("/reportes/notas/planillaSeguimiento/reportePlanillaSeguimiento");
    }

    public void toPlanillaSeguimiento() throws IOException {
        this.redireccionarViewId("/reportes/notas/planillaSeguimiento/planillaSeguimiento");
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
     * @return the seleccionNivel
     */
    public Nivel getSeleccionNivel() {
        return seleccionNivel;
    }

    /**
     * @param seleccionNivel the seleccionNivel to set
     */
    public void setSeleccionNivel(Nivel seleccionNivel) {
        this.seleccionNivel = seleccionNivel;
    }

    /**
     * @return the seleccionGrupo
     */
    public Grupo getSeleccionGrupo() {
        return seleccionGrupo;
    }

    /**
     * @param seleccionGrupo the seleccionGrupo to set
     */
    public void setSeleccionGrupo(Grupo seleccionGrupo) {
        this.seleccionGrupo = seleccionGrupo;
    }

    /**
     * @return the download
     */
    public StreamedContent getDownload() {
        return download;
    }

    /**
     * @return the seleccionMencion
     */
    public Mencion getSeleccionMencion() {
        return seleccionMencion;
    }

    /**
     * @param seleccionMencion the seleccionMencion to set
     */
    public void setSeleccionMencion(Mencion seleccionMencion) {
        this.seleccionMencion = seleccionMencion;
    }

    /**
     * @return the seleccionTurno
     */
    public Turno getSeleccionTurno() {
        return seleccionTurno;
    }

    /**
     * @param seleccionTurno the seleccionTurno to set
     */
    public void setSeleccionTurno(Turno seleccionTurno) {
        this.seleccionTurno = seleccionTurno;
    }
}
