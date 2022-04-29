/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.malbino.orion.controllers;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.malbino.orion.entities.Estudiante;

/**
 *
 * @author Tincho
 */
@Named("FileEstudianteController")
@SessionScoped
public class FileEstudianteController extends AbstractController implements Serializable {

    @Inject
    LoginController loginController;

    private Estudiante estudiante;

    @PostConstruct
    public void init() {
        if (loginController.getUsr() != null) {
            estudiante = estudianteFacade.find(loginController.getUsr().getId_persona());
        }
    }

    public void reinit() {
        if (loginController.getUsr() != null) {
            estudiante = estudianteFacade.find(loginController.getUsr().getId_persona());
        }
    }

    public void editarFileEstudiante() {
        if (estudianteFacade.edit(estudiante)) {
            this.mensajeDeInformacion("Guardado.");
        }
    }

    /**
     * @return the estudiante
     */
    public Estudiante getEstudiante() {
        return estudiante;
    }

    /**
     * @param estudiante the estudiante to set
     */
    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
    }
}
