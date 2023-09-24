/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.malbino.orion.controllers;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.malbino.orion.entities.Carrera;
import org.malbino.orion.entities.Log;
import org.malbino.orion.entities.Mencion;
import org.malbino.orion.enums.EntidadLog;
import org.malbino.orion.enums.EventoLog;
import org.malbino.orion.facades.MencionFacade;
import org.malbino.orion.util.Fecha;

/**
 *
 * @author Tincho
 */
@Named("MencionController")
@SessionScoped
public class MencionController extends AbstractController implements Serializable {

    @EJB
    MencionFacade mencionFacade;
    @Inject
    LoginController loginController;

    private List<Mencion> menciones;
    private Mencion nuevaMencion;
    private Mencion seleccionMencion;
    private Carrera seleccionCarrera;

    private String keyword;

    @PostConstruct
    public void init() {
        menciones = new ArrayList();
        nuevaMencion = new Mencion();
        seleccionMencion = null;

        keyword = null;
    }

    public void reinit() {
        if (seleccionCarrera != null) {
            menciones = mencionFacade.listaMenciones(seleccionCarrera.getId_carrera());
        }
        nuevaMencion = new Mencion();
        seleccionMencion = null;

        keyword = null;
    }

    public void buscar() {
        if (seleccionCarrera != null) {
            menciones = mencionFacade.buscar(keyword, seleccionCarrera.getId_carrera());
        }
    }

    public List<Mencion> listaMenciones() {
        return mencionFacade.listaMenciones(seleccionCarrera.getId_carrera());
    }

    public void crearMencion() throws IOException {
        nuevaMencion.setCarrera(seleccionCarrera);
        if (mencionFacade.buscarPorCodigo(nuevaMencion.getCodigo(), nuevaMencion.getCarrera().getId_carrera()) == null) {
            if (mencionFacade.create(nuevaMencion)) {
                //log
                logFacade.create(new Log(Fecha.getDate(), EventoLog.CREATE, EntidadLog.MENCION, nuevaMencion.getId_mencion(), "Creación mención", loginController.getUsr().toString()));

                this.toMenciones();
            }
        } else {
            this.mensajeDeError("Mencion repetida.");
        }
    }

    public void editarMencion() throws IOException {
        if (mencionFacade.buscarPorCodigo(seleccionMencion.getCodigo(), seleccionMencion.getId_mencion(), seleccionMencion.getCarrera().getId_carrera()) == null) {
            if (mencionFacade.edit(seleccionMencion)) {
                //log
                logFacade.create(new Log(Fecha.getDate(), EventoLog.UPDATE, EntidadLog.MENCION, seleccionMencion.getId_mencion(), "Actualización mención", loginController.getUsr().toString()));

                this.toMenciones();
            }
        } else {
            this.mensajeDeError("Mencion repetida.");
        }
    }

    public void toNuevaMencion() throws IOException {
        this.redireccionarViewId("/planesEstudio/mencion/nuevaMencion");
    }

    public void toEditarMencion() throws IOException {
        this.redireccionarViewId("/planesEstudio/mencion/editarMencion");
    }

    public void toMenciones() throws IOException {
        reinit();

        this.redireccionarViewId("/planesEstudio/mencion/menciones");
    }

    /**
     * @return the menciones
     */
    public List<Mencion> getMenciones() {
        return menciones;
    }

    /**
     * @param menciones the menciones to set
     */
    public void setMenciones(List<Mencion> menciones) {
        this.menciones = menciones;
    }

    /**
     * @return the nuevaMencion
     */
    public Mencion getNuevaMencion() {
        return nuevaMencion;
    }

    /**
     * @param nuevaMencion the nuevaMencion to set
     */
    public void setNuevaMencion(Mencion nuevaMencion) {
        this.nuevaMencion = nuevaMencion;
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
