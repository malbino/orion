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
import javax.inject.Named;
import org.malbino.orion.entities.Carrera;
import org.malbino.orion.entities.Estudiante;
import org.malbino.orion.entities.Pago;
import org.malbino.orion.facades.PagoFacade;
import org.malbino.orion.facades.negocio.FileEstudianteFacade;

/**
 *
 * @author Tincho
 */
@Named("EstudianteController")
@SessionScoped
public class EstudianteController extends AbstractController implements Serializable {

    @EJB
    FileEstudianteFacade fileEstudianteFacade;
    @EJB
    PagoFacade pagoFacade;

    private List<Estudiante> estudiantes;
    private Estudiante nuevoEstudiante;
    private Estudiante seleccionEstudiante;

    private Boolean filter;
    private Carrera seleccionCarrera;
    private String keyword;

    @PostConstruct
    public void init() {
        estudiantes = estudianteFacade.listaEstudiantes();
        nuevoEstudiante = new Estudiante();
        seleccionEstudiante = null;

        filter = false;
        seleccionCarrera = null;
        keyword = null;
    }

    public void reinit() {
        estudiantes = estudianteFacade.listaEstudiantes();
        nuevoEstudiante = new Estudiante();
        seleccionEstudiante = null;

        filter = false;
        seleccionCarrera = null;
        keyword = null;
    }

    public void filtro() {
        if (filter) {
            filter = false;
            seleccionCarrera = null;
            keyword = null;

            estudiantes = estudianteFacade.listaEstudiantes();
        } else {
            filter = true;
            seleccionCarrera = null;
            keyword = null;
        }
    }

    public void buscar() {
        if (seleccionCarrera == null) {
            estudiantes = estudianteFacade.buscar(keyword);
        } else {
            estudiantes = estudianteFacade.buscar(seleccionCarrera.getId_carrera(), keyword);
        }
    }

    public void crearEstudiante() throws IOException {
        if (estudianteFacade.buscarPorDni(nuevoEstudiante.getDni()) == null) {
            if (fileEstudianteFacade.registrarEstudiante(nuevoEstudiante)) {
                this.toEstudiantes();
            }
        } else {
            this.mensajeDeError("Estudiante repetido.");
        }
    }

    public void editarEstudiante() throws IOException {
        if (estudianteFacade.buscarPorDni(seleccionEstudiante.getDni(), seleccionEstudiante.getId_persona()) == null) {
            if (estudianteFacade.edit(seleccionEstudiante)) {
                this.toEstudiantes();
            }
        } else {
            this.mensajeDeError("Estudiante repetido.");
        }
    }

    public void eliminarEstudiante() throws IOException {
        List<Pago> kardexEconomico = pagoFacade.kardexEconomico(seleccionEstudiante.getId_persona());
        if (kardexEconomico.isEmpty()) {
            if (estudianteFacade.remove(seleccionEstudiante)) {
                this.toEstudiantes();
            }
        } else {
            this.mensajeDeError("No se puede eliminar un estudiante con pagos.");
        }
    }

    public void toNuevoEstudiante() throws IOException {
        this.redireccionarViewId("/fileEstudiante/estudiante/nuevoEstudiante");
    }

    public void toEditarEstudiante() throws IOException {
        this.redireccionarViewId("/fileEstudiante/estudiante/editarEstudiante");
    }

    public void toEstudiantes() throws IOException {
        reinit();

        this.redireccionarViewId("/fileEstudiante/estudiante/estudiantes");
    }

    /**
     * @return the estudiantes
     */
    public List<Estudiante> getEstudiantes() {
        return estudiantes;
    }

    /**
     * @param estudiantes the estudiantes to set
     */
    public void setEstudiantes(List<Estudiante> estudiantes) {
        this.estudiantes = estudiantes;
    }

    /**
     * @return the crearEstudiante
     */
    public Estudiante getNuevoEstudiante() {
        return nuevoEstudiante;
    }

    /**
     * @param nuevoEstudiante the crearEstudiante to set
     */
    public void setNuevoEstudiante(Estudiante nuevoEstudiante) {
        this.nuevoEstudiante = nuevoEstudiante;
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
     * @return the filter
     */
    public Boolean getFilter() {
        return filter;
    }

    /**
     * @param filter the filter to set
     */
    public void setFilter(Boolean filter) {
        this.filter = filter;
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
