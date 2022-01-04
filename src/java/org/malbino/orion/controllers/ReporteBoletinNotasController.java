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
import org.malbino.orion.entities.Estudiante;
import org.malbino.orion.entities.Inscrito;
import org.malbino.orion.facades.InscritoFacade;

/**
 *
 * @author Tincho
 */
@Named("ReporteBoletinNotasController")
@SessionScoped
public class ReporteBoletinNotasController extends AbstractController implements Serializable {

    @EJB
    InscritoFacade inscritoFacade;

    private Estudiante seleccionEstudiante;
    private Inscrito seleccionInscrito;
    
    public List<Inscrito> listaInscritos() {
        List<Inscrito> l = new ArrayList();
        if (seleccionEstudiante != null) {
            l = inscritoFacade.listaInscritosPersona(seleccionEstudiante.getId_persona());
        }
        return l;
    }

    @PostConstruct
    public void init() {
        seleccionEstudiante = null;
        seleccionInscrito = null;
    }

    public void reinit() {
        seleccionEstudiante = null;
        seleccionInscrito = null;
    }

    public void generar() throws IOException {
        if (seleccionInscrito != null) {
            this.insertarParametro("id_inscrito", seleccionInscrito.getId_inscrito());

            toBoletinNotas();
        }
    }

    public void toReporteBoletinNotas() throws IOException {
        reinit();

        this.redireccionarViewId("/reportes/boletinNotas/reporteBoletinNotas");
    }

    public void toBoletinNotas() throws IOException {
        this.redireccionarViewId("/reportes/boletinNotas/boletinNotas");
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
     * @return the seleccionInscrito
     */
    public Inscrito getSeleccionInscrito() {
        return seleccionInscrito;
    }

    /**
     * @param seleccionInscrito the seleccionInscrito to set
     */
    public void setSeleccionInscrito(Inscrito seleccionInscrito) {
        this.seleccionInscrito = seleccionInscrito;
    }

}
