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
import org.malbino.orion.entities.Carrera;
import org.malbino.orion.entities.GestionAcademica;
import org.malbino.orion.entities.Grupo;
import org.malbino.orion.entities.Mencion;
import org.malbino.orion.enums.Nivel;
import org.malbino.orion.enums.Turno;
import org.malbino.orion.facades.GrupoFacade;
import org.malbino.orion.facades.MencionFacade;
import org.malbino.orion.facades.negocio.ProgramacionGruposFacade;

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
    MencionFacade mencionFacade;
    @EJB
    ProgramacionGruposFacade programacionGruposFacade;

    private List<Grupo> grupos;
    private Grupo seleccionGrupo;

    private GestionAcademica seleccionGestionAcademica;
    private Carrera seleccionCarrera;
    private Nivel seleccionNivel;
    private Mencion seleccionMencion;
    private Turno seleccionTurno;
    private Integer capacidad;

    private String keyword;

    @PostConstruct
    public void init() {
        grupos = new ArrayList();
        seleccionGrupo = null;

        seleccionGestionAcademica = null;
        seleccionCarrera = null;
        seleccionNivel = null;
        seleccionMencion = null;
        seleccionTurno = null;
        capacidad = null;

        keyword = null;
    }

    public void reinit() {
        if (seleccionGestionAcademica != null && seleccionCarrera != null) {
            grupos = grupoFacade.listaGrupos(seleccionGestionAcademica.getId_gestionacademica(), seleccionCarrera.getId_carrera());
        }
        seleccionGrupo = null;

        seleccionNivel = null;
        seleccionMencion = null;
        seleccionTurno = null;
        capacidad = null;

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

    public List<Mencion> listaMenciones() {
        return mencionFacade.listaMenciones(seleccionCarrera.getId_carrera());
    }

    public void buscar() {
        if (seleccionGestionAcademica != null && seleccionCarrera != null) {
            grupos = grupoFacade.buscar(keyword, seleccionGestionAcademica.getId_gestionacademica(), seleccionCarrera.getId_carrera());
        }
    }

    public long cantidadNotasGrupo(Grupo grupo) {
        return grupoFacade.cantidadNotasGrupo(grupo.getId_grupo());
    }

    public void programarGrupos() throws IOException {
        if (programacionGruposFacade.programarGrupos(seleccionGestionAcademica, seleccionCarrera, seleccionNivel, seleccionMencion, seleccionTurno, capacidad)) {
            toGrupos();
        }
    }

    public void editarGrupo() throws IOException {
        if (grupoFacade.edit(seleccionGrupo)) {
            this.toGrupos();
        }
    }

    public void eliminarGrupo() throws IOException {
        long cantidadNotasGrupo = grupoFacade.cantidadNotasGrupo(seleccionGrupo.getId_grupo());
        if (cantidadNotasGrupo == 0) {
            if (grupoFacade.remove(seleccionGrupo)) {
                this.toGrupos();
            }
        } else {
            this.mensajeDeError("No se puede eliminar grupos con estudiantes inscritos.");
        }
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
     * @return the seleccionMencion
     */
    public Mencion getSeleccionMencion() {
        return seleccionMencion;
    }

    /**
     * @param seleccionMencion the seleccionMencion to set
     */
    public void setSeleccionMencion(Mencion seleccionMencion) {
        this.seleccionMencion = seleccionMencion;
    }

}
