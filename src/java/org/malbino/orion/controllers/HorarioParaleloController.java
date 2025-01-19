/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.malbino.orion.controllers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.malbino.orion.entities.Aula;
import org.malbino.orion.entities.Carrera;
import org.malbino.orion.entities.Clase;
import org.malbino.orion.entities.GestionAcademica;
import org.malbino.orion.entities.Grupo;
import org.malbino.orion.entities.Log;
import org.malbino.orion.entities.Periodo;
import org.malbino.orion.enums.Dia;
import org.malbino.orion.enums.EntidadLog;
import org.malbino.orion.enums.EventoLog;
import org.malbino.orion.enums.Nivel;
import org.malbino.orion.enums.Turno;
import org.malbino.orion.facades.AulaFacade;
import org.malbino.orion.facades.ClaseFacade;
import org.malbino.orion.facades.GrupoFacade;
import org.malbino.orion.facades.PeriodoFacade;
import org.malbino.orion.util.Fecha;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Tincho
 */
@Named("HorarioParaleloController")
@SessionScoped
public class HorarioParaleloController extends AbstractController implements Serializable {

    private static final Logger log = LoggerFactory.getLogger(HorarioParaleloController.class);

    @EJB
    GrupoFacade grupoFacade;
    @EJB
    PeriodoFacade periodoFacade;
    @EJB
    AulaFacade aulaFacade;
    @EJB
    ClaseFacade claseFacade;

    @Inject
    LoginController loginController;

    private GestionAcademica seleccionGestionAcademica;
    private Carrera seleccionCarrera;
    private Nivel seleccionNivel;
    private Turno seleccionTurno;
    private String seleccionParalelo;

    private List<Clase> horario;
    private Clase seleccionClase;

    @PostConstruct
    public void init() {
        seleccionGestionAcademica = null;
        seleccionCarrera = null;
        seleccionNivel = null;
        seleccionParalelo = null;

        horario = new ArrayList();
        seleccionClase = null;
    }

    public void reinit() {
        seleccionGestionAcademica = null;
        seleccionCarrera = null;
        seleccionNivel = null;
        seleccionParalelo = null;

        horario = new ArrayList();
        seleccionClase = null;
    }

    @Override
    public List<Carrera> listaCarreras() {
        List<Carrera> l = new ArrayList();
        if (seleccionGestionAcademica != null) {
            l = carreraFacade.listaCarreras(seleccionGestionAcademica.getRegimen());
        }
        return l;
    }

