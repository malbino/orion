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
import javax.inject.Named;
import org.malbino.orion.entities.Carrera;
import org.malbino.orion.entities.GestionAcademica;
import org.malbino.orion.enums.Nivel;
import org.malbino.orion.enums.Turno;
import org.malbino.orion.facades.GrupoFacade;

/**
 *
 * @author Tincho
 */
@Named("ReporteListaInscritosParaleloController")
@SessionScoped
public class ReporteListaInscritosParaleloController extends AbstractController implements Serializable {

    private GestionAcademica seleccionGestionAcademica;
    private Carrera seleccionCarrera;
    private Nivel seleccionNivel;
    private Turno seleccionTurno;
    private String seleccionParalelo;

    @EJB
    GrupoFacade grupoFacade;

    @PostConstruct
    public void init() {
        seleccionGestionAcademica = null;
        seleccionCarrera = null;
        seleccionNivel = null;
        seleccionParalelo = null;
    }

    public void reinit() {
        seleccionGestionAcademica = null;
        seleccionCarrera = null;
        seleccionNivel = null;
        seleccionParalelo = null;
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

    public void generarReporte() throws IOException {
        if (seleccionGestionAcademica != null && seleccionCarrera != null) {
            this.insertarParametro("id_gestionacademica", seleccionGestionAcademica.getId_gestionacademica());
            this.insertarParametro("id_carrera", seleccionCarrera.getId_carrera());
            this.insertarParametro("nivel", seleccionNivel);
            this.insertarParametro("turno", seleccionTurno);
            this.insertarParametro("paralelo", seleccionParalelo);

            toListaInscritosParalelo();
        }
    }

    public void toReporteListaInscritosParalelo() throws IOException {
        reinit();

        this.redireccionarViewId("/reportes/listaInscritosParalelo/reporteListaInscritosParalelo");
    }

    public void toListaInscritosParalelo() throws IOException {
        this.redireccionarViewId("/reportes/listaInscritosParalelo/listaInscritosParalelo");
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
}
