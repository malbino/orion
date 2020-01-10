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
import org.malbino.orion.entities.GestionAcademica;
import org.malbino.orion.facades.negocio.CentralizadorCalificacionesFacade;
import org.malbino.orion.facades.negocio.CentralizadorCalificacionesFacade.Centralizador;

/**
 *
 * @author Tincho
 */
@Named("ReporteCentralizadorCalificacionesController")
@SessionScoped
public class ReporteCentralizadorCalificacionesController extends AbstractController implements Serializable {

    @EJB
    CentralizadorCalificacionesFacade centralizadorCalificacionesFacade;

    private GestionAcademica seleccionGestionAcademica;
    private Carrera seleccionCarrera;
    private Integer numeroLibro;

    @PostConstruct
    public void init() {
        seleccionGestionAcademica = null;
        seleccionCarrera = null;
        numeroLibro = null;
    }

    public void reinit() {
        seleccionGestionAcademica = null;
        seleccionCarrera = null;
        numeroLibro = null;
    }

    @Override
    public List<Carrera> listaCarreras() {
        List<Carrera> l = new ArrayList();
        if (seleccionGestionAcademica != null) {
            l = carreraFacade.listaCarreras(seleccionGestionAcademica.getRegimen());
        }
        return l;
    }

    public void generarReporte() throws IOException {
        if (seleccionGestionAcademica != null && seleccionCarrera != null && numeroLibro != null) {
            //generamos el centralizador
            Centralizador centralizador = centralizadorCalificacionesFacade.centralizadorCalificaciones(seleccionGestionAcademica, seleccionCarrera, numeroLibro);
            this.insertarParametro("centralizador", centralizador);

            toCentralizadorCalificaciones();
        }
    }

    public void toReporteCentralizadorCalificaciones() throws IOException {
        reinit();

        this.redireccionarViewId("/reportes/centralizadorCalificaciones/reporteCentralizadorCalificaciones");
    }

    public void toCentralizadorCalificaciones() throws IOException {
        this.redireccionarViewId("/reportes/centralizadorCalificaciones/centralizadorCalificaciones");
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
     * @return the numeroLibro
     */
    public Integer getNumeroLibro() {
        return numeroLibro;
    }

    /**
     * @param numeroLibro the numeroLibro to set
     */
    public void setNumeroLibro(Integer numeroLibro) {
        this.numeroLibro = numeroLibro;
    }

}
