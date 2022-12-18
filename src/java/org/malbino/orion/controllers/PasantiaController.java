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
import javax.inject.Named;
import org.malbino.orion.entities.Carrera;
import org.malbino.orion.entities.Pasantia;
import org.malbino.orion.entities.Mencion;
import org.malbino.orion.enums.Nivel;
import org.malbino.orion.facades.PasantiaFacade;
import org.malbino.orion.facades.MencionFacade;

/**
 *
 * @author Tincho
 */
@Named("PasantiaController")
@SessionScoped
public class PasantiaController extends AbstractController implements Serializable {

    @EJB
    PasantiaFacade pasantiaFacade;
    @EJB
    MencionFacade mencionFacade;

    private List<Pasantia> pasantias;
    private Pasantia nuevaPasantia;
    private Pasantia seleccionPasantia;
    private Carrera seleccionCarrera;

    private String keyword;

    @PostConstruct
    public void init() {
        pasantias = new ArrayList();
        nuevaPasantia = new Pasantia();
        seleccionPasantia = null;

        keyword = null;
    }

    public void reinit() {
        if (seleccionCarrera != null) {
            pasantias = pasantiaFacade.listaPasantias(seleccionCarrera);
        }
        nuevaPasantia = new Pasantia();
        seleccionPasantia = null;

        keyword = null;
    }

    public void buscar() {
        if (seleccionCarrera != null) {
            pasantias = pasantiaFacade.buscar(keyword, seleccionCarrera.getId_carrera());
        }
    }

    public Nivel[] listaNiveles() {
        return Nivel.values(seleccionCarrera.getRegimen());
    }

    public List<Mencion> listaMenciones() {
        return mencionFacade.listaMenciones(seleccionCarrera.getId_carrera());
    }

    public List<Pasantia> listaPasantiasCrear() {
        return pasantiaFacade.listaPasantias(seleccionCarrera, nuevaPasantia.getMencion());
    }

    public List<Pasantia> listaPasantiasEditar() {
        return pasantiaFacade.listaPasantias(seleccionPasantia.getCarrera(), seleccionPasantia.getMencion(), seleccionPasantia.getId_pasantia());
    }

    public void crearPasantia() throws IOException {
        nuevaPasantia.setCarrera(seleccionCarrera);
        if (pasantiaFacade.buscarPorCodigo(nuevaPasantia.getCodigo(), nuevaPasantia.getCarrera(), nuevaPasantia.getMencion()).isEmpty()) {
            if (pasantiaFacade.create(nuevaPasantia)) {
                this.toPasantias();
            }
        } else {
            this.mensajeDeError("Pasantia repetida.");
        }
    }

    public void editarPasantia() throws IOException {
        if (pasantiaFacade.buscarPorCodigo(seleccionPasantia.getCodigo(), seleccionPasantia.getId_pasantia(), seleccionPasantia.getCarrera(), seleccionPasantia.getMencion()).isEmpty()) {
            if (pasantiaFacade.edit(seleccionPasantia)) {
                this.toPasantias();
            }
        } else {
            this.mensajeDeError("Pasantia repetida.");
        }
    }

    public void toNuevaPasantia() throws IOException {
        this.redireccionarViewId("/planesEstudio/pasantia/nuevaPasantia");
    }

    public void toEditarPasantia() throws IOException {
        this.redireccionarViewId("/planesEstudio/pasantia/editarPasantia");
    }

    public void toPasantias() throws IOException {
        reinit();

        this.redireccionarViewId("/planesEstudio/pasantia/pasantias");
    }

    /**
     * @return the pasantias
     */
    public List<Pasantia> getPasantias() {
        return pasantias;
    }

    /**
     * @param pasantias the pasantias to set
     */
    public void setPasantias(List<Pasantia> pasantias) {
        this.pasantias = pasantias;
    }

    /**
     * @return the nuevaPasantia
     */
    public Pasantia getNuevaPasantia() {
        return nuevaPasantia;
    }

    /**
     * @param nuevaPasantia the nuevaPasantia to set
     */
    public void setNuevaPasantia(Pasantia nuevaPasantia) {
        this.nuevaPasantia = nuevaPasantia;
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
