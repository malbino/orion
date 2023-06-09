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
import java.util.Arrays;
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
import org.malbino.orion.entities.Log;
import org.malbino.orion.entities.Mencion;
import org.malbino.orion.enums.EventoLog;
import org.malbino.orion.enums.Nivel;
import org.malbino.orion.enums.Turno;
import org.malbino.orion.facades.GrupoFacade;
import org.malbino.orion.facades.MencionFacade;
import org.malbino.orion.facades.negocio.SeguimientoAcademicoFacade;
import org.malbino.orion.pojos.seguimiento.EstudianteSeguimiento;
import org.malbino.orion.pojos.seguimiento.NotaSeguimiento;
import org.malbino.orion.pojos.seguimiento.Seguimiento;
import org.malbino.orion.util.Fecha;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author Tincho
 */
@Named("ReporteSeguimientoAcademicoController")
@SessionScoped
public class ReporteSeguimientoAcademicoController extends AbstractController implements Serializable {

    private static final String PATHNAME_SEMESTRAL = File.separator + "resources" + File.separator + "uploads" + File.separator + "seguimiento_academico_semestral.xlsx";
    private static final String PATHNAME_ANUAL = File.separator + "resources" + File.separator + "uploads" + File.separator + "seguimiento_academico_anual.xlsx";

    @EJB
    GrupoFacade grupoFacade;
    @EJB
    SeguimientoAcademicoFacade seguimientoAcademicoFacade;
    @EJB
    MencionFacade mencionFacade;
    @Inject
    LoginController loginController;

    private GestionAcademica seleccionGestionAcademica;
    private Carrera seleccionCarrera;
    private Mencion seleccionMencion;
    private Nivel seleccionNivel;
    private Turno seleccionTurno;
    private String seleccionParalelo;

    private StreamedContent download;

    @PostConstruct
    public void init() {
        seleccionGestionAcademica = null;
        seleccionCarrera = null;
        seleccionMencion = null;
        seleccionNivel = null;
        seleccionTurno = null;
        seleccionParalelo = null;
    }

