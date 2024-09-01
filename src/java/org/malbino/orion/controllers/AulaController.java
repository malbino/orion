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
import javax.inject.Inject;
import javax.inject.Named;
import org.malbino.orion.entities.Campus;
import org.malbino.orion.entities.Aula;
import org.malbino.orion.entities.Log;
import org.malbino.orion.enums.EntidadLog;
import org.malbino.orion.enums.EventoLog;
import org.malbino.orion.facades.AulaFacade;
import org.malbino.orion.util.Fecha;

/**
 *
 * @author Tincho
 */
@Named("AulaController")
@SessionScoped
public class AulaController extends AbstractController implements Serializable {

    @EJB
    AulaFacade aulaFacade;
    @Inject
    LoginController loginController;

    private List<Aula> aulas;
    private Aula nuevaAula;
    private Aula seleccionAula;
    private Campus seleccionCampus;

    private String keyword;

    @PostConstruct
    public void init() {
        aulas = aulaFacade.listaAulas();
        nuevaAula = new Aula();
        seleccionAula = null;

        keyword = null;
    }

    public void reinit() {
        if (seleccionCampus == null) {
            aulas = aulaFacade.listaAulas();
        } else {
            aulas = aulaFacade.listaAulas(seleccionCampus.getId_campus());
        }
        nuevaAula = new Aula();
        seleccionAula = null;

        keyword = null;
    }

    public void buscar() {
        if (seleccionCampus == null) {
            aulas = aulaFacade.buscar(keyword);
        } else {
            aulas = aulaFacade.buscar(keyword, seleccionCampus.getId_campus());
        }
    }

    public void crearAula() throws IOException {
        if (aulaFacade.buscarPorNombre(nuevaAula.getNombre(), nuevaAula.getCampus().getId_campus()) == null) {
            if (aulaFacade.create(nuevaAula)) {
                //log
                logFacade.create(new Log(Fecha.getDate(), EventoLog.CREATE, EntidadLog.CARRERA, nuevaAula.getId_aula(), "Creación aula", loginController.getUsr().toString()));

                this.toAulas();
            }
        } else {
            this.mensajeDeError("Aula repetida.");
        }
    }

    public void editarAula() throws IOException {
        if (aulaFacade.buscarPorNombre(seleccionAula.getNombre(), seleccionAula.getId_aula(), seleccionAula.getCampus().getId_campus()) == null) {
            if (aulaFacade.edit(seleccionAula)) {
                //log
                logFacade.create(new Log(Fecha.getDate(), EventoLog.UPDATE, EntidadLog.CARRERA, seleccionAula.getId_aula(), "Actualización aula", loginController.getUsr().toString()));

                this.toAulas();
            }
        } else {
            this.mensajeDeError("Aula repetida.");
        }
    }

    public void toNuevaAula() throws IOException {
        this.redireccionarViewId("/horarios/aula/nuevaAula");
    }

    public void toEditarAula() throws IOException {
        this.redireccionarViewId("/horarios/aula/editarAula");
    }

    public void toAulas() throws IOException {
        reinit();

        this.redireccionarViewId("/horarios/aula/aulas");
    }

    /**
     * @return the aulas
     */
    public List<Aula> getAulas() {
        return aulas;
    }

    /**
     * @param aulas the aulas to set
     */
    public void setAulas(List<Aula> aulas) {
        this.aulas = aulas;
    }

    /**
     * @return the nuevaAula
     */
    public Aula getNuevaAula() {
        return nuevaAula;
    }

    /**
     * @param nuevaAula the nuevaAula to set
     */
    public void setNuevaAula(Aula nuevaAula) {
        this.nuevaAula = nuevaAula;
    }

    /**
     * @return the seleccionAula
     */
    public Aula getSeleccionAula() {
        return seleccionAula;
    }

    /**
     * @param seleccionAula the seleccionAula to set
     */
    public void setSeleccionAula(Aula seleccionAula) {
        this.seleccionAula = seleccionAula;
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
