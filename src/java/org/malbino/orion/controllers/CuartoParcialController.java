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
import org.malbino.orion.entities.Nota;
import org.malbino.orion.enums.EntidadLog;
import org.malbino.orion.enums.EventoLog;
import org.malbino.orion.enums.Funcionalidad;
import org.malbino.orion.enums.ModalidadEvaluacion;
import org.malbino.orion.enums.Regimen;
import org.malbino.orion.facades.ActividadFacade;
import org.malbino.orion.facades.GrupoFacade;
import org.malbino.orion.facades.NotaFacade;
import org.malbino.orion.facades.negocio.FileEstudianteFacade;
import org.malbino.orion.facades.negocio.RegistroDocenteFacade;
import org.malbino.orion.util.Fecha;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.file.UploadedFile;

/**
 *
 * @author Tincho
 */
@Named("CuartoParcialController")
@SessionScoped
public class CuartoParcialController extends AbstractController implements Serializable {

    private static final String PATHNAME = File.separator + "resources" + File.separator + "uploads" + File.separator + "registro_pedagogico.xlsx";
    private static final String PARCIAL = "CUARTO PARCIAL";

    private static final String PATHNAME_SEMESTRAL = File.separator + "resources" + File.separator + "uploads" + File.separator + "planilla_seguimiento_semestral.xlsx";
    private static final String PATHNAME_ANUAL = File.separator + "resources" + File.separator + "uploads" + File.separator + "planilla_seguimiento_anual.xlsx";

    @Inject
    LoginController loginController;
    @EJB
    GrupoFacade grupoFacade;
    @EJB
    NotaFacade notaFacade;
    @EJB
    RegistroDocenteFacade registroDocenteFacade;
    @EJB
    ActividadFacade actividadFacade;
    @EJB
    FileEstudianteFacade fileEstudianteFacade;

    private GestionAcademica seleccionGestionAcademica;
    private Carrera seleccionCarrera;
    private Grupo seleccionGrupo;
    private List<Nota> notas;

    private StreamedContent download;
    private UploadedFile upload;

    @PostConstruct
    public void init() {
        seleccionGestionAcademica = null;
        seleccionCarrera = null;
        seleccionGrupo = null;
        notas = new ArrayList();
    }

    public void reinit() {
        seleccionGestionAcademica = null;
        seleccionCarrera = null;
        seleccionGrupo = null;
        notas = new ArrayList();
    }

    @Override
    public List<GestionAcademica> listaGestionesAcademicas() {
        return gestionAcademicaFacade.listaGestionAcademica(ModalidadEvaluacion.ANUAL_4P, true);
    }

    @Override
    public List<Carrera> listaCarreras() {
        List<Carrera> l = new ArrayList();
        if (seleccionGestionAcademica != null) {
            l = carreraFacade.listaCarreras(seleccionGestionAcademica.getRegimen());
        }
        return l;
    }

    public List<Grupo> listaGrupos() {
        List<Grupo> l = new ArrayList();
        if (seleccionGestionAcademica != null && seleccionCarrera != null && loginController.getUsr() != null) {
            l = grupoFacade.listaGruposEmpleado(seleccionGestionAcademica.getId_gestionacademica(), seleccionCarrera.getId_carrera(), loginController.getUsr().getId_persona());
        }
        return l;
    }

    public void actualizarNotas() {
        if (seleccionGrupo != null) {
            notas = notaFacade.listaNotasGrupo(seleccionGrupo.getId_grupo());
        }
    }

