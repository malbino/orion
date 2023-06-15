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
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.malbino.moodle.webservices.CopiarPostulantes;
import org.malbino.orion.entities.Carrera;
import org.malbino.orion.entities.GestionAcademica;
import org.malbino.orion.entities.Log;
import org.malbino.orion.entities.Postulante;
import org.malbino.orion.enums.EntidadLog;
import org.malbino.orion.enums.EventoLog;
import org.malbino.orion.util.Fecha;
import org.malbino.orion.util.Propiedades;

/**
 *
 * @author Tincho
 */
@Named("AdmisionesController")
@SessionScoped
public class AdmisionesController extends AbstractController implements Serializable {

    @Inject
    LoginController loginController;

    private GestionAcademica seleccionGestionAcademica;
    private Carrera seleccionCarrera;

    private List<Postulante> postulantes;
    private Postulante seleccionPostulante;

    private String keyword;

    @PostConstruct
    public void init() {
        seleccionGestionAcademica = null;
        seleccionCarrera = null;

        postulantes = new ArrayList<>();
        seleccionPostulante = null;

        keyword = null;
    }

    public void reinit() {
        if (seleccionGestionAcademica != null && seleccionCarrera == null) {
            postulantes = postulanteFacade.listaPostulantes(seleccionGestionAcademica.getId_gestionacademica());
        } else if (seleccionGestionAcademica != null && seleccionCarrera != null) {
            postulantes = postulanteFacade.listaPostulantes(seleccionGestionAcademica.getId_gestionacademica(), seleccionCarrera.getId_carrera());
        }
        seleccionPostulante = null;

        keyword = null;
    }

    @Override
    public List<Carrera> listaCarreras() {
        List<Carrera> l = new ArrayList();
        if (seleccionGestionAcademica != null) {
            l = carreraFacade.listaCarreras(seleccionGestionAcademica.getRegimen());
        }
        return l;
    }

    public void buscar() {
        if (seleccionGestionAcademica != null && seleccionCarrera == null) {
            postulantes = postulanteFacade.buscar(seleccionGestionAcademica.getId_gestionacademica(), keyword);
        } else if (seleccionGestionAcademica != null && seleccionCarrera != null) {
            postulantes = postulanteFacade.buscar(seleccionGestionAcademica.getId_gestionacademica(), seleccionCarrera.getId_carrera(), keyword);
        }
    }

    public void imprimirFormularioPostulante() throws IOException {
        if (seleccionPostulante != null) {
            //log
            logFacade.create(new Log(Fecha.getDate(), EventoLog.READ, EntidadLog.POSTULANTE, seleccionPostulante.getId_postulante(), "Impresión formulario postulante", loginController.getUsr().toString()));

            this.insertarParametro("id_postulante", seleccionPostulante.getId_postulante());

            toFormularioPostulante();
        }
    }

    //moolde
    public void copiarPostulantes() {
        if (!postulantes.isEmpty()) {
            String[] properties = Propiedades.moodleProperties();

            String webservice = properties[0];
            String login = properties[1];
            String username = properties[2];
            String password = properties[3];
            String serviceName = properties[4];

            if (!webservice.isEmpty() && !login.isEmpty() && !username.isEmpty() && !password.isEmpty() && !serviceName.isEmpty()) {
                CopiarPostulantes copiarGrupo = new CopiarPostulantes(login, webservice, username, password, serviceName, postulantes);
                new Thread(copiarGrupo).start();

                //log
                logFacade.create(new Log(Fecha.getDate(), EventoLog.READ, "Copia de postulantes a Moodle", loginController.getUsr().toString()));
            }
        } else {
            this.mensajeDeError("Ningun postulante para copiar.");
        }
    }

    public void toFormularioPostulante() throws IOException {
        this.redireccionarViewId("/admisiones/formularioPostulante");
    }

    public void toPostulantes() throws IOException {
        this.redireccionarViewId("/admisiones/postulantes");
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
     * @return the postulantes
     */
    public List<Postulante> getPostulantes() {
        return postulantes;
    }

    /**
     * @param postulantes the postulantes to set
     */
    public void setPostulantes(List<Postulante> postulantes) {
        this.postulantes = postulantes;
    }

    /**
     * @return the seleccionPostulante
     */
    public Postulante getSeleccionPostulante() {
        return seleccionPostulante;
    }

    /**
     * @param seleccionPostulante the seleccionPostulante to set
     */
    public void setSeleccionPostulante(Postulante seleccionPostulante) {
        this.seleccionPostulante = seleccionPostulante;
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
