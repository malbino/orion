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
import org.malbino.orion.entities.CarreraEstudiante;
import org.malbino.orion.entities.Estudiante;
import org.malbino.orion.entities.Mencion;
import org.malbino.orion.facades.CarreraEstudianteFacade;
import org.malbino.orion.facades.MencionFacade;
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
    @EJB
    MencionFacade mencionFacade;
    @EJB
    CarreraEstudianteFacade carreraEstudianteFacade;

    private List<Estudiante> estudiantes;
    private Estudiante nuevoEstudiante;
    private Estudiante seleccionEstudiante;

    private Boolean filter;
    private Carrera seleccionCarrera;
    private String keyword;

    private List<CarreraEstudiante> carrerasEstudiante;
    private List<CarreraEstudiante> seleccionCarrerasEstudiante;

    @PostConstruct
    public void init() {
        estudiantes = estudianteFacade.listaEstudiantes();
        nuevoEstudiante = new Estudiante();
        seleccionEstudiante = null;

        filter = false;
        seleccionCarrera = null;
        keyword = null;

        carrerasEstudiante = new ArrayList<>();
        seleccionCarrerasEstudiante = new ArrayList<>();
    }

    public void reinit() {
        estudiantes = estudianteFacade.listaEstudiantes();
        nuevoEstudiante = new Estudiante();
        seleccionEstudiante = null;

        filter = false;
        seleccionCarrera = null;
        keyword = null;

        carrerasEstudiante = new ArrayList<>();
        seleccionCarrerasEstudiante = new ArrayList<>();
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

    public List<CarreraEstudiante> listaCarrerasEstudiante_NuevoEstudiante() {
        List<CarreraEstudiante> l = new ArrayList<>();
        List<Carrera> carreras = carreraFacade.listaCarreras();
        for (Carrera carrera : carreras) {

            List<Mencion> menciones = mencionFacade.listaMenciones(carrera.getId_carrera());
            if (menciones.isEmpty()) {
                CarreraEstudiante.CarreraEstudianteId carreraEstudianteId = new CarreraEstudiante.CarreraEstudianteId();
                carreraEstudianteId.setId_carrera(carrera.getId_carrera());
                carreraEstudianteId.setId_persona(0);
                CarreraEstudiante carreraEstudiante = new CarreraEstudiante();
                carreraEstudiante.setCarreraEstudianteId(carreraEstudianteId);
                carreraEstudiante.setCarrera(carrera);

                l.add(carreraEstudiante);
            } else {
                for (Mencion mencion : menciones) {
                    CarreraEstudiante.CarreraEstudianteId carreraEstudianteId = new CarreraEstudiante.CarreraEstudianteId();
                    carreraEstudianteId.setId_carrera(carrera.getId_carrera());
                    carreraEstudianteId.setId_persona(0);
                    CarreraEstudiante carreraEstudiante = new CarreraEstudiante();
                    carreraEstudiante.setCarreraEstudianteId(carreraEstudianteId);
                    carreraEstudiante.setMencion(mencion);
                    carreraEstudiante.setCarrera(carrera);

                    l.add(carreraEstudiante);
                }
            }
        }
        return l;
    }

    public List<CarreraEstudiante> listaCarrerasEstudiante_EditarEstudiante() {
        List<CarreraEstudiante> l = new ArrayList<>();
        List<Carrera> carreras = carreraFacade.listaCarreras();
        for (Carrera carrera : carreras) {
            List<Mencion> menciones = mencionFacade.listaMenciones(carrera.getId_carrera());
            if (menciones.isEmpty()) {
                CarreraEstudiante.CarreraEstudianteId carreraEstudianteId = new CarreraEstudiante.CarreraEstudianteId();
                carreraEstudianteId.setId_carrera(carrera.getId_carrera());
                carreraEstudianteId.setId_persona(seleccionEstudiante.getId_persona());
                CarreraEstudiante carreraEstudiante = new CarreraEstudiante();
                carreraEstudiante.setCarreraEstudianteId(carreraEstudianteId);
                carreraEstudiante.setCarrera(carrera);

                l.add(carreraEstudiante);
            } else {
                for (Mencion mencion : menciones) {
                    CarreraEstudiante.CarreraEstudianteId carreraEstudianteId = new CarreraEstudiante.CarreraEstudianteId();
                    carreraEstudianteId.setId_carrera(carrera.getId_carrera());
                    carreraEstudianteId.setId_persona(seleccionEstudiante.getId_persona());
                    CarreraEstudiante carreraEstudiante = new CarreraEstudiante();
                    carreraEstudiante.setCarreraEstudianteId(carreraEstudianteId);
                    carreraEstudiante.setMencion(mencion);
                    carreraEstudiante.setCarrera(carrera);

                    l.add(carreraEstudiante);
                }
            }
        }
        return l;
    }

    public String carrerasEstudianteToString(Estudiante estudiante) {
        String s = " ";
        List<CarreraEstudiante> carrerasEstudiante = carreraEstudianteFacade.listaCarrerasEstudiante(estudiante.getId_persona());
        for (CarreraEstudiante carreraEstudiante : carrerasEstudiante) {
            if (s.compareTo(" ") == 0) {
                s = carreraEstudiante.getCarrera().getCodigo();
            } else {
                s += ", " + carreraEstudiante.getCarrera().getCodigo();
            }
        }
        return s;
    }

    public void crearEstudiante() throws IOException {
        if (estudianteFacade.buscarPorDni(nuevoEstudiante.getDni()) == null) {
            if (fileEstudianteFacade.registrarEstudiante(nuevoEstudiante, seleccionCarrerasEstudiante)) {
                this.toEstudiantes();
            }
        } else {
            this.mensajeDeError("Estudiante repetido.");
        }
    }

    public void editarEstudiante() throws IOException {
        if (estudianteFacade.buscarPorDni(seleccionEstudiante.getDni(), seleccionEstudiante.getId_persona()) == null) {
            if (fileEstudianteFacade.editarEstudiante(seleccionEstudiante, seleccionCarrerasEstudiante)) {
                this.toEstudiantes();
            }
        } else {
            this.mensajeDeError("Estudiante repetido.");
        }
    }

    public void eliminarEstudiante() throws IOException {
        if (estudianteFacade.remove(seleccionEstudiante)) {
            this.toEstudiantes();
        }
    }

    public void toNuevoEstudiante() throws IOException {
        carrerasEstudiante = listaCarrerasEstudiante_NuevoEstudiante();
        seleccionCarrerasEstudiante = new ArrayList<>();

        this.redireccionarViewId("/fileEstudiante/estudiante/nuevoEstudiante");
    }

    public void toEditarEstudiante() throws IOException {
        carrerasEstudiante = listaCarrerasEstudiante_EditarEstudiante();
        seleccionCarrerasEstudiante = carreraEstudianteFacade.listaCarrerasEstudiante(seleccionEstudiante.getId_persona());

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

    /**
     * @return the carrerasEstudiante
     */
    public List<CarreraEstudiante> getCarrerasEstudiante() {
        return carrerasEstudiante;
    }

    /**
     * @param carrerasEstudiante the carrerasEstudiante to set
     */
    public void setCarrerasEstudiante(List<CarreraEstudiante> carrerasEstudiante) {
        this.carrerasEstudiante = carrerasEstudiante;
    }

    /**
     * @return the seleccionCarrerasEstudiante
     */
    public List<CarreraEstudiante> getSeleccionCarrerasEstudiante() {
        return seleccionCarrerasEstudiante;
    }

    /**
     * @param seleccionCarrerasEstudiante the seleccionCarrerasEstudiante to set
     */
    public void setSeleccionCarrerasEstudiante(List<CarreraEstudiante> seleccionCarrerasEstudiante) {
        this.seleccionCarrerasEstudiante = seleccionCarrerasEstudiante;
    }
}
