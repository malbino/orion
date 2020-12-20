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
import org.malbino.moodle.webservices.CopiarGrupo;
import org.malbino.orion.entities.Carrera;
import org.malbino.orion.entities.GestionAcademica;
import org.malbino.orion.entities.Grupo;
import org.malbino.orion.entities.Instituto;
import org.malbino.orion.entities.Nota;
import org.malbino.orion.enums.Nivel;
import org.malbino.orion.enums.Turno;
import org.malbino.orion.facades.GrupoFacade;
import org.malbino.orion.facades.NotaFacade;
import org.malbino.orion.facades.negocio.ProgramacionGruposFacade;
import org.malbino.orion.util.Moodle;

/**
 *
 * @author Tincho
 */
@Named("GrupoController")
@SessionScoped
public class GrupoController extends AbstractController implements Serializable {

    @EJB
    GrupoFacade grupoFacade;
    @EJB
    ProgramacionGruposFacade programacionGruposFacade;
    @EJB
    NotaFacade notaFacade;

    private List<Grupo> grupos;
    private Grupo seleccionGrupo;

    private GestionAcademica seleccionGestionAcademica;
    private Carrera seleccionCarrera;
    private Nivel seleccionNivel;
    private Turno seleccionTurno;
    private Integer capacidad;

    private Boolean filter;
    private String keyword;

    @PostConstruct
    public void init() {
        grupos = new ArrayList();
        seleccionGrupo = null;

        seleccionGestionAcademica = null;
        seleccionCarrera = null;
        seleccionNivel = null;
        seleccionTurno = null;
        capacidad = null;

        filter = false;
        keyword = null;
    }

    public void reinit() {
        if (seleccionGestionAcademica != null && seleccionCarrera != null) {
            grupos = grupoFacade.listaGrupos(seleccionGestionAcademica.getId_gestionacademica(), seleccionCarrera.getId_carrera());
        }
        seleccionGrupo = null;

        seleccionNivel = null;
        seleccionTurno = null;
        capacidad = null;

        filter = false;
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
        return Arrays.stream(Nivel.values()).filter(nivel -> nivel.getRegimen().equals(seleccionCarrera.getRegimen())).toArray(Nivel[]::new);
    }

    public void filtro() {
        if (filter) {
            filter = false;
            keyword = null;

            if (seleccionGestionAcademica != null && seleccionCarrera.getId_carrera() != null) {
                grupos = grupoFacade.listaGrupos(seleccionGestionAcademica.getId_gestionacademica(), seleccionCarrera.getId_carrera());
            }
        } else {
            filter = true;
            keyword = null;
        }
    }

    public void buscar() {
        if (seleccionGestionAcademica != null && seleccionCarrera != null) {
            grupos = grupoFacade.buscar(keyword, seleccionGestionAcademica.getId_gestionacademica(), seleccionCarrera.getId_carrera());
        }
    }
    
    public long cantidadNotasGrupo(Grupo grupo){
        return grupoFacade.cantidadNotasGrupo(grupo.getId_grupo());
    }

    public void programarGrupos() throws IOException {
        if (programacionGruposFacade.programarGrupos(seleccionGestionAcademica, seleccionCarrera, seleccionNivel, seleccionTurno, capacidad)) {
            toGrupos();
        }
    }

    public void editarGrupo() throws IOException {
        if (grupoFacade.edit(seleccionGrupo)) {
            this.toGrupos();
        }
    }
    
    //moolde
    public void copiarGrupo() {
        String[] properties = Moodle.getProperties();
        
        String webservice = properties[0];
        String login = properties[1];
        String username = properties[2];
        String password = properties[3];
        String serviceName = properties[4];
       
        List<Nota> listaNotasGrupo = notaFacade.listaNotasGrupo(seleccionGrupo.getId_grupo());
        
        CopiarGrupo copiarGrupo = new CopiarGrupo(login, webservice, username, password, serviceName, seleccionGrupo, listaNotasGrupo);
        new Thread(copiarGrupo).start();
    }

    public void toProgramarGrupos() throws IOException {
        this.redireccionarViewId("/gestionesAcademicas/grupo/programarGrupos");
    }

    public void toEditarGrupo() throws IOException {
        this.redireccionarViewId("/gestionesAcademicas/grupo/editarGrupo");
    }

    public void toGrupos() throws IOException {
        reinit();

        this.redireccionarViewId("/gestionesAcademicas/grupo/grupos");
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

    /**
     * @return the capacidad
     */
    public Integer getCapacidad() {
        return capacidad;
    }

    /**
     * @param capacidad the capacidad to set
     */
    public void setCapacidad(Integer capacidad) {
        this.capacidad = capacidad;
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
