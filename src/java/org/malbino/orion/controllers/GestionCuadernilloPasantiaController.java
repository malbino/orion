/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.malbino.orion.controllers;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.util.ArrayList;
import javax.inject.Inject;
import org.malbino.orion.entities.Asistencia;
import org.malbino.orion.entities.NotaPasantia;
import org.malbino.orion.facades.AsistenciaFacade;
import org.malbino.orion.facades.NotaPasantiaFacade;
import org.malbino.orion.facades.UsuarioFacade;
import org.malbino.orion.util.Redondeo;

/**
 *
 * @author Tincho
 */
@Named("GestionCuadernilloPasantiaController")
@SessionScoped
public class GestionCuadernilloPasantiaController extends AbstractController implements Serializable {

    @EJB
    AsistenciaFacade asistenciaFacade;
    @EJB
    UsuarioFacade usuarioFacade;
    @EJB
    NotaPasantiaFacade notaPasantiaFacade;
    @Inject
    LoginController loginController;

    private NotaPasantia seleccionNotaPasantia;
    private List<Asistencia> asistencias;
    private Asistencia nuevoAsistencia;
    private Asistencia seleccionAsistencia;

    private Boolean filter;
    private String keyword;

    @PostConstruct
    public void init() {
        seleccionNotaPasantia = null;
        asistencias = new ArrayList<>();
        nuevoAsistencia = new Asistencia();
        seleccionAsistencia = null;

        filter = false;
        keyword = null;
    }

    public void reinit() {
        asistencias = asistenciaFacade.listaAsistencias(seleccionNotaPasantia);
        nuevoAsistencia = new Asistencia();
        seleccionAsistencia = null;

        filter = false;
        keyword = null;
    }

    public void filtro() {
        if (filter) {
            filter = false;
            keyword = null;

            asistencias = asistenciaFacade.listaAsistencias(seleccionNotaPasantia);
        } else {
            filter = true;
            keyword = null;
        }
    }

    public void buscar() {
        if (seleccionNotaPasantia != null) {
            asistencias = asistenciaFacade.buscar(seleccionNotaPasantia, keyword);
        }
    }

    public List<NotaPasantia> listaNotaPasantias() {
        List<NotaPasantia> l = new ArrayList<>();
        if (loginController.getUsr() != null) {
            l = notaPasantiaFacade.listaNotaPasantiasEstudiante(loginController.getUsr().getId_persona());
        }
        return l;
    }

    public double total() {
        double total = 0.0;
        for (Asistencia asistencia : asistencias) {
            total += asistencia.horas();
        }
        return Redondeo.redondear_HALFUP(total, 2);
    }

    public void crearAsistencia() throws IOException {
        if (asistenciaFacade.buscarAsistencia(nuevoAsistencia.getIngreso(), nuevoAsistencia.getSalida(), seleccionNotaPasantia) == null) {
            nuevoAsistencia.setNotaPasantia(seleccionNotaPasantia);
            if (asistenciaFacade.create(nuevoAsistencia)) {
                this.toGestionCuadernilloPasantia();
            }
        } else {
            this.mensajeDeError("Asistencia repetida.");
        }
    }

    public void editarAsistencia() throws IOException {
        if (asistenciaFacade.buscarAsistencia(seleccionAsistencia.getIngreso(), seleccionAsistencia.getSalida(), seleccionNotaPasantia, seleccionAsistencia.getId_asistencia()) == null) {
            if (asistenciaFacade.edit(seleccionAsistencia)) {
                this.toGestionCuadernilloPasantia();
            }
        } else {
            this.mensajeDeError("Asistencia repetida.");
        }
    }

    public void toNuevaAsistencia() throws IOException {
        this.redireccionarViewId("/estudiante/cuadernilloPasantia/nuevaAsistencia");
    }

    public void toEditarAsistencia() throws IOException {
        this.redireccionarViewId("/estudiante/cuadernilloPasantia/editarAsistencia");
    }

    public void toCuadernilloPasantia() throws IOException {
        this.insertarParametro("id_notapasantia", seleccionNotaPasantia.getId_notapasantia());

        this.redireccionarViewId("/estudiante/cuadernilloPasantia/cuadernilloPasantia");
    }

    public void toGestionCuadernilloPasantia() throws IOException {
        reinit();

        this.redireccionarViewId("/estudiante/cuadernilloPasantia/gestionCuadernilloPasantia");
    }

    /**
     * @return the seleccionNotaPasantia
     */
    public NotaPasantia getSeleccionNotaPasantia() {
        return seleccionNotaPasantia;
    }

    /**
     * @param seleccionNotaPasantia the seleccionNotaPasantia to set
     */
    public void setSeleccionNotaPasantia(NotaPasantia seleccionNotaPasantia) {
        this.seleccionNotaPasantia = seleccionNotaPasantia;
    }

    /**
     * @return the asistencias
     */
    public List<Asistencia> getAsistencias() {
        return asistencias;
    }

    /**
     * @param asistencias the asistencias to set
     */
    public void setAsistencias(List<Asistencia> asistencias) {
        this.asistencias = asistencias;
    }

    /**
     * @return the nuevoAsistencia
     */
    public Asistencia getNuevoAsistencia() {
        return nuevoAsistencia;
    }

    /**
     * @param nuevoAsistencia the nuevoAsistencia to set
     */
    public void setNuevoAsistencia(Asistencia nuevoAsistencia) {
        this.nuevoAsistencia = nuevoAsistencia;
    }

    /**
     * @return the seleccionAsistencia
     */
    public Asistencia getSeleccionAsistencia() {
        return seleccionAsistencia;
    }

    /**
     * @param seleccionAsistencia the seleccionAsistencia to set
     */
    public void setSeleccionAsistencia(Asistencia seleccionAsistencia) {
        this.seleccionAsistencia = seleccionAsistencia;
    }

    /**
     * @return the filter
     */
    public Boolean getFilter() {
        return filter;
    }

    /**
     * @param filter the filter to set
     */
    public void setFilter(Boolean filter) {
        this.filter = filter;
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

}
