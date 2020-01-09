/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.malbino.orion.controllers;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import org.malbino.orion.entities.Carrera;
import org.malbino.orion.entities.Estudiante;

/**
 *
 * @author Tincho
 */
@Named("ReporteHistorialAcademicoController")
@SessionScoped
public class ReporteHistorialAcademicoController extends AbstractController implements Serializable {

    private Estudiante seleccionEstudiante;
    private Carrera seleccionCarrera;
    private Date fecha;

    @PostConstruct
    public void init() {
        seleccionEstudiante = null;
        seleccionCarrera = null;
        fecha = null;
    }

    public void reinit() {
        seleccionEstudiante = null;
        seleccionCarrera = null;
        fecha = null;
    }

    @Override
    public List<Carrera> listaCarreras() {
        List<Carrera> l = new ArrayList();
        if (seleccionEstudiante != null) {
            l = carreraFacade.listaCarrerasEstudiante(seleccionEstudiante.getId_persona());
        }
        return l;
    }

    public void generarReporte() throws IOException {
        if (seleccionEstudiante != null && seleccionCarrera != null && fecha != null) {
            this.insertarParametro("id_persona", seleccionEstudiante.getId_persona());
            this.insertarParametro("id_carrera", seleccionCarrera.getId_carrera());
            this.insertarParametro("fecha", fecha);

            toHistorialAcademico();
        }
    }

    public void toReporteHistorialAcademico() throws IOException {
        reinit();
        
        this.redireccionarViewId("/reportes/historialAcademico/reporteHistorialAcademico");
    }

    public void toHistorialAcademico() throws IOException {
        this.redireccionarViewId("/reportes/historialAcademico/historialAcademico");
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
     * @return the fecha
     */
    public Date getFecha() {
        return fecha;
    }

    /**
     * @param fecha the fecha to set
     */
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

}
