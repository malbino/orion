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
import org.malbino.orion.entities.Log;
import org.malbino.orion.entities.Recurso;
import org.malbino.orion.entities.Usuario;
import org.malbino.orion.enums.EntidadLog;
import org.malbino.orion.enums.EventoLog;
import org.malbino.orion.facades.ActividadFacade;
import org.malbino.orion.facades.RecursoFacade;
import org.malbino.orion.util.Encriptador;
import org.malbino.orion.util.Fecha;
import org.malbino.orion.util.PasswordValidator;

/**
 *
 * @author malbino
 */
@Named("LoginController")
@SessionScoped
public class LoginController extends AbstractController {

    @EJB
    RecursoFacade recursoFacade;
    @EJB
    ActividadFacade actividadFacade;

    private String usuario;
    private String contrasena;

    private Usuario usr;

    private List<Recurso> listaRecursos;
    private List<Actividad> listaActividadesProximas;
    private List<Actividad> listaActividadesVigentes;

    private String contrasenaActual;
    private String nuevaContrasena;
    private String repitaNuevaContrasena;

    public void login() throws IOException {
        usr = usuarioFacade.buscarPorUsuario(usuario);
        if (usr != null && usr.getContrasena() != null && Encriptador.comparar(contrasena, usr.getContrasena())) {
            listaRecursos = recursoFacade.buscarPorPersonaNombre(usr.getId_persona());
            listaActividadesProximas = actividadFacade.listaActividadesProximas(Fecha.getInicioDia(Fecha.getDate()));
            listaActividadesVigentes = actividadFacade.listaActividadesVigentes();

            //log
            logFacade.create(new Log(Fecha.getDate(), EventoLog.READ, EntidadLog.USUARIO, usr.getId_persona(), "Login usuario", usr.toString()));
            
            toHome();
        } else {
            limpiar();

            mensajeDeError("Autenticación fallida.");
        }
    }

    public String displayMenu(String path) {
        String s = "none";

        if (usr != null) {
            List<Recurso> l = listaRecursos.stream().filter(r -> r.getUrlPattern().startsWith(path)).collect(Collectors.toList());
            if (!l.isEmpty()) {
                s = "anything";
            }
        }

        return s;
    }

    public String display(String path) {
        String s = "none";

        if (usr != null) {
            List<Recurso> l = listaRecursos.stream().filter(r -> r.getUrlPattern().equals(path)).collect(Collectors.toList());
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
        //log
        logFacade.create(new Log(Fecha.getDate(), EventoLog.READ, EntidadLog.USUARIO, usr.getId_persona(), "Logout usuario", usr.toString()));

        usr = null;
        invalidateSession();

        toLogin();
    }

    public void cambiarContrasena() throws IOException {
        if (Encriptador.comparar(contrasenaActual, usr.getContrasena())) {
            if (nuevaContrasena.equals(repitaNuevaContrasena)) {
                if (PasswordValidator.isValid(nuevaContrasena)) {
                    usr.setContrasena(Encriptador.encriptar(nuevaContrasena));

                    if (usuarioFacade.edit(usr)) {
                        //log
                        logFacade.create(new Log(Fecha.getDate(), EventoLog.UPDATE, EntidadLog.USUARIO, usr.getId_persona(), "Actualización por cambio de contraseña por parte del usuario", usr.toString()));

                        
                        this.toHome();
                    }
                } else {
                    this.mensajeDeError("La contraseña debe tener entre 8-20 caracteres. Por lo menos una mayuscula, una minuscula y un dígito.");
                }
            } else {
                this.mensajeDeError("Las nuevas contraseñas no coinciden.");
            }
        } else {
            this.mensajeDeError("Contraseña actual incorrecta.");
        }
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

    /**
     * @return the listaActividadesVigentes
     */
    public List<Actividad> getListaActividadesVigentes() {
        return listaActividadesVigentes;
    }

    /**
     * @param listaActividadesVigentes the listaActividadesVigentes to set
     */
    public void setListaActividadesVigentes(List<Actividad> listaActividadesVigentes) {
        this.listaActividadesVigentes = listaActividadesVigentes;
    }

    /**
     * @return the contrasenaActual
     */
    public String getContrasenaActual() {
        return contrasenaActual;
    }

    /**
     * @param contrasenaActual the contrasenaActual to set
     */
    public void setContrasenaActual(String contrasenaActual) {
        this.contrasenaActual = contrasenaActual;
    }

    /**
     * @return the nuevaContrasena
     */
    public String getNuevaContrasena() {
        return nuevaContrasena;
    }

    /**
     * @param nuevaContrasena the nuevaContrasena to set
     */
    public void setNuevaContrasena(String nuevaContrasena) {
        this.nuevaContrasena = nuevaContrasena;
    }

    /**
     * @return the repitaNuevaContrasena
     */
    public String getRepitaNuevaContrasena() {
        return repitaNuevaContrasena;
    }

    /**
     * @param repitaNuevaContrasena the repitaNuevaContrasena to set
     */
    public void setRepitaNuevaContrasena(String repitaNuevaContrasena) {
        this.repitaNuevaContrasena = repitaNuevaContrasena;
    }
}
