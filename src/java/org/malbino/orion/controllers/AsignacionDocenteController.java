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
import javax.inject.Inject;
import javax.inject.Named;
import org.malbino.moodle.webservices.CopiarGrupo;
import org.malbino.orion.entities.Carrera;
import org.malbino.orion.entities.GestionAcademica;
import org.malbino.orion.entities.Grupo;
import org.malbino.orion.entities.Log;
import org.malbino.orion.entities.Nota;
import org.malbino.orion.enums.EntidadLog;
import org.malbino.orion.enums.EventoLog;
import org.malbino.orion.enums.Nivel;
import org.malbino.orion.facades.GrupoFacade;
import org.malbino.orion.facades.NotaFacade;
import org.malbino.orion.util.Fecha;
import org.malbino.orion.util.Propiedades;

/**
 *
 * @author Tincho
 */
@Named("AsignacionDocenteController")
@SessionScoped
public class AsignacionDocenteController extends AbstractController implements Serializable {

    @EJB
    GrupoFacade grupoFacade;
    @EJB
    NotaFacade notaFacade;
    @Inject
    LoginController loginController;

    private List<Grupo> grupos;
    private Grupo seleccionGrupo;

    private GestionAcademica seleccionGestionAcademica;
    private Carrera seleccionCarrera;

    private String keyword;

    @PostConstruct
    public void init() {
        grupos = new ArrayList();
        seleccionGrupo = null;

        seleccionGestionAcademica = null;
        seleccionCarrera = null;

        keyword = null;
    }

    public void reinit() {
        if (seleccionGestionAcademica != null && seleccionCarrera != null) {
            grupos = grupoFacade.listaGrupos(seleccionGestionAcademica.getId_gestionacademica(), seleccionCarrera.getId_carrera());
        }
        seleccionGrupo = null;

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

    public Nivel[] listaNiveles() {
        return Nivel.values(seleccionCarrera.getRegimen());
    }

    public void buscar() {
        if (seleccionGestionAcademica != null && seleccionCarrera != null) {
            grupos = grupoFacade.buscar(keyword, seleccionGestionAcademica.getId_gestionacademica(), seleccionCarrera.getId_carrera());
        }
    }

    public long cantidadNotasGrupo(Grupo grupo) {
        return grupoFacade.cantidadNotasGrupo(grupo.getId_grupo());
    }

    public void cambiarDocente() throws IOException {
        if (grupoFacade.edit(seleccionGrupo)) {
            //log
            logFacade.create(new Log(Fecha.getDate(), EventoLog.UPDATE, EntidadLog.GRUPO, seleccionGrupo.getId_grupo(), "Cambio de docente", loginController.getUsr().toString()));

            this.toAsignacionDocente();
        }
    }

    //moolde
    public void copiarGrupo() {
        String[] properties = Propiedades.moodleProperties();

        String webservice = properties[0];
        String login = properties[1];
        String username = properties[2];
        String password = properties[3];
        String serviceName = properties[4];

        if (!webservice.isEmpty() && !login.isEmpty() && !username.isEmpty() && !password.isEmpty() && !serviceName.isEmpty()) {
            Grupo grupo = grupoFacade.find(seleccionGrupo.getId_grupo());
            List<Nota> notas = notaFacade.listaNotasGrupo(grupo.getId_grupo());

            CopiarGrupo copiarGrupo = new CopiarGrupo(login, webservice, username, password, serviceName, grupo, notas);
            new Thread(copiarGrupo).start();

            //log
            logFacade.create(new Log(Fecha.getDate(), EventoLog.READ, "Copia grupo a Moodle", loginController.getUsr().toString()));
        }
    }

    public void toCambiarDocente() throws IOException {
        this.redireccionarViewId("/horarios/asignacionDocente/cambiarDocente");
    }

    public void toAsignacionDocente() throws IOException {
        reinit();

        this.redireccionarViewId("/horarios/asignacionDocente/asignacionDocente");
    }

    /**
     * @return the grupos
     */
    public List<Grupo> getGrupos() {
        return grupos;
    }

    /**
     * @param grupos the grupos to set
     */
    public void setGrupos(List<Grupo> grupos) {
        this.grupos = grupos;
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