    public void reinit() {
        seleccionGestionAcademica = null;
        seleccionCarrera = null;
        seleccionMencion = null;
        seleccionNivel = null;
        seleccionTurno = null;
        seleccionParalelo = null;
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
        if (seleccionGestionAcademica != null && seleccionCarrera != null) {
            niveles = Arrays.stream(Nivel.values()).filter(nivel -> nivel.getRegimen().equals(seleccionCarrera.getRegimen())).toArray(Nivel[]::new);
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

    public List<String> listaParalelos() {
        List<String> paralelos = new ArrayList<>();
        if (seleccionGestionAcademica != null && seleccionCarrera != null && seleccionNivel != null && seleccionTurno != null) {
            paralelos = grupoFacade.listaParalelos(seleccionGestionAcademica.getId_gestionacademica(), seleccionCarrera.getId_carrera(), seleccionMencion, seleccionNivel, seleccionTurno);
        }
        return paralelos;
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

    public void descargarArchivo(XSSFWorkbook workbook, String name) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            workbook.write(baos);
            workbook.close();

            InputStream is = new ByteArrayInputStream(baos.toByteArray());

            download = DefaultStreamedContent.builder()
                    .name(name + ".xlsx")
                    .contentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                    .stream(() -> is)
                    .build();
        } catch (IOException ex) {
            this.mensajeDeError("Error: No se pudo descargar el archivo.");
        }
    }

    public void bajarSeguimiento() {
        Seguimiento seguimiento = seguimientoAcademicoFacade.seguimientoAcademico(seleccionGestionAcademica, seleccionCarrera, seleccionMencion, seleccionNivel, seleccionTurno, seleccionParalelo);

        XSSFWorkbook workbook = null;
        if (seleccionGestionAcademica.getRegimen().getCantidadParciales() == 3) {
            workbook = leerArchivo(PATHNAME_SEMESTRAL);
        } else if (seleccionGestionAcademica.getRegimen().getCantidadParciales() == 4) {
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
                        if (cell.getStringCellValue().contains("<<INSTITUTO>>")) {
                            cell.setCellValue(cell.getStringCellValue().replace("<<INSTITUTO>>", seguimiento.getInstituto()));
                        } else if (cell.getStringCellValue().contains("<<GESTION>>")) {
                            cell.setCellValue(cell.getStringCellValue().replace("<<GESTION>>", seguimiento.getGestion()));
                        } else if (cell.getStringCellValue().contains("<<CARRERA>>")) {
                            cell.setCellValue(cell.getStringCellValue().replace("<<CARRERA>>", seguimiento.getCarrera()));
                        } else if (cell.getStringCellValue().contains("<<NIVEL>>")) {
                            cell.setCellValue(cell.getStringCellValue().replace("<<NIVEL>>", seguimiento.getNivel()));
                        } else if (cell.getStringCellValue().contains("<<TURNO>>")) {
                            cell.setCellValue(cell.getStringCellValue().replace("<<TURNO>>", seguimiento.getTurno()));
                        } else if (cell.getStringCellValue().contains("<<PARALELO>>")) {
                            cell.setCellValue(cell.getStringCellValue().replace("<<PARALELO>>", seguimiento.getParalelo()));
                        } else if (cell.getStringCellValue().contains("<<M1>>")) {
                            if (seguimiento.getMateriasSeguimiento()[0] != null) {
                                cell.setCellValue(cell.getStringCellValue().replace("<<M1>>", seguimiento.getMateriasSeguimiento()[0]));
                            } else {
                                cell.setCellValue(cell.getStringCellValue().replace("<<M1>>", ""));
                            }
                        } else if (cell.getStringCellValue().contains("<<M2>>")) {
                            if (seguimiento.getMateriasSeguimiento()[1] != null) {
                                cell.setCellValue(cell.getStringCellValue().replace("<<M2>>", seguimiento.getMateriasSeguimiento()[1]));
                            } else {
                                cell.setCellValue(cell.getStringCellValue().replace("<<M2>>", ""));
                            }
                        } else if (cell.getStringCellValue().contains("<<M3>>")) {
                            if (seguimiento.getMateriasSeguimiento()[2] != null) {
                                cell.setCellValue(cell.getStringCellValue().replace("<<M3>>", seguimiento.getMateriasSeguimiento()[2]));
                            } else {
                                cell.setCellValue(cell.getStringCellValue().replace("<<M3>>", ""));
                            }
                        } else if (cell.getStringCellValue().contains("<<M4>>")) {
                            if (seguimiento.getMateriasSeguimiento()[3] != null) {
                                cell.setCellValue(cell.getStringCellValue().replace("<<M4>>", seguimiento.getMateriasSeguimiento()[3]));
                            } else {
                                cell.setCellValue(cell.getStringCellValue().replace("<<M4>>", ""));
                            }
                        } else if (cell.getStringCellValue().contains("<<M5>>")) {
                            if (seguimiento.getMateriasSeguimiento()[4] != null) {
                                cell.setCellValue(cell.getStringCellValue().replace("<<M5>>", seguimiento.getMateriasSeguimiento()[4]));
                            } else {
                                cell.setCellValue(cell.getStringCellValue().replace("<<M5>>", ""));
                            }
                        } else if (cell.getStringCellValue().contains("<<M6>>")) {
                            if (seguimiento.getMateriasSeguimiento()[5] != null) {
                                cell.setCellValue(cell.getStringCellValue().replace("<<M6>>", seguimiento.getMateriasSeguimiento()[5]));
                            } else {
                                cell.setCellValue(cell.getStringCellValue().replace("<<M6>>", ""));
                            }
                        } else if (cell.getStringCellValue().contains("<<M7>>")) {
                            if (seguimiento.getMateriasSeguimiento()[6] != null) {
                                cell.setCellValue(cell.getStringCellValue().replace("<<M7>>", seguimiento.getMateriasSeguimiento()[6]));
                            } else {
                                cell.setCellValue(cell.getStringCellValue().replace("<<M7>>", ""));
                            }
                        } else if (cell.getStringCellValue().contains("<<M8>>")) {
                            if (seguimiento.getMateriasSeguimiento()[7] != null) {
                                cell.setCellValue(cell.getStringCellValue().replace("<<M8>>", seguimiento.getMateriasSeguimiento()[7]));
                            } else {
                                cell.setCellValue(cell.getStringCellValue().replace("<<M8>>", ""));
                            }
                        } else if (cell.getStringCellValue().contains("<<ESTUDIANTE>>")) {
                            rowNum = row.getRowNum();
                        }
                    }
                }
            }

