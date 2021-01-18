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
import org.malbino.moodle.webservices.CopiarInscrito;
import org.malbino.orion.entities.Estudiante;
import org.malbino.orion.entities.Grupo;
import org.malbino.orion.entities.Inscrito;
import org.malbino.orion.entities.Materia;
import org.malbino.orion.entities.Nota;
import org.malbino.orion.entities.Pago;
import org.malbino.orion.enums.Condicion;
import org.malbino.orion.enums.Funcionalidad;
import org.malbino.orion.enums.Modalidad;
import org.malbino.orion.facades.ActividadFacade;
import org.malbino.orion.facades.GrupoFacade;
import org.malbino.orion.facades.InscritoFacade;
import org.malbino.orion.facades.NotaFacade;
import org.malbino.orion.facades.PagoFacade;
import org.malbino.orion.facades.negocio.InscripcionesFacade;
import org.malbino.orion.util.Fecha;
import org.malbino.orion.util.Moodle;

/**
 *
 * @author Tincho
 */
@Named("InscripcionManualController")
@SessionScoped
public class InscripcionManualController extends AbstractController implements Serializable {

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
    @EJB
    PagoFacade pagoFacade;

    private Estudiante seleccionEstudiante;
    private Inscrito seleccionInscrito;

    private List<Materia> ofertaMaterias;
    private List<Nota> estadoInscripcion;

    private Nota seleccionNota;

    @PostConstruct
    public void init() {
        seleccionInscrito = null;
        ofertaMaterias = new ArrayList();
        estadoInscripcion = new ArrayList();
    }

    public void reinit() {
        seleccionInscrito = null;
        ofertaMaterias = new ArrayList();
        estadoInscripcion = new ArrayList();
    }

    public List<Inscrito> listaInscritos() {
        List<Inscrito> l = new ArrayList();
        if (seleccionEstudiante != null) {
            l = inscritoFacade.listaInscritos(seleccionEstudiante.getId_persona());
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

    public void copiarInscrito(Estudiante estudiante, List<Nota> notas) {
        String[] properties = Moodle.getProperties();

        String webservice = properties[0];
        String login = properties[1];
        String username = properties[2];
        String password = properties[3];
        String serviceName = properties[4];

        CopiarInscrito copiarInscrito = new CopiarInscrito(login, webservice, username, password, serviceName, estudiante, notas);
        new Thread(copiarInscrito).start();
    }

    public void tomarMaterias() throws IOException {
        if (!actividadFacade.listaActividades(Fecha.getDate(), Funcionalidad.INSCRIPCION, seleccionInscrito.getGestionAcademica().getId_gestionacademica()).isEmpty()) {
            List<Pago> listaPagosPagados = pagoFacade.listaPagosPagados(seleccionInscrito.getId_inscrito());
            if (!listaPagosPagados.isEmpty()) {
                if (!ofertaMaterias.isEmpty()) {
                    if (verificarGrupos()) {
                        List<Nota> aux = new ArrayList();
                        for (Materia materia : ofertaMaterias) {
                            Nota nota = new Nota(0, Modalidad.REGULAR, Condicion.REPROBADO, seleccionInscrito.getGestionAcademica(), materia, seleccionInscrito.getEstudiante(), seleccionInscrito, materia.getGrupo());
                            aux.add(nota);
                        }

                        try {
                            if (inscripcionesFacade.tomarMaterias(aux)) {
                                copiarInscrito(seleccionInscrito.getEstudiante(), aux);

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
                this.mensajeDeError("Matricula/Cuota pendiente.");
            }
        } else {
            this.mensajeDeError("Fuera de fecha.");
        }
    }

    public void retirarMateria() throws IOException {
        List<Nota> listaNotasPrerequisito = notaFacade.listaNotasPrerequisito(seleccionInscrito.getCarrera().getId_carrera(), seleccionInscrito.getEstudiante().getId_persona(), seleccionNota.getMateria().getId_materia());
        if (listaNotasPrerequisito.isEmpty()) {
            if (inscripcionesFacade.retirarMateria(seleccionNota)) {
                toEstadoInscripcion();
            }
        } else {
            this.mensajeDeError("La nota es prerequisito.");
        }
    }

    public void toEstadoInscripcion() throws IOException {
        this.actualizarEstadoInscripcion();

        this.redireccionarViewId("/inscripciones/inscripcionManual/estadoInscripcion");
    }

    public void toOfertaMaterias() throws IOException {
        actualizarOferta();

        this.redireccionarViewId("/inscripciones/inscripcionManual/ofertaMaterias");
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
     * @return the seleccionNota
     */
    public Nota getSeleccionNota() {
        return seleccionNota;
    }

    /**
     * @param seleccionNota the seleccionNota to set
     */
    public void setSeleccionNota(Nota seleccionNota) {
        this.seleccionNota = seleccionNota;
    }

}
