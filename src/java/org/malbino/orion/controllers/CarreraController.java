/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.malbino.orion.controllers;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.malbino.orion.entities.Campus;
import org.malbino.orion.entities.Carrera;
import org.malbino.orion.entities.Log;
import org.malbino.orion.enums.EntidadLog;
import org.malbino.orion.enums.EventoLog;
import org.malbino.orion.util.Fecha;

/**
 *
 * @author Tincho
 */
@Named("CarreraController")
@SessionScoped
public class CarreraController extends AbstractController implements Serializable {

    @Inject
    LoginController loginController;

    private List<Carrera> carreras;
    private Carrera nuevaCarrera;
    private Carrera seleccionCarrera;
    private Campus seleccionCampus;

    private String keyword;

    @PostConstruct
    public void init() {
        carreras = carreraFacade.listaCarreras();
        nuevaCarrera = new Carrera();
        seleccionCarrera = null;

        keyword = null;
    }

    public void reinit() {
        if (seleccionCampus == null) {
            carreras = carreraFacade.listaCarreras();
        } else {
            carreras = carreraFacade.listaCarreras(seleccionCampus.getId_campus());
        }
        nuevaCarrera = new Carrera();
        seleccionCarrera = null;

        keyword = null;
    }

    public void buscar() {
        if (seleccionCampus == null) {
            carreras = carreraFacade.buscar(keyword);
        } else {
            carreras = carreraFacade.buscar(keyword, seleccionCampus.getId_campus());
        }
    }

    public void crearCarrera() throws IOException {
        if (carreraFacade.buscarPorCodigo(nuevaCarrera.getCodigo()) == null) {
            if (carreraFacade.create(nuevaCarrera)) {
                //log
                logFacade.create(new Log(Fecha.getDate(), EventoLog.CREATE, EntidadLog.CARRERA, nuevaCarrera.getId_carrera(), "Creación carrera", loginController.getUsr().toString()));

                this.toCarreras();
            }
        } else {
            this.mensajeDeError("Carrera repetida.");
        }
    }

    public void editarCarrera() throws IOException {
        if (carreraFacade.buscarPorCodigo(seleccionCarrera.getCodigo(), seleccionCarrera.getId_carrera()) == null) {
            if (carreraFacade.edit(seleccionCarrera)) {
                //log
                logFacade.create(new Log(Fecha.getDate(), EventoLog.UPDATE, EntidadLog.CARRERA, seleccionCarrera.getId_carrera(), "Actualización carrera", loginController.getUsr().toString()));

                this.toCarreras();
            }
        } else {
            this.mensajeDeError("Carrera repetido.");
        }
    }

    public void toNuevaCarrera() throws IOException {
        this.redireccionarViewId("/planesEstudio/carrera/nuevaCarrera");
    }

    public void toEditarCarrera() throws IOException {
        this.redireccionarViewId("/planesEstudio/carrera/editarCarrera");
    }

    public void toCarreras() throws IOException {
        reinit();

        this.redireccionarViewId("/planesEstudio/carrera/carreras");
    }

    /**
     * @return the carreras
     */
    public List<Carrera> getCarreras() {
        return carreras;
    }

    /**
     * @param carreras the carreras to set
     */
    public void setCarreras(List<Carrera> carreras) {
        this.carreras = carreras;
    }

    /**
     * @return the nuevaCarrera
     */
    public Carrera getNuevaCarrera() {
        return nuevaCarrera;
    }

    /**
     * @param nuevaCarrera the nuevaCarrera to set
     */
    public void setNuevaCarrera(Carrera nuevaCarrera) {
        this.nuevaCarrera = nuevaCarrera;
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

    /**
     * @return the seleccionCampus
     */
    public Campus getSeleccionCampus() {
        return seleccionCampus;
    }

    /**
     * @param seleccionCampus the seleccionCampus to set
     */
    public void setSeleccionCampus(Campus seleccionCampus) {
        this.seleccionCampus = seleccionCampus;
    }
}
