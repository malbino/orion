/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.malbino.orion.controllers;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.malbino.orion.entities.CarreraEstudiante;
import org.malbino.orion.entities.Empresa;
import org.malbino.orion.entities.Estudiante;
import org.malbino.orion.entities.GestionAcademica;
import org.malbino.orion.entities.GrupoPasantia;
import org.malbino.orion.entities.Log;
import org.malbino.orion.entities.NotaPasantia;
import org.malbino.orion.entities.Pasantia;
import org.malbino.orion.enums.EntidadLog;
import org.malbino.orion.enums.EventoLog;
import org.malbino.orion.facades.CarreraEstudianteFacade;
import org.malbino.orion.facades.GrupoPasantiaFacade;
import org.malbino.orion.facades.NotaPasantiaFacade;
import org.malbino.orion.facades.negocio.PasantiaEstudianteFacade;
import org.malbino.orion.util.Fecha;

/**
 *
 * @author Tincho
 */
@Named("InscripcionPasantiaController")
@SessionScoped
public class InscripcionPasantiaController extends AbstractController implements Serializable {

    @EJB
    CarreraEstudianteFacade carreraEstudianteFacade;
    @EJB
    GrupoPasantiaFacade grupoPasantiaFacade;
    @EJB
    PasantiaEstudianteFacade pasantiaEstudianteFacade;
    @EJB
    NotaPasantiaFacade notaPasantiaFacade;
    @Inject
    LoginController loginController;

    private Date seleccionFecha;
    private Estudiante seleccionEstudiante;
    private CarreraEstudiante seleccionCarreraEstudiante;
    private GestionAcademica seleccionGestionAcademica;

    private Pasantia seleccionPasantia;
    private GrupoPasantia seleccionGrupoPasantia;
    private Empresa seleccionEmpresa;

    private String horario;

    @PostConstruct
    public void init() {
        seleccionFecha = null;
        seleccionEstudiante = null;
        seleccionCarreraEstudiante = null;
        seleccionGestionAcademica = null;

        seleccionPasantia = null;
        seleccionGrupoPasantia = null;
        seleccionEmpresa = null;
        
        horario = null;
    }

    public void reinit() {
        seleccionFecha = null;
        seleccionEstudiante = null;
        seleccionCarreraEstudiante = null;
        seleccionGestionAcademica = null;

        seleccionPasantia = null;
        seleccionGrupoPasantia = null;
        seleccionEmpresa = null;
        
        horario = null;
    }

    @Override
    public List<Estudiante> completarEstudiante(String consulta) {
        List<Estudiante> estudiantes = estudianteFacade.findAll();
        List<Estudiante> estudiantesFiltrados = new ArrayList();

        for (Estudiante e : estudiantes) {
            if (e.toString().toLowerCase().contains(consulta.toLowerCase())) {
                estudiantesFiltrados.add(e);
            }
        }

        return estudiantesFiltrados;
    }

    public List<CarreraEstudiante> listaCarrerasEstudiante() {
        List<CarreraEstudiante> l = new ArrayList();
        if (seleccionEstudiante != null) {
            l = carreraEstudianteFacade.listaCarrerasEstudiante(seleccionEstudiante.getId_persona());
        }
        return l;
    }

    @Override
    public List<GestionAcademica> listaGestionesAcademicas() {
        List<GestionAcademica> l = new ArrayList();
        if (seleccionCarreraEstudiante != null) {
            l = gestionAcademicaFacade.listaGestionAcademica(seleccionCarreraEstudiante.getCarrera().getRegimen(), Boolean.TRUE);
        }
        return l;
    }

    public List<Pasantia> listaPasantias() {
        List<Pasantia> l = new ArrayList<>();
        if (seleccionCarreraEstudiante != null) {
            l = pasantiaEstudianteFacade.oferta(seleccionCarreraEstudiante.getCarrera(), seleccionEstudiante);
        }
        return l;
    }

    public List<GrupoPasantia> listaGruposPasantias() {
        List<GrupoPasantia> l = new ArrayList<>();
        if (seleccionGestionAcademica != null && seleccionCarreraEstudiante != null && seleccionPasantia != null) {
            l = grupoPasantiaFacade.listaGrupoPasantiasAbiertos(seleccionGestionAcademica.getId_gestionacademica(), seleccionCarreraEstudiante.getCarrera().getId_carrera(), seleccionPasantia.getId_pasantia());
        }
        return l;
    }

    public void registrarPasantia() throws IOException {
        if (notaPasantiaFacade.buscarNotaPasantia(seleccionEstudiante, seleccionGrupoPasantia, seleccionEmpresa) == null) {
            if (seleccionEstudiante.getDiplomaBachiller()) {
                NotaPasantia notaPasantia = pasantiaEstudianteFacade.registrarPasantia(seleccionFecha, seleccionEstudiante, seleccionGrupoPasantia, seleccionCarreraEstudiante, seleccionEmpresa, horario);
                if (notaPasantia != null) {
                    //log
                    logFacade.create(new Log(Fecha.getDate(), EventoLog.CREATE, EntidadLog.NOTA_PASANTIA, notaPasantia.getId_notapasantia(), "Creación nota pasantía por inscripción a pasantía", loginController.getUsr().toString()));

                    this.insertarParametro("id_notapasantia", notaPasantia.getId_notapasantia());

                    reinit();

                    this.toFichaInscripcion();
                } else {
                    this.mensajeDeError("No se pudo registrar la pasantia.");
                }
            } else {
                this.mensajeDeError("Estudiante sin titulo de bachiller.");
            }
        } else {
            this.mensajeDeError("Pasantia repetida.");
        }
    }

    public void toInscripcionPasantia() throws IOException {
        this.redireccionarViewId("/pasantias/inscripcionPasantia/inscripcionPasantia");
    }

    public void toFichaInscripcion() throws IOException {
        this.redireccionarViewId("/pasantias/inscripcionPasantia/fichaInscripcion");
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
     * @return the seleccionPasantia
     */
    public Pasantia getSeleccionPasantia() {
        return seleccionPasantia;
    }

    /**
     * @param seleccionPasantia the seleccionPasantia to set
     */
    public void setSeleccionPasantia(Pasantia seleccionPasantia) {
        this.seleccionPasantia = seleccionPasantia;
    }

    /**
     * @return the seleccionGrupoPasantia
     */
    public GrupoPasantia getSeleccionGrupoPasantia() {
        return seleccionGrupoPasantia;
    }

    /**
     * @param seleccionGrupoPasantia the seleccionGrupoPasantia to set
     */
    public void setSeleccionGrupoPasantia(GrupoPasantia seleccionGrupoPasantia) {
        this.seleccionGrupoPasantia = seleccionGrupoPasantia;
    }

    /**
     * @return the seleccionEmpresa
     */
    public Empresa getSeleccionEmpresa() {
        return seleccionEmpresa;
    }

    /**
     * @param seleccionEmpresa the seleccionEmpresa to set
     */
    public void setSeleccionEmpresa(Empresa seleccionEmpresa) {
        this.seleccionEmpresa = seleccionEmpresa;
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

    /**
     * @return the horario
     */
    public String getHorario() {
        return horario;
    }

    /**
     * @param horario the horario to set
     */
    public void setHorario(String horario) {
        this.horario = horario;
    }

}
