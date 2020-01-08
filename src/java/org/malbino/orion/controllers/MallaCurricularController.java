/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.malbino.orion.controllers;

import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import org.malbino.orion.entities.Carrera;

/**
 *
 * @author Tincho
 */
@Named("MallaCurricularController")
@SessionScoped
public class MallaCurricularController extends AbstractController implements Serializable {

    private Carrera seleccionCarrera;

    public void generarReporte() {
        if (seleccionCarrera != null) {
            this.insertarParametro("id_carrera", seleccionCarrera.getId_carrera());
        }
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

}
