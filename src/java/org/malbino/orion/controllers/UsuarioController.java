/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.malbino.orion.controllers;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.naming.NamingException;
import org.malbino.orion.entities.Rol;
import org.malbino.orion.entities.Usuario;
import org.malbino.orion.facades.RolFacade;
import org.malbino.orion.facades.UsuarioFacade;
import org.malbino.orion.util.Encriptador;
import org.malbino.orion.util.Generador;
import org.malbino.orion.util.JavaMail;

/**
 *
 * @author Tincho
 */
@Named("UsuarioController")
@SessionScoped
public class UsuarioController extends AbstractController implements Serializable {

    @EJB
    UsuarioFacade usuarioFacade;
    @EJB
    RolFacade rolFacade;

    private List<Usuario> usuarios;
    private Usuario seleccionUsuario;
    private boolean restaurarContrasena;

    private Boolean filter;
    private String keyword;

    @PostConstruct
    public void init() {
        usuarios = usuarioFacade.listaUsuarios();
        seleccionUsuario = null;
        restaurarContrasena = false;

        filter = false;
        keyword = null;
    }

    public void reinit() {
        usuarios = usuarioFacade.listaUsuarios();
        seleccionUsuario = null;
        restaurarContrasena = false;

        filter = false;
        keyword = null;
    }

    public void filtro() {
        if (filter) {
            filter = false;
            keyword = null;

            usuarios = usuarioFacade.listaUsuarios();
        } else {
            filter = true;
            keyword = null;
        }
    }

    public void buscar() {
        usuarios = usuarioFacade.buscar(keyword);
    }

    public List<Rol> listaRoles() {
        return rolFacade.listaRoles();
    }

    public void editarUsuario() throws IOException {
        if (usuarioFacade.buscarPorUsuario(seleccionUsuario.getUsuario(), seleccionUsuario.getId_persona()) == null) {
            if (restaurarContrasena) {
                if (seleccionUsuario.getEmail() != null) {
                    String contrasena = Generador.generarContrasena();
                    seleccionUsuario.setContrasenaSinEncriptar(contrasena);
                    seleccionUsuario.setContrasena(Encriptador.encriptar(contrasena));

                    if (usuarioFacade.edit(seleccionUsuario)) {
                        enviarCorreo(seleccionUsuario);

                        this.toUsuarios();
                    } else {
                        this.mensajeDeError("No se pudo editar el usuario.");
                    }
                } else {
                    this.mensajeDeError("No se puede restaurar la contraseña. El usuario no tiene email.");
                }
            } else {
                if (usuarioFacade.edit(seleccionUsuario)) {
                    this.toUsuarios();
                } else {
                    this.mensajeDeError("No se pudo editar el usuario.");
                }
            }
        } else {
            this.mensajeDeError("Usuario repetido.");
        }
    }

    public void enviarCorreo(Usuario usuario) {
        try {
            List<String> to = new ArrayList();
            if (usuario.getEmail() != null && !usuario.getEmail().isEmpty()) {
                to.add(usuario.getEmail());
            }

            String html = "Hola " + usuario.getNombre() + ","
                    + "<br/>"
                    + "<br/>"
                    + "Para ingresar al sistema utiliza las siguientes credenciales."
                    + "<br/>"
                    + "<br/>"
                    + usuario.getUsuario() + " / " + usuario.getContrasenaSinEncriptar()
                    + "<br/>"
                    + "<br/>"
                    + "Atentamente,"
                    + "<br/>"
                    + "Orion";

            Runnable mailingController = new JavaMail(to, "Credenciales Orion", html);
            new Thread(mailingController).start();
        } catch (NamingException ex) {
            Logger.getLogger(UsuarioController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void toEditarUsuario() throws IOException {
        this.redireccionarViewId("/administrador/usuario/editarUsuario");
    }

    public void toUsuarios() throws IOException {
        reinit();
        
        this.redireccionarViewId("/administrador/usuario/usuarios");
    }

    /**
     * @return the seleccionUsuario
     */
    public Usuario getSeleccionUsuario() {
        return seleccionUsuario;
    }

    /**
     * @param seleccionUsuario the seleccionUsuario to set
     */
    public void setSeleccionUsuario(Usuario seleccionUsuario) {
        this.seleccionUsuario = seleccionUsuario;
    }

    /**
     * @return the reset
     */
    public boolean isRestaurarContrasena() {
        return restaurarContrasena;
    }

    /**
     * @param restaurarContrasena the reset to set
     */
    public void setRestaurarContrasena(boolean restaurarContrasena) {
        this.restaurarContrasena = restaurarContrasena;
    }

    /**
     * @return the usuarios
     */
    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    /**
     * @param usuarios the usuarios to set
     */
    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
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
