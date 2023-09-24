/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.malbino.orion.controllers;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.malbino.orion.entities.Carrera;
import org.malbino.orion.entities.GestionAcademica;
import org.malbino.orion.entities.Grupo;
import org.malbino.orion.entities.Log;
import org.malbino.orion.enums.EventoLog;
import org.malbino.orion.enums.Nivel;
import org.malbino.orion.facades.GrupoFacade;
import org.malbino.orion.util.Fecha;

/**
 *
 * @author Tincho
 */
@Named("ReporteListaInscritosGrupoController")
@SessionScoped
public class ReporteListaInscritosGrupoController extends AbstractController implements Serializable {

    @EJB
    GrupoFacade grupoFacade;
    @Inject
    LoginController loginController;
    
    private GestionAcademica seleccionGestionAcademica;
    private Carrera seleccionCarrera;
    private Nivel seleccionNivel;
    private Grupo seleccionGrupo;

    @PostConstruct
    public void init() {
        seleccionGestionAcademica = null;
        seleccionCarrera = null;
        seleccionNivel = null;
        seleccionGrupo = null;
    }

    public void reinit() {
        seleccionGestionAcademica = null;
        seleccionCarrera = null;
        seleccionNivel = null;
        seleccionGrupo = null;
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
            niveles = Arrays.stream(Nivel.values()).filter(nivel -> nivel.getRegimen().equals(seleccionCarrera.getRegimen())).toArray(Nivel[]::new);
        }
        return niveles;
    }

    public List<Grupo> listaGrupos() {
        List<Grupo> grupos = new ArrayList<>();
        if (seleccionGestionAcademica != null && seleccionCarrera != null && seleccionNivel != null) {
            grupos = grupoFacade.listaGrupos(seleccionGestionAcademica.getId_gestionacademica(), seleccionCarrera.getId_carrera(), seleccionNivel);
        }
        return grupos;
    }

    public void generarReporte() throws IOException {
        if (seleccionGrupo != null) {
            this.insertarParametro("id_grupo", seleccionGrupo.getId_grupo());

            toListaInscritosGrupo();
            
            //log
            logFacade.create(new Log(Fecha.getDate(), EventoLog.READ, "Generación reporte lista inscritos por grupo", loginController.getUsr().toString()));
        }
    }

    public void toReporteListaInscritosGrupo() throws IOException {
        reinit();

        this.redireccionarViewId("/reportes/inscripciones/listaInscritosGrupo/reporteListaInscritosGrupo");
    }

    public void toListaInscritosGrupo() throws IOException {
        this.redireccionarViewId("/reportes/inscripciones/listaInscritosGrupo/listaInscritosGrupo");
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
     * @return the seleccionGrupo
     */
    public Grupo getSeleccionGrupo() {
        return seleccionGrupo;
    }

    /**
     * @param seleccionGrupo the seleccionGrupo to set
     */
    public void setSeleccionGrupo(Grupo seleccionGrupo) {
        this.seleccionGrupo = seleccionGrupo;
    }
}
