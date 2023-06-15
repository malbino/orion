/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.malbino.orion.controllers;

import java.io.IOException;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.naming.NamingException;
import org.malbino.orion.entities.Log;
import org.malbino.orion.entities.Usuario;
import org.malbino.orion.enums.EntidadLog;
import org.malbino.orion.enums.EventoLog;
import org.malbino.orion.util.Encriptador;
import org.malbino.orion.util.Fecha;
import org.malbino.orion.util.Generador;
import org.malbino.orion.util.JavaMail;
import org.malbino.orion.util.Propiedades;
import org.malbino.pfsense.webservices.CopiarUsuario;

/**
 *
 * @author Tincho
 */
@Named("RestoreController")
@SessionScoped
public class RestoreController extends AbstractController implements Serializable {
    
    @Inject
    LoginController loginController;
    
    private String usuario;
    private Usuario usr;
    
    public void reinit() {
        usuario = null;
        usr = null;
    }
    
    public void pin() throws IOException {
        usr = usuarioFacade.buscarPorUsuario(usuario);
        if (usr != null) {
            if (usr.getEmail() != null) {
                usr.setPinGenerado(Generador.generarPIN());
                
                enviarPin(usr);
                
                toRestore();
            } else {
                this.mensajeDeError("Usuario sin email.");
            }
        } else {
            this.mensajeDeError("Usuario invalido.");
        }
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
            logFacade.create(new Log(Fecha.getDate(), EventoLog.READ, EntidadLog.USUARIO, usuario.getId_persona(), "Copia de usuario a pfSense", usr.toString()));
        }
    }
    
    public void restore() throws IOException {
        if (usr != null) {
            if (usr.getEmail() != null) {
                if (usr.getPinIntroducido().equals(usr.getPinGenerado())) {
                    String contrasena = Generador.generarContrasena();
                    usr.setContrasenaSinEncriptar(contrasena);
                    usr.setContrasena(Encriptador.encriptar(contrasena));
                    
                    if (usuarioFacade.edit(usr)) {
                        copiarUsuario(usr);
                        
                        enviarContraseña(usr);

                        //log
                        logFacade.create(new Log(Fecha.getDate(), EventoLog.UPDATE, EntidadLog.USUARIO, usr.getId_persona(), "Actualización por restauración de contraseña por parte del usuario", loginController.getUsr().toString()));
                        
                        reinit();
                        
                        toSuccess();
                    }
                } else {
                    this.mensajeDeError("PIN Invalido.");
                }
            } else {
                this.mensajeDeError("Usuario sin email.");
            }
        } else {
            this.mensajeDeError("Usuario invalido.");
        }
    }
    
    public void enviarPin(Usuario usuario) {
        try {
            List<String> to = new ArrayList();
            if (usuario.getEmail() != null && !usuario.getEmail().isEmpty()) {
                to.add(usuario.getEmail());
            }
            
            String html = "Hola " + this.usr.getNombre() + ","
                    + "<br/>"
                    + "<br/>"
                    + "Para restaurar tu contrase&ntilde;a utiliza el siguiente numero PIN."
                    + "<br/>"
                    + "<br/>"
                    + usuario.getPinGenerado()
                    + "<br/>"
                    + "<br/>"
                    + "Atentamente,"
                    + "<br/>"
                    + "Orion";
            
            Runnable mailingController = new JavaMail(to, "PIN Orion", html);
            new Thread(mailingController).start();
        } catch (NamingException ex) {
            Logger.getLogger(RestoreController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void enviarContraseña(Usuario usuario) {
        try {
            List<String> to = new ArrayList();
            if (usuario.getEmail() != null && !usuario.getEmail().isEmpty()) {
                to.add(usuario.getEmail());
            }
            
            String html = "Hola " + this.usr.getNombre() + ","
                    + "<br/>"
                    + "<br/>"
                    + "Para ingresar al sistema utiliza la siguiente contrase&ntilde;a."
                    + "<br/>"
                    + "<br/>"
                    + usuario.getContrasenaSinEncriptar()
                    + "<br/>"
                    + "<br/>"
                    + "Atentamente,"
                    + "<br/>"
                    + "Orion";
            
            Runnable mailingController = new JavaMail(to, "Contraseña Orion", html);
            new Thread(mailingController).start();
        } catch (NamingException ex) {
            Logger.getLogger(RestoreController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void toRestore() throws IOException {
        this.redireccionarViewId("/restore/restore.xhtml");
    }
    
    public void toSuccess() throws IOException {
        this.redireccionarViewId("/restore/success.xhtml");
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
