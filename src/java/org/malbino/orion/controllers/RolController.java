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
import org.malbino.orion.entities.Recurso;
import org.malbino.orion.entities.Rol;
import org.malbino.orion.facades.RecursoFacade;
import org.malbino.orion.facades.RolFacade;

/**
 *
 * @author Tincho
 */
@Named("RolController")
@SessionScoped
public class RolController extends AbstractController implements Serializable {

    @EJB
    RolFacade rolFacade;
    @EJB
    RecursoFacade resursoFacade;

    private List<Rol> roles;
    private Rol nuevoRol;
    private Rol seleccionRol;

    private String keyword;

    @PostConstruct
    public void init() {
        roles = rolFacade.listaRoles();
        nuevoRol = new Rol();
        seleccionRol = null;

        keyword = null;
    }

    public void reinit() {
        roles = rolFacade.listaRoles();
        nuevoRol = new Rol();
        seleccionRol = null;

        keyword = null;
    }

    public void buscar() {
        roles = rolFacade.buscar(keyword);
    }

    public List<Rol> listaRoles() {
        return rolFacade.listaRoles();
    }

    public List<Recurso> listaRecursos() {
        return resursoFacade.listaRecursos();
    }

    public void nuevoRol() throws IOException {
        if (rolFacade.buscarRol(nuevoRol.getNombre()) == null) {
            if (rolFacade.create(nuevoRol)) {
                this.toRoles();
            }
        } else {
            this.mensajeDeError("Rol repetido.");
        }
    };

    public void editarRol() throws IOException {
        if (rolFacade.buscarRol(seleccionRol.getNombre(), seleccionRol.getId_rol()) == null) {
            if (rolFacade.edit(seleccionRol)) {
                this.toRoles();
            }
        } else {
            this.mensajeDeError("Rol repetido.");
        }
    }

    public void toRoles() throws IOException {
        reinit();

        this.redireccionarViewId("/administrador/rol/roles");
    }

    public void toNuevoRol() throws IOException {
        this.redireccionarViewId("/administrador/rol/nuevoRol");
    }

    public void toEditarRol() throws IOException {
        this.redireccionarViewId("/administrador/rol/editarRol");
    }

    /**
     * @return the roles
     */
    public List<Rol> getRoles() {
        return roles;
    }

    /**
     * @param roles the roles to set
     */
    public void setRoles(List<Rol> roles) {
        this.roles = roles;
    }

    /**
     * @return the nuevoRol
     */
    public Rol getNuevoRol() {
        return nuevoRol;
    }

    /**
     * @param nuevoRol the nuevoRol to set
     */
    public void setNuevoRol(Rol nuevoRol) {
        this.nuevoRol = nuevoRol;
    }

    /**
     * @return the seleccionRol
     */
    public Rol getSeleccionRol() {
        return seleccionRol;
    }

    /**
     * @param seleccionRol the seleccionRol to set
     */
    public void setSeleccionRol(Rol seleccionRol) {
        this.seleccionRol = seleccionRol;
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
