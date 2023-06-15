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
import javax.inject.Inject;
import javax.inject.Named;
import javax.naming.NamingException;
import org.malbino.orion.entities.Log;
import org.malbino.orion.entities.Rol;
import org.malbino.orion.entities.Usuario;
import org.malbino.orion.enums.EntidadLog;
import org.malbino.orion.enums.EventoLog;
import org.malbino.orion.facades.RolFacade;
import org.malbino.orion.util.Encriptador;
import org.malbino.orion.util.Fecha;
import org.malbino.orion.util.Generador;
import org.malbino.orion.util.JavaMail;
import org.malbino.orion.util.PasswordValidator;
import org.malbino.orion.util.Propiedades;
import org.malbino.pfsense.webservices.CopiarUsuario;

/**
 *
 * @author Tincho
 */
@Named("UsuarioController")
@SessionScoped
public class UsuarioController extends AbstractController implements Serializable {

    @EJB
    RolFacade rolFacade;
    @Inject
    LoginController loginController;

    private List<Usuario> usuarios;
    private Usuario seleccionUsuario;
    private boolean restaurarContrasena;

    private Rol seleccionRol;
    private String keyword;

    private String nuevaContrasena;
    private String repitaNuevaContrasena;

    @PostConstruct
    public void init() {
        usuarios = usuarioFacade.listaUsuarios();
        seleccionUsuario = null;
        restaurarContrasena = false;

        seleccionRol = null;
        keyword = null;
    }

    public void reinit() {
        usuarios = usuarioFacade.listaUsuarios();
        seleccionUsuario = null;
        restaurarContrasena = false;

        seleccionRol = null;
        keyword = null;
    }

    public void buscar() {
        if (seleccionRol == null) {
            usuarios = usuarioFacade.buscar(keyword);
        } else {
            usuarios = usuarioFacade.buscar(keyword, seleccionRol.getId_rol());
        }
    }

    public List<Rol> listaRoles() {
        return rolFacade.listaRoles();
    }

    public void copiarUsuario(Usuario usuario) {
        String[] properties = Propiedades.pfsenseProperties();

        String webservice = properties[0];
        String user = properties[1];
        String password = properties[2];

        if (!webservice.isEmpty() && !user.isEmpty() && !password.isEmpty()) {
            CopiarUsuario copiarUsuario = new CopiarUsuario(webservice, user, password, usuario);
            new Thread(copiarUsuario).start();

            //log
            logFacade.create(new Log(Fecha.getDate(), EventoLog.READ, EntidadLog.USUARIO, usuario.getId_persona(), "Copia de usuario a pfSense", loginController.getUsr().toString()));
        }
    }

    public void editarUsuario() throws IOException {
        if (usuarioFacade.buscarPorUsuario(seleccionUsuario.getUsuario(), seleccionUsuario.getId_persona()) == null) {
            if (restaurarContrasena) {
                if (seleccionUsuario.getEmail() != null) {
                    String contrasena = Generador.generarContrasena();
                    seleccionUsuario.setContrasenaSinEncriptar(contrasena);
                    seleccionUsuario.setContrasena(Encriptador.encriptar(contrasena));

                    if (usuarioFacade.edit(seleccionUsuario)) {
                        copiarUsuario(seleccionUsuario);

                        enviarCorreo(seleccionUsuario);

                        //log
                        logFacade.create(new Log(Fecha.getDate(), EventoLog.UPDATE, EntidadLog.USUARIO, seleccionUsuario.getId_persona(), "Actualización por restauración de contraseña por parte del administrador", loginController.getUsr().toString()));

                        this.toUsuarios();
                    }
                } else {
                    this.mensajeDeError("No se puede restaurar la contraseña. El usuario no tiene email.");
                }
            } else {
                if (usuarioFacade.edit(seleccionUsuario)) {
                    //log
                    logFacade.create(new Log(Fecha.getDate(), EventoLog.UPDATE, EntidadLog.USUARIO, seleccionUsuario.getId_persona(), "Actualización usuario", loginController.getUsr().toString()));

                    this.toUsuarios();
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

    public void cambiarContrasena() throws IOException {
        if (nuevaContrasena.equals(repitaNuevaContrasena)) {
            if (PasswordValidator.isValid(nuevaContrasena)) {
                seleccionUsuario.setContrasenaSinEncriptar(nuevaContrasena);
                seleccionUsuario.setContrasena(Encriptador.encriptar(nuevaContrasena));

                if (usuarioFacade.edit(seleccionUsuario)) {
                    copiarUsuario(seleccionUsuario);
                    
                    //log
                    logFacade.create(new Log(Fecha.getDate(), EventoLog.UPDATE, EntidadLog.USUARIO, seleccionUsuario.getId_persona(), "Actualización por cambio de contraseña por parte del administrador", loginController.getUsr().toString()));

                    this.toUsuarios();
                }
            } else {
                this.mensajeDeError("La contraseña debe tener entre 8-20 caracteres. Por lo menos una mayuscula, una minuscula y un dígito.");
            }
        } else {
            this.mensajeDeError("Las nuevas contraseñas no coinciden.");
        }
    }

    public void toEditarUsuario() throws IOException {
        this.redireccionarViewId("/administrador/usuario/editarUsuario");
    }

    public void toCambiarContrasena() throws IOException {
        this.redireccionarViewId("/administrador/usuario/cambiarContrasena");
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