    public Nivel[] listaNiveles() {
        Nivel[] niveles = new Nivel[0];
        if (seleccionGestionAcademica != null && seleccionCarrera != null) {
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

    public List<String> listaParalelos() {
        List<String> paralelos = new ArrayList<>();
        if (seleccionGestionAcademica != null && seleccionCarrera != null && seleccionNivel != null && seleccionTurno != null) {
            paralelos = grupoFacade.listaParalelos(seleccionGestionAcademica.getId_gestionacademica(), seleccionCarrera.getId_carrera(), seleccionNivel, seleccionTurno);
        }
        return paralelos;
    }

    public List<Grupo> listaGrupos() {
        List<Grupo> grupos = new ArrayList<>();
        if (seleccionGestionAcademica != null && seleccionCarrera != null && seleccionNivel != null && seleccionTurno != null && seleccionParalelo != null) {
            grupos = grupoFacade.listaGrupos(seleccionGestionAcademica.getId_gestionacademica(), seleccionCarrera.getId_carrera(), seleccionNivel, seleccionTurno, seleccionParalelo);
        }
        return grupos;
    }

    public List<Aula> listaAulas() {
        List<Aula> aulas = new ArrayList<>();
        if (seleccionGestionAcademica != null && seleccionCarrera != null) {
            aulas = aulaFacade.listaAulas(seleccionCarrera.getCampus().getId_campus());
        }
        return aulas;
    }

    public void cargarHorario() {
        horario = new ArrayList<>();

        Clase claseVacia = new Clase("<br/>");
        horario.add(claseVacia);

        Dia[] dias = Dia.values();
        for (int j = 0; j < dias.length; j++) {
            Dia dia = dias[j];

            Clase claseDia = new Clase(dia.name());
            horario.add(claseDia);
        }

        List<Periodo> periodos = periodoFacade.listaPeriodos();
        for (int i = 0; i < periodos.size(); i++) {
            Periodo periodo = periodos.get(i);

            Clase clasePeriodo = new Clase(periodo.toHtml());
            horario.add(clasePeriodo);

            for (int j = 0; j < dias.length; j++) {
                Dia dia = dias[j];

                Clase clase = claseFacade.buscar(seleccionGestionAcademica.getId_gestionacademica(), seleccionCarrera.getId_carrera(), seleccionNivel, seleccionTurno, seleccionParalelo, periodo, dia);
                if (clase != null) {
                    horario.add(clase);
                } else {
                    Clase claseDia = new Clase(periodo, dia);
                    horario.add(claseDia);
                }
            }
        }
    }

    public void copiarClase() {
        Periodo periodoAnterior = periodoFacade.buscar(seleccionClase.getPeriodo().getInicio());
        if (periodoAnterior != null) {
            Clase claseAnterior = claseFacade.buscar(seleccionGestionAcademica.getId_gestionacademica(), seleccionCarrera.getId_carrera(), seleccionNivel, seleccionTurno, seleccionParalelo, periodoAnterior, seleccionClase.getDia());
            if (claseAnterior != null) {
                seleccionClase.setAula(claseAnterior.getAula());
                seleccionClase.setGrupo(claseAnterior.getGrupo());

                if (seleccionClase.getId_clase() == null) {
                    Clase claseAula = claseFacade.buscar(seleccionClase.getPeriodo(), seleccionClase.getDia(), seleccionClase.getAula());
                    if (claseAula == null) {
                        Clase claseEmpleado = claseFacade.buscar(seleccionClase.getPeriodo(), seleccionClase.getDia(), seleccionClase.getGrupo().getEmpleado());
                        if (claseEmpleado == null) {
                            if (claseFacade.edit(seleccionClase)) {
                                //log
                                logFacade.create(new Log(Fecha.getDate(), EventoLog.UPDATE, EntidadLog.CLASE, seleccionClase.getId_clase(), "Actualización clase", loginController.getUsr().toString()));

                                this.ejecutar("cargarHorario()");
                            } else {
                                this.mensajeDeError("No se pudo editar la clase.");
                            }
                        } else {
                            this.ejecutar("cargarHorario()");
                            this.mensajeDeError("¡Docente no disponible!"
                                    + claseEmpleado.getGrupo().toHtml_Horario());
                            this.actualizar("form:messages");
                        }
                    } else {
                        this.ejecutar("cargarHorario()");
                        this.mensajeDeError("¡Aula no disponible!"
                                + claseAula.getGrupo().toHtml_Horario());
                        this.actualizar("form:messages");
                    }
                } else {
                    Clase claseAula = claseFacade.buscar(seleccionClase.getPeriodo(), seleccionClase.getDia(), seleccionClase.getAula(), seleccionClase.getId_clase());
                    if (claseAula == null) {
                        Clase claseEmpleado = claseFacade.buscar(seleccionClase.getPeriodo(), seleccionClase.getDia(), seleccionClase.getGrupo().getEmpleado(), seleccionClase.getId_clase());
                        if (claseEmpleado == null) {
                            if (claseFacade.edit(seleccionClase)) {
                                //log
                                logFacade.create(new Log(Fecha.getDate(), EventoLog.UPDATE, EntidadLog.CLASE, seleccionClase.getId_clase(), "Actualización clase", loginController.getUsr().toString()));

                                this.ejecutar("cargarHorario()");
                            } else {
                                this.mensajeDeError("No se pudo editar la clase.");
                            }
                        } else {
                            this.ejecutar("cargarHorario()");
                            this.mensajeDeError("¡Docente no disponible!"
                                    + claseEmpleado.getGrupo().toHtml_Horario());
                            this.actualizar("form:messages");
                        }
                    } else {
                        this.ejecutar("cargarHorario()");
                        this.mensajeDeError("¡Aula no disponible!"
                                + claseAula.getGrupo().toHtml_Horario());
                        this.actualizar("form:messages");
                    }
                }
            }
        }
    }

    public void editarClase() {
        if (seleccionClase.getId_clase() == null) {
            Clase claseAula = claseFacade.buscar(seleccionClase.getPeriodo(), seleccionClase.getDia(), seleccionClase.getAula());
            if (claseAula == null) {
                Clase claseEmpleado = claseFacade.buscar(seleccionClase.getPeriodo(), seleccionClase.getDia(), seleccionClase.getGrupo().getEmpleado());
                if (claseEmpleado == null) {
                    if (claseFacade.edit(seleccionClase)) {
                        //log
                        logFacade.create(new Log(Fecha.getDate(), EventoLog.UPDATE, EntidadLog.CLASE, seleccionClase.getId_clase(), "Actualización clase", loginController.getUsr().toString()));

                        this.ejecutar("PF('dlg1').hide()");
                        this.ejecutar("cargarHorario()");
                    } else {
                        this.mensajeDeError("No se pudo editar la clase.");
                    }
                } else {
                    this.ejecutar("cargarHorario()");
                    this.mensajeDeError("¡Docente no disponible!"
                            + claseEmpleado.getGrupo().toHtml_Horario());
                    this.actualizar("form:messages");
                }
            } else {
                this.ejecutar("cargarHorario()");
                this.mensajeDeError("¡Aula no disponible!"
                        + claseAula.getGrupo().toHtml_Horario());
                this.actualizar("form:messages");
            }
        } else {
            Clase claseAula = claseFacade.buscar(seleccionClase.getPeriodo(), seleccionClase.getDia(), seleccionClase.getAula(), seleccionClase.getId_clase());
            if (claseAula == null) {
                Clase claseEmpleado = claseFacade.buscar(seleccionClase.getPeriodo(), seleccionClase.getDia(), seleccionClase.getGrupo().getEmpleado(), seleccionClase.getId_clase());
                if (claseEmpleado == null) {
                    if (claseFacade.edit(seleccionClase)) {
                        //log
                        logFacade.create(new Log(Fecha.getDate(), EventoLog.UPDATE, EntidadLog.CLASE, seleccionClase.getId_clase(), "Actualización clase", loginController.getUsr().toString()));

                        this.ejecutar("PF('dlg1').hide()");
                        this.ejecutar("cargarHorario()");
                    } else {
                        this.mensajeDeError("No se pudo editar la clase.");
                    }
                } else {
                    this.ejecutar("cargarHorario()");
                    this.mensajeDeError("¡Docente no disponible!"
                            + claseEmpleado.getGrupo().toHtml_Horario());
                }
            } else {
                this.ejecutar("cargarHorario()");
                this.mensajeDeError("¡Aula no disponible!"
                        + claseAula.getGrupo().toHtml_Horario());
            }
        }
    }

    public void cancelarClase() {
        this.ejecutar("PF('dlg1').hide()");
        this.ejecutar("cargarHorario()");
    }

    public void eliminarClase() {
        if (claseFacade.remove(seleccionClase)) {
            //log
            logFacade.create(new Log(Fecha.getDate(), EventoLog.DELETE, EntidadLog.CLASE, seleccionClase.getId_clase(), "Borrado clase", loginController.getUsr().toString()));

            this.ejecutar("cargarHorario()");
        } else {
            this.mensajeDeError("No se pudo eliminar la clase.");
        }
    }

    public void toEditarClase() {
        this.actualizar("form_dlg1");
        this.ejecutar("PF('dlg1').show()");
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
     * @return the horario
     */
    public List<Clase> getHorario() {
        return horario;
    }

    /**
     * @param horario the horario to set
     */
    public void setHorario(List<Clase> horario) {
        this.horario = horario;
    }

    /**
     * @return the seleccionClase
     */
    public Clase getSeleccionClase() {
        return seleccionClase;
    }

    /**
     * @param seleccionClase the seleccionClase to set
     */
    public void setSeleccionClase(Clase seleccionClase) {
        this.seleccionClase = seleccionClase;
    }

}