    public void guardar() {
        if (!actividadFacade.listaActividades(Fecha.getDate(), Funcionalidad.REGISTRO_NOTAS_CUARTO_PARCIAL, seleccionGestionAcademica.getId_gestionacademica()).isEmpty()) {
            if (registroDocenteFacade.editarNotas(notas)) {
                actualizarNotas();

                //log
                for (Nota nota : notas) {
                    logFacade.create(new Log(Fecha.getDate(), EventoLog.UPDATE, EntidadLog.NOTA, nota.getId_nota(), "Actualización del cuarto parcial", loginController.getUsr().toString()));
                }

                this.mensajeDeInformacion("Guardado.");
            }
        } else {
            actualizarNotas();

            this.mensajeDeError("Fuera de fecha.");
        }
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
                    .name(PARCIAL + " - " + seleccGrupo.toString_RegistroParcial() + ".xlsx")
                    .contentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                    .stream(() -> is)
                    .build();
        } catch (IOException ex) {
            this.mensajeDeError("Error: No se pudo descargar el archivo.");
        }
    }

    public void bajarRegistro() {
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
                            cell.setCellValue(cell.getStringCellValue().replace("<<INSTITUTO>>", seleccionGrupo.getMateria().getCarrera().getCampus().getInstituto().getNombreRegulador()));
                        } else if (cell.getStringCellValue().contains("<<MATERIA>>")) {
                            cell.setCellValue(cell.getStringCellValue().replace("<<MATERIA>>", seleccionGrupo.getMateria().toString()));
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
                        } else if (cell.getStringCellValue().contains("<<GRUPO>>")) {
                            cell.setCellValue(cell.getStringCellValue().replace("<<GRUPO>>", seleccionGrupo.getCodigo()));
                        } else if (cell.getStringCellValue().contains("<<PARCIAL>>")) {
                            cell.setCellValue(cell.getStringCellValue().replace("<<PARCIAL>>", PARCIAL));
                        } else if (cell.getStringCellValue().contains("<<ESTUDIANTE>>")) {
                            rowNum = row.getRowNum();
                        }
                    }
                }
            }

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
                        } else if (cell.getNumericCellValue() == -2) {
                            cell.setCellValue(nota.getId_nota());
                        }
                    } else if (cell.getCellTypeEnum() == CellType.STRING) {
                        if (cell.getStringCellValue().contains("<<ESTUDIANTE>>")) {
                            cell.setCellValue(cell.getStringCellValue().replace("<<ESTUDIANTE>>", nota.getEstudiante().toString()));
                        }
                    }
                }
            }

        }

        descargarArchivo(workbook, seleccionGrupo);

        //log
        logFacade.create(new Log(Fecha.getDate(), EventoLog.READ, EntidadLog.GRUPO, seleccionGrupo.getId_grupo(), "Descarga Registro Pedagógico", loginController.getUsr().toString()));
    }

    public void subirRegistro(FileUploadEvent event) throws IOException {
        if (!actividadFacade.listaActividades(Fecha.getDate(), Funcionalidad.REGISTRO_NOTAS_CUARTO_PARCIAL, seleccionGestionAcademica.getId_gestionacademica()).isEmpty()) {
            if (seleccionGrupo != null) {
                XSSFWorkbook workbook = new XSSFWorkbook(event.getFile().getInputStream());

                XSSFSheet sheet = workbook.getSheetAt(0);
                if (sheet != null) {
                    Iterator<Row> rowIterator = sheet.rowIterator();

                    int codigoColumnIndex = 0;
                    int teoriaColumnIndex = 0;
                    int practicaColumnIndex = 0;
                    while (rowIterator.hasNext()) {
                        Row row = rowIterator.next();

                        Iterator<Cell> cellIterator = row.cellIterator();
                        while (cellIterator.hasNext()) {
                            Cell cell = cellIterator.next();

                            if (cell.getCellTypeEnum() == CellType.STRING) {
                                if (cell.getStringCellValue().equals("CODIGO")) {
                                    codigoColumnIndex = cell.getColumnIndex();
                                } else if (cell.getStringCellValue().equals("TEORIA")) {
                                    teoriaColumnIndex = cell.getColumnIndex();
                                } else if (cell.getStringCellValue().equals("PRACTICA")) {
                                    practicaColumnIndex = cell.getColumnIndex();
                                }
                            }
                        }
                    }

                    rowIterator = sheet.rowIterator();
                    while (rowIterator.hasNext()) {
                        Row row = rowIterator.next();

                        Cell codigoCell = row.getCell(codigoColumnIndex);
                        if (codigoCell != null && codigoCell.getCellTypeEnum() == CellType.NUMERIC) {
                            Double codigoCellValue = codigoCell.getNumericCellValue();
                            if (codigoCellValue != null) {
                                int id_nota = codigoCellValue.intValue();
                                Nota nota = notaFacade.find(id_nota);
                                if (nota != null) {
                                    Cell teoriaCell = row.getCell(teoriaColumnIndex);
                                    if (teoriaCell != null && (teoriaCell.getCellTypeEnum() == CellType.NUMERIC || teoriaCell.getCellTypeEnum() == CellType.FORMULA)) {
                                        Double teoriaCellValue = teoriaCell.getNumericCellValue();
                                        if (teoriaCellValue != null && teoriaCellValue >= 0 && teoriaCellValue <= 30) {

                                            Cell practicaCell = row.getCell(practicaColumnIndex);
                                            if (practicaCell != null && (teoriaCell.getCellTypeEnum() == CellType.NUMERIC || teoriaCell.getCellTypeEnum() == CellType.FORMULA)) {
                                                Double practicaCellValue = practicaCell.getNumericCellValue();
                                                if (practicaCellValue != null && practicaCellValue >= 0 && practicaCellValue <= 70) {

                                                    if (nota.getGrupo().equals(seleccionGrupo)) {
                                                        nota.setTeoria4(teoriaCellValue.intValue());
                                                        nota.setPractica4(practicaCellValue.intValue());

                                                        fileEstudianteFacade.editarParcial(nota);

                                                        //log
                                                        logFacade.create(new Log(Fecha.getDate(), EventoLog.UPDATE, EntidadLog.NOTA, nota.getId_nota(), "Actualización del cuarto parcial", loginController.getUsr().toString()));
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                actualizarNotas();
            } else {
                this.mensajeDeError("* Ningun grupo seleccionado.");
            }
        } else {
            actualizarNotas();

            this.mensajeDeError("Fuera de fecha.");
        }
    }

    public void generarPDF() throws IOException {
        this.insertarParametro("id_grupo", seleccionGrupo.getId_grupo());

        this.redireccionarViewId("/registroDocente/planillaSeguimiento");

        //log
        logFacade.create(new Log(Fecha.getDate(), EventoLog.READ, EntidadLog.GRUPO, seleccionGrupo.getId_grupo(), "Descarga Planilla de Seguimiento", loginController.getUsr().toString()));
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
     * @return the notas
     */
    public List<Nota> getNotas() {
        return notas;
    }

    /**
     * @param notas the notas to set
     */
    public void setNotas(List<Nota> notas) {
        this.notas = notas;
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
     * @return the upload
     */
    public UploadedFile getUpload() {
        return upload;
    }

    /**
     * @param upload the upload to set
     */
    public void setUpload(UploadedFile upload) {
        this.upload = upload;
    }
}
