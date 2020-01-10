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
import javax.ejb.EJBException;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.malbino.orion.entities.Carrera;
import org.malbino.orion.entities.Grupo;
import org.malbino.orion.entities.Inscrito;
import org.malbino.orion.entities.Materia;
import org.malbino.orion.entities.Nota;
import org.malbino.orion.enums.Condicion;
import org.malbino.orion.enums.Funcionalidad;
import org.malbino.orion.enums.Modalidad;
import org.malbino.orion.facades.ActividadFacade;
import org.malbino.orion.facades.GrupoFacade;
import org.malbino.orion.facades.InscritoFacade;
import org.malbino.orion.facades.NotaFacade;
import org.malbino.orion.facades.negocio.InscripcionesFacade;
import org.malbino.orion.util.Fecha;

/**
 *
 * @author Tincho
 */
@Named("InscripcionInternetController")
@SessionScoped
public class InscripcionInternetController extends AbstractController implements Serializable {

    @Inject
    LoginController loginController;
    @EJB
    InscritoFacade inscritoFacade;
    @EJB
    InscripcionesFacade inscripcionesFacade;
    @EJB
    GrupoFacade grupoFacade;
    @EJB
    NotaFacade notaFacade;
    @EJB
    ActividadFacade actividadFacade;

    private Carrera seleccionCarrera;
    private Inscrito seleccionInscrito;

    private List<Materia> ofertaMaterias;
    private List<Nota> estadoInscripcion;

    @PostConstruct
    public void init() {
        seleccionCarrera = null;
        seleccionInscrito = null;
        ofertaMaterias = new ArrayList();
        estadoInscripcion = new ArrayList();
    }

    public void reinit() {
        seleccionCarrera = null;
        seleccionInscrito = null;
        ofertaMaterias = new ArrayList();
        estadoInscripcion = new ArrayList();
    }

    @Override
    public List<Carrera> listaCarreras() {
        List<Carrera> l = new ArrayList();
        if (loginController.getUsr() != null) {
            l = carreraFacade.listaCarrerasEstudiante(loginController.getUsr().getId_persona());
        }
        return l;
    }

    public List<Inscrito> listaInscritos() {
        List<Inscrito> l = new ArrayList();
        if (loginController.getUsr() != null) {
            l = inscritoFacade.listaInscritos(loginController.getUsr().getId_persona());
        }
        return l;
    }

    public List<Grupo> listaGruposAbiertos(Materia materia) {
        List<Grupo> l = new ArrayList();
        if (seleccionInscrito != null && materia != null) {
            l = grupoFacade.listaGruposAbiertos(seleccionInscrito.getGestionAcademica().getId_gestionacademica(), seleccionInscrito.getCarrera().getId_carrera(), materia.getId_materia());
        }
        return l;
    }

    public void actualizarOferta() {
        if (seleccionInscrito != null) {
            ofertaMaterias = inscripcionesFacade.ofertaTomaMaterias(seleccionInscrito);
        }
    }

    public void actualizarEstadoInscripcion() {
        if (seleccionInscrito != null) {
            estadoInscripcion = notaFacade.listaNotas(seleccionInscrito.getId_inscrito());
        }
    }

    public boolean verificarGrupos() {
        boolean b = true;
        for (Materia m : ofertaMaterias) {
            if (m.getGrupo() == null) {
                b = false;
                break;
            }
        }
        return b;
    }

    public void tomarMaterias() throws IOException {
        if (!actividadFacade.listaActividades(Fecha.getDate(), Funcionalidad.INSCRIPCION_INTERNET, seleccionInscrito.getGestionAcademica().getId_gestionacademica()).isEmpty()) {
            if (!ofertaMaterias.isEmpty()) {
                if (verificarGrupos()) {
                    List<Nota> aux = new ArrayList();
                    for (Materia materia : ofertaMaterias) {
                        Nota nota = new Nota(0, Modalidad.REGULAR, Condicion.REPROBADO, seleccionInscrito.getGestionAcademica(), materia, seleccionInscrito.getEstudiante(), seleccionInscrito, materia.getGrupo());
                        aux.add(nota);
                    }

                    try {
                        if (inscripcionesFacade.tomarMaterias(aux)) {
                            toEstadoInscripcion();
                        }
                    } catch (EJBException e) {
                        this.mensajeDeError(e.getMessage());
                    }
                } else {
                    this.mensajeDeError("Existen materias sin grupos.");
                }
            } else {
                this.mensajeDeError("No existen materias.");
            }
        } else {
            this.mensajeDeError("Fuera de fecha.");
        }
    }

    public void toEstadoInscripcion() throws IOException {
        this.actualizarEstadoInscripcion();

        this.redireccionarViewId("/estudiante/inscripcionInternet/estadoInscripcion");
    }

    public void toOfertaMaterias() throws IOException {
        actualizarOferta();

        this.redireccionarViewId("/estudiante/inscripcionInternet/ofertaMaterias");
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

    /**
     * @return the ofertaMaterias
     */
    public List<Materia> getOfertaMaterias() {
        return ofertaMaterias;
    }

    /**
     * @param ofertaMaterias the ofertaMaterias to set
     */
    public void setOfertaMaterias(List<Materia> ofertaMaterias) {
        this.ofertaMaterias = ofertaMaterias;
    }

    /**
     * @return the estadoInscripcion
     */
    public List<Nota> getEstadoInscripcion() {
        return estadoInscripcion;
    }

    /**
     * @param estadoInscripcion the estadoInscripcion to set
     */
    public void setEstadoInscripcion(List<Nota> estadoInscripcion) {
        this.estadoInscripcion = estadoInscripcion;
    }

}