            EstudianteSeguimiento[] estudiantesSeguimiento = seguimiento.getEstudiantesSeguimiento();

            sheet.shiftRows(rowNum + 1, sheet.getLastRowNum(), estudiantesSeguimiento.length - 1, true, true);

            CellCopyPolicy cellCopyPolicy = new CellCopyPolicy.Builder().cellStyle(true).build();
            for (int i = 0; i < estudiantesSeguimiento.length - 1; i++) {
                sheet.copyRows(rowNum, rowNum, rowNum + i + 1, cellCopyPolicy);
            }

            for (int i = 0; i < estudiantesSeguimiento.length; i++) {
                EstudianteSeguimiento estudianteSeguimiento = estudiantesSeguimiento[i];
                NotaSeguimiento[] notasSeguimiento = estudianteSeguimiento.getNotasSeguimiento();

                XSSFRow row = sheet.getRow(rowNum + i);
                Iterator<Cell> cellIterator = row.cellIterator();

                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();

                    if (cell.getCellTypeEnum() == CellType.NUMERIC) {
                        if (cell.getNumericCellValue() == -1) {
                            cell.setCellValue(i + 1);
                        } else if (cell.getNumericCellValue() == -11) {
                            if (notasSeguimiento[0] != null) {
                                if (notasSeguimiento[0].getNota1() != null) {
                                    cell.setCellValue(notasSeguimiento[0].getNota1());
                                } else {
                                    cell.setCellValue("");
                                }
                            } else {
                                cell.setCellValue("");
                            }
                        } else if (cell.getNumericCellValue() == -12) {
                            if (notasSeguimiento[0] != null) {
                                if (notasSeguimiento[0].getNota2() != null) {
                                    cell.setCellValue(notasSeguimiento[0].getNota2());
                                } else {
                                    cell.setCellValue("");
                                }
                            } else {
                                cell.setCellValue("");
                            }
                        } else if (cell.getNumericCellValue() == -13) {
                            if (notasSeguimiento[0] != null) {
                                if (notasSeguimiento[0].getNota3() != null) {
                                    cell.setCellValue(notasSeguimiento[0].getNota3());
                                } else {
                                    cell.setCellValue("");
                                }
                            } else {
                                cell.setCellValue("");
                            }
                        } else if (cell.getNumericCellValue() == -14) {
                            if (notasSeguimiento[0] != null) {
                                if (notasSeguimiento[0].getNota4() != null) {
                                    cell.setCellValue(notasSeguimiento[0].getNota4());
                                } else {
                                    cell.setCellValue("");
                                }
                            } else {
                                cell.setCellValue("");
                            }
                        } else if (cell.getNumericCellValue() == -10) {
                            if (notasSeguimiento[0] != null) {
                                if (notasSeguimiento[0].getNotaFinal() != null) {
                                    cell.setCellValue(notasSeguimiento[0].getNotaFinal());
                                } else {
                                    cell.setCellValue("");
                                }
                            } else {
                                cell.setCellValue("");
                            }
                        } else if (cell.getNumericCellValue() == -15) {
                            if (notasSeguimiento[0] != null) {
                                if (notasSeguimiento[0].getRecuperatorio() != null) {
                                    cell.setCellValue(notasSeguimiento[0].getRecuperatorio());
                                } else {
                                    cell.setCellValue("");
                                }
                            } else {
                                cell.setCellValue("");
                            }
                        } else if (cell.getNumericCellValue() == -21) {
                            if (notasSeguimiento[1] != null) {
                                if (notasSeguimiento[1].getNota1() != null) {
                                    cell.setCellValue(notasSeguimiento[1].getNota1());
                                } else {
                                    cell.setCellValue("");
                                }
                            } else {
                                cell.setCellValue("");
                            }
                        } else if (cell.getNumericCellValue() == -22) {
                            if (notasSeguimiento[1] != null) {
                                if (notasSeguimiento[1].getNota2() != null) {
                                    cell.setCellValue(notasSeguimiento[1].getNota2());
                                } else {
                                    cell.setCellValue("");
                                }
                            } else {
                                cell.setCellValue("");
                            }
                        } else if (cell.getNumericCellValue() == -23) {
                            if (notasSeguimiento[1] != null) {
                                if (notasSeguimiento[1].getNota3() != null) {
                                    cell.setCellValue(notasSeguimiento[1].getNota3());
                                } else {
                                    cell.setCellValue("");
                                }
                            } else {
                                cell.setCellValue("");
                            }
                        } else if (cell.getNumericCellValue() == -24) {
                            if (notasSeguimiento[1] != null) {
                                if (notasSeguimiento[1].getNota4() != null) {
                                    cell.setCellValue(notasSeguimiento[1].getNota4());
                                } else {
                                    cell.setCellValue("");
                                }
                            } else {
                                cell.setCellValue("");
                            }
                        } else if (cell.getNumericCellValue() == -20) {
                            if (notasSeguimiento[1] != null) {
                                if (notasSeguimiento[1].getNotaFinal() != null) {
                                    cell.setCellValue(notasSeguimiento[1].getNotaFinal());
                                } else {
                                    cell.setCellValue("");
                                }
                            } else {
                                cell.setCellValue("");
                            }
                        } else if (cell.getNumericCellValue() == -25) {
                            if (notasSeguimiento[1] != null) {
                                if (notasSeguimiento[1].getRecuperatorio() != null) {
                                    cell.setCellValue(notasSeguimiento[1].getRecuperatorio());
                                } else {
                                    cell.setCellValue("");
                                }
                            } else {
                                cell.setCellValue("");
                            }
                        } else if (cell.getNumericCellValue() == -31) {
                            if (notasSeguimiento[2] != null) {
                                if (notasSeguimiento[2].getNota1() != null) {
                                    cell.setCellValue(notasSeguimiento[2].getNota1());
                                } else {
                                    cell.setCellValue("");
                                }
                            } else {
                                cell.setCellValue("");
                            }
                        } else if (cell.getNumericCellValue() == -32) {
                            if (notasSeguimiento[2] != null) {
                                if (notasSeguimiento[2].getNota2() != null) {
                                    cell.setCellValue(notasSeguimiento[2].getNota2());
                                } else {
                                    cell.setCellValue("");
                                }
                            } else {
                                cell.setCellValue("");
                            }
                        } else if (cell.getNumericCellValue() == -33) {
                            if (notasSeguimiento[2] != null) {
                                if (notasSeguimiento[2].getNota3() != null) {
                                    cell.setCellValue(notasSeguimiento[2].getNota3());
                                } else {
                                    cell.setCellValue("");
                                }
                            } else {
                                cell.setCellValue("");
                            }
                        } else if (cell.getNumericCellValue() == -34) {
                            if (notasSeguimiento[2] != null) {
                                if (notasSeguimiento[2].getNota4() != null) {
                                    cell.setCellValue(notasSeguimiento[2].getNota4());
                                } else {
                                    cell.setCellValue("");
                                }
                            } else {
                                cell.setCellValue("");
                            }
                        } else if (cell.getNumericCellValue() == -30) {
                            if (notasSeguimiento[2] != null) {
                                if (notasSeguimiento[2].getNotaFinal() != null) {
                                    cell.setCellValue(notasSeguimiento[2].getNotaFinal());
                                } else {
                                    cell.setCellValue("");
                                }
                            } else {
                                cell.setCellValue("");
                            }
                        } else if (cell.getNumericCellValue() == -35) {
                            if (notasSeguimiento[2] != null) {
                                if (notasSeguimiento[2].getRecuperatorio() != null) {
                                    cell.setCellValue(notasSeguimiento[2].getRecuperatorio());
                                } else {
                                    cell.setCellValue("");
                                }
                            } else {
                                cell.setCellValue("");
                            }
                        } else if (cell.getNumericCellValue() == -41) {
                            if (notasSeguimiento[3] != null) {
                                if (notasSeguimiento[3].getNota1() != null) {
                                    cell.setCellValue(notasSeguimiento[3].getNota1());
                                } else {
                                    cell.setCellValue("");
                                }
                            } else {
                                cell.setCellValue("");
                            }
                        } else if (cell.getNumericCellValue() == -42) {
                            if (notasSeguimiento[3] != null) {
                                if (notasSeguimiento[3].getNota2() != null) {
                                    cell.setCellValue(notasSeguimiento[3].getNota2());
                                } else {
                                    cell.setCellValue("");
                                }
                            } else {
                                cell.setCellValue("");
                            }
                        } else if (cell.getNumericCellValue() == -43) {
                            if (notasSeguimiento[3] != null) {
                                if (notasSeguimiento[3].getNota3() != null) {
                                    cell.setCellValue(notasSeguimiento[3].getNota3());
                                } else {
                                    cell.setCellValue("");
                                }
                            } else {
                                cell.setCellValue("");
                            }
                        } else if (cell.getNumericCellValue() == -44) {
                            if (notasSeguimiento[3] != null) {
                                if (notasSeguimiento[3].getNota4() != null) {
                                    cell.setCellValue(notasSeguimiento[3].getNota4());
                                } else {
                                    cell.setCellValue("");
                                }
                            } else {
                                cell.setCellValue("");
                            }
                        } else if (cell.getNumericCellValue() == -40) {
                            if (notasSeguimiento[3] != null) {
                                if (notasSeguimiento[3].getNotaFinal() != null) {
                                    cell.setCellValue(notasSeguimiento[3].getNotaFinal());
                                } else {
                                    cell.setCellValue("");
                                }
                            } else {
                                cell.setCellValue("");
                            }
                        } else if (cell.getNumericCellValue() == -45) {
                            if (notasSeguimiento[3] != null) {
                                if (notasSeguimiento[3].getRecuperatorio() != null) {
                                    cell.setCellValue(notasSeguimiento[3].getRecuperatorio());
                                } else {
                                    cell.setCellValue("");
                                }
                            } else {
                                cell.setCellValue("");
                            }
                        } else if (cell.getNumericCellValue() == -51) {
                            if (notasSeguimiento[4] != null) {
                                if (notasSeguimiento[4].getNota1() != null) {
                                    cell.setCellValue(notasSeguimiento[4].getNota1());
                                } else {
                                    cell.setCellValue("");
                                }
                            } else {
                                cell.setCellValue("");
                            }
                        } else if (cell.getNumericCellValue() == -52) {
                            if (notasSeguimiento[4] != null) {
                                if (notasSeguimiento[4].getNota2() != null) {
                                    cell.setCellValue(notasSeguimiento[4].getNota2());
                                } else {
                                    cell.setCellValue("");
                                }
                            } else {
                                cell.setCellValue("");
                            }
                        } else if (cell.getNumericCellValue() == -53) {
                            if (notasSeguimiento[4] != null) {
                                if (notasSeguimiento[4].getNota3() != null) {
                                    cell.setCellValue(notasSeguimiento[4].getNota3());
                                } else {
                                    cell.setCellValue("");
                                }
                            } else {
                                cell.setCellValue("");
                            }
                        } else if (cell.getNumericCellValue() == -54) {
                            if (notasSeguimiento[4] != null) {
                                if (notasSeguimiento[4].getNota4() != null) {
                                    cell.setCellValue(notasSeguimiento[4].getNota4());
                                } else {
                                    cell.setCellValue("");
                                }
                            } else {
                                cell.setCellValue("");
                            }
                        } else if (cell.getNumericCellValue() == -50) {
                            if (notasSeguimiento[4] != null) {
                                if (notasSeguimiento[4].getNotaFinal() != null) {
                                    cell.setCellValue(notasSeguimiento[4].getNotaFinal());
                                } else {
                                    cell.setCellValue("");
                                }
                            } else {
                                cell.setCellValue("");
                            }
                        } else if (cell.getNumericCellValue() == -55) {
                            if (notasSeguimiento[4] != null) {
                                if (notasSeguimiento[4].getRecuperatorio() != null) {
                                    cell.setCellValue(notasSeguimiento[4].getRecuperatorio());
                                } else {
                                    cell.setCellValue("");
                                }
                            } else {
                                cell.setCellValue("");
                            }
                        } else if (cell.getNumericCellValue() == -61) {
                            if (notasSeguimiento[5] != null) {
                                if (notasSeguimiento[5].getNota1() != null) {
                                    cell.setCellValue(notasSeguimiento[5].getNota1());
                                } else {
                                    cell.setCellValue("");
                                }
                            } else {
                                cell.setCellValue("");
                            }
                        } else if (cell.getNumericCellValue() == -62) {
                            if (notasSeguimiento[5] != null) {
                                if (notasSeguimiento[5].getNota2() != null) {
                                    cell.setCellValue(notasSeguimiento[5].getNota2());
                                } else {
                                    cell.setCellValue("");
                                }
                            } else {
                                cell.setCellValue("");
                            }
                        } else if (cell.getNumericCellValue() == -63) {
                            if (notasSeguimiento[5] != null) {
                                if (notasSeguimiento[5].getNota3() != null) {
                                    cell.setCellValue(notasSeguimiento[5].getNota3());
                                } else {
                                    cell.setCellValue("");
                                }
                            } else {
                                cell.setCellValue("");
                            }
                        } else if (cell.getNumericCellValue() == -64) {
                            if (notasSeguimiento[5] != null) {
                                if (notasSeguimiento[5].getNota4() != null) {
                                    cell.setCellValue(notasSeguimiento[5].getNota4());
                                } else {
                                    cell.setCellValue("");
                                }
                            } else {
                                cell.setCellValue("");
                            }
                        } else if (cell.getNumericCellValue() == -60) {
                            if (notasSeguimiento[5] != null) {
                                if (notasSeguimiento[5].getNotaFinal() != null) {
                                    cell.setCellValue(notasSeguimiento[5].getNotaFinal());
                                } else {
                                    cell.setCellValue("");
                                }
                            } else {
                                cell.setCellValue("");
                            }
                        } else if (cell.getNumericCellValue() == -65) {
                            if (notasSeguimiento[5] != null) {
                                if (notasSeguimiento[5].getRecuperatorio() != null) {
                                    cell.setCellValue(notasSeguimiento[5].getRecuperatorio());
                                } else {
                                    cell.setCellValue("");
                                }
                            } else {
                                cell.setCellValue("");
                            }
                        } else if (cell.getNumericCellValue() == -71) {
                            if (notasSeguimiento[6] != null) {
                                if (notasSeguimiento[6].getNota1() != null) {
                                    cell.setCellValue(notasSeguimiento[6].getNota1());
                                } else {
                                    cell.setCellValue("");
                                }
                            } else {
                                cell.setCellValue("");
                            }
                        } else if (cell.getNumericCellValue() == -72) {
                            if (notasSeguimiento[6] != null) {
                                if (notasSeguimiento[6].getNota2() != null) {
                                    cell.setCellValue(notasSeguimiento[6].getNota2());
                                } else {
                                    cell.setCellValue("");
                                }
                            } else {
                                cell.setCellValue("");
                            }
                        } else if (cell.getNumericCellValue() == -73) {
                            if (notasSeguimiento[6] != null) {
                                if (notasSeguimiento[6].getNota3() != null) {
                                    cell.setCellValue(notasSeguimiento[6].getNota3());
                                } else {
                                    cell.setCellValue("");
                                }
                            } else {
                                cell.setCellValue("");
                            }
                        } else if (cell.getNumericCellValue() == -74) {
                            if (notasSeguimiento[6] != null) {
                                if (notasSeguimiento[6].getNota4() != null) {
                                    cell.setCellValue(notasSeguimiento[6].getNota4());
                                } else {
                                    cell.setCellValue("");
                                }
                            } else {
                                cell.setCellValue("");
                            }
                        } else if (cell.getNumericCellValue() == -70) {
                            if (notasSeguimiento[6] != null) {
                                if (notasSeguimiento[6].getNotaFinal() != null) {
                                    cell.setCellValue(notasSeguimiento[6].getNotaFinal());
                                } else {
                                    cell.setCellValue("");
                                }
                            } else {
                                cell.setCellValue("");
                            }
                        } else if (cell.getNumericCellValue() == -75) {
                            if (notasSeguimiento[6] != null) {
                                if (notasSeguimiento[6].getRecuperatorio() != null) {
                                    cell.setCellValue(notasSeguimiento[6].getRecuperatorio());
                                } else {
                                    cell.setCellValue("");
                                }
                            } else {
                                cell.setCellValue("");
                            }
                        } else if (cell.getNumericCellValue() == -81) {
                            if (notasSeguimiento[7] != null) {
                                if (notasSeguimiento[7].getNota1() != null) {
                                    cell.setCellValue(notasSeguimiento[7].getNota1());
                                } else {
                                    cell.setCellValue("");
                                }
                            } else {
                                cell.setCellValue("");
                            }
                        } else if (cell.getNumericCellValue() == -82) {
                            if (notasSeguimiento[7] != null) {
                                if (notasSeguimiento[7].getNota2() != null) {
                                    cell.setCellValue(notasSeguimiento[7].getNota2());
                                } else {
                                    cell.setCellValue("");
                                }
                            } else {
                                cell.setCellValue("");
                            }
                        } else if (cell.getNumericCellValue() == -83) {
                            if (notasSeguimiento[7] != null) {
                                if (notasSeguimiento[7].getNota3() != null) {
                                    cell.setCellValue(notasSeguimiento[7].getNota3());
                                } else {
                                    cell.setCellValue("");
                                }
                            } else {
                                cell.setCellValue("");
                            }
                        } else if (cell.getNumericCellValue() == -84) {
                            if (notasSeguimiento[7] != null) {
                                if (notasSeguimiento[7].getNota4() != null) {
                                    cell.setCellValue(notasSeguimiento[7].getNota4());
                                } else {
                                    cell.setCellValue("");
                                }
                            } else {
                                cell.setCellValue("");
                            }
                        } else if (cell.getNumericCellValue() == -80) {
                            if (notasSeguimiento[7] != null) {
                                if (notasSeguimiento[7].getNotaFinal() != null) {
                                    cell.setCellValue(notasSeguimiento[7].getNotaFinal());
                                } else {
                                    cell.setCellValue("");
                                }
                            } else {
                                cell.setCellValue("");
                            }
                        } else if (cell.getNumericCellValue() == -85) {
                            if (notasSeguimiento[7] != null) {
                                if (notasSeguimiento[7].getRecuperatorio() != null) {
                                    cell.setCellValue(notasSeguimiento[7].getRecuperatorio());
                                } else {
                                    cell.setCellValue("");
                                }
                            } else {
                                cell.setCellValue("");
                            }
                        }
                    } else if (cell.getCellTypeEnum() == CellType.STRING) {
                        if (cell.getStringCellValue().contains("<<ESTUDIANTE>>")) {
                            cell.setCellValue(cell.getStringCellValue().replace("<<ESTUDIANTE>>", estudianteSeguimiento.getEstudiante()));
                        }
                    }
                }
            }

        }

        String name = seleccionGestionAcademica.toString() + " - " + seleccionCarrera.getNombre() + " - " + seleccionNivel.toString() + " - " + seleccionTurno.toString() + " - " + seleccionParalelo;
        descargarArchivo(workbook, name);
        
        //log
        logFacade.create(new Log(Fecha.getDate(), EventoLog.READ, "Generación reporte seguimiento académico", loginController.getUsr().toString()));
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
     * @return the seleccionParalelo
     */
    public String getSeleccionParalelo() {
        return seleccionParalelo;
    }

    /**
     * @param seleccionParalelo the seleccionParalelo to set
     */
    public void setSeleccionParalelo(String seleccionParalelo) {
        this.seleccionParalelo = seleccionParalelo;
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
}
