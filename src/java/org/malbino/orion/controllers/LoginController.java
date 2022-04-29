/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.malbino.orion.controllers;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import org.malbino.orion.entities.Actividad;
import org.malbino.orion.entities.Recurso;
import org.malbino.orion.entities.Usuario;
import org.malbino.orion.facades.ActividadFacade;
import org.malbino.orion.facades.RecursoFacade;
import org.malbino.orion.facades.UsuarioFacade;
import org.malbino.orion.util.Encriptador;
import org.malbino.orion.util.Fecha;

/**
 *
 * @author malbino
 */
@Named("LoginController")
@SessionScoped
public class LoginController extends AbstractController {

    @EJB
    UsuarioFacade usuarioFacade;
    @EJB
    RecursoFacade recursoFacade;
    @EJB
    ActividadFacade actividadFacade;

    private String usuario;
    private String contrasena;

    private Usuario usr;

    private List<Recurso> listaRecursos;
    private List<Actividad> listaActividadesProximas;

    public void login() throws IOException {
        usr = usuarioFacade.buscarPorUsuario(usuario);
        if (usr != null && usr.getContrasena() != null && Encriptador.comparar(contrasena, usr.getContrasena())) {
            listaRecursos = recursoFacade.buscarPorPersonaNombre(usr.getId_persona());
            listaActividadesProximas = actividadFacade.listaActividadesProximas(Fecha.getInicioDia(Fecha.getDate()));

            toHome();
        } else {
            limpiar();

            mensajeDeError("Autenticación fallida.");
        }
    }

    public String display(String nombre) {
        String s = "none";

        if (usr != null) {
            List<Recurso> l = listaRecursos.stream().filter(r -> r.getNombre().equals(nombre)).collect(Collectors.toList());
            if (!l.isEmpty()) {
                s = "anything";
            }
        }

        return s;
    }

    public void limpiar() {
        usuario = null;
        contrasena = null;
        usr = null;
    }

    public void logout() throws IOException {
        usr = null;
        invalidateSession();

        toLogin();
    }

    public void toOpciones() throws IOException {
        this.redireccionarViewId("/restore/Opciones");
    }

    public void toHome() throws IOException {
        this.redireccionarViewId("/home");
    }

    public void toLogin() throws IOException {
        this.redireccionarViewId("/login");
    }

    /**
     * @return the usuario
     */
    public String getUsuario() {
        return usuario;
    }

    /**
     * @param usuario the usuario to set
     */
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    /**
     * @return the contrasena
     */
    public String getContrasena() {
        return contrasena;
    }

    /**
     * @param contrasena the contrasena to set
     */
    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    /**
     * @return the usr
     */
    public Usuario getUsr() {
        return usr;
    }

    /**
     * @param usr the usr to set
     */
    public void setUsr(Usuario usr) {
        this.usr = usr;
    }

    /**
     * @return the listaRecursos
     */
    public List<Recurso> getListaRecursos() {
        return listaRecursos;
    }

    /**
     * @param listaRecursos the listaRecursos to set
     */
    public void setListaRecursos(List<Recurso> listaRecursos) {
        this.listaRecursos = listaRecursos;
    }

    /**
     * @return the listaActividadesProximas
     */
    public List<Actividad> getListaActividadesProximas() {
        return listaActividadesProximas;
    }

    /**
     * @param listaActividadesProximas the listaActividadesProximas to set
     */
    public void setListaActividadesProximas(List<Actividad> listaActividadesProximas) {
        this.listaActividadesProximas = listaActividadesProximas;
    }
}
