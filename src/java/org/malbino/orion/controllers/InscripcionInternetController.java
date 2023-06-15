/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.malbino.orion.controllers;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.malbino.moodle.webservices.CopiarInscrito;
import org.malbino.orion.entities.CarreraEstudiante;
import org.malbino.orion.entities.Estudiante;
import org.malbino.orion.entities.Grupo;
import org.malbino.orion.entities.Inscrito;
import org.malbino.orion.entities.Log;
import org.malbino.orion.entities.Materia;
import org.malbino.orion.entities.Nota;
import org.malbino.orion.entities.Pago;
import org.malbino.orion.enums.Condicion;
import org.malbino.orion.enums.EntidadLog;
import org.malbino.orion.enums.EventoLog;
import org.malbino.orion.enums.Funcionalidad;
import org.malbino.orion.enums.Modalidad;
import org.malbino.orion.facades.ActividadFacade;
import org.malbino.orion.facades.CarreraEstudianteFacade;
import org.malbino.orion.facades.GrupoFacade;
import org.malbino.orion.facades.InscritoFacade;
import org.malbino.orion.facades.NotaFacade;
import org.malbino.orion.facades.PagoFacade;
import org.malbino.orion.facades.negocio.InscripcionesFacade;
import org.malbino.orion.util.Constantes;
import org.malbino.orion.util.Fecha;
import org.malbino.orion.util.Propiedades;

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
    @EJB
    PagoFacade pagoFacade;
    @EJB
    CarreraEstudianteFacade carreraEstudianteFacade;

    private CarreraEstudiante seleccionCarreraEstudiante;
    private Inscrito seleccionInscrito;

    private List<Materia> ofertaMaterias;
    private List<Nota> estadoInscripcion;

    private String[] grupos = Arrays.copyOfRange(Constantes.ABECEDARIO, 0, 6);
    private String grupo;

    @PostConstruct
    public void init() {
        seleccionCarreraEstudiante = null;
        seleccionInscrito = null;
        ofertaMaterias = new ArrayList();
        estadoInscripcion = new ArrayList();

        grupo = grupos[0];
    }

    public void reinit() {
        seleccionCarreraEstudiante = null;
        seleccionInscrito = null;
        ofertaMaterias = new ArrayList();
        estadoInscripcion = new ArrayList();

        grupo = grupos[0];
    }

    public List<CarreraEstudiante> listaCarrerasEstudiante() {
        List<CarreraEstudiante> l = new ArrayList();
        if (loginController.getUsr() != null) {
            l = carreraEstudianteFacade.listaCarrerasEstudiante(loginController.getUsr().getId_persona());
        }
        return l;
    }

    public List<Inscrito> listaInscritos() {
        List<Inscrito> l = new ArrayList();
        if (loginController.getUsr() != null && seleccionCarreraEstudiante != null) {
            l = inscritoFacade.listaInscritosPorEstudianteCarrera(loginController.getUsr().getId_persona(), seleccionCarreraEstudiante.getCarrera().getId_carrera());
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

            for (Materia materia : ofertaMaterias) {
                List<Grupo> listaGruposAbiertos = grupoFacade.listaGruposAbiertos(seleccionInscrito.getGestionAcademica().getId_gestionacademica(), materia.getId_materia(), grupo);
                Iterator<Grupo> iterator = listaGruposAbiertos.iterator();
                if (iterator.hasNext()) {
                    materia.setGrupo(iterator.next());
                } else {
                    materia.setGrupo(null);
                }
            }
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
        String[] properties = Propiedades.moodleProperties();

        String webservice = properties[0];
        String login = properties[1];
        String username = properties[2];
        String password = properties[3];
        String serviceName = properties[4];

        if (!webservice.isEmpty() && !login.isEmpty() && !username.isEmpty() && !password.isEmpty() && !serviceName.isEmpty()) {
            CopiarInscrito copiarInscrito = new CopiarInscrito(login, webservice, username, password, serviceName, estudiante, notas);
            new Thread(copiarInscrito).start();

            //log
            logFacade.create(new Log(Fecha.getDate(), EventoLog.READ, EntidadLog.ESTUDIANTE, estudiante.getId_persona(), "Copia de inscrito a Moodle", loginController.getUsr().toString()));
        }
    }

    public void tomarMaterias() throws IOException {
        if (!actividadFacade.listaActividades(Fecha.getDate(), Funcionalidad.INSCRIPCION_INTERNET, seleccionInscrito.getGestionAcademica().getId_gestionacademica()).isEmpty()) {
            List<Pago> listaPagosPagados = pagoFacade.listaPagosPagados(seleccionInscrito.getId_inscrito());
            if (!listaPagosPagados.isEmpty()) {
                if (!ofertaMaterias.isEmpty()) {
                    if (verificarGrupos()) {
                        List<Nota> aux = new ArrayList();
                        for (Materia materia : ofertaMaterias) {
                            Nota nota = new Nota(0, Modalidad.REGULAR, Condicion.ABANDONO, seleccionInscrito.getGestionAcademica(), materia, seleccionInscrito.getEstudiante(), seleccionInscrito, materia.getGrupo());
                            aux.add(nota);
                        }

                        try {
                            if (inscripcionesFacade.tomarMaterias(aux)) {
                                copiarInscrito(seleccionInscrito.getEstudiante(), aux);

                                //log
                                for (Nota nota : aux) {
                                    //log
                                    logFacade.create(new Log(Fecha.getDate(), EventoLog.CREATE, EntidadLog.NOTA, nota.getId_nota(), "Creación de nota por toma de materias por internet", loginController.getUsr().toString()));
                                }

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

    public void toEstadoInscripcion() throws IOException {
        this.actualizarEstadoInscripcion();

        this.redireccionarViewId("/estudiante/inscripcionInternet/estadoInscripcion");
    }

    public void toOfertaMaterias() throws IOException {
        actualizarOferta();

        this.redireccionarViewId("/estudiante/inscripcionInternet/ofertaMaterias");
    }

    /**
     * @return the seleccionCarreraEstudiante
     */
    public CarreraEstudiante getSeleccionCarreraEstudiante() {
        return seleccionCarreraEstudiante;
    }

    /**
     * @param seleccionCarreraEstudiante the seleccionCarreraEstudiante to set
     */
    public void setSeleccionCarreraEstudiante(CarreraEstudiante seleccionCarreraEstudiante) {
        this.seleccionCarreraEstudiante = seleccionCarreraEstudiante;
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
     * @return the grupos
     */
    public String[] getGrupos() {
        return grupos;
    }

    /**
     * @param grupos the grupos to set
     */
    public void setGrupos(String[] grupos) {
        this.grupos = grupos;
    }

    /**
     * @return the grupo
     */
    public String getGrupo() {
        return grupo;
    }

    /**
     * @param grupo the grupo to set
     */
    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

}
