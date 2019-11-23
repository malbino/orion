/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.malbino.orion.controllers;

import java.io.IOException;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import org.malbino.orion.entities.Usuario;
import org.malbino.orion.facades.UsuarioFacade;
import org.malbino.orion.util.Encriptador;

/**
 *
 * @author malbino
 */
@Named("LoginController")
@SessionScoped
public class LoginController extends Controller {

    @EJB
    UsuarioFacade usuarioFacade;

    private String usuario;
    private String contrasena;

    private Usuario usr;

    public void login() throws IOException {
        usr = usuarioFacade.buscarPorUsuario(getUsuario());
        if (usr != null) {
            if (Encriptador.comparar(contrasena, usr.getContrasena())) {
                toHome();
            } else {
                limpiar();

                mensajeDeError("Contraseña invalida.");
            }
        } else {
            limpiar();

            mensajeDeError("Usuario invalido.");
        }
    }

    public void logout() throws IOException {
        usr = null;
        invalidateSession();

        toLogin();
    }

    public void limpiar() {
        usuario = null;
        contrasena = null;
        usr = null;
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
}
